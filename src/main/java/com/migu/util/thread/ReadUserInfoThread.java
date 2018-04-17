
package com.migu.util.thread;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.migu.util.model.UserInfo;

/**
 * 读取用户文件信息
 * @author luchenxi
 *
 *         2018年4月11日
 */
public class ReadUserInfoThread extends Thread
{
	Logger	 logger      = LogManager.getLogger(ReadUsrFileThread.class);
	List<UserInfo> queue       = new ArrayList<>();
	String	 usrFilePath = "";

	public ReadUserInfoThread(List<UserInfo> queue , String usrFilePath)
	{
		this.queue = queue;
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
			System.out.println("读取文件" + usrFilePath);
			System.out.println("---------------");
			Map<String , String> smallavatarMap = new HashMap<>();
			while((str = bufferedReader.readLine()) != null)
			{
				try
				{
					UserInfo user = new UserInfo();
					String[] infoArr = str.split("\\|");
					user.setUserId(infoArr[0]);
					user.setUserName(infoArr[1]);
					queue.add(user);
				}
				catch (Exception e)
				{
					//TODO记录日志
					logger.info("格式错误:" + str, e);
				}
			}
			System.out.println("用户信息获取完成");
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
