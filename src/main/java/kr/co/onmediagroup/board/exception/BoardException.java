package kr.co.onmediagroup.board.exception;

import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {
  public BoardException(String message) { super(message); }

  public static class NoBoard extends BoardException {
    public NoBoard() { super("no board data"); }
  }
}
