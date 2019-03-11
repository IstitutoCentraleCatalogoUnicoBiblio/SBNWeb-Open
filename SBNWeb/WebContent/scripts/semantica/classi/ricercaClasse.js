var dewey = false;

function selectCodSistema() {
	var cod_sistema = $("#cod_sistema").val();
	dewey = (cod_sistema == "D");
	var tag_ed = $("#edizione");
	if (!dewey) {
		//per sistemi diversi da dewey l'edizione va eliminata
		tag_ed.val("");
	}

	//disattivazione campo per edizioni <> dewey
	tag_ed.prop("disabled", !dewey);
}

$(document).on("ready", selectCodSistema() );
