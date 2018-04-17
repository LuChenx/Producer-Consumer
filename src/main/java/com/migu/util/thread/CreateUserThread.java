
package com.migu.util.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.migu.util.httpclient.HttpClientHolder;
import com.migu.util.model.UserInfo;

/**
 * 写线程
 * @author luchenxi
 *
 *         2018年3月29日
 */
public class CreateUserThread extends Thread
{
	Logger				 logger     = LogManager.getLogger(CreateUserThread.class);
	BlockingQueue<UserInfo>		queue      = new LinkedBlockingQueue<>(100);
	private static BlockingQueue<UserInfo> writequeue = new LinkedBlockingQueue<>(100);
	static int			     total;
	static int			     success;
	static int			     fail;
	static boolean			 isover     = false;

	public CreateUserThread(BlockingQueue<UserInfo> queue , BlockingQueue<UserInfo> writequeue)
	{
		this.queue = queue;
		CreateUserThread.writequeue = writequeue;
	}

	@ Override
	public void run()
	{
		try
		{
			Map<String , String> res = new HashMap<>();
			while(ReadUsrFileThread.isover == false || CollectionUtils.isNotEmpty(queue))
			{
				UserInfo user = queue.poll();
				if(user != null && StringUtils.isNotEmpty(user.getUserName()))
				{
					try
					{
						//调用户中心创建用户，返回用户ID
						String uid = HttpClientHolder.doPostWithBody(user);
						total++;
						if(StringUtils.isNotEmpty(uid))
						{
							user.setUserId(uid);
							//这里可以直接写文件的...反正都已经这样了，就这样吧
							writequeue.put(user);
							success++;
							res.put(uid, user.getUserName());
						}
						else
						{
							fail++;
							logger.info(user.toString() + "| 创建用户失败：返回uid为空");
						}
					}
					catch (Exception e)
					{
						fail++;
						logger.info("创建用户失败:" + user.toString(), e);
					}
				}
			}
			isover = true;
			System.out.println("用户创建完毕...");
			System.out.println("***************");
			System.out.println("|  total    | " + total + "  |");
			System.out.println("---------------");
			System.out.println("|  success  | " + success + "  |");
			System.out.println("---------------");
			System.out.println("|  fail     | " + fail + "  |");
			System.out.println("***************");
			System.out.println("------------------------------------------------------------");
			System.out
				.println("|               uid              |             用户名                          |");
			System.out.println("------------------------------------------------------------");
			logger.info("用户创建结果---成功：" + success + ",失败：" + fail);
			for(String uid : res.keySet())
			{
				System.out.println("|" + uid + "|    " + res.get(uid) + "     |");
				System.out.println("------------------------------------------------------------");
				logger.info("success:" + uid + "|" + res.get("uid"));
			}
			System.out.println("---------------");
		}
		catch (Exception e)
		{
			System.err.println("LJ！创建用户时失败！");
			System.out.println("结束程序...");
			System.out.println("---------------");
			e.printStackTrace();
			return;
		}
	}
}
