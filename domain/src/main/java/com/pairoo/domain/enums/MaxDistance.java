package com.pairoo.domain.enums;

/**
 * @author Ralf Eichinger
 */
public enum MaxDistance {
	ANY(null), UNITS_20(20), UNITS_50(50), UNITS_100(100), UNITS_250(250);

	private Integer distanceUnits;

	private MaxDistance(Integer distanceUnits) {
		this.distanceUnits = distanceUnits;
	}

	/**
	 * @return the distance units, e.g. 20 of unit "km" (depends on user's preferred locale)
	 */
	public Integer getDistanceUnits() {
		return distanceUnits;
	}
}
