package com.huodong.im.chatdemo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.adapter.DealDetailAdapter;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.entity.BussinessDealEntity;
import com.huodong.im.entity.BussinessInfoEntity;
import com.huodong.im.utils.IMDateUserHelper;
import com.huodong.im.utils.IMUIHelper;
import com.huodong.im.utils.ImageLoaderUtil;

public class BussinessDetailActivity extends BaseActivity implements OnClickListener{
	private BussinessInfoEntity businessInfo;
//	private TextView title,rating,region,distance,address,phone;
	private ImageView avatar;
	private TextView name;
	private ImageView rating;
	private TextView avgPrice;
	private TextView regions;
	private TextView distance;
	private TextView discountNum;
	private TextView btnSelect;
//	public ImageView location,phone;
	private TextView address,phone;
	private ListView dealListview;
	private TextView goto_;
	private List<BussinessDealEntity> dealList=new ArrayList<BussinessDealEntity>();
	private DealDetailAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bussiness_detail);
		initDate();
		initView();
	}

	private void initView() {
		initTopBarForLeft(getString(R.string.bussiness_info));
		name=(TextView)findViewById(R.id.name);
		avatar=(ImageView)findViewById(R.id.avatar);
		
		rating=(ImageView)findViewById(R.id.rating);
		avgPrice=(TextView)findViewById(R.id.avg_price);
		regions=(TextView)findViewById(R.id.regions);
		distance=(TextView)findViewById(R.id.distance);
//		discountNum=(TextView)findViewById(R.id.discount_num);
		phone=(TextView)findViewById(R.id.phone);
		address=(TextView)findViewById(R.id.address);
		goto_=(TextView)findViewById(R.id.goto_);
		goto_.setOnClickListener(this);
		dealListview=(ListView)findViewById(R.id.deals);
		adapter=new DealDetailAdapter(BussinessDetailActivity.this, dealList);
		dealListview.setAdapter(adapter);
		IMUIHelper.setListViewHeightBasedOnChildren(dealListview);
		dealListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Uri uri = Uri.parse(dealList.get(position).getUrl());  
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
                startActivity(intent);
				
			}
		});
		
		String url=businessInfo.getPhotoUrl();
		ImageLoaderUtil.getImageLoaderInstance().displayImage(url, avatar, 
				ImageLoaderUtil.getAvatarOptions(25, R.drawable.default_face));
		ImageLoaderUtil.getImageLoaderInstance().displayImage(businessInfo.getRatingImgUrl(), rating, 
				ImageLoaderUtil.getAvatarOptions(0, R.drawable.default_face));
		name.setText(businessInfo.getName());
		avgPrice.setText(String.format(getString(R.string.average_price), businessInfo.getAvgPrice()));
		regions.setText(businessInfo.getRegions());
		distance.setText(String.format(getString(R.string.distance),businessInfo.getDistance()));
		address.setText(businessInfo.getAddress());
		phone.setText(businessInfo.getTeltphone());
		phone.setOnClickListener(this);
//		discountNum.setText(String.format(getString(R.string.deals_num),businessInfo.getDeals()));
		address.setOnClickListener(this);
	}

	private void initDate() {
		businessInfo=(BussinessInfoEntity) getIntent().getSerializableExtra(IntentConstant.KEY_BUSSINESS_CUR);
		dealList=businessInfo.getDealList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.phone:
			IMUIHelper.openTelPhone(BussinessDetailActivity.this, phone.getText().toString());
 			break;
		case R.id.address:
			IMUIHelper.openMapActivity(BussinessDetailActivity.this, businessInfo.getLongtitude(), businessInfo.getLatitude());
 			break;
 		case R.id.goto_:
 			//数据是使用Intent返回
            Intent intent = new Intent();
            //把返回数据存入Intent
            intent.putExtra("name", businessInfo.getName());
            intent.putExtra("address", businessInfo.getAddress());
            intent.putExtra("longitude", businessInfo.getLongtitude());
            intent.putExtra("latitude", businessInfo.getLatitude());
            //设置返回数据
            BussinessDetailActivity.this.setResult(Activity.RESULT_OK, intent);
            //关闭Activity
            BussinessDetailActivity.this.finish();

		default:
			break;
		}
	}

}
