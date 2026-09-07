package kr.co.onmediagroup.user.service;

import kr.co.onmediagroup.user.exception.UserException;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.model.dto.UserInfo;
import kr.co.onmediagroup.user.model.entity.UserEntity;
import kr.co.onmediagroup.user.model.entity.UserInfoEntity;
import kr.co.onmediagroup.user.repository.UserInfoRepository;
import kr.co.onmediagroup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static kr.co.onmediagroup.util.ModelConverter.MODEL_MAPPER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserInfoRepository userInfoRepository;
  private final PasswordEncoder passwordEncoder;

  // 어드민 계정 생성
  public User.UserJoinRes createAdminUser(User.AdminUserJoinReq req) {
    if (userRepository.existsById(req.userId())) {
      throw new UserException.AlreadyExistUserId();
    }

    if (userRepository.existsByUserEmail(req.userEmail())) {
      throw new UserException.AlreadyExistUserEmail();
    }

    UserEntity userEntity = UserEntity.builder()
      .userId(req.userId())
      .userPassword(passwordEncoder.encode(req.userPassword()))
      .userEmail(req.userEmail())
      .userName(req.userName())
      .phoneNumber(req.phoneNumber())
      .userLevel(User.Level.ADMIN)
      .build();

    try {
      userRepository.save(userEntity);
    } catch (org.springframework.dao.DataIntegrityViolationException e) {
      throw new UserException.AlreadyExistUserId();
    }

    return User.UserJoinRes.builder()
      .userId(userEntity.getUserId())
      .userEmail(userEntity.getUserEmail())
      .phoneNumber(userEntity.getPhoneNumber())
      .userName(userEntity.getUserName())
      .build();
  }

  // 유저 목록 조회
  public Page<User.UserMeRes> getUsers(Pageable pageable) {
    Page<UserEntity> userEntities = userRepository.findAll(pageable);

    if (userEntities.isEmpty()) {
      throw new UserException.UserNotFound();
    }

    Page<User.UserMeRes> userMeRes = userEntities.map(
      userEntity -> MODEL_MAPPER.map(userEntity, User.UserMeRes.class));

    return userMeRes;
  }

  // 자기 자신 정보 조회
  public User.UserMeRes getMe(String userId) {
    UserEntity userEntity = userRepository.findById(userId)
      .orElseThrow(UserException.UserNotFound::new);

    User.UserMeRes userMeRes = MODEL_MAPPER.map(userEntity, User.UserMeRes.class);

    UserInfoEntity userInfoEntity = userInfoRepository.findByUserId(userId);

    if (userInfoEntity != null) {
      UserInfo.UserInfoMeRes userInfoMeRes = MODEL_MAPPER.map(userInfoEntity, UserInfo.UserInfoMeRes.class);
      userMeRes.setUserInfo(userInfoMeRes);
    }

    return userMeRes;
  }

  // 자기 자신 정보 수정
  public void updateMe(
    String userId,
    String userEmail,
    String phoneNumber,
    User.VerifiedYn phoneVerifiedYn,
    String groomName,
    String brideName,
    LocalDateTime weddingDate
  ) {
    // User 정보 수정
    UserEntity userEntity = userRepository.findById(userId)
      .orElseThrow(UserException.UserNotFound::new);

    userEntity.setUserEmail(userEmail);
    userEntity.setPhoneNumber(phoneNumber);
    userEntity.setPhoneVerifiedYn(phoneVerifiedYn);

    userRepository.save(userEntity);

    // User Info 정보 수정
    UserInfoEntity userInfoEntity = userInfoRepository.findByUserId(userId);

    if (userInfoEntity == null) {
      UserInfoEntity newUserinfoEntity = UserInfoEntity.builder()
        .userId(userId)
        .groomName(groomName)
        .brideName(brideName)
        .weddingDate(weddingDate)
        .build();

      userInfoRepository.save(newUserinfoEntity);
    } else {
      userInfoEntity.setGroomName(groomName);
      userInfoEntity.setBrideName(brideName);
      userInfoEntity.setWeddingDate(weddingDate);

      userInfoRepository.save(userInfoEntity);
    }
  }
}
