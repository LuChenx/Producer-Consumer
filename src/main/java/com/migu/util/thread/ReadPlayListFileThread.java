
package com.migu.util.thread;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.migu.util.model.SongInfo;
import com.migu.util.playlistimport.App;

/**
 * 
 * @author luchenxi
 *
 *         2018年4月2日
 */
public class ReadPlayListFileThread extends Thread
{
	Logger			      logger	   = LogManager.getLogger(ReadPlayListFileThread.class);
	BlockingQueue<Map<String , String>> queue	    = new LinkedBlockingQueue<>(500);
	String			      playListFilePath = "";
	private int			 total;
	private int			 fail;
	private int			 success;
	public static boolean	       isover	   = false;

	public ReadPlayListFileThread(BlockingQueue<Map<String , String>> queue , String playListFilePath)
	{
		this.queue = queue;
		this.playListFilePath = playListFilePath;
	}

	@ Override
	public void run()
	{
		try
		{
			FileInputStream inputStream = new FileInputStream(playListFilePath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String str = null;
			String lastLine = null;
			System.out.println("开始读取文件内容...");
			System.out.println("---------------");
			String lastName = "";
			List<String> playListContent = new ArrayList<>();
			List<SongInfo> songList = new ArrayList<>();
			while((str = bufferedReader.readLine()) != null)
			{
				total++;
				String playListName = "";
				Map<String , String> playlistMap = new HashMap<String , String>();
				try
				{
					lastLine = str;
					str = str + "end";
					String[] infoArr = str.split("\\|\\^\\|");
					SongInfo song = new SongInfo();
					playListName = infoArr[2];
					if(StringUtils.isEmpty(playListName))
					{
						continue;
					}
					song.setPlayListName(playListName);
					if(StringUtils.isNotEmpty(lastName) && !lastName.equals(playListName))
					{
						//把这个歌单的歌曲先放内存中
						playListContent = getContentList(songList);
						List<String> idList = App.contentMap.get(lastName);
						if(CollectionUtils.isEmpty(idList))
						{
							idList = playListContent;
						}
						else
						{
							idList.addAll(playListContent);
						}
						App.contentMap.put(lastName, idList);
						playlistMap.put("playListName", lastName);
						playlistMap.put("channel", App.channel);
						playlistMap.put("playListType", "2");
						playlistMap.put("status", "1");
						queue.put(playlistMap);
						success++;
						playListContent = new ArrayList<>();
						songList = new ArrayList<>();
					}
					lastName = playListName;
					String createTime = infoArr[13];
					if(StringUtils.isNotEmpty(createTime))
					{
						song.setCreateTime(createTime);
					}
					song.setSongId(infoArr[14]);
					song.setHasPic(infoArr[15]);
					songList.add(song);
					App.tagMap.put(playListName, getTagIds(infoArr[3]));
				}
				catch (Exception e)
				{
					//TODO记录日志
					fail++;
					logger.info("读取源文件错误:" + str, e);
					System.err.println("读取源文件错误：" + str);
					e.printStackTrace();
				}
			}
			if(CollectionUtils.isNotEmpty(songList))
			{
				Map<String , String> playlistMap = new HashMap<String , String>();
				playListContent = getContentList(songList);
				List<String> idList = App.contentMap.get(lastName);
				if(CollectionUtils.isEmpty(idList))
				{
					idList = playListContent;
				}
				else
				{
					idList.addAll(playListContent);
				}
				App.contentMap.put(lastName, idList);
				playlistMap.put("playListName", lastName);
				playlistMap.put("channel", App.channel);
				playlistMap.put("playListType", "2");
				playlistMap.put("status", "1");
				queue.put(playlistMap);
				success++;
			}
			isover = true;
			System.out.println("文件读取完毕...");
			System.out.println("***************");
			System.out.println("|  总行数    | " + total + "  |");
			System.out.println("---------------");
			System.out.println("|  歌单数  | " + success + "  |");
			System.out.println("---------------");
			System.out.println("|  失败数     | " + fail + "  |");
			System.out.println("***************");
			System.out.println("---------------");
			System.out.println("开始创建歌单...");
			System.out.println("---------------");
			inputStream.close();
			bufferedReader.close();
			logger.info("此次歌单信息源文件读取操作：总行数：" + total + ",歌单数：" + success + ",失败数：" + fail);
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

	private static List<String> getContentList(List<SongInfo> songList)
	{
		List<String> idList = new ArrayList<>();
		List<String> idWithPic = new ArrayList<>();
		List<SongInfo> songWithBoth = new ArrayList<>();
		for(SongInfo song : songList)
		{
			if(StringUtils.isEmpty(song.getHasPic()) || song.getHasPic().equals("0"))
			{
				//没图片的就不管了
				idList.add(song.getSongId());
			}
			else if(song.getCreateTime() == null)
			{
				//有图片但是没时间的
				idWithPic.add(song.getSongId());
			}
			else
			{
				//有图片且有时间的
				songWithBoth.add(song);
			}
		}
		idList.addAll(idWithPic);
		while(CollectionUtils.isNotEmpty(songWithBoth))
		{
			SongInfo song = songWithBoth.get(0);
			for(SongInfo songinfo : songWithBoth)
			{
				if(songinfo.getCreateTime().compareTo(song.getCreateTime()) < 0)
				{
					song = songinfo;
				}
			}
			songWithBoth.remove(song);
			idList.add(song.getSongId());
		}
		return idList;
	}

	private static String getTagIds(String ids)
	{
		String[] idArr = ids.split("、");
		StringBuffer idString = new StringBuffer();
		for(int i = 0 ; i < idArr.length ; i++)
		{
			idString.append(idArr[i] + "|");
		}
		return idString.toString();
	}
}
