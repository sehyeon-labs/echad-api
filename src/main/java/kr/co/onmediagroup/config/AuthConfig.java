package kr.co.onmediagroup.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 인증 관련 설정 클래스
 *
 * application.yml에 정의된 로그인 실패 허용 횟수(`auth.max-login-fail-count`)를 주입받아 관리
 * 전역에서 해당 값을 주입받아 로그인 실패 처리 등에 활용
 *
 * */
@Configuration
@Getter
public class AuthConfig {
  private final Integer maxLoginFailCount;

  public AuthConfig(@Value("${auth.max-login-fail-count}") Integer maxLoginFailCount) {
    this.maxLoginFailCount = maxLoginFailCount;
  }
}
