package com.pairoo.domain.enums;

/**
 * @author Ralf Eichinger
 */
public enum ProductDurationType {
    ONE_MONTH(1), THREE_MONTHS(3), SIX_MONTHS(6), TWELVE_MONTHS(12);

    private int monthlyRateFactor;

    private ProductDurationType(int monthlyRateFactor) {
	this.monthlyRateFactor = monthlyRateFactor;
    }

    /**
     * @return the monthly rate factor (how many months)
     */
    public int getMonthlyRateFactor() {
	return monthlyRateFactor;
    }
}
