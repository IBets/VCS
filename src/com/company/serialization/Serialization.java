package com.company.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

public class Serialization {

    public static <T> T deserialize(byte[] raw) {
        var properties = new Properties();
        try (var inputStream = new ByteArrayInputStream(raw)) {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T)Converter.fromProperties(properties);
    }

    public static <T> byte[] serialize(T obj) {
        try (var outputStream = new ByteArrayOutputStream()) {
            var properties = Converter.toProperties(obj);
            properties.store(outputStream, null);
            return outputStream.toByteArray();
        } catch (Exception e) {
           e.printStackTrace();
           return null;
        }
    }
}

