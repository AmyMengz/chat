package com.huodong.im.chatdemo.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.platform.comapi.map.w;
import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;
import com.huodong.im.config.IntentConstant;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.utils.IMUIHelper;
import com.huodong.im.utils.IMUIHelper.DialogCallBack;
import com.huodong.im.utils.LoadDataFromServerNoLooper;
import com.huodong.im.utils.LoadDataFromServerNoLooper.DataCallBackNoLooper;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.NetworkInfo.DetailedState;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditNoteActivity extends BaseActivity {
	private int dateKey;
	private EditText newNotes;
	private String notes;
	private TextView text_len;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_note);
		initDate();
		initView();
//		如果
	}

	private void initDate() {
		
		dateKey=getIntent().getIntExtra(IntentConstant.KEY_DATE_KEY, 0);
		notes=getIntent().getStringExtra(IntentConstant.KEY_DATE_NOTES);
//		int sponsrId=getIntent().getIntExtra(IntentConstant.KEY_SPONSORID,0);
		
	}

	private void initView() {
		text_len=(TextView)findViewById(R.id.text_len);
		newNotes=(EditText)findViewById(R.id.notes);
		newNotes.setText(notes);
		text_len.setText(String.format(getString(R.string.edit_note_len), newNotes.getText().length()));
		newNotes.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				text_len.setText(String.format(getString(R.string.edit_note_len), s.length()));
			}
		});
		initTopBarforRightText(getString(R.string.edit_my_date_note_tip), getString(R.string.save), new onRighTextClickListener() {
			
			@Override
			public void onClick() {
				notes=newNotes.getText().toString();
				int newLen=notes.length();
				if(newLen>150){
					showDialogTooLong();
				}else {
					Map<String, String> map=new HashMap<String, String>();
					map.put("dateId", String.valueOf(dateKey));
					map.put("notes", notes);
					LoadDataFromServerNoLooper load=new LoadDataFromServerNoLooper(EditNoteActivity.this, UrlConstant.UPDATE_DATE_NOTE_URL, map);
					load.getData(new DataCallBackNoLooper() {
						
						@Override
						public void onDataCallBack(String result) {
							if(result!=null){
								try {
									JSONObject info=new JSONObject(result);
									boolean flag=info.getBoolean("flag");
									if(flag){
										showDialog();
									}
									else {
										String err=info.getString("error_infos");
										ShowToast(getString(R.string.change_notes_reeor)+err);
									}
								} catch (JSONException e) {
									ShowToast(getString(R.string.change_notes_reeor)+e);
									e.printStackTrace();
								}
							}
							else {
								ShowToast(getString(R.string.change_notes_reeor));
							}
						}
					});
				}
			}
		});		

	}
	public void showDialog(){
		IMUIHelper.showDialog(EditNoteActivity.this, getString(R.string.chagne_note_ok), new DialogCallBack() {
			
			@Override
			public void onDialogCallBack(TextView ok, final AlertDialog dialog) {
				ok.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						Intent intent=new Intent();
						intent.putExtra(IntentConstant.RESULT_DATE_NOTES, notes);
						EditNoteActivity.this.setResult(RESULT_OK, intent);
						EditNoteActivity.this.finish();
					}
				});
			}
		});
	}
	public void showDialogTooLong(){
		IMUIHelper.showDialog(EditNoteActivity.this, getString(R.string.chagne_note_too_long), new DialogCallBack() {
			
			@Override
			public void onDialogCallBack(TextView ok, final AlertDialog dialog) {
				ok.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						
					}
				});
			}
		});
	}
	

}
