package com.pairoo.domain.enums;

/**
 * The 12 star signs (Zodiac)
 * @author Ralf Eichinger
 */
public enum Starsign {
	// ordered ascending by end date
	CAPRICORNUS(22, 12, 20, 1), AQUARIUS(21, 1, 19, 2), PISCES(20, 2, 20, 3), ARIES(21, 3, 20, 4), TAURUS(21, 4, 21, 5), GEMINI(
			22, 5, 21, 6), CANCER(22, 6, 23, 7), LEO(24, 7, 23, 8), VIRGO(24, 8, 23, 9), LIBRA(24, 9, 23, 10), SCORPIO(
			24, 10, 22, 11), SAGITTARIUS(23, 11, 21, 12);

	private int endDayofMonth;
	private int endMonth;
	private int startDayofMonth;
	private int startMonth;

	private Starsign(int startDayofMonth, int startMonth, int endDayOfMonth, int endMonth) {
		this.startDayofMonth = startDayofMonth;
		this.startMonth = startMonth;
		this.endDayofMonth = endDayOfMonth;
		this.endMonth = endMonth;
	}

	/**
	 * @return the endDayofMonth
	 */
	public int getEndDayofMonth() {
		return endDayofMonth;
	}

	/**
	 * @param endDayofMonth the endDayofMonth to set
	 */
	public void setEndDayofMonth(int endDayofMonth) {
		this.endDayofMonth = endDayofMonth;
	}

	/**
	 * @return the endMonth
	 */
	public int getEndMonth() {
		return endMonth;
	}

	/**
	 * @param endMonth the endMonth to set
	 */
	public void setEndMonth(int endMonth) {
		this.endMonth = endMonth;
	}

	/**
	 * @return the startDayofMonth
	 */
	public int getStartDayofMonth() {
		return startDayofMonth;
	}

	/**
	 * @param startDayofMonth the startDayofMonth to set
	 */
	public void setStartDayofMonth(int startDayofMonth) {
		this.startDayofMonth = startDayofMonth;
	}

	/**
	 * @return the startMonth
	 */
	public int getStartMonth() {
		return startMonth;
	}

	/**
	 * @param startMonth the startMonth to set
	 */
	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}

}
