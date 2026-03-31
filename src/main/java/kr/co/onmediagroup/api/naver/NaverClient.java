package kr.co.onmediagroup.api.naver;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.co.onmediagroup.api.naver.dto.NaverRequest;
import kr.co.onmediagroup.api.naver.dto.NaverResponse;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static kr.co.onmediagroup.util.JacksonUtils.OBJECT_MAPPER;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;

// ... (existing imports)

@Slf4j
@Component
public class NaverClient {
  private final String CLIENT_ID;
  private final String CLIENT_SECRET;

  private final String API_PRIFIX = "/oauth2.0";
  private final String USER_PROFILE_API_URL = "https://openapi.naver.com/v1/nid/me";

  public NaverClient(
    @Value("${naver.client.id}") String clientId,
    @Value("${naver.client.secret}") String clientSecret
  ) {
    this.CLIENT_ID = clientId;
    this.CLIENT_SECRET = clientSecret;
  }

  // ... (existing buildUrl method)

  /**
   * Get User Profile
   * @param accessToken
   * @return NaverResponse.ProfileResponse
   */
  public NaverResponse.ProfileResponse getUserProfile(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<Map> response = restTemplate.exchange(
      USER_PROFILE_API_URL,
      HttpMethod.GET,
      entity,
      Map.class
    );

    Map<String, Object> responseBody = response.getBody();
    if (responseBody != null && responseBody.containsKey("response")) {
      Map<String, String> profileData = (Map<String, String>) responseBody.get("response");
      return NaverResponse.ProfileResponse.builder()
        .id(profileData.get("id"))
        .name(profileData.get("name"))
        .email(profileData.get("email"))
        .mobile(profileData.get("mobile"))
        .age(profileData.get("age"))
        .gender(profileData.get("gender"))
        .build();
    }
    return null; // Or throw an exception if profile cannot be retrieved
  }

  /**
   * Build Request URL
   *
   * @param path
   * @param params
   * @return request URL
   * @throws URISyntaxException
   */
  private String buildUrl(String path, Map<String, String> params) throws URISyntaxException {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://nid.naver.com")
      .path(API_PRIFIX + path);

    if (params != null) {
       params.forEach(builder::queryParam);
    }

    URI uri = builder.build().encode().toUri();

    return uri.toString();
  }

  /**
   * Authorize (로그인/회원가입)
   *
   * @param state
   * @param redirectUri
   * @return authorize URL
   * @throws URISyntaxException
   */
  public NaverResponse.AuthorizeResponse getAuthorize (String state, String redirectUri) throws URISyntaxException {
    NaverRequest.AuthorizeRequest request = NaverRequest.AuthorizeRequest.builder()
      .response_type("code")
      .client_id(CLIENT_ID)
      .redirect_id(redirectUri)
      .state(state)
      .build();

    Map<String, String> params = OBJECT_MAPPER.convertValue(request, new TypeReference<Map<String, String>>() {});

    String url = buildUrl("/authorize", params);

    NaverResponse.AuthorizeResponse response = NaverResponse.AuthorizeResponse.builder()
      .url(url)
      .state(state)
      .build();

    return response;
  }

  public NaverResponse.TokenResponse authorizationToken (String code, String state) throws URISyntaxException {
    NaverRequest.TokenRequest request = NaverRequest.TokenRequest.builder()
      .grant_type("authorization_code")
      .client_id(CLIENT_ID)
      .client_secret(CLIENT_SECRET)
      .code(code)
      .state(state)
      .build();

    Map<String, String> params = OBJECT_MAPPER.convertValue(request, new TypeReference<Map<String, String>>() {});

    String url = buildUrl("/token", params);

    // HTTP 통신 실행 (RestTemplate 활용)
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<NaverResponse.TokenResponse> response = restTemplate.postForEntity(
      url,
      null,
      NaverResponse.TokenResponse.class
    );

    log.info("네이버 토큰 발급 완료: {}", response.getBody().getAccess_token());

    return response.getBody();
  }

  /**
   * Naver 연동 해제 (회원 탈퇴용)
   *
   * @param accessToken
   * @return TokenResponse
   * @throws URISyntaxException
   */
  public NaverResponse.TokenResponse revokeToken(String accessToken) throws URISyntaxException {
    NaverRequest.TokenRequest request = NaverRequest.TokenRequest.builder()
      .grant_type("delete")
      .client_id(CLIENT_ID)
      .client_secret(CLIENT_SECRET)
      .access_token(accessToken)
      .service_provider("NAVER")
      .build();

    Map<String, String> params = OBJECT_MAPPER.convertValue(request, new TypeReference<Map<String, String>>() {});

    String url = buildUrl("/token", params);

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<NaverResponse.TokenResponse> response = restTemplate.postForEntity(
      url,
      null,
      NaverResponse.TokenResponse.class
    );

    log.info("네이버 연동 해제 결과: {}", response.getBody().getResult());

    return response.getBody();
  }
}
