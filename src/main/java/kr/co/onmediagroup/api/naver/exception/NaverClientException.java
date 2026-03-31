package kr.co.onmediagroup.api.naver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class NaverClientException extends RuntimeException {
    public NaverClientException(String message) {
        super(message);
    }

    public NaverClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
