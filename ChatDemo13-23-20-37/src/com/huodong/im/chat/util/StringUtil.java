package com.huodong.im.chat.util;

import com.huodong.im.chatdemo.R;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

public class StringUtil {


	public static boolean isNumber(String str) {
		return str.matches("[0-9]+");
	}
	
	public static CharSequence SpanSize(CharSequence str1,int size) {
		SpannableStringBuilder builder = new SpannableStringBuilder(str1);
		builder.setSpan(new AbsoluteSizeSpan(size), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}
	public static CharSequence SpanColor(CharSequence str1,int color) {
		SpannableStringBuilder builder = new SpannableStringBuilder(str1);
		builder.setSpan(new ForegroundColorSpan(color), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return builder;
	}

	public static CharSequence SpanAppendLn(Object... params) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		for (int i = 0; i < params.length; i++) {
			CharSequence cs = (CharSequence) params[i];
			if (TextUtils.isEmpty(cs))
				continue;
			if (i != 0)
				builder.append("\n");
			builder.append(cs);
		}
		return builder;
	}
	
	public static CharSequence SpanAppend(Object... params) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		for (int i = 0; i < params.length; i++) {
			CharSequence cs = (CharSequence) params[i];
			if (TextUtils.isEmpty(cs))
				continue;
			builder.append(cs);
		}
		return builder;
	}
	
	public static String getStr(Context context,int str_id){
		String string = context.getResources().getString(str_id);
		return string;
	}
	public static int getColor(Context context,int colorId){
		int  color = context.getResources().getColor(colorId);
		return color;
	}
	public static CharSequence spanNameUid(String name,int uid){
		CharSequence spanSize2 = SpanSize(name, IMConstants.TEXT_SIZE_38);
		CharSequence spanSize3 = SpanSize(" (" + uid + ")", IMConstants.TEXT_SIZE_30);
		CharSequence spanColor = SpanColor(spanSize3, Color.GRAY);
		CharSequence spanAppend = SpanAppend(spanSize2,spanColor);
		return spanAppend;
	}
	
	
	

}
