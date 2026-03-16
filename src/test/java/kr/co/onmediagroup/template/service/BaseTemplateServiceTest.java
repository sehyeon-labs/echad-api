package kr.co.onmediagroup.template.service;

import kr.co.onmediagroup.template.exception.TemplateException;
import kr.co.onmediagroup.template.model.dto.BaseTemplate;
import kr.co.onmediagroup.template.model.entity.BaseTemplateEntity;
import kr.co.onmediagroup.template.repository.BaseTemplateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseTemplateServiceTest {

    @Mock
    private BaseTemplateRepository baseTemplateRepository;

    @InjectMocks
    private BaseTemplateService baseTemplateService;

    @Test
    @DisplayName("어드민용 템플릿 목록 조회 성공")
    void findAllTemplates_Success() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("sortOrder").ascending());
        BaseTemplateEntity entity = BaseTemplateEntity.builder()
                .templateId(1)
                .title("Test Template")
                .sortOrder(1)
                .build();
        Page<BaseTemplateEntity> entityPage = new PageImpl<>(List.of(entity), pageable, 1);

        when(baseTemplateRepository.findAllByDeletedAtIsNull(any(Pageable.class))).thenReturn(entityPage);

        // when
        Page<BaseTemplate.TemplateRes> result = baseTemplateService.findAllTemplates(pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Template", result.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("템플릿이 없을 경우 NoTemplate 예외 발생")
    void findAllTemplates_NoTemplate() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        when(baseTemplateRepository.findAllByDeletedAtIsNull(any(Pageable.class))).thenReturn(Page.empty());

        // when & then
        assertThrows(TemplateException.NoTemplate.class, () -> baseTemplateService.findAllTemplates(pageable));
    }
}
