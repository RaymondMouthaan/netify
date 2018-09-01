package org.mouthaan.netify.domain.model;

public enum Gender {
    UNKNOWN,
    FEMALE,
    MALE;

    public String value() {
        return name();
    }

    public static Gender fromValue(String v) {
        return valueOf(v);
    }
}
