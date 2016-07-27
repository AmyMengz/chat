package com.huodong.im.chatdemo.adapter;

import java.text.DecimalFormat;
import java.util.List;

import org.w3c.dom.Text;

import com.huodong.im.chat.util.pic.ImageLoader;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.DateDetailActivity;
import com.huodong.im.config.FeesType;
import com.huodong.im.entity.SearchDateEntity;
import com.huodong.im.utils.CommonUtil;
import com.huodong.im.utils.IMDateUserHelper;
import com.huodong.im.utils.ImageLoaderUtil;
//import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DateAdapter extends BaseAdapter {
	private List<SearchDateEntity> searchDateList;
	private Context context;  
	ImageLoader imageLoader;
	public DateAdapter(List<SearchDateEntity>searchDateList,Context context){
		this.context=context;
		this.searchDateList=searchDateList;
		imageLoader = new ImageLoader(context, true);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return searchDateList.size();
	}
	/** 
     * 当ListView数据发生变化时,调用此方法来更新ListView 
     * @param list 
     */  
    public void updateListView(List<SearchDateEntity> list){  
        this.searchDateList = list;  
        notifyDataSetChanged();  
    }  

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return searchDateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("Amy item", "position view  "+position);
		SearchDateEntity entity=searchDateList.get(position);
		ViewHolder holder;
		if(convertView==null){
			LayoutInflater inflater=LayoutInflater.from(context);
			convertView=inflater.inflate(R.layout.item_date, null);
			holder=new ViewHolder();
			holder.avatar=(ImageView)convertView.findViewById(R.id.avatar);
			holder.title=(TextView)convertView.findViewById(R.id.date_title);
			holder.userInfo=(TextView)convertView.findViewById(R.id.user_age);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.address=(TextView)convertView.findViewById(R.id.address);
			holder.distance=(TextView)convertView.findViewById(R.id.distance);
			holder.partnerCount=(TextView)convertView.findViewById(R.id.partner_count);
			holder.partnerType=(TextView)convertView.findViewById(R.id.partner_type);
			holder.feesType=(TextView)convertView.findViewById(R.id.fees);
			holder.date_time=(TextView)convertView.findViewById(R.id.date_time);
			holder.apply=(TextView)convertView.findViewById(R.id.date_apply);
			holder.comment=(TextView)convertView.findViewById(R.id.comment);
			holder.hasApply=(ImageView)convertView.findViewById(R.id.has_apply);
			convertView.setTag(holder);
		}
		else {
			holder=(ViewHolder) convertView.getTag();
		}
//		holder.avatar.setImageResource(R.drawable.)
//		ImageLoader.getInstance().displayImage(uri, imageView, options);
		String url=entity.getAvatar();
//		ImageLoaderUtil.getImageLoaderInstance().displayImage("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=285008002,1359587403&fm=58", holder.avatar, 
//				ImageLoaderUtil.getAvatarOptions(25, R.drawable.default_face));
		imageLoader.DisplayImage(String.valueOf(entity.getSponsorID()), holder.avatar);
		holder.title.setText(entity.getDateTitle());
		switch (entity.getGender()) {
		case 1://men
			holder.userInfo.setBackgroundResource(R.drawable.gender_boy);
			break;
		case 2://women
			holder.userInfo.setBackgroundResource(R.drawable.gender_gril);
			break;
		}
		holder.userInfo.setText(entity.getBirthday());
		holder.name.setText(entity.getNickName());
		holder.address.setText(entity.getDateAddress());
		holder.distance.setText(String.format(context.getString(R.string.distance_mail), IMDateUserHelper.formatDistance(entity.getDistance())));
		holder.partnerCount.setText(String.format(context.getString(R.string.limit_partner_num), entity.getPartnerNum()));
		
		holder.partnerType.setText("("+IMDateUserHelper.formatPartner(entity.getPartnerType())+")");
		holder.feesType.setText(IMDateUserHelper.formatFees(entity.getFeesType()));
		holder.date_time.setText(CommonUtil.getTimeString(entity.getDateTime()*1000,CommonUtil.timeFormat1));
		holder.apply.setText(String.format(context.getString(R.string.apply_num), entity.getApply()));
		holder.comment.setText(String.valueOf(entity.getComment()));
		Log.i("Amy getApplyState", "entity.getApplyState()  "+entity.getApplyState());
		switch (entity.getApplyState()) {
			case 1:
				 holder.hasApply.setVisibility(View.GONE);
				break;
			case 2:
				holder.hasApply.setVisibility(View.VISIBLE);
				holder.hasApply.setImageResource(R.drawable.apply);
				break;
			case 3:
				holder.hasApply.setVisibility(View.VISIBLE);
				holder.hasApply.setImageResource(R.drawable.pass);
				break;
		default:
			break;
		}
		return convertView;
	}	
	public class ViewHolder{
		public ImageView avatar;
		public TextView title;
		public TextView userInfo;
		public TextView name;
		public TextView address;
		public TextView distance;
		public TextView partnerCount;
		public TextView partnerType;
		public TextView feesType;
		public TextView date_time;
		public TextView apply;
		public TextView comment;
		private ImageView hasApply;
		
	}

}
