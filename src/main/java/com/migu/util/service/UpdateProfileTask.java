
package com.migu.util.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;

import com.migu.util.httpclient.HttpClientHolder;
import com.migu.util.model.RemoteConfig;
import com.migu.util.model.SystemConfig;
import com.migu.util.model.UserInfo;
import com.migu.util.thread.GetUserSmallavatarThread;
import com.migu.util.thread.ReadUserInfoThread;
import com.migu.util.tools.SpringContextHolder;

/**
 * 更新用户头像
 * @author luchenxi
 *
 *         2018年4月11日
 */
public class UpdateProfileTask
{
	static List<UserInfo>       queue	  = new ArrayList<>();
	static Map<String , String> smallavatarMap = new HashMap<>();

	public static void setSmallavatar()
	{
		SystemConfig config = SpringContextHolder.getBean("systemconfig");
		String usrFilePath = config.getUserInfo_FilePath() + config.getUserInfo_FileName();
		File file = new File(usrFilePath);
		if(!file.exists())
		{
			System.err.println("找不到用户信息文件");
			System.out.println("---------------");
			return;
		}
		//输入源文件路径
		System.out.print("输入用户数据源文件路径:");
		String oldUsrFilePath = "";
		Scanner scan = new Scanner(System.in);
		oldUsrFilePath = scan.nextLine();
		if(oldUsrFilePath.equals("exit"))
		{
			return;
		}
		File file2 = new File(oldUsrFilePath);
		System.out.println("---------------");
		while(!file2.exists())
		{
			System.out.print("文件不存在，请重新输入：");
			oldUsrFilePath = scan.nextLine();
			if(oldUsrFilePath.equals("exit"))
			{
				return;
			}
			file = new File(oldUsrFilePath);
			System.out.println("---------------");
		}
		try
		{
			ReadUserInfoThread read = new ReadUserInfoThread(queue, usrFilePath);
			GetUserSmallavatarThread get = new GetUserSmallavatarThread(smallavatarMap, oldUsrFilePath);
			read.start();
			get.start();
			read.join();
			get.join();
		}
		catch (Exception e)
		{
			System.err.println("LJ！文件读取失败");
			System.out.println("---------------");
			e.printStackTrace();
		}
		try
		{
			System.out.println("开始添加头像");
			System.out.println("---------------");
			for(UserInfo user : queue)
			{
				String smallavatar = smallavatarMap.get(user.getUserName());
				if(StringUtils.isNotEmpty(smallavatar))
				{
					user.setSmallavatar(smallavatar);
					String xmlParam = getParam(user);
					HttpClient httpclient = new HttpClient();
					RemoteConfig rconfig = SpringContextHolder.getBean("remoteconfig");
					PostMethod post = new PostMethod(rconfig.getUpdateuser_address());
					post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
					post.setRequestBody(xmlParam);
					post.setRequestHeader("Authorization", HttpClientHolder.getAuthorizationStr());
					httpclient.executeMethod(post);
					String info = new String(post.getResponseBody(), "utf-8");
					System.out.println("------------------------------------");
					System.out.println("Res:" + info);
				}
			}
		}
		catch (Exception e)
		{
			System.err.println("添加头像失败");
			System.out.println("---------------");
			e.printStackTrace();
		}
	}

	private static String getParam(UserInfo userInfo)
	{
		StringBuffer xmlParam = new StringBuffer("<?xml version='1.0' encoding='utf-8'?>");
		xmlParam.append("<updateProfile><updateProfileReq>");
		xmlParam.append("<userInfo>");
		xmlParam.append("<identityID>" + userInfo.getUserId() + "</identityID>");
		xmlParam.append("<ext>");
		xmlParam.append("<item>");
		xmlParam.append("<key>smallavatar</key>");
		xmlParam.append("<value>" + userInfo.getSmallavatar() + "</value>");
		xmlParam.append("</item>");
		xmlParam.append("</ext>");
		xmlParam.append("</userInfo>");
		xmlParam.append("</updateProfileReq></updateProfile>");
		return xmlParam.toString();
	}
}
