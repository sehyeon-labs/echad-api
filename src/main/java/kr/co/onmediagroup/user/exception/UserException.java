package kr.co.onmediagroup.user.exception;

public class UserException extends RuntimeException{
  public UserException(String message) { super(message); }

  public static class AlreadyExistUserId extends UserException{
    public AlreadyExistUserId() { super("already exist user id"); }
  }

  public static class AlreadyExistUserEmail extends UserException{
    public AlreadyExistUserEmail() { super("already exist user email"); }
  }

  public static class UserNotFound extends UserException{
    public UserNotFound() { super("user not found"); }
  }

  public static class InvalidCurrentPassword extends UserException{
    public InvalidCurrentPassword() { super("invalid current password"); }
  }

  public static class NewPasswordNotMatch extends UserException{
    public NewPasswordNotMatch() { super("new password not match"); }
  }

  public static class SameAsCurrentPassword extends UserException{
    public SameAsCurrentPassword() { super("same as current password"); }
  }

  public static class EmailSendFailed extends UserException{
    public EmailSendFailed() { super("email send failed"); }
  }
}
