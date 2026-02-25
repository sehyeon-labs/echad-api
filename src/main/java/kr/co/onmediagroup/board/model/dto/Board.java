package kr.co.onmediagroup.board.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

public class Board {

  @Getter
  @AllArgsConstructor
  public enum ActiveYn {
    Y("Y"),
    N("N");
    private final String activeYn;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BoardRes {
    private String boardId;
    private String userId;
    private Integer templateId;
    private String boardSchema;
    private ActiveYn activeYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
  }

  public record BoardUpdateVO(
    @NotBlank String boardSchema
  ) {
  }
}
