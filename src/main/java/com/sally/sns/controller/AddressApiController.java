package com.sally.sns.controller;

import com.sally.sns.controller.response.KakaoApiResponse;
import com.sally.sns.controller.response.Response;
import com.sally.sns.service.KakaoAddressSearchService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/sns/addresses")
public class AddressApiController {
	private final KakaoAddressSearchService kakaoAddressSearchService;

	@GetMapping
	public Response<KakaoApiResponse> search(@RequestParam("address") String address,
		Authentication authentication) {
		KakaoApiResponse coordinates = kakaoAddressSearchService.requestAddressSearch(address);
		return Response.success(coordinates);
	}

}
