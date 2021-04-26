package com.sydney.au.ethicalaivalidation.utils;

import java.sql.Timestamp;
import java.util.Date;

/**
 * service tools
 * @author Moment
 */
public class ServiceUtils {
    public static Timestamp getNowTimeStamp() {
        Date now = new Date();
        return new Timestamp(now.getTime());
    }
}
