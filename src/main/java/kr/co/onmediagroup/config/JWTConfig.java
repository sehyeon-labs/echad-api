package kr.co.onmediagroup.config;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * JWT 관련 설정을 구성하는 클래스
 *
 * 서명키와 토큰 만료 시간을 외부 설정 파일에서 주입받아 관리
 * JWT 서명에 사용할 HMAC256 알고리즘을 초기화
 *
 * 설정 항목;
 * auth.jwt.signKey : JWT 서명을 위한 비밀키
 * auth.jwt.expiredSeconds : JWT 토큰의 유효 기간 (초)
 *
 * */
@Configuration
@Getter
public class JWTConfig {
  private final int expiredSeconds;
  private final Algorithm algorithm;

  public JWTConfig(@org.springframework.beans.factory.annotation.Value("${auth.jwt.signKey}") String signKey,
                   @Value("${auth.jwt.expiredSeconds}") int expiredSeconds) {

    this.expiredSeconds = expiredSeconds;
    this.algorithm = Algorithm.HMAC256(signKey.getBytes());
  }

  public int getExpiredSeconds() {
    return expiredSeconds;
  }

  public Algorithm getAlgorithm() {
    return algorithm;
  }
}
