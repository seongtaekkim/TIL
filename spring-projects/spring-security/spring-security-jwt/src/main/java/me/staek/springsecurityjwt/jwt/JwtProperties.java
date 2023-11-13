package me.staek.springsecurityjwt.jwt;

public class JwtProperties {
	public static final String SECRET = "staek"; // secret
	public static final int EXPIRATION_TIME = 86400000; // 1일 (1/1000초)
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
}
