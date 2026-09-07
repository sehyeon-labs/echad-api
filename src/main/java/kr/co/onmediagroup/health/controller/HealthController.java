package kr.co.onmediagroup.health.controller;

import kr.co.onmediagroup.config.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/health")
public class HealthController {

  @GetMapping
  @Description("헬스체크")
  @ResponseStatus(HttpStatus.OK)
  public HealthResponse health() {
    return new HealthResponse("UP", LocalDateTime.now());
  }

  public record HealthResponse(String status, LocalDateTime timestamp) {}
}
