/*
*author:Null
*DATE:2013.5.24
*/

$(function(){		   
	//头页登录
	$("#navul > li").not(".navhome").hover(function(){
		$(this).addClass("navmoon")
	},function(){
		$(this).removeClass("navmoon")
	});
	
});