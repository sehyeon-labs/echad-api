package kr.co.onmediagroup.util;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * JPA Auditing 용 AuditorAware 구현체
 *
 * */
public class AuditorAwareImple implements AuditorAware<String> {
  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.empty();
  }
}
