package com.huodong.im.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.huodong.im.chatdemo.activity.DateDetailActivity;
import com.huodong.im.config.UrlConstant;
import com.huodong.im.utils.LoadDataFromServerNoLooper.DataCallBackNoLooper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;

public class ImageDownLoader {
	/**
	 * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
	 */
	private LruCache<String,Bitmap> mMemoryCache;
	/**
	 * 操作文件相关类对象的引用
	 */
	private FileUtils fileUtils;
	private Context contex;
	
	public ImageDownLoader(Context context){
		this.contex=context;
		//获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
		int maxMemory=(int)Runtime.getRuntime().maxMemory();
		int mCacheSize=maxMemory/8;
		 //给LruCache分配1/8 4M
		mMemoryCache=new LruCache<String, Bitmap>(mCacheSize){
			////必须重写此方法，来测量Bitmap的大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return value.getRowBytes()*value.getHeight();
			}
		};
		fileUtils = new FileUtils(context);
	}
	/**
	 * 添加Bitmap到内存缓存
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {  
		android.util.Log.i("Amy", " "+key+"    bitmap==="+bitmap);
		if(getBitmapFromMemCache(key)==null&&bitmap!=null){
			mMemoryCache.put(key, bitmap);
		}
	}
	/**
	 * 从内存缓存中获取一个Bitmap
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapFromMemCache(String key) {  
	    return mMemoryCache.get(key);  
	} 
	/**
	 * 先从内存缓存中获取Bitmap,如果没有就从SD卡或者手机缓存中获取，SD卡或者手机缓存
	 * 没有就去下载
	 * @param url
	 * @param listener
	 * @return
	 */
	public Bitmap downloadImage(final String url, final onImageLoaderListener listener){
		//替换Url中非字母和非数字的字符，这里比较重要，因为我们用Url作为文件名，比如我们的Url
		//是Http://xiaanming/abc.jpg;用这个作为图片名称，系统会认为xiaanming为一个目录，
		//我们没有创建此目录保存文件就会报错
		final String subURL=url.replaceAll("[^\\w]", "");
		Bitmap bitmap=showCacheBitmap(subURL);
		android.util.Log.i("Amy setting", "bitmap==subURL="+subURL+"     "+bitmap);
		if(bitmap!=null){
			return bitmap;
		}
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg){
				listener.onImageLoader((Bitmap)msg.obj, url);
			}
		};
		new Thread(new Runnable() {
			
			public void run() {
				Bitmap bitmap=getBitmapFormUrl(url);
				if(bitmap==null){
					return;
				}
				Message msg=handler.obtainMessage();
				msg.obj=bitmap;
				handler.sendMessage(msg);
				try {
					//保存在SD卡或者手机目录
					fileUtils.savaBitmap(subURL, bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//将Bitmap 加入内存缓存
				addBitmapToMemoryCache(subURL, bitmap);
			}
		}).start();
		return null;
		
	}
	public Bitmap getBitmapFormUrl(String url){
		Bitmap bitmap = null;
		
		try {
			HttpURLConnection con = null;
			Log.i("Amy setting", "url=="+url);
			URL mUrl=new URL(url);
			
			con=(HttpURLConnection)mUrl.openConnection();
			con.setConnectTimeout(5*1000);
			con.setReadTimeout(5*1000);
			con.setDoInput(true);
			con.setDoOutput(true);Log.i("Amy setting", "getResponseCode------"+con.getResponseCode());
			Log.i("Amy setting", "con=="+con.getInputStream());
			bitmap=BitmapFactory.decodeStream(con.getInputStream());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
		
	}
	Bitmap bitmap = null;
	/*public Bitmap getBitmapFormUrlByPhone(final String phone,int i){
		
		Map<String, String> map=new HashMap<String, String>();
		map.put("id", String.valueOf(47));
		try {
//			map.put("fileName",MD5Encoder.encode(phone+"photofilename1"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoadDataFromServerNoLooper load=new LoadDataFromServerNoLooper(contex, UrlConstant.IMAGE_LOAD_URL, map);
		load.getData(new DataCallBackNoLooper() {
			
			@Override
			public void onDataCallBack(String result) {
				if(!TextUtils.isEmpty(result)){
					try {
						JSONObject info = new JSONObject(result);
						String data = info.getString("data");
						byte[] b2 = Base64Coder.decodeLines(data);
//						Bitmap b=Bytes2Bimap(b2);
						File file = contex.getCacheDir();

						if (!file.exists()) {
							file.mkdirs();
						}
						File file2 = new File(file, phone+"photofilename1");
						if (!file2.exists()) {
							file2.createNewFile();
						}
						FileOutputStream fos = new FileOutputStream(file2);
						fos.write(b2);
						fos.flush();
						fos.close();
						FileInputStream in = new FileInputStream(file2);
						bitmap = BitmapFactory.decodeStream(in);
//						avatar.setImageBitmap(bm)
//						avatar.setImageBitmap(bitmap);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		return bitmap;
	}*/
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Bitmap getBitmapFormByid(final int id){
		
		Map<String, String> map=new HashMap<String, String>();
		map.put("id", String.valueOf(id));
		LoadDataFromServerNoLooper load=new LoadDataFromServerNoLooper(contex, UrlConstant.IMAGE_LOAD_URL, map);
		load.getData(new DataCallBackNoLooper() {
			
			@Override
			public void onDataCallBack(String result) {
				if(!TextUtils.isEmpty(result)){
					try {
						JSONObject info = new JSONObject(result);
						String data = info.getString("data");
						byte[] b2 = Base64Coder.decodeLines(data);
//						Bitmap b=Bytes2Bimap(b2);
						File file = contex.getCacheDir();

						if (!file.exists()) {
							file.mkdirs();
						}
						File file2 = new File(file, id+"photofilename1");
						if (!file2.exists()) {
							file2.createNewFile();
						}
						FileOutputStream fos = new FileOutputStream(file2);
						fos.write(b2);
						fos.flush();
						fos.close();
						FileInputStream in = new FileInputStream(file2);
						bitmap = BitmapFactory.decodeStream(in);
//						avatar.setImageBitmap(bm)
//						avatar.setImageBitmap(bitmap);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		return bitmap;
		
	}
	/**
	 * 获取Bitmap, 内存中没有就去手机或者sd卡中获取，这一步在getView中会调用，比较关键的一步
	 * @param url
	 * @return
	 */
	public Bitmap showCacheBitmap(String url){
		android.util.Log.i("Amy", "getBitmapFromMemCache(url)!=null===="+(getBitmapFromMemCache(url)!=null));
		android.util.Log.i("Amy", "fileUtils.isFileExistsl===="+(fileUtils.isFileExists(url)&&fileUtils.getFileSize(url)!=0));
		if(getBitmapFromMemCache(url)!=null){
			return getBitmapFromMemCache(url);
		}else if (fileUtils.isFileExists(url)&&fileUtils.getFileSize(url)!=0) {
			Bitmap bitmap=fileUtils.getBitmap(url);
			addBitmapToMemoryCache(url, bitmap);
			return bitmap;
		}
		else {
			return null;
		}
	}
	/**
	 * 异步下载图片的回调接口
	 * @author len
	 *
	 */
	public interface onImageLoaderListener{
		void onImageLoader(Bitmap bitmap, String url);
	}
	
}

