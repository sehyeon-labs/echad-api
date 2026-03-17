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
import java.util.Optional;

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

    @Test
    @DisplayName("템플릿 상세 조회 성공")
    void findById_Success() {
        // given
        Integer templateId = 1;
        BaseTemplateEntity entity = BaseTemplateEntity.builder()
                .templateId(templateId)
                .title("Test Template")
                .activeYn(BaseTemplate.ActiveYn.Y)
                .build();
        when(baseTemplateRepository.findByTemplateIdAndDeletedAtIsNull(templateId)).thenReturn(Optional.of(entity));

        // when
        BaseTemplate.TemplateRes result = baseTemplateService.findById(templateId);

        // then
        assertNotNull(result);
        assertEquals("Test Template", result.getTitle());
    }

    @Test
    @DisplayName("템플릿이 존재하지 않을 때 NoTemplate 예외 발생")
    void findById_NoTemplate() {
        // given
        Integer templateId = 99;
        when(baseTemplateRepository.findByTemplateIdAndDeletedAtIsNull(templateId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(TemplateException.NoTemplate.class, () -> baseTemplateService.findById(templateId));
    }

    @Test
    @DisplayName("비활성 템플릿일 때 InactiveTemplate 예외 발생")
    void findById_InactiveTemplate() {
        // given
        Integer templateId = 1;
        BaseTemplateEntity entity = BaseTemplateEntity.builder()
                .templateId(templateId)
                .activeYn(BaseTemplate.ActiveYn.N)
                .build();
        when(baseTemplateRepository.findByTemplateIdAndDeletedAtIsNull(templateId)).thenReturn(Optional.of(entity));

        // when & then
        assertThrows(TemplateException.InactiveTemplate.class, () -> baseTemplateService.findById(templateId));
    }

    @Test
    @DisplayName("어드민용 템플릿 상세 조회 성공 (비활성/삭제 포함)")
    void findAdminTemplateById_Success() {
        // given
        Integer templateId = 1;
        BaseTemplateEntity entity = BaseTemplateEntity.builder()
                .templateId(templateId)
                .title("Admin Test Template")
                .activeYn(BaseTemplate.ActiveYn.N) // 비활성 상태
                .build();
        when(baseTemplateRepository.findById(templateId)).thenReturn(Optional.of(entity));

        // when
        BaseTemplate.TemplateRes result = baseTemplateService.findTemplateOne(templateId);

        // then
        assertNotNull(result);
        assertEquals("Admin Test Template", result.getTitle());
    }

    @Test
    @DisplayName("어드민용 조회 시 템플릿이 존재하지 않을 때 NoTemplate 예외 발생")
    void findAdminTemplateById_NoTemplate() {
        // given
        Integer templateId = 999;
        when(baseTemplateRepository.findById(templateId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(TemplateException.NoTemplate.class, () -> baseTemplateService.findTemplateOne(templateId));
    }
}
