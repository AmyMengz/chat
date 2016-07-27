package com.huodong.im.chatdemo.fragment;

import com.huodong.im.chatdemo.activity.MyApplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

public class BaseTabFragment extends Fragment{
	public int uid;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication app = (MyApplication) getActivity().getApplication();
		uid=app.getUid();
	}
	Toast mToast;
	public void ShowToast(final String text)
	{
		if(!TextUtils.isEmpty(text))
		{
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(mToast==null)
					{
						mToast=Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT);
					}else{
						mToast.setText(text);
					}
					mToast.show();
				}
			});
		}
	}

}
