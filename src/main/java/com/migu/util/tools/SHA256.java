
package com.migu.util.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * <Simple description> <Function described in detail>
 */
public class SHA256
{
	private static SHA256 sha = new SHA256();
	private MessageDigest messageDigest;

	private SHA256()
	{
		try
		{
			this.messageDigest = MessageDigest.getInstance("SHA-256");
		}
		catch (Exception e)
		{
			this.messageDigest = null;
		}
	}

	/**
	 * 加密实现 <Function described in detail>
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String source) throws Exception
	{
		return toHexString(encrypt(source.getBytes()));
	}

	public String decrypt(String source , byte[] raw) throws Exception
	{
		return null;
	}

	public byte[] decrypt(byte[] source) throws Exception
	{
		return decrypt(source, new byte[0]);
	}

	public byte[] decrypt(byte[] source , byte[] raw) throws Exception
	{
		return new byte[0];
	}

	public synchronized byte[] encrypt(byte[] source) throws Exception
	{
		this.messageDigest.update(source);
		return this.messageDigest.digest();
	}

	public byte[] encrypt(byte[] source , byte[] raw) throws Exception
	{
		return encrypt(source);
	}

	public byte[] decrypt(byte[] source , byte[] raw , byte[] algParaSpec) throws Exception
	{
		return new byte[0];
	}

	public byte[] encrypt(byte[] source , byte[] raw , byte[] algParaSpec) throws Exception
	{
		return encrypt(source);
	}

	public String decrypt(String source)
	{
		return null;
	}

	public String encrypt(String source , byte[] raw) throws Exception
	{
		return toHexString(encrypt(source.getBytes(), raw));
	}

	public static String toHexString(byte[] buf)
	{
		if((buf == null) || (buf.length < 1))
		{
			return null;
		}
		StringBuilder strbuf = new StringBuilder(buf.length * 2);
		for(int i = 0 ; i < buf.length ; ++i)
		{
			if((buf[i] & 0xFF) < 16)
			{
				strbuf.append('0');
			}
			strbuf.append(Long.toString(buf[i] & 0xFF, 16));
		}
		return strbuf.toString().toUpperCase(Locale.getDefault());
	}

	/**
	 * 加密算法实现 Base64(SHA256(明文))
	 * @param srcPwd 明文
	 * @return 密文
	 */
	public static String encryptS1B(String srcPwd)
	{
		byte[] tmpEncrypted = null;
		try
		{
			tmpEncrypted = SHA256.sha.encrypt(srcPwd.getBytes());
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
			return StringUtils.EMPTY;
		}
		byte[] encrypted = null;
		try
		{
			encrypted = Base64.encodeBase64(tmpEncrypted);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
			return StringUtils.EMPTY;
		}
		try
		{
			return new String(encrypted, "8859_1");
		}
		catch (UnsupportedEncodingException e)
		{
			return StringUtils.EMPTY;
		}
	}

	public static void main(String[] args)
	{
		System.out.println(SHA256.encryptS1B("admin12345620130204202401"));
	}
}
