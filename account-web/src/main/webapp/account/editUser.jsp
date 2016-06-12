<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta charset="UTF-8">
	<script src="//g.alicdn.com/kissy/k/1.4.8/??seed-min.js,import-style.js" data-config="{combine:true}"></script>
	<link rel="stylesheet" href="//g.alicdn.com/kissy/k/1.4.8/??css/dpl/base-min.css,css/dpl/forms-min.css,button/assets/dpl-min.css">
	<script>
		var base = '${resourceRoot }/account/build';
		var debug = '${kissy.debug}';
		if (KISSY.config('debug')) {
			base = 'http://localhost:5555/account-front'
		}
		KISSY.config({
			packages : [ {
				name : 'account-front',
				base : base,
				tag : '${kissy.tag.account}',
				ignorePackageNameInUri : true,
				combine : false
			} ]
		});
	</script>
	<script src="//cdn.bootcss.com/html5shiv/r29/html5.min.js"></script>
    <script>
    KISSY.importStyle('account-front/editUserWrapper.css,kg/uploader/6.2.7/themes/imageUploader/style.css');
    </script>
</head>
<body>

<header>

</header>

<article>

</article>

<script>
KISSY.use('account-front/editUserWrapper');
</script>
</body>
</html>