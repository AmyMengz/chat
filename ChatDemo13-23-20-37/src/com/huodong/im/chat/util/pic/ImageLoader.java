package com.huodong.im.chat.util.pic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.huodong.im.chatdemo.R;
import com.huodong.im.utils.CommonUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

public class ImageLoader {
	MemoryCacheUtils memoryCacheUtils = new MemoryCacheUtils();
	FileCache fileCache;
	Context mContext;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;

	public ImageLoader(Context context) {
		mContext = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	boolean mIsSetUserPortrait = false;

	public ImageLoader(Context context, boolean mIsSetUserPortrait) {
		mContext = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
		this.mIsSetUserPortrait = mIsSetUserPortrait;
	}

	public void DisplayImage(String url, ImageView imageView) {
		imageView.setTag(String.valueOf(url));
//		imageView.setImageResource(R.drawable.default_face);
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCacheUtils.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			queuePhoto(url, imageView);
		}
	}

	public void DisplayNewImage(String name, ImageView imageView) {
		imageViews.put(imageView, name);
		queuePhoto(name, imageView);
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}
public static final String URL_PORTAINT = "http://120.24.2.49:8787/yj2/servlet/ImageDownServer";
	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		if (mIsSetUserPortrait) {
			HttpClient client = new DefaultHttpClient();
			// �����ϴ�����
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("id", url));
			HttpPost post = new HttpPost(URL_PORTAINT);
			UrlEncodedFormEntity entity;
			try {
				entity = new UrlEncodedFormEntity(formparams, "UTF-8");
				post.setEntity(entity);
				HttpResponse response = client.execute(post);
				HttpEntity e = response.getEntity();
				int code = response.getStatusLine().getStatusCode();
				Log.i("RESULT", "*******  code*"+code);
				if (200 == code) {
					
					String result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
					Log.i("RESULT", "********"+result);
					if (!TextUtils.isEmpty(result)) {
						JSONObject json = new JSONObject(result);
						boolean flag = json.getBoolean("flag");
						if (flag) {
							String data = json.getString("data");
							byte[] b2 = Base64Coder.decodeLines(data);
							File file = mContext.getCacheDir();

							if (!file.exists()) {
								file.mkdirs();
							}
							File file2 = new File(file, url);
							if (!file2.exists()) {
								file2.createNewFile();
							}
							FileOutputStream fos = new FileOutputStream(file2);
							fos.write(b2);
							fos.flush();
							fos.close();
							FileInputStream in = new FileInputStream(file2);
							final Bitmap bitmap = BitmapFactory.decodeStream(in);
							LocalExternalUtil.setBitmapToLocak(bitmap, f);
							return bitmap;
						}
					}
				}
				client.getConnectionManager().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		} else {
			// ����ָ����url������ͼƬ
			try {
				Bitmap bitmap = null;
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				// �Ƿ�������ѭ�ض���
				conn.setInstanceFollowRedirects(true);
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				CopyStream(is, os);
				os.close();
				bitmap = decodeFile(f);
				return bitmap;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}

	private Bitmap decodeFile(File f) {
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
			float screenWidth = display.getWidth();
			float screenHeight = display.getHeight();
			final int REQUIRED_SIZE = (int) screenWidth;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			bmp=CommonUtil.getRoundCornerBitmap(bmp, 40.0f);
			memoryCacheUtils.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				photoToLoad.imageView.setImageBitmap(bitmap);
			}

		}
	}


	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
}
