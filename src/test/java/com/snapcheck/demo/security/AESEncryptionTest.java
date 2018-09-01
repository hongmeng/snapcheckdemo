package com.snapcheck.demo.security;

import org.junit.Before;
import org.junit.Test;

public class AESEncryptionTest {
	AESEncryption enc;
	
	@Before
	public void setUp() throws Exception {
		enc=new AESEncryption();
	}

	@Test
	public void testEncrypt() {
		String s = enc.encrypt("123456");
		assert("H/TtFyMLdHQxER1zY+KjiA==".equals(s));
	}

	@Test
	public void testDecrypt() {
		String s = enc.decrypt("H/TtFyMLdHQxER1zY+KjiA==");
		assert("123456".equals(s));
	}
	
	/*
	public static final void main(String [] arg) {
		AESEncryption enc = new AESEncryption();
		
		System.out.println(enc.encrypt("123456"));
		
		
	}
	*/
}
