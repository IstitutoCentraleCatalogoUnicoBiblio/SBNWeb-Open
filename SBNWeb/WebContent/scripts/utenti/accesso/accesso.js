
$("#id_tessera").keypress(function(e) {
	if (e.which == 13) {
		// $(this).blur();
		submitUserId();
		return false;
	}
});

function setFocusTessera() {
	$("#id_tessera").focus();
}

$(document).ready(setFocusTessera);
	
function clean() {
	$("#result [id^=out_]").text("");
};

function submitUserId() {
	var id_tessera = $("#id_tessera").val();
	if (!id_tessera)
		return;
	
	//reset campo input
	$("#id_tessera").val("");

	var request = $.ajax({
		type : "GET",
		url : "api/1.0/servizi/utente/accesso",
		dataType : "json",
		data : {
			"bib" : BIB,
			"uid" : id_tessera
		},
	});

	request.done(function(data) {
		clean();
		$("#out_autenticato").text("Utente registrato:\xA0" + (data.autenticato ? "Sì" : "No"));
		$("#out_username").text(data.cognome_nome);
		$("#out_user_id").text(data.cod_utente);
		$("#out_event").text(data.evento);
		
		//formatta data escludendo i secondi
		var d = new Date(data.data_evento);
		$("#out_event_date").text($.format.date(d, "dd/MM/yyyy"));
		$("#out_event_hour").text($.format.date(d, "HH:mm"));
		
		window.setTimeout(clean, 3000);
	});

	request.fail(function(jqXHR, textStatus) {
		clean();
		$("#out_event").text("accesso fallito");
		
		window.setTimeout(clean, 3000);
	});
	
	setFocusTessera();
};
