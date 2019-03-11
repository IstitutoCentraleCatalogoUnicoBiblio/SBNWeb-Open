function treeElementHandler(elementId) {
	var hidden = document.getElementById(elementId + "_HIDDEN");
	var img = document.getElementById(elementId + "_IMG");
	var a = document.getElementById(elementId + "_A");
	var ul = document.getElementById(elementId + "_UL");
	if (hidden && img && a) {
		if (hidden.value == "true") {
			hidden.value = "false";
			img.src = img.src.replace("/opened.gif", "/closed.gif");
			a.title = "Espandi";
			if (ul) {
				ul.className = "closed";
			}
		} else {
			hidden.value = "true";
			img.src = img.src.replace("/closed.gif", "/opened.gif");
			a.title = "Chiudi";
			if (ul) {
				ul.className = "opened";
			}
		}
	}
}

function treeElementDeselect(elementId) {
	var label = document.getElementById(elementId);
	if (label) {
		label.className = null;
	}
}

window.onload = function() {
    if (!document.getElementsByTagName) return;
    var lis = document.getElementsByTagName("li");
    for (var i=0; i<lis.length; i++) {
        var li = lis[i];
        var id = li.getAttribute("id");
        if (id && id.indexOf("TREEELEMENT_")==0) {
        	var hidden = document.getElementById(id + "_HIDDEN");
    		var radio = document.getElementById(id + "_RADIO");
        	var label = document.getElementById(id + "_LABEL");
        	var a = document.getElementById(id + "_A");
        	var img = document.getElementById(id + "_IMG");
        	var ul = document.getElementById(id + "_UL");
        	if (a && img) {
	        	if (hidden) {
		        	//img.style.cursor = "pointer";
		        	if (hidden.value == "false") {
						img.src = img.src.replace("/none.gif", "/closed.gif");
						a.title = "Espandi";
			        	if (ul) {
			        		ul.className = "closed";
			        	}
		        	} else {
						img.src = img.src.replace("/none.gif", "/opened.gif");
						a.title = "Chiudi";
			        	if (ul) {
			        		ul.className = "opened";
			        	}
		        	}
		            a.onclick = function() {
		            	var elementId = this.id.substring(0,this.id.length-2);
		            	treeElementHandler(elementId);
		                return false;
		            }
	        	}
	    	}
        }
    }
}
