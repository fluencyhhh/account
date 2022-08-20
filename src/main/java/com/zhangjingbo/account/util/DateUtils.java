package com.zhangjingbo.account.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date getDateBuLocalDate(LocalDate localDate){
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    public static String localDateToString(LocalDate localDate){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = localDate.format(df);
        return dateStr;
    }

    public static LocalDate dateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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

    public static Date stringToDate(String str){
        Date date = new Date();
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf1.parse(str);
        }catch (Exception e){
            System.out.println("datestr 为空");
        }

        return date;
    }
    public static String getCurrentTime(){
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMddHHmmss");
        String dateTime = dateFormat.format(date);
        return dateTime;
    }
    public static String getDate(){
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = dateFormat.format(date);
        return dateTime;
    }

    /**
     * 获取当年的第一天
     * @param
     * @return
     */
    public static String getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return dateToString(getYearFirst(currentYear));
    }

    /**
     * 获取当年的最后一天
     * @param
     * @return
     */
    public static String getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return dateToString(getYearLast(currentYear));
    }
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }
    /**
     * 判断是否本年
     * @param
     * @return boolean
     */
    public static boolean ifThisYear(Date date){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return (currentYear==date.getYear());
    }

    /**
     * 判断是否本月
     * @param
     * @return boolean
     */
    public static boolean ifThisMonth(Date date){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        int currentMonth = currCal.get(Calendar.MONTH)+1;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        return (currentYear==year)&&(currentMonth==month);
    }

    public static LocalDate timestampTOLocalDate(Timestamp timestamp){
        LocalDate localDate = Instant.ofEpochMilli(timestamp.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }

    public static void main(String[] args) {

    }

}
