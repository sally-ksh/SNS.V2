package com.sally.sns.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoApiResponse {
	private MetaDto meta;
	private List<DocumentDto> documents;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class MetaDto {
		@JsonProperty("total_count")
		private int totalCount;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class DocumentDto {
		@JsonProperty("address_name")
		private String addressName;
		@JsonProperty("x")
		private double longitude;
		@JsonProperty("y")
		private double latitude;
	}
}
