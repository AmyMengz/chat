package com.huodong.im.utils;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;

public class CommonUtil {
	 /**
     * @Description 判断存储卡是否存在
     * @return
     */
    public static boolean checkSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
	public final static String ROOTPATH= Environment.getExternalStorageDirectory().getAbsolutePath();
	public static String SAVE_IMAGE_CACHE_PATH=ROOTPATH+File.separator+"ChatDemo"+File.separator+"imgCache"+File.separator;
	/**
	 * 格式化的时间转为long ms
	 * @param time
	 * @param form
	 * @return
	 */
	public static long getTimestamp(String time,String form) {
        long rand = 0;
        try {
//            String time = DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");*/
        	SimpleDateFormat sdf = new SimpleDateFormat(form);
            Date d2 = null;
            try {
                d2 = sdf.parse(time);//将String to Date类型
                rand = d2.getTime();
            } catch (ParseException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rand;
    }
	/**
	 * long型时间格式化
	 * @param time
	 * @return
	 */
	/*public static String timeFormat1="yyyy年MM月dd日 HH:mm";*/
	public static String timeFormat1="yyyy-MM-dd HH:mm";
	public static String timeFormat2="yyyyMMdd";
	public static String timeFormat3="yyyy-MM-dd";
	public static String getTimeString(long time,String timeFormat) {
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");*/
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		Date date=new Date(time);
		String result = sdf.format(date);
		return result;
	}
	/**
	 *获取今天开始时间时间戳 ms
	 * @return
	 */
	 public static long getDayBegin() {
		  Calendar cal = Calendar.getInstance();
		  cal.set(Calendar.HOUR_OF_DAY, 0);
		  cal.set(Calendar.SECOND, 0);
		  cal.set(Calendar.MINUTE, 0);
		  cal.set(Calendar.MILLISECOND, 001);
		  return new Timestamp(cal.getTimeInMillis()).getTime();
		 }
	/**
	 * 获取今天结束时间戳
	 * @return
	 */
	 public static long getDayEnd() {
		  Calendar cal = Calendar.getInstance();
		  cal.set(Calendar.HOUR_OF_DAY, 0);
		  cal.set(Calendar.SECOND, 0);
		  cal.set(Calendar.MINUTE, 0);
		  cal.set(Calendar.MILLISECOND, 001);
		  return new Timestamp(cal.getTimeInMillis()).getTime()+oneDay;
		 }
	 public static long oneDay=24*60*60*1000;
	 public static long oneYear=365*24*60*60*1000;
	 
	 public static Bitmap getRoundCornerBitmap(Bitmap bitmap, float roundPX){  
	        int width = bitmap.getWidth();  
	        int height = bitmap.getHeight();  
	   
	        Bitmap bitmap2 = Bitmap.createBitmap(width, height, Config.ARGB_8888);  
	        Canvas canvas = new Canvas(bitmap2);  
	   
	        final int color = 0xff424242;  
	        final Paint paint = new Paint();  
	        final Rect rect = new Rect(0, 0, width, height);  
	        final RectF rectF = new RectF(rect);  
	   
	        paint.setColor(color);  
	        paint.setAntiAlias(true);  
	        canvas.drawARGB(0, 0, 0, 0);  
	        canvas.drawRoundRect(rectF, roundPX, roundPX, paint);  
	   
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
	        canvas.drawBitmap(bitmap, rect, rect, paint);  
	   
	        return bitmap2;  
	    }  
}
