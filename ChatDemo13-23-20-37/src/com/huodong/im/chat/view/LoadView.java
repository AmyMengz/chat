package com.huodong.im.chat.view;

import com.huodong.im.chat.util.MyRotateAnimation;
import com.huodong.im.chatdemo.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LoadView {
	Activity context;
	public FrameLayout fl_inflate;
	public ImageView iv_init_progress;
	public LinearLayout ll_init_progress;

	public LoadView(Activity context) {
		super();
		this.context = context;
	}

	public View getView() {
		View view = View.inflate(context, R.layout.inflate_base_view, null);
		fl_inflate = (FrameLayout) view.findViewById(R.id.fl_inflate);
		iv_init_progress = (ImageView) view.findViewById(R.id.iv_init_progress);
		ll_init_progress = (LinearLayout) view.findViewById(R.id.ll_init_progress);
		MyRotateAnimation.rotateAnimation(iv_init_progress);
		return view;
	}
	
	public void setView(View view){
		fl_inflate.addView(view);
		fl_inflate.setVisibility(View.VISIBLE);
	}



	public void toLoadInTranlate() {
		ll_init_progress.setBackgroundColor(context.getResources().getColor(R.color.translate1));
		ll_init_progress.setVisibility(View.VISIBLE);
	}

	public void progressVisible() {
		ll_init_progress.setVisibility(View.VISIBLE);
	}

	public void endLoad() {
		ll_init_progress.setVisibility(View.GONE);
	}


}
