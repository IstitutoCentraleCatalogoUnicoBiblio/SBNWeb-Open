package it.finsiel.misc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private DateUtil() {
    }

    public static int getYear(Date date) {
        int d = 0;
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            d = Integer.parseInt(df.format(date));
        }
        return d;
    }

    public static Date toDate(String date) {
        Date d = null;
        try {
            d = _data.parse(date);
        } catch (Exception e) {

        }
        return d;
    }

    public static boolean isData(long date) {
        return !"".equals(formattaData(date));
    }

    public static boolean isData(String date) {
        Date d = toDate(date);
        return d != null && _data.format(d).equals(date);
    }

    /*
     * Format a long into a Date using the pattern MM/dd/yyyy - HH:mm:ss return
     * empty if an error occurs
     */
    public static String formattaDataOra(long date) {
        try {
            return _dataOra.format(new Date(date));
        } catch (Exception e) {

        }
        return "";
    }

    /*
     * dataOra di tipo "dd/MM/yyyy - HH:mm:ss"
     */
    public static Date getDataOra(String dataOra) {
        try {
            return _dataOra.parse(dataOra);
        } catch (Exception e) {

        }
        return null;
    }

    public static String formattaData(long date) {
        try {
            return _data.format(new Date(date));
        } catch (Exception e) {

        }
        return "";
    }

    public static String formattaDataCompleta(long date) {
        try {
            return _dataCompleta.format(new Date(date));
        } catch (Exception e) {

        }
        return "";
    }

    
    public static long diffDays(Calendar start, Calendar end) {
		long endL = end.getTimeInMillis()
				+ end.getTimeZone().getOffset(end.getTimeInMillis());
		long startL = start.getTimeInMillis()
				+ start.getTimeZone().getOffset(start.getTimeInMillis());
		return (endL - startL) / MILLISECS_PER_DAY;
	}
    
    
    private final static SimpleDateFormat _dataOra = new SimpleDateFormat(
            "dd/MM/yyyy - HH:mm");

    private final static SimpleDateFormat _data = new SimpleDateFormat(
            "dd/MM/yyyy");

    private final static SimpleDateFormat _dataCompleta = new SimpleDateFormat(
            "yyyyMMddHHmmssSSS");

    private static final long MILLISECS_PER_MINUTE = 60 * 1000;
    
    private static final long MILLISECS_PER_HOUR = 60 * MILLISECS_PER_MINUTE;
    
    private static final long MILLISECS_PER_DAY = 24 * MILLISECS_PER_HOUR;

    
    public static  String getDate (  )   {  
        DateFormat df = new SimpleDateFormat ( "yyyy-MM-dd" ) ; 
        //df.setTimeZone ( TimeZone.getTimeZone ( "PST" )  ) ; 
        return df.format ( new Date (  )  )  ; 
     }  
     
    public static String getTime (  )   {  
        DateFormat df = new SimpleDateFormat ( "hh:mm:ss:z" ) ; 
        //df.setTimeZone ( TimeZone.getTimeZone ( "PST" )  ) ; 
        //df.setTimeZone ( TimeZone.getTimeZone ( "America/Los_Angeles" )  ) ;         
        return df.format ( new Date (  )  )   ; 
     }      
}