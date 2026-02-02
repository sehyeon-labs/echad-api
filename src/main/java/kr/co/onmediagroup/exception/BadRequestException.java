package kr.co.onmediagroup.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@NoArgsConstructor
public class BadRequestException extends RuntimeException{
  private String message;

  public BadRequestException(String message) {
    super(message);
    this.message = message;
  }
}

