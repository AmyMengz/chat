package com.huodong.im.chatdemo.fragment;

import java.util.ArrayList;
import java.util.Collections;
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
import com.huodong.im.chatdemo.widget.MyListView.OnRefreshListener;
import com.huodong.im.chatdemo.widget.TopTabButton;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.SharePreferenceHelper;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.NearbyUserEntity;
import com.huodong.im.entity.SearchDateEntity;
import com.huodong.im.utils.CommonUtil;
import com.huodong.im.utils.IMDateUserHelper;
import com.huodong.im.utils.IMUIHelper;
import com.huodong.im.utils.LoadDataFromServer;
import com.huodong.im.utils.LoadDataFromServer.DataCallBack;
import com.huodong.im.utils.LoadDataFromServerNoLooper;
import com.huodong.im.utils.LoadDataFromServerNoLooper.DataCallBackNoLooper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NearbyUserFragmentTab extends BaseTabFragment {
	private View curView = null;
    private MyListView nearbyUserListView;
    
    private NearbyUserAdapter nearbyUserAdapter;
    private List<NearbyUserEntity> nearbyUserList;//=new ArrayList<NearbyUserEntity>();
    //请求参数
    private static long activeDegree;
    private static int sex;
	private double longitude,latitude;
	
	private Map<String, String> map;
	
	private boolean isnouser;
	
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
        curView = inflater.inflate(R.layout.fragment_nearby_user_tab, null);
        initDate();
        initView();       
		return curView;
	}

	private void initDate() {
		requestNearByUser();
		nearbyUserList=new ArrayList<NearbyUserEntity>();
		nearbyUserAdapter=new NearbyUserAdapter(getActivity(), nearbyUserList);
	}
	/**
	 * send request nearbyUser;
	 */
	private void requestNearByUser() {
		
		longitude=SharePreferenceHelper.getInstance().getLocationLog();
		latitude=SharePreferenceHelper.getInstance().getLocationLat();
		map=new HashMap<String, String>();
		map.put("uid", String.valueOf(uid));
		map.put("longitude", String.valueOf(longitude));
		map.put("latitude", String.valueOf(latitude));
		if(sex!=0){
			map.put("sex", String.valueOf(sex));
		}
		if (activeDegree!=0) {
			map.put("active_degree",String.valueOf(activeDegree));
		}
			LoadDataFromServerNoLooper loadUserTask=new LoadDataFromServerNoLooper(getActivity(), UrlConstant.NEARBY_USER_URL, map);
			loadUserTask.getData(new DataCallBackNoLooper() {
				@Override
				public void onDataCallBack(String result) {
					if(result!=null){
						hadleJsonResult(result);
						now_=System.currentTimeMillis();
						if(now_-last<1000){
							try {
								Thread.sleep(1000-(now_-last));
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						nearbyUserListView.onRefreshComplete();
					}	
				}

			});
	}
	/**
	 * 处理返回的JSON数据
	 * @param result
	 */
	private void hadleJsonResult(String result) {
		try {
			JSONObject json=new JSONObject(result);
			String flag=json.getString("flag");
			if(flag.equals("true")){
				JSONArray dataArray=json.getJSONArray("data");
				int datalen=dataArray.length();
				nearbyUserList.clear();
				long now=json.getLong("current_time");
				if(datalen<=0){
					isnouser=true;
					ShowToast(getResources().getString(R.string.no_more_nearby));	
				}
				else {
					for (int i = 0; i < datalen; i++) {
						NearbyUserEntity nearUser=new NearbyUserEntity();
						JSONObject userInfo=dataArray.getJSONObject(i);
						nearUser.setUid(userInfo.getInt("uid"));
//							long bir=CommonUtil.getTimestamp(userInfo.getString("birthday"), CommonUtil.timeFormat2)/1000;
//							long age=(now-bir)/(24*60*60*365);
						nearUser.setBirthday(userInfo.getString("birthday"));
						nearUser.setGender(userInfo.getInt("sex"));
						
						nearUser.setDistance(IMDateUserHelper.formatDistance(userInfo.getDouble("distance")));
						/*nearUser.setDistance(userInfo.getDouble("distance"));*/
						nearUser.setNickName(userInfo.getString("name"));
						nearUser.setDateTimes(userInfo.getInt("date_count"));
						nearUser.setFans(userInfo.getInt("fds_count"));
						nearUser.setSignature(userInfo.getString("PSignature"));
						long recenttime=userInfo.getLong("recentTime");
						long recent=(now-recenttime)/60/60;
						nearUser.setRecentTime(recent);
						nearbyUserList.add(nearUser);
					}
					Collections.sort(nearbyUserList);
					
				}
				nearbyUserAdapter.notifyDataSetChanged();
			}else {
				String errorinfo=json.getString("error_infos");
				ShowToast(getResources().getString(R.string.get_nearby_error)+" :"+errorinfo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	long last=0,now_=0;
	private void initView() {
		nearbyUserListView=(MyListView)curView.findViewById(R.id.nearby_user_list);
		nearbyUserListView.setAdapter(nearbyUserAdapter);
		nearbyUserListView.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				last=System.currentTimeMillis();
				requestNearByUser();	
			}
		});
		nearbyUserListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("Amy nearUSer", "position   "+position+"   "+nearbyUserList.get(position-1).getFans());
				IMUIHelper.openUserDetailActivity(getActivity(), nearbyUserList.get(position-1).getNickName(),nearbyUserList.get(position-1).getUid(),nearbyUserList.get(position-1).getFans());
				
			}
		});
		
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		switch (requestCode) {
		case IntentConstant.REQUESTCODE_SELECTION:
			int gender_user=data.getExtras().getInt(IntentConstant.KEY_GENDER_USER); 
			int active_user=data.getExtras().getInt(IntentConstant.KEY_ACTIVE_USER);
			setParams(gender_user,active_user);
			
			break;
		

		default:
			break;
		}
	}
	/**
	 * set request params from SelectionActivity
	 * @param gender_user
	 * @param active_user
	 */

	private void setParams(int gender_user, int active_user) {	
		switch (gender_user) {
		case 1:
			sex=2;
			break;
		case 2:
			sex=1;
			break;
		case 0:
			sex=0;
			break;
		}
		long oneDay=24*60*60*1000;
		switch (active_user) {
		case 0:
			/*long now0=System.currentTimeMillis();
			activeDegree=(now0-100*365*oneDay)/1000;*/
			activeDegree=0;
			break;
		case 1:
			long now1=System.currentTimeMillis();
			activeDegree=(now1-oneDay)/1000;
			break;
		case 2:
			long now2=System.currentTimeMillis();
			activeDegree=(now2-3*oneDay)/1000;
			break;
		case 3:
			long now3=System.currentTimeMillis();
			activeDegree=(now3-7*oneDay)/1000;
			break;
		default:
			break;
		}
		requestNearByUser();
	}
}
