package kr.co.onmediagroup.user.repository;

import kr.co.onmediagroup.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
