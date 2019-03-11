TreeView.stylesIncluded = false;

TreeView.ARROW_STYLE = 'arrow';

TreeView.TRIANGLE_STYLE = 'triangle';

TreeView.DEFAULT_STYLE = 'default';

TreeView.DEFAULT_COLOR = 'blue';

function TreeView (treeId, listStyle, color) {
	if (!listStyle || listStyle == TreeView.DEFAULT_STYLE) {
		this.expandText = '+';
		this.collapsedText = '-';
	} else if (listStyle == TreeView.ARROW_STYLE) {
		this.expandText = '&rarr;';
		this.collapsedText = '&darr;';
	} else if (listStyle == TreeView.TRIANGLE_STYLE) {
		this.expandText = '&#9658;';
		this.collapsedText = '&#9660;';
	}
	if (color) {
		this.color = color;
	} else {
		this.color = TreeView.DEFAULT_COLOR;
	}
	this.leafText = '&nbsp;';
	
	if (document.getElementById && this.expandText) {
		var treeNode = document.getElementById(treeId);
		if (treeNode) {
			if (!TreeView.stylesIncluded) {
				var cssText = 'ul.treeView,ul.treeView ul {margin:0;padding-left:2em} '+
					'ul.treeView li {list-style:none;white-space:nowrap} '+
					'ul.treeView li.closed ul {display:none} '+
					'ul.treeView li .list-item {font-family:monospace;text-decoration:none}';
				if (document.createStyleSheet) {
					var styleSheet = document.createStyleSheet();
					styleSheet.cssText = cssText;
				} else {
					var head = document.getElementsByTagName("head")[0];
					var styleSheet = document.createElement("style");
					styleSheet.type = "text/css";
					styleSheet.innerHTML = cssText;
					head.appendChild(styleSheet);
				}
				TreeView.stylesIncluded = true;
			}
			treeNode.className = treeNode.className + " treeView";
			this.initNode(treeNode);
		}
	}
}

TreeView.prototype.initNode = function(treeNode) {
	var items = treeNode.childNodes;
	if (items) {
		for (var i=0; i<items.length; i++) {
			var item = items[i];
			if (item.tagName && item.tagName.toLowerCase() == "li") {
				var children = item.childNodes;
				if (children) {
					var n = 0;
					for (var j=0; j<children.length; j++) {
						var child = children[j];
						if (child.tagName && child.tagName.toLowerCase() == 'ul') {
							this.initNode(child);
							n++;
						}
					}
					var textSep = document.createTextNode(' ');
					item.insertBefore(textSep, children[0]);
					if (n == 0) {
						var span = document.createElement("span");
						span.className = "list-item";
						span.innerHTML = this.leafText;
						item.insertBefore(span, textSep);
					} else {
						var a = document.createElement("a");
						a.className = "list-item";
						a.style.color = this.color;
						a.href = "javascript://nop/";
						var expandText = this.expandText;
						var collapsedText = this.collapsedText;
						a.innerHTML = this.expandText;
						a.onclick = function() {
							var parent = this.parentNode;
							if (parent.className == "closed") {
								parent.className = "opened";
								this.innerHTML = collapsedText;
							} else {
								parent.className = "closed";
								this.innerHTML = expandText;
							}
							return false;
						}
						item.insertBefore(a, textSep);
						item.className = "closed";
					}
				}
			}
		}
	}
}