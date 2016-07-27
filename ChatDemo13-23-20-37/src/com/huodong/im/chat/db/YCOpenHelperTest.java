package com.huodong.im.chat.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.huodong.im.chat.mode.ChatMode;
import com.huodong.im.chat.mode.User;
import com.huodong.im.chat.mode.UserMode;
import com.huodong.im.chat.util.CharacterParser;
import com.huodong.im.chat.util.IMConstants;
import com.huodong.im.chat.util.MD5Encoder;
import com.huodong.im.chatdemo.activity.MyApplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class YCOpenHelperTest {
	// public class YCOpenHelperTest extends AndroidTestCase {

	public SQLiteDatabase getDataBase() {
		SQLiteDatabase writableDatabase = null;
		try {
			String encode = MD5Encoder.encode(String.valueOf(uid));
			// YCOpenHelper helper = new YCOpenHelper(getContext(), encode +
			// ".db", null, 1);
			YCOpenHelper helper = new YCOpenHelper(context, encode + ".db", null, 1);
			writableDatabase = helper.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writableDatabase;
	}

	Context context;
	int uid = -1;

	public YCOpenHelperTest(Context context, int uid) {
		this.context = context;
		this.uid = uid;
	}

	public YCOpenHelperTest(Context context) {
		this.context = context;
		MyApplication yApp = MyApplication.getInstance();
		uid = yApp.getUid();
	}

	public YCOpenHelperTest() {
		super();
	}

	/**
	 * 执行完上面代码后，通过DDMS，查看/data/data/com.example.sqlite/databases目录，发现产生了两个文件，
	 * person. db和person.db-journal。其中第一个文件就是我们的数据库文件。第一次操作数据库时，person.db-
	 * journal文件会被自动创建， 该文件是sqlite的一个临时的日志文件，主要用于sqlite数据库的事务回滚操作了。
	 * 但是Android系统中将该文件永久的保存在磁盘中，不会被自动清除的，如果没有操作异常或者不需要事务回滚时，此文件的大小为0。
	 * 这种机制避免了每次生成和删除person.db-journal文件的开销
	 */

	// 插入一条数据
	public void insert(Long time, String name, int type, String content, int uid, int chatid) {
		// 获取数据库对象，该方法在本文档1.1章节中已经给出具体代码
		SQLiteDatabase dataBase = getDataBase();
		String sql = "insert into yc(time,name,type,content,uid,chatid) values(?,?,?,?,?,?)";
		// 执行sql语句
		dataBase.execSQL(sql, new Object[] { String.valueOf(time), name, String.valueOf(type), content,
				String.valueOf(uid), String.valueOf(chatid) });
		// 关闭数据库
		dataBase.close();
	}

	// 插入一条数据
	public void insert(Long time, String name, int type, int uid) {
		SQLiteDatabase dataBase = getDataBase();
		String sql = "insert into yc(time,name,type,uid) values(?,?,?,?)";
		dataBase.execSQL(sql, new Object[] { time, name, type, uid });
		// 关闭数据库
		dataBase.close();
	}

	// 插入一条数据
	public void insertChat(Long time, String content, int type, int uid) {
		SQLiteDatabase dataBase = getDataBase();
		String sql = "insert into yc(time,content,type,uid) values(?,?,?,?)";
		dataBase.execSQL(sql, new Object[] { time, content, type, uid });
		// 关闭数据库
		dataBase.close();
	}

	// public void upDateChat(Long time_tag, Long time) {
	// SQLiteDatabase dataBase = getDataBase();
	//
	// String sql = "select id from yc where time = ? limit 1";
	//// String sql = "update yc set time = ? where time = ? ";
	//// dataBase.execSQL(sql, new Object[] { time, time_tag});
	// Cursor cursor = dataBase.rawQuery(sql, new
	// String[]{String.valueOf(time_tag)});
	// int update=0;
	// if(cursor.moveToNext()){
	// int id = cursor.getInt(cursor.getColumnIndex(ChatMode.ID));
	// ContentValues values = new ContentValues();
	// values.put("time", time);
	// update = dataBase.update("yc", values , "id=?", new
	// String[]{String.valueOf(id)});
	// }
	//
	// Log.v(IMConstants.TAG_YJ, "update_time:"+time);
	// Log.v(IMConstants.TAG_YJ, "update_time_tag:"+time_tag);
	// Log.v(IMConstants.TAG_YJ, "update_count:"+update);
	// dataBase.close();
	// }

	public void insertContacts(ArrayList<User> userList) {
		SQLiteDatabase dataBase = getDataBase();
		String sql = "insert into yccontact(name,uid,chatid) values (?,?,?)";
		for (User user : userList) {
			int uid = user.getUid();
			String name = user.getName();
			int chatid = user.getId();
			dataBase.execSQL(sql, new Object[] { name, uid, chatid });
		}
		dataBase.close();
	}

	public ArrayList<User> queryContacts() {
		SQLiteDatabase dataBase = getDataBase();
		CharacterParser characterParser = CharacterParser.getInstance();
		String sql = "select name,uid from yccontact where chatid > 0 group by uid";
		Cursor cursor = dataBase.rawQuery(sql, null);
		ArrayList<User> userList = new ArrayList<User>();
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex(UserMode.NAME));
			int uid = cursor.getInt(cursor.getColumnIndex(UserMode.UID));
			User user = new User();
			user.setUid(uid);
			user.setName(name);
			String pinYin = characterParser.getSelling(name).toUpperCase();
			user.setFenLei(pinYin);
			userList.add(user);
		}
		cursor.close();
		dataBase.close();
		return userList;
	}
