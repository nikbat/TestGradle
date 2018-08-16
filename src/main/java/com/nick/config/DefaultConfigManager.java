package com.nick.config;


import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @see ConfigManagerFactory
 */
//@Service --- this is produced by the factory instead
public class DefaultConfigManager implements ConfigManager {
  public static final String NAME = "falcm";
  public static final String TAG_MODE = "mode";
  public static final String TAG_PROFILES = "profiles";
  public static final String KEYPAIR_ALGO = "RSA";
  public static final String KEYPAIR_SIGN_ALGO = "SHA256withRSA";
  public static final int KEYPAIR_SIZE = 2048;
  public static final String PROD = "prod";
  public static final String TEST = "test";
  public static final String ENCRYPTION_ALGO = "AES";     // AES defaults to AES/ECB/PKCS5Padding in Java 7+
  public static final int ENCRYPTION_KEY_SIZE = 128;
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfigManager.class);
  private static final String VERSION = "0.0.1";
  private static final String KEY_AUTHORIZED_OS_USER = "os.user";
  private static final String HOME_DIR;
  private static final String UNSPECIFIED_OS_USER = "";
  private static final String TAG_SYS_TYPE = "system.type";
  private static final String DEFAULT_SYS_TYPE;
  private static final String JVM_ID;
  private static final String PUBKEY = "pubkey";
  private static final String INVALID_CONFIG_STATE_KEY_MISMATCH = "invalid config state - key mismatch";
  private static final boolean IS_RRODUCTION;

  static {
    JVM_ID = UUID.randomUUID().toString();
    HOME_DIR = System.getProperty(NAME + ".home", System.getProperty("user.home") + "/." + NAME);
    DEFAULT_SYS_TYPE = System.getProperty(TAG_SYS_TYPE, "");
    IS_RRODUCTION = PROD.equals(calcMode(Paths.get(HOME_DIR)));
  }

  private final String mode;
  private final Path configHome;
  private final String configurationFileName;
  private final String publicKeyFileName;
  private final String privateKeyFileName;
  private JsonDocument allConfig;

  /**
   * PKI - keys.
   */
  private final KeyPair keys;
  /**
   * If true the public key in allConfig is identical to the public key on the file system.
   */
  private boolean isValidKeysForConfig;
  private boolean isDirty;
  private boolean isFirstRun;

