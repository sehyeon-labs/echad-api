package kr.co.onmediagroup.template.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.co.onmediagroup.template.model.dto.Template;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "template", indexes = {
  @Index(name = "ux__title__version", columnList = "title, schema_version", unique = true)
})
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TemplateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "template_id")
  private Integer templateId;

  @NotBlank
  @Size(max = 255)
  @Column(name = "title")
  private String title;

  @NotBlank
  @Size(max = 255)
  @Column(name = "preview_url")
  private String previewUrl;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "is_premium")
  private Template.IsPremium isPremium = Template.IsPremium.N;

  @Column(name = "template_schema", columnDefinition = "json")
  private String templateSchema;

  @Builder.Default
  @Column(name = "schema_version")
  private Integer schemaVersion = 1;

  @Builder.Default
  @Column(name = "sort_order")
  private Integer sortOrder = 0;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "active_yn")
  private Template.ActiveYn activeYn = Template.ActiveYn.N;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "created_at")
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private LocalDateTime updatedAt;
}
