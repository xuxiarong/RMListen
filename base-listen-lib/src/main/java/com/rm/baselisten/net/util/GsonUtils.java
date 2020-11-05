// -*- Mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-

package com.rm.baselisten.net.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public class GsonUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final Gson sLocalGson = createLocalGson();

    private static GsonBuilder createLocalGsonBuilder() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gsonBuilder.serializeNulls();
        gsonBuilder.setDateFormat(DATE_FORMAT);
        return gsonBuilder;
    }

    private static Gson createLocalGson() {
        return createLocalGsonBuilder().create();
    }


    public static Gson getLocalGson() {
        return sLocalGson;
    }

    public static <T> T fromLocalJson(String json, Class<T> clazz){
        try {
            return sLocalGson.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T fromLocalJson(String json, Type typeOfT){
        try {
            return sLocalGson.fromJson(json, typeOfT);
        } catch (Exception e){
            return null;
        }
    }

    public static  <T> T fromJson(JsonElement json, Type typeOfT){
        try {
            return sLocalGson.fromJson(json, typeOfT);
        } catch (Exception e){
            return null;
        }
    }

    public static String toJson(Object src) {
        return sLocalGson.toJson(src);
    }

}
