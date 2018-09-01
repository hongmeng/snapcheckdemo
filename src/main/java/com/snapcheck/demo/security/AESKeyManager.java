package com.snapcheck.demo.security;

import org.springframework.stereotype.Component;

@Component
public class AESKeyManager {
	
	/* This should never be used in production as the key itself is not protected */
	public static String getKey() {
		return "SnapCheck!";
	}
}
