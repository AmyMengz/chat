package com.huodong.im.chat.util.pic;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存工具�?
 * 
 * @author Kevin
 * 
 */
public class MemoryCacheUtils {

	private LruCache<String, Bitmap> mMemCache;

	public MemoryCacheUtils() {
		// 获取到可用内存的�?��值，使用内存超出这个值会引起OutOfMemory异常�?
		long maxMemory = Runtime.getRuntime().maxMemory();// 模拟器默认是16M内存
		// LruCache通过构�?函数传入缓存值，以KB为单位�?
		// 使用�?��可用内存值的1/8作为缓存的大小�?
		mMemCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();// 返回图片大小
			}
		};
	}

	/**
	 * 从内存中取图�?
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap get(String url) {
		return mMemCache.get(url);
	}

	/**
	 * 向内存中存图�?
	 * 
	 * @param url
	 * @param bitmap
	 */
	public void put(String url, Bitmap bitmap) {
		mMemCache.put(url, bitmap);
	}

}
