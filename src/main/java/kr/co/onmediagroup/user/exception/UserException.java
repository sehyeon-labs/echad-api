package kr.co.onmediagroup.user.exception;

public class UserException extends RuntimeException{
  public UserException(String message) { super(message); }

  public static class AlreadyExistUserId extends UserException{
    public AlreadyExistUserId() { super("already exist user id"); }
  }
}
