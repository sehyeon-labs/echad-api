package kr.co.onmediagroup.template.model.entity;

import jakarta.persistence.*;
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
@Table(name = "template_custom")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TemplateCustomEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "template_custom_id")
  private Integer templateCustomId;

  @Column(name = "custom_id", nullable = false)
  private String customId;

  @Column(name = "template_id", nullable = false)
  private Integer templateId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "template_id", referencedColumnName = "template_id", insertable = false, updatable = false)
  private TemplateEntity template;

  @Builder.Default
  @Column(name = "sort_order")
  private Integer sortOrder = 0;

  @Column(name = "custom_schema", columnDefinition = "json")
  private String customSchema;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "created_at")
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private LocalDateTime updatedAt;
}
