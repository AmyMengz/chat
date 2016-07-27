package com.huodong.im.chatdemo.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.huodong.im.chat.util.pic.ImageLoader;
import com.huodong.im.chatdemo.R;
import com.huodong.im.entity.NearbyUserEntity;
import com.huodong.im.utils.CommonUtil;
import com.huodong.im.utils.IMDateUserHelper;
import com.huodong.im.utils.IMUIHelper;
import com.huodong.im.utils.ImageLoaderUtil;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NearbyUserAdapter extends BaseAdapter {
	
	private Context context;
	private List<NearbyUserEntity> list;
	private boolean isManagerShow=false;
	private OnClickListener listener;
	ImageLoader imageLoader;
	public NearbyUserAdapter(Context context,List<NearbyUserEntity> list){
		this.context=context;
		this.list=list;
		imageLoader = new ImageLoader(context, true);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NearbyViewHolder holder;
		NearbyUserEntity entity=list.get(position);
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_nearby_user, null);
			holder=new NearbyViewHolder();
			holder.avatar=(ImageView)convertView.findViewById(R.id.avatar);
			holder.nickName=(TextView)convertView.findViewById(R.id.nick_name);
			holder.distance=(TextView)convertView.findViewById(R.id.distance);
			holder.recentTime=(TextView)convertView.findViewById(R.id.recent_time);
			holder.genderAge=(TextView)convertView.findViewById(R.id.gender_age);
			holder.dateTimes=(TextView)convertView.findViewById(R.id.date_times);
			holder.fans=(TextView)convertView.findViewById(R.id.fans);
			holder.signature=(TextView)convertView.findViewById(R.id.signature);
			holder.btnAllow=(TextView)convertView.findViewById(R.id.allow);
			holder.phone=(TextView)convertView.findViewById(R.id.phone);
			holder.manager=(RelativeLayout)convertView.findViewById(R.id.manager);
			convertView.setTag(holder);
		}
		else {
			holder=(NearbyViewHolder) convertView.getTag();
		}
		
		/*String url=entity.getAvatar();
		ImageLoaderUtil.getImageLoaderInstance().displayImage("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=285008002,1359587403&fm=58", holder.avatar, ImageLoaderUtil.getAvatarOptions(25, R.drawable.default_face));
		*/
		imageLoader.DisplayImage(String.valueOf(entity.getUid()), holder.avatar);
		holder.distance.setText(String.format(context.getString(R.string.distance_mail),entity.getDistance()));
		holder.nickName.setText(entity.getNickName());
		holder.recentTime.setText(" | "+IMDateUserHelper.getRecentTime(entity.getRecentTime()));
		switch (entity.getGender()) {
		case 1://men
			holder.genderAge.setBackgroundResource(R.drawable.gender_boy);
			break;
		case 2://women
			holder.genderAge.setBackgroundResource(R.drawable.gender_gril);
			break;
		}
		holder.genderAge.setText(""+IMDateUserHelper.getUserAge(entity.getBirthday(), System.currentTimeMillis()/1000));
		holder.dateTimes.setText(String.format(context.getString(R.string.date_times), entity.getDateTimes()));
		holder.fans.setText(String.format(context.getString(R.string.fans), entity.getFans()));
		if(TextUtils.isEmpty(entity.getSignature())){
			holder.signature.setText(context.getString(R.string.no_signature));
		}
		else {
			holder.signature.setText(entity.getSignature());
		}
		if(isManagerShow){
			holder.manager.setVisibility(View.VISIBLE);
			holder.signature.setVisibility(View.GONE);
			int isPassed=entity.getPassed();
			switch (isPassed) {
			case 0:
				holder.phone.setText(context.getString(R.string.allowed_to_phone));
				holder.btnAllow.setText(context.getString(R.string.allow));
				if(listener!=null){
					holder.btnAllow.setOnClickListener(listener);
				}
				break;
			case 1:
				holder.phone.setText(entity.getPhone());
				holder.btnAllow.setText(context.getString(R.string.allowed));
				holder.btnAllow.setClickable(false);
				break;
			}
		}
		else {
			holder.manager.setVisibility(View.GONE);
		}
		return convertView;
	}
	/**
	 * 设置批准部分是否显示
	 * @param show
	 */
	public void setApplyManagerShow(boolean show){
		isManagerShow=show;
	}
	
	public void setApplyManagerOnclick(OnClickListener listener){
		this.listener=listener;
	}

	
	public class NearbyViewHolder{
		public ImageView avatar;
		public TextView nickName;
		public TextView distance;
		public TextView recentTime;
		public TextView genderAge;
		public TextView dateTimes;
		public TextView fans;
		public TextView signature;
		public TextView phone;
		public TextView btnAllow;
		public RelativeLayout manager;
	}

}
