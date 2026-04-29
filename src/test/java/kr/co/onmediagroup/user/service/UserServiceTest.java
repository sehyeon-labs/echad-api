package kr.co.onmediagroup.user.service;

import kr.co.onmediagroup.user.exception.UserException;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.model.entity.UserEntity;
import kr.co.onmediagroup.user.repository.UserRepository;
import kr.co.onmediagroup.user.repository.UserInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("어드민 계정 생성 성공")
    void createAdminUser_Success() {
        // Given
        User.AdminUserJoinReq req = new User.AdminUserJoinReq("adminId", "password", "admin@test.com", "AdminName", "010-1234-5678");
        
        when(userRepository.existsById(req.userId())).thenReturn(false);
        when(userRepository.existsByUserEmail(req.userEmail())).thenReturn(false);
        when(passwordEncoder.encode(req.userPassword())).thenReturn("encodedPassword");
        
        // When
        User.UserJoinRes result = userService.createAdminUser(req);
        
        // Then
        assertNotNull(result);
        assertEquals(req.userId(), result.getUserId());
        assertEquals(req.userEmail(), result.getUserEmail());
        assertEquals(req.userName(), result.getUserName());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("어드민 계정 생성 실패 - 아이디 중복")
    void createAdminUser_Fail_DuplicateId() {
        // Given
        User.AdminUserJoinReq req = new User.AdminUserJoinReq("adminId", "password", "admin@test.com", "AdminName", "010-1234-5678");
        when(userRepository.existsById(req.userId())).thenReturn(true);
        
        // When & Then
        assertThrows(UserException.AlreadyExistUserId.class, () -> userService.createAdminUser(req));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("어드민 계정 생성 실패 - 이메일 중복")
    void createAdminUser_Fail_DuplicateEmail() {
        // Given
        User.AdminUserJoinReq req = new User.AdminUserJoinReq("adminId", "password", "admin@test.com", "AdminName", "010-1234-5678");
        when(userRepository.existsById(req.userId())).thenReturn(false);
        when(userRepository.existsByUserEmail(req.userEmail())).thenReturn(true);
        
        // When & Then
        assertThrows(UserException.AlreadyExistUserEmail.class, () -> userService.createAdminUser(req));
        verify(userRepository, never()).save(any());
    }
}
