package kr.co.onmediagroup.user.service;

import kr.co.onmediagroup.user.exception.UserInfoException;
import kr.co.onmediagroup.user.model.entity.UserInfoEntity;
import kr.co.onmediagroup.user.repository.UserInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInfoServiceTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private UserInfoService userInfoService;

    @Test
    @DisplayName("자기 자신 정보 수정 성공")
    void updateMe_Success() {
        // Given
        String userId = "user1";
        String newGroomName = "New Groom";
        String newBrideName = "New Bride";
        LocalDateTime newWeddingDate = LocalDateTime.of(2026, 3, 23, 12, 0);

        UserInfoEntity userInfoEntity = UserInfoEntity.builder()
                .userId(userId)
                .groomName("Old Groom")
                .brideName("Old Bride")
                .weddingDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .build();

        when(userInfoRepository.findByUserId(userId)).thenReturn(userInfoEntity);

        // When
        userInfoService.updateMe(userId, newGroomName, newBrideName, newWeddingDate);

        // Then
        assertEquals(newGroomName, userInfoEntity.getGroomName());
        assertEquals(newBrideName, userInfoEntity.getBrideName());
        assertEquals(newWeddingDate, userInfoEntity.getWeddingDate());
        verify(userInfoRepository, times(1)).save(userInfoEntity);
    }

    @Test
    @DisplayName("자기 자신 정보 수정 실패 - 데이터 없음")
    void updateMe_Fail_NoUserInfo() {
        // Given
        String userId = "nonexistent";
        when(userInfoRepository.findByUserId(userId)).thenReturn(null);

        // When & Then
        assertThrows(UserInfoException.NoUserInfo.class, () -> 
            userInfoService.updateMe(userId, "Groom", "Bride", LocalDateTime.now())
        );
        verify(userInfoRepository, never()).save(any());
    }
}
