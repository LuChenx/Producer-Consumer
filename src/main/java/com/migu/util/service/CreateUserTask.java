
package com.migu.util.service;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.migu.util.model.UserInfo;
import com.migu.util.thread.CreateUserThread;
import com.migu.util.thread.ReadUsrFileThread;
import com.migu.util.thread.WriteFileThread;

/**
 * 创建用户
 * @author luchenxi
 *
 *         2018年3月29日
 */
public class CreateUserTask
{
	private static BlockingQueue<UserInfo> queue      = new LinkedBlockingQueue<>(100);
	private static BlockingQueue<UserInfo> writequeue = new LinkedBlockingQueue<>(100);

	public static void createUser()
	{
		//输入源文件路径
		System.out.print("输入用户数据源文件路径:");
		String usrFilePath = "";
		Scanner scan = new Scanner(System.in);
		usrFilePath = scan.nextLine();
		if(usrFilePath.equals("exit"))
		{
			return;
		}
		File file = new File(usrFilePath);
		System.out.println("---------------");
		while(!file.exists())
		{
			System.out.print("文件不存在，请重新输入：");
			usrFilePath = scan.nextLine();
			if(usrFilePath.equals("exit"))
			{
				return;
			}
			file = new File(usrFilePath);
			System.out.println("---------------");
		}
		try
		{
			ReadUsrFileThread read = new ReadUsrFileThread(queue, usrFilePath);
			CreateUserThread create = new CreateUserThread(queue, writequeue);
			WriteFileThread write = new WriteFileThread(writequeue);
			read.start();
			create.start();
			write.start();
			read.join();
			create.join();
			write.join();
		}
		catch (Exception e)
		{
			System.err.println("LJ！文件读取失败");
			System.out.println("---------------");
			e.printStackTrace();
		}
	}
}
