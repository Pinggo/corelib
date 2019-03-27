package com.dev.pipi.commlib.datahelper.db;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
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
public class StringConverter implements PropertyConverter<List<String>, String> {
    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        else {
            List<String> list = Arrays.asList(databaseValue.split(","));
            return list;
        }
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        if(entityProperty==null){
            return null;
        }
        else{
            StringBuilder sb= new StringBuilder();
            for(String link:entityProperty){
                sb.append(link);
                sb.append(",");
            }
            return sb.toString();
        }
    }
}

