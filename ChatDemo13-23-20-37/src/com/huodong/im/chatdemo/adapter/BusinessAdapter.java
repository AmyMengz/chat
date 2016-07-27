package com.huodong.im.chatdemo.adapter;

import java.util.List;

import org.w3c.dom.Text;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.SelectCityActivity;
import com.huodong.im.config.FeesType;
import com.huodong.im.entity.BussinessInfoEntity;
import com.huodong.im.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BusinessAdapter extends BaseAdapter {
	private List<BussinessInfoEntity> list;
	private Context context;  
	public BusinessAdapter(List<BussinessInfoEntity>list,Context context){
		this.context=context;
		this.list=list;
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
		final BussinessInfoEntity businessInfo=list.get(position);
		ViewHolder holder;
		if(convertView==null){
			LayoutInflater inflater=LayoutInflater.from(context);
			convertView=inflater.inflate(R.layout.item_bussinessinfo, null);
			holder=new ViewHolder();
			holder.avatar=(ImageView)convertView.findViewById(R.id.avatar);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.rating=(ImageView)convertView.findViewById(R.id.rating);
			holder.avgPrice=(TextView)convertView.findViewById(R.id.avg_price);
			holder.regions=(TextView)convertView.findViewById(R.id.regions);
			holder.distance=(TextView)convertView.findViewById(R.id.distance);
			holder.discountNum=(TextView)convertView.findViewById(R.id.discount_num);
			holder.btnSelect=(TextView)convertView.findViewById(R.id.btn_select);
			holder.location=(ImageView)convertView.findViewById(R.id.location);
			
			convertView.setTag(holder);
		}
		else {
			holder=(ViewHolder) convertView.getTag();
		}
//		holder.avatar.setImageResource(R.drawable.)
//		ImageLoader.getInstance().displayImage(uri, imageView, options);
		String url=businessInfo.getPhotoUrl();
		ImageLoaderUtil.getImageLoaderInstance().displayImage(url, holder.avatar, 
				ImageLoaderUtil.getAvatarOptions(25, R.drawable.default_face));
		ImageLoaderUtil.getImageLoaderInstance().displayImage(businessInfo.getRatingImgUrl(), holder.rating, 
				ImageLoaderUtil.getAvatarOptions(0, R.drawable.default_face));
		holder.name.setText(businessInfo.getName());
		holder.avgPrice.setText(String.format(context.getString(R.string.average_price), businessInfo.getAvgPrice()));
		holder.regions.setText(businessInfo.getRegions());
		holder.distance.setText(String.format(context.getString(R.string.distance_mail),businessInfo.getDistance()));
		holder.discountNum.setText(String.format(context.getString(R.string.deals_num),businessInfo.getDeals()));
//		holder.btnSelect.setText(businessInfo.getPartnerType().toString());
//		holder.feesType.setText(businessInfo.getFeesType().toString());
//		holder.date_time.setText(businessInfo.getDateTime());
//		holder.apply.setText(businessInfo.getApply());
//		holder.comment.setText(businessInfo.getComment());
		holder.btnSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                Log.i("Amy bussinessAda", businessInfo.getLongtitude()+ "  "+businessInfo.getLatitude());
                intent.putExtra("name", businessInfo.getName());
                intent.putExtra("address", businessInfo.getAddress());
                intent.putExtra("longitude", businessInfo.getLongtitude());
                intent.putExtra("latitude", businessInfo.getLatitude());
                //设置返回数据
                ((Activity) context).setResult(Activity.RESULT_OK, intent);
                //关闭Activity
                ((Activity) context).finish();
			}
		});
		return convertView;
	}
	public class ViewHolder{
		public ImageView avatar;
		public TextView name;
		public ImageView rating;
		public TextView avgPrice;
		public TextView regions;
		public TextView distance;
		public TextView discountNum;
		public TextView btnSelect;
		public ImageView location;
	}

}
