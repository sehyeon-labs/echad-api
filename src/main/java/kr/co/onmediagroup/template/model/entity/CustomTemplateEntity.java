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
@Table(name = "custom_template")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CustomTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_template_id")
    private Integer customTemplateId;

    @Column(name = "custom_id")
    private String customId;

    @Column(name = "template_custom_id")
    private Integer templateCustomId;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_id", insertable = false, updatable = false)
    private CustomEntity custom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_custom_id", insertable = false, updatable = false)
    private TemplateCustomEntity templateCustom;
}
