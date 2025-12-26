package com.app.hrportal.enums;

import lombok.Getter;

@Getter
public enum ApplicationSource {

    LINKEDIN("linkedin"),
    REFERRAL("referral"),
    COMPANY_WEBSITE("company_website"),
    INDEED("indeed"),
    TWITTER("twitter"),
    OTHER("other");

    private final String value;

    ApplicationSource(String value) {
        this.value = value;
    }

    public static ApplicationSource fromValue(String value) {
        for (ApplicationSource source : values()) {
            if (source.value.equalsIgnoreCase(value)) {
                return source;
            }
        }
        return OTHER;
    }
}

