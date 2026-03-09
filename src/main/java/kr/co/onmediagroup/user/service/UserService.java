package kr.co.onmediagroup.user.service;

import kr.co.onmediagroup.user.exception.UserException;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.model.dto.UserInfo;
import kr.co.onmediagroup.user.model.entity.UserEntity;
import kr.co.onmediagroup.user.model.entity.UserInfoEntity;
import kr.co.onmediagroup.user.repository.UserInfoRepository;
import kr.co.onmediagroup.user.repository.UserRepository;
import kr.co.onmediagroup.util.ModelConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserInfoRepository userInfoRepository;

  /**
   * 내 정보 조회
   */
  public User.UserMeRes getMe(String userId) {
    UserEntity userEntity = userRepository.findById(userId)
      .orElseThrow(UserException.UserNotFound::new);

    UserInfoEntity userInfoEntity = userInfoRepository.findByUserId(userId);

    User.UserMeRes res = ModelConverter.MODEL_MAPPER.map(userEntity, User.UserMeRes.class);
    if (userInfoEntity != null) {
      res.setUserInfo(ModelConverter.MODEL_MAPPER.map(userInfoEntity, UserInfo.UserInfoMeRes.class));
    }

    return res;
  }

  /**
   * 내 정보 수정
   */
  public void updateMe(String userId, User.UserUpdateReq req) {
    UserEntity userEntity = userRepository.findById(userId)
      .orElseThrow(UserException.UserNotFound::new);

    UserInfoEntity userInfoEntity = userInfoRepository.findByUserId(userId);

    userEntity.update(req);
    if (userInfoEntity != null) {
      userInfoEntity.update(req);
    }
  }
}
