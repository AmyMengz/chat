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
import com.huodong.im.chat.util.StringUtil;
import com.huodong.im.chat.util.pic.ImageLoader;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.fragment.ChatFragment;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HistoryChatAdapter extends BaseAdapter {
	List<ChatMode> data = new ArrayList<ChatMode>();
	Context context;
	ChatFragment chatFragment;
	int color_sky_56abe4;
	SmileyParser mSmileyParser;
	private ImageLoader imageLoader;
	public HistoryChatAdapter(Context context, ChatFragment chatFragment) {
		super();
		this.context = context;
		this.chatFragment = chatFragment;
		mSetReadUtil = new SetReadUtil(context);
		yDB = new YCOpenHelperTest(context);
		toGetResources();
		mSmileyParser = SmileyParser.getInstance(context);
		imageLoader = new ImageLoader(context, true);
	}

	String new_fds,has_be_fds,had_apply_fds,apply_to_be_fds;
	
	private void toGetResources() {
		color_sky_56abe4 = context.getResources().getColor(R.color.sky_56abe4);
		new_fds =context.getResources().getString(R.string.new_fds);
		has_be_fds =context.getResources().getString(R.string.has_be_fds);
		had_apply_fds =context.getResources().getString(R.string.had_apply_fds);
		apply_to_be_fds =context.getResources().getString(R.string.apply_to_be_fds);
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
	HashMap<Integer, Integer> mHasSetReadMap = new HashMap<Integer,Integer>();
	HashMap<Integer, Integer> mNameGetMap = new HashMap<Integer,Integer>();
	HashMap<Integer, String> mNameMap = new HashMap<Integer,String>();
	private SetReadUtil mSetReadUtil;
	private YCOpenHelperTest yDB;
	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.item_add_friend, null);
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.iv_sanjiao = (ImageView) convertView.findViewById(R.id.iv_sanjiao);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.avatar.setBackgroundResource(R.drawable.new_fds_150);
		ChatMode chatMode = data.get(position);
		int id = chatMode.getId();
		int type = chatMode.getType();
		int isRead = chatMode.getIsRead();
		String name = chatMode.name;
		int uid = chatMode.getUid();
		imageLoader.DisplayImage(String.valueOf(uid), holder.avatar);
		Long time = chatMode.getTime();
		if(TextUtils.isEmpty(name)){
			if(mNameMap.containsKey(uid)){
				 name = mNameMap.get(uid);
			}else {
				mNameGetMap.put(uid, 1);
				name = yDB.getName(uid);
				if(TextUtils.isEmpty(name)){
					mNameGetMap.remove(uid);
				} else {
					mNameMap.put(uid, name);
				}
			}
		}
		if (type == IMConstants.ChaType.CHAT_FROM || type == IMConstants.ChaType.CHAT_TO) {
			CharSequence strToSmiley = mSmileyParser.strToSmiley(chatMode.content, 40);
			if(strToSmiley.length()>CONTENT_COUNT){
				strToSmiley = strToSmiley.subSequence(0, CONTENT_COUNT);
				strToSmiley = StringUtil.SpanAppend(strToSmiley," ...");
			}
			String str =GetTimeUtil.getYToMin(time);
			CharSequence spanNameUid = StringUtil.spanNameUid(name, uid);
			CharSequence spanSize2 = StringUtil.SpanSize(str, IMConstants.TEXT_SIZE_30);
			CharSequence spanAppendLn2 = StringUtil.SpanAppendLn(strToSmiley,spanSize2);
			CharSequence spanColor3 = StringUtil.SpanColor(spanAppendLn2, Color.GRAY);
			CharSequence spanAppendLn = StringUtil.SpanAppendLn(spanNameUid,spanColor3);
			holder.name.setText(spanAppendLn);
			if(type == IMConstants.ChaType.CHAT_FROM){
				if(isRead!=2){
					SetReadUtil.setOfNet(chatMode.chatid,isRead,context);
				}
				if(isRead!=1&&isRead!=2){
					mSetReadUtil.setOfLocal(id, isRead);
					chatFragment.setNeedChagneData(true);
					holder.iv_sanjiao.setVisibility(View.VISIBLE);
				} else {
					holder.iv_sanjiao.setVisibility(View.INVISIBLE);
				}
			} else {
				holder.iv_sanjiao.setVisibility(View.INVISIBLE);
			}
		} else {
			if(isRead!=2){
				SetReadUtil.setOfNet(chatMode.chatid,isRead,context);
			}
			if(isRead!=1&&isRead!=2){
				mSetReadUtil.setOfLocal(id, isRead);
				chatFragment.setNeedChagneData(true);
				holder.iv_sanjiao.setVisibility(View.VISIBLE);
			} else {
				holder.iv_sanjiao.setVisibility(View.INVISIBLE);
			}
			String str = name + "(" + uid + ")   ";
			switch (type) {
			case IMConstants.ChaType.AGREE:
				str += has_be_fds;
				break;
			case IMConstants.ChaType.APPLY_FDS:
				str += had_apply_fds;
				break;
			case IMConstants.ChaType.BE_APPLY_FDS:
				str += apply_to_be_fds;
				break;
			default:
				break;
			}
			str+="\n"+GetTimeUtil.getYToMin(time);
			CharSequence spanSize1 = StringUtil.SpanSize(new_fds,IMConstants.TEXT_SIZE_38);
			CharSequence spanColor4 = StringUtil.SpanColor(spanSize1, color_sky_56abe4);
			CharSequence spanSize2 = StringUtil.SpanSize(str, IMConstants.TEXT_SIZE_30);
			CharSequence spanColor3 = StringUtil.SpanColor(spanSize2, Color.GRAY);
			CharSequence spanAppendLn = StringUtil.SpanAppendLn(spanColor4,spanColor3);
			holder.name.setText(spanAppendLn);
		}
		return convertView;
	}

	final int CONTENT_COUNT = 12;

	class Holder {
		ImageView avatar;
		ImageView iv_sanjiao;
		TextView name;
	}

}
