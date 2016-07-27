package com.huodong.im.config;

public class UrlConstant {
	//大众点评参数
	public static final String appkey = "334421635";
	public static final String secret = "21527523e680416ba210fded39d1408f";
	public static final String DZDP_API_BUSSINESS_URL = "http://api.dianping.com/v1/business/find_businesses";
	public static final String DZDP_API_REGION_URL = "http://api.dianping.com/v1/metadata/get_regions_with_businesses";
	/**
	 * DYT
	 */
	public static final String BASIC_URL="http://120.24.2.49:8787/yj2/servlet";
	public static final String UPDATE_POS_URL =BASIC_URL+"/UpdatePosServlet";//上传位置信息
	public static final String NEW_DATE_URL =BASIC_URL+"/SendDateServlet";//发布约会
	public static final String NEARBY_USER_URL =BASIC_URL+"/RecentPeopleServlet";//附近的人
	public static final String NEARBY_DATE_URL =BASIC_URL+"/DateInfoServlet";//附近的人
	
	public static final String MY_DATE_URL =BASIC_URL+"/MyDateInfoServlet";//附近的人
	public static final String DATE_DETAIL_URL =BASIC_URL+"/DataDetailServlet";//约会详情
	public static final String DATE_APPLY_URL =BASIC_URL+"/ApplyDateServlet";//apply约会
	public static final String DATE_CANCEL_APPLY_URL =BASIC_URL+"/DeleteDateApplyServlet";//cancel约会apply
	public static final String DATE_DELETE_DATE_URL =BASIC_URL+"/DeleteDateServlet";//delete约会
	
	
	public static final String NEARBY_USER_DETAIL_URL =BASIC_URL+"/UserInfoServlet";//附近的人detail
	public static final String GET_DATE_COUNT_URL =BASIC_URL+"/GetDateCountServlet";
	public static final String MANAGE_APPLY_URL =BASIC_URL+"/DateInfoByDateIdServlet";//管理约会报名
	public static final String ALLOW_APPLY_URL =BASIC_URL+"/PassDateApplyServlet";//批准约会报名  
//	public static final String ALLOW_APPLY_URL =BASIC_URL+"/UserInfoServlet";
	
	public static final String IMAGE_LOAD_URL =BASIC_URL+"/ImageDownServer";//图片下载url  
	public static final String UPDATE_DATE_NOTE_URL =BASIC_URL+"/UpdateDateNoteServlet";//更新约会说明
}
