package kr.co.onmediagroup.config.aspect;

import kr.co.onmediagroup.config.annotation.CheckAdminUser;
import kr.co.onmediagroup.config.annotation.CheckUserLevel;
import kr.co.onmediagroup.exception.AuthException;
import kr.co.onmediagroup.exception.ForbiddenException;
import kr.co.onmediagroup.user.model.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuthAspect {
    private static final Logger log = LoggerFactory.getLogger(AuthAspect.class);

    @Before("@annotation(kr.co.onmediagroup.config.annotation.CheckUserLevel) || @annotation(kr.co.onmediagroup.config.annotation.CheckAdminUser)")
    public void checkLevel(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 어노테이션 정보 추출 (상속된 어노테이션 포함)
        CheckUserLevel checkUserLevel = AnnotatedElementUtils.findMergedAnnotation(method, CheckUserLevel.class);
        if (checkUserLevel == null) {
            return;
        }

        User.Level requiredLevel = checkUserLevel.level();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 1. 로그인 여부 확인
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new AuthException.UnauthorizedMe();
        }

        // 2. 권한 확인
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User.UserPrincipal userPrincipal)) {
            throw new AuthException("유효하지 않은 유저 정보입니다.");
        }

        User.Level userLevel = userPrincipal.getUserLevel();

        // ADMIN은 모든 권한 통과, 그 외에는 레벨 일치 여부 확인
        if (userLevel != User.Level.ADMIN && userLevel != requiredLevel) {
            log.warn("Access Denied: User[{}] level[{}] required[{}]", userPrincipal.getUserId(), userLevel, requiredLevel);
            throw new ForbiddenException("해당 기능에 대한 권한이 없습니다.");
        }
    }
}
