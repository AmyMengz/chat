package com.huodong.im.chatdemo.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.huodong.im.chat.AddFriendActivity;
import com.huodong.im.chat.db.YCOpenHelperTest;
import com.huodong.im.chat.mode.User;
import com.huodong.im.chat.mode.UserMode;
import com.huodong.im.chat.util.HttpUtil;
import com.huodong.im.chat.util.HttpUtil.ResponseListner;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.ToActivityUtil;
import com.huodong.im.chat.view.ClearEditText;
import com.huodong.im.chat.view.ContactAdapter;
import com.huodong.im.chat.view.MyLetterView;
import com.huodong.im.chat.view.MyLetterView.OnTouchingLetterChangedListener;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.MainActivity;
import com.huodong.im.chatdemo.activity.MyApplication;
import com.huodong.im.chatdemo.adapter.DateAdapter;
import com.huodong.im.chatdemo.adapter.NearbyUserAdapter;
import com.huodong.im.chatdemo.widget.MyListView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
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
import android.widget.ListView;
import android.widget.TextView;

public class ContactsFragment extends BaseFragment {
	private View curView = null;
	private MyListView dateListView;
	private MyListView nearbyUserListView;

	private DateAdapter dateAdapter;
	private NearbyUserAdapter nearbyUserAdapter;

	private static Handler uiHandler = null;
	private ListView list_friends;
	private ContactAdapter mContactAdapte;
	private TextView tv_explain;
	private FragmentActivity activity;

	public static Handler getHandler() {
		return uiHandler;
	}

	MainActivity mainActivity;

	public ContactsFragment(MainActivity mainActivity) {
		super();
		this.mainActivity = mainActivity;
		yDB = new YCOpenHelperTest(mainActivity);
		getNetFds();
		initReceiver();
	}

	private void initReceiver() {
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(IMConstants.ACTION_CONTACT);
		mainActivity.registerReceiver(new GetContactReceiver(), filter2);
	}

	private boolean isGetNetFds = false;

