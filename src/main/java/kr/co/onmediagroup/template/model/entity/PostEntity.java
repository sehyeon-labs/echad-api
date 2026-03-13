package kr.co.onmediagroup.template.model.entity;

import jakarta.persistence.*;
import kr.co.onmediagroup.template.model.dto.Post;
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
@Table(name = "post")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PostEntity {

    @Id
    @Column(name = "post_id")
    private String postId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "active_yn")
    private Post.ActiveYn activeYn = Post.ActiveYn.N;

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
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    private List<PostBlockEntity> blocks = new ArrayList<>();
}
