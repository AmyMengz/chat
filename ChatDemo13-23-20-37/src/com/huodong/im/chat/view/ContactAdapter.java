package com.huodong.im.chat.view;

import java.util.ArrayList;

import com.huodong.im.chat.mode.User;
import com.huodong.im.chat.util.StringUtil;
import com.huodong.im.chat.util.pic.ImageLoader;
import com.huodong.im.chatdemo.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter {
	ArrayList<User> data = new ArrayList<User>();
	Context context;

	public ContactAdapter(Context context) {
		super();
		this.context = context;
		imageLoader = new ImageLoader(context, true);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	public ArrayList<User> getData() {
		return data;
	}
	boolean isSearch = false;
	public void setData(ArrayList<User> data,boolean isSearch) {
		this.data = data;
		this.isSearch = isSearch;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	private ImageLoader imageLoader;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.item_add_friend, null);
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.avatar.setBackgroundResource(R.drawable.new_fds_150);
		final User user = data.get(position);
		int uid = user.uid;
		imageLoader.DisplayImage(String.valueOf(uid), holder.avatar);
		holder.name.setText(StringUtil.spanNameUid(user.name, uid));
		String alpha = user.getAlpha();
		if(TextUtils.isEmpty(alpha)||isSearch){
			holder.alpha.setVisibility(View.GONE);
		} else {
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(alpha);
		}
		return convertView;
	}

	class Holder {
		ImageView avatar;
		TextView name;
		TextView alpha;
	}

}