//
//	public void update(int type, int uid) {
//		SQLiteDatabase dataBase = getDataBase();
//		String sql = "update yc set type = ? where uid =? and type = ?";
//		dataBase.execSQL(sql, new Object[] { type, uid, IMConstants.ChaType.BE_APPLY_FDS });
//		dataBase.close();
//	}

	public void insert(ArrayList<ChatMode> dataList) {
		SQLiteDatabase dataBase = getDataBase();
		String sql = "insert into yc(time,name,type,content,uid,chatid) values(?,?,?,?,?,?)";
		for (ChatMode chatMode : dataList) {
			Long time = chatMode.getTime();
			String name = chatMode.getName();
			int type = chatMode.getType();
			String content = chatMode.getContent();
			int uid = chatMode.getUid();
			int chatid = chatMode.getChatid();
			dataBase.execSQL(sql, new Object[] { time, name, type, content, uid, chatid });
		}
		dataBase.close();
	}

	// 查询单个数据
	// public void query() {
	// SQLiteDatabase dataBase = getDataBase();
	// String sql = "select name,age,phone from person where name=?";
	// // 执行rawQuery查询，返回Cursor对象
	// Cursor cursor = dataBase.rawQuery(sql, new String[] { "zhangsan" });
	// Person person = new Person();
	// // 如果游标还有下一个元素，跟我们集合中Iterator中hasNext()方法类似
	// while (cursor.moveToNext()) {
	// // 获取当前游标的第0个元素，元素是从0开始的，而不是1
	// String name = cursor.getString(0);
	// // 也可以通过列名来查询该字段在游标中的位置
	// int age = cursor.getInt(cursor.getColumnIndex("age"));
	// String phone = cursor.getString(2);
	// person.setName(name);
	// person.setAge(age);
	// person.setPhone(phone);
	// }
	// // 关闭游标
	// cursor.close();
	// System.out.println(person);
	// }

	// // 更新数据
	// public void update() {
	// SQLiteDatabase dataBase = getDataBase();
	// String sql = "update person set age=? where name=?";
	// // 将zhangsan的年龄修改为18
	// dataBase.execSQL(sql, new String[] { "18", "zhangsan" });
	// dataBase.close();
	// }

	// 查询所有数据
	public List<ChatMode> queryDifUser() {
		// SELECT user_name,max(`layer_id`) as id FROM `layer_comment_95` GROUP
		// BY `layer_id` ORDER BY `layer_id` ASC
		SQLiteDatabase dataBase = getDataBase();
		List<ChatMode> chatList = new ArrayList<ChatMode>();
		// desc降序 ase 升序
		String sql = "select max(time) as time,type,uid,name,content,id,isRead,chatid from yc where type = '"
				+ IMConstants.ChaType.CHAT_TO + "' or  type = '" + IMConstants.ChaType.CHAT_FROM
				+ "' GROUP BY uid order by time desc";
		Cursor cursor = dataBase.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			ChatMode chatMode = new ChatMode();
			long time = cursor.getLong(cursor.getColumnIndex(ChatMode.TIME));
			int uid = cursor.getInt(cursor.getColumnIndex(ChatMode.UID));
			int id = cursor.getInt(cursor.getColumnIndex(ChatMode.ID));
			int chatid = cursor.getInt(cursor.getColumnIndex(ChatMode.CHATID));
			int isRead = cursor.getInt(cursor.getColumnIndex(ChatMode.IS_READ));
			int type = cursor.getInt(cursor.getColumnIndex(ChatMode.TYPE));
			String content = cursor.getString(cursor.getColumnIndex(ChatMode.CONTENT));
			String name = cursor.getString(cursor.getColumnIndex(ChatMode.NAME));
			chatMode.setTime(time);
			chatMode.setId(id);
			chatMode.setChatid(chatid);
			chatMode.setUid(uid);
			chatMode.setIsRead(isRead);
			chatMode.setName(name);
			chatMode.setContent(content);
			chatMode.setType(type);
			chatList.add(chatMode);
		}
		sql = "select max(time) as time,type,uid,name,content,id,isRead,chatid from yc where type != '"
				+ IMConstants.ChaType.CHAT_TO + "' and  type != '" + IMConstants.ChaType.CHAT_FROM + "' limit 1";
		cursor = dataBase.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			ChatMode chatMode = new ChatMode();
			int id = cursor.getInt(cursor.getColumnIndex(ChatMode.ID));
			if(id==0)
				return chatList;
			long time = cursor.getLong(cursor.getColumnIndex(ChatMode.TIME));
			int chatid = cursor.getInt(cursor.getColumnIndex(ChatMode.CHATID));
			int uid = cursor.getInt(cursor.getColumnIndex(ChatMode.UID));
			int type = cursor.getInt(cursor.getColumnIndex(ChatMode.TYPE));
			String content = cursor.getString(cursor.getColumnIndex(ChatMode.CONTENT));
			int isRead = cursor.getInt(cursor.getColumnIndex(ChatMode.IS_READ));
			String name = cursor.getString(cursor.getColumnIndex(ChatMode.NAME));
			chatMode.setTime(time);
			chatMode.setId(id);
			chatMode.setUid(uid);
			chatMode.setIsRead(isRead);
			chatMode.setChatid(chatid);
			chatMode.setName(name);
			chatMode.setContent(content);
			chatMode.setType(type);
			chatList.add(chatMode);
			Collections.sort(chatList, new Comparator<ChatMode>() {

				@Override
				public int compare(ChatMode lhs, ChatMode rhs) {
					return (int) (rhs.time - lhs.time);
				}

			});
		}
	
		// // 关闭游标
		cursor.close();
		dataBase.close();
		return chatList;
	}

	public void setChatReadOfLocal(int id) {
		SQLiteDatabase dataBase = getDataBase();
		String sql = "update yc set isRead = 1 where id ='" + id + "'";
		dataBase.execSQL(sql);
		dataBase.close();
	}
	public void setChatReadOfNet(int chatid) {
		SQLiteDatabase dataBase = getDataBase();
		String sql = "update yc set isRead = 2 where chatid is not null and (chatid ='" + chatid + "' or chatid < '"+chatid+"') and isRead != 2 ";
		dataBase.execSQL(sql);
		dataBase.close();
	}

	public int queryYcMaxChatId() {
		SQLiteDatabase dataBase = getDataBase();
		String sql = "select  chatid from yc where chatid > 0 order by chatid desc limit 1";
		Cursor cursor = dataBase.rawQuery(sql, null);
		int chatid = 0;
		if (cursor.moveToNext()) {
			chatid = cursor.getInt(cursor.getColumnIndex(ChatMode.CHATID));
		}
		cursor.close();
		dataBase.close();
		return chatid;
	}

	public int queryYccontactMaxChatId() {
		SQLiteDatabase dataBase = getDataBase();
		String sql = "select  chatid   from yccontact where chatid > 0 order by chatid desc limit 1";
		Cursor cursor = dataBase.rawQuery(sql, null);
		int chatid = 0;
		if (cursor.moveToNext()) {
			chatid = cursor.getInt(cursor.getColumnIndex(ChatMode.CHATID));
		}
		cursor.close();
		dataBase.close();
		return chatid;
	}

	public String getName(int uid) {
		String name ="";
		SQLiteDatabase dataBase = getDataBase();
		String sql = "select name from yccontact where uid ='" + uid + "' limit 1";
		Cursor cursor = dataBase.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			name = cursor.getString(cursor.getColumnIndex(ChatMode.NAME));
		}
		cursor.close();
		dataBase.close();
		return name;
	}

	// 查询uid用户所有数据
	public List<ChatMode> queryAll(int uid, int beginId) {
		SQLiteDatabase dataBase = getDataBase();
		List<ChatMode> chatList = new ArrayList<ChatMode>();
		// desc降序
		String sql = "select time,chatid,content,type,id,isRead from yc where id > " + beginId + " and uid =" + uid
				+ " and (type =" + IMConstants.ChaType.CHAT_FROM + " or type =" + IMConstants.ChaType.CHAT_TO
				+ ") group by time ORDER BY time asc";
		Cursor cursor = dataBase.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			ChatMode chatMode = new ChatMode();
			long time = cursor.getLong(cursor.getColumnIndex(ChatMode.TIME));
			int chatid = cursor.getInt(cursor.getColumnIndex(ChatMode.CHATID));
			int type = cursor.getInt(cursor.getColumnIndex(ChatMode.TYPE));
			int id = cursor.getInt(cursor.getColumnIndex(ChatMode.ID));
			int is_read = cursor.getInt(cursor.getColumnIndex(ChatMode.IS_READ));
			String content = cursor.getString(cursor.getColumnIndex(ChatMode.CONTENT));
			chatMode.setTime(time);
			chatMode.setId(id);
			chatMode.setChatid(chatid);
			chatMode.setContent(content);
			chatMode.setType(type);
			chatMode.setIsRead(is_read);
			chatList.add(chatMode);
		}
		// // 关闭游标
		cursor.close();
		dataBase.close();
		return chatList;
	}

	public List<ChatMode> queryNewFds() {
		SQLiteDatabase dataBase = getDataBase();
		List<ChatMode> chatList = new ArrayList<ChatMode>();
		// desc降序
		String sql = "select time,name,type,uid from yc where type !=" + IMConstants.ChaType.CHAT_FROM + " and type !="
				+ IMConstants.ChaType.CHAT_TO + " group by uid ORDER BY time desc";
		Cursor cursor = dataBase.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			ChatMode chatMode = new ChatMode();
			long time = cursor.getLong(cursor.getColumnIndex(ChatMode.TIME));
			int type = cursor.getInt(cursor.getColumnIndex(ChatMode.TYPE));
			int uid = cursor.getInt(cursor.getColumnIndex(ChatMode.UID));
			String name = cursor.getString(cursor.getColumnIndex(ChatMode.NAME));
			chatMode.setTime(time);
			chatMode.setName(name);
			chatMode.setUid(uid);
			chatMode.setType(type);
			chatList.add(chatMode);
		}
		// // 关闭游标
		cursor.close();
		return chatList;
	}

}
