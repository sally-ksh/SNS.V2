package com.sally.sns.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
	private String addressName;
	private double longitude; // x
	private double latitude; // y

	public static Location of(String addressName, double longitude, double latitude) {
		return new Location(addressName, longitude, latitude);
	}
}
