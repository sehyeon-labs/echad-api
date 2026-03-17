package kr.co.onmediagroup.template.repository;

import kr.co.onmediagroup.template.model.dto.BaseTemplate;
import kr.co.onmediagroup.template.model.entity.BaseTemplateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaseTemplateRepository extends JpaRepository<BaseTemplateEntity, Integer> {

  List<BaseTemplateEntity> findByActiveYnAndDeletedAtIsNullOrderBySortOrderDesc(BaseTemplate.ActiveYn activeYn);

  boolean existsByTitleAndComponentAndComponentType(String title, BaseTemplate.Component component, Integer componentType);

  boolean existsByTemplateIdAndActiveYnAndDeletedAtIsNull(Integer templateId, BaseTemplate.ActiveYn activeYn);

  Page<BaseTemplateEntity> findAllByDeletedAtIsNull(Pageable pageable);

  Optional<BaseTemplateEntity> findByTemplateIdAndDeletedAtIsNull(Integer templateId);
}
