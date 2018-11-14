package uk.gov.dft.bluebadge.service.applicationmanagement.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class S3Config {
  @Value("${amazon.profile:default}")
  @NotNull
  private String profile;

  @Value("${amazon.s3bucket}")
  @NotNull
  private String s3Bucket;

  @Value("${amazon.thumbnail-height-px:300}")
  @NotNull
  private Integer thumbnailHeight;

  @Value("${amazon.signed-url-duration-ms:5000}")
  @NotNull
  private Integer signedUrlDurationMs;

  @Bean
  public AmazonS3 amazonS3() {
    return AmazonS3ClientBuilder.standard()
        .withCredentials(new ProfileCredentialsProvider(profile))
        .build();
  }
}
