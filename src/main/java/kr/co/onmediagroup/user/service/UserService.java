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

  public Page<User.UserModel> findAll(Pageable pageable) {
    Page<UserEntity> entityPage = this.userRepository.findAll(pageable);

    if (entityPage.isEmpty()) {
      throw new UserException.UserNotFound();
    }

    Page<User.UserModel> modelPage = entityPage.map(entity -> MODEL_MAPPER.map(entity, User.UserModel.class));

    return modelPage;
  }
}
