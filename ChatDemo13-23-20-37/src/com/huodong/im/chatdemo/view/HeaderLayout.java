package com.huodong.im.chatdemo.view;

import com.huodong.im.chatdemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HeaderLayout extends LinearLayout{
	public HeaderLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	private LayoutInflater mInflater;
	private View mHeader;
	//HeaderView控件左侧容器
	private LinearLayout mLayoutLeftContainer;
	//HeaderView控件右侧容器
	private LinearLayout mLayoutRightContainer;
	//标题
	private TextView mHtvSubTitle;
	//右侧按钮布局
	private LinearLayout mLayoutRightImageButtonLayout;
	//右侧按钮
	private Button mRightImageButton;
	//右侧按钮监听接口
	public onRightImageButtonClickListener mRightImageButtonClickListener;
	//左侧按钮布局
	private LinearLayout mLayoutLeftImageButtonLayout;
	//左侧按钮
	private ImageButton mLeftImageButton;
	//左侧按钮监听接口
	public onLeftImageButtonClickListener mLeftImageButtonClickListener;
	//右侧文字
	public TextView mRighttext;
	//右侧文字监听接口
	public onRighTextClickListener mRighTextClickListener;
	//头部整体样式
	public enum HeaderStyle{
		DEFAULT_TITLE, TITLE_LIFT_IMAGEBUTTON, TITLE_RIGHT_IMAGEBUTTON, TITLE_DOUBLE_IMAGEBUTTON,
		TITLE_LIFT_IMAGEBUTTON_RIGHT_TEXT,TITLE_LIFT_IMAGEBUTTON_RIGHT_TEXTNO;
	}
	/*
	 * 设置右侧按钮监听接口
	 */
	public interface onRightImageButtonClickListener{
		void onClick();
	}
	public void setOnRightImageButtonClickListener(onRightImageButtonClickListener listener)
	{
		this.mRightImageButtonClickListener=listener;
	}
	/*
	 * 设置左侧按钮监听接口
	 */
	public interface onLeftImageButtonClickListener{
		void onClick();
	}
	public void setOnLeftImageButtonClickListener(onLeftImageButtonClickListener listener){
		this.mLeftImageButtonClickListener=listener;
	}
	/*
	 * 设置右侧文字监听借口
	 */
	public interface onRighTextClickListener{
		void onClick();
	}
	public void setonRighTextClickListener(onRighTextClickListener listener)
	{
		this.mRighTextClickListener=listener;
	}
	public HeaderLayout(Context context) {
		super(context);
		init(context);
	}
	public HeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context); 
		// TODO Auto-generated constructor stub
	} 
	
	/*
	 * 实现初始化，加载布局文件
	 */
	public void init(Context context)
	{
		mInflater=LayoutInflater.from(context);
		
		mHeader= mInflater.inflate(R.layout.common_header, null);  
		addView(mHeader);
		initViews();
	}
	/*
	 * 初始化控件
	 */
	private void initViews()
	{
		mLayoutLeftContainer = (LinearLayout) findViewByHeaderId(R.id.header_layout_leftview_container);
		// mLayoutMiddleContainer = (LinearLayout)
		// findViewByHeaderId(R.id.header_layout_middleview_container);中间部分添加搜索或者其他按钮时可打开
		mLayoutRightContainer = (LinearLayout) findViewByHeaderId(R.id.header_layout_rightview_container);
		mHtvSubTitle = (TextView) findViewByHeaderId(R.id.header_htv_subtitle);
	}
	public View findViewByHeaderId(int id)
	{
		return mHeader.findViewById(id);
	}
	/*
	 * 设置空间样式
	 */
	public void setStyle(HeaderStyle hStyle)
	{
		switch(hStyle)
		{
		case DEFAULT_TITLE:
			defaultTitle();
			break;
		case TITLE_LIFT_IMAGEBUTTON:
			defaultTitle();
			titleLeftImageButton();
			break;
		case TITLE_RIGHT_IMAGEBUTTON:
			defaultTitle();
			titleRightImageButton();
			break;
		case TITLE_DOUBLE_IMAGEBUTTON:
			defaultTitle();
			titleLeftImageButton();
			titleRightImageButton();
			break;
		case TITLE_LIFT_IMAGEBUTTON_RIGHT_TEXT:
			defaultTitle();
			titleLeftImageButton();
			titleRightcharacter();
			break;
		case TITLE_LIFT_IMAGEBUTTON_RIGHT_TEXTNO://无点击效果的右边文字按钮
			defaultTitle();
			titleLeftImageButton();
			titleRightcharacterno();
		}
	}
	/*
	 * 默认文字标题
	 */
	private void defaultTitle()
	{
		mLayoutLeftContainer.removeAllViews();
		mLayoutRightContainer.removeAllViews();
		
	}
	/*
	 * 自定义左侧按钮
	 */
	private void titleLeftImageButton()
	{
		View mleftImageButtonView=mInflater.inflate(R.layout.common_header_leftbutton, null);
		mLayoutLeftContainer.addView(mleftImageButtonView);
		mLayoutLeftImageButtonLayout=(LinearLayout) mleftImageButtonView.findViewById(R.id.header_layout_imagebuttonlayout);
		mLeftImageButton=(ImageButton) mleftImageButtonView.findViewById(R.id.header_leftbutton);
		mLayoutLeftImageButtonLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mLeftImageButtonClickListener!=null)
				{
					//回调方法，调用onLeftIamgeButtonClickListener接口实现类的方法
					mLeftImageButtonClickListener.onClick();
				}
			}
		});
	}
	/*
	 * 自定义右侧按钮
	 */
	private void titleRightImageButton()
	{
		View mRightImageButtonView=mInflater.inflate(R.layout.common_header_rightbutton, null);
		mLayoutRightContainer.addView(mRightImageButtonView);
		mLayoutRightImageButtonLayout=(LinearLayout) mRightImageButtonView.findViewById(R.id.header_layout_imagebuttonlayout);
		mRightImageButton=(Button) mRightImageButtonView.findViewById(R.id.header_rightbutton);
		mLayoutRightImageButtonLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mRightImageButtonClickListener!=null)
				{
					//回调方法，调用onRightImageButtonClickListener接口实现类的方法
					mRightImageButtonClickListener.onClick();
				}
			}
		});
	}
	/*
	 * 自定义右侧文字，有点击效果
	 */
	private void titleRightcharacter()
	{
		mLayoutRightContainer.removeAllViews();
		View mRightTextview=mInflater.inflate(R.layout.com_header_righttext, null);
		mLayoutRightContainer.addView(mRightTextview);
		mRighttext=(TextView) mRightTextview.findViewById(R.id.header_righttext);
		mRighttext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mRighTextClickListener!=null)
				{
					//回调方法，调用onRightImageButtonClickListener接口实现类的方法
					mRighTextClickListener.onClick();
				}
			}
		});
		
	}
	/*
	 * 自定义右侧文字，无点击效果
	 */
	private void titleRightcharacterno()
	{
		View mRightTextview=mInflater.inflate(R.layout.nocom_header_righttext, null);
		mLayoutRightContainer.addView(mRightTextview);
		mRighttext=(TextView) mRightTextview.findViewById(R.id.header_righttextno);
	}
	/*
	 * 获取右边按钮
	 */
	public Button getRightImageButton()
	{
		if(mRightImageButton!=null)
		{
			return mRightImageButton;
		}
		return null;
	}
	/*
	 * 设置标题
	 */
	public void setDefaultTitle(CharSequence title)
	{
		if(title!=null)
		{
			mHtvSubTitle.setText(title);
		}else{
			mHtvSubTitle.setVisibility(View.GONE);
		}
	}
	/*
	 * 设置右侧按钮侦听接口的实现类，还包括标题文本，按钮图片
	 */
	@SuppressWarnings("deprecation")
	public void setTitleAndRightImageButton(CharSequence title,int id,String text,onRightImageButtonClickListener listener)
	{
		setDefaultTitle(title);
		if(mRightImageButton!=null)
		{
			mRightImageButton.setWidth(45);
			mRightImageButton.setHeight(40);
			mRightImageButton.setTextColor(getResources().getColor(R.color.transparent));
			mRightImageButton.setBackgroundResource(id);
			setOnRightImageButtonClickListener(listener);
		}
		mLayoutRightContainer.setVisibility(View.VISIBLE);
	}
	@SuppressWarnings("deprecation")
	public void setTitleAndRightImageButton(CharSequence title,int backid,onRightImageButtonClickListener onRightImageButtonClickListener)
	{
		setDefaultTitle(title);
		mLayoutRightContainer.setVisibility(View.VISIBLE);
		if(mRightImageButton!=null&&backid>0)
		{
			
			mRightImageButton.setWidth(30);
			mRightImageButton.setHeight(30);
			mRightImageButton.setTextColor(getResources().getColor(R.color.transparent));
			mRightImageButton.setBackgroundResource(backid);
			setOnRightImageButtonClickListener(onRightImageButtonClickListener);
		}
	}
	/*
	 * 设置左侧按钮，右侧文本监听借口实现类，还包括标题文本
	 */
	public void setTitleAndRightText(CharSequence title,String rightText,onRighTextClickListener listener)
	{
		setDefaultTitle(title);
		if(rightText!=null)
		{
			mRighttext.setText(rightText);
			setonRighTextClickListener(listener);
		}
			
	}
	/*
	 * 设置左侧按钮，右侧文本监听借口实现类，还包括标题文本
	 */
	public void setRightText(String rightText)
	{
		if(rightText!=null)
		{
			mRighttext.setText(rightText);	
		}
	}
	/*
	 * 设置左侧按钮，右侧文本监听借口实现类，还包括标题文本,无点击效果
	 */
	public void setTitleAndRightTextno(CharSequence title,String rightText)
	{
		setDefaultTitle(title);
		if(rightText!=null)
		{
			mRighttext.setText(rightText);
			
		}
	}
	/*
	 * 设置左侧按钮侦听接口的实现类，还包括了标题文本，按钮图片
	 */
	public void setTitleaAndLeftImageButton(CharSequence title,int id,onLeftImageButtonClickListener listener)
	{
		setDefaultTitle(title);
		if(mLeftImageButton!=null&&id>0)
		{
			mLeftImageButton.setImageResource(id);
			setOnLeftImageButtonClickListener(listener);
		}
		mLayoutRightContainer.setVisibility(View.VISIBLE);
	}
}
