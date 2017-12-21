package org.core.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 配置文件
 * 
 * @author junhu.sun
 *
 */
public class PropertyUtils {
	/**
	 * 默认配置文件
	 */
	public static final String DB = "db_server.properties";

	/**
	 * 文件加载Properties
	 * 
	 * @param fileName
	 * @return
	 */
	public static Properties loadPropertyFile(String fileName) {
		Properties properties = new Properties();
		try {
			InputStream inStream = PropertyUtils.class.getClassLoader().getResourceAsStream(fileName);
			properties.load(inStream);
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * key得到value值
	 * 
	 * @param fileName
	 * @param property
	 * @return
	 */
	public static String getProperty(String fileName, String property) {
		return loadPropertyFile(fileName).getProperty(property);
	}

	/**
	 * 得到所有的key
	 * 
	 * @param fileName
	 * @return
	 */
	public static List<String> getAllKeys(String fileName) {
		Properties prop = loadPropertyFile(fileName);
		List<String> keylist = new java.util.ArrayList<String>();
		for (Object object : prop.keySet()) {
			keylist.add(object.toString());
		}
		return keylist;
	}
    /**
     * 添加新的key-value值,或修改
     * @param fileName
     * @param key
     * @param value
     */
	public static void setProperty(String fileName, String key, String value) {
		URL fileUrl = PropertyUtils.class.getClassLoader().getResource(fileName);
		Properties prop = loadPropertyFile(fileName);
		OutputStream output = null;
		try {
			output = new FileOutputStream(fileUrl.toURI().toString().replace("file:", ""));
			prop.setProperty(key, value);
			prop.store(output, "andieguo modify" + new Date().toString());// 保存键值对到文件中
		} catch (Exception io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		List<String> alllines = getAllKeys(PropertyUtils.DB);
		for (String object : alllines) {
			System.out.println(object);
		}

		System.out.println(getProperty(PropertyUtils.DB, "url"));
		
		setProperty(PropertyUtils.DB, "sjh","888");
	}
}

