package com.huodong.im.chat.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

public class SmileyParser {
	private static SmileyParser sInstance;

	public static SmileyParser getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new SmileyParser(context);
		}
		return sInstance;
	}

	public void tvReplace(CharSequence str, TextView tv) {
		int size = 45;
		CharSequence replace = strToSmiley(str, size);
		tv.setText(replace);
	}

	private final Context mContext;
	private final String[] mSmileyTexts;
	private final Pattern mPattern;
	
	public String[] getmSmileyTexts() {
		return mSmileyTexts;
	}

	private final HashMap<String, Integer> mSmileyToRes;

	private SmileyParser(Context context) {
		mContext = context;
		mSmileyTexts = Expressioin.expressionStr();
		initId();
		mSmileyToRes = buildSmileyToRes();
		mPattern = buildPattern();
	}

	public Integer[] DEFAULT_SMILEY_RES_IDS = new Integer[Expressioin.sIconIds.length];

	private void initId() {
		for (int i = 0; i < Expressioin.sIconIds.length; i++) {
			DEFAULT_SMILEY_RES_IDS[i] = Expressioin.sIconIds[i];
		}
	}

	private HashMap<String, Integer> buildSmileyToRes() {
		if (DEFAULT_SMILEY_RES_IDS.length != mSmileyTexts.length) {
			throw new IllegalStateException("Smiley resource ID/text mismatch");
		}
		HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(mSmileyTexts.length);
		for (int i = 0; i < mSmileyTexts.length; i++) {
			smileyToRes.put(mSmileyTexts[i], DEFAULT_SMILEY_RES_IDS[i]);
		}
		return smileyToRes;
	}

	private Pattern buildPattern() {
		StringBuilder patternString = new StringBuilder(mSmileyTexts.length * 3);
		patternString.append('(');
		for (String s : mSmileyTexts) {
			patternString.append(Pattern.quote(s));
			patternString.append('|');
		}
		patternString.replace(patternString.length() - 1, patternString.length(), ")");
		return Pattern.compile(patternString.toString());
	}

	public CharSequence strToSmiley(CharSequence text, int size) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		Matcher matcher = mPattern.matcher(text);
		while (matcher.find()) {
			int resId = mSmileyToRes.get(matcher.group());
			Drawable drawable = mContext.getResources().getDrawable(resId);
			drawable.setBounds(0, 0, size, size);// ��������ͼƬ�Ĵ�С
			ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
			builder.setSpan(imageSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}
}
