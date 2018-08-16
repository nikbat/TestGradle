/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 */

package com.nick.config;

import oracle.fa.lcm.model.JsonDocument;
import org.jvnet.hk2.annotations.Contract;

import java.util.Set;

/**
 * Provides configuration management to an arbitrary set of subsystems in the runtime.
 */
@Contract
public interface ConfigManager extends EncrypterDecrypter {

  /**
   * Returns the configuration for a named subsystem.
   *
   * @param subsystemName the name of the subsystem
   */
  JsonDocument get(String subsystemName);

  /**
   * Establishes the configuration for a named subsystem.
   *
   * @param subsystemName the name of the subsystem
   * @param config        the configuration for the subsystem
   * @throws RuntimeException if running in a production environment, where config is read-only and not settable.
   */
  void set(String subsystemName, JsonDocument config);

  /**
   * Apply a set of overlay changes to the root configuration.
   */
  void mergeOverlay(JsonDocument overlay, boolean encryptPassword);

  /**
   * Reloads the configuration from external storage.
   */
  ConfigManager reset();

  /**
   * Saves the configuration to external storage. Only available if not in production mode.
   *
   * @throws RuntimeException if running in a production environment, where config is read-only and not settable.
   */
  void save();

  /**
   * Provides an indication whether the configuration subsystem is in a valid state.  Some operations may fail (e.g.,
   * saving encrypted properties) when the config subsystem is not in a valid state.
   */
  boolean isValidState();

  /**
   * Provides an indication whether the configuration subsystem is dirty and needs to be saved.  Can only be true if
   * not in production mode.
   */
  boolean isDirty();

  /**
   * Returns the name of the runtime mode that is currently set for this [JVM] instance, typically "prod" or "test"
   */
  String getModeName();

  /**
   * Returns the first character for the {@link #getModeName()}.
   */
  String getModeNameAbbrev();

  /**
   * Returns true if the current mode is the production mode.
   */
  boolean isProductionMode();

  /**
   * Always non-null, but might be empty (e.g., fassm or fapodm).
   */
  String getSystemType();

  /**
   * The name of the active profiles.
   */
  Set<String> getActiveProfiles();

  /**
   * A unique id that represents this runtime process (not the pid though).
   */
  String getSystemUniqueJvmId();

  /**
   * Returns the home directory for where configuration, and other files are kept on the fs.
   */
  String getHomeDirectory();

  /**
   * Returns the authorized OS user, empty if no OS user is specified.
   */
  String getAuthorizedOsUser();

}
