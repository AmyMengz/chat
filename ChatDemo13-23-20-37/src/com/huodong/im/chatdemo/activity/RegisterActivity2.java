package com.huodong.im.chatdemo.activity;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.model.UserData;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;
import com.huodong.im.config.AvatorManager;
import com.huodong.im.config.DateTimePickDialogUtil;
import com.huodong.im.config.ServeConstants;
import com.huodong.im.utils.PhotoUtil;

public class RegisterActivity2 extends BaseActivity {
	ImageView imguploading;
	TextView tvsex, tvbirthday;
	RelativeLayout rllayout_photo, rllayout_choose, rllayout_cancel;
	RelativeLayout rllayout_girl, rllayout_boy, rllayout_sexcancel;
	PopupWindow avatorPop;
	
	public String filePath = "";
	LinearLayout layout_all;
	boolean isupload=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register2);
		initviews();
	}
	
	
		
	private void initviews() {
		// TODO Auto-generated method stub
		layout_all = (LinearLayout) findViewById(R.id.layout_all);
		imguploading = (ImageView) findViewById(R.id.img_uploading);
		tvbirthday = (TextView) findViewById(R.id.tv_Birthday);

		tvsex = (TextView) findViewById(R.id.tv_sex);
		// 初始化，先把没有右侧文字点击效果的HeaderLayout装载进来
		initTopBarforRightTextno("个人资料(2/4)", "下一步");
		imguploading.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 点击了相机图片
				ShowAvatarPoptophoto();
				
			}
		});
		tvbirthday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//点击了生日
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						RegisterActivity2.this, "");
				dateTimePicKDialog.dateTimePicKDialog(tvbirthday);
				
				canclick();

			}
		});
		tvsex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 点击了性别
				ShowAvatarPoptosex();
				
					
				
				
			}
		});
	}
	
	private void ShowAvatarPoptophoto() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.pop_showavatorforphoto, null);
		rllayout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		rllayout_choose = (RelativeLayout) view
				.findViewById(R.id.layout_choose);
		rllayout_cancel = (RelativeLayout) view
				.findViewById(R.id.layout_cancel);

		rllayout_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				File dir = new File(AvatorManager.MyAvatarDir);
				if (!dir.exists())// 判断路径dir是否存在
				{
					dir.mkdirs();// 如果路径不存在，将创建其目录
				}
				// 原图
				File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date()));
				filePath = file.getAbsolutePath();
				Uri imageUri = Uri.fromFile(file);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent,
						AvatorManager.REQUESTCODE_UPLOADAVATAR_CAMERA);
				
			}
		});

		rllayout_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 点击了相册
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent,
						AvatorManager.REQUESTCODE_UPLOADAVATAR_LOCATION);
				
			}
		});
		rllayout_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// 点击了取消
				avatorPop.dismiss();
			}
		});

		avatorPop = new PopupWindow(view, mScreenWidth, 600);
		avatorPop.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {

					// 判断点击按钮外部区域，实现弹出框消失
					avatorPop.dismiss();
					return true;
				}
				return false;
			}
		});

		avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		avatorPop.setTouchable(true);
		avatorPop.setFocusable(true);
		avatorPop.setOutsideTouchable(true);
		avatorPop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从底部弹起
		avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);// 动画效果样式
		avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
	}
	Bitmap newBitmap;
	boolean isFromCamera = false;// 区分拍照旋转
	int degree = 0;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ServeConstants.REQUESTCODE_UPLOADAVATAR_CAMERA:// 拍照修改头像
			if (resultCode == RESULT_OK) {
				//判断SD卡能不能使用
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					ShowToast("SD不可用");
					return;
				}
				isFromCamera = true;
				File file = new File(filePath);
				degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
				Log.i("life", "拍照后的角度：" + degree);
				startImageAction(Uri.fromFile(file), 200, 200,
						ServeConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
			}
			break;
		case ServeConstants.REQUESTCODE_UPLOADAVATAR_LOCATION:// 本地修改头像
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			Uri uri = null;
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					ShowToast("SD不可用");
					return;
				}
				isFromCamera = false;
				uri = data.getData();
				startImageAction(uri, 200, 200,
						ServeConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
			} else {
				ShowToast("照片获取失败");
			}
			break;
		case ServeConstants.REQUESTCODE_UPLOADAVATAR_CROP:
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			if (data == null) {
				// Toast.makeText(this, "取消选择", Toast.LENGTH_SHORT).show();
				return;
			} else {
				saveCropAvator(data);
			}// 裁剪头像返回
			// 初始化文件路径
						filePath = "";
						// 上传头像
						uploadAvatar();
			break;
		}
	}
	/*
	 * 打开系统剪辑图片
	 */
	private void startImageAction(Uri uri, int outputX, int outputY,
			int requestCode, boolean isCrop) {
		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}
	/*
	 * 全局判断是否可以将下一步按钮设置为可点击状态
	 */
	public void canclick()
	{
		
		if(isupload&&tvbirthday.getText().toString()!=null&&tvbirthday.getText()!=""&&tvsex.getText()!=null&&tvsex.getText()!="")
		{
			initTopBarforRightText("个人资料(2/4)", "下一步",
					new onRighTextClickListener() {

						@Override
						public void onClick() {
							// TODO Auto-generated method stub
							savebirthday();
							startAnimActivity(RegisterActivity3.class);
							
						}
					});
		}else{
			initTopBarforRightTextno("个人资料(2/4)", "下一步");
		}
	}
	/*
	 * 保存裁剪的头像
	 */
	String path;
	private void saveCropAvator(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			Log.i("life", "avatar - bitmap = " + bitmap);
			if (bitmap != null) {
				bitmap = PhotoUtil.toRoundCorner(bitmap, 10);
				if (isFromCamera && degree != 0) {
					bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
				}
				imguploading.setImageBitmap(bitmap);
				//判断是否已经选择图片
				isupload=true;
				canclick();
				// 保存图片
				String filename = new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date())+".png";
				path = ServeConstants.MyAvatarDir + filename;
				PhotoUtil.saveBitmap(ServeConstants.MyAvatarDir, filename,
						bitmap, true);
				// 上传头像
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
	}
	private void uploadAvatar() {
		//上传图片到服务器
	}
	String sex="";
	private void ShowAvatarPoptosex() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.pop_showavatorforsex, null);
		rllayout_girl = (RelativeLayout) view.findViewById(R.id.layout_girl);
		rllayout_boy = (RelativeLayout) view.findViewById(R.id.layout_bay);
		rllayout_sexcancel = (RelativeLayout) view
				.findViewById(R.id.layout_sexcancel);

		rllayout_girl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 选择女
				tvsex.setText("女");
				sex="2";
				canclick();
				avatorPop.dismiss();
			}
		});

		rllayout_boy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 选择男
				tvsex.setText("男");
				sex="1";
				avatorPop.dismiss();
				canclick();
				
			}
		});
		rllayout_sexcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// 点击了取消
				avatorPop.dismiss();
			}
		});

		avatorPop = new PopupWindow(view, mScreenWidth, 600);
		avatorPop.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {

					// 判断点击按钮外部区域，实现弹出框消失
					avatorPop.dismiss();
					return true;
				}
				return false;
			}
		});

		avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		avatorPop.setTouchable(true);
		avatorPop.setFocusable(true);
		avatorPop.setOutsideTouchable(true);
		avatorPop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从底部弹起
		avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);// 动画效果样式
		avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
		
	}
	/*
	 * 储存生日，性别，图片文件路径等信息
	 */
	private void savebirthday()
	{
		String birthday=tvbirthday.getText().toString();
		
		birthday=birthday.trim();
		String str2="";
		
		if(birthday != null && !"".equals(birthday)){
		for(int i=0;i<birthday.length();i++){
		if(birthday.charAt(i)>=48 && birthday.charAt(i)<=57){
		str2+=birthday.charAt(i);
		}
		}
			 SharedPreferences sp = getSharedPreferences("nickname", MODE_PRIVATE);
		        Editor editor = sp.edit();
		        editor.putString("birthday", str2);
		        editor.putString("sex", sex);
		        editor.putString("photopath", path);
		        editor.commit();
		     
		
	}
	}
}
