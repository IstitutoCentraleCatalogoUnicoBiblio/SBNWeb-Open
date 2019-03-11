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
			style="text-align: center"><html:link
			page="/amministrazionesistema/dettaglioCodice.do?cmd=codice">
			<bean:message key="elenco.codici.titolo.codice"
				bundle="amministrazioneSistemaLabels" />
		</html:link></th>
		<th class="etichetta" scope="col" bgcolor="#dde8f0"
			style="text-align: center"><html:link
			page="/amministrazionesistema/dettaglioCodice.do?cmd=titolo">
			<bean:message key="dettaglio.codici.titolo.descrizione"
				bundle="amministrazioneSistemaLabels" />
		</html:link></th>
		<c:if test="${navForm.configCodici.cdUnimarcUsed}">
			<th class="etichetta" scope="col" bgcolor="#dde8f0"
				style="text-align: center"><bean:message
				key="dettaglio.codici.titolo.cdSbnMarc"
				bundle="amministrazioneSistemaLabels" /></th>
		</c:if>
		<c:if test="${navForm.configCodici.cdMarc21Used}">
			<th class="etichetta" scope="col" bgcolor="#dde8f0"
				style="text-align: center"><bean:message
				key="dettaglio.codici.titolo.cdUnimarc"
				bundle="amministrazioneSistemaLabels" /></th>
		</c:if>
		<c:if test="${navForm.configCodici.tpMaterialeUsed}">
			<th class="etichetta" scope="col" bgcolor="#dde8f0"
				style="text-align: center"><bean:message
				key="dettaglio.codici.titolo.tpMateriale"
				bundle="amministrazioneSistemaLabels" /></th>
		</c:if>
		<c:if test="${navForm.configCodici.dsUlterioreUsed}">
			<th class="etichetta" scope="col" bgcolor="#dde8f0"
				style="text-align: center"><bean:message
				key="dettaglio.codici.titolo.dsUlteriore"
				bundle="amministrazioneSistemaLabels" /></th>
		</c:if>
		<logic:iterate id="flag" name="navForm"
			property="configCodici.usedFlags">
			<th class="etichetta" scope="col" bgcolor="#dde8f0"
				style="text-align: center"><bean:write name="flag" property="label" /></th>
		</logic:iterate>
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
		<td  style="text-align: center">
			<sbn:linkbutton name="item" index="cdTabella" value="cdTabella"
				property="dettaglioSelezRadio" key="dettaglio.codici.button.modifica"
				bundle="amministrazioneSistemaLabels" title="Modifica"
				checkAttivita="GESTIONE_TABELLA_CODICI" />
		</td>
		<td  style="text-align: left"><c:out
			value="${item.descrizione}"></c:out></td>
		<c:if test="${navForm.configCodici.cdUnimarcUsed}">
			<td  style="text-align: left"><c:out
				value="${item.cd_unimarc}"></c:out></td>
		</c:if>
		<c:if test="${navForm.configCodici.cdMarc21Used}">
			<td  style="text-align: left"><c:out
				value="${item.cd_marc21}"></c:out></td>
		</c:if>
		<c:if test="${navForm.configCodici.tpMaterialeUsed}">
			<td  style="text-align: left"><c:out
				value="${item.materiale}"></c:out></td>
		</c:if>
		<c:if test="${navForm.configCodici.dsUlterioreUsed}">
			<td  style="text-align: left"><c:out
				value="${item.ds_cdsbn_ulteriore}"></c:out></td>
		</c:if>
		<logic:iterate id="flag" name="navForm"
			property="configCodici.usedFlags">
			<td  style="text-align: left">
				<c:set var="flagValue" value="flag${flag.flg}" />
				<c:choose>
					<c:when test="${flag.type eq 'LIST'}">
						<bean:write name="item"	property="descrizioneFlag[${flag.flg}]" />
					</c:when>
					<c:otherwise>
						<bean:write name="item"	property="${flagValue}" />
					</c:otherwise>
				</c:choose>

			</td>
		</logic:iterate>
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

