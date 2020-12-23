package org.sid.sec;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
public class SecurityConstants {
	
	public static final String SECRET = "khalil@elaz.com";
	public static final long EXPIRATION_TIME = 864_000_000;//10jrs
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";

}
