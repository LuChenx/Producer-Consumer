
package com.migu.util.tools;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.StringUtils;

/**
 * 
 * @author luchenxi
 *
 *         2018年3月9日
 */
public class DateUtil
{
	/**
	 * 默认的格式:yyyy-MM-dd
	 */
	public static final String PATTERN_DEFAULT	       = "yyyy-MM-dd";
	/** 格式：yyyy-MM-dd HH:mm:ss.S */
	public static final String FULL_STANDARD_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
	/** 格式：yyyy-MM-dd HH:mm:ss */
	public static final String STANDARD_DATETIME_FORMAT      = "yyyy-MM-dd HH:mm:ss";
	/** 格式：yyyy-MM-dd HH:mm */
	public static final String INCOMPLETE_DATETIME_FORMAT    = "yyyy-MM-dd HH:mm";
	/** 格式：yyyyMMddHHmmss */
	public static final String SHORT_DATETIME_FORMAT	 = "yyyyMMddHHmmss";
	/** 格式：yyyy-MM */
	public static final String STANDARD_MONTH_FORMAT	 = "yyyy-MM";
	/** 格式：yyyyMMdd */
	public static final String SHORT_DATE_FORMAT	     = "yyyyMMdd";
	/** 格式：MMdd */
	public static final String SHORT_MMDD_FORMAT	     = "MM-dd";
	/** 格式：HHmmss */
	public static final String SHORT_TIME_FORMAT	     = "HHmmss";
	/** 格式：HHmmss */
	public static final String STANDARD_TIME_FORMAT	  = "HH:mm:ss";
	/** 格式：yyyy年MM月dd日(年月日) */
	public static final String STANDARD_DATE_FORMAT_NYR      = "yyyy年MM月dd日";

