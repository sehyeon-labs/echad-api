package kr.co.onmediagroup.template.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.co.onmediagroup.template.model.dto.TemplateCustom;
import kr.co.onmediagroup.user.model.entity.UserEntity;
import kr.co.onmediagroup.util.StringMapConverter;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "template_custom", indexes = {
  @Index(name = "uq__user_id__custom_id__sort_order", columnList = "user_id, custom_id, sort_order", unique = true),
  @Index(name = "idx__custom_id__sort_order", columnList = "custom_id, sort_order", unique = false),
  @Index(name = "idx__custom_id__deleted_at", columnList = "custom_id, deleted_at", unique = false)
})
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TemplateCustomEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "template_custom_id")
  private Integer templateCustomId;

  @NotBlank
  @Size(max = 255)
  @Column(name = "custom_id")
  private String customId;

  @NotBlank
  @Size(max = 255)
  @Column(name = "user_id")
  private String userId;

  @NotNull
  @Column(name = "template_id")
  private Integer templateId;

  @Builder.Default
  @NotNull
  @Column(name = "sort_order")
  private Integer sortOrder = 0;

  @Column(name = "custom_schema")
  @Convert(converter = StringMapConverter.class)
  private Map<String, String> customSchema;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "active_yn")
  private TemplateCustom.ActiveYn activeYn = TemplateCustom.ActiveYn.N;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "created_at")
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private LocalDateTime updatedAt;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "template_id", referencedColumnName = "template_id", insertable = false, updatable = false)
  private TemplateEntity template;

  @Builder.Default
  @OneToMany(fetch = FetchType.LAZY)
  @OrderBy("sortOrder ASC")
  @JoinColumn(name = "template_custom_id", referencedColumnName = "template_custom_id", insertable = false, updatable = false)
  private List<TemplateCustomImageEntity> images = new ArrayList<>();
}
