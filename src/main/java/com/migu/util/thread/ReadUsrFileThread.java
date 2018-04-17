
package com.migu.util.thread;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.migu.util.model.UserInfo;

/**
 * 读线程
 * @author luchenxi
 *
 *         2018年3月29日
 */
public class ReadUsrFileThread extends Thread
{
	Logger		  logger      = LogManager.getLogger(ReadUsrFileThread.class);
	BlockingQueue<UserInfo> queue       = new LinkedBlockingQueue<>(100);
	String		  usrFilePath = "";
	private int	     total;
	private int	     fail;
	private int	     success;
	public static boolean   isover      = false;

	public ReadUsrFileThread(BlockingQueue<UserInfo> queue , String usrFilePath)
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
			System.out.println("开始读取文件内容...");
			System.out.println("---------------");
			while((str = bufferedReader.readLine()) != null)
			{
				total++;
				try
				{
					UserInfo user = new UserInfo();
					String[] infoArr = str.split("\\|");
					user.setSmallavatar(infoArr[2]);
					user.setUserName(infoArr[1]);
					queue.put(user);
					success++;
				}
				catch (Exception e)
				{
					//TODO记录日志
					fail++;
					logger.info("格式错误:" + str, e);
				}
			}
			isover = true;
			System.out.println("文件读取完毕...");
			System.out.println("***************");
			System.out.println("|  total    | " + total + "  |");
			System.out.println("---------------");
			System.out.println("|  success  | " + success + "  |");
			System.out.println("---------------");
			System.out.println("|  fail     | " + fail + "  |");
			System.out.println("***************");
			System.out.println("---------------");
			System.out.println("开始创建用户...");
			System.out.println("---------------");
			inputStream.close();
			bufferedReader.close();
			logger.info("此次用户信息源文件读取操作：总行数：" + total + ",成功行数：" + success + ",失败行数：" + fail);
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
