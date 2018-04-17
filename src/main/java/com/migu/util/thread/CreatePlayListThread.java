
package com.migu.util.thread;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.migu.util.httpclient.HttpClientHolder;
import com.migu.util.model.CreatePlayListResponse;
import com.migu.util.model.RemoteConfig;
import com.migu.util.model.SystemConfig;
import com.migu.util.model.UserInfo;
import com.migu.util.tools.SpringContextHolder;

/**
 * 创建歌单
 * @author luchenxi
 *
 *         2018年4月2日
 */
public class CreatePlayListThread extends Thread
{
	Logger			      logger      = LogManager.getLogger(CreatePlayListThread.class);
	BlockingQueue<Map<String , String>> queue       = new LinkedBlockingQueue<>(500);
	BlockingQueue<Map<String , String>> importQueue = new LinkedBlockingQueue<>(500);
	private int			 total;
	private int			 fail;
	private int			 success;
	public static boolean	       isover      = false;

	public CreatePlayListThread(BlockingQueue<Map<String , String>> queue ,
		BlockingQueue<Map<String , String>> importQueue)
	{
		this.queue = queue;
		this.importQueue = importQueue;
	}

	@ Override
	public void run()
	{
		SystemConfig config = SpringContextHolder.getBean("systemconfig");
		Set<String> playlistNameSet = new HashSet<>();
		//读用户文件，取用户信息
		RandomAccessFile raf = null;
		try
		{
			raf = new RandomAccessFile(config.getUserInfo_FilePath() + config.getUserInfo_FileName(), "r");
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		while(ReadPlayListFileThread.isover == false || CollectionUtils.isNotEmpty(queue))
		{
			UserInfo user = new UserInfo();
			try
			{
				//从第一行开始用
				String usrinfo = raf.readLine();
				//最后了，就从头再开始
				if(StringUtils.isEmpty(usrinfo))
				{
					raf = new RandomAccessFile(config.getUserInfo_FilePath() +
						config.getUserInfo_FileName(), "r");
					usrinfo = raf.readLine();
				}
				String[] usrArr = usrinfo.split("\\|");
				user.setUserId(usrArr[0]);
			}
			catch (Exception e)
			{
				System.err.println("随机读取用户信息失败");
				System.out.println("---------------");
				e.printStackTrace();
				continue;
			}
			try
			{
				Map<String , String> paramMap = queue.poll();
				if(paramMap != null && playlistNameSet.add(paramMap.get("playListName")))
				{
					total++;
					RemoteConfig rconfig = SpringContextHolder.getBean("remoteconfig");
					List<NameValuePair> nvps = new ArrayList<NameValuePair>();
					nvps.add(new BasicNameValuePair("createUserId", user.getUserId()));
					for(String key : paramMap.keySet())
					{
						nvps.add(new BasicNameValuePair(key, paramMap.get(key)));
					}
					String result = HttpClientHolder.doPostWithParam(
						rconfig.getCreatePlayList_address(), nvps);
					if(StringUtils.isEmpty(result))
					{
						logger.info("创建歌单失败:http调用失败|uid:" + user.getUserId() + "|playList:" +
							paramMap.get("playListName"));
						fail++;
						continue;
					}
					CreatePlayListResponse response = JSON.parseObject(result,
						CreatePlayListResponse.class);
					if(response.getCode().equals("000000"))
					{
						//导入歌曲、添加标签
						paramMap.put("oprUserId", user.getUserId());
						paramMap.put("playListId", response.getPlayListId());
						//添加操作
						paramMap.put("oprType", "1");
						success++;
						importQueue.put(paramMap);
					}
					else
					{
						logger.info("创建歌单失败:" + response.getInfo() + "|uid:" +
							user.getUserId() + "|playList:" + paramMap.get("playListName"));
						fail++;
					}
				}
			}
			catch (Exception e)
			{
				System.err.println("创建歌单失败");
				System.out.println("---------------");
				fail++;
				e.printStackTrace();
			}
		}
		isover = true;
		System.out.println("歌单创建完毕...");
		System.out.println("***************");
		System.out.println("|  总数    | " + total + "  |");
		System.out.println("---------------");
		System.out.println("|  成功  | " + success + "  |");
		System.out.println("---------------");
		System.out.println("|  失败     | " + fail + "  |");
		System.out.println("***************");
		System.out.println("---------------");
		System.out.println("开始导入歌曲...");
		System.out.println("---------------");
		logger.info("此次歌单创建操作：总数：" + total + ",成功：" + success + ",失败：" + fail);
	}
}
