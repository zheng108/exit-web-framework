(function($){
	
	$.extend($.fn,{
		selection:function(index){
			
			var me = $(this);
			me.hide();
			
			var selectionWidget = $('<div class="selection_container">'),
			selection = $('<div class="selection up">'),
			selectionUl = $('<ul>').css({"z-index":index});
			emptyText = me.attr("emptyText") || "",
			execState = me.attr("execState"),
			search=$.isEmpty(me.attr("search")) ? false : me.attr("search").booleanValue(), 
			multiSelect=$.isEmpty(me.attr("multiple")) ? false : me.attr("multiple").booleanValue("multiple"),
			notReslutText = me.attr("notresult") || "找不到任何记录";
			
			if (execState) {
				return ;
			}
			
			var width = ((me.attr("size") * 1) || 10) * 7;
			
			if ($.browser.msie) {
				width += 27;
			} else  if ($.browser.mozilla) {
				width += 23;
			} else {
				width += 28;
			}
			
			me.parent().append(selectionWidget);
			
			selectionWidget.css({
				width:width
			});
			
			selectionWidget.append(selection);
			selectionUl.css({width:width - 2});
			var itemBox = $('<div class="item_box">').css({height:me.attr("height") || 'auto'});
			selectionUl.append(itemBox);
			
			var initDefaultSelectItem = function(){
				
				var selected = me.find("option[selected='selected']");
				if (multiSelect) {
					var array = new Array();
					$.each(selected,function(i,o){
						array.push($(o).text());
					});
					emptyText = array.join(",");
				} else {
					emptyText = $(selected).text();
				}
			};
			
			if (me.find("option").length <= 0) {
				itemBox.append("<li>" + "没有任何记录" + "</li>");
			} else {
				var array = new Array();
				$.each(me.find("option"),function(i,o){
					if (multiSelect) {
						var box = $('<input class="checkbox" type="checkbox" name="'+me.attr("name")+'" value="'+$(o).attr("value")+'"/>');
						box.on("click",function(){
							$(this).attr('checked', !$(this).is(':checked'));
						});
						
						var li = $('<li>'+$(o).text()+'</li>');
						itemBox.append(li);
						li.prepend(box);
						
						if ($(this).attr("selected") == "selected") {
							box.attr('checked', true);
							array.push($(this).text());
						}
						
					} else {
						emptyText = $(o).text();
					}
				});
				if (array.length > 0) {
					emptyText = array.join(",");
				}
			}
			
			selection.append('<div class="empty_text">'+emptyText+'</div>').append('<div class="trigger_up">');
			selectionWidget.append(selectionUl);
			
			var li = selectionUl.find("li:not(:input)");
			
			if (search && selectionUl.find("li").length > 0) {
				
				selectionUl.prepend('<li><input class="text_search_small" style="width:'+ (width - 47) +'px;"/></li>');
				selectionUl.find("input").keydown(function(){
					selectionUl.find("li.not_reslut").remove();
					var li = selectionUl.find("li:gt(0)");
					li.show();

					var filter = function(){
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
			
			li.hover(
				function(){
					$(this).addClass("over");
				},
				function(){
					$(this).removeClass("over");
				}
			).click("click",function(){
				if ($.isEmpty(me.attr("multiple")) || !me.attr("multiple").booleanValue("multiple")) {
					var t = $(this).text();
					selection.find("div.empty_text").text(t);
					me.find("option:selected").removeAttr("selected");
					$.each(me.find("option"),function(i,v){
						var o = $(v);
						if(o.text() === t) {
							o.attr("selected", true);
						}
					});
					selection.removeClass().addClass("selection up");
					selection.find("div.trigger_down").removeClass().addClass("trigger_up");
					selectionWidget.find("ul").hide();
				} else {
					var input = $(this).find("input");
					input.attr('checked', !input.is(':checked'));
					var array = new Array();
					$.each(selectionUl.find("li:has(input:checked)"),function(i,v){
						array.push($(v).text());
					});
					selection.find("div.empty_text").text(array.join(","));
				}
				
			});
			
			selection.click(function(){
				if (selection.hasClass("selection up")) {
					selection.removeClass().addClass("selection down");
					selection.find("div.trigger_up").removeClass().addClass("trigger_down");
					selectionWidget.find("ul").show();
				} else {
					selection.removeClass().addClass("selection up");
					selection.find("div.trigger_down").removeClass().addClass("trigger_up");
					selectionWidget.find("ul").hide();
				}
				
			});
			
			me.parents("form").bind("reset",initDefaultSelectItem);
			
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
			var hasTarget = targetEl.length > 0,targetIsRender = false;
			if (hasTarget) {
				targetEl.hide();
			}
			
			var close = function(){
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
			
			var render = function(){
				loading.remove();
				
				maskWidget.append(dialogWidget);
				
				dialogWidget.append(dialogTitle);
				dialogWidget.append(dialogContent);
				
				maskWidget.prependTo("body");
				
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
				
				if (hasTarget && targetIsRender) {
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
				
				if (modal) {
					elMask.append(loading);
					elMask.prependTo("body");
					maskAll();
				} else {
					loading.prependTo("body");
				}
				
				if (hasTarget) {
					render();
					dialogContent.append(targetEl);
					targetEl.show();
					targetIsRender = true;
				} else {
					dialogContent.load(url,render);
				}
				
				$(window).resize(maskAll);
				
				return false;
			};
			
			return $(this).off("click").on("click",initialize);
		}
	});

	var initializeUi = function(){
		var select = $("select[class='selection']");
		$.each(select,function(i,s){
			$(s).selection(select.length - i);
		});
		$.each($("a[target='dialog']"),function(i,a){
			$(a).dialog();
		});
	};
	
	$(document).ajaxStop(initializeUi);
	
	$(document).ready(initializeUi);
	
})(jQuery);