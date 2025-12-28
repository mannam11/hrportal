package com.app.hrportal.utils;

import java.security.SecureRandom;

public class OtpGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static int generate() {
        return secureRandom.nextInt(900_000) + 100_000;
    }

}

