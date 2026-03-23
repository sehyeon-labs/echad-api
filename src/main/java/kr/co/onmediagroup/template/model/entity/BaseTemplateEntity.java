package kr.co.onmediagroup.template.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.co.onmediagroup.template.model.dto.BaseTemplate;
import kr.co.onmediagroup.util.StringMapConverter;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "base_template")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BaseTemplateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "template_id")
  private Integer templateId;

  @NotBlank
  @Size(max = 255)
  @Column(name = "title")
  private String title;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "component", nullable = false)
  private BaseTemplate.Component component = BaseTemplate.Component.TEXT;

  @Builder.Default
  @Column(name = "component_type", nullable = false)
  private Integer componentType = 1;

  @Size(max = 255)
  @Column(name = "preview_url")
  private String previewUrl;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "is_premium")
  private BaseTemplate.IsPremium isPremium = BaseTemplate.IsPremium.N;

  @Column(name = "template_schema")
  @Convert(converter = StringMapConverter.class)
  private Map<String, String> templateSchema;

  @Builder.Default
  @Column(name = "sort_order")
  private Integer sortOrder = 0;

  @Builder.Default
  @Setter
  @Enumerated(EnumType.STRING)
  @Column(name = "active_yn")
  private BaseTemplate.ActiveYn activeYn = BaseTemplate.ActiveYn.N;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "created_at")
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private LocalDateTime updatedAt;
}
