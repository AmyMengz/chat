package com.huodong.im.chatdemo.fragment;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ls.LSInput;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.adapter.DateAdapter;
import com.huodong.im.chatdemo.adapter.LvAdapter;
import com.huodong.im.chatdemo.adapter.NearbyUserAdapter;
import com.huodong.im.chatdemo.widget.MyListView;
import com.huodong.im.chatdemo.widget.MyListView.OnRefreshListener;
import com.huodong.im.entity.SearchDateEntity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class NearbyDateFragment extends Fragment {
	private View curView = null;
	private MyListView dateListView;
    private DateAdapter dateAdapter;
//    LvAdapter adapter;
//    List<String> list;
    List<SearchDateEntity> searchDateList;
    private EditText query;
    private ImageButton clearSearch;
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != curView) {
            ((ViewGroup) curView.getParent()).removeView(curView);
            return curView;
        }
        curView = inflater.inflate(R.layout.fragment_neatby_date_tab, null);
        initView();
        
		return curView;
	}

	private void initView() {
		query = (EditText) curView.findViewById(R.id.query);
		clearSearch=(ImageButton)curView.findViewById(R.id.search_clear);
		query.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0) {
					clearSearch.setVisibility(View.VISIBLE);
				} else {
					clearSearch.setVisibility(View.INVISIBLE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		clearSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				query.getText().clear();
				
			}
		});
		dateListView=(MyListView)curView.findViewById(R.id.date_list);
		searchDateList=new ArrayList<SearchDateEntity>();
//		SearchDateEntity date=new SearchDateEntity("key","http","title","20","name");
//		searchDateList.add(date);
//		searchDateList.add(date);
//		list=new ArrayList<String>();
//		list.add("aaa");
//		adapter=new LvAdapter(list, getActivity());
		dateAdapter=new DateAdapter(searchDateList, getActivity());
		dateListView.setAdapter(dateAdapter);
		dateListView.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {  
                    protected Void doInBackground(Void... params) {  
                        try {  
                            Thread.sleep(1000);  
                            //网络获取信数据
                        } catch (Exception e) {  
                            e.printStackTrace();  
                        }  
//                        list.add("刷新后添加的内容");  
                        return null;  
                    }  
  
                    @Override  
                    protected void onPostExecute(Void result) {  
                    	dateAdapter.notifyDataSetChanged();  
                        dateListView.onRefreshComplete();  
                    }  
                }.execute(null, null, null);  
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
