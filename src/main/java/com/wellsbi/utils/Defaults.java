package com.wellsbi.utils;

import javax.crypto.spec.IvParameterSpec;

/**
 *
 * Just some constants.
 * 
 * @author Hrishi Dixit [hrishi@wellsbi.com]
 *
 */
public interface Defaults {
	// UTM tracking sources
	public static final String ACQUISITION = "organic";
	
	// Org ID
	public static final Long ORG = 1L; 
	
	// content mimetype
	public static final String CONTENT_TYPE = "application/json";

	// timezone
	public static final String TZ = "America/New_York";

	// AES encryption
	public static final String KEY_ENCRYPT_ALGORITHM = "AES";
	public static final String STRING_CIPHER_CRYPTO = "AES/CFB8/NoPadding";
	public static final String BYTE_CIPHER_CRYPTO = "AES/CBC/PKCS5Padding";
	public static final String IV_SPEC_STRING = "B4EA0AD5E78552E2";
	public static final IvParameterSpec IV_SPEC = new IvParameterSpec(IV_SPEC_STRING.getBytes());

	// encoding
	public static final String CHARSET = "UTF-8";
	
	// country
	public static final String COUNTRY = "USA";
	
	// storage mechamism
	public static final String STORAGE = "S3";
	
	// file content type
	public static final String MIME_TYPE = "application/octet-stream";
	public static final String EXTENSION = "bin";
	
	// cache settings
	public static final Integer CACHE_TIMEOUT = 30 * 60; // 30 minutes 
	
	// email settings
	public static final String MAIL_FROM = "The Wellsbi Team <team@wellsbi.com>";
	
	// post-login url
	public static final String POST_LOGIN_URL = "healthboard";
	
	// org defaults
	public static final String ORG_MEMBERSHIP_LEVEL = "BASIC";
	public static final String ORG_TYPE = "CUSTOMER";
}

