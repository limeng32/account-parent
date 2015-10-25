<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>daily构筑demo1</title>
<meta charset="UTF-8">
<script
	src="//g.alicdn.com/kissy/k/5.0.1/??seed-debug.js,import-style.js"
	data-config="{combine:true}"></script>
<script>
    	require.config({
            packages: [
                {
                    name: 'bee-demo',
                    base: '../build',
                    tag: "201509061150",
                    ignorePackageNameInUri: true,
                    combine: false
                }
            ]}
        );
    	require.config({
		    packages: [
		        {
		            name: "mypkg",
		            tag: "201509061150",
		            path: "${resourceRoot}/js/kissy/module", 
		            combine : false,
		            charset: "utf-8"
		        }
		    ]
		});
    </script>


<script>
    	modulex.importStyle('bee-demo/index.css');
    	modulex.importStyle('bee-demo/oa-basic.css');
    	modulex.importStyle('bee-demo/oa-button.css');
    	modulex.importStyle('bee-demo/oa-menu.css');
    	modulex.importStyle('bee-demo/oa-menu-markable.css');
    	modulex.importStyle('bee-demo/article/demoTable/demoTable.css');
    </script>
</head>
<body>

<header></header>

<article></article>

<script>
	var a = 'aaa';
	modulex.use('bee-demo/index');
</script>

</body>
</html>