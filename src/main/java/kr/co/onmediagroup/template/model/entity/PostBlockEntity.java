package kr.co.onmediagroup.template.model.entity;

import jakarta.persistence.*;
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
@Table(name = "post_block")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PostBlockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_block_id")
    private Integer postBlockId;

    @Column(name = "post_id", nullable = false)
    private String postId;

    @Column(name = "template_id", nullable = false)
    private Integer templateId;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "custom_schema")
    @Convert(converter = StringMapConverter.class)
    private Map<String, String> customSchema;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", insertable = false, updatable = false)
    private BaseTemplateEntity template;

    @Builder.Default
    @OneToMany(mappedBy = "postBlock", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<PostBlockImageEntity> images = new ArrayList<>();

    public void updateSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
