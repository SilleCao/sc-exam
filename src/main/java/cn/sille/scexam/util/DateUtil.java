package cn.sille.scexam.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {


    public static String getDatePattern() {
        return "yyyy-MM-dd";
    }
    public static String getSimpleDatePattern() {
        return "yyyyMMdd";
    }

    public static String getShortDateTimePattern() {
        return getDatePattern() + " HH:mm:ss";
    }

    public static String convertDateToString(Date aDate, String aMask) {
        return getDateTime(aMask, aDate);
    }

    public static Date convertStringToDate(String aMask, String strDate) throws ParseException {
        SimpleDateFormat df;
        Date date;
        df = new SimpleDateFormat(aMask);

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {

            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    public static String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        df = new SimpleDateFormat(aMask);
        returnValue = df.format(aDate);

        return (returnValue);
    }

    public static Date dayAdd(Date src, int add) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(src);
        ca.set(Calendar.DAY_OF_YEAR, ca.get(Calendar.DAY_OF_YEAR) + add);
        return ca.getTime();
    }

    public static String getDate(Date aDate) {
        SimpleDateFormat df;
        String returnValue = null;

        if (aDate != null) {
            df = new SimpleDateFormat(getShortDateTimePattern());
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    public static Date convertStringToDate(String strDate) throws ParseException {

        return convertStringToDate(getDatePattern(), strDate);
    }

    public static Date convertStringToDate4Short(String strDate) throws ParseException {

        return convertStringToDate(getShortDateTimePattern(), strDate);
    }

    public static Boolean isPassedHours(Date compareDate, int passHours) {

        if (null != compareDate) {

            Calendar compareCalendar = Calendar.getInstance();
            Calendar currentCalendar = Calendar.getInstance();

            compareCalendar.setTime(compareDate);
            compareCalendar.add(Calendar.HOUR_OF_DAY, passHours);

            if(currentCalendar.after(compareCalendar)) {
                return true;
            }
        }

        return false;
    }

    public static Date asDate() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp asTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getDateUniqueString(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }

    public static boolean isDate(String formatter,String str){
        SimpleDateFormat format = new SimpleDateFormat(formatter);
        format.setLenient(false);
        try {
            format.parse(str);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isDate (String str) {
        boolean validDate=false;
        if (StringUtils.isBlank(str)) {
            return validDate;
        }
        String formatStr = formatSimpleDateStr(str);

        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(getDatePattern());

        if (formatStr.length() != format.toPattern().length()) {
            return validDate;
        }
        format.setLenient(Boolean.FALSE);
        try {
            //parse the inDate parameter
            Date date = format.parse(formatStr.trim());
            String dateStr = format.format(date);

            validDate = formatStr.equals(dateStr);
        }
        catch (ParseException pe) {
            return validDate;
        }
        return true;
    }

    public static String formatSimpleDateStr (String str) {
        String formatStr = "";
        if (StringUtils.isBlank(str)) {
            return formatStr;
        }
        String[] dates = str.split("-");
        if (StringUtils.isBlank(dates[1]) || StringUtils.isBlank(dates[2])) {
            return str;
        }
        formatStr = formatStr.concat(dates[0]).concat("-");
        if (dates[1].length()<2) {
            formatStr = formatStr.concat("0").concat(dates[1]).concat("-");
        } else {
            formatStr = formatStr.concat(dates[1]).concat("-");
        }

        if (dates[2].length()<2) {
            formatStr = formatStr.concat("0").concat(dates[2]);
        } else {
            formatStr = formatStr.concat(dates[2]);
        }
        return formatStr;
    }

    public static Date getYesterday () {
        Calendar ca = Calendar.getInstance();
        ca.setTime(asDate());
        ca.set(Calendar.DAY_OF_YEAR, ca.get(Calendar.DAY_OF_YEAR) - 1);
        return ca.getTime();
    }

    public static Date getStartDateOfDay (Date date) throws ParseException {
        String dayStr = getDateTime(getDatePattern(),date);
        dayStr = dayStr.concat(" 00:00:00");
        return convertStringToDate4Short(dayStr);
    }
    public static Date getEndDateOfDay (Date date) throws ParseException {
        String dayStr = getDateTime(getDatePattern(),date);
        dayStr = dayStr.concat(" 23:59:59");
        return convertStringToDate4Short(dayStr);
    }


    /**
     * 20150425
     * 20010725
     * 20150424
     * @param str
     * @return
     */
    public static boolean isValidateBirthday(String str){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = df.parse(str);
            Calendar now = Calendar.getInstance();
            now.setTime(date);
            Calendar before = Calendar.getInstance();
            before.set(Calendar.YEAR, before.get(Calendar.YEAR) - 13);
            Calendar after = Calendar.getInstance();
            after.set(Calendar.MONTH, before.get(Calendar.MONTH) + 9);

            if((now.after(before)) && now.before(after)) {
                return true;
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return false;
    }

}
