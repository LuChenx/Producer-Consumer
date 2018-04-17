
package com.migu.util.httpclient;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.migu.util.model.RemoteConfig;
import com.migu.util.model.UserInfo;
import com.migu.util.tools.DateUtil;
import com.migu.util.tools.SHA256;
import com.migu.util.tools.SpringContextHolder;

/**
 * http调用工具类
 * @author luchenxi
 *
 *         2018年3月29日
 */
public class HttpClientHolder
{
	static Logger logger = LogManager.getLogger(HttpClientHolder.class);

	@ SuppressWarnings ("deprecation")
	public static String doPostWithBody(UserInfo user) throws Exception
	{
		HttpClient httpclient = new HttpClient();
		RemoteConfig config = SpringContextHolder.getBean("remoteconfig");
		PostMethod post = new PostMethod(config.getCreateuser_address());
		post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		post.setRequestBody(getParam(user));
		post.setRequestHeader("Authorization", getAuthorizationStr());
		httpclient.executeMethod(post);
		String info = new String(post.getResponseBody(), "utf-8");
		//解析XML
		System.out.println("return:" + info);
		logger.info(post.toString());
		return xmlAnalysis(info);
	}

	public static String doPostWithParam(String url , List<NameValuePair> nvps) throws Exception
	{
		String result = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
		CloseableHttpResponse response = httpclient.execute(httpPost);
		if(null != response.getStatusLine() && 200 == response.getStatusLine().getStatusCode())
		{
			result = EntityUtils.toString(response.getEntity());
			EntityUtils.consume(response.getEntity());
		}
		else if(null != response.getStatusLine() && 200 != response.getStatusLine().getStatusCode())
		{
			System.err.println("Http调用失败！" + String.valueOf(response.getStatusLine().getStatusCode()));
		}
		else
		{
			System.err.println("Http调用失败！未知异常");
		}
		return result;
	}

	private static String xmlAnalysis(String xml)
	{
		String uid = "";
		Document doc = null;
		try
		{
			doc = DocumentHelper.parseText(xml);
			// 获取根节点
			Element rootElt = doc.getRootElement();
			String retuncode = rootElt.elementTextTrim("resultCode");
			if(retuncode.equals("0"))
			{
				//TODO 记录失败
				Element registerRsp = rootElt.element("registerRsp");
				uid = registerRsp.elementTextTrim("identityID");
			}
		}
		catch (Exception e)
		{
			System.err.println("xml解析失败");
			System.out.println("---------------");
			e.printStackTrace();
		}
		return uid;
	}

	public static String getAuthorizationStr()
	{
		String address = "";
		try
		{
			address = InetAddress.getLocalHost().getHostAddress().toString();
		}
		catch (Exception e)
		{
			System.err.println("IP获取失败");
			e.printStackTrace();
		}
		String nonce = getRundom(6);
		String created = DateUtil.getCurDateStr(DateUtil.SHORT_DATETIME_FORMAT);
		RemoteConfig config = SpringContextHolder.getBean("remoteconfig");
		String sourcestr = config.getPassword() + nonce + created;
		String password = SHA256.encryptS1B(sourcestr);
		StringBuilder sb = new StringBuilder();
		sb.append("Basic appid=\"").append(config.getAppid()).append("\"");
		sb.append(", account=\"").append(config.getAccount()).append("\"");
		sb.append(", password=\"").append(password).append("\"");
		sb.append(", nonce=\"").append(nonce).append("\"");
		sb.append(", created=\"").append(created).append("\"");
		sb.append(", userip=\"").append(address).append("\"");
		sb.append(", appkey=\"").append(config.getAppkey()).append("\"");
		String channelid = "Ios_migu".equals(config.getChannelid()) ? "4" : "3";
		sb.append(", channelid=\"").append(channelid).append("\"");
		return sb.toString();
	}

	/**
	 * 产生X位的随机数
	 * @param x
	 * @return
	 */
	private static String getRundom(int x)
	{
		StringBuilder sb = new StringBuilder();
		for(int y = 0 ; y < x ; y++)
		{
			sb.append(RandomUtils.nextInt(0, 10));
		}
		return sb.toString();
	}

	private static String getParam(UserInfo userInfo)
	{
		StringBuffer xmlParam = new StringBuffer("<?xml version='1.0' encoding='utf-8'?>");
		xmlParam.append("<register><registerReq><accountInfoList><accountInfo>");
		xmlParam.append("<accountName>" + UUID.randomUUID().toString() + "</accountName>");
		xmlParam.append("<accountType>" + 1 + "</accountType>");
		xmlParam.append("<verified>" + 1 + "</verified>");
		xmlParam.append("</accountInfo></accountInfoList>");
		xmlParam.append("<userInfo><ext><item>");
		xmlParam.append("<key>smallavatar</key>");
		xmlParam.append("<value>" + userInfo.getSmallavatar() + "</value></item><item>");
		xmlParam.append("<key>nickname</key>");
		xmlParam.append("<value>" + userInfo.getUserName() + "</value>");
		xmlParam.append("</item></ext></userInfo>");
		xmlParam.append("</registerReq></register>");
		System.out.println("param:" + xmlParam);
		return xmlParam.toString();
	}
}
