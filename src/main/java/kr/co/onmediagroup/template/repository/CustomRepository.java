package kr.co.onmediagroup.template.repository;

import kr.co.onmediagroup.template.model.entity.CustomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomRepository extends JpaRepository<CustomEntity, String> {
  Optional<CustomEntity> findByCustomIdAndUserIdAndDeletedAtIsNull(String customId, String userId);
}
