package com.huodong.im.chat.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public class HttpUtil {

	public void post(List<NameValuePair> params, String url) {
		new PostAsyncTask().execute(params, url);
	}

	public void post(List<NameValuePair> params, String url, Long time) {
		new PostAsyncTask().execute(params, url, time);
	}

	public void get(String url) {
		new GetAsyncTask().execute(url);
	}

	public void get(String url, Long time) {
		new GetAsyncTask().execute(url, time);
	}

	class PostAsyncTask extends AsyncTask<Object, Object, String> {

		@Override
		protected String doInBackground(Object... params) {
			DefaultHttpClient client = new DefaultHttpClient();
			List<NameValuePair> paramsList = (List<NameValuePair>) params[0];
//			for (NameValuePair nameValuePair : paramsList) {
//				String name = nameValuePair.getName();
//				String value = nameValuePair.getValue();
////				Log.v(IMConstants.TAG_YJ, "name/value:"+name+"/"+value);
//			}
			String url = (String) params[1];
			int length = params.length;
			if (length > 2) {
				Long time = (Long) params[2];
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			HttpPost httpPost = new HttpPost(url);
//			Log.v(IMConstants.TAG_YJ, "url:"+url);
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
				HttpResponse response = client.execute(httpPost);
				int code = response.getStatusLine().getStatusCode();
//				Log.v(IMConstants.TAG_YJ, "code:"+code);
				if (code == 200) {
					String result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
					return result;
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
//				Log.v(IMConstants.TAG_YJ, "UnsupportedEncodingException:"+e.toString());
			} catch (ClientProtocolException e) {
				e.printStackTrace();
//				Log.v(IMConstants.TAG_YJ, "ClientProtocolException:"+e.toString());
			} catch (IOException e) {
				e.printStackTrace();
//				Log.v(IMConstants.TAG_YJ, "IOException:"+e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			listner.result(result);
		}

	}

	class GetAsyncTask extends AsyncTask<Object, Object, String> {

		@Override
		protected String doInBackground(Object... params) {
			DefaultHttpClient client = new DefaultHttpClient();
			String url = (String) params[0];
			int length = params.length;
			if (length > 1) {
				Long time = (Long) params[1];
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			HttpGet httpGet = new HttpGet(url);
			try {
				HttpResponse response = client.execute(httpGet);
				int code = response.getStatusLine().getStatusCode();
				if (code == 200) {
					String result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
					return result;
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			listner.result(result);
		}

	}

	private ResponseListner listner;

	public void setResponseListner(ResponseListner listner) {
		this.listner = listner;
	}

	public interface ResponseListner {
		void result(String result);
	};

}
