package com.eorion.bo.enhancement.delegation.domain.constants;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum DelegationSortByConstant {

    START_DATE_TIME("startDateTime", "START_DATE_TS");

    private static final Map<String, DelegationSortByConstant> BY_VALUE = new HashMap<>();

    static {
        for (DelegationSortByConstant e : values()) {
            BY_VALUE.put(e.key, e);
        }
    }

    private final String key;
    private final String value;

    DelegationSortByConstant(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static DelegationSortByConstant from(String s) {
        return BY_VALUE.get(s);
    }
}
