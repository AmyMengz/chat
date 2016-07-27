package com.huodong.im.chatdemo.fragment;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.widget.TopTabButton;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

public abstract class BaseFragment extends Fragment {
	protected ImageView topLeftBtn;
	protected ImageView topRightBtn;
	protected TextView topTitleTxt;
	protected TextView topLeftTitleTxt;
	protected TextView topRightTitleTxt;

	protected ViewGroup topBar;
	protected TopTabButton topTabTitle;
	protected ViewGroup topContentView;
//	protected RelativeLayout topLeftContainerLayout;
	protected TopTabButton gettopButton(){
		return topTabTitle;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		topContentView=(ViewGroup)LayoutInflater.from(getActivity()).inflate(R.layout.base_fragment,null);
		ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
		topContentView.setLayoutParams(params);
		topBar = (ViewGroup) topContentView.findViewById(R.id.topbar);
		
		topTabTitle=(TopTabButton)topContentView.findViewById(R.id.neatby_tile);
		topTitleTxt = (TextView) topContentView.findViewById(R.id.base_fragment_title);
		
		topLeftTitleTxt = (TextView) topContentView.findViewById(R.id.left_text);
		topLeftBtn = (ImageView) topContentView.findViewById(R.id.left_image);
		
		topRightTitleTxt = (TextView) topContentView.findViewById(R.id.right_text);
		topRightBtn = (ImageView) topContentView.findViewById(R.id.right_image);
		
		topLeftBtn.setVisibility(View.GONE);
		topLeftTitleTxt.setVisibility(View.GONE);
		topRightBtn.setVisibility(View.GONE);
		topRightTitleTxt.setVisibility(View.GONE);
		topTabTitle.setVisibility(View.GONE);
	}
	/**
	 * ?????????
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != topContentView) {
			((ViewGroup) topContentView.getParent()).removeView(topContentView);
			return topContentView;
		}
		return topContentView;
	}
	/**
	 * set and show Title
	 * @param title
	 */
	protected void setTopTitle(String title) {
		if (title == null) {
			return;
		}
		if (title.length() > 30) {
			title = title.substring(0, 29) + "...";
		}
		topTitleTxt.setText(title);
		topTitleTxt.setVisibility(View.VISIBLE);
	}
	/**
	 * hide title
	 */
	protected void hideTopTitle() {
		topTitleTxt.setVisibility(View.GONE);
	}
	/**
	 * show tabBar
	 */
	protected void showTabTopBar() {
		topTabTitle.setVisibility(View.VISIBLE);
	}
	/**
	 * hide tabBar
	 */
	protected void hideTabTopBar() {
		topTabTitle.setVisibility(View.GONE);
	}
	/**
	 * RightText
	 * @param text
	 */
	protected void setTopRightText(String text) {
		if (null == text) {
			return;
		}
		topRightTitleTxt.setText(text);
		topRightTitleTxt.setVisibility(View.VISIBLE);
	}
	/**
	 * RightButton
	 * @param int
	 */
	protected void setTopRightButton(int resID) {
		if (resID <= 0) {
			return;
		}
		topRightBtn.setImageResource(resID);
		topRightBtn.setVisibility(View.VISIBLE);
	}
	
	protected void hideTopRightButton() {
		topRightBtn.setVisibility(View.GONE);
	}
	/**
	 * RightText
	 * @param text
	 */
	protected void setTopLeftText(String text) {
		if (null == text) {
			return;
		}
		topLeftTitleTxt.setText(text);
		topLeftTitleTxt.setVisibility(View.VISIBLE);
	}
	/**
	 * RightButton
	 * @param int
	 */
	protected void setTopLeftButton(int resID) {
		if (resID <= 0) {
			return;
		}
		topLeftBtn.setImageResource(resID);
		topLeftBtn.setVisibility(View.VISIBLE);
	}
	
	protected void hideTopLeftButton() {
		topLeftBtn.setVisibility(View.GONE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	protected  void initHandler(){};
	
	

}
