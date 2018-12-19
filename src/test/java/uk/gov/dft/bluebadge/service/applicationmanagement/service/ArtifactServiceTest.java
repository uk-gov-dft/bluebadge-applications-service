package uk.gov.dft.bluebadge.service.applicationmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3;
import com.google.common.collect.ImmutableList;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.model.applicationmanagement.generated.Artifact;
import uk.gov.dft.bluebadge.service.applicationmanagement.config.S3Config;
import uk.gov.dft.bluebadge.service.applicationmanagement.repository.domain.ArtifactEntity;

public class ArtifactServiceTest {
  private static final String VALID_S3_URL_1 =
      "https://test-bucket.s3.eu-west-2.amazonaws.com/test/key.jpg";
  private static final String VALID_S3_URL_2 =
      "https://test-bucket.s3.eu-west-2.amazonaws.com/test2/key2.jpg";
  private static final String DEST_BUCKET = "dest-bucket";

  private ArtifactService artifactService;

  @Mock AmazonS3 amazonS3Mock;
  private List<Artifact> testArifacts;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    S3Config config = new S3Config();

    config.setS3Bucket(DEST_BUCKET);
    config.setThumbnailHeight(300);
    config.setSignedUrlDurationMs(1000);

    artifactService = new ArtifactService(amazonS3Mock, config);

