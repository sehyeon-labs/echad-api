package kr.co.onmediagroup.board.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.onmediagroup.board.model.dto.Board;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BoardEntity {

  @Id
  @Size(max = 100)
  @Column(name = "board_id")
  private String boardId;

  @NotBlank
  @Size(max = 100)
  @Column(name = "user_id")
  private String userId;

  @NotNull
  @Column(name = "template_id")
  private Integer templateId;

  @NotBlank
  @Setter
  @Column(name = "board_schema", columnDefinition = "json")
  private String boardSchema;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "active_yn")
  private Board.ActiveYn activeYn = Board.ActiveYn.Y;

  @Column(name = "created_at", updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private LocalDateTime updatedAt;
}
