(function($){
	//基础样式
	var baseClasses = "ui-button ui-state-default ui-corner-all",
	//状态样式
	stateClasses = "ui-state-hover ui-state-active ui-state-focus",
	//按钮类型样式
	typeClasses = "ui-button-text-only ui-button-icon-only ui-button-text",
	//如果button在form里面并且是reset类型的button时，用该方法重置表单
	formResetHandler = function(){
		var buttons = $( this ).find( ":ui-button" );
		buttons.button( "refresh" );
	};
	
	$.widget( "ui.button",{
		options:{
			icon:null
		},
		/**
		*
		* 实现父类方法创建ui方法，
		*
		**/
		_create:function(){
			
			var type = this.getElementType();
			//如果该element不是按钮也不是input也不是a标签就什么都不创建
			if (type == null){
				return ;
			}
			
			//如果button在form里面并且是reset类型的button时，用该方法重置表单
			this.element.closest( "form" )
			.unbind( "reset.button" )
			.bind( "reset.button", formResetHandler );
			
			//将禁用属性设置到该el的属性中
			this.element.attr( "disabled", this.options.disabled );
			
			this.options.label = this.element.html();
			
			var hoverClass = "ui-state-hover",
			    focuClass = "ui-state-focus",
				activeClass = "ui-state-active",
				options = this.options;
			
			/*
			*
			* 添加样式和监听元素事件，
			* 1.当鼠标经过与离开的时添加ui-state-hover样式或者移除ui-state-hover样式。事件名:mouseenter.button、mouseleave.button
			* 2.当鼠标左键按下与放开时添加ui-state-active样式或者移除ui-state-active。事件名:mousedown.button、mouseup.button
			* 3.当键盘按下空格键获取回车键与放开键盘按钮时添加ui-state-active样式或者移除ui-state-active。
			* 事件名:mousedown.button、mouseup.button
			*
			*/
			this.element
				.addClass(baseClasses)
				.attr( "role", "button" )
				.bind( "mouseenter.button", function() {
					if ( options.disabled ) {
						return;
					}
					$( this ).addClass( hoverClass );
				})
				.bind( "mouseleave.button", function() {
					if ( options.disabled ) {
						return;
					}
					$( this ).removeClass( hoverClass );
				})
				.bind( "click.button", function( event ) {
					if ( options.disabled ) {
						event.preventDefault();
						event.stopImmediatePropagation();
					}
				})
				.bind( "mousedown.button", function() {
					if ( options.disabled ) {
						return false;
					}
					$( this ).addClass( activeClass );
				})
				.bind( "mouseup.button", function() {
					if ( options.disabled ) {
						return false;
					}
					$( this ).removeClass( activeClass );
				})
				.bind( "keydown.button", function(event) {
					if ( options.disabled ) {
						return false;
					}
					if ( event.keyCode == $.ui.keyCode.SPACE || event.keyCode == $.ui.keyCode.ENTER ) {
						$( this ).addClass( activeClass );
					}
				})
				.bind( "keyup.button", function() {
					$( this ).removeClass( activeClass );
				});
			//如果该元素是a标签时候，按下回车和空格键时，触发该元素的click事件
			if ( this.getElementType() == "a" ) {
				this.element.keyup(function(event) {
					if ( event.keyCode === $.ui.keyCode.SPACE || event.keyCode == $.ui.keyCode.ENTER ) {
						$( this ).click();
					}
				});
			}
			
			/**
			*
			* 由于ie6/7下button一添加border时会多出inline border问题，为了解决该问题
			* 添加一个sapn来做border样式来保外着button做边框，会生成以下HTML代码。
			* 
			* <span class="ui-button-ie-border"><button><button></span>
			*
			**/
			if((this.getElementType() == "button" || this.getElementType() == "input") && 
			$.browser.msie && ($.browser.version == "6.0" || $.browser.version == "7.0")) {
				this.element.wrap('<span></span>');
				
			}
			
			//设置是否禁用
			this._setOption( "disabled", this.options.disabled );
			
			//重置按钮
			this.resetButton();
		},
		/**
		*
		* 重置按钮样式
		* 最终生成的html为:
		*
		* 1.不带icon的button标签html：
		* <button class="ui-button ui-state-default ui-corner-all">
		*	<span class="ui-button-text ui-button-text-only"> 文本 </span>
		* </button>
		*
		* 2.带icon的button标签html：
		* <button class="ui-button ui-state-default ui-corner-all">
		*	<span class="ui-button-text ui-button-icon-only user_icon"> 文本 </span>
		* </button>
		*
		* 3.不带icon的a标签html：
		* <a class="ui-button ui-state-default ui-corner-all">
		*	<span class="ui-button-text ui-button-text-only"> 文本 </span>
		* </a>
		*
		* 4.带icon的a标签html：
		* <a class="ui-button ui-state-default ui-corner-all">
		*	<span class="ui-button-text ui-button-icon-only user_icon"> 文本 </span>
		* </a>
		*
		**/
		resetButton:function(){
			//如果该元素为input标签，不能加任何icon只能是单纯的问题和背景图片
			if ( this.getElementType() === "input" ) {
				if ( this.options.label ) {
					this.element.val( this.options.label );
				}
				return;
			}
			
			//添加<span>标签来控制文本
			var buttonText = $( "<span></span>", this.element[0].ownerDocument )
				.addClass( "ui-button-text" )
				.html( this.options.label )
				.appendTo( this.element.empty()),
			icon = this.options.icon;
			
			if ($.isNotEmpty(icon)) {
				buttonText.addClass("ui-button-icon-only " + icon);
			} else {
				buttonText.addClass("ui-button-text-only")
			}
		},
		/**
		*
		* 获取元素标签名称
		*
		**/
		getElementType:function(){
			
			if (this.element.is("input")){
				return "input";
			} else if (this.element.is("a")){
				return "a";
			} else if (this.element.is("button")){
				return "button";
			} else {
				return null;
			}
			
		},
		/**
		*
		* 重写父类方法，如果该ui被销毁时，删除所有css把原来的样貌还原
		*
		**/
		destroy: function() {
			
			this.element
				.removeClass( baseClasses + " " + stateClasses)
				.removeAttr( "role" )
				.removeAttr( "aria-pressed" )
				.html( this.element.find(".ui-button-text").html() );
			$.Widget.prototype.destroy.call( this );
		},
		/**
		*
		* 重写父类方法，当父类添加完css后将该元素的disabled标志更改
		*
		**/
		_setOption: function( key, value ) {
			$.Widget.prototype._setOption.apply( this, arguments );
			
			if ( key === "disabled" ) {
				if ( value ) {
					this.element.propAttr( "disabled", true );
					if((this.getElementType() == "button" || this.getElementType() == "input") && 
						$.browser.msie && ($.browser.version == "6.0" || $.browser.version == "7.0")) {
						this.element.parent("span").removeClass("ui-button-ie-border").addClass("ui-button-ie-state-disabled");
					}
					if (this.getElementType() == "a" && $.browser.msie && ($.browser.version == "6.0" || $.browser.version == "7.0")) {
						this.element.removeClass("ui-button-ie-border").addClass("ui-button-ie-state-disabled");
						
					}
				} else {
					this.element.propAttr( "disabled", false );
					if((this.getElementType() == "button" || this.getElementType() == "input") && 
						$.browser.msie && ($.browser.version == "6.0" || $.browser.version == "7.0")) {
						this.element.parent("span").removeClass("ui-button-ie-state-disabled").addClass("ui-button-ie-border");
					}
					if (this.getElementType() == "a" && $.browser.msie && ($.browser.version == "6.0" || $.browser.version == "7.0")) {
						this.element.removeClass("ui-button-ie-state-disabled").addClass("ui-button-ie-border");
						
					}
				}
				return;
			}
		}
	});
})(jQuery);