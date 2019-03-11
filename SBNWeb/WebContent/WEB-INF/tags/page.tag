<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "DTD/xhtml1-strict.dtd">

<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<html>
<head>
<meta name="referrer" content="no-referrer" />
<title>
	<l:present name="POLO_NAME" scope="session">
		<bs:write scope="session" name="POLO_NAME" />
	</l:present>
</title>

<link rel="icon" href='<c:url value="/images/book.png" />' type="image/png" />
<link rel="stylesheet" href='<c:url value="/styles/sbnweb.css" />' type="text/css" />
<link rel="stylesheet" href='<c:url value="/styles/sbnweb2.css" />' type="text/css" />

<style type="text/css">

	@media print {
		#divHeader { /*nasconde la testata*/
			display: none;
		}
		#divMenu { /*nasconde il menu*/
			display: none;
		}
		#divContent { /*riposiziona il div dati a tutta pagina*/
			/*border: solid; border-width: 1px; border-color: blue;*/
			position: absolute;
			top: 0px; /*invece di 67px dichiari nel foglio di stile*/
			padding-left: 0px; /*invece di 170 px*/
		}
		#divNavPath { /*nasconde il divPath*/
			display: none;
		}
		table.blocchiDefault {
			/*nasconde la tabella contenenti i dati di paginazione*/
			display: none;
		}
		#divFooterCommon { /*nasconde i dati pre-bottoniera*/
			/*border: solid; border-width: 1px; border-color: yellow;*/
			display: none;
		}
		#divFooter { /*nasconde la bottoniera*/
			display: none;
		}
	}
</style>

<script type="text/javascript" src='<c:url value="/scripts/lib/jquery-1.12.4.min.js" />'></script>
<script type="text/javascript" src='<c:url value="/scripts/lib/js.cookie-2.1.4.min.js" />'></script>
<script type="text/javascript" src='<c:url value="/scripts/scripts.js" />'></script>
<script type="text/javascript">

	//javascript only class
	$(document).ready(function() {
		$(".js-only").css({
			"display" : "inherit"
		});
	});
	
	function renderPageAnchors() {
		//altezza totale div di testata (banner + navigazione)
		var divs_height = $('#divHeader').height() + $('#divNavPath').height();
		//mostra/nascondi link a fine pagina (bottoniera)
		$("#toBottom").css({
			"display" : isScrolledIntoView('#divFooter') ? "none" : "block",
			"top" : Math.max(0, divs_height - $(document).scrollTop())
		});

		//mostra/nascondi link a inizio pagina (barra navigazione)
		$("#toTop").css({
			"display" : isScrolledIntoView('#divNavPath') ? "none" : "block"
		});

	};
	
	$(document).on('ready scroll', renderPageAnchors);
</script>

<%
	// gestione anchor su caricamento pagina
	String anchor = (String) request.getAttribute("anchor");
	if (anchor != null) {
		StringBuilder buf = new StringBuilder();
		buf.append("<script type=\"text/javascript\">")
			.append("$(document).ready(goThere(\"#").append(anchor).append("\", '_self'))</script>");
		out.println(buf.toString());
	}

%>

</head>

<body>
	<div id="divContainer1">
		<div id="divHeader">
			<l:present name="POLO_NAME" scope="session">
				<p class="IntestazioneBiblioteca">
					<bs:write scope="session" name="POLO_NAME" />
				</p>
			</l:present>
			<l:present name="UTENTE_KEY" scope="session">
				&nbsp;Biblioteca:&nbsp;<strong><bs:write scope="session" name="UTENTE_KEY" property="codBib" />
				&nbsp;-&nbsp;<bs:write scope="session" name="UTENTE_KEY" property="biblioteca" /></strong>
				&nbsp;Utente:&nbsp;<strong><bs:write scope="session" name="UTENTE_KEY" property="userId" /></strong>
			</l:present>
	
			<div class="language">
				<html:link action="/logout.do">Logout</html:link>
				<a href="?language=IT"> 
					<img src='<c:url value="/images/it.gif" />' />
				</a>&nbsp;
				<a href="?language=EN">
					<img src='<c:url value="/images/en.gif" />' />
				</a>
			</div>
		</div>
		<%-- fine divHeader --%>
	
		<div id="divMenu">
			<sbn:menu />
			<table border="0" style="font-size: 75%">
				<l:present name="SBNMARC_BUILD_TIME" scope="session">
					<tr>
						<td>&nbsp;sbnmarc:</td>
						<td><bs:write scope="session" name="SBNMARC_BUILD_TIME" /></td>
					</tr>
				</l:present>
				<l:present name="BUILD_TIME" scope="session">
					<tr>
						<td>&nbsp;sbnweb:</td>
						<td><bs:write scope="session" name="BUILD_TIME" /></td>
					</tr>
				</l:present>
			</table>
		</div>
	
		<div id="divContent">
			<jsp:doBody />
		</div>
		<div style="display: none;" id="toTop">
			<a href="#divHeader">top</a>
		</div>
	</div>
</body>
</html>
