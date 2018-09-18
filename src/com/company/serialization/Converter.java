package com.company.serialization;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;


class IDKey {
    private Object m_key;
    IDKey(Object key){
        m_key = key;
    }
    public Object getKey(){
        return m_key;
    }
    public boolean equals(Object obj) {
        return (obj instanceof IDKey) && ((IDKey) obj).m_key == m_key;
    }
    public int hashCode() {
        return System.identityHashCode(m_key);
    }
}

class Encoder {
    interface Function<T1, T2, T3> {
        T1 compute(T2 x, T3 y) throws IllegalAccessException;
    }
    private static Map<Class<?>, Function<String, Field, Object>> readers = new HashMap<>();
    static {
        readers.put(Integer.TYPE, (field, instance) -> String.valueOf(field.getInt(instance)));
        readers.put(Character.TYPE, (field, instance) -> String.valueOf(field.getChar(instance)));
        readers.put(Byte.TYPE, (field, instance) -> String.valueOf(field.getByte(instance)));
        readers.put(Short.TYPE, (field, instance) -> String.valueOf(field.getShort(instance)));
        readers.put(Long.TYPE, (field, instance) -> String.valueOf(field.getLong(instance)));
        readers.put(Boolean.TYPE, (field, instance) -> String.valueOf(field.getBoolean(instance)));
        readers.put(Double.TYPE, (field, instance) -> String.valueOf(field.getDouble(instance)));
        readers.put(Float.TYPE, (field, instance) -> String.valueOf(field.getFloat(instance)));
    }

    private Properties m_data = new Properties();
    private Map<IDKey, String> m_objects = new HashMap<>();
    private int m_classCount = 0;
    private int m_arrayCount = 0;

    Properties toProperties(Object obj) {
        m_data.put("mainObjectID", String.valueOf(storeObject(obj)));
        return m_data;
    }

    private String storeObject(Object obj) {
        if (obj == null) return null;
        else if (obj.getClass().equals(String.class)) return (String) obj;
        else if (obj.getClass().isArray()) return storeArrayImpl(obj);
        else return storeObjectImpl(obj);
    }

    private String storeObjectImpl(Object obj)  {
        if (obj == null) return null;
        var key = new IDKey(obj);
        var objID = m_objects.get(key);
        if (objID != null) return objID;

        m_classCount++;
        var result = "o" + m_classCount;

        m_objects.put(key, result);

        var keyPrefix   = result    + ".";
        var fieldPrefix = keyPrefix + "f.";

        var clazz = obj.getClass();
        m_data.put(keyPrefix + "className", clazz.getName());

        var fieldsList = Converter.buildFieldsList(clazz);
        for (var field : fieldsList) {
            var fieldType = field.getType();
            var fieldName = field.getName();
            var fieldValue = "";
            try {
                field.setAccessible(true);
                var reader = readers.get(fieldType);
                if (reader != null) fieldValue = reader.compute(field, obj);
                else fieldValue = storeObject(field.get(obj));
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
                fieldValue = null;
            }
            if (fieldValue != null) m_data.setProperty(fieldPrefix+fieldName, String.valueOf(fieldValue));
            else m_data.setProperty(fieldPrefix + fieldName + ".isNull", "true");
        }
        return result;
    }

    private String storeArrayImpl(Object obj) {
        if (obj == null) return null;
        var key = new IDKey(obj);
        var objID = m_objects.get(key);
        if (objID != null) return objID;

        m_arrayCount++;
        var result = "a" + m_arrayCount;

        m_objects.put(key, result);

        var clazz = obj.getClass();
        var fieldType = clazz.getComponentType();

        var keyPrefix   = result    + ".";
        var fieldPrefix = keyPrefix + "f.";

        var length = Array.getLength(obj);
        m_data.put(keyPrefix + "className", fieldType.getName());
        m_data.put(keyPrefix + "length", String.valueOf(length));

        var fieldValue = "";
        for (int index = 0; index < length; index++)  {
            if (fieldType.equals(Integer.TYPE)) fieldValue = String.valueOf(Array.getInt(obj, index));
            else if (fieldType.equals(Character.TYPE)) fieldValue = String.valueOf(Array.getChar(obj, index));
            else if (fieldType.equals(Byte.TYPE)) fieldValue = String.valueOf(Array.getByte(obj, index));
            else if (fieldType.equals(Short.TYPE)) fieldValue = String.valueOf(Array.getShort(obj, index));
            else if (fieldType.equals(Long.TYPE)) fieldValue = String.valueOf(Array.getLong(obj, index));
            else if (fieldType.equals(Boolean.TYPE)) fieldValue = String.valueOf(Array.getBoolean(obj, index));
            else if (fieldType.equals(Double.TYPE)) fieldValue = String.valueOf(Array.getDouble(obj, index));
            else if (fieldType.equals(Float.TYPE)) fieldValue = String.valueOf(Array.getFloat(obj, index));
            else fieldValue = storeObject(Array.get(obj, index));

            var fieldName = "" + index;
            if (fieldValue != null) m_data.setProperty(fieldPrefix + fieldName, String.valueOf(fieldValue));
            else m_data.setProperty(fieldPrefix + fieldName + ".isNull", "true");
        }
        return result;
    }
}

class Decoder{

    private Properties m_data;
    private Map<String, Object> m_objects = new HashMap<>();

    Object fromProperties(Properties prop){
        m_data = prop;
        return restoreObject((String) m_data.get("mainObjectID"));
    }

    private Object restoreObject(String value){

        if (value == null) return null;
        else if (value.startsWith("a")) return restoreArrayImpl(value);
        else if (value.startsWith("o")) return restoreObjectImpl(value);
        else return value;
    }

