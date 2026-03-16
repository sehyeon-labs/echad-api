package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.template.exception.TemplateException;
import kr.co.onmediagroup.template.model.dto.BaseTemplate;
import kr.co.onmediagroup.template.model.entity.BaseTemplateEntity;
import kr.co.onmediagroup.template.repository.BaseTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static kr.co.onmediagroup.util.ModelConverter.MODEL_MAPPER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BaseTemplateService {
  private final BaseTemplateRepository baseTemplateRepository;

  public List<BaseTemplate.TemplateRes> findActiveTemplates() {
    List<BaseTemplateEntity> entityList = this.baseTemplateRepository
      .findByActiveYnAndDeletedAtIsNullOrderBySortOrderDesc(BaseTemplate.ActiveYn.Y);

    if (entityList.isEmpty()) {
      throw new TemplateException.NoTemplate();
    }

    return entityList.stream()
      .map(entity -> MODEL_MAPPER.map(entity, BaseTemplate.TemplateRes.class))
      .toList();
  }

  public Page<BaseTemplate.TemplateRes> findAllTemplates(Pageable pageable) {
    Page<BaseTemplateEntity> entityPage = this.baseTemplateRepository.findAllByDeletedAtIsNull(pageable);

    if (entityPage.isEmpty()) {
      throw new TemplateException.NoTemplate();
    }

    return entityPage.map(entity -> MODEL_MAPPER.map(entity, BaseTemplate.TemplateRes.class));
  }

  public BaseTemplate.TemplateRes create(
    String title,
    BaseTemplate.Component component,
    Integer componentType,
    String previewUrl,
    BaseTemplate.IsPremium isPremium,
    Map<String, String> templateSchema,
    Integer sortOrder
  ) {
    boolean exists = this.baseTemplateRepository.existsByTitleAndComponentAndComponentType(title, component, componentType);

    if (exists) {
      throw new TemplateException.AlreadyExistTemplate();
    }

    BaseTemplateEntity entity = BaseTemplateEntity.builder()
      .title(title)
      .component(component)
      .componentType(componentType)
      .previewUrl(previewUrl)
      .isPremium(isPremium)
      .templateSchema(templateSchema)
      .sortOrder(sortOrder)
      .build();

    entity = this.baseTemplateRepository.save(entity);

    return MODEL_MAPPER.map(entity, BaseTemplate.TemplateRes.class);
  }
}
