/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 */

package com.nick.config;


import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class ConfigHelper {

  static boolean HANDLE_EXCEPTION = true;

  /*
  public static void main(String args[]) {
    // use the locator to find the ConfigManager...
    final ServiceLocator locator = Hk2Util.createAndPopulateServiceLocator();
    final ConfigManager cfgMgr = locator.getService(ConfigManager.class);
    //assert(null != cfgMgr && !cfgMgr.isProductionMode());

    Option help = new Option("help", "Used to either -gen, -inject, or -merge configuration related elements");
    Option gen = Option.builder("gen")
        .longOpt("gen")
        .desc("generate sample configuration from all " + ConfiguredServiceProvider.class.getSimpleName() + "'s to stdout").build();
    Option get = Option.builder("get")
        .longOpt("get")
        .desc("fetch a value from the existing " + cfgMgr.getHomeDirectory() + " configuration").build();
    Option inject = Option.builder("inject")
        .longOpt("inject")
        .desc("inject a value into the existing " + cfgMgr.getHomeDirectory() + " configuration").build();
    Option decrypt = Option.builder("dec")
        .longOpt("dec")
        .desc("(used with -get) decrypt the value being fetched").build();
    Option encrypt = Option.builder("enc")
        .longOpt("enc")
        .desc("(used with -inject) encrypt the value being injected").build();
    Option subsystem  = Option.builder("subsystem")
        .longOpt("subsystem")
        .hasArg(true).numberOfArgs(1)
        .desc("(used with -inject) the subsystem module name containing the injectable value").build();
    Option attribute = Option.builder("attrpath")
        .longOpt("attrpath")
        .hasArg(true).numberOfArgs(1)
        .desc("(used with -inject) the attribute path, in the form of x/y/z, for the injectable value").build();
    Option value = Option.builder("value")
        .longOpt("value")
        .hasArg(true).numberOfArgs(1)
        .desc("(used with -inject) the attribute value to set; if not passed then value will be collected from console input").build();
    Option merge = Option.builder("merge")
        .longOpt("merge")
        .desc("merge/overlay content taking -srcJsonFile, overlaying with -overlayJsonFile, and writing to -outputJsonFile").build();
    Option encryptPasswords = Option.builder("encPasswords")
        .longOpt("encPasswords")
        .desc("(used with -merge) encrypt all 'asswords' and 'ecret' being injected").build();
    Option overlayJsonFile = Option.builder("overlayJsonFile")
        .longOpt("overlayJsonFile")
        .hasArg(true).numberOfArgs(1)
        .desc("(used with -merge) the overlay json file to use that will be applied and overlayed over the srcJsonFile").build();

    Options options = new Options();
    options.addOption(help);
    options.addOption(gen);
    options.addOption(get);
    options.addOption(inject);
    options.addOption(subsystem);
    options.addOption(attribute);
    options.addOption(encrypt);
    options.addOption(decrypt);
    options.addOption(encryptPasswords);
    options.addOption(value);
    options.addOption(merge);
    options.addOption(overlayJsonFile);

    // create the parser
    CommandLineParser parser = new DefaultParser();
    try {
      // parse the command line arguments
      CommandLine line = parser.parse(options, args);

      if (line.hasOption("gen")) {
        generateSampleConfigurations(locator);
        return;
      } else if (line.hasOption("inject")) {
        String subsystemName = line.getOptionValue("subsystem");
        String attributeName = line.getOptionValue("attrpath");
        boolean enc = line.hasOption("enc");
        if (null == subsystemName || null == attributeName) {
          throw new IllegalArgumentException("subsystem and attrpath are required arguments");
        }

        String toBeSet = line.getOptionValue("value");
        if (null == toBeSet) {
          Console console = System.console();
          if (null == console) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            toBeSet = reader.readLine();
          } else {
            char[] input = console.readPassword("Value to inject into [" + subsystemName + ":" + attributeName + "]:");
            toBeSet = new String(input);
          }
        }

        if (null == toBeSet || toBeSet.trim().isEmpty()) {
          throw new IllegalArgumentException("attribute value to be injected can't be empty");
        }

        if (enc) {
          toBeSet = cfgMgr.encrypt(toBeSet);
          assert(null != toBeSet);
        }

        JsonDocument cfg = cfgMgr.get(subsystemName);
        if (null == cfg) {
          cfg = new JsonDocument();
        }
        makeSubDocument(cfg, attributeName, toBeSet);
        cfgMgr.set(subsystemName, cfg);
        cfgMgr.save();
        assert(!cfgMgr.isDirty());

        System.out.println("configuration attribute injected into: " + cfgMgr.getHomeDirectory());
      } else if (line.hasOption("get")) {
        String subsystemName = line.getOptionValue("subsystem");
        String attributeName = line.getOptionValue("attrpath");
        boolean dec = line.hasOption("dec");
        if (null == subsystemName || null == attributeName) {
          throw new IllegalArgumentException("subsystem and attrpath are required arguments");
        }

        JsonDocument cfg = cfgMgr.get(subsystemName);
        if (null == cfg) {
          System.out.println("nil");
        } else {
          String val = cfg.getString(attributeName, null);
          if (null == val) {
            System.out.println("nil");
          } else {
            if (dec) {
              val = cfgMgr.decrypt(val);
            }
            System.out.print(val);
          }
        }
      } else if (line.hasOption("merge")) {
        String overlay = line.getOptionValue("overlayJsonFile");

        File overlayFile = new File(overlay);

        merge(cfgMgr, overlayFile, line.hasOption("encPasswords"));

        System.out.println("config files have been merged and written");
      } else {
        throw new IllegalArgumentException("either -gen or -inject or -merge are required to be passed");
      }
    } catch (Exception exp) {
      System.err.println("Parsing failed. Re: " + exp.getMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp( ConfigHelper.class.getName(), options );
      if (!HANDLE_EXCEPTION) {
        throw (exp instanceof RuntimeException) ? (RuntimeException)exp : new RuntimeException(exp);
      }
    }
  }

  public static void merge( ConfigManager configManager, File overlayFile, boolean encrypt) throws IOException {

    JsonDocument overlayDoc = load(overlayFile);

    configManager.mergeOverlay(overlayDoc, encrypt);
  }

  private static JsonDocument load(File file) throws IOException {
    try (FileReader reader = new FileReader(file)) {
      return JsonDocument.toJsonDocument(reader, true);
    }
  }


  public static void makeSubDocument(JsonDocument cfg, String attributeName, String encValue) {
    String split[] = attributeName.split("/");
    int i = 0;
    while (i < split.length - 1) {
      JsonDocument subCfg = cfg.getJsonDocument(split[i]);
      if (null == subCfg) {
        subCfg = new JsonDocument();
        cfg.set(split[i], subCfg);
      }
      cfg = subCfg;
      i++;
    }
    cfg.set(split[i], encValue);
  }

  public static void generateSampleConfigurations(ServiceLocator locator) {
    final List<ConfiguredServiceProvider> providers = Hk2Util.safe_getAllServices(ConfiguredServiceProvider.class);

    JsonDocument cfg = new JsonDocument();

    HashSet<String> modulesSeen = new HashSet<>();
    for (ConfiguredServiceProvider csp : providers) {
      Collection<String> names = csp.getSubsystemNames();
      if (null != names) {
        for (String name : names) {
          if (!modulesSeen.add(name)) {
            throw new IllegalStateException("two services support this configuration subsystem name: " + name + "; " + csp + " and another...");
          }

          JsonDocument subCfg = csp.generateSampleConfiguration(name);
          if (null != subCfg) {
            cfg.set(name, subCfg);
          }
        }
      }
    }

    System.out.println(cfg);
  }
  */

}
