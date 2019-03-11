<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<table class="sintetica">
	<tr>
		<th class="etichetta" scope="col" bgcolor="#dde8f0"
			style="text-align: center">#</th>
		<th class="etichetta" scope="col" bgcolor="#dde8f0"
			style="text-align: center">
			<bean:write	name="navForm" property="configCodici.nomeTabellaP" />&nbsp;
			&lpar;<bean:write name="navForm" property="configCodici.tipoTabP" />&rpar;
		</th>
		<th class="etichetta" scope="col" bgcolor="#dde8f0"
			style="text-align: center">#</th>
		<th class="etichetta" scope="col" bgcolor="#dde8f0"
			style="text-align: center">
			<bean:write	name="navForm" property="configCodici.nomeTabellaC" />&nbsp;
			&lpar;<bean:write name="navForm" property="configCodici.tipoTabC" />&rpar;
		</th>
		<th class="etichetta" scope="col" bgcolor="#dde8f0"
			style="text-align: center"><bean:message
			key="elenco.codici.titolo.data.attivazione"
			bundle="amministrazioneSistemaLabels" /></th>
		<th class="etichetta" scope="col" bgcolor="#dde8f0"
			style="text-align: center" width="2%"></th>

	</tr>

	<logic:iterate id="item" property="dettaglioElencoCodici"
		name="navForm" indexId="riga">
		<sbn:rowcolor var="color" index="riga" />
		<c:choose>
			<c:when test="${item.attivo eq 'FALSE'}">
				<tr bgcolor="${color}" style="color: red;">
			</c:when>
			<c:otherwise>
				<tr bgcolor="${color}">
			</c:otherwise>
		</c:choose>
		<td  style="text-align: center"><c:out
			value="${item.flag1}"></c:out></td>
		<td  style="text-align: left"><c:out
			value="${item.descrP}"></c:out></td>
		<td  style="text-align: center"><c:out
			value="${item.flag2}"></c:out></td>
		<td  style="text-align: left"><c:out
			value="${item.descrC}"></c:out></td>
		<td  style="text-align: center"><c:out
			value="${item.dataAttivazione}"></c:out></td>
		<td  style="text-align: center">
			<sbn:disableAll checkAttivita="GESTIONE_TABELLA_CODICI">
				<html:radio	property="dettaglioSelezRadio" value="${item.cdTabella}" />
			</sbn:disableAll>
		</td>

		</tr>
	</logic:iterate>
</table>

