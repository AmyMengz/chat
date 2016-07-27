package com.huodong.im.config;
/**
 * intent bundle 传值设定
 * @author Administrator
 *
 */

public class IntentConstant {
	/**
	 * 跳转Activity 参数
	 */
	public static final String KEY_DATE_KEY = "date_key";
	public static final String KEY_SPONSORID = "key_sponsor_id";
	public static final String KEY_LONGITUDE = "key_longitude";
	public static final String KEY_LATITUDE = "key_latitude";
	public static final String KEY_DATE_CUR = "cur_date";
	public static final String KEY_UID = "uid";
	public static final String KEY_NICK_NAME = "name";
	public static final String KEY_PARTNER_NUM = "partner_num";
	public static final String KEY_DATE_NOTES = "date_notes";
	public static final String KEY_FANS = "fans";
	public static final String KEY_BUSSINESS_CUR = "cur_bussiness";//当前商户
	/**
	 * DZDP 请求参数
	 */
	public static final String KEY_CITY = "city";
	public static final String KEY_REGION = "region";
	public static final String KEY_KEYWORD = "keyword";
	public static final String KEY_DISCOUNT = "isDiscountOnly";
	
	/**
	 * 跳转到SelectionActivity 选择布局参数
	 */
	public static final String KEY_SELECTION_INDEX = "selection_ac_index";
	
	
	/**
	 * activity Result 筛选附近的人参数
	 */
	public static final String KEY_GENDER_USER = "gender_user";
	public static final String KEY_ACTIVE_USER = "active_user";
	/**
	 * activity Result 筛选附近的约会
	 */
	public static final String KEY_GENDER_LIMIT = "gender_limit";
	public static final String KEY_FEES_LIMIT = "fees_limit";
	public static final String KEY_TIME_LIMIT = "time_limit";
	public static final String RESULT_DATE_NOTES = "new_notes";//startActivityForResult  编辑约会说明
	public static final String RESULT_DATE_REFUSH = "date_refresh";//startActivityForResult  刷新约会列表 result 参数

	
	/**
	 * start ActivityForResult requestCode
	 */
	public static final int REQUESTCODE_CITY = 0;
	public static final int REQUESTCODE_ADDRESS = 1;
	public static final int REQUESTCODE_ADDRESS_DETAIL = 2;
	
	public static final int REQUESTCODE_SELECTION = 3;
	public static final int REQUESTCODE_EDIT_NOTE = 4;
	public static final int REQUESTCODE_DATE_DETAIL = 5;//约会详情
	public static final int REQUESTCODE_BUSSINESS_DETAIL = 6;//商家详情
	
	
	/**
	 * handler DateDetailActivity
	 */
	public static final int HANDLER_APPLY = 0;      //0 1
	public static final int HANDLER_CANCEL_APPLY = 2;//2 3
	public static final int HANDLER_DELETE_DATE = 4;//4 5
	public static final int HANDLER_DELETE_DATE_CAN_NOT = 6;
	public static final int HANDLER_GET_ALLOW_STATE =7;      //7 8
	public static final int HANDLER_GET_INFO = 9;      //9 10  //获取用户信息handler
		
	
	public static final int HANDLER_OK = 0;
	public static final int HANDLER_ERROR = 1;
	
	public static final int HANDLER_USER_INFO = 0;      //0 1
	public static final int HANDLER_DATE_COUNT = 2;//2 3
	
	public static final int HANDLER_APPLY_MANAGE = 0;      //0 1
	public static final int HANDLER_APPLY_MANAGE_NO = 3; 
//	public static final int HANDLER_DATE_COUNT = 2;//2 3
	public static final int HANDLER_APPLY_MANAGE_ALLOW = 4;      //4 5
	
	
	
	
	/**
	 * 时间选择控制
	 * 0 选择时间小于当前时间
	 * 1选择时间太近，请大于2小时
	 * 2选择时间太长 ，请在一个月内
	 */
	public static final int DATE_TIME_INVALID=0;
	public static final int DATE_TIME_TOO_CLOSE=1;
	public static final int DATE_TIME_TOO_FAR=2;
	public static final int DATE_TIME_NORMAL=3;
	
	
	
	
	

}
