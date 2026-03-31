package kr.co.onmediagroup.api.naver.service;

import kr.co.onmediagroup.api.naver.NaverClient;
import kr.co.onmediagroup.api.naver.dto.NaverResponse;
import kr.co.onmediagroup.exception.BadRequestException;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.model.dto.UserInfo;
import kr.co.onmediagroup.user.model.entity.UserEntity;
import kr.co.onmediagroup.user.model.entity.UserInfoEntity;
import kr.co.onmediagroup.user.repository.UserInfoRepository;
import kr.co.onmediagroup.user.repository.UserRepository;
import kr.co.onmediagroup.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

import static kr.co.onmediagroup.util.ModelConverter.MODEL_MAPPER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NaverService {
  private final NaverClient naverClient;
  private final UserRepository userRepository;
  private final UserInfoRepository userInfoRepository;
  private final JWTUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;

  public NaverResponse.AuthorizeResponse naverLogin() throws URISyntaxException {
    String state = UUID.randomUUID().toString();
    String redirectUri = "http://localhost:5173/naver/callback";
    NaverResponse.AuthorizeResponse response = this.naverClient.getAuthorize(state, redirectUri);
    log.info("response : {}", response);
    return response;
  }

  public User.UserLoginModel naverLoginCallback(
    String code,
    String state
  ) throws URISyntaxException {
    NaverResponse.TokenResponse tokenResponse = this.naverClient.authorizationToken(code, state);

    log.info("token : {}", tokenResponse);

    if (tokenResponse.getError() != null) {
      throw new BadRequestException("네이버 로그인 오류: " + tokenResponse.getErrorDescription());
    }

    NaverResponse.ProfileResponse profileResponse = this.naverClient.getUserProfile(tokenResponse.getAccess_token());

    if (profileResponse == null || profileResponse.getEmail() == null) {
      throw new BadRequestException("네이버 프로필 정보를 가져올 수 없습니다.");
    }

    // 이메일 중복 확인 (일반 회원가입 유저와 중복될 경우)
    Optional<UserEntity> existingUserByEmail = userRepository.findByUserEmail(profileResponse.getEmail());
    if (existingUserByEmail.isPresent() && existingUserByEmail.get().getSocialType() == User.SocialType.NORMAL) {
      throw new BadRequestException("이미 일반 회원으로 가입된 이메일입니다.");
    }

    // 소셜 로그인 유저 확인 (가입 또는 로그인)
    Optional<UserEntity> socialUser = userRepository.findBySocialIdAndSocialType(
      profileResponse.getId(),
      User.SocialType.NAVER
    );

    UserEntity userEntity;
    UserInfoEntity userInfoEntity = null;
    if (socialUser.isPresent()) {
      // 기존 소셜 유저 로그인
      userEntity = socialUser.get();
      // 필요한 경우 사용자 정보 업데이트 (예: 이름, 나이, 성별)
      userEntity.setUserName(profileResponse.getName());
      userEntity.setUserAge(profileResponse.getAge());
      userEntity.setUserGender(User.Gender.valueOf(profileResponse.getGender().toUpperCase()));
      userRepository.save(userEntity);

      userInfoEntity = userInfoRepository.findByUserId(userEntity.getUserId());
    } else {
      // 새로운 소셜 유저 회원가입
      String newUserId = UUID.randomUUID().toString(); // 새로운 유저 ID 생성
      userEntity = UserEntity.builder()
        .userId(newUserId)
        .userEmail(profileResponse.getEmail())
        .socialType(User.SocialType.NAVER)
        .socialId(profileResponse.getId())
        .userName(profileResponse.getName())
        .userAge(profileResponse.getAge())
        .userGender(User.Gender.valueOf(profileResponse.getGender().toUpperCase()))
        .userPassword(null) // 소셜 로그인 사용자는 비밀번호 없음
        .build();
      userRepository.save(userEntity);

      userInfoEntity = UserInfoEntity.builder()
        .userId(newUserId)
        .groomName(profileResponse.getGender().equals(User.Gender.M.name()) && profileResponse.getName() != null ? profileResponse.getName() : null)
        .brideName(profileResponse.getGender().equals(User.Gender.F.name()) && profileResponse.getName() != null ? profileResponse.getName() : null)
        .build();
      userInfoRepository.save(userInfoEntity);
    }

    // JWT 토큰 발급
    String token = this.jwtUtil.createToken(userEntity);

    // UserLoginModel 반환
    User.UserSimpleModel userModel = User.UserSimpleModel.builder()
      .userId(userEntity.getUserId())
      .userLevel(userEntity.getUserLevel())
      .activeYn(userEntity.getActiveYn())
      .build();

    UserInfo.UserInfoName userInfoName = UserInfo.UserInfoName.builder()
      .groomName(userInfoEntity.getGroomName())
      .brideName(userInfoEntity.getBrideName())
      .build();

    return User.UserLoginModel.builder()
      .token(token)
      .user(userModel)
      .userInfo(userInfoName)
      .build();
  }

  /**
   * 회원 탈퇴
   *
   * @param userId
   * @param accessToken
   * @throws URISyntaxException
   */
  public void withdraw(String userId, String accessToken) throws URISyntaxException {
    UserEntity userEntity = userRepository.findById(userId)
      .orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));

    // 네이버 연동 해제 (소셜 로그인 사용자인 경우)
    if (userEntity.getSocialType() == User.SocialType.NAVER && accessToken != null) {
      try {
        naverClient.revokeToken(accessToken);
      } catch (Exception e) {
        log.error("네이버 연동 해제 실패 (userId: {}): {}", userId, e.getMessage());
        // 연동 해제 실패해도 DB 삭제는 진행 (가정 준수)
      }
    }

    // DB 삭제 (UserInfoEntity는 UserEntity와 연관관계가 있거나 별도로 삭제 필요)
    UserInfoEntity userInfoEntity = userInfoRepository.findByUserId(userId);
    if (userInfoEntity != null) {
      userInfoRepository.delete(userInfoEntity);
    }
    userRepository.delete(userEntity);

    log.info("회원 탈퇴 완료: {}", userId);
  }
}