	class GetContactReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			getNetFds();
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initHandler();
	}
	MyLetterView  right_letter;
	ClearEditText et_msg_search;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null != curView) {
			((ViewGroup) curView.getParent()).removeView(curView);
			return curView;
		}
		curView = inflater.inflate(R.layout.fragment_contacts, topContentView);
		tv_explain = (TextView) curView.findViewById(R.id.tv_explain);
		et_msg_search = (ClearEditText) curView.findViewById(R.id.et_msg_search);
		right_letter = (MyLetterView) curView.findViewById(R.id.right_letter);
		activity = getActivity();
		initRes();
		list_friends.setAdapter(mContactAdapte);
		getLocalFds();
		setListner();
		return curView;
	}

	private void getLocalFds() {
		if (mContactAdapte == null || tv_explain == null)
			return;
		mUserList = yDB.queryContacts();
		orderData();
		mContactAdapte.setData(mUserList,false);
		mContactAdapte.notifyDataSetChanged();
		if (mUserList.size() > 0){
			tv_explain.setVisibility(View.GONE);
		} else {
			tv_explain.setVisibility(View.VISIBLE);
		}

	}
	HashMap<String, Integer> mIndexMap;
	private void orderData() {
		Collections.sort(mUserList,new Comparator<User>() {

			@Override
			public int compare(User lhs, User rhs) {
				String name1 = lhs.getFenLei();
				String name2 = rhs.getFenLei();
				return name1.compareTo(name2);
			}
		});
		 mIndexMap = new HashMap<String,Integer>();
		for (int i=0;i<mUserList.size();i++) {
			User user = mUserList.get(i);
			String fenLei = user.getFenLei();
			String first_str = fenLei.subSequence(0, 1).toString();
			if(first_str.matches("[A-Z]")){
				if(!mIndexMap.containsKey(first_str)){
					mIndexMap.put(first_str, i);
					user.setAlpha(first_str);
				}
			} else {
				if(!mIndexMap.containsKey("#")){
					mIndexMap.put("#", i);
					user.setAlpha("#");
				}
			}
		}
	}

	private void setListner() {
		list_friends.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				User user = mUserList.get(position);
				ToActivityUtil.chat(user.uid, activity,user.name);
			}

		});
		right_letter.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				if(mIndexMap.containsKey(s)){
					Integer pos = mIndexMap.get(s);
					list_friends.setSelection(pos);
				} 
			}
		});
		et_msg_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String str = s.toString();
				String str_pin_ying = s.toString().toUpperCase();
				if(TextUtils.isEmpty(str)){
					right_letter.setVisibility(View.VISIBLE);
					mContactAdapte.setData(mUserList, false);
					mContactAdapte.notifyDataSetChanged();
				}else {
					right_letter.setVisibility(View.GONE);
					mSearchList.removeAll(mSearchList);
					for (User user : mUserList) {
						String fenLei = user.getFenLei();
						String name = user.getName();
						if(fenLei.contains(str_pin_ying)||name.contains(str)){
							mSearchList.add(user);
						}
					}
					mContactAdapte.setData(mSearchList, true);
					mContactAdapte.notifyDataSetChanged();
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}

	private void initRes() {
		setTopTitle(getString(R.string.contacts));
		setTopRightButton(R.drawable.add);
		topRightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), AddFriendActivity.class));
			}
		});
		list_friends = (ListView) curView.findViewById(R.id.list_friends);
		mContactAdapte = new ContactAdapter(activity);
		initString();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	private String mFdsGetFail, re_get, fds_no, fds_fail, getting_fds, apply_fds_fail, connect_error, apply_fds,
			has_apply_fds;
	private YCOpenHelperTest yDB;
	private ArrayList<User> mUserList = new ArrayList<User>();
	private ArrayList<User> mSearchList = new ArrayList<User>();

	private void initString() {
		getting_fds = activity.getResources().getString(R.string.getting_fds);
		fds_fail = activity.getResources().getString(R.string.fds_fail);
		re_get = activity.getResources().getString(R.string.re_get);
		fds_no = activity.getResources().getString(R.string.fds_no);
		apply_fds_fail = activity.getResources().getString(R.string.apply_fds_fail);
		connect_error = activity.getResources().getString(R.string.connect_error);
		apply_fds = activity.getResources().getString(R.string.apply_fds);
		has_apply_fds = activity.getResources().getString(R.string.has_apply_fds);
		mFdsGetFail = fds_fail + "\n\n" + re_get;
	}

	private boolean isGetNetFds() {
		return isGetNetFds;
	}

	private void setIsGetNetFds(boolean mIsGetNetFds) {
		this.isGetNetFds = mIsGetNetFds;
	}

	private void getNetFds() {
		if (isGetNetFds())
			return;
		setIsGetNetFds(true);
		setTvExplain(getting_fds);
		int beginId = yDB.queryYccontactMaxChatId();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		MyApplication yApp = MyApplication.getInstance();
		int uid = yApp.getUid();
		params.add(new BasicNameValuePair(UserMode.UID, String.valueOf(uid)));
		params.add(new BasicNameValuePair(UserMode.ID, String.valueOf(beginId)));
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.post(params, IMConstants.URL_FRIENDS);
		httpUtil.setResponseListner(new ResponseListner() {

			@Override
			public void result(String result) {
				setIsGetNetFds(false);
				if (!TextUtils.isEmpty(result)) {
					Gson gson = new Gson();
					UserMode userMode = gson.fromJson(result, UserMode.class);
					boolean flag = userMode.getFlag();
					if (flag) {
						ArrayList<User> userList = userMode.getData();
						if (userList.size() > 0) {
							yDB.insertContacts(userList);
							getLocalFds();
						} else {
							// 没有好友
							setTvExplain(fds_no);
						}
					} else {
						// 数据错误
						setTvExplain(mFdsGetFail);
					}

				} else {
					// 连接失败
					setTvExplain(mFdsGetFail);
				}

			}
		});

	}

	private void setTvExplain(String str) {
		if (tv_explain != null && !TextUtils.isEmpty(str))
			tv_explain.setText(str);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void initHandler() {
		// TODO Auto-generated method stub

	}

}
