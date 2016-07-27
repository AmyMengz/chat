package com.huodong.im.chatdemo.adapter;

import java.util.List;

import com.huodong.im.chatdemo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;
	 private LayoutInflater inflater=null;
	
	public GridViewAdapter(Context context,List<String> list){
		this.context=context;
		this.list=list;
		inflater=LayoutInflater.from(context);
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
	        // TODO Auto-generated method stub  
//	        获得holder以及holder对象中tv和img对象的实例  
	        Holder holder;  
	        if(convertView==null){ 
	            convertView=inflater.inflate(R.layout.item_region, null);  
	            holder=new Holder();  
	            holder.tv=(TextView) convertView.findViewById(R.id.region);  
	            holder.img=(ImageView) convertView.findViewById(R.id.gridview_img);   
	            convertView.setTag(holder);        
	        }else{  
	            holder=(Holder) convertView.getTag();  
	              
	        }  
	        if(clickTemp == position){
	        	holder.tv.setTextColor(context.getResources().getColor(R.color.common_botton_bar_blue));
	        }else {
	        	holder.tv.setTextColor(context.getResources().getColor(R.color.black));
			}
//	        为holder中的tv和img设置内容  
	        holder.tv.setText(list.get(position));  
//	        holder.img.setImageResource(imgId[position]);  
//	        注意  默认为返回null,必须得返回convertView视图  
	        return convertView;  
	    }  
	 int clickTemp=0;
	//标识选择的Item
		public void setSeclection(int position) {
		clickTemp = position;
		}
	 public class Holder{  
         
	       public TextView tv=null;  
	       public ImageView img=null;  
	        public TextView getTv() {  
	            return tv;  
	        }  
	        public void setTv(TextView tv) {  
	            this.tv = tv;  
	        }  
	        public ImageView getImg() {  
	            return img;  
	        }  
	        public void setImg(ImageView img) {  
	            this.img = img;  
	        }  
	          
	    }  

}
