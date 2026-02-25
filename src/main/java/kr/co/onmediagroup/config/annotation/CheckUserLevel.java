package kr.co.onmediagroup.config.annotation;

import kr.co.onmediagroup.user.model.dto.User;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 유저 레벨 체크 어노테이션
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserLevel {
    User.Level level() default User.Level.USER;
}
