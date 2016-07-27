package com.huodong.im.utils;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Map.Entry;

import com.huodong.im.chatdemo.R;
import com.huodong.im.config.IntentConstant;

import android.content.Context;
import android.util.Log;

public class IMDateUserHelper {
	
	static Context context;
	public IMDateUserHelper(Context context){
		this.context=context;
	}
	public static long oneYear=60*60*24*365;
	public static long oneDay=24*60*60*1000;
	
	/**
	  * 获取约会初始化时间
	  * 当天或明天20:00 
	  * @return
	  */
	 public static String getDateTmieInit() {
		 long now=System.currentTimeMillis();
		 if(now<CommonUtil.getDayBegin()+18*60*60*1000){
			 return  CommonUtil.getTimeString(CommonUtil.getDayBegin()+20*60*60*1000,CommonUtil.timeFormat1);
		 }else {
			 return  CommonUtil.getTimeString(CommonUtil.getDayBegin()+44*60*60*1000,CommonUtil.timeFormat1);
		}
		  
		 }
	 /**
	  * 获取出生日期的 时间戳 （s为单位）20000101--->long s
	  * @param birthday
	  * @return
	  */
	 public static long getUserBirthdaylong(String birthday){
		 long bir=CommonUtil.getTimestamp(birthday, CommonUtil.timeFormat2)/1000;
		return bir; 
	 }
	 /**
	  * 由出生日期获取 年龄
	  * @param birthday  s
	  * @param now
	  * @return
	  */
	 public static long getUserAge(String birthday,long now){
		 long bir=getUserBirthdaylong(birthday);
		 
		 long age=(now-bir)/(oneYear);
		 Log.i("Amy age", age+" "+birthday);
		 return age; 
	 }
	 /**
	  * 格式化 建立约会的时间
	  * @param dateTime
	  * @return
	  */
	 public static String formatDateTime(String dateTime){
		 Log.i("Amy 3-18", "dateTime   "+dateTime);
		 int i=dateTime.indexOf(" ");
		 String time=dateTime.substring(i, dateTime.length());
		 String formatDateTime;
		 long dateTime_long=CommonUtil.getTimestamp(dateTime, CommonUtil.timeFormat1);
		 long today=CommonUtil.getDayEnd();
		 long tomorrow=CommonUtil.getDayEnd()+oneDay;
		 long theDayAfterTomorrow=tomorrow+oneDay;
		 if(dateTime_long<today){
			 formatDateTime=context.getResources().getString(R.string.today)+time;
		 }else if (dateTime_long>=today&&dateTime_long<tomorrow) {
			formatDateTime=context.getResources().getString(R.string.tomorrow)+time;
		}else if (dateTime_long>=tomorrow&&dateTime_long<theDayAfterTomorrow) {
			formatDateTime=context.getResources().getString(R.string.the_day_after_tomorrow)+time;
		}else {
			formatDateTime=dateTime;
		}
		 Log.i("Amy 3-18", "formatDateTime   "+formatDateTime);
		 return formatDateTime; 
	 }
	 /**
	  * 从今天 明天后天转换为 2016-04-23 20:00形式
	  * @param dateTime
	  * @return
	  */
	 public static String paraseFormatedDateTime(String dateTime){
		 String result=dateTime;
		 int i=dateTime.indexOf(" ");
		 if(i>0){
			 Log.i("Amy dateTime", dateTime+"  "+i);
			 String day=dateTime.substring(0, i);
			 String time=dateTime.substring(i, dateTime.length());
			 if(day.equals(context.getResources().getString(R.string.today))){
				 result=CommonUtil.getTimeString(CommonUtil.getDayBegin()+oneDay/2, CommonUtil.timeFormat3)+time;
			 }else if (day.equals(context.getResources().getString(R.string.tomorrow))) {
				 result=CommonUtil.getTimeString(CommonUtil.getDayEnd()+oneDay/2, CommonUtil.timeFormat3)+time;
			}
			 else if (day.equals(context.getResources().getString(R.string.the_day_after_tomorrow))) {
				 result=CommonUtil.getTimeString(CommonUtil.getDayEnd()+oneDay+oneDay/2, CommonUtil.timeFormat3)+time;
			}
			 Log.i("Amy 3-18", "result   "+result);
		 }
		 
		return result;
		 
	 }
	 /**
	  * 验证选择的时间是否合理
	  * @param dateTime
	  * @return
	  */
	 public static int getInvalidDateTimeTips(String dateTime){
		long selectTime=CommonUtil.getTimestamp(paraseFormatedDateTime(dateTime), CommonUtil.timeFormat1);
		
		long now=System.currentTimeMillis();
		if(selectTime<now){
			return IntentConstant.DATE_TIME_INVALID;
		}
		long twoHour=System.currentTimeMillis()+2*60*60*1000;
		if(selectTime<twoHour&&selectTime>now){
			return IntentConstant.DATE_TIME_TOO_CLOSE;
		}
		long month=(System.currentTimeMillis()+30*oneDay);
		if(selectTime>month){
			return IntentConstant.DATE_TIME_TOO_FAR;
		}
		else {
			return IntentConstant.DATE_TIME_NORMAL;
		}		 
	 }
	 /**
	  * 验证选择的时间是否合理 2 是否当前有发布的约会时间为2小时内的
	  * @param dateTime
	  * @return
	  */
	 public static boolean getInvalidDateTime(String dateTime,Map<Integer, Long> map){
		 Log.i("Amy invalid", "dateTime "+dateTime+" map"+map.size());
		long selectTime=Long.valueOf(dateTime);
		long[] time=new long[map.size()];
		if(map.size()>0){
			for(Map.Entry<Integer, Long> entry:map.entrySet()){
//				time[entry.getKey()]=entry.getValue();
				 Log.i("Amy invalid", "dateTime "+(Math.abs(selectTime-entry.getValue())<2));
				if(Math.abs(selectTime-entry.getValue())<2){
					return false;
				}
			}return true;
		}
		else {
			return true;
		}
	 }
	 /**
		 * 格式化distance
		 * @param distance m
		 * @return  0.0公里  
		 */
		public static String formatDistance(double distance) {
			String dis="0";
			Log.i("Amy format", "bbb====="+distance);
			DecimalFormat df = new DecimalFormat("0.0");
			String result = df.format(distance/1000);
			return result;
		}
		/**
		 * 格式化付费规则
		 * @param feesType
		 * @return
		 */
		public static String formatFees(int feesType) {
			String type="";
			switch (feesType) {
			case 1:
				type=context.getString(R.string.date_fees_type_i);
				break;
			case 2:
				type=context.getString(R.string.date_fees_type_man_a);
				break;
			case 3:
				type=context.getString(R.string.date_fees_type_aa);
				break;
			case 4:
				type=context.getString(R.string.date_fees_type_u);
				break;
			}
			return type;
		}
		/**
		 * 格式化约会对象  123->文字
		 */
		public static String formatPartner(int partnerType) {
			String type="";
			switch (partnerType) {
			case 0://不限
				type=context.getString(R.string.date_partner_type_nogender);
				break;
			case 1://女
				type=context.getString(R.string.date_partner_type_women);
				break;
			case 2://男
				type=context.getString(R.string.date_partner_type_man);
				break;
			}
			return type;
		}
		/**
		 * 查看自己是否符合性别限制  
		 */
		public static boolean islimitPartner(int partnerType,int myGender) {
			switch (myGender) {
			case 1://女
				if(partnerType==2)return false;
				else return true;
				
			case 2://男
				if(partnerType==1)return false;
				else return true;
				
			default:return true;
			}
		}
		/**
		 * 格式化最近活动时间
		 * @param recentTime
		 * @return
		 */
		public static String getRecentTime(long recentTime) {
			String recent_=context.getResources().getString(R.string.recent_just_now);
			if(recentTime<=2){recent_=context.getResources().getString(R.string.recent_just_now);}
			else if (recentTime>2&&recentTime<4) {
				recent_=context.getResources().getString(R.string.recent_two_hour);
			}
			else if (recentTime>4&&recentTime<12) {
				recent_=context.getResources().getString(R.string.recent_four_hour);
			}
			else if (recentTime>12&&recentTime<36) {
				recent_=context.getResources().getString(R.string.recent_twelve_hour);
			}
			else if (recentTime>36&&recentTime<84) {
				recent_=context.getResources().getString(R.string.recent_three_day);
			}
			else {
				recent_=context.getResources().getString(R.string.recent_seven_day_out);
			}
			return recent_;
		}
		/**
		 * 计算两点间距离
		 * @param long1
		 * @param lat1
		 * @param long2
		 * @param lat2
		 * @return  m
		 */
		public static double Distance(double long1, double lat1, double long2,  
		        double lat2) {  
			Log.i("Amy   dis", long1 +" "+lat1+" "+ long2+ " "+lat2 );
		    double a, b, R;  
		    R = 6378137; // 地球半径  
		    lat1 = lat1 * Math.PI / 180.0;  
		    lat2 = lat2 * Math.PI / 180.0;  
		    a = lat1 - lat2;  
		    b = (long1 - long2) * Math.PI / 180.0;  
		    double d;  
		    double sa2, sb2;  
		    sa2 = Math.sin(a / 2.0);  
		    sb2 = Math.sin(b / 2.0);  
		    d = 2  
		            * R  
		            * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)  
		                    * Math.cos(lat2) * sb2 * sb2));  
		    return d;  
		}  
		private final static int[] dayArr = new int[] { 20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22 };  
		private final static String[] constellationArr = new String[] { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };  
		  
		  
		public static String getConstellation(String birthday) {  
//			birthday ="200003020";
			if(birthday.length()>=8){
				int month=Integer.valueOf(birthday.substring(4, 6));
				int day=Integer.valueOf(birthday.substring(6));
			    return day < dayArr[month - 1] ? constellationArr[month - 1] : constellationArr[month];
			}
			else {
				return constellationArr[0];
			}
			  
		}  


}
