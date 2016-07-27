package com.huodong.im.chatdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.view.HeaderLayout;
import com.huodong.im.chatdemo.view.HeaderLayout.HeaderStyle;
import com.huodong.im.chatdemo.view.HeaderLayout.onLeftImageButtonClickListener;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;
import com.huodong.im.chatdemo.view.HeaderLayout.onRightImageButtonClickListener;
/*
 * Activity基类
 */
public class BaseActivity extends Activity{
	public static final String TAG="tag";
//	BmobUserManager userManager;
	
	protected int mScreenWidth;
	protected int mScreenHeight;
	public int uid;
	MyApplication app;
	//公用的Header布局
	private HeaderLayout mHeaderLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app = (MyApplication)getApplication();
		uid=app.getUid();
		DisplayMetrics metric=new DisplayMetrics();
		//将当前窗口的一些信息放在DisplayMetrics类中
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth=metric.widthPixels;
		mScreenHeight=metric.heightPixels;
	}
	/*
	 * 打印toast
	 */
	Toast mToast;
	public void ShowToast(final String text)
	{
		if(!TextUtils.isEmpty(text))
		{
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(mToast==null)
					{
						mToast=Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
					}else{
						mToast.setText(text);
					}
					mToast.show();
				}
			});
		}
	}
	public void ShowToast(final int resId)
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(mToast==null)
				{
					mToast=Toast.makeText(BaseActivity.this.getApplicationContext(),  resId,Toast.LENGTH_SHORT);
				}else{
					mToast.setText(resId);
				}
				mToast.show();
			}
		});
	}
	/*
	 * 打印Log
	 * 
	 */
	public void ShowLog(String msg)
	{
		Log.i("TAG",msg);
	}
	/*
	 * 动画启动页面
	 */
	public void startAnimActivity(Class<?> cla)
	{
		this.startActivity(new Intent(this,cla));
	}
	public void startAnimActivity(Intent intent)
	{
		this.startActivity(intent);
	}
	/*
	 * 只有标题
	 */
	public void initTopBarForOnlyTitle(String titleName)
	{
		mHeaderLayout=(HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.setStyle(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle(titleName); 
	}
	/*
	 * 初始化标题栏-带左右按钮
	 */
	public void initTopBarBoth(String titleName,int rightDrawableId,String text,onRightImageButtonClickListener listener)
	{
		mHeaderLayout=(HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.setStyle(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleaAndLeftImageButton(titleName,R.drawable.base_action_bar_back_bg_selector,new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightImageButton(titleName,rightDrawableId,text,listener);
	}
	public void initTopBarForBoth(String titleName,int rightDrawableId,onRightImageButtonClickListener listener)
	{
		mHeaderLayout=(HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.setStyle(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleaAndLeftImageButton(titleName,R.drawable.base_action_bar_back_bg_selector,new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightImageButton(titleName,rightDrawableId,listener);
	}
	/*
	 * 只有左边按钮和Title
	 */
	public void initTopBarForLeft(String titleName)
	{
		mHeaderLayout=(HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.setStyle(HeaderStyle.TITLE_LIFT_IMAGEBUTTON);
		mHeaderLayout.setTitleaAndLeftImageButton(titleName, R.drawable.base_action_bar_back_bg_selector, new OnLeftButtonClickListener());
	}
	/*
	 * 初始化标题栏-带左边按钮及右边文字
	 */
	public void initTopBarforRightText(String titleName,String rightname,onRighTextClickListener listener)
	{
		
		mHeaderLayout=(HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.setStyle(HeaderStyle.TITLE_LIFT_IMAGEBUTTON_RIGHT_TEXT);
		mHeaderLayout.setTitleaAndLeftImageButton(titleName,R.drawable.base_action_bar_back_bg_selector,new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightText(titleName, rightname,listener);
		
	}
	public void initTopBarforRightTextno(String titleName,String rightname)
	{
		mHeaderLayout=(HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.setStyle(HeaderStyle.TITLE_LIFT_IMAGEBUTTON_RIGHT_TEXTNO);
		mHeaderLayout.setTitleaAndLeftImageButton(titleName,R.drawable.base_action_bar_back_bg_selector,new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightTextno(titleName, rightname);
	}
	/*
	 * 左边按钮的点击事件
	 */
	public class OnLeftButtonClickListener implements onLeftImageButtonClickListener{
		public void onClick()
		{
			finish();
		}
	}
	public void setTopBarRightText(String str){
		mHeaderLayout=(HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.setRightText(str);
	}
}
