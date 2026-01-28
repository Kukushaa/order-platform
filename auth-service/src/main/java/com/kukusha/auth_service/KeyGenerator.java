package com.kukusha.auth_service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair keyPair = kpg.generateKeyPair();

        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        String privateKeyPEM = Base64.getEncoder().encodeToString(privateKeyBytes);
        System.out.println("Private Key:");

        System.out.println("-----BEGIN PRIVATE KEY-----");
        System.out.println(privateKeyPEM);
        System.out.println("-----END PRIVATE KEY-----");

        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        String publicKeyPEM = Base64.getEncoder().encodeToString(publicKeyBytes);
        System.out.println("\nPublic Key:");

        System.out.println("-----BEGIN PUBLIC KEY-----");
        System.out.println(publicKeyPEM);
        System.out.println("-----END PUBLIC KEY-----");
    }
}
