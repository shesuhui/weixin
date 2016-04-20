package com.sherwin.weixin;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MessageHandler implements Runnable {

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;

	public MessageHandler(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public void run() {
		StringBuffer sb = new StringBuffer();
		BufferedReader bufferedReader = null;
		try {
			request.setCharacterEncoding("UTF-8");
			bufferedReader = request.getReader();
			char[] charBuffer = new char[128];
			int bytesRead;
			while ((bytesRead = bufferedReader.read(charBuffer)) != -1) {
				sb.append(charBuffer, 0, bytesRead);
			}
			System.out.println(sb);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // new BufferedReader(new
			// InputStreamReader(inputStream));

	}

}
