package com.huodong.im.chatdemo.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.platform.comapi.map.w;
import com.huodong.im.chat.util.pic.ImageLoader;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.R.string;
import com.huodong.im.chatdemo.adapter.AllowedUserAdapter;
import com.huodong.im.chatdemo.adapter.GridViewAdapter;
import com.huodong.im.chatdemo.controller.DateDetailController;
import com.huodong.im.chatdemo.controller.DateDetailController.DetailDateCallBack;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.entity.SearchDateEntity;
import com.huodong.im.utils.Base64Coder;
import com.huodong.im.utils.CommonUtil;
import com.huodong.im.utils.IMDateUserHelper;
import com.huodong.im.utils.IMUIHelper;
import com.huodong.im.utils.IMUIHelper.DialogCallBack;
import com.huodong.im.utils.LoadDataFromServerNoLooper;
import com.huodong.im.utils.LoadDataFromServerNoLooper.DataCallBackNoLooper;
import com.huodong.im.utils.MD5Encoder;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo.DetailedState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DateDetailActivity extends BaseActivity implements OnClickListener{
	private int dateKey;
	private int sponsrId;
	int isapply;
	
	int myGender=1;
	private ImageView avatar;
	private TextView dateTitle,genderAge,nickName,dateAddress,distance,partnerNum,partnerType,feesType,dateTime,detailaddress,dateNotes,phoneNumber,
	verifyNum,applyNum,commentNum;
	private GridView allowedGv;
	private AllowedUserAdapter adapter;
	private static boolean isApplyed=false;//需要初始化  赋值
	private Map<String, String> map;//
	private DateDetailController controller;
	int applyCount=0;
	//审核通过的用户id数组
//	int[] allowed = null;
	List<Integer> allowed=new ArrayList<Integer>();
	
	private SearchDateEntity curDate;
	private TextView btnApplyManage,btnComment,btnShare,btnReport;
	Handler uiHandler=new Handler(){
		@Override
		public void handleMessage(Message message){
			switch (message.what) {
			//报名 成功 失败
			case IntentConstant.HANDLER_APPLY+IntentConstant.HANDLER_OK:
				ShowToast(R.string.apply_ok);
				isApplyed=true;
				btnApplyManage.setText(getString(R.string.cancel_apply));
				break;
			case IntentConstant.HANDLER_APPLY+IntentConstant.HANDLER_ERROR:
				if(message.obj!=null){
					ShowToast(R.string.apply_error_tip+(String)message.obj);
				}else {
					ShowToast(R.string.apply_error_tip);
				}
				isApplyed=false;
				btnApplyManage.setText(getString(R.string.i_want_apply));
				break;
//				撤销报名 成功 失败
			case IntentConstant.HANDLER_CANCEL_APPLY+IntentConstant.HANDLER_OK:
				ShowToast(R.string.cancel_apply_ok);
				isApplyed=false;
				btnApplyManage.setText(getString(R.string.i_want_apply));
				break;
			case IntentConstant.HANDLER_CANCEL_APPLY+IntentConstant.HANDLER_ERROR:
				if(message.obj!=null){
					ShowToast(R.string.cancel_apply_error_tip+(String)message.obj);
				}else {
					ShowToast(R.string.cancel_apply_error_tip);
				}
				isApplyed=true;
				btnApplyManage.setText(getString(R.string.cancel_apply));
				break;
				//删除约会成功 失败
			case IntentConstant.HANDLER_DELETE_DATE+IntentConstant.HANDLER_OK:
				ShowToast(R.string.delete_date_ok);
			//还要刷新约起来列表与我自己的约会列表
				Intent intent=new Intent();
				intent.putExtra(IntentConstant.RESULT_DATE_REFUSH, true);
				DateDetailActivity.this.setResult(RESULT_OK, intent);
				DateDetailActivity.this.finish();
				break;
			case IntentConstant.HANDLER_DELETE_DATE+IntentConstant.HANDLER_ERROR:
				if(message.obj!=null){
					if(((String)message.obj).equals("1")){
						ShowToast(R.string.delete_date_can_not);
					}else {
						ShowToast(R.string.delete_date_error+(String)message.obj);
					}
				}else {
					ShowToast(R.string.delete_date_error);
				}
				break;
				//有人申请过的不能删除
			case IntentConstant.HANDLER_DELETE_DATE_CAN_NOT:
				ShowToast(R.string.delete_date_can_not);
			}
		}
	};
	ImageLoader imageLoader;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_detail);
	
		controller=new DateDetailController(DateDetailActivity.this);
		initDate();
		initView();
		imageLoader = new ImageLoader(DateDetailActivity.this, true);
		/*imageLoader.DisplayImage(String.valueOf(47), avatar);*/
		imageLoader.DisplayImage(String.valueOf(sponsrId), avatar);
