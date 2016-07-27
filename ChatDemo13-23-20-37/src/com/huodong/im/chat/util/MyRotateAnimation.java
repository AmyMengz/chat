package com.huodong.im.chat.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class MyRotateAnimation {

	public static void rotateAnimation(View view) {
		Animation ar = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF,
				0.5f);
		ar.setDuration(800);
		ar.setInterpolator(new LinearInterpolator());// 不停�?
		ar.setRepeatCount(-1);
		ar.setFillAfter(true);
		view.startAnimation(ar);
	}

}
