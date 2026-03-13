package kr.co.onmediagroup.template.repository;

import kr.co.onmediagroup.template.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, String> {
  Optional<PostEntity> findByPostIdAndUserIdAndDeletedAtIsNull(String postId, String userId);
}
