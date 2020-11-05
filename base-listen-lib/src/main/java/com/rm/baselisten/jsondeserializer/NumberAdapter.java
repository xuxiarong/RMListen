package com.rm.baselisten.jsondeserializer;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class NumberAdapter implements JsonDeserializer<Number> {
    @Override
    public Number deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return json.getAsNumber();
        } catch (Exception e) {
            Log.e("CustomJsonDeserializer", json.isJsonNull() ? "null" : json.getAsString() + " is not Number.");
            return 0;
        }
    }
}