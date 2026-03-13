package kr.co.onmediagroup.template.repository;

import kr.co.onmediagroup.template.model.entity.CustomTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomTemplateRepository extends JpaRepository<CustomTemplateEntity, Integer> {
}
