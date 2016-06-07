<script>
	var base = '${resourceRoot }/account/build';
	var debug = '${kissy.debug}';
	if (debug == 'true' && KISSY.config('debug')) {
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