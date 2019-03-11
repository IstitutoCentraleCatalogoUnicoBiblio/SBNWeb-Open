//funzioni per linea calendario

function addFascia(day) {
	deleteFascia(day, 0);
}

function deleteFascia(day, index) {
	var field = $('#action_index');
	field.val(day + ':' + index);
}