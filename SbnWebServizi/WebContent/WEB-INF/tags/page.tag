<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "DTD/xhtml1-strict.dtd">

<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<html>
<head>
<meta name="referrer" content="no-referrer" />
<title><bean:message key="application.title" /></title>
<link rel="stylesheet" href='<c:url value="/styles/sbnweb.css" />' type="text/css" />
<link rel="stylesheet" href='<c:url value="/styles/sbnweb2.css" />' type="text/css" />
<link rel="stylesheet" href='<c:url value="/styles/serviziWeb.css" />' type="text/css" />
<script type="text/javascript" src='<c:url value="/scripts/jquery-1.12.4.min.js" />'></script>
<script type="text/javascript" src='<c:url value="/scripts/js.cookie-2.1.4.min.js" />'></script>
<script type="text/javascript" src='<c:url value="/scripts/scripts.js" />'></script>
</head>

	<%
		// gestione anchor su caricamento pagina
		String anchor = (String) request.getAttribute("anchor"); 
		if (anchor != null) 
			out.println("<body onload='goThere(\"#" + anchor + "\");'>");
		else
			out.println("<body>");
	%>	
	
	
	<%--
	
	almaviva2    TUTTO SOSTUITO SOTTO
<div id="divContainer1"> <div id="divHeader">
<p class="IntestazioneBiblioteca">SBN Web</p> 
 			<logic:present name="UTENTE_KEY" scope="session">
				Polo:&nbsp;<bean-struts:write scope="session" name="UTENTE_WEB_KEY" property="codPolo" />
				<%--&nbsp;Biblioteca:&nbsp;<bean-struts:write scope="session" name="UTENTE_WEB_KEY" property="codBib" /> 
				&nbsp;Utente:&nbsp;<bean-struts:write scope="session" name="UTENTE_WEB_KEY" property="userId" />
			</logic:present>
--%>

	<div id="divContainer1"> <div id="divHeader">
		<p class="IntestazioneBiblioteca"></p> 
	
 			<logic:present name="UTENTE_KEY" scope="session">
				<span class="formattazioneCodPolo">
					Polo:&nbsp;<bean-struts:write scope="session" name="UTENTE_WEB_KEY" property="codPolo" />
					<c:if test="${not empty sessionScope.COD_BIBLIO}">
						&nbsp;&nbsp;Biblio:<bean-struts:write scope="session" name="COD_BIBLIO" />
					</c:if>
				</span><br/><br/>
				<span class="formattazioneCodUtente">
				Utente:&nbsp;<bean-struts:write scope="session" name="UTENTE_WEB_KEY" property="userId" /></span>
			</logic:present>


<div class="language"> 
<html:link
	page="/Documentazione/GuidaSbnweb.htm" target="_guida">
	<bean:message key="application.help" /></html:link>&nbsp;|&nbsp;<html:link
	action="/logout.do">Logout</html:link> <a href="?language=IT">
<img border="0" src='<c:url value="/images/it.gif" />'></a>&nbsp;
<a href="?language=EN">
<img border="0" src='<c:url value="/images/en.gif" />'></a> </div> </div>
<%-- fine divHeader --%>

<div id="divMenu"> 
	<sbn:menu />
	<table border="0" style="font-size:75%">
		<logic:present name="BUILD_TIME" scope="session">
			<tr>	
				<td>&nbsp;build:</td><td><bean-struts:write scope="session" name="BUILD_TIME" /></td>
			</tr>	
	</logic:present>
	</table>
</div>

<div id="divContent">
<%-- <div id="divNavPath" class="etichetta">
		<sbn:navigation />
	</div> --%>

	<jsp:doBody />
</div>

</div>

</body>
</html>
