
package com.migu.util.thread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.migu.util.model.SystemConfig;
import com.migu.util.model.UserInfo;
import com.migu.util.tools.SpringContextHolder;

/**
 * 创建成功的用户信息写入文件
 * @author luchenxi
 *
 *         2018年3月30日
 */
public class WriteFileThread extends Thread
{
	Logger				 logger     = LogManager.getLogger(WriteFileThread.class);
	private static BlockingQueue<UserInfo> writequeue = new LinkedBlockingQueue<>(100);

	public WriteFileThread(BlockingQueue<UserInfo> writequeue)
	{
		WriteFileThread.writequeue = writequeue;
	}

	@ Override
	public void run()
	{
		try
		{
			SystemConfig config = SpringContextHolder.getBean("systemconfig");
			String filename = config.getUserInfo_FileName();
			String filepath = config.getUserInfo_FilePath();
			File file = new File(filepath + filename);
			if(!file.exists())
			{
				file.createNewFile();
			}
			FileWriter fw = null;
			BufferedWriter writer = null;
			fw = new FileWriter(file, true);
			writer = new BufferedWriter(fw);
			while(CreateUserThread.isover == false || CollectionUtils.isNotEmpty(writequeue))
			{
				UserInfo user = writequeue.poll();
				if(user != null)
				{
					try
					{
						writer.write(user.getUserId() + "|" + user.getUserName());
						writer.newLine();//换行
					}
					catch (Exception e)
					{
						logger.info("写文件失败：" + user.toString(), e);
					}
				}
			}
			try
			{
				writer.close();
				fw.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			System.out.println("输出文件：" + filepath + filename);
			System.out.println("***************");
		}
		catch (Exception e)
		{
			System.err.println("LJ！用户信息文件写操作失败！");
			System.out.println("结束程序...");
			System.out.println("---------------");
			e.printStackTrace();
			return;
		}
	}
}
