package com.imfondof.world.other.net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Imfondof on 2020/4/8
 */
public class GsonFactory {
    private static final String TAG = GsonFactory.class.getSimpleName();

    private Gson mGson;
    private static GsonFactory ourInstance = new GsonFactory();

    public GsonFactory() {
        mGson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                .registerTypeAdapter(long.class, new LongDefault0Adapter())
                .create();
    }

    public static GsonFactory getInstance() {
        return ourInstance;
    }

    public String toJson(Object object) {
        try {
            return mGson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * JSON 2 OBJ
     */
    public <T> T parseJson(Context context, String jsonStr, Class<T> reqClass, boolean AESDec, String target_url) throws Exception {
        return mGson.fromJson(jsonStr, reqClass);
    }

    public <T> Class<T> getClassT(Object object) {
        Class<T> mTClass = null;
        try {
            Type[] sType = object.getClass().getGenericInterfaces();
            Type[] generics = ((ParameterizedType) sType[0]).getActualTypeArguments();
            mTClass = (Class<T>) (generics[0]);
        } catch (Throwable e) {
        }
        return mTClass;
    }

    /**
     * 类型转换器
     */
    private class IntegerDefault0Adapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为int类型,如果后台返回""或者null,则返回0
                    return 0;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsNumber().intValue();
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        @Override
        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    private class LongDefault0Adapter implements JsonSerializer<Long>, JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为long类型,如果后台返回""或者null,则返回0
                    return 0l;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsNumber().longValue();
            } catch (NumberFormatException e) {
                return 0l;
            }
        }

        @Override
        public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    private class DoubleDefault0Adapter implements JsonSerializer<Double>, JsonDeserializer<Double> {
        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为double类型,如果后台返回""或者null,则返回0.00
                    return 0.00;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsNumber().doubleValue();
            } catch (NumberFormatException e) {
                return 0.00;
            }
        }

        @Override
        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }
}
