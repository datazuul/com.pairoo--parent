package com.pairoo.domain.enums;

public enum Importance {
    NOT_IMPORTANT(0), IMPORTANT(1), VERY_IMPORTANT(2);

    private final int value;

    private Importance(final int newValue) {
	value = newValue;
    }

    public int getValue() {
	return value;
    }
}
