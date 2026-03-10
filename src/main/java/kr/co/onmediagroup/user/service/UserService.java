package kr.co.onmediagroup.user.service;

import jakarta.transaction.Transactional;
import kr.co.onmediagroup.user.exception.UserException;
import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.model.entity.UserEntity;
import kr.co.onmediagroup.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static kr.co.onmediagroup.util.ModelConverter.MODEL_MAPPER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  /**
   * 유저 전체 목록 조회 (페이징)
   *
   * @param pageable 페이징 및 정렬 정보
   * @return 페이징된 유저 목록
   */
  public Page<User.UserModel> findAll(Pageable pageable) {
    Page<UserEntity> entityPage = this.userRepository.findAll(pageable);

    if (entityPage.isEmpty()) {
      throw new UserException.UserNotFound();
    }

    return entityPage.map(entity -> MODEL_MAPPER.map(entity, User.UserModel.class));
  }
}
