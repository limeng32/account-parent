<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=10" >
	<script src="//g.alicdn.com/kissy/k/1.4.8/??seed-min.js,import-style.js" data-config="{combine:true}"></script>
	<script type="text/javascript" src="account/kissyuploader/5.0.0/build/index.js"></script>
	<script type="text/javascript" src="core/build/jsonx/jsonx.js"></script>
	<link rel="stylesheet" href="//g.alicdn.com/kissy/k/1.4.8/??css/dpl/base-min.css,css/dpl/forms-min.css,button/assets/dpl-min.css">
	<%@include file="initKissy.jsp" %>
</head>
<body>

<header>

</header>

<article>

</article>

<script>
var token = '${accountToken }';
KISSY.use('account-front/editUserWrapper');
</script>
</body>
</html>