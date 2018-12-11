package uk.gov.service.bluebadge.test.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;
import java.net.URISyntaxException;

public class S3Utils {

  private AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
  private String bucket;

  public S3Utils(String bucket) {
    this.bucket = bucket;
  }

  public String putFile(String fileName, String s3key) throws URISyntaxException {
    File f = new File(this.getClass().getResource(fileName).toURI());
    if (!s3.doesObjectExist(bucket, s3key)) {
      s3.putObject(bucket, s3key, f);
    }
    return s3key;
  }

  public boolean fileExists(String key){
    return s3.doesObjectExist(bucket, key);
  }
}
