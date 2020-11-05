package com.rm.baselisten.jsondeserializer;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ShortAdapter implements JsonDeserializer<Short> {
    @Override
    public Short deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return json.getAsShort();
        } catch (Exception e) {
            Log.e("CustomJsonDeserializer", json.isJsonNull() ? "null" : json.getAsString() + " is not Short.");
            return 0;
        }
    }
}