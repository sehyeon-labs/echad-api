package kr.co.onmediagroup.template.exception;

import lombok.Getter;

@Getter
public class TemplateException extends RuntimeException {
  public TemplateException(String message) { super(message); }

  public static class NoTemplate extends TemplateException {
    public NoTemplate() { super("no template data"); }
  }

  public static class AlreadyExistTemplate extends TemplateException {
    public AlreadyExistTemplate() { super("already exist template"); }
  }
}
