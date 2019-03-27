package com.dev.pipi.commlib.datahelper.db;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/09/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public abstract class CustomerConverter<T> implements PropertyConverter<List<T>,String> {

    private final Gson mGson;

    public CustomerConverter() {
        mGson = new Gson();
    }
    public  <T> List<T> fromJsonArray(String json, Class<T> clazz) throws Exception {
        List<T> lst = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            lst.add(mGson.fromJson(elem, clazz));
        }
        return lst;
    }

    @Override
    public List<T> convertToEntityProperty(String databaseValue) {
        /*
        泛型擦除
        if (databaseValue == null) {
            return null;
        }
        Type type = new TypeToken<ArrayList<T>>() {
        }.getType();
        ArrayList<T> itemList = mGson.fromJson(databaseValue, type);
        return itemList;*/
        List<T> itemList = new ArrayList<>();
        try {
            itemList = fromJsonArray(databaseValue, getClazz());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemList;
    }
    protected abstract Class<T> getClazz();
    @Override
    public String convertToDatabaseValue(List<T> entityProperty) {
        if (entityProperty == null) {
            return null;
        }
        return mGson.toJson(entityProperty);
    }
}

