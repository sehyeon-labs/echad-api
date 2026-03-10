package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.template.exception.TemplateException;
import kr.co.onmediagroup.template.model.dto.Template;
import kr.co.onmediagroup.template.model.entity.TemplateEntity;
import kr.co.onmediagroup.template.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.co.onmediagroup.util.ModelConverter.MODEL_MAPPER;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TemplateService {

  private final TemplateRepository templateRepository;

  public List<Template.TemplateResponse> findActiveTemplates() {
    List<TemplateEntity> entityList = this.templateRepository
      .findByActiveYnAndDeletedAtIsNullOrderBySortOrderDesc(Template.ActiveYn.Y);

    List<Template.TemplateResponse> res = entityList.stream()
      .map(entity -> MODEL_MAPPER.map(entity, Template.TemplateResponse.class))
      .toList();

    return res;
  }

  @Transactional
  public Template.TemplateResponse create(
    String title,
    Template.Component component,
    Integer componentType,
    String previewUrl,
    Template.IsPremium isPremium,
    String templateSchema,
    Integer sortOrder
  ) {
    // 중복검사
    boolean exists = this.templateRepository.existsByTitleAndComponentAndComponentType(title, component, componentType);

    if (exists) {
      throw new TemplateException.AlreadyExistTemplate();
    }

    TemplateEntity entity = TemplateEntity.builder()
      .title(title)
      .component(component)
      .componentType(componentType)
      .previewUrl(previewUrl)
      .isPremium(isPremium)
      .templateSchema(templateSchema)
      .sortOrder(sortOrder)
      .build();

    entity = this.templateRepository.save(entity);

    return MODEL_MAPPER.map(entity, Template.TemplateResponse.class);
  }
}
