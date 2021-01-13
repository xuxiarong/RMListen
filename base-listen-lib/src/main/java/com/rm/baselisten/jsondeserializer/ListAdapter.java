package com.rm.baselisten.jsondeserializer;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.rm.baselisten.net.util.GsonUtils;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;


public class ListAdapter implements JsonDeserializer<List<?>>{
    @Override
    public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
            return GsonUtils.fromJson(json, typeOfT);
        } else {
            Log.e("CustomJsonDeserializer", json.isJsonNull() ? "null" : json.getAsString() + " is not array.");
            return Collections.emptyList();
        }
    }
}