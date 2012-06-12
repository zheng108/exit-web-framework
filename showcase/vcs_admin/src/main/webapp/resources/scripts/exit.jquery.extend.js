// JavaScript Document
(function($) {
	
	$.extend(jQuery,{
		
		apply:function(o, c, defaults) {
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
		
		applyIf:function(o, c){
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
        
        isEmpty: function(v, allowBlank){
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
        maskLoad:function(config){
			$.applyIf(config,{
				url:'#',
				urlCharReplaceEmpty:'',
				removeFirst:false,
				target:'',
				param:{},
				callback:function(){}
			});
			
			$(config.target).mask();
			var url = config.url.replace(config.urlCharReplaceEmpty,"");
			if (config.removeFirst) {
				url = url.substring(1,url.length);
			}
			
			$.get(url,config.param,function(data){
				$(config.target).unmask();
				$(config.target).empty().append(data);
				config.callback.call(this,data);
			});
			
		},
        form:{
        	resetForm:function(form){
        		$(form).resetForm();
        	},
        	getParameters:function(form,notEmptyValue){
        		var param = {};
        		
    			$.each($(form).serializeArray(),function(i,o){
        			if (notEmptyValue && $.isNotEmpty(o.value)) {
        				param[o.name] = o.value;
        			}
        			
        			if (!notEmptyValue) {
        				param[o.name] = o.value;
        			}
        		});
        		
        		return param;
        	},
        	submitMaskForm:function(form,option){
            	if ($.isNotEmpty(option.promptMsg)) {
            		var c = confirm(option.promptMsg);
            		if (c == false) {
            			return ;
            		}
            	}
            	$.applyIf(option,{
            		maskEl:"",
            		error:function(){
            			$(option.maskEl).unmask();
            			if ($.isFunction(option.errorSuccess)) {
            				option.errorSuccess(responseText, statusText, xhr, $form);
            			}
            		},
            		success:function(responseText, statusText, xhr, $form){
            			$(option.maskEl).unmask();
            			if ($.isFunction(option.submitSuccess)) {
            				option.submitSuccess(responseText, statusText, xhr, $form);
            			}
            		}
            	});
            	
            	if (option.maskEl) {
            		$(option.maskEl).mask();
            	}
            	
            	var me = $(form);
            	
            	me.ajaxForm(option);
            	
            	me.submit();
            }
        },
        ellipsis : function(value, len, word) {
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
	
	$.applyIf($.fn,{
		
		isHide:function(){
			return $.isHide(this);
		}
	
	});
	
	$.applyIf(Function.prototype, {
	    createInterceptor : function(fcn, scope){
	        var method = this;
	        return !$.isFunction(fcn) ?
	                this :
	                function() {
	                    var me = this,
	                        args = arguments;
	                    fcn.target = me;
	                    fcn.method = method;
	                    return (fcn.apply(scope || me || window, args) !== false) ?
	                            method.apply(me || window, args) :
	                            null;
	                };
	    },
	    
	    createCallback : function(){
	        var args = arguments,
	            method = this;
	        return function() {
	            return method.apply(window, args);
	        };
	    },
	    
	    createDelegate : function(obj, args, appendArgs){
	        var method = this;
	        return function() {
	            var callArgs = args || arguments;
	            if (appendArgs === true){
	                callArgs = Array.prototype.slice.call(arguments, 0);
	                callArgs = callArgs.concat(args);
	            }else if ($.isNumeric(appendArgs)){
	                callArgs = Array.prototype.slice.call(arguments, 0);
	                var applyArgs = [appendArgs, 0].concat(args);
	                Array.prototype.splice.apply(callArgs, applyArgs); 
	            }
	            return method.apply(obj || window, callArgs);
	        };
	    },
	
	    defer : function(millis, obj, args,  appendArgs){
	        var fn = this.createDelegate(obj, args, appendArgs);
	        if(millis > 0){
	            return setTimeout(fn, millis);
	        }
	        fn();
	        return 0;
	    }
	});
	
	$.applyIf(String.prototype, {
	
		messageFormat : function(){
			var args = [];
			
			for(var i = 0 ; i < arguments.length ; i++){
				args.push(arguments[i]);
			}
			
	        return this.replace(/\{(\d+)\}/g, function(m, i){
	            return args[i];
	        });
	    },
	    toggle:function(value, other){
	        return this.toString() == value ? other : value;
	    },
	    leftPad : function (size, ch) {
	    	var val = this.toString();
	        var result = String(val);
	        if(!ch) {
	            ch = " ";
	        }
	        while (result.length < size) {
	            result = ch + result;
	        }
	        return result;
	    },
	    ellipsis:function(len, word){
	    	var value = this.toString();
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
	    },
	    booleanValue:function(compareValue){
	    	var value = this.toString();
	    	if ($.isNotEmpty(value) && (value === "true" || value === "1" || ($.isNotEmpty(compareValue) || value == compareValue))) {
	    		return true;
	    	}
	    	
	    	return false;
	    }
	});

	$.applyIf(Array.prototype, {

	    indexOf : function(o, from){
	        var len = this.length;
	        from = from || 0;
	        from += (from < 0) ? len : 0;
	        for (; from < len; ++from){
	            if(this[from] === o){
	                return from;
	            }
	        }
	        return -1;
	    },
	    
	    remove : function(o){
	        var index = this.indexOf(o);
	        if(index != -1){
	            this.splice(index, 1);
	        }
	        return this;
	    }
	});
	
})(jQuery);