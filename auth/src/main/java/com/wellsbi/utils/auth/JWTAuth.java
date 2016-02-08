package com.wellsbi.utils.auth;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;


public class JWTAuth {
	
	private JWTAuth() {}
	
	public static String signJwtClaims(PrivateKey rsaPrivateKey, JwtClaims claims) {
		
	    JsonWebSignature jws = new JsonWebSignature();

	    // The payload of the JWS is JSON content of the JWT Claims
	    jws.setPayload(claims.toJson());

	    // The JWT is signed using the private key
	    jws.setKey(rsaPrivateKey);
	    
	    // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
	    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);

	    // Sign the JWS and produce the compact serialization or the complete JWT/JWS
	    // representation, which is a string consisting of three dot ('.') separated
	    // base64url-encoded parts in the form Header.Payload.Signature
	    // If you wanted to encrypt it, you can simply set this jwt as the payload
	    // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
	    return serialize(jws);
	}
	
	public static boolean validateJwtSignature(PublicKey rsaPublicKey, String jwt) {

	    JsonWebSignature jsonWebSignature = createJsonWebSignature(jwt);
	    
	    jsonWebSignature.setKey(rsaPublicKey);

	    // Check the signature
		try {
			return jsonWebSignature.verifySignature();
		} catch (JoseException e) {
			throw new IllegalStateException("Could not validate signature" , e);
		}
	}

	private static JsonWebSignature createJsonWebSignature(String jwt) {
		JsonWebSignature jsonWebSignature = new JsonWebSignature();
		try {
			jsonWebSignature.setCompactSerialization(jwt);
		} catch (JoseException e) {
			throw new IllegalArgumentException("Could not set JWT string as compact serialization" , e);
		}
		return jsonWebSignature;
	}

	private static String serialize(JsonWebSignature jws) {
		try {
			return jws.getCompactSerialization();
		} catch (JoseException e) {
			throw new IllegalStateException("Could not serialize JWT claim" , e);
		}
	}
	
	public static String getIssuerForCompressedJwt(String jwt) {
		String payload = decodePayload(jwt);
		return getIssuer(payload);
	}
	
	private static String decodePayload(String jwt) {
		return new String(Base64.getDecoder().decode(jwt.split("\\.")[1]));
	}

	private static String getIssuer(String payload) {
		try {
			return JwtClaims.parse(payload).getIssuer();
		} catch (MalformedClaimException | InvalidJwtException e) {
			throw new IllegalArgumentException("Could not extract issuer from jwt", e);
		}
	}

	
}
