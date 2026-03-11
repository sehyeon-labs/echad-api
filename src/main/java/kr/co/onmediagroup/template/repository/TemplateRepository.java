package kr.co.onmediagroup.template.repository;

import kr.co.onmediagroup.template.model.dto.Template;
import kr.co.onmediagroup.template.model.entity.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<TemplateEntity, Integer> {

  List<TemplateEntity> findByActiveYnAndDeletedAtIsNullOrderBySortOrderDesc(Template.ActiveYn activeYn);

  boolean existsByTitleAndComponentAndComponentType(String title, Template.Component component, Integer componentType);

  boolean existsByTemplateIdAndActiveYnAndDeletedAtIsNull(Integer templateId, Template.ActiveYn activeYn);
}