    private Object restoreObjectImpl(String id){
        if (id == null || id.equals("o0"))
            return null;

        var result = m_objects.get(id);
        if (result == null) {
            try {
                var keyPrefix   = id        + ".";
                var fieldPrefix = keyPrefix + "f.";

                var classType = (String) m_data.get(keyPrefix + "className");

                var clazz = Class.forName(classType);
                var constr = clazz.getDeclaredConstructor();

                constr.setAccessible(true);
                result = constr.newInstance();
                m_objects.put(id, result);

                var fieldsList = Converter.buildFieldsList(clazz);

                for (var field : fieldsList) {
                    var fieldType = field.getType();
                    var fieldName = field.getName();
                    var fieldValue = m_data.getProperty(fieldPrefix+fieldName);
                    var isNull = Boolean.parseBoolean(m_data.getProperty(fieldPrefix+fieldName+".isNull", "false"));

                    try {
                        field.setAccessible(true);
                        if (fieldType.equals(Integer.TYPE)) field.setInt(result, Integer.parseInt(fieldValue));
                        else if (fieldType.equals(Character.TYPE)) field.setChar(result, fieldValue.charAt(0));
                        else if (fieldType.equals(Byte.TYPE)) field.setByte(result, Byte.parseByte(fieldValue));
                        else if (fieldType.equals(Short.TYPE)) field.setShort(result, Short.parseShort(fieldValue));
                        else if (fieldType.equals(Long.TYPE)) field.setLong(result, Long.parseLong(fieldValue));
                        else if (fieldType.equals(Boolean.TYPE)) field.setBoolean(result, Boolean.parseBoolean(fieldValue));
                        else if (fieldType.equals(Double.TYPE)) field.setDouble(result, Double.parseDouble(fieldValue));
                        else if (fieldType.equals(Float.TYPE)) field.setFloat(result, Float.parseFloat(fieldValue));
                        else {
                            if (isNull) field.set(result, null);
                            else field.set(result, restoreObject(fieldValue));
                        }
                    }
                    catch (IllegalAccessException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                result = null;
            }
        }
        return result;
    }

    private Object restoreArrayImpl(String id){
        if (id == null || id.equals("a0"))
            return null;
        var result = m_objects.get(id);
        if (result == null) {

            try {
                var keyPrefix   = id        + ".";
                var fieldPrefix = keyPrefix + "f.";

                var componentType = (String) m_data.get(keyPrefix + "className");
                var length = Integer.parseInt((String)m_data.get(keyPrefix + "length"));

                Class<?> fieldType;
                if (Integer.TYPE.getName().equals(componentType)) fieldType = Integer.TYPE;
                else if (Character.TYPE.getName().equals(componentType)) fieldType = Character.TYPE;
                else if (Byte.TYPE.getName().equals(componentType)) fieldType = Byte.TYPE;
                else if (Short.TYPE.getName().equals(componentType)) fieldType = Short.TYPE;
                else if (Long.TYPE.getName().equals(componentType)) fieldType = Long.TYPE;
                else if (Boolean.TYPE.getName().equals(componentType)) fieldType = Boolean.TYPE;
                else if (Double.TYPE.getName().equals(componentType)) fieldType = Double.TYPE;
                else if (Float.TYPE.getName().equals(componentType)) fieldType = Float.TYPE;
                else fieldType = Class.forName(componentType);

                result = Array.newInstance(fieldType, length);
                m_objects.put(id, result);

                for (var index = 0; index < length; index++) {
                    var fieldName = ""+index;
                    var fieldValue = m_data.getProperty(fieldPrefix+fieldName);
                    var isNull = Boolean.parseBoolean(m_data.getProperty(fieldPrefix+fieldName+".isNull", "false"));

                    if (fieldType.equals(Integer.TYPE)) Array.setInt(result, index, Integer.parseInt(fieldValue));
                    else if (fieldType.equals(Character.TYPE)) Array.setChar(result, index, fieldValue.charAt(0));
                    else if (fieldType.equals(Byte.TYPE)) Array.setByte(result, index, Byte.parseByte(fieldValue));
                    else if (fieldType.equals(Short.TYPE)) Array.setShort(result, index, Short.parseShort(fieldValue));
                    else if (fieldType.equals(Long.TYPE)) Array.setLong(result, index, Long.parseLong(fieldValue));
                    else if (fieldType.equals(Boolean.TYPE)) Array.setBoolean(result, index, Boolean.parseBoolean(fieldValue));
                    else if (fieldType.equals(Double.TYPE)) Array.setDouble(result, index, Double.parseDouble(fieldValue));
                    else if (fieldType.equals(Float.TYPE)) Array.setDouble(result, index, Float.parseFloat(fieldValue));
                    else {
                        if (isNull) Array.set(result, index, null);
                        else Array.set(result, index, restoreObject(fieldValue));
                    }
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                result = null;
            }
        }
        return result;
    }
}

class Converter {

    static Properties toProperties(Object obj){
        return new Encoder().toProperties(obj);
    }

    static Object fromProperties(Properties prop){
        return new Decoder().fromProperties(prop);
    }

    static List<Field> buildFieldsList(Class<?> clazz){
        var fieldsList = new ArrayList<Field>();
        var currentClass = clazz;
        while (currentClass != null){
            var fields = currentClass.getDeclaredFields();
            fieldsList.addAll(Arrays.asList(fields));
            currentClass = currentClass.getSuperclass();
        }
        var fields = fieldsList.iterator();
        while (fields.hasNext()) {
            var field = fields.next();
            if (Modifier.isStatic(field.getModifiers())) fields.remove();
            else if (Modifier.isTransient(field.getModifiers())) fields.remove();
            else if (field.getName().startsWith("class$")) fields.remove();
        }
        return fieldsList;
    }

}