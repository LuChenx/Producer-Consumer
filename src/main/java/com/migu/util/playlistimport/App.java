
package com.migu.util.playlistimport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.migu.util.service.CreateUserTask;
import com.migu.util.service.ImportPlaylistTask;
import com.migu.util.service.UpdateProfileTask;

/**
 * Hello world!
 *
 */
public class App
{
	//放歌曲信息
	public static Map<String , List<String>> contentMap = new HashMap<>();
	//放标签信息
	public static Map<String , String>       tagMap     = new HashMap<>();
	public static String		     channel;

	public static void main(String[] args)
	{
		try
		{
			String[] path = {"applicationContext.xml"};
			ApplicationContext act = new ClassPathXmlApplicationContext(path);
			if(act != null)
			{
				System.out.println("***************************************");
				System.out.println("|               程序启动完成                                          |");
				System.out.println("***************************************");
			}
		}
		catch (Exception e)
		{
			System.err.println("LJ！程序启动失败！");
			System.out.println("结束程序...");
			System.out.println("---------------");
			e.printStackTrace();
			return;
		}
		String order = "0";
		order = showMenu(order);
		while(!order.equals("0"))
		{
			//创建用户
			if(order.equals("1"))
			{
				CreateUserTask.createUser();
			}
			else if(order.equals("2"))
			{
				ImportPlaylistTask.importPlaylist();
			}
			else if(order.equals("3"))
			{
				UpdateProfileTask.setSmallavatar();
			}
			order = "0";
			order = showMenu(order);
		}
		return;
	}

	private static String showMenu(String order)
	{
		try
		{
			while(!order.equals("1") && !order.equals("2") && !order.equals("3"))
			{
				System.out.println("---------------");
				System.out.println("|   -MENU-    |");
				System.out.println("---------------");
				System.out.println("|  1  | 创建用户  |");
				System.out.println("---------------");
				System.out.println("|  2  | 导入歌单  |");
				System.out.println("---------------");
				System.out.println("|  3  | 添加头像  |");
				System.out.println("---------------");
				System.out.println("|  0  | 退出程序  |");
				System.out.println("---------------");
				System.out.print("请输入指令:");
				Scanner scan = new Scanner(System.in);
				order = scan.nextLine();
				System.out.println("---------------");
				if(order.equals("0"))
				{
					System.out.println("结束程序...");
					System.out.println("---------------");
					return order;
				}
				if(!order.equals("1") && !order.equals("2") && !order.equals("3"))
				{
					System.err.println("无效指令，你是瞎了么！");
					System.out.println("---------------");
				}
			}
		}
		catch (Exception e)
		{
			System.err.println("LJ！指令接收失败！");
			System.out.println("结束程序...");
			System.out.println("---------------");
			e.printStackTrace();
		}
		return order;
	}
}
