package com.huodong.im.utils;

import java.util.Map;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.BaiduMapActivity;
import com.huodong.im.chatdemo.activity.BussinessDetailActivity;
import com.huodong.im.chatdemo.activity.DateDetailActivity;
import com.huodong.im.chatdemo.activity.DetailNearbyActivity;
import com.huodong.im.chatdemo.activity.EditNoteActivity;
import com.huodong.im.chatdemo.activity.HisDateActivity;
import com.huodong.im.chatdemo.activity.ManageApplyActivity;
import com.huodong.im.chatdemo.activity.SelectAddressActivityDetail;
import com.huodong.im.chatdemo.activity.SelectCityActivity;
import com.huodong.im.chatdemo.activity.SelectionActivity;
import com.huodong.im.chatdemo.activity.SelectAddressActivity;
import com.huodong.im.chatdemo.adapter.BusinessAdapter;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.entity.BussinessInfoEntity;
import com.huodong.im.entity.SearchDateEntity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class IMUIHelper {
	//跳转到筛选页面
    public static void openSelectionActivity(Context ctx,int currentIndex) {
        Intent intent = new Intent(ctx, SelectionActivity.class);
        intent.putExtra(IntentConstant.KEY_SELECTION_INDEX, currentIndex);
        ((Activity) ctx).startActivityForResult(intent, IntentConstant.REQUESTCODE_SELECTION);
    }
/*//    约会详情
    public static void openDateDetailActivity(Context ctx,int currentDateID,int sponsorID) {
        Intent intent = new Intent(ctx, DateDetailActivity.class);
        intent.putExtra(IntentConstant.KEY_DATE_KEY, currentDateID);//dateKey
        intent.putExtra(IntentConstant.KEY_SPONSORID, sponsorID);//sponsorID
        ctx.startActivity(intent);
    }*/
//  约会详情
  public static void openDateDetailActivity(Context ctx,SearchDateEntity currentDateEntity) {
      Intent intent = new Intent(ctx, DateDetailActivity.class);
      Bundle bundle = new Bundle();
      bundle.putSerializable(IntentConstant.KEY_DATE_CUR, currentDateEntity);
      intent.putExtras(bundle);
//      intent.putExtra(IntentConstant.KEY_DATE_CUR, currentDateEntity);
      ((Activity) ctx).startActivityForResult(intent,IntentConstant.REQUESTCODE_DATE_DETAIL);
  }
    //编辑约会说明
    public static void openEditNoteActivity(Context ctx,int currentDateID,String notes) {
        Intent intent = new Intent(ctx, EditNoteActivity.class);
        intent.putExtra(IntentConstant.KEY_DATE_KEY, currentDateID);//dateKey
        intent.putExtra(IntentConstant.KEY_DATE_NOTES, notes);//dateKey
        ((Activity) ctx).startActivityForResult(intent,IntentConstant.REQUESTCODE_EDIT_NOTE);
    }
    //选择约会地点
    public static void openSeleteAddressActivity(Context ctx,String currentDateID) {
        Intent intent = new Intent(ctx, SelectAddressActivity.class);
        intent.putExtra(IntentConstant.KEY_DATE_KEY, currentDateID);//dateKey
//        intent.putExtra(IntentConstant.KEY_LONGITUDE, log);
//        intent.putExtra(IntentConstant.KEY_LATITUDE, lat);
        ((Activity) ctx).startActivityForResult(intent, IntentConstant.REQUESTCODE_ADDRESS);
    }
  /*//选择约会地点
    public static void openSeleteAddressDetailActivity(Context ctx,String currentDateID) {
        Intent intent = new Intent(ctx, SelectAddressActivityDetail.class);
        intent.putExtra(IntentConstant.KEY_DATE_KEY, currentDateID);//dateKey
//        intent.putExtra(IntentConstant.KEY_LONGITUDE, log);
//        intent.putExtra(IntentConstant.KEY_LATITUDE, lat);
        ctx.startActivity(intent);
    }*/
  //选择约会地点
    public static void openSeleteAddressDetailActivity(Context ctx,Map<String, String> map) {
        Intent intent = new Intent(ctx, SelectAddressActivityDetail.class);
        for(Map.Entry<String, String> entry:map.entrySet()){
        	intent.putExtra(entry.getKey(), entry.getValue());
        }
        ((Activity) ctx).startActivityForResult(intent, IntentConstant.REQUESTCODE_ADDRESS_DETAIL);
    }
  //选择约会城市
    public static void openSeleteAddressCityActivityForResult(Context ctx,String currentDateID) {
        Intent intent = new Intent(ctx, SelectCityActivity.class);
        intent.putExtra(IntentConstant.KEY_DATE_KEY, currentDateID);//dateKey
//        intent.putExtra(IntentConstant.KEY_LONGITUDE, log);
//        intent.putExtra(IntentConstant.KEY_LATITUDE, lat);
        ((Activity) ctx).startActivityForResult(intent, IntentConstant.REQUESTCODE_CITY);
    }
//  报名管理
    public static void openMenageApplyActivity(Context ctx,int dateid,int num) {
      Intent intent = new Intent(ctx, ManageApplyActivity.class);
      Bundle bundle = new Bundle();
      intent.putExtra(IntentConstant.KEY_DATE_KEY, dateid);
      intent.putExtra(IntentConstant.KEY_PARTNER_NUM, num);
      ctx.startActivity(intent);
  }
    //    附近的人详情
    public static void openUserDetailActivity(Context ctx,String nickname,int uid,int fans) {
        Intent intent = new Intent(ctx, DetailNearbyActivity.class);
        intent.putExtra(IntentConstant.KEY_UID, uid);//dateKey
        intent.putExtra(IntentConstant.KEY_NICK_NAME, nickname);//sponsorID
        intent.putExtra(IntentConstant.KEY_FANS, fans);//sponsorID
        ctx.startActivity(intent);
    }
//  附近的人正在发起的约会
  public static void openHisDateActivity(Context ctx,int uid) {
      Intent intent = new Intent(ctx, HisDateActivity.class);
      intent.putExtra(IntentConstant.KEY_UID, uid);//dateKey
//      intent.putExtra(IntentConstant.KEY_NICK_NAME, nickname);//sponsorID
      ctx.startActivity(intent);
  }
//地点选择详情
  public static void openBussinessDetailActivity(Context ctx,BussinessInfoEntity currentBussinessEntity) {
    Intent intent = new Intent(ctx, BussinessDetailActivity.class);
    Bundle bundle = new Bundle();
    bundle.putSerializable(IntentConstant.KEY_BUSSINESS_CUR, currentBussinessEntity);
    intent.putExtras(bundle);
//    intent.putExtra(IntentConstant.KEY_DATE_CUR, currentDateEntity);
    ((Activity) ctx).startActivityForResult(intent,IntentConstant.REQUESTCODE_BUSSINESS_DETAIL);
}
//拨打电话
  public static void openTelPhone(Context ctx,String number) {
	  Intent intent=new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:"+number));  
		ctx.startActivity(intent);
}
//打开map
  public static void openMapActivity(Context ctx,double longitude,double latitude) {
	  Intent intent = new Intent(ctx, BaiduMapActivity.class);
      intent.putExtra(IntentConstant.KEY_LONGITUDE, longitude);
      intent.putExtra(IntentConstant.KEY_LATITUDE, latitude);
      ctx.startActivity(intent);
}
    
    
    
    /**
     * 显示带scrollView的listview 需要重新测量  这里应该用反射
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {   
    	
        // 获取ListView对应的Adapter   
        BaseAdapter listAdapter = (BaseAdapter) listView.getAdapter();  
        if(listAdapter == null) {   
            return;   
        }   
   
        int totalHeight = 0;   
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
            // listAdapter.getCount()返回数据项的数目   
            View listItem = listAdapter.getView(i, null, listView);   
            // 计算子项View 的宽高   
            listItem.measure(0, 0);    
            // 统计所有子项的总高度   
            totalHeight += listItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
        // listView.getDividerHeight()获取子项间分隔符占用的高度   
        // params.height最后得到整个ListView完整显示需要的高度   
        listView.setLayoutParams(params);   
    }  
    /**
     * title ok cacle 的中间显示的dialog
     * @param context
     * @param message
     * @param title_
     * @param dialogCallback
     */
    public static void showDialog(Context context,String title_,DialogCallBack dialogCallback){
    	final AlertDialog dialog=new AlertDialog.Builder(context).create();
		Window window = dialog.getWindow();
		dialog.show();
		window.setContentView(R.layout.dialog_center);
		TextView title=(TextView)window.findViewById(R.id.dialog_title_c);
		TextView cancel=(TextView)window.findViewById(R.id.cancel);
		TextView ok=(TextView)window.findViewById(R.id.ok);
		title.setText(title_);
		cancel.setText(context.getString(R.string.cancel));
		ok.setText(context.getString(R.string.ok));
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		if(dialogCallback!=null){
			dialogCallback.onDialogCallBack(ok,dialog);
		}
    }
    public interface DialogCallBack{
    	void onDialogCallBack(TextView ok,AlertDialog dialog);
    }
    

}
