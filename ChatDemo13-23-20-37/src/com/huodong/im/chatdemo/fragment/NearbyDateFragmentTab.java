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
import com.huodong.im.chatdemo.adapter.LvAdapter;
import com.huodong.im.chatdemo.adapter.NearbyUserAdapter;
import com.huodong.im.chatdemo.widget.MyListView;
import com.huodong.im.chatdemo.widget.MyListView.OnRefreshListener;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.SharePreferenceHelper;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.SearchDateEntity;
import com.huodong.im.utils.CommonUtil;
import com.huodong.im.utils.IMDateUserHelper;
import com.huodong.im.utils.IMUIHelper;
import com.huodong.im.utils.LoadDataFromServerNoLooper;
import com.huodong.im.utils.LoadDataFromServerNoLooper.DataCallBackNoLooper;

import android.content.Intent;
import android.net.NetworkInfo.DetailedState;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class NearbyDateFragmentTab extends BaseTabFragment {
	private View curView = null;
	private MyListView dateListView;
    private DateAdapter dateAdapter;
    List<SearchDateEntity> searchDateList;
    private EditText query;
    private ImageButton clearSearch;
   
    
  //请求参数
    private static long startTime=System.currentTimeMillis()/1000,endTime=(System.currentTimeMillis()+CommonUtil.oneYear)/1000;;
    private static int sex,feesType;
	private double longitude,latitude;
	private Map<String, String> map;
	
	long last=0,now_=0;
	EditText results;
	
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
        results=(EditText)curView.findViewById(R.id.result);
        initDate();
        initView();
        
		return curView;
	}

	private void initDate() {
		searchDateList=new ArrayList<SearchDateEntity>();
		requestNearByDate();
		dateAdapter=new DateAdapter(searchDateList, getActivity());
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
				 filterData(s.toString());
			}
		});
		clearSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				query.getText().clear();
				
			}
		});
		dateListView=(MyListView)curView.findViewById(R.id.date_list);
		dateListView.setAdapter(dateAdapter);
		dateListView.setonRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				last=System.currentTimeMillis();
				requestNearByDate();
			}
		});
		dateListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SearchDateEntity currentDateEntity=(SearchDateEntity) dateAdapter.getItem(position-1);
//				Log.i("Amy item", "position   "+position);
				int currentDateID=currentDateEntity.getDateKey();
				int sponsorID=currentDateEntity.getSponsorID();
				/*IMUIHelper.openDateDetailActivity(getActivity(),currentDateID,sponsorID);*/
				IMUIHelper.openDateDetailActivity(getActivity(),currentDateEntity);
			}
		});
		
	}
	
	@Override
	public void onResume() {
//		requestNearByDate();
		MyApplication app = (MyApplication) getActivity().getApplication();
		
		boolean isRefush=app.getRefresh();
		Log.i("Amy isRefush", "isRefush   "+isRefush);
		if(isRefush){
			requestNearByDate();
			app.setRefresh(false);
		}
		super.onResume();
	}

	private void requestNearByDate() {
		longitude=SharePreferenceHelper.getInstance().getLocationLog();
		latitude=SharePreferenceHelper.getInstance().getLocationLat();
		map=new HashMap<String, String>();
		map.put("uid", String.valueOf(uid));
		map.put("sex", String.valueOf(sex));
		map.put("feesType", String.valueOf(feesType));
		map.put("startTime", String.valueOf(startTime));
		map.put("endTime", String.valueOf(endTime));
//		map.put("dateTime", String.valueOf(Long.MAX_VALUE));
		map.put("longitude", String.valueOf(longitude));
		map.put("latitude", String.valueOf(latitude));
		LoadDataFromServerNoLooper loadUserTask=new LoadDataFromServerNoLooper(getActivity(), UrlConstant.NEARBY_DATE_URL, map);
		loadUserTask.getData(new DataCallBackNoLooper() {

			@Override
			public void onDataCallBack(String result) {
				results.setText(result);
				hadleJsonResult(result);
				now_=System.currentTimeMillis();
				if(now_-last<1000){
					try {
						Thread.sleep(1000-(now_-last));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				dateListView.onRefreshComplete();
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
				}else {
					for (int i = 0; i < datalen; i++) {
						
						JSONObject dateInfo=dataArray.getJSONObject(i);
						SearchDateEntity entity=new SearchDateEntity();
						long dateTime=dateInfo.getLong("date_time");
						if(dateTime>now){//约会时间大于当前时间时才为有效的date
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
							searchDateList.add(entity);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		switch (requestCode) {
		case IntentConstant.REQUESTCODE_SELECTION:
			int gender_limit=data.getExtras().getInt(IntentConstant.KEY_GENDER_LIMIT); 
			int fees_limit=data.getExtras().getInt(IntentConstant.KEY_FEES_LIMIT);
			int time_limit=data.getExtras().getInt(IntentConstant.KEY_TIME_LIMIT);
			setParams(gender_limit,fees_limit,time_limit);
			break;
		case IntentConstant.REQUESTCODE_DATE_DETAIL:
			boolean refush=data.getExtras().getBoolean(IntentConstant.RESULT_DATE_REFUSH); 
			if(refush){
				requestNearByDate();
			}
			break;

		default:
			break;
		}
	}
	long oneDay=24*60*60*1000;
	private void setParams(int gender_limit, int fees_limit, int time_limit) {
		sex=gender_limit;
		feesType=fees_limit;
		switch (time_limit) {
		case 0:
			startTime=System.currentTimeMillis()/1000;
			endTime=(System.currentTimeMillis()+CommonUtil.oneYear)/1000;
			break;
		case 1:
			startTime=System.currentTimeMillis()/1000;
			endTime=(CommonUtil.getDayEnd())/1000;
			break;
		case 2:
			startTime=(CommonUtil.getDayBegin()+oneDay)/1000;
			endTime=(CommonUtil.getDayEnd()+oneDay)/1000;
			break;
		case 3:
			startTime=System.currentTimeMillis()/1000;
			endTime=(CommonUtil.getDayEnd()+oneDay*7)/1000;

		default:
			break;
		}
//		Log.i("Amy curtime", System.currentTimeMillis()/1000+"  ");
		requestNearByDate();
		/*dateTime;
	    private static int sex,feesType;*/
	}
	
	private void filterData(String filterStr) {  
        List<SearchDateEntity> filterDateList = new ArrayList<SearchDateEntity>();  
  
        if (TextUtils.isEmpty(filterStr)) {  
            filterDateList = searchDateList;  
        } else {  
            filterDateList.clear();  
            for (SearchDateEntity sortModel : searchDateList) {  
                String name = sortModel.getDateTitle();  
                if (name.toUpperCase().indexOf(  
                        filterStr.toString().toUpperCase()) != -1  
                        /*|| characterParser.getSelling(name).toUpperCase()  
                                .startsWith(filterStr.toString().toUpperCase())*/) {  
                    filterDateList.add(sortModel);  
                }  
            }  
        }  
          
        // 根据a-z进行排序  
//        Collections.sort(filterDateList, pinyinComparator);  
//        dateAdapter.
        dateAdapter.updateListView(filterDateList);  
    }  
	
	
}
