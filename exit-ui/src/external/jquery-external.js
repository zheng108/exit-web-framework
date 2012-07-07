(function($){
	$.extend(jQuery,{
	
		apply : function(o, c, defaults) {
			if(defaults){
				$.apply(o, defaults);
			}
			if(o && c && typeof c == 'object'){
				for(var p in c){
					o[p] = c[p];
				}
			}
			return o;
		},
		
		applyIf : function(o, c){
			if(o){
				for(var p in c){
					if(!$.isDefined(o[p])){
						o[p] = c[p];
					}
				}
			}
			return o;
		},
		
		invoke : function(arr, methodName){
			var ret = [],
				args = Array.prototype.slice.call(arguments, 2);
			$.each(arr, function(i,v) {
				if (v && typeof v[methodName] == 'function') {
					ret.push(v[methodName].apply(v, args));
				} else {
					ret.push(undefined);
				}
			});
			return ret;
		},
		
		escapeRe : function(s) {
			return s.replace(/([-.*+?^${}()|[\]\/\\])/g, "\\$1");
		},
		
		override : function(origclass, overrides){
			if(overrides){
				var p = origclass.prototype;
				$.apply(p, overrides);
				if($.browser.msie && overrides.hasOwnProperty('toString')){
					p.toString = overrides.toString;
				}
			}
		},
		
		isEmpty : function(v, allowBlank){
			return v === null || v === undefined || (($.isArray(v) && !v.length)) || (!allowBlank ? v === '' : false);
		},
		
		isNotEmpty : function(v, allowBlank){
			return !$.isEmpty(v, allowBlank);
		},
		
		isDate : function(v){
			return toString.apply(v) === '[object Date]';
		},
	
		isObject : function(v){
			return !!v && Object.prototype.toString.call(v) === '[object Object]';
		},
	
		isString : function(v){
			return typeof v === 'string';
		},
		
		isElement : function(v) {
			return v ? !!v.tagName : false;
		},
		
		isDefined : function(v){
			return typeof v !== 'undefined';
		},
		
		isHide:function(el){
			return $(el).css("display") === "none" || $(el).css("visibility") === "hidden";
		},
		
		number:function(v,defaultValue){
			v = Number($.isEmpty(v) || $.isArray(v) || typeof v == 'boolean' || (typeof v == 'string' && v.trim().length == 0) ? NaN : v);
			return isNaN(v) ? defaultValue : v;
		},
		
		value : function(v, defaultValue, allowBlank){
			return $.isEmpty(v, allowBlank) ? defaultValue : v;
		},
		
		ellipsis : function(value, len, word) {
			if (!$.isString(value)){
				return value;
			}
			if (value && value.length > len) {
				if (word) {
					var vs    = value.substr(0, len - 2),
						index = Math.max(vs.lastIndexOf(' '), vs.lastIndexOf('.'), vs.lastIndexOf('!'), vs.lastIndexOf('?'));
					if (index == -1 || index < (len - 15)) {
						return value.substr(0, len - 3) + "...";
					} else {
						return vs.substr(0, index) + "...";
					}
				} else {
					return value.substr(0, len - 3) + "...";
				}
			}
			return value;
		}
	});
})(jQuery)