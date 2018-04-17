
package com.migu.util.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * httpclient调用时一些加密配置
 * @author luchenxi
 *
 *         2018年3月30日
 */
@ Data
@ NoArgsConstructor
@ AllArgsConstructor
public class RemoteConfig
{
	String password;
	String appid;
	String account;
	String channelid;
	String appkey;
	String createuser_address;
	String updateuser_address;
	String createPlayList_address;
	String oprPlayList_address;
	String oprTag_address;
}
