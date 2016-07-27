package com.huodong.im.chat.view;

import com.huodong.im.chat.util.Expressioin;
import com.huodong.im.chatdemo.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ExpressionAdapter extends BaseAdapter {

	Activity mActivity;

	public ExpressionAdapter(Activity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	@Override
	public int getCount() {
		return Expressioin.sIconIds.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(mActivity, R.layout.inflate_image, null);
		ImageView iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
		iv_pic.setBackgroundResource(Expressioin.sIconIds[position]);
		return view;
	}

}
