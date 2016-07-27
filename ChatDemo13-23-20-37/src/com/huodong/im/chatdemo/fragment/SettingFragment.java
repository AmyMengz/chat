package com.huodong.im.chatdemo.fragment;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.activity.PersonalDataActivity;
import com.huodong.im.chatdemo.activity.SettingNewsActivity;
import com.huodong.im.chatdemo.adapter.DateAdapter;
import com.huodong.im.chatdemo.adapter.NearbyUserAdapter;
import com.huodong.im.chatdemo.widget.MyListView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingFragment extends BaseFragment {
	private View curView = null;
	private MyListView dateListView;
    private MyListView nearbyUserListView;
    private RelativeLayout layout_settingdata,layout_changenick,layout_settingsignature,layout_news;
    private DateAdapter dateAdapter;
    private NearbyUserAdapter nearbyUserAdapter;
    private Dialog dialog;
    private TextView tv_nickshow,tv_signature;

	private static Handler uiHandler = null;
	public static Handler getHandler() {
        return uiHandler;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initHandler();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != curView) {
            ((ViewGroup) curView.getParent()).removeView(curView);
            return curView;
        }
        curView = inflater.inflate(R.layout.fragment_setting, topContentView);
        initRes();
        init();
		return curView;
	}

	private void initRes() {
		setTopTitle(getString(R.string.setting));
		
	}
	private void init()
	{
		tv_nickshow=(TextView) curView.findViewById(R.id.seting_tvnick);
		tv_signature=(TextView) curView.findViewById(R.id.seting_tvsignature);
		layout_news=(RelativeLayout) curView.findViewById(R.id.layout_settingnews);
		//打开用户个人资料
		layout_settingdata=(RelativeLayout) curView.findViewById(R.id.layout_settingdata);
		layout_settingdata.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),PersonalDataActivity.class);
				startActivity(intent);
				
			}
		});
		//修改昵称
		layout_changenick=(RelativeLayout) curView.findViewById(R.id.layout_nick);
		layout_changenick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialognick(tv_nickshow.getText().toString());
			}
		});
		//修改签名
		layout_settingsignature=(RelativeLayout) curView.findViewById(R.id.layout_signature);
		layout_settingsignature.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialogsignature(tv_signature.getText().toString());
			}
		});
		//打开消息设置按钮
		layout_news.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),SettingNewsActivity.class);
				startActivity(intent);
			}
		});
		
	}
	private EditText et_changenick;
	private RelativeLayout bt_nickcancel,bt_nickconfirm;
	// 弹窗-修改昵称
	protected void showDialognick(String nickname) {
		// TODO Auto-generated method stub
				/**
				 * 将xml转化为View
				 */
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				View view = inflater.inflate(R.layout.dialog_nick_change, null);
				et_changenick = (EditText) view.findViewById(R.id.et_set);
				et_changenick.setText(nickname);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("修改昵称");// 设置标题
				builder.setView(view);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						tv_signature.setText(et_changenick.getText().toString());
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				builder.show();// 获取dialog
		}
	// 弹窗-修改签名
		protected void showDialogsignature(String signature) {
			// TODO Auto-generated method stub
					/**
					 * 将xml转化为View
					 */
					LayoutInflater inflater = LayoutInflater.from(getActivity());
					View view = inflater.inflate(R.layout.dialog_nick_change, null);
					et_changenick = (EditText) view.findViewById(R.id.et_set);
					et_changenick.setText(signature);
					
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle("修改签名");// 设置标题
					builder.setView(view);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							tv_signature.setText(et_changenick.getText().toString());
						}
					});
					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					builder.show();// 获取dialog
			}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void initHandler() {
		// TODO Auto-generated method stub
		
	}
}
