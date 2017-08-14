package com.vther.zookeeper.demo.simple;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;

public class CreateAuthString {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // echo -n wither:123456 | openssl dgst -binary -sha1 | openssl base64
        System.out.println(DigestAuthenticationProvider.generateDigest("wither:123456"));
    }
}
