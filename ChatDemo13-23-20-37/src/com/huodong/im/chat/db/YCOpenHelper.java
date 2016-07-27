package com.huodong.im.chat.db;

import com.huodong.im.chat.util.MD5Encoder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class YCOpenHelper extends SQLiteOpenHelper {
	/**
	 * 
	 * @param context
	 *            上下文对象
	 * @param name
	 *            数据库名称
	 * @param factory
	 *            游标结果集工厂，如果需要使用则需要自定义结果集工厂，null值代表使用默认结果集工厂
	 * @param version
	 *            数据库版本号，必须大于等于1
	 */
	public YCOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * 数据库第一次被创建时调用该方法，这里面主要进行对数据库的初始化操作
	 */
	public void onCreate(SQLiteDatabase db) {
		// 数据库第一次被创建的时候执行如下sql语句创建一个person表，已经有了就不会再创建
		// db.execSQL("create table yc(id integer primary key autoincrement);");
		db.execSQL("create table yc(id integer primary key autoincrement,"
				+ "time integer(20) not null, uid integer (11) not null, chatid integer (20), "
				+ "name varchar(10),type integer(2) not null,isRead int(2),content varchar(2000));");
		db.execSQL("create table yccontact(id integer primary key autoincrement," + "uid integer (11) not null,"
				+ "name varchar(10) not null,chatid varchar(10) not null);");
	}

	/**
	 * 数据库更新的时候调用该方法
	 * 
	 * @param db
	 *            当前操作的数据库对象
	 * @param oldVersion
	 *            老版本号
	 * @param enwVersion
	 *            新版本号
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1 && newVersion == 2) {

		}
	}

}
