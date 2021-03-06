package uk.gov.dft.bluebadge.service.applicationmanagement.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@Slf4j
public class S3Config {
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
  AmazonS3 amazonS3() {
    return AmazonS3ClientBuilder.defaultClient();
  }
}
