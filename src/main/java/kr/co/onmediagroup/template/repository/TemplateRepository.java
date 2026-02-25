package kr.co.onmediagroup.template.repository;

import kr.co.onmediagroup.template.model.dto.Template;
import kr.co.onmediagroup.template.model.entity.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<TemplateEntity, Integer> {

  List<TemplateEntity> findByActiveYnOrderBySortOrderDesc(Template.ActiveYn activeYn);

  boolean existsByTitleAndSchemaVersion(String title, Integer schemaVersion);
}
