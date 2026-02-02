package kr.co.onmediagroup.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import kr.co.onmediagroup.config.JWTConfig;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTUtil {
  private final JWTConfig jwtConfig;

  /**
   * JWT 셍성
   * @param user
   * @return JWT String
   */
  public String createToken(UserEntity user) {
    int expiredSeconds = jwtConfig.getExpiredSeconds();
    Date expiredDate = Date.from(
      ZonedDateTime.now()
        .plusSeconds(expiredSeconds) // 현재 시각 + N초
        .toInstant()); // Instant로 변환 (UTC 기반)
    Algorithm algorithm = jwtConfig.getAlgorithm();

    return JWT.create()
      .withSubject(user.getUserId()) // 사용자 식별자
      .withClaim("userLevel", user.getUserLevel().name()) // 권한
      .withClaim("activeYn", user.getActiveYn().name()) // 활성 상태
      .withClaim("type", "ACCESS")
      .withExpiresAt(expiredDate) // 만료 시간
      .sign(algorithm); // 비밀키 알고리즘으로 서명
  }

  /**
   * JWT 유효성 검증
   * @param token 클라이언트로부터 전달받은 JWT
   * @return 유효하면 true, 유효하지 않으면 false
   */
  public boolean verifyToken(String token) {
    try {
      JWT.require(jwtConfig.getAlgorithm())
        .build()
        .verify(token);
      return true;
    } catch (JWTVerificationException e) {
      log.warn("JWT 토큰 검증 에러: {}", e.getMessage());
      return false;
    } catch (Exception e) {
      log.warn("기타 토큰 검증 에러: {}", e.getMessage());
      return false;
    }
  }

  /**
   * JWT 사용자 정보 추출
   * @param token
   * @return User.UserPrincipal
   */
  public User.UserPrincipal verifyTokenWithUserPrincipal(String token) {
    try {
      DecodedJWT decodedJWT = JWT.require(jwtConfig.getAlgorithm())
        .build()
        .verify(token);

      String userId = decodedJWT.getSubject();
      String userLevel = decodedJWT.getClaim("userLevel").asString();
      String activeYn = decodedJWT.getClaim("activeYn").asString();

      return User.UserPrincipal.builder()
        .userId(userId)
        .userLevel(User.Level.valueOf(userLevel))
        .activeYn(User.ActiveYn.valueOf(activeYn))
        .build();
    } catch (JWTVerificationException e) {
      log.warn("JWT 토큰 검증 실패: {}", e.getMessage());
      return null;
    } catch (Exception e) {
      log.error("JWT 사용자 정보 추출 중 에러", e);
      return null;
    }
  }

}
