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
@Table(name = "template_custom")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TemplateCustomEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "template_custom_id")
  private Integer templateCustomId;

  @NotBlank
  @Size(max = 255)
  @Column(name = "user_id")
  private String userId;

  @NotNull
  @Column(name = "template_id")
  private Integer templateId;

  @Column(name = "custom_schema")
  @Convert(converter = StringMapConverter.class)
  private Map<String, String> customSchema;

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
