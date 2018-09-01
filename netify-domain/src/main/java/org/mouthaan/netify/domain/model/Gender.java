package org.mouthaan.netify.domain.model;

/**
 * Created by in482mou on 29-7-2017.
 */
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
