<script>
    	var base = '${resourceRoot }/account/build';
	   	var debug = '${kissy.debug}';
    	if(debug == 'true' && KISSY.config('debug')){
	       base = 'http://localhost:5555/account-front'
	    }
    	KISSY.config({
            packages: [
                {
                    name: 'account-front',
                    base: base,
                    tag:  '${kissy.tag.account}',
                    ignorePackageNameInUri: true,
                    combine:false
                }
            ]}
        );
    </script>