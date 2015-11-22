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
        var base = '${resourceRoot }/build';
        KISSY.config({
            packages: [
                {
                    name: 'account-front',
                    base: base,
                    tag:  '2015112102',
                    ignorePackageNameInUri: true,
                    combine:false
                }
            ]}
        );
    </script>


    <script>
    KISSY.importStyle('account-front/signUpWrapper.css,kg/auth/2.0.6/plugin/msgs/style.css');
    </script>
</head>
<body>

<header>

</header>

<article>

</article>

<script>
KISSY.use('account-front/signUpWrapper');
</script>
</body>
</html>