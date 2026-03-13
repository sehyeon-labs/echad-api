package kr.co.onmediagroup.template.model.entity;

import jakarta.persistence.*;
import kr.co.onmediagroup.template.model.dto.TemplateCustom;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "custom")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CustomEntity {

    @Id
    @Column(name = "custom_id")
    private String customId;

    @Column(name = "user_id")
    private String userId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "active_yn")
    private TemplateCustom.ActiveYn activeYn = TemplateCustom.ActiveYn.N;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "custom", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<CustomTemplateEntity> customTemplates = new ArrayList<>();
}
