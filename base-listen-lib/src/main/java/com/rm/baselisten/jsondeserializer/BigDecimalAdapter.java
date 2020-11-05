package com.rm.baselisten.jsondeserializer;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class BigDecimalAdapter implements JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return json.getAsBigDecimal();
        } catch (Exception e) {
            Log.e("CustomJsonDeserializer", json.isJsonNull() ? "null" : json.getAsString() + " is not BigDecimal.");
            return BigDecimal.ZERO;
        }
    }
}