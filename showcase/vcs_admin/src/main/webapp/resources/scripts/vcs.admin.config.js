$(document).ready(function(){
	
	$('#navigation ul li:has(ul),#account ul li:has(ul)').hover(
		function() {
			window.clearTimeout($(this).attr("call"));
			$(this).children("ul").fadeIn("normal");
		},
		function() {
			var liObj=$(this);
			function hide(){
				liObj.children("ul").stop(true,true).fadeOut("normal");
			}
			liObj.attr("call",window.setTimeout(function(){hide();},200));
		}
	);
	
	var initTheView = function(){
		
		$('table tbody tr').hover(
			function() {
				$(this).addClass("tbody_over");
			},
			function() {
				$(this).removeClass("tbody_over");
			}
		);
		
		$("table tbody tr").click(function(){
			$("table tbody tr.tbody_click").removeClass("tbody_click");
			$(this).addClass("tbody_click");
		});
	
	};
	
	$.validator.setDefaults({
		onfocusin:function(element, event){
			$(element).poshytip('show');
		},
		onfocusout: function(element, event) {
			if (this.check(element)) {
				$(element).poshytip('destroy');
			} else {
				$(element).poshytip('hide');
			}
		},
		showErrors:function(errorMap, errorList){
			$.each(errorList,function(i,e) {
				$(e.element).poshytip('destroy');
				$(e.element).poshytip({
					content:e.message,
					showOn: 'none',
					alignTo: 'target',
					alignX: 'inner-left',
					offsetX: 0,
					offsetY: 5,
					fade: false,
					slide: false
				});
				$(e.element).poshytip('show');
			});
		}
	});
	
	$(document).ajaxStop(initTheView);
	
	initTheView();
	
	$(document).ajaxError(function(event,request, settings){
	     alert("系统错误");
	     window.location.reload();
	});
	
});