package com.tomtom.itcu.traffic.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TrafficResponseUtil {

    private static ObjectMapper object;

    static {
        object = new ObjectMapper();
    }

    public static Object convetToObject(String json, Class clazz) {
        try {
            return object.readValue(json, clazz);
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
