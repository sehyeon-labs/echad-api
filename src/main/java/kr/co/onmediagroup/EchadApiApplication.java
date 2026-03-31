package kr.co.onmediagroup;

import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EchadApiApplication {
  public static void main(String[] args) {
    SpringApplication springApplication = new SpringApplication(EchadApiApplication.class);
    springApplication.addListeners(new ApplicationPidFileWriter());
    springApplication.run(args);
  }

  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient();
  }
}
