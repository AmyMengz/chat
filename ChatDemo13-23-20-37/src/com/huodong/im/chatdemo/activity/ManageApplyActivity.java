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
import com.huodong.im.chatdemo.adapter.NearbyUserAdapter;
import com.huodong.im.chatdemo.controller.ManageApplyController;
import com.huodong.im.chatdemo.controller.ManageApplyController.ManageApplyCallback;
import com.huodong.im.chatdemo.controller.NearbyUserDetailController;
import com.huodong.im.chatdemo.controller.NearbyUserDetailController.NearbyUserDateCallback;
import com.huodong.im.chatdemo.controller.NearbyUserDetailController.NearbyUserDetailCallback;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.SharePreferenceHelper;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.NearbyUserEntity;
import com.huodong.im.entity.SearchDateEntity;
import com.huodong.im.entity.UserEntity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ManageApplyActivity extends BaseActivity{

	ListView applyList;
//	long mLongtitud,mLatitude;
	 private NearbyUserAdapter userAdapter;
	List<NearbyUserEntity> userList;
//	private ProgressBar processBar;
	private TextView no_list;
//	private LinearLayout text_tip;
	//请求参数
	private int dateId;
	
	int partner_num;//限制人数
	int allowedNum=0;//已经允许的人数
	private Map<String, String> map;
	
	private ManageApplyController controller;
	SharePreferenceHelper helper=SharePreferenceHelper.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_apply);
		controller=new ManageApplyController(ManageApplyActivity.this);
		dateId=getIntent().getIntExtra(IntentConstant.KEY_DATE_KEY, 0);
		partner_num=getIntent().getIntExtra(IntentConstant.KEY_PARTNER_NUM, 0);
		
		initView();
		
		controller.getApplyedUser(dateId, new ManageApplyCallback() {
			
			@Override
			public void onManageApplyCallback(Object obj) {
				Message msg=(Message) obj;
//				processBar.setVisibility(View.GONE);
				switch (msg.what) {
				case  IntentConstant.HANDLER_APPLY_MANAGE_NO:
					no_list.setVisibility(View.VISIBLE); 
					break;
				case IntentConstant.HANDLER_APPLY_MANAGE+IntentConstant.HANDLER_ERROR:
					no_list.setVisibility(View.VISIBLE);
					if(msg.obj!=null){
						ShowToast(R.string.get_data_error+" "+msg.obj);
					}
					else {
						ShowToast(R.string.get_data_error);
					}
					break;
				case IntentConstant.HANDLER_APPLY_MANAGE+IntentConstant.HANDLER_OK:
					if(msg.obj!=null){
						userList.clear();
						
						List<NearbyUserEntity> list=(List<NearbyUserEntity>) msg.obj;
						int len=list.size();
						if(len>0){
							no_list.setVisibility(View.GONE);
							for (int j = 0; j < len; j++) {
								switch (list.get(j).getPassed()) {
								case 1:allowedNum++;
								break;
								}
							}
							for (int i = 0; i < list.size(); i++) {
								final NearbyUserEntity entity=list.get(i);
								final int uid=list.get(i).getUid();
								switch (list.get(i).getPassed()) {
								case 0:
									userAdapter.setApplyManagerOnclick(new OnClickListener() {
										@Override
										public void onClick(View v) {
											if(allowedNum>=partner_num){
												ShowToast(getString(R.string.allow_beyond_limit));
											}else {
												controller.makeAllowed(dateId, uid,new ManageApplyCallback() {
													
													@Override
													public void onManageApplyCallback(Object obj) {
														Message msg=(Message) obj;
														switch (msg.what) {
														case IntentConstant.HANDLER_APPLY_MANAGE_ALLOW+IntentConstant.HANDLER_OK:
															//批准通过
															allowedNum++;
														entity.setIsPassed(1);
														ShowToast(getString(R.string.allowed));
														userAdapter.notifyDataSetChanged();
															break;

														default:
															break;
														}
													}
												}, String.format(getString(R.string.allow_ta_), entity.getNickName()));
											}
											
										}
									});
									break;
								}
							}
							for (int i = 0; i < list.size(); i++) {
								userList.add(list.get(i));
							}
							IMUIHelper.setListViewHeightBasedOnChildren(applyList);
							userAdapter.notifyDataSetChanged();		
						}else {
							no_list.setVisibility(View.VISIBLE);
						}
										
					}else {
						no_list.setVisibility(View.VISIBLE);
					}
				}
				
			}
		});
		
		
	}
	private void initView() {
		
		initTopBarForLeft(getString(R.string.manage_apply));
		no_list=(TextView)findViewById(R.id.no_list);
//		text_tip=(LinearLayout)findViewById(R.id.text_tip);
//		processBar=(ProgressBar)findViewById(R.id.processBar);
		applyList=(ListView)findViewById(R.id.apply_list);
		userList=new ArrayList<NearbyUserEntity>();
		/*userList.add(new NearbyUserEntity());
		userList.add(new NearbyUserEntity());
		userList.add(new NearbyUserEntity());*/
		userAdapter=new NearbyUserAdapter(ManageApplyActivity.this,userList);
		userAdapter.setApplyManagerShow(true);
		applyList.setAdapter(userAdapter);
		applyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NearbyUserEntity currentUser=(NearbyUserEntity) userAdapter.getItem(position);
//				view.getTag();
				IMUIHelper.openUserDetailActivity(ManageApplyActivity.this, currentUser.getNickName(), currentUser.getUid(),currentUser.getFans());
			}
		});
	}
	

}
