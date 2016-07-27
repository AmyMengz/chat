package com.huodong.im.chatdemo.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.MyApplication;
import com.huodong.im.chatdemo.adapter.DateAdapter;
import com.huodong.im.chatdemo.adapter.NearbyUserAdapter;
import com.huodong.im.chatdemo.widget.MyListView;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.SharePreferenceHelper;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.SearchDateEntity;
import com.huodong.im.utils.CommonUtil;
import com.huodong.im.utils.IMDateUserHelper;
import com.huodong.im.utils.IMUIHelper;
import com.huodong.im.utils.LoadDataFromServerNoLooper;
import com.huodong.im.utils.LoadDataFromServerNoLooper.DataCallBackNoLooper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class NewDateStartingFragmentTab extends BaseTabFragment {
	private View curView = null;
	private ListView myDateListView;
    
    private DateAdapter dateAdapter;
    List<SearchDateEntity> searchDateList;
  //请求参数
	private double longitude,latitude;
	private Map<String, String> map;
	//记录约会时间的map
	public static Map<Integer, Long> mapdate=new HashMap<Integer, Long>();
	
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
        curView = inflater.inflate(R.layout.fragment_newdate_starting_tab, null);
        initDate();
        initView();
		return curView;
	}
	private void initDate() {
		searchDateList=new ArrayList<SearchDateEntity>();
		requestMyDate();
		/*SearchDateEntity date=new SearchDateEntity("key","http","title","20","name");
		searchDateList.add(date);
		searchDateList.add(date);*/
		dateAdapter=new DateAdapter(searchDateList, getActivity());
	}
	private void initView() {
		myDateListView=(ListView)curView.findViewById(R.id.my_date_list);
		myDateListView.setAdapter(dateAdapter);
		myDateListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SearchDateEntity currentDateEntity=(SearchDateEntity) dateAdapter.getItem(position);
				int currentDateID=currentDateEntity.getDateKey();
				int sponsorID=currentDateEntity.getSponsorID();
				IMUIHelper.openDateDetailActivity(getActivity(),currentDateEntity);
			}
		});
	}
	public void requestMyDate() {
		longitude=SharePreferenceHelper.getInstance().getLocationLog();
		latitude=SharePreferenceHelper.getInstance().getLocationLat();
		map=new HashMap<String, String>();
		map.put("uid", String.valueOf(uid));
		map.put("sex", String.valueOf(0));
		map.put("feesType", String.valueOf(0));
		map.put("startTime", String.valueOf(System.currentTimeMillis()/1000));
		map.put("endTime", String.valueOf((System.currentTimeMillis()+CommonUtil.oneYear)/1000));
		map.put("longitude", String.valueOf(longitude));
		map.put("latitude", String.valueOf(latitude));
		/*LoadDataFromServerNoLooper loadUserTask=new LoadDataFromServerNoLooper(getActivity(), UrlConstant.MY_DATE_URL, map);*/
		LoadDataFromServerNoLooper loadUserTask=new LoadDataFromServerNoLooper(getActivity(), UrlConstant.NEARBY_DATE_URL, map);
		loadUserTask.getData(new DataCallBackNoLooper() {

			@Override
			public void onDataCallBack(String result) {
				hadleJsonResult(result);
				
				
			}		
		});
	}
	private void hadleJsonResult(String result) {
		JSONObject json;
		try {
			mapdate.clear();
			json = new JSONObject(result);
			String flag=json.getString("flag");
			if(flag.equals("true")){
				JSONArray dataArray=json.getJSONArray("data");
				int datalen=dataArray.length();
				searchDateList.clear();
				long now=System.currentTimeMillis()/1000;
				if(datalen<=0){
					ShowToast(getResources().getString(R.string.no_more_nearby));	
				}else {
						for (int i = 0; i < datalen; i++) {
						
						JSONObject dateInfo=dataArray.getJSONObject(i);
						SearchDateEntity entity=new SearchDateEntity();
						long dateTime=dateInfo.getLong("date_time");
						if(dateTime>now){//约会时间大于当前时间时才为有效的date
							if(dateInfo.getInt("uid")==uid){
								entity.setDateTile(dateInfo.getString("dateTitle"));
								entity.setBirthday(String.valueOf(IMDateUserHelper.getUserAge(dateInfo.getString("birthday"), now)));
								entity.setAddress(dateInfo.getString("address"));
								entity.setFeesType(dateInfo.getInt("feesType"));
								entity.setDistance(dateInfo.getDouble("distance"));
								entity.setApply(dateInfo.getInt("apply"));
								entity.setGender(dateInfo.getInt("sex"));
								entity.setComment(dateInfo.getInt("commentCount"));
								entity.setSponsorID(dateInfo.getInt("uid"));
								entity.setPartnerType(dateInfo.getInt("partner"));
								entity.setPartnerNum(dateInfo.getInt("date_num"));
								entity.setDateTime(dateInfo.getLong("date_time"));
								entity.setDateKey(dateInfo.getInt("dateId"));
								entity.setNickName(dateInfo.getString("name"));
								entity.setDetailAddress(dateInfo.getString("detailAddress"));
								entity.setApplyState(dateInfo.getInt("apply_state"));
								entity.setNotes(dateInfo.getString("notes"));
								entity.setLongitude(dateInfo.getDouble("longitude"));
								entity.setLatitude(dateInfo.getDouble("latitude"));
								mapdate.put(i, dateInfo.getLong("date_time"));
								searchDateList.add(entity);
							}
						}	
					}
				}
				dateAdapter.notifyDataSetChanged();
			}else {
				String errorinfo=json.getString("error_infos");
				ShowToast(getResources().getString(R.string.get_nearby_date_error)+" :"+errorinfo);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        switch (requestCode) {
        	case IntentConstant.REQUESTCODE_DATE_DETAIL:
        		if(resultCode==Activity.RESULT_OK){
        			boolean refush=data.getExtras().getBoolean(IntentConstant.RESULT_DATE_REFUSH); 
        			if(refush){
        				requestMyDate();
        			}
        		}
        }
    }
	
	
}
