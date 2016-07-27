package com.huodong.im.chatdemo.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apaches.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.adapter.BusinessAdapter;
import com.huodong.im.chatdemo.controller.DZDPUrlHelper;
import com.huodong.im.chatdemo.controller.GetDZDPData;
import com.huodong.im.chatdemo.controller.GetDZDPData.DataCallBack;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.SharePreferenceHelper;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.BussinessInfoEntity;
import com.huodong.im.utils.IMUIHelper;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SelectAddressActivityDetail extends BaseActivity {
	private String longitude,latitude;
	private Map<String, String> paramMap;
	private static final int CODE = 1;
	private String codes;
	private URL url;
	private boolean isDiscount;
	private String city,region,keyword;
	private int has_coupon,has_deal,sort;
	
	private TextView receiveContent;
	private ListView bussinessInfoList;
	private ProgressBar processBar;
	private TextView no_list;
	private BusinessAdapter adapter;
	private List<BussinessInfoEntity> list;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_address_detail);
		initDate();
		initView();
//		getDataFormNet(codes);
		GetDZDPData.getInstance().getDataFormNet(url,isDiscount,new DataCallBack() {

			@Override
			public void onDataCallBack(
					List<BussinessInfoEntity> list2) {
//				receiveContent.setText(result);
//				list=list2;//不可以
//				if(list2)
				processBar.setVisibility(View.GONE);
				Log.i("Amy DZDP", (list2==null)+"  "+list2);
				if(list2.size()<=0){
					no_list.setVisibility(View.VISIBLE);
				}else {
					no_list.setVisibility(View.GONE);
					for (int i = 0; i < list2.size(); i++) {
						list.add(list2.get(i));
					}
					IMUIHelper.setListViewHeightBasedOnChildren(bussinessInfoList);
					adapter.notifyDataSetChanged();
				}
			}
		});
		
	}

	private void initDate() {
		
		list=new ArrayList<BussinessInfoEntity>();
		adapter=new BusinessAdapter(list, SelectAddressActivityDetail.this);
		longitude=String.valueOf(SharePreferenceHelper.getInstance().getLocationLog());
		latitude=String.valueOf(SharePreferenceHelper.getInstance().getLocationLat());
		city=getIntent().getStringExtra(IntentConstant.KEY_CITY);
		region=getIntent().getStringExtra(IntentConstant.KEY_REGION);
		keyword=getIntent().getStringExtra(IntentConstant.KEY_KEYWORD);
		isDiscount=Boolean.valueOf(getIntent().getStringExtra(IntentConstant.KEY_DISCOUNT));
		createParamTable();
		DZDPUrlHelper dzdpUrlHelper=new DZDPUrlHelper(paramMap);
		codes=dzdpUrlHelper.sortForParamKey();
//		codes = sortForParamKey();
		try {
			url=dzdpUrlHelper.codecParams(codes,UrlConstant.DZDP_API_BUSSINESS_URL);
			Log.i(TAG+"3-18", "url  "+url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initView() {
		processBar=(ProgressBar)findViewById(R.id.processBar);
		no_list=(TextView)findViewById(R.id.no_list);
//		receiveContent = (TextView) findViewById(R.id.youhui);
		initTopBarforRightText(getString(R.string.selection), getString(R.string.ok), new onRighTextClickListener() {
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				
			}
		});
		bussinessInfoList=(ListView)findViewById(R.id.bussiness_info_list);
		bussinessInfoList.setAdapter(adapter);	
		bussinessInfoList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				IMUIHelper.openBussinessDetailActivity(SelectAddressActivityDetail.this, list.get(position));
			}
		});
	}
	/**
	 * 对需要参数进行设置
	 */
	private void createParamTable() {
		paramMap = new HashMap<String, String>();

		paramMap.put("format", "json");
		if(!TextUtils.isEmpty(city)){
			paramMap.put("city", city);
		}
		//不能在请求时限制 要在选出的结果中筛选  buyong
		if(Boolean.valueOf(isDiscount))
			{has_coupon=1;has_deal=1;
//			paramMap.put("has_coupon", "1");
			paramMap.put("has_deal", "1");
			}
		else {
			has_coupon=0;has_deal=0;
		}
//		paramMap.put("latitude", latitude);
//		paramMap.put("longitude", longitude);
//		paramMap.put("category", "美食");
//		paramMap.put("region", region);
		paramMap.put("limit", "40");
//		paramMap.put("radius", "5000");
//		paramMap.put("offset_type", "0");
		
//		paramMap.put("page", "1");
		paramMap.put("keyword", keyword);
		if(!TextUtils.isEmpty(region)){
			paramMap.put("region", region);
		}
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case IntentConstant.REQUESTCODE_BUSSINESS_DETAIL:
				if(resultCode==RESULT_OK){
					//数据是使用Intent返回
		            Intent intent = new Intent();
		            //把返回数据存入Intent
		            intent.putExtra("name", data.getStringExtra("name"));
		            intent.putExtra("address", data.getStringExtra("address"));
		            intent.putExtra("longitude", data.getDoubleExtra("longitude", 0.0));
		            intent.putExtra("latitude", data.getDoubleExtra("latitude",0.0));
		            //设置返回数据
		            SelectAddressActivityDetail.this.setResult(Activity.RESULT_OK, intent);
		            //关闭Activity
		            SelectAddressActivityDetail.this.finish();
				}
		}
	}
}   


