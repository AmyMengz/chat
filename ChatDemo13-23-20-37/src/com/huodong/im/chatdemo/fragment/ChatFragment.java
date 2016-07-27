package com.huodong.im.chatdemo.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.huodong.im.chat.ChatActivity;
import com.huodong.im.chat.NewFdsActivity;
import com.huodong.im.chat.db.YCOpenHelperTest;
import com.huodong.im.chat.mode.ChatDataMode;
import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.mode.UserMode;
import com.huodong.im.chat.util.HttpUtil;
import com.huodong.im.chat.util.HttpUtil.ResponseListner;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.MyRotateAnimation;
import com.huodong.im.chat.util.NetUtil;
import com.huodong.im.chat.util.NoticUtil;
import com.huodong.im.chat.util.SendBroadUtil;
import com.huodong.im.chat.util.StringUtil;
import com.huodong.im.chat.util.ToActivityUtil;
import com.huodong.im.chat.view.HistoryChatAdapter;
import com.huodong.im.chat.view.XListView.RefreshListener;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.MainActivity;
import com.huodong.im.chatdemo.activity.MyApplication;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ChatFragment extends BaseFragment {
	private View curView = null;

	private static Handler uiHandler = null;

	public static Handler getHandler() {
		return uiHandler;
	}

	MainActivity mainActivity;

	public ChatFragment(MainActivity mainActivity) {
		super();
		this.mainActivity = mainActivity;
		mYDB = new YCOpenHelperTest(mainActivity);
		getNetChat();
		initReceiver();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initHandler();
	}

	private TextView tv_explain, tv_no_data;
	private com.huodong.im.chat.view.XListView list_friends;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null != curView) {
			((ViewGroup) curView.getParent()).removeView(curView);
			return curView;
		}
		curView = inflater.inflate(R.layout.fragment_chat, topContentView);
		tv_explain = (TextView) curView.findViewById(R.id.tv_explain);
		tv_no_data = (TextView) curView.findViewById(R.id.tv_no_data);
		list_friends = (com.huodong.im.chat.view.XListView) curView.findViewById(R.id.list_friends);
		list_friends.setHas_head_view(false);
		list_friends.setFadingEdgeLength(getId());
		list_friends.setFoot_no_data(true);
		list_friends.setOnRefreshListener(new ChatRefreshListener());
		activity = getActivity();
		initRes();
		chatAdapter = new HistoryChatAdapter(activity, ChatFragment.this);
		list_friends.setAdapter(chatAdapter);
		setTvNet();
		getLocalChat();
		setListner();
		return curView;
	}

	@Override
	public void onPause() {
		super.onPause();
		setPause(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		setPause(false);
		judgeToUpdateData();
	}

	private boolean needChagneData = false;

	public boolean isNeedChagneData() {
		return needChagneData;
	}

	public void setNeedChagneData(boolean needChagneData) {
		this.needChagneData = needChagneData;
	}

	private void setListner() {
		list_friends.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Log.v(IMConstants.TAG_YJ, "position:"+position);
				// Log.v(IMConstants.TAG_YJ, "id:"+id);
				ChatMode chatMode = muChatList.get(position);
				int type = chatMode.getType();
				if (type == IMConstants.ChaType.CHAT_FROM || type == IMConstants.ChaType.CHAT_TO) {
					int uid = chatMode.getUid();
					String name = chatMode.getName();
					ToActivityUtil.chat(uid, activity, name);
				} else {
					// new fds
					Intent intent = new Intent(activity, NewFdsActivity.class);
					startActivityForResult(intent, IMConstants.CodeTag.CHAT_CHANGE);

				}
			}
		});

	}

	private void initReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(IMConstants.ACTION_MESSAGE);
		mainActivity.registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String str_json = intent.getStringExtra(IMConstants.PROPERTY_DATA);
				JSONObject json;
				try {
					json = new JSONObject(str_json);
					String flag = json.getString(IMConstants.FLAG);
					if (IMConstants.FLAG_MESSAGE.equals(flag)) {
						newMsNotic(json);
					} else if (IMConstants.FLAG_NET_CHANGE.equals(flag)) {
						setTvNet();
					} else if (IMConstants.FLAG_GET_NET_CHAT.equals(flag)) {
						getNetChat();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.v(IMConstants.TAG_YJ, "Exception11--" + e.toString());
				}
			}
		}, intentFilter);

	}

	private void newMsNotic(JSONObject json) throws Exception {
		setNeedChagneData(true);
		judgeToUpdateData();
		int index = mainActivity.getIndex();
		int uid = json.getInt(ChatMode.UID);
		MyApplication yApp = MyApplication.getInstance();
		int uid2 = yApp.getUid();
		ActivityManager localActivityManager = (ActivityManager) mainActivity
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName localComponentName = ((ActivityManager.RunningTaskInfo) localActivityManager.getRunningTasks(1)
				.get(0)).topActivity;
		String className1 = localComponentName.getClassName();
		if (uid != uid2) {
			String className2 = ChatActivity.class.getName();
			String className3 = MainActivity.class.getName();
			if (className1.equals(className2)) {
				int share_uid = ChatActivity.getShare_uid();
				if (uid != share_uid) {
					NoticUtil.show(mainActivity);
				}
			} else if (index != 1 || !className1.equals(className3)) {
				NoticUtil.show(mainActivity);
			}
		}
	}
	private void newMsNotic()  {
		setNeedChagneData(true);
		judgeToUpdateData();
		int index = mainActivity.getIndex();
		ActivityManager localActivityManager = (ActivityManager) mainActivity
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName localComponentName = ((ActivityManager.RunningTaskInfo) localActivityManager.getRunningTasks(1)
				.get(0)).topActivity;
		String className1 = localComponentName.getClassName();
			String className3 = MainActivity.class.getName();
			if (index != 1 || !className1.equals(className3)) {
				NoticUtil.show(mainActivity);
			}
	}

	private boolean iSHasNet = false;

	private void setTvNet() {
		if (NetUtil.iSHasNet(mainActivity)) {
			setTvExplainGone();
		} else {
			setTvExplain(StringUtil.getStr(mainActivity, R.string.no_net));
		}
	}



	public void judgeToUpdateData() {
		if (isNeedChagneData() && !isPause()) {
			setNeedChagneData(false);
			getLocalChat();
		}
	}

	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

	private boolean isPause = true;

	class ChatRefreshListener implements RefreshListener {

		@Override
		public void onRefresh() {
			MyRotateAnimation.rotateAnimation(list_friends.head_iv_progress);

		}

		@Override
		public void onLoadMore() {

		}

	}

	private void setTvExplain(String str) {
		if (tv_explain == null)
			return;
		tv_explain.setText(str);
		tv_explain.setVisibility(View.VISIBLE);
	}

	private void setTvNoData() {
		tv_no_data.setVisibility(View.VISIBLE);
	}

	private void setTvExplainGone() {
		if (tv_explain == null)
			return;
		tv_explain.setVisibility(View.GONE);
	}

	private void getLocalChat() {
		if (tv_explain == null || chatAdapter == null)
			return;
		muChatList = mYDB.queryDifUser();
		if (muChatList.size() > 0) {
			tv_no_data.setVisibility(View.GONE);
			chatAdapter.setData(muChatList);
			chatAdapter.notifyDataSetChanged();
		} else {
			// 无聊天内容
			tv_no_data.setVisibility(View.VISIBLE);
		}
	}

	HashMap<Integer, Boolean> userMap = new HashMap<Integer, Boolean>();
	List<ChatMode> muChatList = new ArrayList<ChatMode>();
	ChatMode mNewFdsData;
	ArrayList<ChatMode> mApplyFdsList = new ArrayList<ChatMode>();

	private int mNewFdsPos = -1;
	boolean isGetNetChat = false;

	private void getNetChat() {
		if (!NetUtil.iSHasNet(mainActivity) || isGetNetChat) {
			return;
		}
		isGetNetChat = true;
		int beginId = 0;
		beginId = mYDB.queryYcMaxChatId();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		MyApplication yApp = MyApplication.getInstance();
		int uid = yApp.getUid();
		params.add(new BasicNameValuePair(UserMode.UID, String.valueOf(uid)));
		params.add(new BasicNameValuePair(UserMode.ID, String.valueOf(beginId)));
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.post(params, IMConstants.URL_GETCHAT);
		httpUtil.setResponseListner(new ResponseListner() {

			@Override
			public void result(String result) {
				if (!TextUtils.isEmpty(result)) {
					Gson gson = new Gson();
					ChatDataMode userMode = gson.fromJson(result, ChatDataMode.class);
					boolean flag = userMode.getFlag();
					if (flag) {
						ArrayList<ChatMode> dataList = userMode.getData();
						if (dataList.size() > 0) {
							mYDB.insert(dataList);
							getLocalChat();
							newMsNotic();
							SendBroadUtil.sendHasGetNetChat(mainActivity);
						} else {
							// 无聊天信息
						}
					} else {
						// 数据错误
					}

				} else {
					// 连接失败
				}
				isGetNetChat = false;
			}

		});
	}

	private void initRes() {
		setTopTitle(getString(R.string.session));

	}

	private FragmentActivity activity;
	private HistoryChatAdapter chatAdapter;

	private YCOpenHelperTest mYDB;

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void initHandler() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IMConstants.CodeTag.CHAT_CHANGE:
			if (requestCode == resultCode) {
				getLocalChat();
			}

			break;

		default:
			break;
		}
	}
}
