package com.huodong.im.chatdemo.fragment;

import java.util.ArrayList;
import java.util.List;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.adapter.DateAdapter;
import com.huodong.im.chatdemo.adapter.MyPagerAdapter;
import com.huodong.im.chatdemo.adapter.NearbyUserAdapter;
import com.huodong.im.chatdemo.widget.MyListView;
import com.huodong.im.chatdemo.widget.TopTabButton;
import com.huodong.im.config.HandlerConstant;
import com.huodong.im.config.IntentConstant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class NewDateFragment extends BaseFragment {
	private View curView = null;
	private MyListView newDateListView;
    private MyListView StartingListView;
    
    private DateAdapter newDateAdapter;
    private NearbyUserAdapter StartingAdapter;
    
//    private TopTabButton topTabButton=null;
    
    private ViewPager pager=null;
    private MyPagerAdapter nearbyAdapter=null;
    private List<Fragment> listFragements;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initHandler();
	}
	static Handler handler;
	public  void initHandler(){
		handler=new Handler(){
			@Override
			public void handleMessage(Message msg){
				setCurrentPager(msg.what);
				((NewDateStartingFragmentTab) listFragements.get(msg.what)).requestMyDate();
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		if (null != curView) {
//            ((ViewGroup) curView.getParent()).removeView(curView);
//            return curView;
//        }
        curView = inflater.inflate(R.layout.fragment_new_date, topContentView);
        initRes();
        initData();
        initView();
		return curView;
	}
	private void initData() {
		listFragements =new ArrayList<Fragment>();
		listFragements.add(new NewDateNewFragmentTab());
		listFragements.add(new NewDateStartingFragmentTab());
		
	}

	private void initView() {
		nearbyAdapter=new MyPagerAdapter(getChildFragmentManager(), listFragements);
		pager=(ViewPager)curView.findViewById(R.id.vp_content);
		pager.setAdapter(nearbyAdapter);
		setCurrentPager(0);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				setCurrentPager(arg0);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
		
	}
	private void setCurrentPager(int index){
		switch (index) {
		case 0:
			topTabTitle.setLeftSelected();
			pager.setCurrentItem(0);
			break;
		case 1:
			topTabTitle.setRightSelected();
			pager.setCurrentItem(1);
			/*((NewDateStartingFragmentTab) listFragements.get(1)).requestMyDate();*/
			break;
		default:
			break;
		}
	}

	private void initRes() {
		showTabTopBar();
		hideTopTitle();
		if(topTabTitle!=null){
			topTabTitle.setLeftText(getString(R.string.satrt_date));
			topTabTitle.setRightText(getString(R.string.starting_date));
		}
		topTabTitle.getLeftbtn().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setCurrentPager(0);
			}
		});
		topTabTitle.getRightbtn().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setCurrentPager(1);
			}
		});
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case IntentConstant.REQUESTCODE_ADDRESS:
			listFragements.get(0).onActivityResult(requestCode, resultCode, data);
			break;
		case IntentConstant.REQUESTCODE_DATE_DETAIL:
			if(listFragements!=null){
				listFragements.get(1).onActivityResult(requestCode, resultCode, data);
			}
			break;
		}

	}

}
