
package com.migu.util.thread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.migu.util.model.OprPlayListResponse;
import com.migu.util.model.OprPlayListTagResponse;
import com.migu.util.model.RemoteConfig;
import com.migu.util.model.SystemConfig;
import com.migu.util.playlistimport.App;
import com.migu.util.tools.DateUtil;
import com.migu.util.tools.SpringContextHolder;

/**
 * 添加内容
 * @author luchenxi
 *
 *         2018年4月2日
 */
public class OprPlayListContentThread extends Thread
{
	Logger				     logger      = LogManager.getLogger(OprPlayListContentThread.class);
	static BlockingQueue<Map<String , String>> importQueue = new LinkedBlockingQueue<>(500);

	public OprPlayListContentThread(BlockingQueue<Map<String , String>> importQueue)
	{
		OprPlayListContentThread.importQueue = importQueue;
	}

	@ Override
	public void run()
	{
		try
		{
			SystemConfig config = SpringContextHolder.getBean("systemconfig");
			RemoteConfig rconfig = SpringContextHolder.getBean("remoteconfig");
			String filename = config.getPlaylistInfo_FileName() + "_" + DateUtil.getCurDateStr("") + ".txt";
			String filepath = config.getPlaylistInfo_FilePath();
			File file = new File(filepath + filename);
			if(!file.exists())
			{
				file.createNewFile();
			}
			FileWriter fw = null;
			BufferedWriter writer = null;
			fw = new FileWriter(file, true);
			writer = new BufferedWriter(fw);
			while(CreatePlayListThread.isover == false || CollectionUtils.isNotEmpty(importQueue))
			{
				Map<String , String> info = importQueue.poll();
				if(info != null)
				{
					try
					{
						writer.write(info.get("oprUserId") + "|" + info.get("playListId") +
							"|" + info.get("playListName"));
						writer.newLine();//换行
					}
					catch (Exception e)
					{
						logger.info(
							"写文件失败：" + info.get("oprUserId") + "|" + info.get("playListId"),
							e);
					}
					//导入歌曲
					List<String> songIdList = App.contentMap.get(info.get("playListName"));
					String tagIds = App.tagMap.get(info.get("playListName"));
					if(CollectionUtils.isNotEmpty(songIdList))
					{
						List<NameValuePair> nvps = new ArrayList<NameValuePair>();
						StringBuffer resourceIds = new StringBuffer("");
						for(String id : songIdList)
						{
							resourceIds.append(id + "|");
						}
						nvps.add(new BasicNameValuePair("resourceIds", resourceIds.toString()));
						for(String key : info.keySet())
						{
							nvps.add(new BasicNameValuePair(key, info.get(key)));
						}
						String result = HttpClientHolder.doPostWithParam(
							rconfig.getOprPlayList_address(), nvps);
						if(StringUtils.isEmpty(result))
						{
							logger.info("添加歌曲失败！", info.toString());
							continue;
						}
						OprPlayListResponse response = JSON.parseObject(result,
							OprPlayListResponse.class);
						if(response.getCode().equals("000000"))
						{
							try
							{
								nvps.add(new BasicNameValuePair("tagids", tagIds));
								nvps.add(new BasicNameValuePair("createUserId", info
									.get("oprUserId")));
								String tagResult = HttpClientHolder.doPostWithParam(
									rconfig.getOprTag_address(), nvps);
								if(StringUtils.isEmpty(tagResult))
								{
									logger.info("添加标签失败!", info.toString());
								}
								OprPlayListTagResponse tagResponse = JSON
									.parseObject(tagResult,
										OprPlayListTagResponse.class);
								if(tagResponse.getCode().equals("000000"))
								{
									logger.info("添加标签成功:" +
										tagResponse.getPlayListId() + "|" +
										tagResponse.getSuccessNum() + "|" +
										tagResponse.getFailTagIds());
									App.tagMap.remove(info.get("playListName"));
								}
								else
								{
									logger.info("添加标签失败!", tagResponse.getInfo() +
										"|" + info.toString());
								}
							}
							catch (Exception e)
							{
								logger.info("添加标签失败!" + info.toString(), e);
							}
							logger.info("添加歌曲成功:" + response.getPlayListId() + "|" +
								response.getSuccessNum() + "|" +
								response.getFailContentIds());
							App.contentMap.remove(info.get("playListName"));
						}
						else
						{
							logger.info("添加歌曲失败:" + response.getInfo() + "|" +
								info.toString());
						}
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
			System.out.println("歌单导入完毕...");
			System.out.println("***************");
			System.out.println("输出文件：" + filepath + filename);
			System.out.println("***************");
		}
		catch (Exception e)
		{
			System.err.println("添加歌曲操作失败！");
			System.out.println("结束程序...");
			System.out.println("---------------");
			e.printStackTrace();
			return;
		}
	}
}
