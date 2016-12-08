$(function() {
	$("#pageWrapper").height($(window).height()-10);
	$("#content").height($("#pageWrapper").height()-280);
	$("#contentLeft").height($("#pageWrapper").height()-280);
	$("#contentCenter").height($("#pageWrapper").height()-290);
	
	$("#bookIndex").height($("#contentLeft").height()-170);
});