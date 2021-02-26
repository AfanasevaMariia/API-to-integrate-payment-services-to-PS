package com.example.demo.entities;

import java.util.HashMap;
import java.util.Map;

public enum FIELDS_WITH_ELEMENTS {

    F48_AddData_Private(48);

    int number;
    private static Map<Integer, FIELDS_WITH_ELEMENTS> map = new HashMap();

    public static boolean isBelong(int number) {
        if (map.containsKey(number))
            return true;
        return false;
    }

    FIELDS_WITH_ELEMENTS(int num) {
        this.number = num;
    }

    static {
        FIELDS_WITH_ELEMENTS[] var0 = values();
        int var1 = var0.length;
        map.put(48, var0[0]);
    }
}
