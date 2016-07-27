package com.huodong.im.chatdemo.fragment;

import java.util.ArrayList;
import java.util.List;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.SelectionActivity;
import com.huodong.im.chatdemo.adapter.DateAdapter;
import com.huodong.im.chatdemo.adapter.MyPagerAdapter;
import com.huodong.im.chatdemo.adapter.NearbyUserAdapter;
import com.huodong.im.chatdemo.widget.MyListView;
import com.huodong.im.chatdemo.widget.TopTabButton;
import com.huodong.im.config.HandlerConstant;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.utils.IMUIHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;

public class NearbyFragment extends BaseFragment {
	private View curView = null;
    private ViewPager pager=null;
    private MyPagerAdapter nearbyAdapter=null;
    private List<Fragment> listFragements;
    private int currentIndex;
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initHandler();
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != curView) {
            ((ViewGroup) curView.getParent()).removeView(curView);
            return curView;
        }
        curView = inflater.inflate(R.layout.fragment_nearby, topContentView);
        initData();
        initView();
        initRes();
        
		return curView;
	}
	private void initData() {
		listFragements =new ArrayList<Fragment>();
		listFragements.add(new NearbyDateFragmentTab());
		listFragements.add(new NearbyUserFragmentTab());
		
	}

	private void initView() {
		nearbyAdapter=new MyPagerAdapter(getChildFragmentManager(), listFragements);
		pager=(ViewPager)curView.findViewById(R.id.vp_content);
		pager.setAdapter(nearbyAdapter);
		setCurrentPager(currentIndex);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				currentIndex=arg0;
				setCurrentPager(currentIndex);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
		
	}

	private void initRes() {
		showTabTopBar();
		hideTopTitle();
//		topTabButton=gettopButton();
		if(topTabTitle!=null){
			topTabTitle.setLeftText(getString(R.string.date_invite));
			topTabTitle.setRightText(getString(R.string.nearby_users));
		}
		setTopRightText(getString(R.string.selection));
		topRightTitleTxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				IMUIHelper.openSelectionActivity(getActivity(),currentIndex);
//				startActivity(new Intent(getActivity(), SelectionActivity.class));
			}
		});
		topTabTitle.getLeftbtn().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentIndex=0;
				setCurrentPager(currentIndex);
			}
		});
		topTabTitle.getRightbtn().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentIndex=1;
				setCurrentPager(currentIndex);
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
		
	private void setCurrentPager(int index){
		switch (index) {
		case 0:
			topTabTitle.setLeftSelected();
			currentIndex=0;
			pager.setCurrentItem(currentIndex);
			break;
		case 1:
			topTabTitle.setRightSelected();
			currentIndex=1;
			pager.setCurrentItem(currentIndex);
			break;
		default:
			break;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case IntentConstant.REQUESTCODE_SELECTION:
			switch (currentIndex) {
			case 0:
				listFragements.get(0).onActivityResult(requestCode, resultCode, data);
				break;
			case 1:
				listFragements.get(1).onActivityResult(requestCode, resultCode, data);
				break;
			}
			break;
		case IntentConstant.REQUESTCODE_DATE_DETAIL:
			listFragements.get(0).onActivityResult(requestCode, resultCode, data);
				break;

		default:
			break;
		}
	}
}
