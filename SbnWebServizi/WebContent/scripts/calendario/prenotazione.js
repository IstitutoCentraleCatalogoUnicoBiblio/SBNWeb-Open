//business logic prenotazione

//slot selezionati sulla griglia
var selectedSlots = [];

function getSlotId(e) {
	var tokens = $(e).attr('id').split('-');
	return parseInt(tokens[1]);
}

function updateLabel() {
	var selection = $('#selection');
	var len = selectedSlots.length;
	if (len == 0) {
		selection.html('&nbsp;');
		return;
	}
	
	var start = $('#slot-' + selectedSlots[0]);
	start = $(start).find('#start');
	start = start.text();
	
	var end = $('#slot-' + selectedSlots[len - 1]);
	end = $(end).find('#end');
	end = end.text();
	
	selection.html(start + ' - ' + end);
	//selection.show();
}

function toggleSlot(e) {
	var len = selectedSlots.length;
	var id = getSlotId(e);

	if ($(e).hasClass('checked')) {
		if (len > 0) {
			//si può eliminare solo agli estremi
			if (id != selectedSlots[0] && id != selectedSlots[len - 1])
				return;
		}
		
		// rimuove slot da array
		selectedSlots = $.grep(selectedSlots, function(s) {
			return s != id;
		});
		
		$(e).removeClass('checked');
		
	} else {
		//check max slot selezionabili
		if (config.max_slots > 0) {
			if (len == config.max_slots)
				return;
		}
		
		if (len > 0) {
			//si può aggiungere solo agli estremi
			if (id != (selectedSlots[0] - 1) && id != (selectedSlots[len - 1] + 1))
				return;
		}
		
		selectedSlots.push(id);
		selectedSlots.sort(function(a, b) { return a - b; });
		$(e).addClass('checked');
	}

	// imposta campo selectedFascia
	var json = JSON.stringify(selectedSlots);
	$('input[name="selectedFascia"]').val(json);
	
	updateLabel();
}

//jquery event adapter 
function toggleSlot_jq(event) {
	toggleSlot(event.currentTarget);
}

function selectUtente(value) {
	var intervallo = $('#selected_utente');
	intervallo.val(value);
}

/* Inizializzazione pagina */

//evento click su slot
$(document).ready(function() {
	if (!config.conferma) {
		$('[id^="slot"]').on("click", toggleSlot_jq );
		
		//autoscroll pulsanti prev&next
		$('[id^="nav_"]').on("click", function(e) {
			//e.preventDefault();
			Cookies.set('scroll_y', window.pageYOffset);
		});
	}
	
	// ripristino slot selezionati
	var selected = $('input[name="selectedFascia"]').val();
	if (selected != undefined && selected.length > 0) {
		var slots = JSON.parse(selected);
		for (var s = 0; s < slots.length; s++) {
			var e = $('#slot-' + slots[s])[0];
			toggleSlot(e);
		}		
	}
	
	// check scroll dopo aggiornamento pagina
	var jumpTo = Cookies.get('scroll_y');
	if (jumpTo !== undefined) {
		window.scrollTo(0, jumpTo);
		Cookies.remove('scroll_y'); // cancella cookie
	} else {
		//scroll di default a data corrente, se non visibile
		if (!isScrolledIntoView('#today') ) {
			var tag = $('#today');
			var pos = tag.length > 0 ? tag.offset().top : 0;
			window.scrollTo(0, pos);
		}
	}
});
