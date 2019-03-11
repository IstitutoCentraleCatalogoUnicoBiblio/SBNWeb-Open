<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
<div id="header"></div>
<div id="data">
	<sbn:navform action="/serviziweb/DirittiUtente.do" >
	<table cellspacing="0" width="100%" border="0">
			<tr>
				<td >
					<div id="divMessaggio"><sbn:errors bundle="serviziWebMessages" /></div>
				</td>
			</tr>
			<tr>
				<th colspan="4" class="etichetta" align="right">
					<c:out value="${DirittiUtenteForm.biblioSel}"> </c:out>-

					<c:out value="${DirittiUtenteForm.ambiente}"> </c:out> -
					<bean:message key="servizi.utenti.utenteConn" bundle="serviziWebLabels" />

					<c:out value="${DirittiUtenteForm.utenteCon}"> </c:out>
				<hr>
				</th>
			</tr>

			<tr>
				<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
					<bean:message key="servizi.utente.servizi" bundle="serviziWebLabels" />
				</th>
				<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
					<bean:message key="servizi.utente.scadenza"
					bundle="serviziWebLabels" />
				</th>
				<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
					<bean:message key="servizi.utente.sospesoDal"
					bundle="serviziWebLabels" />
				</th>
				<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
					<bean:message key="servizi.utente.sospesoAl"
					bundle="serviziWebLabels" />
				</th>
				</tr>
				<logic:iterate id="item" property="listaDir" name="DirittiUtenteForm" indexId="riga">
				<tr>
					<td>
						<c:out value="${item.servizi}"> </c:out>
					</td>
					<td>
						<c:out value="${item.scadenza}"> </c:out>
					</td>
					<td>
						<c:out value="${item.sospesoDal}"> </c:out>
					</td>
					<td>
						<c:out value="${item.sospesoAl}"> </c:out>
					</td>
				</tr>
				</logic:iterate>
		</table>
	</sbn:navform>
</div>
</layout:page>
