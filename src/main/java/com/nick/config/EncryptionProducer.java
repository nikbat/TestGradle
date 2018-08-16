/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 */

package com.nick.config;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 */
public class EncryptionProducer {

  public static String encrypt(String plainText, PublicKey remotePubKey, PrivateKey signingKey, String salt, StringBuffer signature) {
    try {
      String secretKey = createSecretKeyAsString();
      String encryptedText = encryptTextUsingSecretKey(plainText, secretKey);
      String encryptedSecretKey = encryptKey(secretKey, remotePubKey);

      StringBuffer encoded = new StringBuffer();
      encoded.append(encryptedSecretKey);
      encoded.append(",");
      encoded.append(encryptedText);

      if (null != signature && null != signingKey) {
        signature.setLength(0);
        Signature privateSignature = Signature.getInstance(DefaultConfigManager.KEYPAIR_SIGN_ALGO);
        privateSignature.initSign(signingKey);
        privateSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
        if (null != salt) {
          privateSignature.update(salt.getBytes(StandardCharsets.UTF_8));
        }

        byte[] signatureBytes = privateSignature.sign();
        signature.append(Base64.encodeBase64String(signatureBytes));
      }

      return encoded.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String decrypt(String cipherStr, PrivateKey decryptPrivKey, PublicKey signingKey, String salt, String expectedSignature) {
    String split[] = cipherStr.split(",");
    if (2 != split.length) {
      throw new IllegalArgumentException("unknown cipherText format: " + cipherStr);
    }

    try {
      String secretKey = decryptKey(split[0], decryptPrivKey);
      String decoded = decryptText(split[1], secretKey);

      if (null != signingKey && null != expectedSignature) {
        Signature publicSignature = Signature.getInstance(DefaultConfigManager.KEYPAIR_SIGN_ALGO);
        publicSignature.initVerify(signingKey);
        publicSignature.update(decoded.getBytes(StandardCharsets.UTF_8));
        if (null != salt) {
          publicSignature.update(salt.getBytes(StandardCharsets.UTF_8));
        }

        byte[] signatureBytes = Base64.decodeBase64(expectedSignature);
        if (!publicSignature.verify(signatureBytes)) {
          throw new RuntimeException("invalid signature");
        }
      }

      return decoded;
    } catch (Exception e) {
      throw new RuntimeException(cipherStr, e);
    }
  }


  static String createSecretKeyAsString() throws Exception {
    KeyGenerator generator = KeyGenerator.getInstance(DefaultConfigManager.ENCRYPTION_ALGO);
    generator.init(DefaultConfigManager.ENCRYPTION_KEY_SIZE);
    SecretKey secKey = generator.generateKey();
    String encodedKey = Base64.encodeBase64String(secKey.getEncoded());
    return encodedKey;
  }

  static String encryptKey(String plainAESKey, PublicKey publicKey) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    return Base64.encodeBase64String(cipher.doFinal(plainAESKey.getBytes(StandardCharsets.UTF_8)));
  }

  static String encryptTextUsingSecretKey(String plainText, String keyString) throws Exception {
    byte[] decodedKey = Base64.decodeBase64(keyString);
    SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, DefaultConfigManager.ENCRYPTION_ALGO);
    Cipher cipher = Cipher.getInstance(DefaultConfigManager.ENCRYPTION_ALGO);
    cipher.init(Cipher.ENCRYPT_MODE, originalKey);
    byte[] byteCipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    return Base64.encodeBase64String(byteCipherText);
  }

  static String decryptText(String encryptedText, String keyString) throws Exception {
    byte[] decodedKey = Base64.decodeBase64(keyString);
    SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, DefaultConfigManager.ENCRYPTION_ALGO);
    Cipher cipher = Cipher.getInstance(DefaultConfigManager.ENCRYPTION_ALGO);
    cipher.init(Cipher.DECRYPT_MODE, originalKey);
    byte[] bytePlainText = cipher.doFinal(Base64.decodeBase64(encryptedText));
    return new String(bytePlainText);
  }

  static String decryptKey(String encryptedKey, PrivateKey privateKey) throws Exception {
    Cipher cipher = Cipher.getInstance(DefaultConfigManager.KEYPAIR_ALGO);
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    return new String(cipher.doFinal(Base64.decodeBase64(encryptedKey)));
  }

}
