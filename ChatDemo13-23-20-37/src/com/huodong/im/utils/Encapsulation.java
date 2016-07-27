package com.huodong.im.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.huodong.im.chatdemo.activity.LoginActivity;

public class Encapsulation {
	/*
	 * 验证手机号码格式是否正确
	 */
	

	public static boolean isMobileNum(String string) {
		// TODO Auto-generated method stub
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
				Matcher m = p.matcher(string);
				System.out.println(m.matches() + "---");
				return m.matches();
		
	}
	/*
	 * 解析验证用户手机号码，昵称是否存在时服务器返回的数据，true or false
	 */
	public static String analysis(String input)
	{
		
		int start=8+input.indexOf("affect");
		int end=input.indexOf("}",start);
		String nickResult=input.substring(start,end);
		return nickResult;
		
	}
	public static String analysis2(String input)
	{
		int start=input.indexOf("id")+4;
		int end=input.indexOf("}",start);
		String nickResultid=input.substring(start,end);
		return nickResultid;
	}
	/*
	 * 解析创建用户时服务器返回的数据，true or false
	 */
	
	public static String analysisuser(String result) {
		// TODO Auto-generated method stub
		int start=6+result.indexOf("flag");
		int end=result.indexOf("}",start);
		String nickResult=result.substring(start,end);
		return nickResult;
	}
	/*
	 * 解析服务器获取的用户id
	 */
	public static String analysisuserid(String result)
	{
		
		String a[]=result.split(",");
		String b=a[a.length-1];
	
		int start=b.indexOf("id")+4;
		Log.i("-------", b);
		int end=b.indexOf("}}",start);
		Log.i("-------", String.valueOf(end));
		String userid=b.substring(start,end);
		Log.i("-------", userid);
		return userid;
		

	}
	/*
	 * 解析服务器获取的用户id是否成功
	 */
	public static String isanalysisuserid(String result)
	{
		try {
			JSONArray jsonArray=new JSONArray(result);
			JSONObject jsonobject=jsonArray.getJSONObject(0);
			String flag=jsonobject.getString("flag");
			return flag;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/*
	 * 解析修改密码时服务器返回的数据
	 */
	public static String analyupdatepasswordresult(String input) {
		// TODO Auto-generated method stub
		int start=6+input.indexOf("flag");
		int end=input.indexOf("}",start);
		String result=input.substring(start,end);
		return result;
	}
	/*
	 * 解析服务器返回的约会个数
	 */
	public static List<String>  analayengagement(String input)
	{
		List<String> listdata=new ArrayList<String>();
		String data[]=input.split(",");
		String myapplynumber=data[0];
		int Startapplynumber=myapplynumber.indexOf("apply_count")+13;
		Log.i("infoifnof", Startapplynumber+"");
		String resultmynumber=myapplynumber.substring(Startapplynumber);
		listdata.add(resultmynumber);
		String mynumber=data[1];
		int startnumber=mynumber.indexOf("date_count")+12;
		Log.i("infoifnof", startnumber+"");
		String resultapplynumber=mynumber.substring(startnumber);
		listdata.add(resultapplynumber);
		return listdata;
	}
	
}
