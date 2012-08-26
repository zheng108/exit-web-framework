
(function($) {
	/**
	 * 对Jquery扩张方法
	 */
	$.extend(jQuery,{
		
		/**
		 * 重c参数中复制所有配置属性到o参数中
		 * 
		 * @param o 被复制的对象
		 * @param c 要复制的对象
		 * @param defaults 如果存在不同对象是，将该对象也复制到o参数中
		 */
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
		
		/**
		 * 如果o参数对象没有该属性时，重c参数中复制所有配置属性到o参数中。
		 * 
		 * @param o 被复制的对象
		 * @param c 要复制的对象
		 */
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
		
		/**
		 * 在一个数组中遍历调用方法，返回调用方法后的新数组,
		 * <code>
		 * 	$.invoke(Ext.query("p"), "getAttribute", "id");
		 *  返回 [el1.getAttribute("id"), el2.getAttribute("id"), ..., elN.getAttribute("id")]
		 * </code>
		 * 
		 * @param arr 待调用方法的数组
		 * @param methodName 要执行的方法名称
		 */
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
        
        /**
         * 重写某个类，当方法名相同时候会覆盖当前类的方法，当方法名不存在时，将会添加到该类中，属性也一样
         * 
         * <code>
         * 	$.override(MyClass, {
		 *	    newMethod1: function(){
		 *	        // etc.
		 *	    },
		 *	    newMethod2: function(foo){
		 *	        // etc.
		 *	    }
		 *	});
         * </code>
         * 
         * @param origclass 要重写的类
         * @param overrides 重写方法或者属性的对象
         */
        override : function(origclass, overrides){
            if(overrides){
                var p = origclass.prototype;
                $.apply(p, overrides);
                if($.browser.msie && overrides.hasOwnProperty('toString')){
                    p.toString = overrides.toString;
                }
            }
        },
        
        /**
         * 判断一个值或对象是否为空
         * 
         * @param v 要判断的值
         * @param allowBlank true 为允许字符串为''值
         */
        isEmpty: function(v, allowBlank){
            return v === null || v === undefined || (($.isArray(v) && !v.length)) || (!allowBlank ? v === '' : false);
		},
		
		/**
         * 判断一个值或对象是否不为空
         * 
         * @param v 要判断的值
         * @param allowBlank true 为允许字符串为''值
         */
		isNotEmpty : function(v, allowBlank){
			return !$.isEmpty(v, allowBlank);
		},
		
		/**
         * 判断一个值是否日期类(Date)型
         * 
         * @param v 要判断的值
         */
        isDate : function(v){
            return toString.apply(v) === '[object Date]';
        },
        
        /**
         * 判断一个值是否对象([object:object])类型
         * 
         * @param v 要判断的值
         */
        isObject : function(v){
            return !!v && Object.prototype.toString.call(v) === '[object Object]';
        },

        /**
         * 判断一个值是否String类型
         * 
         * @param v 要判断的值
         */
        isString : function(v){
            return typeof v === 'string';
        },
        
        /**
         * 判断一个值是否HTML元素
         * 
         * @param v 要判断的值
         */
        isElement : function(v) {
            return v ? !!v.tagName : false;
        },
        
        /**
         * 判断一个值是否已经定义
         * 
         * @param v 要判断的值
         */
        isDefined : function(v){
            return typeof v !== 'undefined';
        },
        
        /**
         * 判断一个HTML是否为隐藏状态
         * 
         * @param v 要判断的值
         */
        isHide:function(el){
        	return $(el).css("display") === "none" || $(el).css("visibility") === "hidden";
        },
        
        /**
         * 判断一个值是否是整型，如果是返回当前值，如果不是返回defaultValue参数的值
         * 
         * @param v 要判断的值
         * @param 如果不是整型将要返回的值
         */
        number:function(v,defaultValue){
        	v = Number($.isEmpty(v) || $.isArray(v) || typeof v == 'boolean' || (typeof v == 'string' && v.trim().length == 0) ? NaN : v);
            return isNaN(v) ? defaultValue : v;
        },
        
        /**
         * 如果v参数值为空，返回defaultValue参数值。
         * 
         * @param v 值
         * @param defaultValue 如果值为空将要返回的值
         * @param allowBlank true表示允许值为''值
         */
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
			
			$.post(url,config.param,function(data){
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
            		validate:true,
            		error:function(){
            			$(option.maskEl).unmask();
            			if ($.isFunction(option.errorCall)) {
            				option.successFn(responseText, statusText, xhr, $form);
            			}
            		},
            		success:function(responseText, statusText, xhr, $form){
            			$(option.maskEl).unmask();
            			if ($.isFunction(option.successCall)) {
            				option.successCall(responseText, statusText, xhr, $form);
            			}
            		}
            	});
            	
            	var me = $(form);
            	
            	if (option.validate) {
            		me.validate({
            			submitHandler: function(f) {
            				if (option.maskEl) {
                        		$(option.maskEl).mask();
                        	}
            				$(f).ajaxSubmit(option);
            			}
            		});
            	} else {
            		if (option.maskEl) {
                		$(option.maskEl).mask();
                	}
            		me.ajaxForm(option);
            	}

        		me.submit();
        		
            }
        },

	    /**
	     * 如果value参数值超过指定长度,截断字符串并添加一个省略号(“…”)到结束的位置中
	     * 
	     * <code>
	     * $.ellipsis("123456789",5)
	     * 返回:12345...
	     * </code>
	     * 
	     * @param len 要截取的长度
	     * 
	     */
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
		
		/**
		 * 判断当前对象是否为隐藏状态
		 */
		isHide:function(){
			return $.isHide(this);
		}
	
	});
	
	/**
	 * 对JS Function类扩展
	 */
	$.applyIf(Function.prototype, {
		/**
		 * 对某个参数创建一个拦截器方法，如果拦截器方法返回false时，将不会调用要执行的方法，如果返回true时调用要执行的方法
		 * <code>
		 * 	var sayHi = function(name){
		 *	    alert('Hi, ' + name);
		 *	}
		 *	
		 *	sayHi('Fred'); // alerts "Hi, Fred"
		 *	
		 *	// 创建拦截器
		 *	// 如果name==Brian后在执行sayHi方法
		 *	var sayHiToFriend = sayHi.createInterceptor(function(name){
		 *	    return name == 'Brian';
		 *	});
		 *	
		 *	sayHiToFriend('Fred');  // no alert
		 *	sayHiToFriend('Brian'); alerts "Hi, Brian"
		 * </code>
		 * 
		 * @param fcn 拦截器方法
		 * @param scope 拦截器方法的作用域
		 */
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
	    
	    /**
	     * 通过arguments[0], arguments[1], arguments[2],创建一个callback方法来将参数传递到要执行的方法中
	     * 
	     * <code>
	     * 	var sayHi = function(name){
		 *	    alert('Hi, ' + name);
		 *	}
		 *	
		 *	
		 *	sayHi.createCallback('Fred') //alerts "Hi, Fred"
	     * </code>
	     */
	    createCallback : function(){
	        var args = arguments,
	            method = this;
	        return function() {
	            return method.apply(window, args);
	        };
	    },
	    
	    /**
	     * 创建一个委托回调，将obj作为当前方法的作用域去执行方法体里的代码，可以通过args参数来对该方法的要接受的参数进行传递
	     * 
	     * <code>
	     * 	var sayHi = function(name){
		 *	    alert('Hi, ' + name);
		 *	}
		 *	
		 *	
		 *	sayHi.createDelegate(window,["Fred"]) //alerts "Hi, Fred"
	     * </code>
	     * 
	     * @param obj 作用域
	     * @param args 传递的参数
	     * @param appendArgs 如果为true args参数将不会覆盖原有方法的参数值
	     */
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
	
	    /**
	     * 类似setTimeout方法，该方法执行时间的同时，可以把方法的作用域以及参数传递的方法体中
	     * 
	     * <code>
	     * var sayHi = function(name){
		 *	    alert('Hi, ' + name);
		 *	}
		 *	
		 *	// 执行方法:
		 *	sayHi('Fred');
		 *	
		 *	// 两秒中后执行方法:
		 *	sayHi.defer(2000, this, ['Fred']);
	     * </code>
	     * 
	     * @param millis 等待多用毫秒执行该方法
	     * @param obj 作用域
	     * @param args 要传递的参数
	     * @param appendArgs  如果为true args参数将不会覆盖原有方法的参数值
	     * 
	     */
	    defer : function(millis, obj, args,  appendArgs){
	        var fn = this.createDelegate(obj, args, appendArgs);
	        if(millis > 0){
	            return setTimeout(fn, millis);
	        }
	        fn();
	        return 0;
	    }
	});
	
	/**
	 * 重写String类属性
	 */
	$.applyIf(String.prototype, {
	
		/**
		 * 通过占位符格式化字符串
		 * <code>
		 * "1{0}3{1}5".messageFormat("2","4");
		 * 返回:12345
		 * </code>
		 * 
		 */
		messageFormat : function(){
			var args = [];
			
			for(var i = 0 ; i < arguments.length ; i++){
				args.push(arguments[i]);
			}
			
	        return this.replace(/\{(\d+)\}/g, function(m, i){
	            return args[i];
	        });
	    },
	    
	    /**
	     * 如果value等于当前值，返回当前值，反则返回other参数的值
	     */
	    toggle:function(value, other){
	        return this.toString() == value ? other : value;
	    },
	    /**
	     * 如果当前值的长度小于size参数的长度是,将当前值从左边开始补位.
	     * 
	     * <code>
	     * 	"123".leftPad(5,"00");
	     * 返回："00123"
	     * </code>
	     * 
	     * @param size 要判断的长度
	     * @param ch 要不为的字符
	     */
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
	    
	    /**
	     * 如果当前值超过指定长度,截断字符串并添加一个省略号(“…”)到结束的位置中
	     * 
	     * <code>
	     * "123456789".ellipsis(5)
	     * 返回:12345...
	     * </code>
	     * 
	     * @param len 要截取的长度
	     * 
	     */
	    ellipsis:function(len, word){
	    	
            return $.ellipsis(this.toString(),len,word);
	    },
	    
	    /**
	     * 将当前值转换为boolean类型
	     * 
	     * <code>
	     * 	"1".booleanValue() 返回:true
	     *  "0".booleanValue() 返回false
	     *  "true".booleanValue() 返回 true
	     *  "false".booleanValue() 返回 false
	     *  "abc".booleanValue("abc") 返回 true
	     *  "abc".booleanValue("aaa") 返回 false
	     * </code>
	     * 
	     * @param compareValue 自定义比对值
	     */
	    booleanValue:function(compareValue){
	    	var value = this.toString();
	    	return $.isNotEmpty(value) && (value === "true" || value === "1" || ($.isNotEmpty(compareValue) || value == compareValue));
	    }
	    
	});

	/**
	 * 扩张数组属性
	 */
	$.applyIf(Array.prototype, {

		/**
		 * 在当前数组中遍历寻找o对象，如果相等返回当前所在数组的未知，返回返回-1
		 * 
		 * @o 要遍历寻找的对象
		 * @from 从第几位开始遍历
		 */
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
	    
	    /**
	     * 从当前数组中删除对象
	     * 
	     * @param o 要删除的对象
	     */
	    remove : function(o){
	        var index = this.indexOf(o);
	        if(index != -1){
	            this.splice(index, 1);
	        }
	        return this;
	    }
	});
	
})(jQuery);