package com.huodong.im.chat.util.pic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class LocalExternalUtil {

	private static String getCachePath(Context context) {
		String cache_path = context.getExternalFilesDir(context.getPackageName()).getAbsolutePath();
		return cache_path;
	}

	public static void setBitmapToLocak(Bitmap bitmap, File file) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {// 如果文件夹不存在, 创建文件�?
				parentFile.mkdirs();
			}
			// 将图片保存在本地
			bitmap.compress(CompressFormat.PNG, 100, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static File setBitmapToLocak(Context context, Bitmap bitmap, String fileName) {
		FileOutputStream fos;
		try {
			File file = getFile(context, fileName);
			fos = new FileOutputStream(file);
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) {// 如果文件夹不存在, 创建文件�?
				parentFile.mkdirs();
			}
			// 将图片保存在本地
			bitmap.compress(CompressFormat.PNG, 100, fos);
			return file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getUrl(Context context, String fileName) {
		String cachePath = getCachePath(context);
		File file = new File(cachePath, String.valueOf(fileName.hashCode()));
		String absolutePath = file.getAbsolutePath();
		return absolutePath;
	}

	public static File getFile(Context context, String fileName) {
		String cachePath = getCachePath(context);
		File file = new File(cachePath, String.valueOf(fileName.hashCode()));
		return file;
	}

}
