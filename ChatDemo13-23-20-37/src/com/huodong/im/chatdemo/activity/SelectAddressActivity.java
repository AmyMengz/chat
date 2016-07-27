package com.huodong.im.chatdemo.activity;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.adapter.GridViewAdapter;
import com.huodong.im.chatdemo.controller.DZDPUrlHelper;
import com.huodong.im.chatdemo.controller.GetDZDPRegion;
import com.huodong.im.chatdemo.controller.GetDZDPRegion.DataCallBack;
import com.huodong.im.chatdemo.controller.SeleteAddressController;
import com.huodong.im.chatdemo.controller.SeleteAddressController.SeleteAddressCallback;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.SharePreferenceHelper;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.BussinessInfoEntity;
import com.huodong.im.utils.IMUIHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.huodong.im.chatdemo.adapter.GridViewAdapter.Holder;
public class SelectAddressActivity extends BaseActivity implements OnClickListener{
	private String longitude,latitude;
	private TextView ktv,bar,other,nearbyChoice;
	private TextView[] tvChoise;
	private CheckBox discount_only;
	private EditText query;
	private ImageButton clearSearch;
	private GridView regionView;
	
	private int choseIndex=0;
	private int len;
	private String[] nearbyChoiceText;
	private String[] requestKeyWords={"KTV","酒吧","其他"};
	private String reqCity="深圳",reqRegion,reqKeyword;
	private boolean isDiscountOnly;
	private Map<String, String> map=new HashMap<String, String>();
	
	private List<String> regionList;
	GridViewAdapter adapter;
	private SeleteAddressController controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_address);
		controller=SeleteAddressController.getInstance(this);
		initDate();
		initView();
		reqCity=SharePreferenceHelper.getInstance().getCity();
		Log.i("Amy ciy", reqCity+  longitude);
		if(reqCity!=null){
			reqCity=reqCity.substring(0, reqCity.length()-1);
		}
		
		setTopBarRightText(reqCity);
		controller.getRegion(reqCity, new SeleteAddressCallback() {
			
			@Override
			public void onSeleteAddressCallback(Object obj) {
				List<String> list =(List<String>) obj;
				if(obj!=null){
					regionList.clear();
					regionList.add(getResources().getString(R.string.all));
					int len=list.size();
					for (int i = 0; i < len; i++) {
						regionList.add(list.get(i));
					}
					adapter.notifyDataSetChanged();
//					regionList=list;
				}
			}
		});
		if(regionList.size()>0){
			reqRegion=regionList.get(0);
		}
		
		
		
	}

	private void initDate() {
		longitude=String.valueOf(SharePreferenceHelper.getInstance().getLocationLog());
		latitude=String.valueOf(SharePreferenceHelper.getInstance().getLocationLat());
		tvChoise=new TextView[3];
		len=tvChoise.length;
		nearbyChoiceText=new String[3];
		nearbyChoiceText[0]=getString(R.string.nearby_ktv);
		nearbyChoiceText[1]=getString(R.string.nearby_bar);
		nearbyChoiceText[2]=getString(R.string.nearby_other);
		regionList=new ArrayList<String>();
		regionList.add(getResources().getString(R.string.all));
		
	}

	private void initView() {
		initTopBarforRightText(getString(R.string.date_address), getString(R.string.city), new onRighTextClickListener() {
			@Override
			public void onClick() {
				IMUIHelper.openSeleteAddressCityActivityForResult(SelectAddressActivity.this, "31");
			}
		});
		query = (EditText)findViewById(R.id.query);
		clearSearch=(ImageButton)findViewById(R.id.search_clear);
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
		tvChoise[0]=(TextView)findViewById(R.id.ktv);
		tvChoise[1]=(TextView)findViewById(R.id.bar);
		tvChoise[2]=(TextView)findViewById(R.id.other);
		nearbyChoice=(TextView)findViewById(R.id.nearby_choice);
		discount_only=(CheckBox)findViewById(R.id.discount_only);
		
		for (int i = 0; i < len; i++) {
			tvChoise[i].setOnClickListener(this);
		}
		nearbyChoice.setOnClickListener(this);
		setSelected(choseIndex);
		regionView=(GridView)findViewById(R.id.region_gridview);
		adapter=new GridViewAdapter(SelectAddressActivity.this, regionList);
		regionView.setAdapter(adapter);
		regionView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("Amy position", position+"");
				reqRegion=regionList.get(position);
				adapter.setSeclection(position);
				adapter.notifyDataSetChanged();
				
			}
		});
