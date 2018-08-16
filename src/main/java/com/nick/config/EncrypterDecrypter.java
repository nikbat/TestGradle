/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 */

package com.nick.config;



public interface EncrypterDecrypter {

  /**
   * Provides a means to encrypt an attribute value that needs to be securely associated with a configuration document.
   *
   * @param value the value to encrypt
   */
  String encrypt(String value);

  /**
   * Provides a means to decrypt an attribute value that was previously encrypted.
   *
   * @param value the value to decrypt
   */
  String decrypt(String value);

  /**
   * Produces hashes.
   */
  String hashOf(String value);

}