    Artifact artifact1 = new Artifact();
    artifact1.type(Artifact.TypeEnum.PROOF_ADD).link(VALID_S3_URL_1);
    Artifact artifact2 = new Artifact();
    artifact2.type(Artifact.TypeEnum.PROOF_ELIG).link(VALID_S3_URL_2);
    testArifacts = ImmutableList.of(artifact1, artifact2);
  }

  @Test
  public void saveArtifacts() {
    when(amazonS3Mock.doesObjectExist(any(), any())).thenReturn(true);
    UUID applicationId = UUID.randomUUID();
    List<ArtifactEntity> result = artifactService.saveArtifacts(testArifacts, applicationId);

    String destKey1 = applicationId.toString() + "/test/key.jpg";
    verify(amazonS3Mock).copyObject("test-bucket", "test/key.jpg", DEST_BUCKET, destKey1);
    String destKey2 = applicationId.toString() + "/test2/key2.jpg";
    verify(amazonS3Mock).copyObject("test-bucket", "test2/key2.jpg", DEST_BUCKET, destKey2);

    assertThat(result).hasSize(2);
    assertThat(result).extracting("applicationId").containsOnly(applicationId);
    assertThat(result)
        .extracting("type", "link")
        .containsOnly(
            tuple(Artifact.TypeEnum.PROOF_ADD.name(), destKey1),
            tuple(Artifact.TypeEnum.PROOF_ELIG.name(), destKey2));
  }

  @Test
  public void saveArtifacts_nullArtifacts() {
    List<ArtifactEntity> artifactEntities = artifactService.saveArtifacts(null, UUID.randomUUID());
    assertThat(artifactEntities).isEmpty();
  }

  @Test
  public void saveArtifacts_emptyArtifacts() {
    List<ArtifactEntity> artifactEntities =
        artifactService.saveArtifacts(Collections.emptyList(), UUID.randomUUID());
    assertThat(artifactEntities).isEmpty();
  }

  @Test
  public void saveArtifacts_invalidS3Link_noBucket() {
    UUID applicationId = UUID.randomUUID();
    Artifact badArtifact = new Artifact();
    badArtifact.type(Artifact.TypeEnum.PROOF_ID).link("https://s3.eu-west-2.amazonaws.com/");
    testArifacts = ImmutableList.of(badArtifact);

    try {
      artifactService.saveArtifacts(testArifacts, applicationId);
      fail("no exception thrown");
    } catch (BadRequestException e) {
      @SuppressWarnings("ConstantConditions")
      Error error = e.getResponse().getBody().getError();
      assertThat(error.getMessage()).startsWith("Failed to extract S3 object bucket from url");
      assertThat(error.getMessage()).endsWith("https://s3.eu-west-2.amazonaws.com/");
    }

    verify(amazonS3Mock, never()).copyObject(any(), any(), any(), any());
  }

  @Test
  public void saveArtifacts_invalidS3Link_noKey() {
    UUID applicationId = UUID.randomUUID();
    Artifact badArtifact = new Artifact();
    badArtifact
        .type(Artifact.TypeEnum.PROOF_ID)
        .link("https://test-bucket1.s3.eu-west-2.amazonaws.com");
    testArifacts = ImmutableList.of(badArtifact);

    try {
      artifactService.saveArtifacts(testArifacts, applicationId);
      fail("no exception thrown");
    } catch (BadRequestException e) {
      Error error = e.getResponse().getBody().getError();
      assertThat(error.getMessage()).startsWith("Failed to extract S3 object key from url");
      assertThat(error.getMessage()).endsWith("https://test-bucket1.s3.eu-west-2.amazonaws.com");
    }

    verify(amazonS3Mock, never()).copyObject(any(), any(), any(), any());
  }

  @Test
  public void saveArtifacts_invalidS3Link_notFoundWithinS3() {
    when(amazonS3Mock.doesObjectExist("test-bucket-x", "key-x")).thenReturn(false);

    UUID applicationId = UUID.randomUUID();
    Artifact badArtifact = new Artifact();
    badArtifact
        .type(Artifact.TypeEnum.PROOF_ID)
        .link("https://test-bucket-x.s3.eu-west-2.amazonaws.com/key-x");
    testArifacts = ImmutableList.of(badArtifact);

    try {
      artifactService.saveArtifacts(testArifacts, applicationId);
      fail("no exception thrown");
    } catch (BadRequestException e) {
      Error error = e.getResponse().getBody().getError();
      assertThat(error.getMessage()).startsWith("Artifact does not exist within S3");
      assertThat(error.getMessage())
          .endsWith("https://test-bucket-x.s3.eu-west-2.amazonaws.com/key-x");
    }

    verify(amazonS3Mock, never()).copyObject(any(), any(), any(), any());
  }

  @Test
  public void saveArtifacts_whenOneBadLink_thenNoLinkSaved() {
    when(amazonS3Mock.doesObjectExist("test-bucket-x", "key-x")).thenReturn(false);

    UUID applicationId = UUID.randomUUID();
    Artifact badArtifact = new Artifact();
    badArtifact
        .type(Artifact.TypeEnum.PROOF_ID)
        .link("https://test-bucket-x.s3.eu-west-2.amazonaws.com/key-x");
    ImmutableList<Artifact> artifacts =
        ImmutableList.<Artifact>builder().addAll(testArifacts).add(badArtifact).build();

    try {
      artifactService.saveArtifacts(artifacts, applicationId);
      fail("no exception thrown");
    } catch (BadRequestException e) {
    }

    verify(amazonS3Mock, never()).copyObject(any(), any(), any(), any());
  }

  @Test
  public void backOutArtifacts() {
    List<ArtifactEntity> artifacts = getArtifactEntities();

    when(amazonS3Mock.doesObjectExist(DEST_BUCKET, "artifact/link1")).thenReturn(true);
    when(amazonS3Mock.doesObjectExist(DEST_BUCKET, "artifact/link2")).thenReturn(true);
    artifactService.backOutArtifacts(artifacts);
    verify(amazonS3Mock).deleteObject(DEST_BUCKET, "artifact/link1");
    verify(amazonS3Mock).deleteObject(DEST_BUCKET, "artifact/link2");
  }

  private List<ArtifactEntity> getArtifactEntities() {
    ArtifactEntity artifactEntity1 =
        ArtifactEntity.builder()
            .applicationId(UUID.randomUUID())
            .type("x")
            .link("artifact/link1")
            .build();
    ArtifactEntity artifactEntity2 =
        ArtifactEntity.builder()
            .applicationId(UUID.randomUUID())
            .type("x")
            .link("artifact/link2")
            .build();
    return ImmutableList.of(artifactEntity1, artifactEntity2);
  }

  @Test
  public void createAccessibleLinks() throws Exception {
    when(amazonS3Mock.generatePresignedUrl(any()))
        .thenReturn(new URL("http://art1"), new URL("http://art2"));
    List<ArtifactEntity> artifactEntities = getArtifactEntities();
    List<Artifact> accessibleLinks = artifactService.createAccessibleLinks(artifactEntities);

    assertThat(accessibleLinks).hasSize(2);
    assertThat(accessibleLinks).extracting("link").containsOnly("http://art1", "http://art2");
  }

  @Test
  public void createAccessibleLinks_nullArtifactEntities() {
    List<Artifact> accessibleLinks = artifactService.createAccessibleLinks(null);
    assertThat(accessibleLinks).isEmpty();
    verify(amazonS3Mock, never()).generatePresignedUrl(any());
  }

  @Test
  public void createAccessibleLinks_emptyArtifactEntities() {
    List<Artifact> accessibleLinks = artifactService.createAccessibleLinks(null);
    assertThat(accessibleLinks).isEmpty();
    verify(amazonS3Mock, never()).generatePresignedUrl(any());
  }
}
