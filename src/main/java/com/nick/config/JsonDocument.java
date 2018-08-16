/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
 */

package com.nick.config;

import com.eclipsesource.json.*;
import org.apache.commons.codec.binary.Base64;
import org.dizitart.no2.Document;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Intended to be a simple isolation layer to a Json document, and handling of that document.  Ideally this can be
 * an unstructured, schema-less way to handle the underpinnings of configuration, rest marshalling, persistence, etc.
 *
 * TODO: should this really exists? We can use Jackson for all of these usages
 */
public class JsonDocument implements Cloneable {

  /**
   * Not super obvious and very nasty (will probably not work in Java9+ due to more elaborate string internalization).
   */
  public static final String NULL = new String();

  private final JsonObject json;

  public JsonDocument() {
    this(new JsonObject(), Flags.BYVAL);
  }

  public JsonDocument(JsonDocument source) {
    this(source.getInternalJsonObject(), Flags.BYVAL);
  }

  public JsonDocument(String source) {
    this(Json.parse(source).asObject());
  }

  public JsonDocument(Flags flags) {
    this(new JsonObject(), flags);
  }

  // TODO: eliminate the exposure of JsonObject
  public JsonDocument(JsonObject thisIsHack) {
    this.json = thisIsHack;
  }

  private JsonDocument(JsonObject source, Flags flags) {
    this(toJsonObject(source, flags));
  }

  @Override
  public JsonDocument clone() {
    return new JsonDocument(this);
  }

  public static JsonDocument toJsonDocument(Reader reader, boolean isReadOnly) throws IOException {
    return new JsonDocument(Json.parse(reader).asObject(), isReadOnly ? Flags.READONLY : Flags.BYREF);
  }

  private static JsonObject toJsonObject(JsonObject source, Flags flags) {
    if (null == flags || Flags.BYVAL == flags) {
      JsonObject clone = new JsonObject();
      Iterator<JsonObject.Member> iter = source.iterator();
      while (iter.hasNext()) {
        JsonObject.Member member = iter.next();
        if (member.getValue().isObject()) {
          clone.add(member.getName(), toJsonObject(member.getValue().asObject(), flags));
        } else {
          clone.add(member.getName(), member.getValue());
        }
      }
      return clone;
    } else if (Flags.BYREF == flags) {
      return source;
    } else if (Flags.READONLY == flags) {
      return JsonObject.unmodifiableObject(source);
    }
    throw new IllegalStateException();
  }

  public JsonDocument unmodifiableClone() {
    return new JsonDocument(json, Flags.READONLY);
  }

  public void write(Writer writer, boolean minimal) throws IOException {
    writer.write(minimal ? toExternalizableString() : toString());
  }

  //TODO: remove all reference to this.
  public JsonObject getInternalJsonObject() {
    return json;
  }

  @Override
  public int hashCode() {
    return json.hashCode();
  }

  @Override
  public boolean equals(Object another) {
    if (!(another instanceof JsonDocument)) {
      return false;
    }

    return json.equals(((JsonDocument) another).json);
  }

  @Override
  public String toString() {
    return json.toString(WriterConfig.PRETTY_PRINT);
  }

  public String toExternalizableString() {
    return json.toString(WriterConfig.MINIMAL);
  }

  public Collection<String> names() {
    return json.names();
  }

  public JsonDocument add(String key, String value) {
    if (null != value) {
      if (NULL == value) {
        json.add(key, Json.NULL);
      } else {
        json.add(key, value);
      }
    }
    return this;
  }

  public JsonDocument add(String key, boolean value) {
    json.add(key, value);
    return this;
  }

  public JsonDocument add(String key, int value) {
    json.add(key, value);
//    json.add(key, 0.0D + value);
    return this;
  }

  public JsonDocument add(String key, long value) {
    json.add(key, value);
//    json.add(key, 0.0D + value);
    return this;
  }

  public JsonDocument add(String key, double value) {
    json.add(key, value);
    return this;
  }

  public JsonDocument add(String key, JsonDocument value) {
    if (null != value) {
      json.add(key, value.getInternalJsonObject());
    }
    return this;
  }

  // TODO:  make this private.
  public JsonDocument add(String key, JsonValue value) {
    if (null != value) {
      json.add(key, value);
    }
    return this;
  }

  public JsonDocument add(String key, DateTime value) {
    return add(key, (null == key) ? null : value.toString());
  }

  public Map<String, Object> getMap() {
    Map<String, Object> map = new LinkedHashMap<>();

    Iterator<JsonObject.Member> iter = getInternalJsonObject().iterator();
    while (iter.hasNext()) {
      JsonObject.Member member = iter.next();
      JsonValue val = member.getValue();
      if (val.isNull()) {
        map.put(member.getName(), null);
      } else {
        map.put(member.getName(), val.isString() ? val.asString() : val.toString());
      }
    }

    return map;
  }

