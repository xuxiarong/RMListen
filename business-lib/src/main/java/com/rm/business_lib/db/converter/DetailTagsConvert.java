package com.rm.business_lib.db.converter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rm.business_lib.bean.DetailTags;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * desc   :
 * date   : 2020/11/10
 * version: 1.0
 */
public class DetailTagsConvert implements PropertyConverter<List<DetailTags>, String> {
    @Override
    public List<DetailTags> convertToEntityProperty(String databaseValue) {
        try{
            if (TextUtils.isEmpty(databaseValue)) {
                return new ArrayList();
            }
            // 先得获得这个，然后再typeToken.getType()，否则会异常
            TypeToken<List<DetailTags>> typeToken = new TypeToken<List<DetailTags>>() {
            };
            return new Gson().fromJson(databaseValue, typeToken.getType());
        }catch ( Exception e){
            return new ArrayList();
        }
    }

    @Override
    public String convertToDatabaseValue(List<DetailTags> entityProperty) {
        if (entityProperty == null || entityProperty.size() == 0) {
            return "";
        } else {
            return new Gson().toJson(entityProperty);
        }
    }
}