//		regionView.setSelection(0);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ktv:
			choseIndex=0;
			setSelected(choseIndex);
			break;
		case R.id.bar:
			choseIndex=1;
			setSelected(choseIndex);
			break;
		case R.id.other:
			choseIndex=2;
			setSelected(choseIndex);
			break;
		case R.id.nearby_choice:
			getRequestInfo();
			
			IMUIHelper.openSeleteAddressDetailActivity(SelectAddressActivity.this,map);
			break;

		default:
			break;
		}
	}

	private void getRequestInfo() {
		isDiscountOnly=discount_only.isChecked();
		map.put(IntentConstant.KEY_CITY, reqCity);
		if(reqRegion.equals(getString(R.string.all))){
			map.put(IntentConstant.KEY_REGION, null);
		}else {
			map.put(IntentConstant.KEY_REGION, reqRegion);
		}
		map.put(IntentConstant.KEY_KEYWORD, reqKeyword);
		map.put(IntentConstant.KEY_DISCOUNT, String.valueOf(isDiscountOnly));
		Log.i("Amy myreqCity", "reqCity   "+reqCity);
	}

	private void setSelected(int choseIndex2) {
		for (int i = 0; i < len; i++) {
			tvChoise[i].setTextColor(getResources().getColor(R.color.black));
		}
		tvChoise[choseIndex2].setTextColor(getResources().getColor(R.color.common_botton_bar_blue));
		nearbyChoice.setText(nearbyChoiceText[choseIndex2]);
		reqKeyword=requestKeyWords[choseIndex2];
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case IntentConstant.REQUESTCODE_CITY:
			if(resultCode==RESULT_OK){
				String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
		        setTopBarRightText(result);
		        //要去请求区域划分
		        controller.getRegion(result, new SeleteAddressCallback() {
					
					@Override
					public void onSeleteAddressCallback(Object obj) {
						List<String> list =(List<String>) obj;
						if(obj!=null){
							regionList.clear();
							regionList.add(getString(R.string.all));
							int len=list.size();
							for (int i = 0; i < len; i++) {
								regionList.add(list.get(i));
							}
							
							adapter.notifyDataSetChanged();
//							regionList=list;
						}
					}
				});
		        reqCity=result;
			}
			break;
		case IntentConstant.REQUESTCODE_BUSSINESS_DETAIL:
			
		case IntentConstant.REQUESTCODE_ADDRESS_DETAIL:
			if(resultCode==RESULT_OK){
				String name = data.getExtras().getString("name");
				String address = data.getExtras().getString("address");//得到新Activity 关闭后返回的数据
				/*String longitude = data.getExtras().getString("longitude");//得到新Activity 关闭后返回的数据
				String latitude = data.getExtras().getString("latitude");//得到新Activity 关闭后返回的数据
*/				double longitude = data.getExtras().getDouble("longitude");//得到新Activity 关闭后返回的数据
				double latitude = data.getExtras().getDouble("latitude");//得到新Activity 关闭后返回的数据
				Intent intent = new Intent();
				Log.i("Amy bussinessAda", longitude+ "  "+latitude);
                //把返回数据存入Intent
				intent.putExtra("name", name);
                intent.putExtra("address", address);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                //设置返回数据
                SelectAddressActivity.this.setResult(Activity.RESULT_OK, intent);
                //关闭Activity
                SelectAddressActivity.this.finish();
			}
			break;
		
		default:
			break;
		}   
    }
}
