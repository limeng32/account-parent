define('bee-demo/dataFilter', [], function(require, exports, module) {
	exports.init = function() {
		var conditionArray = new Array();
		var _filter = function(p) {
			var ret = {};
			if (p.pageSize != null) {
				ret.pageSize = p.pageSize;
			}
			if (p.pageNo != null) {
				ret.pageNo = p.pageNo;
			}
			for (var i = 0; i < conditionArray.length; i++) {
				var _temp = conditionArray[i].handler(conditionArray[i].node);
				if (_temp != null) {
					ret[conditionArray[i].id] = _temp;
				}
			}
			return ret;
		};
		var _add = function(condition) {
			conditionArray.push(condition);
		};
		var _ret = {
			filter : function(p) {
				return _filter(p);
			},
			add : function(condition) {
				_add(condition);
				return this;
			}
		};
		return _ret;
	};
});