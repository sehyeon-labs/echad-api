package kr.co.onmediagroup.template.repository;

import kr.co.onmediagroup.template.model.entity.PostBlockImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostBlockImageRepository extends JpaRepository<PostBlockImageEntity, Integer> {
}
