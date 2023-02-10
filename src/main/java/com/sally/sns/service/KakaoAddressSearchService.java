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
	private final RestTemplate restTemplate;
	private final KakaoRestApiProperty kakaoRestApiProperty;
	private static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL = "https://dapi.kakao.com/v2/local/search/address.json";

	public KakaoApiResponse requestAddressSearch(String address) {
		if (ObjectUtils.isEmpty(address)) {
			throw new RuntimeException("[KakaoAddressSearchService] No address parameter.");
		}
		System.out.println(address);
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_LOCAL_SEARCH_ADDRESS_URL);
		uriBuilder.queryParam("query", address);
		URI uri = uriBuilder.build().encode().toUri();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiProperty.getRestApiKey());
		HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
		return restTemplate
			.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponse.class)
			.getBody();
	}
}
