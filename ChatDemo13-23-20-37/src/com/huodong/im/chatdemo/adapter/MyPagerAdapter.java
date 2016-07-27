package com.huodong.im.chatdemo.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> list_fragments;

	public MyPagerAdapter(FragmentManager fm,List<Fragment> list_fragments) {
		super(fm);
		this.list_fragments=list_fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return list_fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return list_fragments != null ? list_fragments.size() : 0;
	}
	/*public void setListFragmet(List<Fragment> list){
		
	}*/

}
