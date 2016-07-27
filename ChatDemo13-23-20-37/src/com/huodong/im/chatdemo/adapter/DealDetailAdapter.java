package com.huodong.im.chatdemo.adapter;

import java.util.List;

import com.huodong.im.chatdemo.R;
import com.huodong.im.entity.BussinessDealEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DealDetailAdapter extends BaseAdapter {
	List<BussinessDealEntity> list;
	Context context;
	public DealDetailAdapter(Context context,List<BussinessDealEntity> list){
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
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			LayoutInflater inflater=LayoutInflater.from(context);
			convertView=inflater.inflate(R.layout.item_deal_detail, null);
			holder.description=(TextView)convertView.findViewById(R.id.descripton);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		holder.description.setText(list.get(position).getDescription());
		return convertView;
	}
	public class ViewHolder{
		public TextView description;
		public ImageView url;
	}

}
