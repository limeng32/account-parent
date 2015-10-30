<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta charset="UTF-8">
	<script src="//g.alicdn.com/kissy/k/1.4.8/??seed-min.js,import-style.js" data-config="{combine:true}"></script>
    <script>
        var base = 'build';
        if(KISSY.config('debug')){
            base = 'http://localhost:5555/'
        }
        KISSY.config({
            packages: [
                {
                    name: 'account-front',
                    base: base,
                    ignorePackageNameInUri: true,
                    combine:false
                }
            ]}
        );
    </script>


    <script>
    KISSY.importStyle('account-front/index.css');
    </script>
</head>
<body>

<header>

</header>

<article>

</article>

<script>
KISSY.use('account-front/index');
</script>
</body>
</html>