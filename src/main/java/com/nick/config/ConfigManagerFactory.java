/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 */
package com.nick.config;

import org.glassfish.hk2.api.Factory;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Singleton;

@Service
@Singleton
public class ConfigManagerFactory implements Factory<ConfigManager> {

  private static final DefaultConfigManager defaultConfigManager = new DefaultConfigManager();

  public static ConfigManager getInstance() {
    return defaultConfigManager;
  }

  @Override
  public ConfigManager provide() {
    return defaultConfigManager;
  }

  @Override
  public void dispose(ConfigManager instance) {
    // NOP
  }

}
