package com.del.ministry.utils;

import org.apache.log4j.Logger;

public class Utils {

    final static private Logger logger = Logger.getLogger("Ministry Logger");

    public static Logger getLogger() {
        return logger;
    }

    public static boolean isTrimmedEmpty(Object val) {
        return val == null || val.toString().trim().length() == 0;
    }

}
