package com.sally.sns.service;

import com.sally.sns.configuration.KakaoRestApiProperty;
import com.sally.sns.controller.response.KakaoApiResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoAddressSearchService {
	private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";
	public static final String KAKAO_LOCAL_SEARCH_QUERY_KEY = "query";
	public static final String KAKAO_REST_API_KEY = "KakaoAK ";
	private final RestTemplate restTemplate;
	private final KakaoRestApiProperty kakaoRestApiProperty;

	public KakaoApiResponse requestAddressSearch(String address) {
		URI uri = getUri(address);
		var httpEntity = getHttpEntity(KAKAO_REST_API_KEY + kakaoRestApiProperty.getRestApiKey());  // 분리하기 쉬운 구조
		return restTemplate
			.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponse.class)
			.getBody();
	}

	private URI getUri(String address) {
		if (ObjectUtils.isEmpty(address)) {
			throw new RuntimeException("[KakaoAddressSearchService] No address parameter.");
		}
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL);
		uriBuilder.queryParam(KAKAO_LOCAL_SEARCH_QUERY_KEY, address);
		URI uri = uriBuilder.build().encode().toUri();
		return uri;
	}

	private HttpEntity<Object> getHttpEntity(String restApiKey) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.AUTHORIZATION, restApiKey);
		HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
		return httpEntity;
	}
}
