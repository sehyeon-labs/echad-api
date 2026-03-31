package kr.co.onmediagroup.user.repository;

import jakarta.validation.constraints.Size;
import kr.co.onmediagroup.user.model.dto.UserInfo;
import kr.co.onmediagroup.user.model.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInfoRepository extends JpaRepository<UserInfoEntity, String> {
  UserInfoEntity findByUserId(String userId);

  @Query("""
    SELECT new kr.co.onmediagroup.user.model.dto.UserInfo$UserInfoName(
      u.groomName,
      u.brideName
    )
    FROM UserInfoEntity u
    WHERE u.userId = :userId
  """)
  UserInfo.UserInfoName findUserNameByUserId(@Param("userId") String userId);
}
