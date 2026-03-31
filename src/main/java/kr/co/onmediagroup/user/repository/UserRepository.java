package kr.co.onmediagroup.user.repository;

import kr.co.onmediagroup.user.model.dto.User;
import kr.co.onmediagroup.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
  Optional<UserEntity> findByUserEmail(String userEmail);
  Optional<UserEntity> findBySocialIdAndSocialType(String socialId, User.SocialType socialType);
}
