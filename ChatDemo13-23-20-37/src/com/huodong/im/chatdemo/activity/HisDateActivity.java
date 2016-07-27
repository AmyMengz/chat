package com.huodong.im.chatdemo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.adapter.DateAdapter;
import com.huodong.im.chatdemo.controller.NearbyUserDetailController;
import com.huodong.im.chatdemo.controller.NearbyUserDetailController.NearbyUserDateCallback;
import com.huodong.im.chatdemo.controller.NearbyUserDetailController.NearbyUserDetailCallback;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.SharePreferenceHelper;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.SearchDateEntity;
import com.huodong.im.entity.UserEntity;
import com.huodong.im.utils.CommonUtil;
import com.huodong.im.utils.IMDateUserHelper;
import com.huodong.im.utils.IMUIHelper;
import com.huodong.im.utils.LoadDataFromServerNoLooper;
import com.huodong.im.utils.LoadDataFromServerNoLooper.DataCallBackNoLooper;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class HisDateActivity extends BaseActivity{
//	int uid=31;
	ListView hisDate;
//	long mLongtitud,mLatitude;
	 private DateAdapter dateAdapter;
	List<SearchDateEntity> searchDateList;
	private ProgressBar processBar;
	private TextView no_list;
	//请求参数
	double mLongitud,mLatitude;
	private Map<String, String> map;
	int hisId;
	SharePreferenceHelper helper=SharePreferenceHelper.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_his_date);
		mLongitud= helper.getLocationLog();
		mLatitude=helper.getLocationLat();
		hisId=getIntent().getIntExtra(IntentConstant.KEY_UID, 0);
		
		initView();
		initDate();
		
		
	}

	private void initDate() {
		
	}

	private void initView() {
		
		initTopBarForLeft(getString(R.string.he_date_count));
		no_list=(TextView)findViewById(R.id.no_list);
		processBar=(ProgressBar)findViewById(R.id.processBar);
		hisDate=(ListView)findViewById(R.id.his_date_list);
		searchDateList=new ArrayList<SearchDateEntity>();
		requestMyDate();
		dateAdapter=new DateAdapter(searchDateList, HisDateActivity.this);
		Log.i("err", " "+(dateAdapter==null)+" "+(hisDate==null));
		hisDate.setAdapter(dateAdapter);
		hisDate.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SearchDateEntity currentDateEntity=(SearchDateEntity) dateAdapter.getItem(position);
				int currentDateID=currentDateEntity.getDateKey();
				int sponsorID=currentDateEntity.getSponsorID();
				IMUIHelper.openDateDetailActivity(HisDateActivity.this,currentDateEntity);
			}
		});
		
		
	}
	public void requestMyDate() {
		
		map=new HashMap<String, String>();
		map.put("uid", String.valueOf(uid));
		map.put("sex", String.valueOf(0));
		map.put("feesType", String.valueOf(0));
		map.put("startTime", String.valueOf(System.currentTimeMillis()/1000));
		map.put("endTime", String.valueOf((System.currentTimeMillis()+CommonUtil.oneYear)/1000));
		map.put("longitude", String.valueOf(mLongitud));
		map.put("latitude", String.valueOf(mLatitude));
		/*LoadDataFromServerNoLooper loadUserTask=new LoadDataFromServerNoLooper(getActivity(), UrlConstant.MY_DATE_URL, map);*/
		LoadDataFromServerNoLooper loadUserTask=new LoadDataFromServerNoLooper(HisDateActivity.this, UrlConstant.NEARBY_DATE_URL, map);
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
			json = new JSONObject(result);
			String flag=json.getString("flag");
			if(flag.equals("true")){
				JSONArray dataArray=json.getJSONArray("data");
				int datalen=dataArray.length();
				searchDateList.clear();
				long now=System.currentTimeMillis()/1000;
				if(datalen<=0){
					ShowToast(getResources().getString(R.string.no_more_nearby));	
					no_list.setVisibility(View.VISIBLE);
					processBar.setVisibility(View.GONE);
					
				}else {
					for (int i = 0; i < datalen; i++) {
						JSONObject dateInfo=dataArray.getJSONObject(i);
						SearchDateEntity entity=new SearchDateEntity();
						long dateTime=dateInfo.getLong("date_time");
						if(dateTime>now){//约会时间大于当前时间时才为有效的date
							if(dateInfo.getInt("uid")==hisId){
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
								Log.i("Amy state_apply", dateInfo.getInt("apply_state")+"");
								searchDateList.add(entity);
							}
						}
						/*JSONObject dateInfo=dataArray.getJSONObject(i);
						SearchDateEntity entity=new SearchDateEntity();
						entity.setDateTile(dateInfo.getString("dateTitle"));
						long bir=CommonUtil.getTimestamp(dateInfo.getString("birthday"), CommonUtil.timeFormat2)/1000;
						long age=(now-bir)/(24*60*60*365);
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
						searchDateList.add(entity);*/
					}
					Log.i("Amy hisDate","----"+searchDateList.size());
					processBar.setVisibility(View.GONE);
					no_list.setVisibility(View.GONE);
				}
				IMUIHelper.setListViewHeightBasedOnChildren(hisDate);
				dateAdapter.notifyDataSetChanged();
				
			}else {
				String errorinfo=json.getString("error_infos");
				ShowToast(getResources().getString(R.string.get_nearby_date_error)+" :"+errorinfo);
				processBar.setVisibility(View.GONE);
				no_list.setVisibility(View.VISIBLE);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

}
