package com.sherwin.weixin;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public MyServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		String echostr = request.getParameter("echostr");
		if (echostr != null) {
			System.out.println(echostr);
			response.getWriter().write(echostr);
			response.getWriter().flush();
		}
		else{
			OutputStream ps = response.getOutputStream();  
			ps.write("参数不正确".getBytes("UTF-8"));
			ps.flush();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
        
		MessageHandler  mh=new MessageHandler(request, response);
		
		new Thread(mh).start();
		
		String res = "<xml><ToUserName><![CDATA[oHgoKw7AX0vYoGlJyySAc9cJnz-A]]></ToUserName><FromUserName><![CDATA[gh_617a79e3dda7]]></FromUserName><CreateTime>12345678</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[你好]]></Content></xml>";
//		response.getWriter().write("success");
		response.getOutputStream().write(res.getBytes("UTF-8"));
		response.getOutputStream().flush();
	}

}
