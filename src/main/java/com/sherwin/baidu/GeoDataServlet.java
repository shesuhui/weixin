package com.sherwin.baidu;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class GeoDataServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
		String kw=req.getParameter("keyword");
		if(StringUtils.isNotBlank(kw)){
			GeoDataCollector gdc=new GeoDataCollector();
			String ret= gdc.getResouceInfo("深圳", kw, "0");
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("applicaton/html");
			OutputStream ps =resp.getOutputStream();
			ps.write(ret.getBytes("UTF-8"));
			ps.flush();
			
		}
		
	}
}
