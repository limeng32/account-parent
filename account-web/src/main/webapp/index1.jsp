<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>本页面用来测试kissy框架</title>
<script src="//g.alicdn.com/kissy/k/5.0.1/seed.js"
	data-config="{combine:true}"></script>
</head> 
<body>
	<input type="button" value="articleJson" id="articleJson">
	<script>  
		require.config({
		    packages: [
		        {
		            name: "mypkg",
		            tag: "201505242056",
		            path: "${resourceRoot}/js/kissy/module", 
		            combine : false,
		            charset: "utf-8"
		        }
		    ]
		});
		require([ 'node', 'io', 'json',  'util', 'mypkg/jsonx' ], function($, IO, JSON,  UTIL, JSONX) {
			IO.get('index/testMix?_content=json', {
			}, function(data) {
				console.log(JSONX.decode(data)[0].name);
			}, 'text')
		})
	</script>
</body>
</html>