package kr.co.onmediagroup.user.exception;

import lombok.Getter;

/**
 * 로그인 관련 예외 처리
 * */
@Getter
public class LoginException extends RuntimeException{
  private final Integer loginFailCount;

  public LoginException(String message) {
    super(message);
    loginFailCount = null;
  }

  public LoginException(String message, Integer loginFailCount) {
    super(message);
    this.loginFailCount = loginFailCount;
  }

  // 없는 유저 로그인 시도
  public static class NoUser extends LoginException {
    public NoUser() { super("no user"); }
  }

  // 로그인 실패 횟수 초과
  public static class TooManyFailedLogin extends LoginException {
    public TooManyFailedLogin(Integer loginFailCount) {
      super("too many failed login", loginFailCount);
    }
  }

  // 비활성화 계정 로그인 시도
  public static class DeactivatedUser extends LoginException {
    public DeactivatedUser(Integer loginFailCount) {
      super("deactivated user", loginFailCount);
    }
  }

  // 비밀번호 틀림
  public static class InvalidPassword extends LoginException {
    public InvalidPassword(Integer loginFailCount) {
      super("invalid password", loginFailCount);
    }
  }
}
