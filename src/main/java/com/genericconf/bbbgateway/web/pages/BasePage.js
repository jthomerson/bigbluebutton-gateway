$(document).ready(function(){
});
$(window).load(function(){
	$('.feedbackPanel li.ui-state-error').prepend('<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .2em;"></span>');
	$('.feedbackPanel li.ui-state-highlight').prepend('<span class="ui-icon ui-icon-info" style="float: left; margin-right: .2em;"></span>');
	
	var rtHeight = $('#rightbar').height();
	var ltHeight = $('#main-content').height();
	
	if (rtHeight > ltHeight) {
		var diff = rtHeight - ltHeight;
		var orig = $('#main-content-body').height();
		$('#main-content-body').height(orig + diff);
	} else if (ltHeight > rtHeight) {
		var diff = ltHeight - rtHeight;
		var orig = $('#rightbar .last-widget .ui-widget-content').height();
		$('#rightbar .last-widget .ui-widget-content').height(orig + diff);
	}
	
});
