package com.huodong.im.chat.util;

import java.util.Calendar;
import java.util.TimeZone;

public class GetTimeUtil {
	/**
	 * 显示 年 到 分 的时间
	 * 
	 * @param time
	 *            秒数
	 * @return
	 */

	public static String get(Long time) {
		time = time * 1000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getDefault());
		calendar.setTimeInMillis(time);
		Integer year = calendar.get(Calendar.YEAR);
		Integer month = calendar.get(Calendar.MONTH) + 1;
		Integer date = calendar.get(Calendar.DATE);
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		Integer minute = calendar.get(Calendar.MINUTE);
		String str_month = month.toString();
		String str_date = date.toString();
		String str_hour = hour.toString();
		String str_minute = minute.toString();
		if (str_month.length() == 1) {
			str_month = "0" + str_month;
		}
		if (str_date.length() == 1) {
			str_date = "0" + str_date;
		}
		if (str_hour.length() == 1) {
			str_hour = "0" + str_hour;
		}
		if (str_minute.length() == 1) {
			str_minute = "0" + str_minute;
		}
		String str = year + "年" + str_month + "月" + str_date + "日 " + str_hour + ":" + str_minute;
		return str;
	}

	/**
	 * 显示 年 到 秒 的时间
	 * 
	 * @param time
	 *            秒数
	 * @return
	 */
	public static String getFullTime(Long time) {
		time = time * 1000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getDefault());
		calendar.setTimeInMillis(time);
		Integer year = calendar.get(Calendar.YEAR);
		Integer month = calendar.get(Calendar.MONTH) + 1;
		Integer date = calendar.get(Calendar.DATE);
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		Integer minute = calendar.get(Calendar.MINUTE);
		Integer second = calendar.get(Calendar.SECOND);
		String str_month = month.toString();
		String str_date = date.toString();
		String str_hour = hour.toString();
		String str_minute = minute.toString();
		String str_second = second.toString();
		if (str_month.length() == 1) {
			str_month = "0" + str_month;
		}
		if (str_date.length() == 1) {
			str_date = "0" + str_date;
		}
		if (str_hour.length() == 1) {
			str_hour = "0" + str_hour;
		}
		if (str_minute.length() == 1) {
			str_minute = "0" + str_minute;
		}
		if (str_second.length() == 1) {
			str_second = "0" + str_second;
		}
		String str = year + "年" + str_month + "月" + str_date + "日 " + str_hour + ":" + str_minute + "--" + str_second;
		return str;
	}
	/**
	 * 显示 年 到 秒 的时间
	 * 
	 * @param time
	 *            秒数
	 * @return
	 */
	public static String getYToMin(Long time) {
		time = time * 1000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getDefault());
		calendar.setTimeInMillis(time);
		Integer year = calendar.get(Calendar.YEAR);
		Integer month = calendar.get(Calendar.MONTH) + 1;
		Integer date = calendar.get(Calendar.DATE);
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		Integer minute = calendar.get(Calendar.MINUTE);
		Integer second = calendar.get(Calendar.SECOND);
		String str_month = month.toString();
		String str_date = date.toString();
		String str_hour = hour.toString();
		String str_minute = minute.toString();
		String str_second = second.toString();
		if (str_month.length() == 1) {
			str_month = "0" + str_month;
		}
		if (str_date.length() == 1) {
			str_date = "0" + str_date;
		}
		if (str_hour.length() == 1) {
			str_hour = "0" + str_hour;
		}
		if (str_minute.length() == 1) {
			str_minute = "0" + str_minute;
		}
		String str = year + "-" + str_month + "月" + str_date + "日 " + str_hour + ":" + str_minute ;
		return str;
	}

	/**
	 * 显示 时 到 秒 的时间
	 * 
	 * @param time
	 *            秒数
	 * @return
	 */
	public static String getHourToSecondTime(Long time) {
		time = time * 1000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getDefault());
		calendar.setTimeInMillis(time);
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		Integer minute = calendar.get(Calendar.MINUTE);
		Integer second = calendar.get(Calendar.SECOND);
		String str_hour = hour.toString();
		String str_minute = minute.toString();
		String str_second = second.toString();
		if (str_hour.length() == 1) {
			str_hour = "0" + str_hour;
		}
		if (str_minute.length() == 1) {
			str_minute = "0" + str_minute;
		}
		if (str_second.length() == 1) {
			str_second = "0" + str_second;
		}
		String str = str_hour + ":" + str_minute + "--" + str_second;
		return str;
	}

}

