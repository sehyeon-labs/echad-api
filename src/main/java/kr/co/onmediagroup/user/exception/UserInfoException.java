package kr.co.onmediagroup.user.exception;

public class UserInfoException extends RuntimeException {
  public UserInfoException(String message) { super(message); }

  public static class NoUserInfo extends UserInfoException{
    public NoUserInfo() { super("no user info data"); }
  }
}
