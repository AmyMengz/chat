package com.huodong.im.chat;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.handler.chain.ChainedIoHandler;
import org.json.JSONException;
import org.json.JSONObject;

import com.huodong.im.chat.db.YCOpenHelperTest;
import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.mode.UserMode;
import com.huodong.im.chat.service.ChatInterface;
import com.huodong.im.chat.service.ChatIoHandler;
import com.huodong.im.chat.service.ChatServiceConn;
import com.huodong.im.chat.util.Expressioin;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.MD5Encoder;
import com.huodong.im.chat.util.MyRotateAnimation;
import com.huodong.im.chat.util.NetUtil;
import com.huodong.im.chat.util.SmileyParser;
import com.huodong.im.chat.util.SoftInput;
import com.huodong.im.chat.util.StringUtil;
import com.huodong.im.chat.util.ToastUtil;
import com.huodong.im.chat.view.ChatAdapter;
import com.huodong.im.chat.view.ExpressionAdapter;
import com.huodong.im.chat.view.HeaderLayout;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.MyApplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ChatActivity extends Activity {

	private ListView lv_list;
	FrameLayout fl_reply;
	private EditText et_et;
	private TextView tv_send;
	public  int mUid;
	public  static int share_uid;
	private List<ChatMode> chatList = new ArrayList<ChatMode>();
	private ChatAdapter chatAdapter;
	private YCOpenHelperTest yDB;
	
	public static int getShare_uid() {
		return share_uid;
	}

	private static void setShare_uid(int share_uid) {
		ChatActivity.share_uid = share_uid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		Intent intent = getIntent();
		mUid = intent.getIntExtra(ChatMode.UID, -1);
		setShare_uid(mUid);
		mName = intent.getStringExtra(ChatMode.NAME);
		findViewById();
		chatAdapter = new ChatAdapter(this,mUid);
		lv_list.setAdapter(chatAdapter);
		getChatList();
		getString();
		setListner();
		mPref = getSharedPreferences(IMConstants.PREF_CHAT, Context.MODE_PRIVATE);
		getChat();
		setTextCursor();
	}
	

	private String please_check_net, send_fail, send, re_send;

	private void getString() {
		please_check_net = getResources().getString(R.string.please_check_net);
		send_fail = getResources().getString(R.string.send_fail);
		send = getResources().getString(R.string.send);
		re_send = getResources().getString(R.string.re_send);
	}
	Long current_time_tag;
	private Long getCurrent_time_tag() {
		return current_time_tag;
	}

	private void setCurrent_time_tag(Long current_time_tag) {
		this.current_time_tag = current_time_tag;
	}

	private void setListner() {
		tv_send.setOnClickListener(new OnClickListener() {

			private MyApplication yAPP;

			@Override
			public void onClick(View v) {
				String content = et_et.getText().toString().trim();
				if (!TextUtils.isEmpty(content)) {
					if (!NetUtil.iSHasNet(ChatActivity.this)) {
						 sendFail();
						ToastUtil.show(ChatActivity.this, send_fail + "\n" + please_check_net);
						return;
					}
					ChatInterface chat = ChatServiceConn.getChat();
					if (chat != null) {
						JSONObject json = new JSONObject();
						try {
							if (yAPP == null)
								 yAPP = MyApplication.getInstance();
							json.put(IMConstants.FLAG, IMConstants.FLAG_MESSAGE);
							json.put(UserMode.UID, yAPP.getUid());
							json.put(UserMode.UID2, mUid);
							json.put(ChatMode.CONTENT, content);
							final Long time_tag =System.currentTimeMillis()/1000;
							setCurrent_time_tag(time_tag);
							if (chat.isSessioinConnect()) {
								long getmNetCurrentTime = ChatIoHandler.getmNetCurrentTime();
								json.put(ChatMode.TIME, getmNetCurrentTime);
								chat.send(json.toString());
								new Thread(new Runnable() {
									public void run() {
										try {
											Thread.sleep(10000);
											Long current_time_tag2 = getCurrent_time_tag();
											if(current_time_tag2==time_tag){
												runOnUiThread(new Runnable() {
													public void run() {
														ToastUtil.show(ChatActivity.this, send_fail);
														sendFail();
													}
												});
											}
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
									}
								}).start();
								sending();
							} else {
								ToastUtil.show(ChatActivity.this, send_fail);
								sendFail();
								chat.connect();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							ToastUtil.show(ChatActivity.this, send_fail);
							sendFail();
						}
					} else {
						ToastUtil.show(ChatActivity.this, send_fail);
						sendFail();
					}
				} else {
					// 不能为空
				}
			}
		});

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(IMConstants.ACTION_MESSAGE);
		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String str_json = intent.getStringExtra(IMConstants.PROPERTY_DATA);
				MyApplication yApp = MyApplication.getInstance();
				int uid_now = yApp.getUid();
				JSONObject json;
				try {
					json = new JSONObject(str_json);
					String flag = json.getString(IMConstants.FLAG);
					if (IMConstants.FLAG_MESSAGE.equals(flag)) {
						int uid = json.getInt(ChatMode.UID);
						if(uid_now==uid){
							//发送
							sendOK();
							getChatList();
						} else {
							//接收
							if(uid==mUid){
								//是这个Activity
								setNeedChagneData(true);
								judgeToUpdateData();
							} 
							
						}
					} else if(IMConstants.FLAG_HAS_GET_NET_CHAT.equals(flag)){
						getChatList();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.v(IMConstants.TAG_YJ, "str_json-JSONException---"+e.toString());
				}
			}
		}, intentFilter);
		et_et.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String string = s.toString();
				saveChat(string);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		et_et.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etReplace(et_et.getText(), 0);
				mGvExpression.setVisibility(View.GONE);
				return false;
			}
		});
		controlKeyboardLayout(ll_reply);
	}
	public GridView mGvExpression;
	private void controlKeyboardLayout(final View root) {
		root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				root.getWindowVisibleDisplayFrame(rect);
				int mRootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
				if (mRootInvisibleHeight > 100) {
					lv_list.setSelection(chatList.size()-1);
				} 
			}
		});
	}
	
	private void judgeToUpdateData(){
		if(isNeedChagneData()&&!isPause()){
			setNeedChagneData(false);
			getChatList();
		}
	}
	
	
	private boolean isPause = true;
	private boolean needChagneData = false;
	
	public boolean isNeedChagneData() {
		return needChagneData;
	}

	public void setNeedChagneData(boolean needChagneData) {
		this.needChagneData = needChagneData;
	}

	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

	@Override
	public void onPause() {
		super.onPause();
		setPause(true);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setShare_uid(mUid);
		setPause(false);
		judgeToUpdateData();
	}

	private void saveChat(String content) {
		try {
			mPref.edit().putString(MD5Encoder.encode(String.valueOf(mUid)), content).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getChat() {
		String string = "";
		try {
			string = mPref.getString(MD5Encoder.encode(String.valueOf(mUid)), "");
			etReplace(string, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}
	
	// 把光标放到最后面
		private void setTextCursor() {
			Editable text = et_et.getText();
			int position = text.length();
			Selection.setSelection(text, position);
		}

	private void sendFail() {
		fl_progress.setVisibility(View.GONE);
		et_et.setTextColor(Color.BLACK);
		et_et.setEnabled(true);
		tv_send.setText(re_send);
		tv_send.setTextColor(Color.RED);
		tv_send.setVisibility(View.VISIBLE);
	}
	private void sendOK() {
		long currentTimeMillis = System.currentTimeMillis();
		setCurrent_time_tag(currentTimeMillis);
		mGvExpression.setVisibility(View.GONE);
		fl_progress.setVisibility(View.GONE);
		et_et.setTextColor(Color.BLACK);
		et_et.setText("");
		et_et.setEnabled(true);
		tv_send.setText(send);
		tv_send.setTextColor(Color.BLACK);
		tv_send.setVisibility(View.VISIBLE);
	}
	private void sending() {
		tv_send.setVisibility(View.GONE);
		et_et.setTextColor(Color.GRAY);
		et_et.setEnabled(false);
		fl_progress.setVisibility(View.VISIBLE);
		MyRotateAnimation.rotateAnimation(iv_progress);
	}


	private void getChatList() {
		if (yDB == null)
			yDB = new YCOpenHelperTest(this);
		List<ChatMode> dataList;
		if (chatList.size() == 0) {
			dataList = yDB.queryAll(mUid, 0);
		} else {
			dataList = yDB.queryAll(mUid, chatList.get(chatList.size() - 1).getId());
		}
		chatList.addAll(dataList);
		chatAdapter.setData(chatList);
		chatAdapter.notifyDataSetChanged();
		lv_list.setSelection(chatList.size() - 1);
	}
	LinearLayout ll_reply;
	HeaderLayout common_actionbar;
	private void findViewById() {
		lv_list = (ListView) findViewById(R.id.lv_list);
		ll_reply = (LinearLayout) findViewById(R.id.ll_reply);
		common_actionbar = (HeaderLayout) findViewById(R.id.common_actionbar);
		fl_reply = (FrameLayout) findViewById(R.id.fl_reply);
		fl_reply.addView(addSendView());
		common_actionbar.setBack();
		common_actionbar.setTitle(StringUtil.spanNameUid(mName, mUid));
		common_actionbar.setRigthVisibility(false);
	}

	FrameLayout fl_progress;
	ImageView iv_progress,iv_expression;
	private SharedPreferences mPref;
	SmileyParser mSmileyParser;
	String[] mExpressionStrArray;
	private String mName;
	
	private View addSendView() {
		View view = View.inflate(ChatActivity.this, R.layout.view_send, null);
		et_et = (EditText) view.findViewById(R.id.et_et);
		fl_progress = (FrameLayout) view.findViewById(R.id.fl_progress);
		iv_progress = (ImageView) view.findViewById(R.id.iv_progress);
		iv_expression = (ImageView) view.findViewById(R.id.iv_expression);
		mGvExpression = (GridView) view.findViewById(R.id.gv_expression);
		tv_send = (TextView) view.findViewById(R.id.tv_send);
		mGvExpression.setAdapter(new ExpressionAdapter(ChatActivity.this));
		// // 去掉GridView背景
				mGvExpression.setSelector(new ColorDrawable(Color.TRANSPARENT));
				mSmileyParser = SmileyParser.getInstance(this);
				mExpressionStrArray = Expressioin.expressionStr();
				mGvExpression.setOnItemClickListener(new GvExpressionItemListener());
		iv_expression.setOnClickListener(new TvExpressionListener());
		return view;
	}
	
	class TvExpressionListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			SoftInput.KeyBoardCancle(ChatActivity.this);
			mGvExpression.setVisibility(View.VISIBLE);
		}

	}
	
	class GvExpressionItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String str = mExpressionStrArray[position].toString();
			String et_str = et_et.getText().toString();
			int selectionStart = et_et.getSelectionStart();
			String substring1 = et_str.substring(0, selectionStart);
			String substring2 = et_str.substring(selectionStart);
			et_str = substring1 + str + substring2;
			int length = str.length();
			etReplace(et_str, length);
		}

	}
	
	private void etReplace(CharSequence str, int expressionLength) {
		CharSequence replace = mSmileyParser.strToSmiley(str, 50);
		int selectionStart = et_et.getSelectionStart();
		et_et.setText(replace);
		et_et.setSelection(selectionStart + expressionLength);// 设置光标位置
	}


}
