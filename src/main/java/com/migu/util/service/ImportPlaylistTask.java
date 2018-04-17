
package com.migu.util.service;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.migu.util.playlistimport.App;
import com.migu.util.thread.CreatePlayListThread;
import com.migu.util.thread.OprPlayListContentThread;
import com.migu.util.thread.ReadPlayListFileThread;

/**
 * 
 * @author luchenxi
 *
 *         2018年4月2日
 */
public class ImportPlaylistTask
{
	static BlockingQueue<Map<String , String>> queue       = new LinkedBlockingQueue<>(500);
	static BlockingQueue<Map<String , String>> importQueue = new LinkedBlockingQueue<>(500);

	public static void importPlaylist()
	{
		//输入源文件路径
		System.out.print("输入歌单数据源文件路径:");
		String playListFilePath = "";
		Scanner scan = new Scanner(System.in);
		playListFilePath = scan.nextLine();
		if(playListFilePath.equals("exit"))
		{
			return;
		}
		File file = new File(playListFilePath);
		System.out.println("---------------");
		while(!file.exists())
		{
			System.out.print("文件不存在，请重新输入：");
			playListFilePath = scan.nextLine();
			if(playListFilePath.equals("exit"))
			{
				return;
			}
			file = new File(playListFilePath);
			System.out.println("---------------");
		}
		System.out.print("输入channel:");
		App.channel = scan.nextLine();
		System.out.println("---------------");
		try
		{
			ReadPlayListFileThread read = new ReadPlayListFileThread(queue, playListFilePath);
			CreatePlayListThread create = new CreatePlayListThread(queue, importQueue);
			OprPlayListContentThread oprContent = new OprPlayListContentThread(importQueue);
			create.start();
			read.start();
			oprContent.start();
			read.join();
			create.join();
			oprContent.join();
		}
		catch (Exception e)
		{
			System.err.println("LJ！文件读取失败");
			System.out.println("---------------");
			e.printStackTrace();
		}
	}
}