  public <T> Map<String, T> getMap(String key, Type type) {
    if (JsonDocument.class != type && String.class != type) {
      throw new UnsupportedOperationException("type: " + type + " not supported");
    }

    JsonDocument doc = getJsonDocument(key);
    if (null == doc) {
      return null;
    }

    Map<String, T> map = new LinkedHashMap<>();
    Iterator<JsonObject.Member> iter = doc.getInternalJsonObject().iterator();
    while (iter.hasNext()) {
      JsonObject.Member member = iter.next();
      JsonValue val = member.getValue();
      if (String.class == type) {
        String str = (val.isString()) ? val.asString() : (val.isNull() ? null : val.toString());
        map.put(member.getName(), (T) str);
      } else if (JsonDocument.class == type) {
        if (val.isObject()) {
          JsonObject sub = val.asObject();
          map.put(member.getName(), (T) new JsonDocument(sub, Flags.READONLY));
        } else {
          JsonDocument subDoc = new JsonDocument();
          subDoc.add(member.getName(), val);
          map.put(member.getName(), (T) subDoc);
        }
      }
    }

    return Collections.unmodifiableMap(map);
  }

  public boolean hasKey(String key) {
    JsonValue val = json.get(key);
    return (null != val && !val.isNull());
  }

  /**
   * Will deeply interleave in the srcDoc into this json document. At leave node level, if there is any keys duplicated
   * then the srcDoc will "win".
   *
   * @see #setAll(JsonDocument)
   */
  public void mergeOverlay(JsonDocument srcDoc) {
    if (null != srcDoc) {
      mergeOverlay(getInternalJsonObject(), srcDoc.getInternalJsonObject());
    }
  }

  protected void mergeOverlay(JsonObject doc1, JsonObject doc2) {
    Iterator<JsonObject.Member> iter = doc1.iterator();
    while (iter.hasNext()) {
      JsonObject.Member member = iter.next();
      String key1 = member.getName();
      JsonValue tempEle1 = member.getValue();
      JsonValue tempEle2 = doc2.get(key1);
      if (null != tempEle1 && null != tempEle2 && !tempEle2.isNull()) {
        if (tempEle1.isObject() && tempEle2.isObject()) {
          mergeOverlay(tempEle1.asObject(), tempEle2.asObject());
        } else {
          doc1.set(key1, tempEle2);
        }
      }
    }

    iter = doc2.iterator();
    while (iter.hasNext()) {
      JsonObject.Member member = iter.next();
      String key2 = member.getName();
      JsonValue tempEle1 = doc1.get(key2);
      if (null == tempEle1) {
        JsonValue tempEle2 = member.getValue();
        doc1.set(key2, tempEle2);
      }
    }
  }

  public void performDepthFirstVistor(Visitor visitor) {
    performDepthFirstVistor(json, visitor);
  }

  private void performDepthFirstVistor(JsonObject obj, Visitor visitor) {
    Iterator<JsonObject.Member> iter = obj.iterator();
    while (iter.hasNext()) {
      JsonObject.Member member = iter.next();
      performDepthFirstVistor(member, visitor);

      JsonValue val = member.getValue();
      if (val.isObject() && !val.isNull()) {
        performDepthFirstVistor(val.asObject(), visitor);
      }
    }
  }

  protected void performDepthFirstVistor(JsonObject.Member member, Visitor visitor) {
    visitor.visit(member);
  }

