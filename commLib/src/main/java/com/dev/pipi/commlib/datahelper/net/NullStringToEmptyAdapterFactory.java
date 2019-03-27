package com.dev.pipi.commlib.datahelper.net;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * <pre>
 *     author : pipi
 *     e-mail : xxx@xx
 *     time   : 2018/11/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        if (rawType != String.class) {
            return null;
        }
        return (TypeAdapter<T>) new StringNullAdapter();
    }
}