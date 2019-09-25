package com.omniwyse.placessdksample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Chandra Kant on 25/9/19.
 */
public class GsonUtils {
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (null == json) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<T>(){}.getType());
    }
}
