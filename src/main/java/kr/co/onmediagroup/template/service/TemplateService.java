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
import java.util.Map;

import static kr.co.onmediagroup.util.ModelConverter.MODEL_MAPPER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TemplateService {
  private final TemplateRepository templateRepository;

  // 활성화 상태의 템플릿 목록 조회
  public List<Template.TemplateRes> findActiveTemplates() {
    List<TemplateEntity> entityList = this.templateRepository
      .findByActiveYnAndDeletedAtIsNullOrderBySortOrderDesc(Template.ActiveYn.Y);

    if (entityList.isEmpty()) {
      throw new TemplateException.NoTemplate();
    }

    List<Template.TemplateRes> res = entityList.stream()
      .map(entity -> MODEL_MAPPER.map(entity, Template.TemplateRes.class))
      .toList();

    return res;
  }

  // 템플릿 생성
  public Template.TemplateRes create(
    String title,
    Template.Component component,
    Integer componentType,
    String previewUrl,
    Template.IsPremium isPremium,
    Map<String, String> templateSchema,
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

    return MODEL_MAPPER.map(entity, Template.TemplateRes.class);
  }
}
