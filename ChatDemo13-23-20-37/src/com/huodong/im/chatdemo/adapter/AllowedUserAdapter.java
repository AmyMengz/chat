package com.huodong.im.chatdemo.adapter;

import java.util.List;

import com.huodong.im.chat.util.pic.ImageLoader;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.DateDetailActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllowedUserAdapter extends BaseAdapter{
	private Context context;
	private List<Integer> list;
	 private LayoutInflater inflater=null;
	 ImageLoader imageLoader;
	 public AllowedUserAdapter(Context context,List<Integer> list){
			this.context=context;
			this.list=list;
			inflater=LayoutInflater.from(context);
			imageLoader = new ImageLoader(context, true);
			/*imageLoader.DisplayImage(String.valueOf(47), avatar);*/
			
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
//		        获得holder以及holder对象中tv和img对象的实例  
		        Holder holder;  
		        if(convertView==null){  
		              
		              
		            convertView=inflater.inflate(R.layout.item_allowed, null);  
		            holder=new Holder();  
		            holder.tv=(TextView) convertView.findViewById(R.id.region);  
		            holder.img=(ImageView) convertView.findViewById(R.id.gridview_img);  
		              
		            convertView.setTag(holder);  
		              
		        }else{  
		            holder=(Holder) convertView.getTag();  
		              
		        }  
//		        为holder中的tv和img设置内容  
//		        holder.tv.setText(list.get(position));   
		        imageLoader.DisplayImage(String.valueOf(list.get(position)), holder.img);
		       /* holder.img.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});*/
//		        注意  默认为返回null,必须得返回convertView视图  
		        return convertView;  
		    }  
		 private class Holder{  
	         
		        TextView tv=null;  
		        ImageView img=null;  
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