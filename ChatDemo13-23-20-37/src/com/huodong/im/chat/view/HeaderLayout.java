package com.huodong.im.chat.view;

import com.huodong.im.chat.AddFriendActivity;
import com.huodong.im.chatdemo.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * �Զ���ͷ������
 * 
 * @ClassName: HeaderLayout
 * @Description: TODO
 * @author smile
 * @date 2014-5-19 ����2:30:30
 */
public class HeaderLayout extends FrameLayout {
	private LayoutInflater mInflater;
	private View mHeader;
	private TextView base_fragment_title;
	private TextView tv_right;
	public ImageView left_image;

	public enum HeaderStyle {// ͷ��������ʽ
		DEFAULT_TITLE, TITLE_LIFT_IMAGEBUTTON, TITLE_RIGHT_IMAGEBUTTON, TITLE_DOUBLE_IMAGEBUTTON;
	}

	Context context;

	public HeaderLayout(Context context) {
		super(context);
		init(context);
	}

	public HeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void init(Context context) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mHeader = mInflater.inflate(R.layout.base_fragment, null);
		addView(mHeader);
		initViews();
	}

	public void initViews() {
		base_fragment_title = (TextView) findViewById(R.id.base_fragment_title);
		tv_right = (TextView) findViewById(R.id.right_text);
		left_image = (ImageView) findViewById(R.id.left_image);
		// tv_right.setVisibility(View.INVISIBLE);

	}

	public void setTitle(CharSequence str) {
		base_fragment_title.setText(str);
	}

	public void setRigthVisibility(boolean isVisibility) {
		if (isVisibility) {
			tv_right.setVisibility(View.VISIBLE);
		} else {
			tv_right.setVisibility(View.GONE);
		}
	}

	public void setLeftImageVisibility(boolean isVisibility) {
		if (isVisibility) {
			left_image.setVisibility(View.VISIBLE);
		} else {
			left_image.setVisibility(View.GONE);
		}
	}

	public void setLeftImage(int imageId) {
		left_image.setImageResource(imageId);
		setLeftImageVisibility(true);
	}

	public void setBack() {
		setLeftImage(R.drawable.base_action_bar_back_bg_n);
		left_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((Activity) context).finish();
			}
		});
	}

}
