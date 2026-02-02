package kr.co.onmediagroup.exception;

import lombok.Getter;

/**
 * 인증 에외처리
 * */
@Getter
public class AuthException extends RuntimeException{
  public AuthException(String message) { super(message); }

  // 비정상 토큰
  public static class InvalidAccessToken extends AuthException {
    public InvalidAccessToken() { super("invalid access token"); }
  }

  // 인증 오류
  public static class UnauthorizedMe extends AuthException{
    public UnauthorizedMe() { super("unauthorized"); }
  }
}
