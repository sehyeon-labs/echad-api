package kr.co.onmediagroup.config;

import kr.co.onmediagroup.util.AuditorAwareImple;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 설정 클래스
 *
 * 엔티티 생성자 및 수정자 정보를 자동으로 주입하기 위해 AuditorAware 구현체를 등록
 * @CreatedBy 등
 *
 * 작성자 정보를 제공하는 구현체
 * */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JPAConfig {

  @Bean
  AuditorAware<String> auditorProvider() { return new AuditorAwareImple(); }
}
