//funzioni per linea calendario

function addFascia(day) {
	deleteFascia(day, 0);
}

function deleteFascia(day, index) {
	var field = $('#action_index');
	field.val(day + ':' + index);
}

function selectIntervallo(value) {
	var intervallo = $('#selected_intervallo');
	intervallo.val(value);
}

//event hook: pagina caricata
$(document).ready(function() {
	
	var cookie_name = 'intervallo_scroll_y';
	
	//event hook: autoscroll pulsanti aggiungi/rimuovi
	$('[class^="button"]').on("click", function(e) {
			//e.preventDefault();
			Cookies.set(cookie_name, window.pageYOffset);
	});
	
	// check scroll dopo aggiornamento pagina
	var jumpTo = Cookies.get(cookie_name);
	if (jumpTo !== "undefined") {
		window.scrollTo(0, jumpTo);
		Cookies.remove(cookie_name); // cancella cookie
	}
});