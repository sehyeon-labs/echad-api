package kr.co.onmediagroup.template.exception;

import lombok.Getter;

@Getter
public class PostException extends RuntimeException {
  public PostException(String message) { super(message); }

  public static class NoPost extends PostException {
    public NoPost() { super("no post data"); }
  }

  public static class AlreadyExistPost extends PostException {
    public AlreadyExistPost() { super("already exist post"); }
  }

  public static class UnauthorizedPostAccess extends PostException {
    public UnauthorizedPostAccess() { super("Post without permission"); }
  }
}