  public String checksum() {
    try {
      final MessageDigest digest = MessageDigest.getInstance("MD5");

      Visitor visitor = new Visitor() {
        @Override
        public void visit(JsonObject.Member member) {
          digest.update(String.valueOf(member.hashCode()).getBytes());
        }
      };
      performDepthFirstVistor(visitor);

      byte[] digestBytes = digest.digest();
      String checksum = Base64.encodeBase64String(digestBytes);
      return checksum;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public JsonDocument toCondensed() {
    JsonDocument doc = new JsonDocument();
    condenseInto(doc.getInternalJsonObject(), getInternalJsonObject());
    return doc;
  }

  protected void condenseInto(JsonObject dest, JsonObject src) {
    Iterator<JsonObject.Member> iter = src.iterator();
    while (iter.hasNext()) {
      JsonObject.Member member = iter.next();
      String key = member.getName();
      JsonValue val = member.getValue();

      JsonValue existing = dest.get(key);
      if (null == existing || !existing.isObject()) {
        if (val.isObject()) {
          dest.set(key, new JsonDocument(val.asObject()).getInternalJsonObject());
        } else {
          dest.set(key, val);
        }
      } else {  // existing is an object...
        if (val.isObject()) {
          condenseInto(existing.asObject(), val.asObject());
        } else {
          dest.set(key, val);
        }
      }
    }
  }

  /**
   * SetAll is only handled at top level of the json, and not at lower levels of interleaving. Further, it is replacement based, so
   * the srcDoc will always "win" over on an attribute level.
   *
   * @see #mergeOverlay(JsonDocument)
   */
  public JsonDocument setAll(JsonDocument srcDoc) {
    if (null != srcDoc) {
      Iterator<JsonObject.Member> iter = srcDoc.getInternalJsonObject().iterator();
      while (iter.hasNext()) {
        JsonObject.Member member = iter.next();
        set(member.getName(), member.getValue());
      }
    }

    return this;
  }

  public static <K> Map<K, JsonDocument> clone(Map<K, JsonDocument> map) {
    if (null == map) {
      return null;
    }

    LinkedHashMap<K, JsonDocument> result = new LinkedHashMap<>();

    for (Map.Entry<K, JsonDocument> entry : map.entrySet()) {
      result.put(entry.getKey(), entry.getValue().clone());
    }

    return result;
  }

  public void remove(String key) {
    json.remove(key);
  }

  public JsonDocument set(String key, String value) {
    if (null == value) {
      // TODO: filed bug against Nitrite to fix this: https://github.com/dizitart/nitrite-database/issues/87
      if (false) {
        json.remove(key);
      } else { // work-around for Nitrite issue (see PureNitriteTest + PersistenceTest)
        Object oldVal = json.get(key);
        if (null != oldVal) {
          json.set(key, Json.NULL);
        }
      }
    } else {
      if (NULL == value) {
        json.set(key, Json.NULL);
      } else {
        json.set(key, value);
      }
    }

    return this;
  }

  public JsonDocument set(String key, boolean value) {
    json.set(key, value);
    return this;
  }

  public JsonDocument set(String key, int value) {
    json.set(key, value);
//    json.set(key, 0.0D + value);
    return this;
  }

  public JsonDocument set(String key, long value) {
    json.set(key, value);
//    json.set(key, 0.0D + value);
    return this;
  }

  public JsonDocument set(String key, double value) {
    json.set(key, value);
    return this;
  }

  public JsonDocument set(String key, DateTime value) {
    return set(key, (null == value) ? null : value.toString());
  }

  public JsonDocument set(String key, JsonDocument value) {
    if (null == value) {
      json.remove(key);
    } else {
      json.set(key, value.getInternalJsonObject());
    }
    return this;
  }

  public JsonDocument set(String key, JsonValue value) {
    if (null == value) {
      json.remove(key);
    } else {
      json.set(key, value);
    }
    return this;
  }

  public JsonDocument set(String key, Map<?, ?> value) {
    if (null == value) {
      json.remove(key);
    } else {
      JsonDocument doc = new JsonDocument();
      json.set(key, doc.getInternalJsonObject());
      for (Map.Entry<String, ?> entry : ((Map<String, ?>) value).entrySet()) {
        doc.set(entry.getKey(), entry.getValue());
      }
    }
    return this;
  }

  public JsonDocument set(String key, Collection<?> value) {
    if (null == value) {
      json.remove(key);
    } else {
      JsonArray array = new JsonArray();
      for (Object val : value) {
        if (val == null) {
          array.add((String) null);
        } else if (val instanceof Integer) {
          array.add((Integer) val);
        } else if (val instanceof Long) {
          array.add((Long) val);
        } else if (val instanceof Float) {
          array.add((Float) val);
        } else if (val instanceof Double) {
          array.add((Double) val);
        } else if (val instanceof Boolean) {
          array.add((Boolean) val);
        } else if (val instanceof JsonValue) {
          array.add((JsonValue) val);
        } else if (val instanceof Document) {
          array.add(toJson((Document) val));
        } else if (val instanceof String) {
          array.add((String) val);
        } else {
          throw new IllegalArgumentException("Doesnt support arrays of complex types " + val.getClass().getCanonicalName());
        }
      }
      json.set(key, array);
    }
    return this;
  }

  public JsonDocument set(String key, Object value) {
    if (null == value) {
      json.set(key, (String) null);
    } else if (value instanceof Boolean) {
      json.set(key, (Boolean) value);
    } else if (value instanceof String) {
      json.set(key, (String) value);
    } else if (value instanceof Long) {
      json.set(key, (Long) value);
    } else if (value instanceof Integer) {
      json.set(key, (Integer) value);
    } else if (value instanceof Double) {
      json.set(key, (Double) value);
    } else if (value instanceof Float) {
      json.set(key, (Float) value);
    } else if (value instanceof DateTime) {
      json.set(key, value.toString());
    } else if (value instanceof JsonValue) {
      json.set(key, (JsonValue) value);
    } else if (value instanceof JsonDocument) {
      json.set(key, ((JsonDocument) value).getInternalJsonObject());
    } else if (value instanceof Map) {
      set(key, (Map) value);
    } else if (value instanceof Collection) {
      set(key, (Collection) value);
    } else {
      throw new IllegalArgumentException("unknown value type for key: " + key + " of type " + value.getClass());
    }

    return this;
  }

  public JsonDocument setAll(Map<?, ?> map) {
    if (null != map) {
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        set(entry.getKey().toString(), entry.getValue());
      }
    }

    return this;
  }

  public String getString(String key, String defaultValue) {
    JsonValue val = json.get(key);

    if (null != val && val.isNull()) {
      return defaultValue;
    }

    return (null == val) ? defaultValue : (val.isString() ? val.asString() : val.toString());
  }

  public Boolean getBoolean(String key, Boolean defaultValue) {
    JsonValue val = json.get(key);

    if (null != val && val.isNull()) {
      return defaultValue;
    }

    return (null == val || val.isNull()) ? defaultValue : (Boolean) val.asBoolean();
  }

  public Integer getInt(String key, Integer defaultValue) {
    JsonValue val = json.get(key);

    if (null != val && val.isNull()) {
      return defaultValue;
    }

    return (null == val || val.isNull()) ? defaultValue : (Integer) val.asInt();
  }

  public Long getLong(String key, Long defaultValue) {
    JsonValue val = json.get(key);

    if (null != val && val.isNull()) {
      return defaultValue;
    }

    return (null == val || val.isNull()) ? defaultValue : (Long) val.asLong();
  }

  public Double getDouble(String key, Double defaultValue) {
    JsonValue val = json.get(key);

    if (null != val && val.isNull()) {
      return defaultValue;
    }

    return (null == val || val.isNull()) ? defaultValue : (Double) val.asDouble();
  }

  public DateTime getDateTime(String key, DateTime defaultValue) {
    JsonValue val = json.get(key);

    if (null != val && val.isNull()) {
      return defaultValue;
    }

    return (null == val || val.isNull()) ? defaultValue : DateTime.parse(val.asString());
  }

  /**
   * Returns a JsonValue derivative...
   */
  public Object get(String key, Object defaultValue) {
    Object val = json.get(key);

    if (val instanceof JsonValue && ((JsonValue) val).isNull()) {
      return defaultValue;
    }

    return (null == val) ? defaultValue : val;
  }

  /**
   * Will never return a JsonValue derivative...  For numerics, will convert to Double types.
   */
  public Object getObj(String key, Object defaultValue) {
    Object val = json.get(key);

    if (val instanceof JsonValue && ((JsonValue) val).isNull()) {
      return defaultValue;
    }

    if (val instanceof JsonValue) {
      if (((JsonValue)val).isString()) {
        return ((JsonValue) val).asString();
      } else if (((JsonValue)val).isNumber()) {
        return ((JsonValue)val).asDouble();
      } else if (((JsonValue)val).isBoolean()) {
        return ((JsonValue)val).asBoolean();
      }

      return val.toString();
    }

    return (null == val) ? defaultValue : val;
  }

  public JsonDocument getJsonDocument(String key) {
    if (null == key) {
      return null;
    }

    JsonValue val = json.get(key);
    return (null == val || val.isNull()) ? null : new JsonDocument(val.asObject());
  }

  public int size() {
    return json.size();
  }

  private JsonObject toJson(Document doc) {
    JsonObject obj = new JsonObject();
    for (Map.Entry<String, Object> entry : doc.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      if (null == value) {
        obj.set(key, (String) null);
      } else if (value instanceof Boolean) {
        obj.set(key, (Boolean) value);
      } else if (value instanceof String) {
        obj.set(key, (String) value);
      } else if (value instanceof Long) {
        obj.set(key, (Long) value);
      } else if (value instanceof Integer) {
        obj.set(key, (Integer) value);
      } else if (value instanceof Double) {
        obj.set(key, (Double) value);
      } else if (value instanceof Float) {
        obj.set(key, (Float) value);
      } else if (value instanceof DateTime) {
        obj.set(key, value.toString());
      } else if (value instanceof JsonValue) {
        obj.set(key, (JsonValue) value);
      } else if (value instanceof JsonDocument) {
        obj.set(key, ((JsonDocument) value).getInternalJsonObject());
      } else if (value instanceof Document) {
        set(key, toJson((Document) value));
      } else if (value instanceof Collection) {
        set(key, (Collection) value);
      } else {
        throw new IllegalArgumentException("unknown value type for key: " + key + " of type " + value.getClass());
      }
    }

    return obj;
  }

  public enum Flags {
    BYVAL,
    BYREF,
    READONLY,
  }


  public interface Visitor {
    void visit(JsonObject.Member member);
  }

}
