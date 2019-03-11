//business logic dettaglio titolo

/* Inizializzazione pagina */

//event hook: pagina caricata
$(document).ready(function() {

	// event hook: autoscroll pulsanti aggiungi/rimuovi
	$('[class^="buttonImage"]').on("click", function(e) {
		Cookies.set('scroll_y', window.pageYOffset);
	});

	// autoscroll tastiera virtuale
	$('[id^="vkbd-"]').on("click", function(e) {
		Cookies.set('scroll_y', window.pageYOffset);
	});

	// check scroll dopo aggiornamento pagina
	var jumpTo = Cookies.get('scroll_y');
	if (jumpTo !== "undefined") {
		window.scrollTo(0, jumpTo);
		Cookies.remove('scroll_y'); // cancella cookie
	}
});
