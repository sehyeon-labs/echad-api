package kr.co.onmediagroup.config.annotation;

import kr.co.onmediagroup.user.model.dto.User;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 관리자 권한 체크 어노테이션
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@CheckUserLevel(level = User.Level.ADMIN)
public @interface CheckAdminUser {
}
