package com.opencharge.opencharge.domain.parsers.impl;

import android.support.v4.util.Pair;

import com.opencharge.opencharge.domain.Entities.User;
import com.opencharge.opencharge.domain.parsers.UsersParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DmnT on 18/05/2017.
 */

public class FirebaseUsersParser implements UsersParser {

    public static final String USERNAME_KEY = "username";
    public static final String PHOTO_KEY = "photo";
    public static final String EMAIL_KEY = "email";
    public static final String MINUTES_KEY = "minutes";
    public static final String CREATS_KEY = "punts";
    public static final String POINT_ID="first";
    public static final String POINT_NAME="second";

    @Override
    public User parseFromMap(String key, Map<String, Object> map) {
        User user = new User(key);
        user.setUsername(parseStringKeyFromMap(USERNAME_KEY, map));
        user.setPhoto(parseStringKeyFromMap(PHOTO_KEY, map));
        user.setEmail(parseStringKeyFromMap(EMAIL_KEY, map));
        user.setMinutes(parseLongKeyFromMap(MINUTES_KEY, map).intValue());
        user.setPoints(parseArrayListFromMap(CREATS_KEY, map));

        return user;
    }

    private ArrayList<Pair<String,String>> parseArrayListFromMap(String key, Map<String, Object> map) {
        ArrayList<Pair<String,String>> arrayList = new ArrayList<>();
        if (map.containsKey(key)) {

            List<HashMap<String, String>> totsPunts = (List<HashMap<String, String>>) map.get(key);
            for (HashMap<String,String> element : totsPunts) {
                String idPunt = element.get(POINT_ID);
                String nomPunt = element.get(POINT_NAME);
                Pair<String, String> point = new Pair<>(idPunt, nomPunt);
                arrayList.add(point);
            }

        }

        return arrayList;
    }

    private Long parseLongKeyFromMap(String key, Map<String, Object> map) {
        long value = 0;
        if (map.containsKey(key)) {
            value = (Long)map.get(key);
        }
        return value;
    }

    private String parseStringKeyFromMap(String key, Map<String, Object> map) {
        if (map.containsKey(key)) {
            return (String)map.get(key);
        }
        else {
            return null;
        }
    }
}
