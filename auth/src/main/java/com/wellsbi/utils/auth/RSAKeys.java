package com.wellsbi.utils.auth;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.IOUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class RSAKeys {
	
	private static Cache<String, PublicKey> publicKeys = CacheBuilder.newBuilder().maximumSize(100).build();
	private static Cache<String, PrivateKey> privateKeys = CacheBuilder.newBuilder().maximumSize(100).build();

	private RSAKeys() {}
	
	public static PrivateKey loadPrivateKey(String filePath) {
		try {
			return privateKeys.get(filePath, new Callable<PrivateKey>() {
				@Override
				public PrivateKey call() throws Exception {
				    byte[] keyBytes = readKeyBytes(getStream(filePath));
				    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
				    KeyFactory keyFactory = getRSAKeyFactory();
				    return generatePrivate(spec, keyFactory);
				}
			});
		} catch (ExecutionException e) {
			throw new IllegalStateException("Could not retrieve private key", e);
		}
		
	}
	
	public static PublicKey loadPublicKey(String filePath) {
		try {
			return publicKeys.get(filePath, new Callable<PublicKey>() {

				@Override
				public PublicKey call() throws Exception {
				    byte[] keyBytes = readKeyBytes(getStream(filePath));
				    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
				    KeyFactory keyFactory = getRSAKeyFactory();
				    return generatePublic(spec, keyFactory);
				}
				
			});
		} catch (ExecutionException e) {
			throw new IllegalStateException("Could not retrieve public key", e);
		}
	  }
	
	private static InputStream getStream(String filePath) {
		return RSAKeys.class.getResourceAsStream(filePath);
	}

	private static PublicKey generatePublic(X509EncodedKeySpec spec, KeyFactory keyFactory) {
		try {
			return keyFactory.generatePublic(spec);
		} catch (InvalidKeySpecException e) {
			throw new IllegalStateException("Could not generate public key from public key file", e);
		}
	}

	private static PrivateKey generatePrivate(PKCS8EncodedKeySpec spec, KeyFactory keyFactory) {
		try {
			return keyFactory.generatePrivate(spec);
		} catch (InvalidKeySpecException e) {
			throw new IllegalStateException("Could not generate private key from private key file", e);
		}
	}

	private static KeyFactory getRSAKeyFactory() {
		try {
			return KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Error creating an RSA key factory.", e);
		}
		
	}

	private static byte[] readKeyBytes(InputStream is)  {
		try {
			return IOUtils.toByteArray(is);
		} catch (IOException e) {
			throw new IllegalStateException("Could not read file" ,e);
		}
	}
	
}
