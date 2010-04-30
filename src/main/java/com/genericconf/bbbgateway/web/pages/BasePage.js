/*
 * Copyright 2010 Generic Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

function initializeFormStuff(prependSelector) {
	$(prependSelector + 'fieldset legend').button();
	$(prependSelector + 'input[type=submit]').button();
	$(prependSelector + '.feedbackPanel li.ui-state-error').prepend('<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .2em;"></span>');
	$(prependSelector + '.feedbackPanel li.ui-state-highlight').prepend('<span class="ui-icon ui-icon-info" style="float: left; margin-right: .2em;"></span>');
}

$(document).ready(function(){
	$(".dataTables").dataTable({
		"bPaginate": false,
		"bLengthChange": false,
		"bFilter": false,
		"bSort": true,
		"bInfo": false,
		"bAutoWidth": true,
		"bJQueryUI": true,
		"sPaginationType": "full_numbers"
	});

	initializeFormStuff('');

	// equalize heights:
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
