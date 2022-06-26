package com.pmart5a.enums;

public enum ParametersInt {
    NUMBER_SERVICE_THREADS(2),
    MIN_CHARACTER_CODE(48),
    MAX_CHARACTER_CODE(57);
    private int value;

    ParametersInt(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}