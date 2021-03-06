package com.rm.baselisten.jsondeserializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class IntegerAdapter extends TypeAdapter<Integer> {
    @Override
    public void write(JsonWriter out, Integer value) throws IOException {
        try {
            if(value == null){
                value = -1;
            }
            out.value(value);
        }catch (Exception e){
            out.value(-1);
        }
    }

    @Override
    public Integer read(JsonReader in) {
        try {
            Integer value;
            if(in.peek() == JsonToken.NULL) {
                in.nextNull();
                return -1;
            }
            if(in.peek() == JsonToken.BOOLEAN){
                boolean b = in.nextBoolean();
                return b?1:-1;
            }
            if(in.peek() == JsonToken.STRING){
                //这里按自己意愿进行处理
                return 0;
            }else {
                value = in.nextInt();
                return value;
            }
        }catch (Exception e){
            return -1;
        }
    }
}