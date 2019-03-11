//business logic nuova richiesta

/* Inizializzazione pagina */

//event hook: pagina caricata
$(document).ready(function() {

	// check scroll dopo aggiornamento pagina
	var jumpTo = Cookies.get('scroll_y');
	if (jumpTo !== "undefined") {
		window.scrollTo(0, jumpTo);
		Cookies.remove('scroll_y'); // cancella cookie
	}
});
