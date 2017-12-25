package main;

public class t {

	public static void main(String[] args) {
		String data ="1,京B02128,2017-12-24 21:37:24,1";
		if(!data.equals("") && data.split(",").length==4) {
		System.out.println("--------------"+Integer.parseInt(data.split(",")[0]));
		}
	}

}
