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
		String kw = req.getParameter("keyword");
		String page_num=req.getParameter("pageno");
		if(StringUtils.isBlank(page_num)) page_num="0";
		if (StringUtils.isNotBlank(kw)) {
			GeoDataCollector gdc = new GeoDataCollector();
			String ret = gdc.getResouceInfo("深圳", kw,page_num);
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("applicaton/html");
			OutputStream ps = resp.getOutputStream();
			ps.write(ret.getBytes("UTF-8"));
			ps.flush();
		}

	}

}
