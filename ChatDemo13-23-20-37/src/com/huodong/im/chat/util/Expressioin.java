package com.huodong.im.chat.util;

import com.huodong.im.chatdemo.R;

public class Expressioin {

	public static final int[] sIconIds = { R.drawable.grinning, R.drawable.blush, R.drawable.grin, R.drawable.smirk,
			R.drawable.relieved, R.drawable.sweat_smile, R.drawable.joy, R.drawable.sunglasses,
			R.drawable.stuck_out_tongue_winking_eye, R.drawable.stuck_out_tongue, R.drawable.unamused, R.drawable.wink,
			R.drawable.heart_eyes, R.drawable.kissing_heart, R.drawable.kissing_closed_eyes, R.drawable.kissing,
			R.drawable.sleeping, R.drawable.weary, R.drawable.expressionless, R.drawable.mask, R.drawable.anguished,
			R.drawable.fearful, R.drawable.cold_sweat, R.drawable.sweat, R.drawable.worried, R.drawable.sob,
			R.drawable.disappointed, R.drawable.cry, R.drawable.angry, R.drawable.rage, R.drawable.triumph,
			R.drawable.clap, R.drawable.ok_hand, R.drawable.thumbsup, R.drawable.v};

	public static String[] expressionStr() {
		String str[] = new String[sIconIds.length];
		for (int i = 0; i < sIconIds.length; i++) {
			str[i] = "[:" + i + "]";
		}
		return str;
	}

}
