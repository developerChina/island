package main;

import java.util.Properties;
import utils.LoadPropertyUtil;

public class VehicleMain {
	public static void main(String[] args) {
		Properties properties = LoadPropertyUtil.loadPropertyFile("system.properties");
		String eparkIp=(String) properties.get("eparkIp");
		int unIsland=Integer.valueOf((String) properties.get("unIsland"));
		
		VehicleIdent.Dev_Bash_Init_1(eparkIp,unIsland);
		while (true);
	}
}
