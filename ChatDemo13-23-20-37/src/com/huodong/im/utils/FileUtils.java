package com.huodong.im.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

public class FileUtils {
	//SD卡根目录
	private static String mSdRootPath=CommonUtil.ROOTPATH;
	/**
	 * 手机的缓存根目录
	 */
	private static String mDataRootPath = null;
	/**
	 * 保存Image的目录名
	 */
	private final static String FOLDER_NAME = CommonUtil.SAVE_IMAGE_CACHE_PATH;
	public FileUtils(Context context){
		mDataRootPath=context.getCacheDir().getPath();
		removeCache(getStorageDirectory());
		Log.i("Amy", "mDataRootPath---"+mDataRootPath);
	}
	/**
	 * 获取储存Image的目录
	 * @return
	 */
	private String getStorageDirectory(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
	}
	/**
	 * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
	 * @param fileName 
	 * @param bitmap   
	 * @throws IOException
	 */
	public void savaBitmap(String fileName, Bitmap bitmap) throws IOException{
		 if(bitmap==null){
			 return;
		 }
		 String path=getStorageDirectory();
		 File filefloder=new File(path);
		 if(!filefloder.exists()){
			 filefloder.mkdirs();
		 }
		 File file=new File(path+File.separator+fileName);
	     file.createNewFile();
	     FileOutputStream fos=new FileOutputStream(file);
	     bitmap.compress(CompressFormat.JPEG, 100, fos);
	     fos.flush();
	     fos.close();
	}
	public void savaBytes(String fileName, byte[] buffer) throws IOException{
		 if(buffer==null){
			 return;
		 }
		 String path=getStorageDirectory();
		 File filefloder=new File(path);
		 if(!filefloder.exists()){
			 filefloder.mkdirs();
		 }
		 File file=new File(path+File.separator+fileName);
	     file.createNewFile();
	     FileOutputStream fos=new FileOutputStream(file);
	     fos.write(buffer);
	     fos.flush();
	     fos.close();
	}
	/**
	 * 从手机或者sd卡获取Bitmap
	 * @param fileName
	 * @return
	 */
	public Bitmap getBitmap(String fileName){
		return BitmapFactory.decodeFile(getStorageDirectory()+File.separator+fileName);
	}
	public byte[] getBytes(String fileName){
		byte[] buffer = null;
		try{
			File file=new File(getStorageDirectory()+File.separator+fileName);
			
			FileInputStream fis = new FileInputStream(file);
			int lenght = 0;
			 lenght = fis.available();
	          buffer = ByteBuffer.allocate(lenght).array();
	            fis.read(buffer);
	            android.util.Log.i("Amy gif", "bytes===="+buffer);
			
	            fis.close();
	            }
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return buffer;
	}
	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @return
	 */
	public boolean isFileExists(String fileName){
		return new File(getStorageDirectory()+File.separator+fileName).exists();
	}
	/**
	 * 获取文件的大小
	 * @param fileName
	 * @return
	 */
	public long getFileSize(String fileName) {
		return new File(getStorageDirectory()+File.separator+fileName).length();
	}
	/**
	 * 删除SD卡或者手机的缓存图片和目录
	 */
	public void deleteFile() {
		File dirFile = new File(getStorageDirectory());
		if(! dirFile.exists()){
			return;
		}
		if (dirFile.isDirectory()) {
			String[] childern=dirFile.list();
			for(int i=0;i<childern.length;i++){
				new File(dirFile, childern[i]).delete();
			}
		}
		dirFile.delete();
	}
	 //当SD卡剩余空间小于10M的时候会清理缓存
	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;
	private static final int CACHE_SIZE = 10;
	private static final int MB = 1024*1024;
	private boolean removeCache(String dirPath) 
    {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        
        if (files == null) 
        {
            return true;
        }
        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
        {
            return false;
        }
                                                            
        int dirSize = 0;
        for (int i = 0; i < files.length; i++) 
        {
                dirSize += files[i].length();
        }
                                                            
        if (dirSize > CACHE_SIZE *MB || FREE_SD_SPACE_NEEDED_TO_CACHE > SdCardFreeSpace()) 
        {
            int removeFactor = (int) (0.4 * files.length);
            Arrays.sort(files, new FileLastModifSort());
            for (int i = 0; i < removeFactor; i++) 
            {
                    files[i].delete();
            }
        }
                                                            
        if (SdCardFreeSpace() <= CACHE_SIZE) 
        {
            return false;
        }
                                                                    
        return true;
    }
	 /**
     * 根据文件的最后修改时间进行排序
     */
    private class FileLastModifSort implements Comparator 
    {
		@Override
		public int compare(Object file0, Object file1) {
			 if (((File)file0).lastModified() > ((File) file1).lastModified())
	            {
	                return 1;
	            } 
	            else if (((File)file0).lastModified() == ((File) file1).lastModified()) 
	            {
	                return 0;
	            } 
	            else 
	            {
	                return -1;
	            }
		}
    }
    /** 
     * 计算SD卡上的剩余空间 
     */
    private int SdCardFreeSpace()
    {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double)stat.getAvailableBlocks() * (double) stat.getBlockSize()) /MB;
        return (int) sdFreeMB;
    } 
   /* public static void saveGifToSD(String path,String fileName,byte[] content){
    	 try {
             String gifSavePath = CommonUtil.getSavePath(SysConstant.FILE_SAVE_TYPE_IMAGE);
             File file = new File(path);
             if(!file.exists()){
            	 file.mkdirs();
             }
             File gifFile=new File(path+fileName);
             FileOutputStream fops = new FileOutputStream(gifFile);
             fops.write(content);
             fops.flush();
             fops.close();
//             return gifSavePath;
         } catch (Exception e) {
             e.printStackTrace();
//             return null;
         }
    }*/
    public static String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }
    public static boolean saveCopy(String fromPath,String toPath,String fileName) {
  	   File fromFile=new File(fromPath);
  	  File dirFile = new File(toPath);
       if(!dirFile.exists()){
           dirFile.mkdir();
       }
  	   File toFile=new File(toPath+fileName);
  	   try{
  		   InputStream fosfrom = new FileInputStream(fromFile);
  			OutputStream fosto = new FileOutputStream(toFile);
  			byte bt[] = new byte[1024];
  			int c;
  			while ((c = fosfrom.read(bt)) !=-1) 
  			{
  				fosto.write(bt, 0, c);
  			}
  			fosfrom.close();
  			fosto.close();
  			return true;
  	   }catch(Exception e){
  		   e.printStackTrace();
  		   return false;
  	   }
  }
    private static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;   
    public static void addImageAsApplication(Context context,String name,   long dateTaken,
            String filename) { 
    	ContentResolver cr=context.getContentResolver();
    	ContentValues values = new ContentValues(5);  
        values.put(Images.Media.TITLE, name);  
        values.put(Images.Media.DISPLAY_NAME, filename);//  
        values.put(Images.Media.DATE_TAKEN, dateTaken);  
        values.put(Images.Media.MIME_TYPE, "image/jpeg");  //文件类型
        values.put(Images.Media.DATA, CommonUtil.SAVE_IMAGE_CACHE_PATH+filename);  //文件路径
        
        cr.insert(IMAGE_URI, values);
    }
    

}
