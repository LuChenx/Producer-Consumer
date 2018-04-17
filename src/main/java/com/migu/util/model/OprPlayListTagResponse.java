
package com.migu.util.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author luchenxi
 *
 *         2018年4月4日
 */
@ Data
@ NoArgsConstructor
@ AllArgsConstructor
public class OprPlayListTagResponse
{
	/**
	 * 结果码
	 */
	protected String code;
	/**
	 * 结果描述
	 */
	protected String info;
	/**
	 * 歌单ID
	 */
	private String   playListId;
	/**
	 * 成功添加的歌曲个数
	 */
	private String   successNum;
	/**
	 * 失败的ID
	 */
	private String   failTagIds;
}