	/**
	 * 转换日期格式
	 * @param dateStr 日期字符串
	 * @param src 日期源格式
	 * @param dest 日期转后后的目标格式
	 * @return 转换格式后的日期字符串
	 * @throws ParseException 如果格式转换失败，则抛出异常
	 */
	public static String changeDateStrFormate(String dateStr , String src , String dest) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(src);
		Date date = sdf.parse(dateStr);
		sdf = new SimpleDateFormat(dest);
		return sdf.format(date);
	}

	/**
	 * 获取指定格式的当前时间字符串
	 * @param pattern
	 * @return
	 */
	public static String getCurDateStr(String pattern)
	{
		String dateStr = null;
		try
		{
			if(!StringUtils.isEmpty(pattern))
			{
				dateStr = DateFormatUtils.format(new Date(), pattern);
			}
			else
			{
				dateStr = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return dateStr;
	}

	/**
	 * 将字符串转换为Date类型
	 * @param dateStr 日期字符串
	 * @param pattern 格式
	 * @return Date对象
	 * @throws ParseException 当进行格式化的时候失败，抛出该异常。
	 */
	public static Date getDateByString(String dateStr , String pattern) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(dateStr);
	}

	/**
	 * 将Date类型转换为String类型
	 * @param date 日期
	 * @param pattern 格式
	 * @return 日期对应格式的字符串
	 * @throws ParseException 当格式转换出现异常，抛出
	 */
	public static String getStringByDate(Date date , String pattern) throws ParseException
	{
		if(StringUtils.isEmpty(pattern))
		{
			return getDefaultDateFormat().format(date);
		}
		else
		{
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
	}

	/**
	 * 获取默认格式对象
	 * @return 简单的时间格式化对象
	 */
	private static SimpleDateFormat getDefaultDateFormat()
	{
		return new SimpleDateFormat(PATTERN_DEFAULT);
	}

	/**
	 * 格式完全日期时间字符串（包含毫秒，标准格式：yyyy-MM-dd HH:mm:ss.S）
	 * 
	 * @param date 日期时间
	 * @return 完全日期时间字符串
	 */
	public static String formatFullStandardDateTime(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(FULL_STANDARD_DATETIME_FORMAT);
		return sdf.format(date);
	}

	/**
	 * 格式日期时间字符串（标准格式：yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param date 日期时间
	 * @return 日期时间字符串
	 */
	public static String formatStandardDateTime(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_DATETIME_FORMAT);
		return sdf.format(date);
	}

	/**
	 * 格式日期时间字符串（短格式：yyyyMMddHHmmss）
	 * 
	 * @param date 日期时间
	 * @return 日期时间字符串
	 */
	public static String formatShortDateTime(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATETIME_FORMAT);
		return sdf.format(date);
	}

	/**
	 * 格式日期字符串（标准格式：yyyy-MM-dd）
	 * 
	 * @param date 日期
	 * @return 日期字符串
	 */
	public static String formatStandardDate(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DEFAULT);
		return sdf.format(date);
	}

	/**
	 * 格式日期字符串（标准格式：yyyy-MM）
	 * 
	 * @param date 日期
	 * @return 日期字符串
	 */
	public static String formatStandardMonth(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_MONTH_FORMAT);
		return sdf.format(date);
	}

	/**
	 * 格式日期字符串（短格式：yyyyMMdd）
	 * 
	 * @param date 日期
	 * @return 日期字符串
	 */
	public static String formatShortDate(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATE_FORMAT);
		return sdf.format(date);
	}

	/**
	 * 格式日期字符串（短格式：MMdd）
	 * 
	 * @param date 日期
	 * @return 日期字符串
	 */
	public static String formatShortMMDDDate(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_MMDD_FORMAT);
		return sdf.format(date);
	}

	/**
	 * 格式时间字符串（短格式：HHmmss）
	 * 
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String formatShortTime(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_TIME_FORMAT);
		return sdf.format(date);
	}

	/**
	 * 解析完全日期时间字符串（包含毫秒，标准格式：yyyy-MM-dd HH:mm:ss.S）
	 * 
	 * @param dateTimeStr 完全日期时间字符串
	 * @return 日期时间
	 * @throws ParseException
	 */
	public static Date parseFullStandardDateTime(String dateTimeStr) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(FULL_STANDARD_DATETIME_FORMAT);
		return sdf.parse(dateTimeStr);
	}

	/**
	 * 解析日期时间字符串（标准格式：yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param dateTimeStr 日期时间字符串
	 * @return 日期时间
	 * @throws ParseException
	 */
	public static Date parseStandardDateTime(String dateTimeStr) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_DATETIME_FORMAT);
		return sdf.parse(dateTimeStr);
	}

	/**
	 * 解析日期时间字符串 (不完整格式：yyyy-MM-dd HH:mm)
	 * 
	 * @param dateTimeStr
	 * @return
	 * @throws ParseException
	 */
	public static Date parseIncompleteDateTime(String dateTimeStr) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(INCOMPLETE_DATETIME_FORMAT);
		return sdf.parse(dateTimeStr);
	}

	/**
	 * 格式化日期时间 (不完整格式：yyyy-MM-dd HH:mm)
	 * 
	 * @param dateTime
	 * @return 格式化后的时间
	 * @throws ParseException
	 */
	public static String formatIncompleteDateTime(Date dateTime) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(INCOMPLETE_DATETIME_FORMAT);
		return sdf.format(dateTime);
	}

	/**
	 * 解析日期时间字符串（短格式：yyyyMMddHHmmss）
	 * 
	 * @param dateTimeStr 日期时间字符串
	 * @return 日期时间
	 * @throws ParseException
	 */
	public static Date parseShortDateTime(String dateTimeStr) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATETIME_FORMAT);
		return sdf.parse(dateTimeStr);
	}

	/**
	 * 解析日期字符串（标准格式：yyyy-MM-dd）
	 * 
	 * @param dateStr 日期字符串
	 * @return 日期
	 * @throws ParseException
	 */
	public static Date parseStandardDate(String dateStr) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DEFAULT);
		return sdf.parse(dateStr);
	}

	/**
	 * 解析日期字符串（标准格式：yyyy-MM）
	 * 
	 * @param dateStr 日期字符串
	 * @return 日期
	 * @throws ParseException
	 */
	public static Date parseStandardMonth(String dateStr) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_MONTH_FORMAT);
		return sdf.parse(dateStr);
	}

	/**
	 * 解析日期字符串（短格式：yyyyMMdd）
	 * 
	 * @param dateStr 日期字符串
	 * @return 日期
	 * @throws ParseException
	 */
	public static Date parseShortDate(String dateStr) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATE_FORMAT);
		return sdf.parse(dateStr);
	}

	/**
	 * 解析日期字符串（短格式：MMdd）
	 * 
	 * @param dateStr 日期字符串
	 * @return 日期
	 * @throws ParseException
	 */
	public static Date parseShortMMDDDate(String dateStr) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_MMDD_FORMAT);
		return sdf.parse(dateStr);
	}

	/**
	 * 解析时间字符串（短格式：HHmmss）
	 * 
	 * @param dateStr 时间字符串
	 * @return 时间
	 * @throws ParseException
	 */
	public static Date parseShortTime(String dateStr) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_TIME_FORMAT);
		return sdf.parse(dateStr);
	}

	/**
	 * 格式时间字符串（短格式：HH:mm:ss）
	 * 
	 * @param date 时间
	 * @return 时间字符串
	 */
	public static String formatStandardTime(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_TIME_FORMAT);
		return sdf.format(date);
	}

	/**
	 * 获取传入日期的上一天
	 * 
	 * @param date
	 * @return 日期
	 */
	public static Date getStandardDatePre(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 获取传入日期的下一天
	 * 
	 * @param date
	 * @return 日期
	 */
	public static Date getStandardDateNext(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * 获取指定日期的0时0分0秒
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartTimeOfDay(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取当天的0时0分0秒
	 * 
	 * @return
	 */
	public static Date getStartTimeOfToday()
	{
		return getStartTimeOfDay(new Date());
	}

	/**
	 * 格式 yyyyMMdd:HHmmss
	 * 
	 * 契合老版本的日期格式
	 * 
	 * @return
	 */
	public static final String getTimestep4CurrentdateSwitchV3()
	{
		Format formatter = new SimpleDateFormat("yyyyMMdd:HHmmss");
		return formatter.format(new Date());
	}

	/**
	 * 格式日期字符串（标准格式：yyyy-MM-dd）
	 * 
	 * @param date 日期
	 * @return 日期字符串
	 */
	public static String formatStandardDateNYR(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(STANDARD_DATE_FORMAT_NYR);
		return sdf.format(date);
	}

	/**
	 * 格式日期字符串（标准格式：yyyy年MM月dd日）
	 * 
	 * @param date 日期
	 * @return 日期字符串
	 */
	public static String formatStandardDateNYR(String dateTimeStr)
	{
		try
		{
			Date date = parseStandardDateTime(dateTimeStr);
			return formatStandardDateNYR(date);
		}
		catch (ParseException e)
		{
			return "";
		}
	}

	/**
	 * 获取日期标识： 与传入的日期比较，如果日期属于本周则返回0，属于上周则返回1，属于两周前则返回2
	 * @param date 比较的日期
	 * @return
	 */
	public static int getDateFlag(Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date thisWeekStart = null;
		Date lastWeekStart = null;
		try
		{
			String strThisWeek = DateUtil.formatStandardDate(cal.getTime());
			thisWeekStart = DateUtil.parseStandardDateTime(strThisWeek + " 00:00:00");
			cal.add(Calendar.WEEK_OF_YEAR, -1);
			String strLastWeek = DateUtil.formatStandardDate(cal.getTime());
			lastWeekStart = DateUtil.parseStandardDateTime(strLastWeek + " 00:00:00");
		}
		catch (Exception e)
		{
			return 2;
		}
		if(date.after(thisWeekStart))
			return 0;
		else if(date.after(lastWeekStart))
			return 1;
		else
			return 2;
	}
}
