package kr.co.onmediagroup.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.onmediagroup.util.JacksonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
  @Bean
  public ObjectMapper objectMapper() {
    return JacksonUtils.createObjectMapper();
  }
}

