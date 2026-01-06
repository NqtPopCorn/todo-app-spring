package com.example.demo;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class TodoAppApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void generateJwtToken() {
		String token = generateJwtToken("demo", 1500);
		assert token != null;
	}

	private String generateJwtToken(String subject, long expiryTime) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject(subject)
				.expirationTime(new Date(System.currentTimeMillis() + expiryTime))
				.build();
		SignedJWT signedJWT = new SignedJWT(header, claimsSet);
		try {
			signedJWT.sign(new MACSigner("gJqfnoWdYaovg4ll95gZhZIWBNLlDeKm"));
			return signedJWT.serialize();
		} catch (Exception e) {
			throw new RuntimeException(e);
//			throw new ApplicationException(500, "Error signing JWT");

		}
	}

}
