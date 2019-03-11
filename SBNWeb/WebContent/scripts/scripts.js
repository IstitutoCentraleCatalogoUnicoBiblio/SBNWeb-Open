
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
	if (init == true) {
		with (navigator) {
			if ((appName == "Netscape") && (parseInt(appVersion) == 4)) {
				document.MM_pgW = innerWidth;
				document.MM_pgH = innerHeight;
				onresize = MM_reloadPage;
			}
		}
	} else {
		if (innerWidth != document.MM_pgW || innerHeight != document.MM_pgH) {
			location.reload();
		}
	}
}

MM_reloadPage(true);

function goThere(loc, target) {
	var w = window.open(loc, target);
	if (w != null) {
		w.focus();
	}
}

function MM_openBrWindow(theURL, winName, features) { //v2.0
	window.open(theURL, winName, features);
}

function MM_goToURL() { //v3.0
	var i, args = MM_goToURL.arguments;
	document.MM_returnValue = false;
	for (i = 0; i < (args.length - 1); i += 2) {
		eval(args[i] + ".location='" + args[i + 1] + "'");
	}
}

function validateSubmit(field, value) {
	var form = document.forms[0];
	var len = form.elements.length;
	for (var i = 0; i < len; i++) {
		var current = form.elements[i];
		if (current.name == field) {
			current.value = value;
			// event hook: autoscroll dopo submit
			Cookies.set('scroll_y', window.pageYOffset);
			form.submit();
			return;
		}
	}
}

function navigate(field, value) {
	validateSubmit(field, value)
	return;
}	 

function insertAtCursor(myField, myValue) {
	if (document.selection) {
		// For browsers like Internet Explorer
		myField.focus();
		var sel = document.selection.createRange();
		sel.text = myValue;
		myField.focus();
	} else if (myField.selectionStart || myField.selectionStart == '0') {
		// For browsers like Firefox and Webkit based
		var startPos = myField.selectionStart;
		var endPos = myField.selectionEnd;
		var scrollTop = myField.scrollTop;
		myField.value = myField.value.substring(0, startPos) + myValue + myField.value.substring(endPos, myField.value.length);
		myField.focus();
		myField.selectionStart = startPos + myValue.length;
		myField.selectionEnd = startPos + myValue.length;
		myField.scrollTop = scrollTop;
	} else {
		myField.value += myValue;
		myField.focus();
	}
}

// almaviva5_20130212 evolutive google
function submitOnEnter(e, idButton) {
	// look for window.event in case event isn't passed in
	if (typeof e == 'undefined' && window.event) {
		e = window.event;
	}
	if (e.keyCode == 13) {
		document.getElementById(idButton).click();
	}
}


function setFocusOnLoad(id) {
	
	var f = function() {
		var elem = document.getElementById(id);
		if (elem != null) {
			elem.focus();
		}
	}
	
	if (window.addEventListener) {
		window.addEventListener('load', f, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', f);
	}	
}

/**
* This function helps to autocomplete the date format MMDDYYY
* Converts M to 0M and MMD to MM0D. Ex. `1/` to `01/`, `01/1/` to `01/01/`
* Adds slash for MM and MMDD Ex. `01` to `01/`, `01/02` to `01/02/`
* Converts YY to YYYY. Ex. `01/01/01` to `01/01/2001`
*
* @param {String} str
* @return {String}
*/
var autocompleteDateFormat = function (str) {
        str = str.trim();
        var matches, year,
                looksLike_MM_slash_DD = /^(\d\d\/)?\d\d$/,
                looksLike_MM_slash_D_slash = /^(\d\d\/)?(\d\/)$/,
                looksLike_MM_slash_DD_slash_DD = /^(\d\d\/\d\d\/)(\d\d)$/;
 
        if( looksLike_MM_slash_DD.test(str) ){
                str += "/";
        }else if( looksLike_MM_slash_D_slash.test(str) ){
                str = str.replace( looksLike_MM_slash_D_slash, "$10$2");
        }else if( looksLike_MM_slash_DD_slash_DD.test(str) ){
                matches = str.match( looksLike_MM_slash_DD_slash_DD );
                year = Number( matches[2] ) < 20 ? "20" : "19";
                str = String( matches[1] ) + year + String(matches[2]);
        }
        return str;
};

function wrapText(elementID, tag) {
    var textArea = $('#' + elementID);
    var len = textArea.val().length;
    var start = textArea[0].selectionStart;
    var end = textArea[0].selectionEnd;
    var selectedText = textArea.val().substring(start, end);
    var replacement = '[' + tag + ']' + selectedText + '[/' + tag + ']';
    textArea.val(textArea.val().substring(0, start) + replacement + textArea.val().substring(end, len));
    textArea.focus();
}

function isScrolledIntoView(elem) {
    if ($(elem).length == 0) {
        return false;
    }
    var docViewTop = $(window).scrollTop();
    var docViewBottom = docViewTop + $(window).height();

    var elemTop = $(elem).offset().top;
    var elemBottom = elemTop + $(elem).height();
    //  return ((elemBottom <= docViewBottom) && (elemTop >= docViewTop)); //try it, will only work for text
    return (docViewBottom >= elemTop && docViewTop <= elemBottom);
}

/**
 * JavaScript Get URL Parameter
 * 
 * @param String prop The specific URL parameter you want to retreive the value for
 * @return String|Object If prop is provided a string value is returned, otherwise an object of all properties is returned
 */
function getUrlParams(property) {
	var params = {};
	var search = decodeURIComponent(window.location.href.slice(window.location.href.indexOf('?') + 1));
	var definitions = search.split('&');

	definitions.forEach(function(val, key) {
		var parts = val.split('=', 2);
		params[parts[0]] = parts[1];
	});

	if (property) {
		return (property in params) ? params[property] : undefined;
	}

	return params;
}

function addTableRow(jQtable){
    jQtable.each(function(){
        var $table = $(this);
        // Number of td's in the last table row
        var n = $('tr:last td', this).length;
        var tds = '<tr>';
        for(var i = 0; i < n; i++){
            tds += '<td>&nbsp;</td>';
        }
        tds += '</tr>';
        if($('tbody', this).length > 0){
            $('tbody', this).append(tds);
        }else {
            $(this).append(tds);
        }
    });
};
