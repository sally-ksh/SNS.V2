package com.sally.sns.controller.reuqest;

import com.sally.sns.model.Location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostRequest {
	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Creation {
		private String title;
		private String content;
		private PlaceInfo place;
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PlaceInfo {
		private String name;
		private double x;
		private double y;

		public Location toLocation() {

			return Location.of(name, toLongitude(), toLatitude());
		}

		public double toLongitude() {
			return this.x;
		}

		public double toLatitude() {
			return this.y;
		}
	}

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Modification {
		private String title;  // TODO validate
		private String content;
	}
}
