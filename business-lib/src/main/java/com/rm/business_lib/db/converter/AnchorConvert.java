package com.rm.business_lib.db.converter;

import com.google.gson.Gson;
import com.rm.business_lib.bean.Anchor;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * desc   :
 * date   : 2020/11/10
 * version: 1.0
 */
public class AnchorConvert implements PropertyConverter<Anchor, String> {
    @Override
    public Anchor convertToEntityProperty(String databaseValue) {
        return new Gson().fromJson(databaseValue, Anchor.class);
    }

    @Override
    public String convertToDatabaseValue(Anchor entityProperty) {
        return new Gson().toJson(entityProperty);
    }
}
