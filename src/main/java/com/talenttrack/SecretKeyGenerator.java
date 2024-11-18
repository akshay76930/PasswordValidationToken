package com.talenttrack;

import java.security.Key;
import java.util.Base64;

import javax.crypto.KeyGenerator;

public class SecretKeyGenerator {
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256); // Initialize with 256 bits for HS256
        Key key = keyGen.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Generated Key: " + encodedKey);
    }
}