//		controller.getPhotoDate(sponsrId,new DetailDateCallBack() {
//			@Override
//			public void onDetailDateCallBack(int what, Object result) {
//				avatar.setImageBitmap((Bitmap)result);
//			}
//		});
	}
	
	private void initDate() {
		curDate=(SearchDateEntity) getIntent().getSerializableExtra(IntentConstant.KEY_DATE_CUR);
		dateKey=curDate.getDateKey();
		sponsrId=curDate.getSponsorID();
		isapply=curDate.getApplyState();
//		
		Log.i("Amy sponsorID isapply", "sponsrId  "+sponsrId+"  "+isapply+"uid"+uid);
		map=new HashMap<String, String>();
		map.put("dateId", String.valueOf(dateKey));
		map.put("uid", String.valueOf(uid));
		getDateDetialFromNet();
		
	}
	private void initView() {
		allowedGv=(GridView)findViewById(R.id.allowed_user_gv);
		avatar=(ImageView)findViewById(R.id.avatar);
		dateTitle=(TextView)findViewById(R.id.date_title);
		genderAge=(TextView)findViewById(R.id.sponsor_info);
		nickName=(TextView)findViewById(R.id.sponsor_nick);
		dateAddress=(TextView)findViewById(R.id.address);
		distance=(TextView)findViewById(R.id.distance);
		partnerNum=(TextView)findViewById(R.id.partner_num);
		partnerType=(TextView)findViewById(R.id.partner_type);
		
		feesType=(TextView)findViewById(R.id.fees_type);
		dateTime=(TextView)findViewById(R.id.date_time);
		detailaddress=(TextView)findViewById(R.id.detail_address);
		detailaddress.setOnClickListener(this);
		dateNotes=(TextView)findViewById(R.id.notes);
		phoneNumber=(TextView)findViewById(R.id.phone_number);
		verifyNum=(TextView)findViewById(R.id.verify_num);
		applyNum=(TextView)findViewById(R.id.apply_num);
		commentNum=(TextView)findViewById(R.id.comments_num);
		dateTitle.setText(curDate.getDateTitle());
		switch (curDate.getGender()) {
		case 1://men
			genderAge.setBackgroundResource(R.drawable.gender_boy);
			break;
		case 2://women
			genderAge.setBackgroundResource(R.drawable.gender_gril);
			break;
		}
		genderAge.setText(curDate.getBirthday());
		nickName.setText(curDate.getNickName());
		dateAddress.setText(curDate.getDateAddress());
		distance.setText(String.format(this.getString(R.string.distance_mail), IMDateUserHelper.formatDistance(curDate.getDistance())));
		partnerType.setText("("+IMDateUserHelper.formatPartner(curDate.getPartnerType())+")");
		partnerNum.setText(String.format(this.getString(R.string.limit_partner_num), curDate.getPartnerNum()));
		feesType.setText(IMDateUserHelper.formatFees(curDate.getFeesType()));
		dateTime.setText(CommonUtil.getTimeString(curDate.getDateTime()*1000,CommonUtil.timeFormat1));
		detailaddress.setText(curDate.getDetailAddress());
		dateNotes.setText(curDate.getNotes());
		applyNum.setText(String.format(this.getString(R.string.apply_num), curDate.getApply()));
		commentNum.setText(String.valueOf(curDate.getComment()));
		verifyNum.setText(String.format(getString(R.string.verify_num), 0));
		
		btnApplyManage=(TextView)findViewById(R.id.apply_manage);
		
		btnComment=(TextView)findViewById(R.id.comment);
		btnShare=(TextView)findViewById(R.id.share);
		btnReport=(TextView)findViewById(R.id.report);
		btnApplyManage.setOnClickListener(this);
		
		
		switch (isapply) {
		case 1:
			isApplyed=false;
			break;
		case 2:
			isApplyed=true;
			
			break;
		case 3:
			isApplyed=true;
			btnApplyManage.setClickable(false);
			btnApplyManage.setBackgroundColor(getResources().getColor(R.color.grey_dark));
			break;
		}
		
		if (sponsrId==uid) {
			phoneNumber.setText(getString(R.string.u_own_date));
			phoneNumber.setTextColor(getResources().getColor(R.color.color_text_grey));
		}
		adapter=new AllowedUserAdapter(DateDetailActivity.this, allowed);
		allowedGv.setAdapter(adapter);
		allowedGv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				IMUIHelper.openUserDetailActivity(DateDetailActivity.this, "", allowed.get(position),0);
			}
		});
		initTopBar();
	}

	private void initTopBar() {
//		if(sponsrId==LoginMAnager.getLoginId){
			if(sponsrId==uid){
				btnApplyManage.setText(getString(R.string.apply_manage));
				initTopBarforRightText(getString(R.string.date_detail), getString(R.string.edit_my_date), new onRighTextClickListener() {
				
				@Override
				public void onClick() {
					final AlertDialog dialog=new AlertDialog.Builder(DateDetailActivity.this).create();
					 Window window = dialog.getWindow();  
					    window.setGravity(Gravity.BOTTOM);
					    window.setWindowAnimations(R.style.bottom_dialog);
					    dialog.show(); 
					    window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					    window.setContentView(R.layout.dialog_bottom);
					    LinearLayout dialogLayout=(LinearLayout)window.findViewById(R.id.dialog);
						 TextView dialogTitle = (TextView) window.findViewById(R.id.tv_dialog_title);
						 TextView deleteDate = (TextView) window.findViewById(R.id.dialog_delete_date);
						 TextView editDateNotes = (TextView) window.findViewById(R.id.dialog_edit_notes);
						 TextView cancel = (TextView) window.findViewById(R.id.dialog_cancle);
						 dialogTitle.setText(getString(R.string.edit_date_tips)); 
						 deleteDate.setText(getString(R.string.delete_date));
						 editDateNotes.setText(getString(R.string.edit_date_notes));
						 cancel.setText(getString(R.string.cancel));
						 deleteDate.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								String url=UrlConstant.DATE_DELETE_DATE_URL;
								
								applyCount=curDate.getApply();
								Log.i("Amy applyCount>0", applyCount+"");
								if(applyCount>0){
									url=null;
								}
								controller.dateApplyManagerDelete(map, new DetailDateCallBack() {
									
									@Override
									public void onDetailDateCallBack(int what, Object result) {
										Message msg=uiHandler.obtainMessage();
										msg.what=what;
										if(result!=null)msg.obj=result;
										uiHandler.sendMessage(msg);
									}
								}, getString(R.string.sure_cancel_date), url, 
								IntentConstant.HANDLER_DELETE_DATE);
							}
						});
						 editDateNotes.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								dialog.dismiss();
								IMUIHelper.openEditNoteActivity(DateDetailActivity.this,dateKey,dateNotes.getText().toString());
								
							}
						});
						 cancel.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				}
			});
			
		}
			else {
				initTopBarForLeft(getString(R.string.date_detail));
				if(isApplyed){
					btnApplyManage.setText(getString(R.string.cancel_apply));
				}else {
					btnApplyManage.setText(getString(R.string.i_want_apply));
				}
			}
	}
