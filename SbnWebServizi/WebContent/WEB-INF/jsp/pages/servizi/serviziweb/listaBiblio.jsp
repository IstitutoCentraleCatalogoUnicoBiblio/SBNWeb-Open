<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<html:xhtml />
<head>
	<title>Servizi SBN Web</title>

	<link rel="stylesheet" type="text/css"
		href='<html:rewrite page="/styles/login.css" paramScope="request" paramName="url" />' />
</head>


<div id="header"></div>
<div id="data">
<div><logic:present name="POLO_NAME" scope="session">
	<p id="polo"><bean-struts:write scope="session" name="POLO_NAME" /></p>
</logic:present></div>
 <div id="divForm">
 <div id="divMessaggio">
		<sbn:errors bundle="serviziWebMessages" />
	</div>
			<html:form	action="/serviziweb/listaBiblio.do">
			<bean:message key="servizi.selezione.biblioteca" bundle="serviziWebLabels" />
		<table style="margin-top:0"  border="1">
			<tr class="etichetta" bgcolor="#dde8f0">
				<th style="width: 8%"><bean:message
					key="servizi.codice.biblioteca" bundle="serviziWebLabels" /></th>
				<th><bean:message key="servizi.nome.biblioteca"	bundle="serviziWebLabels" /></th>
				<th style="width: 3%">
				<div align="center"></div>
				</th>
			</tr>
			<logic:iterate id="item" property="biblio" name="listaBiblioForm" indexId="riga">
				<sbn:rowcolor var="color" index="riga" />
				<tr bgcolor="${color}">
					<td><bean-struts:write name="item"
						property="cod_bib" /></td>
					<td><bean-struts:write name="item"
						property="nom_biblioteca" /></td>
					<td>
						<html:radio property="riga" value="${riga}" />

					</td>
				</tr>
			</logic:iterate>
		</table>
		 <br >

		<html:submit styleClass="pulsanti" property="paramlistaBiblio">
		<bean:message key="button.logout" bundle="serviziWebLabels" />
		</html:submit> <html:submit styleClass="pulsanti" property="paramlistaBiblio">
		<bean:message key="button.avanti" bundle="serviziWebLabels" />
		</html:submit>
		<br/>
		</html:form>
	</div>
</div>

