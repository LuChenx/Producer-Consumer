
package com.migu.util.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author luchenxi
 *
 *         2018年4月2日
 */
@ Data
@ NoArgsConstructor
@ AllArgsConstructor
public class CreatePlayListResponse
{
	/**
	 * 成功创建返回ID
	 */
	private String   playListId;
	/**
	 * 结果码
	 */
	protected String code;
	/**
	 * 结果描述
	 */
	protected String info;
}
