package com.zhangjingbo.account.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    public static Date getDateBuLocalDate(LocalDate localDate){
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }
    public static String dateToString(Date date){
        String strDate = "";
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            strDate = sdf1.format(date);
        }catch (Exception e){
            System.out.println("date为空");
        }

        return strDate;
    }
}
