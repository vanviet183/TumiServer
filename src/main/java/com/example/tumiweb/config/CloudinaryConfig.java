package com.example.tumiweb.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

  @Bean
  public Cloudinary config() {
    Map<String, String> config = new HashMap<>();
    config.put("cloud_name", "dlqdesqni");
    config.put("api_key", "355535952451761");
    config.put("api_secret", "8jymQqkrM5HHSZMtW-yIMcAPK78");
    return new Cloudinary(config);
  }

}