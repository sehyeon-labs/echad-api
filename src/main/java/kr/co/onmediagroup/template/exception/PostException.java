package kr.co.onmediagroup.template.exception;

import lombok.Getter;

@Getter
public class PostException extends RuntimeException {
  public PostException(String message) { super(message); }

  public static class NoTemplate extends PostException {
    public NoTemplate() { super("no template data"); }
  }

  public static class AlreadyExistTemplate extends PostException {
    public AlreadyExistTemplate() { super("already exist template"); }
  }
}
