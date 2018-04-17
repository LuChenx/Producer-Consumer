
package com.migu.util.thread;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.migu.util.model.UserInfo;

/**
 * 读线程
 * @author luchenxi
 *
 *         2018年3月29日
 */
public class GetUserSmallavatarThread extends Thread
{
	Logger	       logger	 = LogManager.getLogger(GetUserSmallavatarThread.class);
	Map<String , String> smallavatarMap = new HashMap<>();
	String	       usrFilePath    = "";

	public GetUserSmallavatarThread(Map<String , String> smallavatarMap , String usrFilePath)
	{
		this.smallavatarMap = smallavatarMap;
		this.usrFilePath = usrFilePath;
	}

	@ Override
	public void run()
	{
		try
		{
			FileInputStream inputStream = new FileInputStream(usrFilePath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String str = null;
			System.out.println("开始读取文件内容...");
			System.out.println("---------------");
			while((str = bufferedReader.readLine()) != null)
			{
				try
				{
					UserInfo user = new UserInfo();
					String[] infoArr = str.split("\\|");
					smallavatarMap.put(infoArr[1], infoArr[2]);
				}
				catch (Exception e)
				{
					//TODO记录日志
					logger.info("格式错误:" + str, e);
				}
			}
			System.out.println("用户头像获取完成");
			System.out.println("---------------");
			inputStream.close();
			bufferedReader.close();
		}
		catch (Exception e)
		{
			System.err.println("LJ！文件内容获取失败！");
			System.out.println("结束程序...");
			System.out.println("---------------");
			e.printStackTrace();
			return;
		}
	}
}
