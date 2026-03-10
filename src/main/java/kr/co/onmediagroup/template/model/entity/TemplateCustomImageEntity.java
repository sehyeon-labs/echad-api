package kr.co.onmediagroup.template.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "template_custom_image")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TemplateCustomImageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "template_custom_image_id")
  private Integer templateCustomImageId;

  @Column(name = "template_custom_id", nullable = false)
  private Integer templateCustomId;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @Builder.Default
  @Column(name = "sort_order")
  private Integer sortOrder = 0;

  @Column(name = "created_at")
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private LocalDateTime updatedAt;
}
