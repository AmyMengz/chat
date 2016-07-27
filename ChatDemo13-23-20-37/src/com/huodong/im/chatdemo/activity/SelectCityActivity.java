package com.huodong.im.chatdemo.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.huodong.im.chatdemo.R;
import com.huodong.im.chatdemo.adapter.CityAdapter;
import com.huodong.im.chatdemo.data.CityData;
import com.huodong.im.chatdemo.view.HeaderLayout.onRighTextClickListener;
import com.huodong.im.chatdemo.widget.city.ContactItemInterface;
import com.huodong.im.chatdemo.widget.city.ContactListViewImpl;
import com.huodong.im.utils.CityItem;
import com.huodong.im.utils.IMUIHelper;

public class SelectCityActivity extends BaseActivity implements TextWatcher{
	private Context context_ =SelectCityActivity.this;
	private ContactListViewImpl listview;

	private EditText searchBox;
	private ImageButton clearSearch;
	private String searchString;

	private Object searchLock = new Object();
	boolean inSearchMode = false;
	List<ContactItemInterface> contactList;
	List<ContactItemInterface> filterList;
	private SearchListTask curSearchTask = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_city);
//		initDate();
		initView();
		
		
	}
	private void initView() {
		initTopBarForLeft(getString(R.string.chose_city));
		filterList = new ArrayList<ContactItemInterface>();
		contactList = CityData.getSampleContactList();

		CityAdapter adapter = new CityAdapter(this,R.layout.item_city, contactList);

		listview = (ContactListViewImpl) this.findViewById(R.id.listview);
		listview.setFastScrollEnabled(true);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView parent, View v, int position,
					long id)
			{
				List<ContactItemInterface> searchList = inSearchMode ? filterList
						: contactList;
				ShowToast(searchList.get(position).getDisplayInfo());
				
				//数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("result", searchList.get(position).getDisplayInfo());
                //设置返回数据
                SelectCityActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                SelectCityActivity.this.finish();
			}
		});

		searchBox = (EditText) findViewById(R.id.query);
		searchBox.addTextChangedListener(this);
		clearSearch=(ImageButton)findViewById(R.id.search_clear);
		clearSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchBox.getText().clear();
				
			}
		});
	}
	private class SearchListTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			filterList.clear();

			String keyword = params[0];

			inSearchMode = (keyword.length() > 0);

			if (inSearchMode)
			{
				// get all the items matching this
				for (ContactItemInterface item : contactList)
				{
					CityItem contact = (CityItem) item;

					boolean isPinyin = contact.getFullName().toUpperCase().indexOf(keyword) > -1;
					boolean isChinese = contact.getNickName().indexOf(keyword) > -1;

					if (isPinyin || isChinese)
					{
						filterList.add(item);
					}

				}

			}
			return null;
		}

		protected void onPostExecute(String result)
		{

			synchronized (searchLock)
			{

				if (inSearchMode)
				{

					CityAdapter adapter = new CityAdapter(context_,R.layout.item_city, filterList);
					adapter.setInSearchMode(true);
					listview.setInSearchMode(true);
					listview.setAdapter(adapter);
				} else
				{
					CityAdapter adapter = new CityAdapter(context_,R.layout.item_city, contactList);
					adapter.setInSearchMode(false);
					listview.setInSearchMode(false);
					listview.setAdapter(adapter);
				}
			}

		}
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() > 0) {
			clearSearch.setVisibility(View.VISIBLE);
		} else {
			clearSearch.setVisibility(View.INVISIBLE);
		}
	}
	@Override
	public void afterTextChanged(Editable s) {
		searchString = searchBox.getText().toString().trim().toUpperCase();

		if (curSearchTask != null
				&& curSearchTask.getStatus() != AsyncTask.Status.FINISHED)
		{
			try
			{
				curSearchTask.cancel(true);
			} catch (Exception e)
			{
				Log.i(TAG, "Fail to cancel running search task");
			}

		}
		curSearchTask = new SearchListTask();
		curSearchTask.execute(searchString); 
	}


}
