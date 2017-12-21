package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.core.utils.PropertyUtils;

public class LoadPropertyUtil {

	public static void main(String[] args) {
		Properties properties = loadPropertyFile("system.properties");
		System.out.println(properties.get("unIsland"));

	}

	public static Properties loadPropertyFile(String fullFile) {
		Properties properties = new Properties();
		try {
			InputStream inStream = PropertyUtils.class.getClassLoader().getResourceAsStream(fullFile);
			properties.load(inStream);
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
