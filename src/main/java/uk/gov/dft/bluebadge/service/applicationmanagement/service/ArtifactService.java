package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact;
import uk.gov.dft.bluebadge.service.applicationmanagement.config.S3Config;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ArtifactEntity;

@Slf4j
@Service
@Transactional
public class ArtifactService {

  private final AmazonS3 amazonS3;
  private final S3Config s3Config;

  @Autowired
  ArtifactService(AmazonS3 amazonS3, S3Config s3Config) {
    this.amazonS3 = amazonS3;
    this.s3Config = s3Config;
  }

  public List<ArtifactEntity> saveArtifacts(List<Artifact> artifacts, final UUID applicationId) {
    Assert.notNull(applicationId, "The application ID must be set.");
    Assert.notNull(artifacts, "The artifact collection is null.");

    return artifacts
        .stream()
        .map(a -> saveS3Artifact(a, applicationId))
        .collect(Collectors.toList());
  }

  private ArtifactEntity saveS3Artifact(Artifact artifact, UUID applicationId) {
    AmazonS3URI amazonS3URI = processS3URL(artifact.getLink());
    String aritfactKey = applicationId.toString() + "/" + amazonS3URI.getKey();
    amazonS3.copyObject(
        amazonS3URI.getBucket(), amazonS3URI.getKey(), s3Config.getS3Bucket(), aritfactKey);
    return ArtifactEntity.builder()
        .link(aritfactKey)
        .applicationId(applicationId)
        .type(artifact.getType().name())
        .build();
  }

  private AmazonS3URI processS3URL(String url) {
    AmazonS3URI amazonS3URI = new AmazonS3URI(url);
    if (null == amazonS3URI.getBucket()) {
      throw new IllegalArgumentException("Failed to extract S3 object bucket from url: " + url);
    }
    if (null == amazonS3URI.getKey()) {
      throw new IllegalArgumentException("Failed to extract S3 object key from url: " + url);
    }
    if (!amazonS3.doesObjectExist(amazonS3URI.getBucket(), amazonS3URI.getKey())) {
      throw new IllegalArgumentException(
          "Object does not exist within S3. Extracted bucket: "
              + amazonS3URI.getBucket()
              + ", key:"
              + amazonS3URI.getKey()
              + ", from link:"
              + url);
    }
    return amazonS3URI;
  }

  public void backOutArtifacts(List<ArtifactEntity> artifactEntities) {
    artifactEntities.forEach(a -> amazonS3.deleteObject(s3Config.getS3Bucket(), a.getLink()));
  }
}
