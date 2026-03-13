package kr.co.onmediagroup.template.repository;

import kr.co.onmediagroup.template.model.entity.PostBlockEntity;
import kr.co.onmediagroup.template.model.dto.BaseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostBlockRepository extends JpaRepository<PostBlockEntity, Integer> {
    @Query("""
        SELECT DISTINCT pb FROM PostBlockEntity pb
        JOIN FETCH pb.template t
        LEFT JOIN FETCH pb.images pbi
        WHERE pb.postId = :postId
          AND t.activeYn = :activeYn
          AND t.deletedAt IS NULL
        ORDER BY pb.sortOrder ASC
    """)
    List<PostBlockEntity> findAllByPostIdWithDetails(
        @Param("postId") String postId,
        @Param("activeYn") BaseTemplate.ActiveYn activeYn
    );
}
