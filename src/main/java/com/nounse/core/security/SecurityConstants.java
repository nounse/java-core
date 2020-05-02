package com.nounse.core.security;

public class SecurityConstants {
     public static final long EXPIRATION_TIME = 864_000_000; // 10 days;
     public static final String SECRET = "MY-JWT-SECRET";
     public static final String TOKEN_PREFIX = "Bearer ";
     public static final String AUTHORIZATION_HEADER = "Authorization";
     public static final String ROLE_CLAIM_KEY = "nounse.r";

     public class Schemes {
          public static final String JWT = "bearer-jwt";
     }
}

