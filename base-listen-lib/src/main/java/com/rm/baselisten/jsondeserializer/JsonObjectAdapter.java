package com.rm.baselisten.jsondeserializer;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


public class JsonObjectAdapter implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return json.getAsJsonObject();
        } catch (Exception e) {
            Log.e("CustomJsonDeserializer", json.isJsonNull() ? "null" : json.getAsString() + " is not JsonObject.");
            return new Object();
        }
    }
}