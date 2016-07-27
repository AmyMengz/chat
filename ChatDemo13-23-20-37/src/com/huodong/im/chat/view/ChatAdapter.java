package com.huodong.im.chat.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.huodong.im.chat.db.YCOpenHelperTest;
import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.util.GetTimeUtil;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.SetReadUtil;
import com.huodong.im.chat.util.SmileyParser;
import com.huodong.im.chat.util.pic.ImageLoader;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.MyApplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {
	List<ChatMode> data = new ArrayList<ChatMode>();
	Context context;
	SmileyParser mSmileyParser;
	private ImageLoader imageLoader;
	int uid_from;
	int uid_to;
	public ChatAdapter(Context context, int mUid) {
		super();
		this.context = context;
		mSmileyParser = SmileyParser.getInstance(context);
		yDB = new YCOpenHelperTest(context);
		imageLoader = new ImageLoader(context, true);
		uid_from=mUid;
		MyApplication yApp = MyApplication.getInstance();
		 uid_to = yApp.getUid();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	public void setData(List<ChatMode> data) {
		this.data = data;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	HashMap<Integer, Integer> mHasSetReadMap = new HashMap<Integer,Integer>();
	private YCOpenHelperTest yDB;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.view_chat, null);
			holder.iv_portrait_from = (ImageView) convertView.findViewById(R.id.iv_portrait_from);
			holder.iv_portrait_to = (ImageView) convertView.findViewById(R.id.iv_portrait_to);
			holder.tv_content_from = (TextView) convertView.findViewById(R.id.tv_content_from);
			holder.tv_content_to = (TextView) convertView.findViewById(R.id.tv_content_to);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
			
		}
		ChatMode chatMode = data.get(position);
		int type = chatMode.getType();
		setTime(position,holder.tv_time);
		int id = chatMode.getId();
		switch (type) {
		case IMConstants.ChaType.CHAT_FROM:
			int isRead = chatMode.getIsRead();
			if(isRead!=2){
				SetReadUtil.setOfNet(chatMode.chatid,isRead,context);
			}
			if(isRead!=1&&!mHasSetReadMap.containsKey(id)&&isRead!=2){
				mHasSetReadMap.put(id, 1);
				yDB.setChatReadOfLocal(id);
			}
			holder.iv_portrait_to.setVisibility(View.GONE);
			holder.tv_content_to.setVisibility(View.GONE);
			mSmileyParser.tvReplace(chatMode.content, holder.tv_content_from);
			holder.iv_portrait_from.setVisibility(View.VISIBLE);
			holder.tv_content_from.setVisibility(View.VISIBLE);
			imageLoader.DisplayImage(String.valueOf(uid_from), holder.iv_portrait_from);
			holder.iv_portrait_from.setBackgroundResource(R.drawable.new_fds_150);
			break;
		case IMConstants.ChaType.CHAT_TO:
			holder.iv_portrait_from.setVisibility(View.GONE);
			holder.tv_content_from.setVisibility(View.GONE);
			mSmileyParser.tvReplace(chatMode.content, holder.tv_content_to);
			holder.iv_portrait_to.setVisibility(View.VISIBLE);
			holder.tv_content_to.setVisibility(View.VISIBLE);
			imageLoader.DisplayImage(String.valueOf(uid_to), holder.iv_portrait_to);
			holder.iv_portrait_to.setBackgroundResource(R.drawable.new_fds_150);
			break;

		default:
			break;
		}
		
		return convertView;
	}

	private void setTime(int position, TextView tv_time) {
		Long time = data.get(position).getTime();
//		if(position==0){
//			tv_time.setText(GetTimeUtil.getYToMin(time));
//			tv_time.setVisibility(View.VISIBLE);
//		} else {
//			Long lastTime = data.get(position-1).getTime();
//			Long dif_time =time-lastTime;
//			if(dif_time>IMConstants.FIVE_MIN){
//				tv_time.setText(GetTimeUtil.getYToMin(time));
//				tv_time.setVisibility(View.VISIBLE);
//			} else {
//				tv_time.setVisibility(View.GONE);
//			}
//		}
		tv_time.setText(GetTimeUtil.getFullTime(time));
		tv_time.setVisibility(View.VISIBLE);
	}

	class Holder {
		ImageView iv_portrait_from;
		ImageView iv_portrait_to;
		TextView tv_content_from;
		TextView tv_content_to;
		TextView tv_time;
	}

}
