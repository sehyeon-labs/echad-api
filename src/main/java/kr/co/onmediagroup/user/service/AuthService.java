package kr.co.onmediagroup.user.service;

import kr.co.onmediagroup.config.AuthConfig;
import kr.co.onmediagroup.user.exception.LoginException;
import kr.co.onmediagroup.user.exception.UserException;
import kr.co.onmediagroup.user.exception.UserInfoException;
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

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepository;
  private final UserInfoRepository userInfoRepository;
  private final AuthConfig authConfig;
  private final PasswordEncoder passwordEncoder;
  private final JWTUtil jwtUtil;

  // 유저 정보 이름만 조회
  public UserInfo.UserInfoName getUserInfoName(String userId) {
    UserInfo.UserInfoName dto = this.userInfoRepository.findUserNameByUserId(userId);

    if (dto == null) {
      throw new UserInfoException.NoUserInfo();
    }

    return dto;
  }

  // 해당 예외가 발생해도 rollback 안 되고, save() 반영됨.
  @Transactional(noRollbackFor = {LoginException.InvalidPassword.class})
  public User.UserLoginModel login(String userId, String userPassword) {

    // 유저 확인
    UserEntity userEntity = this.userRepository.findById(userId)
      .orElseThrow(LoginException.NoUser::new);

    // 로그인 실패 횟수 확인
    Integer maxLoginFailCount = this.authConfig.getMaxLoginFailCount();
    Integer currentLoginFailCount = userEntity.getLoginFailCount();

    if (currentLoginFailCount > maxLoginFailCount) {
      throw new LoginException.TooManyFailedLogin(currentLoginFailCount);
    }

    // 비활성화 유저 확인
    User.ActiveYn userActiveYn = userEntity.getActiveYn();
    if (userActiveYn != User.ActiveYn.Y) {
      throw new LoginException.DeactivatedUser(currentLoginFailCount);
    }

    // 비밀번호 확인
    String userPwHash = userEntity.getUserPassword();
    boolean isMatchedPwd = this.passwordEncoder.matches(userPassword, userPwHash);
    log.info("로그인 확인 : {}", userEntity.getLoginFailCount());

    if (!isMatchedPwd) {
      userEntity = userEntity.increaseFailedLogin(maxLoginFailCount);
      log.info("로그인 최대 : {}", userEntity.getLoginFailCount());
      userEntity = this.userRepository.save(userEntity);
      log.info("로그인 오류 횟수 : {}", userEntity.getLoginFailCount());
      throw new LoginException.InvalidPassword(userEntity.getLoginFailCount());
    }

    // 로그인 성공 시, 로그인 실패 횟수 초기화
    userEntity = userEntity.clearFailedLogin();
    userEntity = this.userRepository.save(userEntity);

    // 토큰 발급
    String token = this.jwtUtil.createToken(userEntity);

    // 유저 상세 정보 조회
    UserInfoEntity userInfoEntity = this.userInfoRepository.findByUserId(userEntity.getUserId());
    if (userInfoEntity == null) {
      throw new UserInfoException.NoUserInfo();
    }

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

  // 회원가입
  public User.UserJoinRes join(
    String userId,
    String userPassword,
    String userEmail,
    String phoneNumber,
    User.VerifiedYn phoneVerifiedYn,
    String groomName,
    String brideName,
    LocalDateTime weddingDate
  ) {
    boolean existsById = this.userRepository.existsById(userId);
    if (existsById) {
      throw new UserException.AlreadyExistUserId();
    }

    UserEntity userEntity = UserEntity.builder()
      .userId(userId)
      .userPassword(passwordEncoder.encode(userPassword))
      .userEmail(userEmail)
      .phoneNumber(phoneNumber)
      .phoneVerifiedYn(phoneVerifiedYn)
      .build();

    this.userRepository.save(userEntity);

    UserInfoEntity userInfoEntity = UserInfoEntity.builder()
      .userId(userId)
      .groomName(groomName)
      .brideName(brideName)
      .weddingDate(weddingDate)
      .build();

    this.userInfoRepository.save(userInfoEntity);

    return User.UserJoinRes.builder()
      .userId(userEntity.getUserId())
      .userEmail(userEntity.getUserEmail())
      .phoneNumber(userEntity.getPhoneNumber())
      .phoneVerifiedYn(userEntity.getPhoneVerifiedYn())
      .groomName(groomName)
      .brideName(brideName)
      .weddingDate(weddingDate)
      .build();
  }

  // 비밀번호 변경
  public void updatePassword(String userId, User.UserPasswordUpdateReq req) {
    UserEntity userEntity = this.userRepository.findById(userId)
      .orElseThrow(UserException.UserNotFound::new);

    // 1. 현재 비밀번호 일치 확인
    if (!passwordEncoder.matches(req.currentPassword(), userEntity.getUserPassword())) {
      throw new UserException.InvalidCurrentPassword();
    }

    // 2. 새 비밀번호와 확인 비밀번호 일치 확인
    if (!req.newPassword().equals(req.newPasswordConfirm())) {
      throw new UserException.NewPasswordNotMatch();
    }

    // 3. 현재 비밀번호와 새 비밀번호가 동일한지 확인
    if (passwordEncoder.matches(req.newPassword(), userEntity.getUserPassword())) {
      throw new UserException.SameAsCurrentPassword();
    }

    // 4. 비밀번호 암호화 및 저장
    userEntity.setUserPassword(passwordEncoder.encode(req.newPassword()));
    this.userRepository.save(userEntity);
  }
}
