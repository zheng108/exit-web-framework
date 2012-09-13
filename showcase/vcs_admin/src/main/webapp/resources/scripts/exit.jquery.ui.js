(function($){
	
	$.extend($.fn,{
		selection:function(index){

			var me = $(this);
			me.hide();
			var selectionWidget = $('<div class="selection_container"></div>'),
			selection = $('<div class="selection up"></div>'),
			selectionUl = $('<ul></ul>').css({"z-index":index}),
			emptyText = me.attr("emptyText") || "",
			search=$.isEmpty(me.attr("search")) ? false : me.attr("search").booleanValue(), 
			multiSelect=$.isEmpty(me.attr("multiple")) ? false : me.attr("multiple").booleanValue("multiple"),
			notReslutText = me.attr("notresult") || "没有任何记录",
			itemBox = $('<li class="item_box"></li>').css({height:me.attr("height") || '100%'}),
			itemUl = $("<ul></ul>");
			
			var width = ((me.attr("size") * 1) || 10) * 7;
			
			if ($.browser.msie) {
				width += 27;
			} else  if ($.browser.mozilla) {
				width += 23;
				selectionUl.css({width:width - 2});
			} else {
				width += 28;
				selectionUl.css({width:width - 2});
			}
			
			me.parent().append(selectionWidget);
			
			selectionWidget.css({
				width:width
			});
			
			selectionWidget.append(selection);
			selectionWidget.append(selectionUl);
			selection.append('<div class="text"></div>').append('<div class="trigger_up"></div>');
			selectionUl.css({width:width - 2});
			selectionUl.append(itemBox);
			itemBox.append(itemUl);
			
			selection.find("div.text").css({
				width:width - selection.find("div.trigger_up").width() - 15
			});
			
			var setSelectedValue = function(){
				
				var selected = me.find("option[selected='selected']"),
				result="";
				
				if (multiSelect) {
					var array = new Array();
					$.each(selected,function(i,o){
						array.push($(o).text().replace(/[\r\n]/g,"").replace(/(^\s*)|(\s*$)/g, ""));
					});
					result = array.join(",");
				} else {
					result = $(selected).text().replace(/[\r\n]/g,"").replace(/(^\s*)|(\s*$)/g, "");
				}
				
				var t = selection.find("div.text");
				
				if ($.isNotEmpty(result)){
					t.removeClass("empty").text(result.ellipsis(t.width() / 8));
				} else{
					t.addClass("empty").text(emptyText.ellipsis(t.width() / 8));
				}
				
			};
			
			if (me.find("option").length <= 0) {
				itemUl.append("<li>" + notReslutText + "</li>");
			} else {
				$.each(me.find("option"),function(i,o){
					var li = $('<li></li>').attr("evalue",$(o).val());
					if ($.browser.msie) {
						li.css({"line-height":"1.5"});
					}
					if (multiSelect) {
						var checker = $('<span class="checker"></span>');
						
						checker.hover(function(){
							$(this).addClass("hover");
						},function(){
							$(this).removeClass("hover");
						});
						
						if (($.isEmpty($(o).attr("selected")) ? false : $(o).attr("selected").booleanValue("selected"))) {
							checker.addClass("click");
						}
						
						li.appendTo(itemUl);
						li.prepend(checker);
					} else {
						li.appendTo(itemUl);
					}
					
					li.append($(o).text().replace(/[\r\n]/g,"").replace(/(^\s*)|(\s*$)/g, ""));
				});
			}
			
			if (search && selectionUl.find("li").length > 0) {
				
				selectionUl.prepend('<li class="search"><input class="text_search_big" style="width:'+ (width - 47)+'px;"/></li>');
				selectionUl.find("input").keydown(function(){
					
					var li = selectionUl.find("li:gt(0)");
					li.show();

					var filter = function(){
						selectionUl.find("li.not_reslut").remove();
						var value = $(this).val(),
						flag = false;
						if ($.isEmpty(value)) {
							return ;
						}
						$.each(li,function(i,v){
							var me = $(v);
							var text = me.text();
							if (text.indexOf(value) > 0) {
								flag = true;
							} else {
								me.hide();
							}
							
						});
						if (!flag) {
							selectionUl.append('<li class="not_reslut">'+notReslutText+'</li>');
						}
					};
					
					filter.defer(500,this);
				});
			}

			setSelectedValue();
			
			var li = selectionUl.find("li.item_box li");
			
			li.hover(
				function(){
					$(this).addClass("over");
				},
				function(){
					$(this).removeClass("over");
				}
			).click("click",function(){

				var value = $.isEmpty(this.attributes["evalue"]) ? "" : this.attributes["evalue"].nodeValue;
				
				if ($.isEmpty(me.attr("multiple")) || !me.attr("multiple").booleanValue("multiple")) {
					me.find("option:selected").removeAttr("selected");
					
					if ($.isNotEmpty(value)) {
						me.find("option[value='"+value+"']").attr("selected", true);
						me.find("option:contains("+value+")").attr("selected", true);
					}
					
					selection.removeClass().addClass("selection up");
					selection.find("div.trigger_down").attr("class","trigger_up");
					selectionUl.hide();
					setSelectedValue();
				} else {
					
					var checker = $(this).find(".checker");
					
					if (checker.hasClass("click")) {
						checker.removeClass("click");
					} else {
						checker.addClass("click");
					}
					
					if ($.isNotEmpty(value)) {
						var op = $(me.find("option[value='"+value+"']")) || $(me.find("option:contains("+value+")"));
						var flag = $.isEmpty(op.attr("selected")) ? true : !op.attr("selected").booleanValue("selected");
						op.attr("selected", flag);
					} 
					
					setSelectedValue();
				}
				
			});
			
			selection.click(function(){
				if (selection.hasClass("selection up")) {
					selection.attr("class","selection down");
					selection.find("div.trigger_up").attr("class","trigger_down");
					selectionUl.show();
				} else {
					selection.attr("class","selection up");
					selection.find("div.trigger_down").attr("class","trigger_up");
					selectionUl.hide();
				}
				
			});
			
			me.parents("form").bind("reset",setSelectedValue);
			
			me.attr("execState",true);
			
		},
		
		checker:function(){
			
			var me = $(this);
			me.hide();
			
			var checker = me.attr("type") == "radio" ? $('<span class="radio"></span>') : $('<span class="checker"></span>');
			checker.appendTo(me.parent());
			me.appendTo(checker);
			
			checker.hover(function(){
				checker.addClass("hover");
			},function(){
				checker.removeClass("hover");
			});
			
			if ($.isNotEmpty(me.attr("disabled")) && me.attr("disabled").boolean("disabled")) {
				checker.addClass("disable");
				return ;
			}
			if (me.is(':checked')) {
				checker.addClass("click");
			}
			
			me.bind("click",function(o){
				if ($(o.target).is(':checked')) {
					checker.addClass("click");
				} else {
					checker.removeClass("click");
				}
				return false;
			});
			
			checker.bind("mousedown.checker",function(){
				me.attr("checked",!me.is(':checked'));
				me.click();
				return false;
			});
			
			me.attr("execState",true);
		},
		
		mask:function(){
			var me = $(this);
			var w = me.width(),
			h = me.height();
			var html = '<div class="el_mask" style="width:'+w+'px; height:'+h+'px;"><div class="el_mask_loading_img"></div></div>';
			me.prepend(html);
		},
		
		unmask:function(){
			var me = $(this);
			me.find("div.el_mask").remove();
		},
		
		dialog:function(){
			var me = $(this);
			
			var url = me.attr("href"),
			modal = $.isEmpty(me.attr("modal")) ? false : me.attr("modal").booleanValue(),
			meWidth = me.attr("width") || "auto",
			meHieght = me.attr("height") || "auto",
			title = me.attr("title") || "",
			id = me.attr("dialogId") || "",
			icon = me.attr("icon") || "";
			
			var loading = $('<div class="el_mask_loading_img"></div>'),
			maskWidget = $('<div class="el_mask_widget" id="'+id+'"></div>').css({width:meWidth}),
			dialogWidget = $('<div class="dialog_widget"></div>'),
			dialogClose = $('<div class="close"></div>'),
			elMask = $('<div class="el_mask"></div>').css({left:0,top:0}),
			dialogTitle = $('<div class="dialog_title"></div>').append('<span class="'+icon+'">' + title + '</span>'),
			dialogContent = $('<div class="dialog_content"></div>').css({height:meHieght});

			dialogTitle.append(dialogClose);
			
			var targetEl = $(url);
			var hasTarget = targetEl.length > 0, isRender = $.isEmpty(targetEl.attr("render")) ? false  : targetEl.attr("render").booleanValue();
			
			if (hasTarget && !isRender) {
				targetEl.hide();
			}
			
			var close = function(){
				
				dialogContent.find("input").poshytip('destroy');
				
				if (hasTarget) {
					hideTargetDialog();
				} else {
					maskWidget.remove();
					elMask.remove();
				}
			};
			
			var center = function(){
				maskWidget.css({
					left :($(window).width() - dialogWidget.outerWidth()) / 2,
					top : ($(window).height() - dialogWidget.outerHeight()) / 2
				});
			};
			
			var render = function(el){
				loading.remove();
				
				maskWidget.append(dialogWidget);
				
				dialogWidget.append(dialogTitle);
				dialogWidget.append(dialogContent);
				
				maskWidget.prependTo(el || "body");
				
				center();
			};
			
			var showTargetDialog = function(){
				maskWidget.show();
				targetEl.show();
				center();
			};
			
			var hideTargetDialog = function(){
				maskWidget.hide();
				targetEl.hide();
			};
			
			var initialize = function(){
				
				if (hasTarget && isRender) {
					showTargetDialog();
					return false;
				}
				
				dialogClose.hover(function(){
					$(this).addClass("hover");
				},function(){
					$(this).removeClass("hover");
				}).on("click",close);
				
				var maskAll = function(){
					elMask.width($(window).outerWidth());
					elMask.height($(window).outerHeight());
				};
				
				maskWidget.easydrag();
				maskWidget.setHandler(dialogTitle);
				
				maskWidget.ondrag(function(){
					dialogContent.find("input").poshytip('hide');
				});
				
				if (modal) {
					elMask.append(loading);
					elMask.prependTo("body");
					elMask.css({top:$(window).scrollTop()});
					maskAll();
				} else {
					loading.prependTo("body");
				}
				
				if (hasTarget) {
					render(targetEl.parent());
					dialogContent.append(targetEl);
					targetEl.show();
					targetEl.attr("render","true");
					isRender = true;
				} else {
					dialogContent.load(url,function(){
						render();
					});
				}
				
				$(window).resize(maskAll);
				$(window).scroll(function () {
					elMask.css({top:$(window).scrollTop()});
				});
				return false;
			};
			me.attr("execState",true);
			return $(this).off("click").on("click",initialize);
		}
	});
	
	var initializeUi = function(){
		
		$(":input[placeholder]").placeholder();
		
		var select = $("select[class='selection'][execstate!='true']");
		
		$.each(select,function(i,s){
			$(s).selection(select.length - i);
		});
		
		$.each($("input[type='checkbox'][execstate!='true'].checker"),function(i,c){
			$(c).checker();
		});
		
		$.each($("input[type='radio'][execstate!='true'].checker"),function(i,r){
			$(r).checker();
		});
		
		$.each($("a[target='dialog'][execstate!='true']"),function(i,a){
			$(a).dialog();
		});
		
	};
	
	var destroyUi = function(){
		
		$.each($("*[class='tip-yellow']"),function(i,t) {
			$(t).remove();
		});
	};

	$(document).ajaxStop(initializeUi);
	$(document).ajaxStart(destroyUi);
	$(document).ready(initializeUi);
	
})(jQuery);

