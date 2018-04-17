
package com.migu.util.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 一些别的配置项
 * @author luchenxi
 *
 *         2018年3月30日
 */
@ Data
@ NoArgsConstructor
@ AllArgsConstructor
public class SystemConfig
{
	String userInfo_FilePath;
	String userInfo_FileName;
	String playlistInfo_FilePath;
	String playlistInfo_FileName;
}
