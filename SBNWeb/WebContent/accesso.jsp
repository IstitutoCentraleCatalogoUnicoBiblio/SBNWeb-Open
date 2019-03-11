<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src='<c:url value="/scripts/lib/jquery-1.12.4.min.js" />'></script>
<script type="text/javascript" src='<c:url value="/scripts/lib/jquery-dateFormat-1.0.2.min.js" />'></script>
<script type="text/javascript" src='<c:url value="/scripts/scripts.js" />'></script>

<link rel="stylesheet" href='<c:url value="/styles/sbnweb.css" />' type="text/css" />

<style>

h1, h2, h3 {
	margin: 0;
}

.content {
	margin-top: 10%;
	margin-left: auto;
	margin-right: auto;
	background-color: #FFCC66;
	padding-top: 1em;
	padding-bottom: 2em;
	padding-left: 2em;
	padding-right: 2em;
	border-radius: 15px;
	border-color: #d2d2d2;
	border-width: 5px;
	box-shadow: 0 1px 0 #FFCC66;
	min-width: 20em;
	width: -moz-fit-content;
	width: -webkit-fit-content;
	width: -ms-fit-content;
}

#result {
	margin-top: 2em;
}

#out_event {
	text-align: center;
	font-size: xx-large;
	margin-top: 0.5em;
	margin-bottom: 0.5em;
}

#out_event_date, #out_event_hour {
	text-align: right;
}

#out_event_hour {
	font-size: xx-large;
}

</style>

<title>SbnWeb - Accesso</title>
</head>
<body>
	<div id="divHeader">
		<p class="IntestazioneBiblioteca">SBN Web - Modulo di Accesso</p>
	</div>
	<div class="content">
		<div>
			<label for="id_tessera">Identificativo</label> <input type="text"
				maxlength="50" size="24" id="id_tessera" />
			<button onclick="submitUserId()">Conferma</button>

		</div>
		<div id="result">
			<h2 id="out_autenticato"></h2>
			<h2 id="out_username"></h2>
			<h2 id="out_user_id"></h2>

			<h1 id="out_event"></h1>
			<h3 id="out_event_date"></h3>
			<h1 id="out_event_hour"></h1>
		</div>
	</div>

	<script type="text/javascript">
		var BIB = getUrlParams("bib");
		if (BIB === undefined) {
			console.log("BIB non definita.");
			BIB = "und";
		}
	</script>
	<script type="text/javascript"
		src='<c:url value="/scripts/utenti/accesso/accesso.js" />'></script>
</body>

</html>