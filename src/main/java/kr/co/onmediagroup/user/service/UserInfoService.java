package kr.co.onmediagroup.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import kr.co.onmediagroup.user.exception.UserInfoException;
import kr.co.onmediagroup.user.model.entity.UserInfoEntity;
import kr.co.onmediagroup.user.repository.UserInfoRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoService {

  private final UserInfoRepository userInfoRepository;

  public void updateMe(
    String userId,
    String groomName,
    String brideName,
    LocalDateTime weddingDate
  ) {
    UserInfoEntity userInfoEntity = userInfoRepository.findByUserId(userId);

    if (userInfoEntity == null) {
      throw new UserInfoException.NoUserInfo();
    }

    userInfoEntity.setGroomName(groomName);
    userInfoEntity.setBrideName(brideName);
    userInfoEntity.setWeddingDate(weddingDate);

    userInfoRepository.save(userInfoEntity);
  }
}
