<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>法治资源查询</title>
</head>
<script language="javascript" src="http://code.jquery.com/jquery-1.7.2.js"></script>
<body>
	请输入查询关键字：
	<input id="keyword"></input>
	<button id='query' type="button">查询</button>
	<table>
		<tr>
			<th>名称</th>
			<th>位置</th>
			<th>地址</th>
			<th>street_id</th>
			<th>uid</th>
		</tr>
	</table>
	<div id='result'></div>
</body>
<script type="text/javascript">
<!--文档加载 -->
	$(function() {
		$("#query").click(function() {
			htmlobj = $.ajax({
				url : "/weixin/geodata?keyword=" + $("#keyword").val(),
				async : false
			});
			$("#result").html(htmlobj.responseText);
		});

	});
</script>
</html>