package com.mo2christian.market.function.model;

import com.google.cloud.datastore.Key;

public class Basket {

    private Key key;

    public Basket(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public static class Field {

        public static String KIND = "basket";

        public static String KEY = "key";

        public static String CREATED_DATE = "created_date";

    }
}
