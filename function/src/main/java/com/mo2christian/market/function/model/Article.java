package com.mo2christian.market.function.model;

public class Article {

    private String name;

    private String comment;

    public Article(String name) {
        this.name = name;
    }

    public Article(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment == null? "" : comment;
    }

    public static class Field {

        public static String KIND = "article";

        public static String NAME = "name";

        public static String COMMENT = "comment";

        public static String CHECK = "check";

    }
}
