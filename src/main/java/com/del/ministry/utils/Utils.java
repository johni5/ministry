package com.del.ministry.utils;

import org.apache.log4j.Logger;

import java.util.ResourceBundle;

public class Utils {

    final static private Logger logger = Logger.getLogger("Ministry Logger");

    private static ResourceBundle bundle;

    public static Logger getLogger() {
        return logger;
    }

    public static boolean isTrimmedEmpty(Object val) {
        return val == null || val.toString().trim().length() == 0;
    }

    public static <T> T nvl(T t1, T t2) {
        return t1 == null ? t2 : t1;
    }

    public static ResourceBundle getBundle() {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("info");
        }
        return bundle;
    }

}
