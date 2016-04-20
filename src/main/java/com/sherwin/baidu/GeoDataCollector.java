package com.sherwin.baidu;

import java.io.IOException;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GeoDataCollector {

	private final static String ak = "Y2VZjezayyyUmDmIQWf5Xik7p6gpvuGg";

	private final static String hostName = "api.map.baidu.com";

	private static final String fixedParam = "&output=json&ak=" + ak;

	/**
	 * {@literal 根据关键字从百度地图api检索资源地理信息及详情，作为法治地图的初始数据}
	 * 
	 * @author ssh
	 * @param city
	 * @param keyWords
	 * @return
	 * @throws IOException
	 */
	public String getResouceInfo(String city, String keyWords,String page_num) throws IOException {
		String encodedCity = URLEncoder.encode(city, "UTF-8");
		String ecKW = URLEncoder.encode(keyWords, "UTF-8");
		StringBuffer serviceUrl = new StringBuffer();
		serviceUrl.append("/place/v2/search?").append("query=").append(ecKW).append("&").append("region=")
				.append(encodedCity).append("&").append("scope=1").append("&page_size=10").append("&page_num=").append(page_num).append(fixedParam);
		return  invoke(serviceUrl.toString());
	}
	
	public String getResouceDetailInfo(String uid) throws IOException{
		StringBuffer serviceUrl = new StringBuffer();
		serviceUrl.append("/place/v2/detail?").append("uid=").append(uid).append("&").append("scope=2").append(fixedParam);
		
		return  invoke(serviceUrl.toString());
	}
	
	private String invoke(String serviceUrl) throws IOException{
		MultiThreadedHttpClient httpClient = new MultiThreadedHttpClient(hostName, 80, null, null);
		String response = httpClient.executeHttpGet(serviceUrl.toString());
		return  response;
	}
	
	public static void main(String[] args){
		GeoDataCollector gdc=new GeoDataCollector();
		try {
			String jo=gdc.getResouceInfo("深圳", "人民调解委员会","0");
			System.out.println(jo);
//			System.out.println(gdc.getResouceDetailInfo("4058d684edf5050bfaf0cb85"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
