package kr.co.onmediagroup.user.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.co.onmediagroup.user.model.dto.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_info", indexes = {
  @Index(name = "uq__info_id__user_id", columnList = "info_id, user_id", unique = true),
  @Index(name = "idx__user_id__wedding_date", columnList = "user_id, wedding_date", unique = false)
})
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserInfoEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Size(max = 255)
  @Column(name = "info_id")
  private String infoId;

  @NotBlank
  @Size(max = 255)
  @Column(name = "user_id")
  private String userId;

  @Setter
  @NotBlank
  @Size(max = 100)
  @Column(name = "groom_name")
  private String groomName;

  @Setter
  @NotBlank
  @Size(max = 100)
  @Column(name = "bride_name")
  private String brideName;

  @Setter
  @Column(name = "wedding_date")
  private LocalDateTime weddingDate;

  @Column(name = "created_at")
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private LocalDateTime updatedAt;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
  private UserEntity user;

}
