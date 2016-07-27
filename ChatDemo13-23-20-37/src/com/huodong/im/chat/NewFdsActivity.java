package com.huodong.im.chat;

import java.util.List;

import org.json.JSONObject;

import com.huodong.im.chat.db.YCOpenHelperTest;
import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.StringUtil;
import com.huodong.im.chat.view.HeaderLayout;
import com.huodong.im.chat.view.NewFdsAdapter;
import com.huodong.im.chatdemo.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

public class NewFdsActivity extends Activity {

	private ListView lv_list;
	private NewFdsAdapter newFdsAdapter;
	private List<ChatMode> dataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_fds);
		initReceiver();
		findViewById();
		yDB = new YCOpenHelperTest(NewFdsActivity.this);
		newFdsAdapter = new NewFdsAdapter(NewFdsActivity.this);
		lv_list.setAdapter(newFdsAdapter);
		getLocalData();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		isPause = true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		isPause = false;
	}
	
	private boolean isPause = true;
	
	private void initReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(IMConstants.ACTION_MESSAGE);
		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String str_json = intent.getStringExtra(IMConstants.PROPERTY_DATA);
				JSONObject json;
				try {
					json = new JSONObject(str_json);
					String flag = json.getString(IMConstants.FLAG);
					if (IMConstants.FLAG_HAS_GET_NET_CHAT.equals(flag)&&!isPause) {
							getLocalData();
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, intentFilter);

	}


	public void getLocalData(){
		dataList = yDB.queryNewFds();
		newFdsAdapter.setData(dataList);
		newFdsAdapter.notifyDataSetChanged();
	}
	HeaderLayout	common_actionbar;
	private void findViewById() {
		lv_list = (ListView) findViewById(R.id.lv_list);
		common_actionbar = (HeaderLayout) findViewById(R.id.common_actionbar);
		common_actionbar.setBack();
		common_actionbar.setTitle(StringUtil.getStr(this, R.string.new_fds));
		common_actionbar.setRigthVisibility(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IMConstants.ChaType.APPLY_FDS:
			if(IMConstants.ChaType.AGREE==resultCode){
				getLocalData();
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private boolean isDataChange = false;
	private YCOpenHelperTest yDB;
//	public void beFds() {
//		setDataChange();
//		int pos = newFdsAdapter.getAgreeing_pos();
//		ChatMode chatMode = dataList.get(pos);
//		chatMode.setType(IMConstants.ChaType.AGREE);
//		dataList.set(pos, chatMode);
//		newFdsAdapter.setData(dataList);
//		newFdsAdapter.notifyDataSetChanged();
//	}
	
	private void setDataChange(){
		isDataChange = true;
		setResult(IMConstants.CodeTag.CHAT_CHANGE, new Intent());
	}

}