//  @Inject
//  private AuthorizedOsUserResourceCheck authorizedOsUserResourceCheck;

  public DefaultConfigManager() {
    this(Paths.get(System.getProperty(NAME + ".cfg.home", HOME_DIR + "/cfg")));
  }

  private DefaultConfigManager(Path configHome) {
    this(calcMode(configHome), configHome);
  }

  private DefaultConfigManager(String mode, Path home) {
    JsonDocument config;

    try {
      this.mode = calcMode(mode);
      this.configHome = home;
      ensureConfigDir();

      this.publicKeyFileName = this.mode + "-pub.key";
      this.privateKeyFileName = "." + this.mode + "-priv.key";
      this.keys = ensureKeys();

      this.configurationFileName = (this.mode + "-cfg.json");
      config = ensureConfigurationFile();



    } catch (Exception e) {
      LOGGER.error("Failure during configuration", e);
      throw new IllegalStateException("Unable to process " + getConfigFile().toAbsolutePath(), e);
    }

    this.allConfig = config;
    this.isValidKeysForConfig = validate();
  }

  private static String byteArrayToHex(byte[] array) {
    StringBuilder sb = new StringBuilder(array.length * 2);
    for (byte b : array) {
      sb.append(String.format("%02x", b));
    }

    return sb.toString().toUpperCase();
  }

  public static String calcMode(Path configHome) {
    String m = System.getProperty(TAG_MODE);
    LOGGER.info("Calculating mode from {}:'{}'", TAG_MODE, m);
    if (null == m) {
      if (Files.isRegularFile(configHome.resolve(PROD + "-cfg.json"))) {
        m = PROD;
      } else {
        m = TEST;
      }
    } else {
      m = m.toLowerCase();
    }

    return m;
  }

  public String getMode() {
    return mode;
  }

  private String getConfigFileName() {
    return configurationFileName;
  }

  private String getPubKeyFileName() {
    return publicKeyFileName;
  }


  private String getPrivKeyFileName() {
    return privateKeyFileName;
  }

  private String calcMode(String m) {
    if (null == m) {
      if (Files.isRegularFile(getConfigHome().resolve(PROD + "-cfg.json"))) {
        m = PROD;
      } else {
        m = TEST;
      }
    } else {
      m = m.toLowerCase();
    }

    return m;
  }

  //  @PostConstruct
  private synchronized void load() {
    // should be a noop'
  }

  /**
   * Validate the configuration's public key against the file system public key.
   */
  private boolean validate() {
    String publicKey = allConfig.getString(PUBKEY, "");
    String publicKeyFile = Base64.encodeBase64String(keys.getPublic().getEncoded());
    boolean valid = publicKey.equals(publicKeyFile);

    if (!valid) {
      LOGGER.error(INVALID_CONFIG_STATE_KEY_MISMATCH);
    }
    return valid;
  }

  @Override
  public String toString() {
    return "DefaultConfigManager{"
        + "allConfig=" + allConfig
        + ", configHome='" + configHome + '\''
        + ", configurationFileName='" + configurationFileName + '\''
        + ", isDirty=" + isDirty
        + ", isFirstRun=" + isFirstRun
        + ", isValidKeysForConfig=" + isValidKeysForConfig
        + ", mode='" + mode + '\''
        + ", privateKeyFileName='" + privateKeyFileName + '\''
        + ", publicKeyFileName='" + publicKeyFileName + '\''
        + '}';
  }

  private JsonDocument ensureConfigurationFile()
      throws IOException {
    Path cfgFile = getConfigFile();

    JsonDocument conf;

    if (Files.isRegularFile(cfgFile)) {
      LOGGER.info("Reading configuration from {}", cfgFile);
      try (BufferedReader reader = Files.newBufferedReader(cfgFile, Charset.defaultCharset())) {
        boolean readOnly = isProductionMode() && !isFirstRun();
        conf = JsonDocument.toJsonDocument(reader, readOnly);
      }
      if (isFirstRun) {
        conf.set(PUBKEY, Base64.encodeBase64String(keys.getPublic().getEncoded()));
      }
      // TODO: we need to re-enable this.. https://jira.oraclecorp.com/jira/browse/FALCMOCI-477
//        if (isProductionMode) {
//          authorizedOsUserResourceCheck.checkFileAccess(getAuthorizedOsUser(), cfgFile);
//          authorizedOsUserResourceCheck.checkFileAccess(getAuthorizedOsUser(), privKeyFile);
//          authorizedOsUserResourceCheck.checkFileAccess(getAuthorizedOsUser(), pubKeyFile);
//        }

    } else {
      if (isProductionMode()) {
        throw new IllegalStateException("config directory expected to exist in production mode: " + cfgFile.toAbsolutePath());
      }

      conf = new JsonDocument();
      conf.set("name", NAME);
      conf.set("ver", VERSION);
      conf.set(PUBKEY, Base64.encodeBase64String(keys.getPublic().getEncoded()));
      conf.set(KEY_AUTHORIZED_OS_USER, UNSPECIFIED_OS_USER);
      conf.set(TAG_SYS_TYPE, DEFAULT_SYS_TYPE);
      LOGGER.info("Creating new configuration {}", conf);
    }

    return conf;
  }

  @Override
  public synchronized void set(String key, JsonDocument config) {
    if (isProductionMode() && !isFirstRun()) {
      throw new IllegalStateException("unable to save config in production mode");
    }

    if (null == config) {
      allConfig.set(key, (String) null);
    } else {
      allConfig.set(key, new JsonDocument(config));
    }
    isDirty = true;
  }


  @Override
  public String hashOf(String value) {
    if (null == value) {
      return null;
    }

    String hash;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(value.getBytes());
      hash = byteArrayToHex(md.digest());
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }

    return hash;
  }


  @Override
  public synchronized ConfigManager reset() {
    if (isProductionMode()) {
      throw new IllegalStateException("unable to save config in production mode");
    }

    LOGGER.debug("Resetting Configuration Manager");
    try {
        Files.deleteIfExists(getConfigFile());
        Files.deleteIfExists(getPrivateKeyFile());
        Files.deleteIfExists(getPublicKeyFile());
    } catch (IOException e) {
      throw new RuntimeException("Error resetting configuration", e);
    }

    //TODO: it seems like if the config home property is changed we will read a different config
    return new DefaultConfigManager(calcMode(System.getProperty(TAG_MODE)),
        Paths.get(System.getProperty(NAME + ".cfg.home", HOME_DIR + "/cfg")));
  }


  /**
   * Saving the configuration, side effects isDirty will be cleared as well as
   * {@link #isFirstRun} will be set to false.
   */
  @Override
  public synchronized void save() {
    LOGGER.info("Saving Configuration Manager (isDirty == {}, isFirstRun == {})", isDirty, isDirty);
    if (isProductionMode()) {
      if (isFirstRun()) {
        // only one try at this.
        setFirstRun(false);
      } else {
        throw new IllegalStateException("unable to save config in production mode");
      }
    }

    Path configFile = getConfigFile();
    try (Writer writer = Files.newBufferedWriter(configFile, Charset.defaultCharset())) {
      allConfig.write(writer, false);
    } catch (IOException e) {
      LOGGER.info("Cannot save " + configFile, e);
      throw new IllegalStateException(e);
    }

    isDirty = false;
  }


  /**
   * Will deeply interleave in the srcDoc into this json document. At leave node level, if there is any keys duplicated
   * then the srcDoc will "win".
   */
  @Override
  public void mergeOverlay(JsonDocument srcDoc, boolean encryptPasswords) {
    allConfig.mergeOverlay(srcDoc);

    if (encryptPasswords) {
      encryptPasswords(allConfig.getInternalJsonObject());
    }

    isDirty = true;
    setFirstRun(true);
    save();
  }

  private JsonObject encryptPasswords(JsonObject result) {
    for (JsonObject.Member member : result) {
      String entryKey = member.getName();
      JsonValue entryValue = member.getValue();

      if (entryValue.isObject()) {
        result.set(entryKey, encryptPasswords(entryValue.asObject()));
      } else {
        if (entryValue.isString() && (entryKey.contains("assword") || entryKey.contains("ecret"))) {
          LOGGER.trace("Encrypting {}", entryKey);
          String toEncypt = entryValue.asString();
          // assume these are at least base64 encoded
          toEncypt = new String(Base64.decodeBase64(toEncypt));
          result.set(entryKey, encrypt(toEncypt));
        }
      }
    }

    return result;
  }

  private void setPerms(Path keyFile) {
    if (FileSystems.getDefault().supportedFileAttributeViews().contains("posix")) {
      //using PosixFilePermission to set file permissions 777
      Set<PosixFilePermission> perms = new HashSet<>();

      //add owners permission
      perms.add(PosixFilePermission.OWNER_READ);

      try {
        Files.setPosixFilePermissions(keyFile, perms);
      } catch (IOException e) {
        LOGGER.error("Unable to set ownership of " + keyFile.toAbsolutePath(), e);
        throw new IllegalStateException(e);
      }
    }
  }

  @Override
  public boolean isValidState() {
    return isValidKeysForConfig;
  }

  @Override
  public boolean isDirty() {
    return isDirty;
  }

  @Override
  public String getModeName() {
    return getMode();
  }

  @Override
  public String getModeNameAbbrev() {
    assert (null != getMode());
    return getMode().substring(0, 1);
  }

  @Override
  public boolean isProductionMode() {
    return PROD.equals(mode);
  }

  public static boolean isProduction() {
    return IS_RRODUCTION;
  }

  @Override
  public String getSystemType() {
    return allConfig.getString(TAG_SYS_TYPE, DEFAULT_SYS_TYPE);
  }

  @Override
  public Set<String> getActiveProfiles() {
    String val = System.getProperty(TAG_PROFILES);
    if (null == val) {
      val = "";
    } else {
      val = val.toLowerCase();
    }

    String[] profiles = val.split(",");
    Set<String> set = new LinkedHashSet<>();
    for (String v : profiles) {
      set.add(v.trim());
    }
    return set;
  }

  @Override
  public String getAuthorizedOsUser() {
    return allConfig.getString(KEY_AUTHORIZED_OS_USER, UNSPECIFIED_OS_USER);
  }

  @Override
  public String getSystemUniqueJvmId() {
    return JVM_ID;
  }

  @Override
  public String getHomeDirectory() {
    return HOME_DIR;
  }

  @Override
  public JsonDocument get(String key) {
    return allConfig.getJsonDocument(key);
  }


  @Override
  public String encrypt(String value) {
    if (!isValidState()) {
      throw new IllegalStateException(INVALID_CONFIG_STATE_KEY_MISMATCH);
    }
    if (null == value) {
      return null;
    }

    return EncryptionProducer.encrypt(value, keys.getPublic(), null, null, null);
  }

  @Override
  public String decrypt(String value) {
    if (!isValidState()) {
      throw new IllegalStateException(INVALID_CONFIG_STATE_KEY_MISMATCH);
    }
    if (null == value) {
      return null;
    }

    return EncryptionProducer.decrypt(value, keys.getPrivate(), null, null, null);
  }

  public Path getConfigHome() {
    return configHome;
  }

  public Path getConfigFile() {
    return getConfigHome().resolve(getConfigFileName());
  }

  private Path getPrivateKeyFile() {
    return getConfigHome().resolve(getPrivKeyFileName());
  }

  private Path getPublicKeyFile() {
    return getConfigHome().resolve(getPubKeyFileName());
  }

  private void ensureConfigDir() throws IOException {
    Path cfgDir = getConfigHome();

    if (!Files.isDirectory(cfgDir)) {
      if (isProductionMode()) {
        throw new IllegalStateException("config directory expected to exist in production mode: " + cfgDir.toAbsolutePath());
      }

      Files.createDirectories(cfgDir);
    }
  }

  /**
   * Make sure that {@link #keys} and {@link #isFirstRun} is assigned accordingly.
   *
   * @throws IOException if keys are missing and cannot be created
   */
  private KeyPair ensureKeys() throws IOException {

    Path privKeyFile = getPrivateKeyFile();
    Path pubKeyFile = getPublicKeyFile();
    LOGGER.trace("Ensuring key");

    KeyPair keys;

    if (!Files.isRegularFile(privKeyFile)) {
      setFirstRun(true);

      if (Files.isRegularFile(pubKeyFile)) {
        throw new IllegalStateException("public key file exists without private key file: " + pubKeyFile.toAbsolutePath());
      }

      keys = createNewKeyPair(privKeyFile, pubKeyFile);

    } else {
      keys = readKeyPair(privKeyFile, pubKeyFile);
    }
    return keys;
  }

  /**
   * Read keys from the specified private and public key locations.
   * At completion {@link #keys} will be initialized.
   *
   * @param privKeyFile the private key {@link Path}
   * @param pubKeyFile  the private key {@link Path}
   */
  private KeyPair readKeyPair(Path privKeyFile, Path pubKeyFile) {
    try {
      LOGGER.info("Reading private key {} and public key {}",
          privKeyFile.toAbsolutePath(), pubKeyFile.toAbsolutePath());
      byte[] pub = Base64.decodeBase64(Files.readAllBytes(pubKeyFile));
      byte[] priv = Base64.decodeBase64(Files.readAllBytes(privKeyFile));

      KeyFactory keyFactory = KeyFactory.getInstance(KEYPAIR_ALGO);
      X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pub);
      PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

      PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(priv);
      PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

      return new KeyPair(publicKey, privateKey);
    } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
      LOGGER.error("Cannot read keys ", e);
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Create keys fopr the specified private and public key locations.
   * At completion {@link #keys} will be initialized. Also {@link #isFirstRun} will be set to true
   *
   * @param privKeyFile the private key {@link Path}
   * @param pubKeyFile  the private key {@link Path}
   */
  private KeyPair createNewKeyPair(Path privKeyFile, Path pubKeyFile) throws IOException {
    try {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEYPAIR_ALGO);
      kpg.initialize(KEYPAIR_SIZE);
      KeyPair keys = kpg.generateKeyPair();

      LOGGER.info("Writing private key {} public key {}", privKeyFile.toAbsolutePath(), pubKeyFile.toAbsolutePath());
      Files.write(pubKeyFile, Base64.encodeBase64(keys.getPublic().getEncoded()));
      Files.write(privKeyFile, Base64.encodeBase64(keys.getPrivate().getEncoded()));
      setPerms(privKeyFile);

      return keys;
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("Cannot generate keys", e);
      throw new IllegalArgumentException(e);
    }
  }

  private boolean isFirstRun() {
    return isFirstRun;
  }

  private void setFirstRun(boolean firstRun) {
    LOGGER.info("Setting firstRun {}", firstRun);
    isFirstRun = firstRun;
  }

}
