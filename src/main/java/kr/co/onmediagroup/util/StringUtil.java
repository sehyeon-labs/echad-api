package kr.co.onmediagroup.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 문자열 유틸리티 클래스
 *
 * @param givenString 검사할 문자열
 * @return 공백이 아닌 경우 true, 그 외는 false
 * */
@Slf4j
public class StringUtil {

  public static boolean isExist(String givenString) {
    if (givenString == null) { // check null
      return false;
    }
    if (givenString.isBlank()) { // " " blank check
      return false;
    }
    return true;
  }

}
