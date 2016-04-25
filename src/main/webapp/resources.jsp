<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>法治资源查询</title>
</head>
<!-- script language="javascript"src="http://code.jquery.com/jquery-1.7.2.js"></script-->
<script language="javascript"src="js/jquery-1.7.2.js"></script>
<body>
	请输入查询关键字：
	<input id="keyword"  onkeydown="if(event.keyCode==13){$('#query').click();return false;}" ></input>
	<button id='query' type="button">查询</button>
	<div id='page'></div>
	<table>
		<tr>
			<th>名称</th>
			<th>位置</th>
			<th>地址</th>
			<th>street_id</th>
			<th>uid</th>
		</tr>
	</table>
	<pre id='result'></pre>
</body>
<script type="text/javascript">
<!--文档加载 -->
	$(function() {
		$("#query").click(function() {
			clearPageBtn();
			query($("#keyword").val(), '0', true);
		});

	});
	
	function clearPageBtn(){
		$('.pagenum').remove();
	}

	function aclick(num) {
		query($("#keyword").val(), num, false);
	}

	function query(keyword, page_num, firstLoad) {
		if (keyword && page_num>=0) {
			$.ajax({
				type : "GET",
				url : "/weixin/geodata?keyword=" + keyword + "&pageno="
						+ page_num,
				success : function(data) {
					$("#result").html(data);
					if (firstLoad) {
						jdata = eval("(" + data + ")");
						totalpage = jdata.total;
						pageAmount = Math.ceil(totalpage / 10);
						for (i = 0; i < pageAmount; i++) {
							num = i + 1;
							$('#page').append(
									"<button class='pagenum' onclick='aclick("
											+ i + ")'>" + num + "</button>");
						};
						$(".pagenume").each(function() {
							$(this).click(aclick($(this).val()))
						});
					}
				}
			});
		}
	}
</script>
</html>