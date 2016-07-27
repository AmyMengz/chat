package com.huodong.im.chatdemo.widget;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.fragment.NearbyFragment;
import com.huodong.im.config.HandlerConstant;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TopTabButton extends FrameLayout {
	 private Context context = null;
	 /*private Button tabLeftBtn = null;
	 private Button tabRightBtn = null;*/
	 private TextView tabLeftBtn = null;
	 private TextView tabRightBtn = null;

	public TopTabButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
		initView();
	}

	public TopTabButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		initView();
	}

	public TopTabButton(Context context) {
		super(context);
		this.context=context;
		initView();
	}

	private void initView() {
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.top_tab_btn, this);
		/*tabLeftBtn=(Button)findViewById(R.id.left_btn);
		tabRightBtn=(Button)findViewById(R.id.right_btn);*/
		tabLeftBtn=(TextView)findViewById(R.id.left_btn);
		tabRightBtn=(TextView)findViewById(R.id.right_btn);
//		tabRightBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Handler handler=NearbyFragment.getHandler();
//				Message msg=handler.obtainMessage();
//				msg.what=HandlerConstant.HANDLER_CHANGE_NEARBY_TAB;
//				msg.obj=1;//右侧选中
//				handler.sendMessage(msg);
//				setSelTextColor(1);
//				tabRightBtn.setBackgroundResource(R.drawable.nearby_top_right_sel);
//                tabLeftBtn.setBackgroundResource(R.drawable.nearby_top_left_nor);
//			}
//		});
//		tabLeftBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Handler handler=NearbyFragment.getHandler();
//				Message msg=handler.obtainMessage();
//				msg.what=HandlerConstant.HANDLER_CHANGE_NEARBY_TAB;
//				msg.obj=0;//左侧选中
//				handler.sendMessage(msg);
//				setSelTextColor(0);
//				tabRightBtn.setBackgroundResource(R.drawable.nearby_top_right_nor);
//                tabLeftBtn.setBackgroundResource(R.drawable.nearby_top_left_sel);
//			}
//		});
		
	}
	private void setSelTextColor(int index) {
		if (0 == index) {
			tabLeftBtn.setTextColor(getResources().getColor(android.R.color.white));
			tabRightBtn.setTextColor(getResources().getColor(R.color.default_blue_color));
        } else {
        	tabRightBtn.setTextColor(getResources().getColor(android.R.color.white));
        	tabLeftBtn.setTextColor(getResources().getColor(R.color.default_blue_color));
        }
	}
	public void setLeftText(String str){
		tabLeftBtn.setText(str);
	}
	public void setRightText(String str){
		tabRightBtn.setText(str);
	}
	/**
	 * left selected for viewPager
	 */
	public void setLeftSelected(){
		setSelTextColor(0);
		tabRightBtn.setBackgroundResource(R.drawable.nearby_top_right_nor);
        tabLeftBtn.setBackgroundResource(R.drawable.nearby_top_left_sel);
	}
	/**
	 * right selected for viewPager
	 */
	public void setRightSelected(){
		setSelTextColor(1);
		tabRightBtn.setBackgroundResource(R.drawable.nearby_top_right_sel);
        tabLeftBtn.setBackgroundResource(R.drawable.nearby_top_left_nor);
	}
	/*public Button getRightbtn(){
		return tabRightBtn;
	}
	public Button getLeftbtn(){
		return tabLeftBtn;
	}*/
	public TextView getRightbtn(){
		return tabRightBtn;
	}
	public TextView getLeftbtn(){
		return tabLeftBtn;
	}

}
