
//hacks dettaglio movimento

function toggle(id) {
	var tag = $(id);
	var hidden = tag.hasClass("hidden");
	
	if (hidden) {
		tag.removeClass("hidden");
	} else {
		tag.addClass("hidden");
	}

	return false;
}

function toggle_btn(id) {
	var btn = $(id);
	var plus = btn.hasClass("buttonAggiungi");
	
	if (plus) {
		btn.removeClass("buttonAggiungi").addClass("buttonRimuovi");
	} else {
		btn.removeClass("buttonRimuovi").addClass("buttonAggiungi");
	}

	return false;
}

$(document).ready(
		function() {
			var w = $("#titolo").offset().left + $("#titolo").outerWidth();
			$("#dettaglio").width(w);
			
			//dettagli documento (default = chiuso)
			toggle("#grp_dati_doc");
			toggle_btn("#btn_dati_doc");

			//dettagli scadenze (default = aperto per locale, chiuso per ill)
			if (ill) {
				toggle("#grp_dati_mov_locale");
				toggle_btn("#btn_dati_mov_locale");
			}
			
		}
);