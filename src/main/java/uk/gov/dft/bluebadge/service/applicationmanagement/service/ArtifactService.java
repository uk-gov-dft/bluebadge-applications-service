package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.InternalServerException;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact;
import uk.gov.dft.bluebadge.service.applicationmanagement.config.S3Config;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ArtifactEntity;

@Slf4j
@Service
@Transactional
public class ArtifactService {

  private static final String S3_NOT_FOUND_ERR_MSG =
      "Artifact does not exist within S3. Extracted bucket:%s, key:%s, from link:%s";
  private static final String ARTIFACT_LINK_ERR_FIELD = "artifact.link";

  private final AmazonS3 amazonS3;
  private final S3Config s3Config;

  @Autowired
  ArtifactService(AmazonS3 amazonS3, S3Config s3Config) {
    this.amazonS3 = amazonS3;
    this.s3Config = s3Config;
  }

  public List<ArtifactEntity> saveArtifacts(List<Artifact> artifacts, final UUID applicationId) {
    Assert.notNull(applicationId, "The application ID must be set.");
    if (null == artifacts || artifacts.isEmpty()) {
      return Collections.emptyList();
    }

    return artifacts
        .stream()
        .peek(a -> checkURL(a.getLink()))
        .map(a -> saveS3Artifact(a, applicationId))
        .collect(Collectors.toList());
  }

  private ArtifactEntity saveS3Artifact(Artifact artifact, UUID applicationId) {
    log.debug("Saving S3 artifact. link:{}", artifact.getLink());
    AmazonS3URI amazonS3URI = new AmazonS3URI(artifact.getLink(), false);
    String artifactKey = applicationId.toString() + "/" + amazonS3URI.getKey();
    String bucketName = getBucketName();
    log.debug("Copying from bucket {} to bucket {}", amazonS3URI.getBucket(), bucketName);
    amazonS3.copyObject(amazonS3URI.getBucket(), amazonS3URI.getKey(), bucketName, artifactKey);
    return ArtifactEntity.builder()
        .link(artifactKey)
        .applicationId(applicationId)
        .type(artifact.getType().name())
        .build();
  }

  private String getBucketName() {
    String bucketName = s3Config.getS3Bucket();
    if (!amazonS3.doesBucketExistV2(bucketName)) {
      log.debug("Bucket does not exist, so creating. name:{}", bucketName);
      CreateBucketRequest bucketRequest = new CreateBucketRequest(bucketName);
      amazonS3.createBucket(bucketRequest);
    }
    return s3Config.getS3Bucket();
  }

  private void checkURL(String url) {
    AmazonS3URI amazonS3URI;
    try {
      amazonS3URI = new AmazonS3URI(url, false);
    } catch (Exception e) {
      log.info("Failed to extract S3 URI. Link:{}", url, e);
      Error error =
          new Error()
              .message("Failed to extract S3 bucket and key from url: " + url)
              .reason(ARTIFACT_LINK_ERR_FIELD);
      throw new BadRequestException(error);
    }
    if (null == amazonS3URI.getBucket()) {
      Error error =
          new Error()
              .message("Failed to extract S3 object bucket from url: " + url)
              .reason(ARTIFACT_LINK_ERR_FIELD);
      throw new BadRequestException(error);
    }
    if (null == amazonS3URI.getKey()) {
      Error error =
          new Error()
              .message("Failed to extract S3 object key from url: " + url)
              .reason(ARTIFACT_LINK_ERR_FIELD);
      throw new BadRequestException(error);
    }
    if (!amazonS3.doesObjectExist(amazonS3URI.getBucket(), amazonS3URI.getKey())) {
      String message =
          String.format(S3_NOT_FOUND_ERR_MSG, amazonS3URI.getBucket(), amazonS3URI.getKey(), url);
      log.info(message);
      Error error = new Error().message(message).reason(ARTIFACT_LINK_ERR_FIELD);
      throw new BadRequestException(error);
    }
  }

  public void backOutArtifacts(List<ArtifactEntity> artifactEntities) {
    artifactEntities.forEach(a -> amazonS3.deleteObject(s3Config.getS3Bucket(), a.getLink()));
  }

  public List<Artifact> createAccessibleLinks(List<ArtifactEntity> artifacts) {
    if (null == artifacts) {
      return Collections.emptyList();
    }

    return artifacts
        .stream()
        .map(
            a -> {
              String signedUrl = generateSignedS3Url(a.getLink());
              return new Artifact().link(signedUrl).type(Artifact.TypeEnum.fromValue(a.getType()));
            })
        .collect(Collectors.toList());
  }

  private String generateSignedS3Url(String link) {
    if (null == link) {
      return null;
    }

    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += s3Config.getSignedUrlDurationMs();
    expiration.setTime(expTimeMillis);

    // Generate the pre-signed URL with expiry.
    try {
      GeneratePresignedUrlRequest generatePresignedUrlRequest =
          new GeneratePresignedUrlRequest(s3Config.getS3Bucket(), link)
              .withMethod(HttpMethod.GET)
              .withExpiration(expiration);
      URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
      return url.toString();
    } catch (AmazonServiceException e) {
      throw handleSdkClientException(
          e, "Generate signed url for image failed, s3 storage could not process request.");
    } catch (SdkClientException e) {
      throw handleSdkClientException(
          e, "Generate signed url for image failed, s3 storage could not be contacted.");
    }
  }

  private InternalServerException handleSdkClientException(SdkClientException e, String message) {
    // Amazon S3 couldn't be contacted for a response, or the client
    // couldn't parse the response from Amazon S3.
    log.error(message, e);
    Error error = new Error();
    error.setMessage(message);
    return new InternalServerException(error);
  }
}
