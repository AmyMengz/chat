package com.huodong.im.chat;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.huodong.im.chat.mode.User;
import com.huodong.im.chat.mode.UserMode;
import com.huodong.im.chat.util.HttpUtil;
import com.huodong.im.chat.util.HttpUtil.ResponseListner;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.NetUtil;
import com.huodong.im.chat.util.SoftInput;
import com.huodong.im.chat.util.StringUtil;
import com.huodong.im.chat.util.ToastUtil;
import com.huodong.im.chat.view.AddFAdapter;
import com.huodong.im.chat.view.HeaderLayout;
import com.huodong.im.chat.view.LoadView;
import com.huodong.im.chat.view.XListView.RefreshListener;
import com.huodong.im.chatdemo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddFriendActivity extends Activity {

	private EditText et_find_name;
	private ImageButton btn_search;
	private com.huodong.im.chat.view.XListView list_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoadView = new LoadView(this);
		setContentView(mLoadView.getView());
		View view = getView();
		mLoadView.setView(view);
		initTitle();
		initString();
		initXListView();
		list_search.setOnRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh() {

			}

			@Override
			public void onLoadMore() {
				list_search.setAdapter(addFAdapter);
				list_search.setSelection(list_search.getCount());
				int id = mFindList.get(mFindList.size() - 1).getId();
				getNetData(id, mFindStr);
			}
		});
		addFAdapter = new AddFAdapter(AddFriendActivity.this);
		list_search.setAdapter(addFAdapter);
	}

	String mFindStr;

	private View getView() {
		View view = View.inflate(this, R.layout.activity_add_contact, null);
		btn_search = (ImageButton) view.findViewById(R.id.btn_search);
		list_search = (com.huodong.im.chat.view.XListView) view.findViewById(R.id.list_search);
		list_search.setHas_head_view(false);
		et_find_name = (EditText) view.findViewById(R.id.et_find_name);
		common_actionbar = (HeaderLayout) view.findViewById(R.id.common_actionbar);
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFindStr = et_find_name.getText().toString().trim();
				boolean loadMore = list_search.isLoadMore();
				if (loadMore)
					return;
				if (TextUtils.isEmpty(mFindStr)) {
					String str = StringUtil.getStr(AddFriendActivity.this, R.string.input_contact_name);
					ToastUtil.show(AddFriendActivity.this, str);
					return;
				}
				if (!NetUtil.iSHasNet(AddFriendActivity.this)) {
					String str1 = StringUtil.getStr(AddFriendActivity.this, R.string.find_fail);
					String str2 = StringUtil.getStr(AddFriendActivity.this, R.string.please_check_net);
					ToastUtil.show(AddFriendActivity.this, str1 + "\n\n" + str2);
					return;
				}
				SoftInput.KeyBoardCancle(AddFriendActivity.this);
				mLoadView.toLoadInTranlate();
				list_search.setFoot_no_data(false);
				list_search.setLoadMore(true);
				addFAdapter.removeData();
				addFAdapter.notifyDataSetChanged();
				getNetData(Integer.MAX_VALUE, mFindStr);
			}
		});
		return view;
	}

	final int GET_COUNT = IMConstants.COUNT_15;
	String find_fail, find_no, connect_error;

	private void initString() {
		find_fail = getResources().getString(R.string.find_fail);
		find_no = getResources().getString(R.string.find_no);
		connect_error = getResources().getString(R.string.connect_error);
	}

	private AddFAdapter addFAdapter;
	private LoadView mLoadView;
	ArrayList<User> mFindList = new ArrayList<User>();

	private void getNetData(int begin, final String find) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(UserMode.FIND, find));
		params.add(new BasicNameValuePair(UserMode.ID, String.valueOf(begin)));
		params.add(new BasicNameValuePair(UserMode.LIMIT, String.valueOf(GET_COUNT)));
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.post(params, IMConstants.URL_FIND_FRIENDS, 500l);
		httpUtil.setResponseListner(new ResponseListner() {

			@Override
			public void result(String result) {
				mLoadView.endLoad();
				list_search.onRefreshComplete(false);
				if (!find.equals(mFindStr))
					return;
				if (result != null) {
					Gson gson = new Gson();
					UserMode userMode = gson.fromJson(result, UserMode.class);
					boolean flag = userMode.getFlag();
					if (flag) {
						ArrayList<User> data = userMode.getData();
						if (data.size() > 0) {

							mFindList.addAll(data);
							addFAdapter.setData(mFindList);
							addFAdapter.notifyDataSetChanged();
						} else if (mFindList.size() == 0) {
							// 没有找到联系人
							ToastUtil.show(AddFriendActivity.this, find_no);
						}
						if (data.size() != GET_COUNT) {
							list_search.setFoot_no_data(true);
						}
					} else {
						// 错误
						ToastUtil.show(AddFriendActivity.this, find_fail);
					}

				} else {
					ToastUtil.show(AddFriendActivity.this, connect_error);
				}

			}
		});
	}

	com.huodong.im.chat.view.HeaderLayout common_actionbar;

	private void initTitle() {
		common_actionbar.setRigthVisibility(false);
		common_actionbar.setTitle("查找好友");
		common_actionbar.setBack();
//		common_actionbar.setLeftImage(R.drawable.base_action_bar_back_bg_n);
//		common_actionbar.left_image.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				AddFriendActivity.this.finish();
//			}
//		});
	}

	private void initXListView() {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case IMConstants.ChaType.APPLY_FDS:
			String result = data.getStringExtra("result");
			break;

		default:
			break;
		}
	}

}
