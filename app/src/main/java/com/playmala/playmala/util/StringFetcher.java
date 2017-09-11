package com.playmala.playmala.util;


import com.playmala.playmala.Blocks5App;

public class StringFetcher {

    public static String getString(int id) {
        return Blocks5App.getContext().getString(id);
    }

    public static String getString(int id, Object... format) {
        return Blocks5App.getContext().getString(id, format);
    }

}