//获得当前date的申请人id
	private void getDateDetialFromNet() {
		controller.getApplyInfoFromNet(map, new DetailDateCallBack() {
			
			@Override
			public void onDetailDateCallBack(int what, Object result) {
				switch (what) {
				case IntentConstant.HANDLER_GET_ALLOW_STATE+IntentConstant.HANDLER_OK:
					if(result!=null){
						JSONArray allow=(JSONArray)result;
						int len=allow.length();
						verifyNum.setText(String.format(getString(R.string.verify_num), len));
						
						allowed.clear();
						for (int i = 0; i < len; i++) {
							try {
								JSONObject u=(JSONObject) allow.get(i);
								allowed.add(u.getInt("uid"));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						adapter.notifyDataSetChanged();
					}else {
						verifyNum.setText(String.format(getString(R.string.verify_num), 0));
					}
				
					break;
				case IntentConstant.HANDLER_GET_ALLOW_STATE+IntentConstant.HANDLER_ERROR:
					
					break;

				default:
					break;
				}
				
			}
		});
		controller.getDateSponsorPhone(47, new DetailDateCallBack() {
			
			@Override
			public void onDetailDateCallBack(int what, Object result) {
				Message msg=uiHandler.obtainMessage();
				msg.what=what;
				if(result!=null)msg.obj=result;
				uiHandler.sendMessage(msg);
				switch (what) {
				case IntentConstant.HANDLER_GET_INFO+IntentConstant.HANDLER_OK:
					String phone=(String) result;
				
					if(isapply==3){
						phoneNumber.setText(phone);
						phoneNumber.setTextColor(getResources().getColor(R.color.red));
						phoneNumber.setOnClickListener(DateDetailActivity.this);
					}
					break;
				default:
					break;
				}
			}
		});
		
	}
	public Bitmap Bytes2Bimap(byte[] b) {
		        if (b.length != 0) {
		             return BitmapFactory.decodeByteArray(b, 0, b.length);
		         } else {
		            return null;
		         }
		    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.detail_address:
			
			IMUIHelper.openMapActivity(DateDetailActivity.this, curDate.getLongitude(), curDate.getLatitude());
			break;
		case R.id.apply_manage:
			if(sponsrId==uid){//管理自己的报名
				IMUIHelper.openMenageApplyActivity(DateDetailActivity.this, curDate.getDateKey(),curDate.getPartnerNum());
				
			}else {
//				Log.i(tag, msg)
				if(isApplyed){//已经申请了 要撤销
					
					controller.dateApplyManagerDelete(map, new DetailDateCallBack() {
						@Override
						public void onDetailDateCallBack(int what, Object result) {
							Message msg=uiHandler.obtainMessage();
							msg.what=what;
							if(result!=null)msg.obj=result;
							uiHandler.sendMessage(msg);
						}
					},getString(R.string.sure_cancel_apply),UrlConstant.DATE_CANCEL_APPLY_URL,IntentConstant.HANDLER_CANCEL_APPLY);
					app.setRefresh(true);
					controller.send(sponsrId,String.format(getString(R.string.request), String.valueOf(uid)));
				}
				else {//没有申请 点击申请
					//要用本子存的个人信息  curDate.getPartnerType()
					boolean isgender=IMDateUserHelper.islimitPartner(curDate.getPartnerType(), myGender);
					if(isgender){
						controller.dateApplyManagerDelete(map, new DetailDateCallBack() {
							@Override
							public void onDetailDateCallBack(int what, Object result) {
								Message msg=uiHandler.obtainMessage();
								msg.what=what;
								if(result!=null)msg.obj=result;
								uiHandler.sendMessage(msg);
							}
						},getString(R.string.sure_apply_date),UrlConstant.DATE_APPLY_URL,IntentConstant.HANDLER_APPLY);
					}
					else {
						ShowToast(getString(R.string.your_gender_no));
					}
					app.setRefresh(true);
					controller.send(sponsrId,String.format(getString(R.string.un_request), String.valueOf(uid)));
				}
			}
			break;
		case R.id.phone_number:
			IMUIHelper.openTelPhone(DateDetailActivity.this, phoneNumber.getText().toString());
			break;
		default:
			break;
		}
		
	}
	boolean isNotesChange=false;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
//		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentConstant.REQUESTCODE_EDIT_NOTE:
			if(resultCode==RESULT_OK){
				String newnotes=data.getExtras().getString(IntentConstant.RESULT_DATE_NOTES);
				dateNotes.setText(newnotes);
				isNotesChange=true;
			}
			break;

		default:
			break;
		}
	}
	@Override 
	public void onBackPressed() { 
		if(isNotesChange){//如果notes更改过要刷新
			Intent intent=new Intent();
			intent.putExtra(IntentConstant.RESULT_DATE_REFUSH, true);
			DateDetailActivity.this.setResult(RESULT_OK, intent);
			DateDetailActivity.this.finish();
		}
		else {
			Intent intent=new Intent();
			intent.putExtra(IntentConstant.RESULT_DATE_REFUSH, false);
			DateDetailActivity.this.setResult(RESULT_OK, intent);
			DateDetailActivity.this.finish();
			DateDetailActivity.this.finish();
		}
	} 
	
	
	

}
