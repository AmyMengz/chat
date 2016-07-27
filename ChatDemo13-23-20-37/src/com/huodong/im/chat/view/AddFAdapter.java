package com.huodong.im.chat.view;

import java.util.ArrayList;

import com.huodong.im.chat.mode.User;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.ToActivityUtil;
import com.huodong.im.chat.util.pic.ImageLoader;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.MyApplication;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddFAdapter extends BaseAdapter {
	ArrayList<User> data = new ArrayList<User>();
	Context context;
	private ImageLoader imageLoader;
	public AddFAdapter(Context context) {
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

	public void setData(ArrayList<User> data) {
		this.data = data;
	}
	
	public void removeData(){
		data.removeAll(data);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(context, R.layout.item_add_friend, null);
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.btn_add = (Button) convertView.findViewById(R.id.btn_add);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.avatar.setBackgroundResource(R.drawable.new_fds_150);
		final User user = data.get(position);
		final int id = user.id;
		holder.name.setText(user.name + " (" + id+ ")");
		holder.btn_add.setVisibility(View.VISIBLE);
		imageLoader.DisplayImage(String.valueOf(id), holder.avatar);
		holder.btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyApplication yApp = MyApplication.getInstance();
				int uid = yApp.getUid();
				ToActivityUtil.applyFdsForResult(uid, id, user.name, context,IMConstants.ChaType.APPLY_FDS);
			}
		});
		return convertView;
	}

	class Holder {
		ImageView avatar;
		Button btn_add;
		TextView name;
	}

}
