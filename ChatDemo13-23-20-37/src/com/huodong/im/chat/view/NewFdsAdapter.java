package com.huodong.im.chat.view;

import java.util.ArrayList;
import java.util.List;

import com.huodong.im.chat.NewFdsActivity;
import com.huodong.im.chat.db.YCOpenHelperTest;
import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.service.ChatIoHandler;
import com.huodong.im.chat.util.GetTimeUtil;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.StringUtil;
import com.huodong.im.chat.util.ToActivityUtil;
import com.huodong.im.chat.util.ToastUtil;
import com.huodong.im.chat.util.pic.ImageLoader;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.MyApplication;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewFdsAdapter extends BaseAdapter {
	List<ChatMode> data = new ArrayList<ChatMode>();
	NewFdsActivity mNewFdsActivity;
	private ImageLoader imageLoader;
	public NewFdsAdapter(NewFdsActivity context) {
		super();
		this.mNewFdsActivity = context;
		imageLoader = new ImageLoader(context, true);
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(mNewFdsActivity, R.layout.view_new_fds, null);
			holder.iv_portrait = (ImageView) convertView.findViewById(R.id.iv_portrait);
			holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
			holder.btn_unagree = (TextView) convertView.findViewById(R.id.btn_unagree);
			holder.btn_agree = (TextView) convertView.findViewById(R.id.btn_agree);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final ChatMode chatMode = data.get(position);
		int type = chatMode.getType();
		String name = chatMode.name;
		String time = GetTimeUtil.getYToMin(chatMode.getTime());
		int uid = chatMode.uid;
		String str1=name+ " (" + uid + ")" ;
		imageLoader.DisplayImage(String.valueOf(uid), holder.iv_portrait);
		String str2 ="";
		switch (type) {
		case IMConstants.ChaType.AGREE:
			holder.btn_unagree.setVisibility(View.INVISIBLE);
			holder.btn_agree.setVisibility(View.INVISIBLE);
			str2 ="你们已经成为好友";
			break;
		case IMConstants.ChaType.APPLY_FDS:
			holder.btn_unagree.setVisibility(View.INVISIBLE);
			holder.btn_agree.setVisibility(View.INVISIBLE);
			str2 ="已经申请好友";
			break;
		case IMConstants.ChaType.BE_APPLY_FDS:
			holder.btn_unagree.setVisibility(View.VISIBLE);
			holder.btn_agree.setVisibility(View.VISIBLE);
			str2 ="请求加为好友";
			break;
		case IMConstants.ChaType.UN_AGREE:
			holder.btn_unagree.setVisibility(View.GONE);
			holder.btn_agree.setVisibility(View.GONE);
			str2 ="你已拒绝";
			break;

		default:
			break;
		}
		str2+="\n"+time;
		CharSequence spanSize1 = StringUtil.SpanSize(str1,IMConstants.TEXT_SIZE_38);
		CharSequence spanSize2 = StringUtil.SpanSize(str2, IMConstants.TEXT_SIZE_30);
		CharSequence spanColor3 = StringUtil.SpanColor(spanSize2, Color.GRAY);
		CharSequence spanAppendLn = StringUtil.SpanAppendLn(spanSize1,spanColor3);
		holder.tv_text.setText(spanAppendLn);
		holder.btn_unagree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				long time = ChatIoHandler.getmNetCurrentTime();
				YCOpenHelperTest yDB= new YCOpenHelperTest(mNewFdsActivity);
				if(time>0){
					yDB.insert(time, chatMode.name, IMConstants.ChaType.UN_AGREE, chatMode.uid);
					mNewFdsActivity.getLocalData();
				} else {
					ToastUtil.show(mNewFdsActivity, StringUtil.getStr(mNewFdsActivity, R.string.connect_error));
				}
			}
		});
		holder.btn_agree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyApplication yApp = MyApplication.getInstance();
				int uid = yApp.getUid();
				agreeing_pos = position;
				ToActivityUtil.applyFdsForResult(uid, chatMode.uid, chatMode.name, mNewFdsActivity, IMConstants.ChaType.AGREE);
			}
		});

		return convertView;
	}


	private int agreeing_pos = -1;

	public int getAgreeing_pos() {
		return agreeing_pos;
	}

	public void setAgreeing_pos(int agreeing_pos) {
		this.agreeing_pos = agreeing_pos;
	}

	class Holder {
		ImageView iv_portrait;
		TextView btn_unagree;
		TextView btn_agree;
		TextView tv_text;
	}

}
