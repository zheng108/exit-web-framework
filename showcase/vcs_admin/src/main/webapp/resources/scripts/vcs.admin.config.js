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
	
		
		$(".selectAll").click(function(){
			$(this).parents("table").find("input[type='checkbox']").attr('checked', $(this).is(':checked'));
		});
	};
	
	$(document).ajaxStop(initTheView);
	initTheView();
	
});

var loadResource = function(li){
	var i = 0;
}