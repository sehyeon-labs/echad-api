package kr.co.onmediagroup.template.exception;

import lombok.Getter;

@Getter
public class PostBlockException extends RuntimeException {
    public PostBlockException(String message) { super(message); }

    public static class NoPostBlock extends PostBlockException {
        public NoPostBlock() { super("no post block data"); }
    }
}
