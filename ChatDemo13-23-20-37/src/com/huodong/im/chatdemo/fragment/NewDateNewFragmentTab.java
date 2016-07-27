package com.huodong.im.chatdemo.fragment;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.MyApplication;
import com.huodong.im.chatdemo.adapter.DateAdapter;
import com.huodong.im.chatdemo.adapter.NearbyUserAdapter;
import com.huodong.im.chatdemo.widget.DateTimePickDialogUtil;
import com.huodong.im.chatdemo.widget.DateTimePickDialogUtil.DateTimerCallback;
import com.huodong.im.chatdemo.widget.MyListView;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.SharePreferenceHelper;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.NewDateEntity;
import com.huodong.im.utils.CommonUtil;
import com.huodong.im.utils.IMDateUserHelper;
import com.huodong.im.utils.IMUIHelper;
import com.huodong.im.utils.LoadDataFromServer;
import com.huodong.im.utils.LoadDataFromServerNoLooper;
import com.huodong.im.utils.LoadDataFromServerNoLooper.DataCallBackNoLooper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewDateNewFragmentTab extends BaseTabFragment implements OnClickListener{
	private View curView = null;
	private EditText dateTitle,datePartnerNum,dateNote;
	private TextView dateTime,dateAddress;
	private TextView datePartnerType[];
	private TextView dateFeesType[];
	private Button releaseDateBtn;
	int partnerIndex,feesIndex;
	private NewDateEntity newDateEntity;
	private String mdateTitle,mdatePartnerNum,mdateTime,mdateAddress,mdateNote,mDtailAddress;
	private String mdatePartnerType,mdateFeesType,mdateAddressLog,mdateAddressLat;
	private Map<String, String> map;
	private String longitude,latitude;
	private String currentTime;
	
	
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
        curView = inflater.inflate(R.layout.fragment_newdate_new_tab, null); 
        initView();
        initDate();
		return curView;
	}

	private void initDate() {
//		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		getPublishedDateCount();
		newDateEntity=new NewDateEntity();
		setSelectedPartner(partnerIndex);
		setSelectedFees(feesIndex);
		map=new HashMap<String, String>();
		longitude=String.valueOf(SharePreferenceHelper.getInstance().getLocationLog());
		latitude=String.valueOf(SharePreferenceHelper.getInstance().getLocationLat());
		
		mdateAddressLog=longitude;
		mdateAddressLat=latitude;
	}
	private void initView() {
		dateTitle=(EditText)curView.findViewById(R.id.date_title);
		datePartnerNum=(EditText)curView.findViewById(R.id.date_partner_num);
		dateTime=(TextView)curView.findViewById(R.id.date_time);
		dateAddress=(TextView)curView.findViewById(R.id.date_address);
		dateNote=(EditText)curView.findViewById(R.id.date_notes);
		releaseDateBtn=(Button)curView.findViewById(R.id.btn_release_date);
		datePartnerType=new TextView[3];
		datePartnerType[0]=(TextView)curView.findViewById(R.id.date_partner_type_nogender);
		datePartnerType[1]=(TextView)curView.findViewById(R.id.date_partner_type_women);
		datePartnerType[2]=(TextView)curView.findViewById(R.id.date_partner_type_man);
		dateFeesType=new TextView[4];
		dateFeesType[0]=(TextView)curView.findViewById(R.id.date_fees_type_i);
		dateFeesType[1]=(TextView)curView.findViewById(R.id.date_fees_type_man_a);
		dateFeesType[2]=(TextView)curView.findViewById(R.id.date_fees_type_aa);
		dateFeesType[3]=(TextView)curView.findViewById(R.id.date_fees_type_u);
		releaseDateBtn.setOnClickListener(this);
		for (int i = 0; i < 3; i++) {
			datePartnerType[i].setOnClickListener(this);
		}
		for (int i = 0; i < 4; i++) {
			dateFeesType[i].setOnClickListener(this);

		}
		dateTime.setOnClickListener(this);
		dateAddress.setOnClickListener(this);
		}	
	private void getInfo(){
		mdateTitle=dateTitle.getText().toString();
		mdatePartnerNum=datePartnerNum.getText().toString();
		if(!TextUtils.isEmpty(dateTime.getText().toString().trim())){
			mdateTime=IMDateUserHelper.paraseFormatedDateTime(dateTime.getText().toString());
			mdateTime=String.valueOf(CommonUtil.getTimestamp(mdateTime, CommonUtil.timeFormat1)/1000);
		}
		
		mdateAddress=dateAddress.getText().toString();
		mdateNote=dateNote.getText().toString();
		map.put("UId", String.valueOf(uid));
		map.put("dateTitle",mdateTitle);
		map.put("datePartnerNum",mdatePartnerNum);
		map.put("datePartnerType",mdatePartnerType);
		map.put("dateFeesType",mdateFeesType);//
		map.put("dateTime",mdateTime);
		map.put("dateAddressLog",mdateAddressLog);
		map.put("dateAddressLat",mdateAddressLat);
		map.put("dateNotes",mdateNote);
		map.put("address",mdateAddress);
		map.put("detailAddress",mDtailAddress);
	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.date_partner_type_nogender:
			partnerIndex=0;
			setSelectedPartner(partnerIndex);
			break;
		case R.id.date_partner_type_women:
			partnerIndex=1;
			setSelectedPartner(partnerIndex);
			break;
		case R.id.date_partner_type_man:
			partnerIndex=2;
			setSelectedPartner(partnerIndex);
			break;

		case R.id.date_fees_type_i:
			feesIndex=0;
			setSelectedFees(feesIndex);
			break;
		case R.id.date_fees_type_man_a:
			feesIndex=1;
			setSelectedFees(feesIndex);
			break;
		case R.id.date_fees_type_aa:
			feesIndex=2;
			setSelectedFees(feesIndex);
			break;
		case R.id.date_fees_type_u:
			feesIndex=3;
			setSelectedFees(feesIndex);
			break;
		case R.id.btn_release_date:
			getInfo();
			if(verifyInfo()){
				LoadDataFromServerNoLooper loadTask=new LoadDataFromServerNoLooper(getActivity(), UrlConstant.NEW_DATE_URL, map);
				loadTask.getData(new DataCallBackNoLooper() {
					
					@Override
					public void onDataCallBack(String result) {
						if (result!=null) {
							try {
								JSONObject jobject=new JSONObject(result);
								String flag=jobject.getString("flag");
								if(flag.equals("true")){
									ShowToast(getString(R.string.release_ok_tip));
									MyApplication app = (MyApplication) getActivity().getApplication(); 
									app.setRefresh(true);
									NewDateFragment.handler.sendEmptyMessage(1);
//									((NewDateStartingFragmentTab) listFragements.get(1)).requestMyDate();
								}
								else {
									String error=jobject.getString("error_infos");
									ShowToast(getString(R.string.release_error_tip)+error);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								ShowToast(getString(R.string.release_error_tip)+e);
							}
							
						}
					}
				});
			}
			break;
		case R.id.date_time:
			if(TextUtils.isEmpty(dateTime.getText())){
				currentTime=IMDateUserHelper.getDateTmieInit();
			}
			else {
				currentTime=IMDateUserHelper.paraseFormatedDateTime(dateTime.getText().toString());
			}
			 
			DateTimePickDialogUtil dateTimePicKDialog=new DateTimePickDialogUtil(getActivity(), currentTime);
			dateTimePicKDialog.dateTimePicKDialog(dateTime, new DateTimerCallback() {
				
				@Override
				public void onDateTimerCallback(String result) {
					int flag=IMDateUserHelper.getInvalidDateTimeTips(result);
					Log.i("Amy  flag", result+"===="+flag+"");
					switch (flag) {
					case IntentConstant.DATE_TIME_INVALID:
						ShowToast(getString(R.string.date_time_invalid));
						dateTime.setText(IMDateUserHelper.formatDateTime(currentTime));
						break;
					case IntentConstant.DATE_TIME_TOO_CLOSE:
						ShowToast(getString(R.string.date_time_too_close));
						dateTime.setText(IMDateUserHelper.formatDateTime(currentTime));
						break;
					case IntentConstant.DATE_TIME_TOO_FAR:
						ShowToast(getString(R.string.date_time_too_far));
						dateTime.setText(IMDateUserHelper.formatDateTime(currentTime));
						break;
					default:
						dateTime.setText(result);
						break;
					}
				}
			});
			
			break;
		case R.id.date_address:
			IMUIHelper.openSeleteAddressActivity(getActivity(), "31");
		}
		
	}
	/**
	 * 验证输入信息
	 * @return
	 */
	private boolean verifyInfo() {
//		Log.i("Amy ver", IMDateUserHelper.getInvalidDateTime(mdateTime, NewDateStartingFragmentTab.mapdate)+"");
		getPublishedDateCount();
		if(TextUtils.isEmpty(mdateTitle)){
			ShowToast(getString(R.string.date_title_need));
			return false;
		}else if (TextUtils.isEmpty(mdatePartnerNum)) {
			ShowToast(getString(R.string.date_partner_num_need));
			return false;
		}else if (TextUtils.isEmpty(mdateTime)) {
			ShowToast(getString(R.string.date_time_need));
			return false;
		}else if (!IMDateUserHelper.getInvalidDateTime(mdateTime, NewDateStartingFragmentTab.mapdate)) {//两小时内是否存在约会
			ShowToast(getString(R.string.date_time_limit));
			return false;
		}
		else if (TextUtils.isEmpty(mdateAddress)) {
			ShowToast(getString(R.string.date_address_need));
			return false;
		}else if(datecount>=2){//GET_DATE_COUNT_URL
			ShowToast(getString(R.string.date_count_limit));
			return false;
		}
		else {
			return true;
		}
	}
	int datecount=0;
	private void getPublishedDateCount(){
		Map<String, String>  map=new HashMap<String, String>();
		map.put("uid", String.valueOf(uid));
		LoadDataFromServerNoLooper load=new LoadDataFromServerNoLooper(getActivity(), UrlConstant.GET_DATE_COUNT_URL, map);
		load.getData(new DataCallBackNoLooper() {
			@Override
			public void onDataCallBack(String result) {
				if(result!=null){
					try {
						JSONObject info=new JSONObject(result);
						String flag=info.getString("flag");
						if(flag.equals("true")){
							datecount=info.getInt("date_count");	
							Log.i("Amy count", datecount+"");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});		
	}
	
	/**
	 * 选择费用规则
	 * @param feesIndex2
	 */
	private void setSelectedFees(int feesIndex2) {
		for (int i = 0; i < 4; i++) {
			dateFeesType[i].setTextColor(getResources().getColor(R.color.black));
		}
		dateFeesType[feesIndex2].setTextColor(getResources().getColor(R.color.common_botton_bar_blue));
		mdateFeesType=String.valueOf(feesIndex2+1);
	}
	/**
	 * 选择约会对象
	 * @param partnerIndex2
	 */
	private void setSelectedPartner(int partnerIndex2) {
		for (int i = 0; i < 3; i++) {
			datePartnerType[i].setTextColor(getResources().getColor(R.color.black));
		}
		datePartnerType[partnerIndex2].setTextColor(getResources().getColor(R.color.common_botton_bar_blue));
		mdatePartnerType=String.valueOf(partnerIndex2);
		
	}
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        switch (requestCode) {
        	case IntentConstant.REQUESTCODE_ADDRESS:
        		if(resultCode==Activity.RESULT_OK){
        			String name = data.getExtras().getString("name");
        			String address = data.getExtras().getString("address");
        			String longitude = String.valueOf(data.getDoubleExtra("longitude", 0.0));//getExtras().getString("longitude");
    				String latitude = String.valueOf(data.getDoubleExtra("latitude", 0.0));//data.getExtras().getString("latitude");
    				data.getExtras().getDouble("longitude");
    				Log.i("Amy bussinessAda", longitude+ "  "+latitude+"  "+data.getExtras().getDouble("longitude"));
        			dateAddress.setText(name);
        			mdateAddress=name;
        			mDtailAddress=address;
        			mdateAddressLog=longitude;
        			mdateAddressLat=latitude;
        		}
        
        }
    }
	
	
}
