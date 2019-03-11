<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<div id="divForm"><sbn:navform
		action="/amministrazionesistema/sinteticaCodici.do">
		<div id="divMessaggio"><sbn:errors /></div>

		<sbn:blocchi numBlocco="bloccoCorrente" numNotizie="totRighe"
			parameter="methodSinCodici" totBlocchi="totBlocchi"
			elementiPerBlocco="maxRighe" />

		<table class="sintetica">
			<tr>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="7%"><html:link
					page="/amministrazionesistema/sinteticaCodici.do?cmd=codice">
					<bean:message key="elenco.codici.titolo.codice"
						bundle="amministrazioneSistemaLabels" />
				</html:link></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="91%"><html:link
					page="/amministrazionesistema/sinteticaCodici.do?cmd=titolo">
					<bean:message key="elenco.codici.titolo.descrizione"
						bundle="amministrazioneSistemaLabels" />
				</html:link></th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0"
					style="text-align: center" width="2%"></th>
			</tr>

			<logic:iterate id="item" property="elencoCodici"
				name="sinteticaCodiciForm" indexId="riga">
				<sbn:rowcolor var="color" index="riga" />
				<c:choose>
					<c:when test="${item.permessi eq 'READ'}">
						<tr bgcolor="${color}" style="color: blue;">
					</c:when>
					<c:otherwise>
						<tr bgcolor="${color}">
					</c:otherwise>
				</c:choose>
				<td  style="text-align: center"><sbn:linkbutton
					name="item" index="cdTabella" property="selezRadio"
					value="cdTabella" key="elenco.codici.button.dettaglio"
					bundle="amministrazioneSistemaLabels"
					title="Lista codici" /></td>
				<td  style="text-align: left"><c:out
					value="${item.descrizione}"></c:out></td>

				<td  style="text-align: center"><html:radio
					property="selezRadio" value="${item.cdTabella}"></html:radio></td>
				</tr>
			</logic:iterate>
		</table>


		<sbn:blocchi numBlocco="bloccoCorrente" numNotizie="totRighe"
			parameter="methodSinCodici" totBlocchi="totBlocchi"
			elementiPerBlocco="maxRighe" bottom="true"></sbn:blocchi>

		<table border="0" align="center" width="100%">
			<tr>
				<td align="left"
					style="font-size: 80%; font-style: oblique; color: blue;"><bean:message
					key="elenco.codici.messaggio" bundle="amministrazioneSistemaLabels" />
				</td>
		</table>

		<div id="divFooter">
		<table border="0" style="height: 40px" align="center">
			<tr>
				<td width="100%">
					<html:submit styleClass="pulsanti" property="methodSinCodici">
						<bean:message key="elenco.codici.button.dettaglio" bundle="amministrazioneSistemaLabels" />
					</html:submit>
					<sbn:checkAttivita idControllo="GESTIONE_TABELLA_CODICI">
						<html:submit styleClass="pulsanti" property="methodSinCodici">
							<bean:message key="elenco.codici.button.abilita"
								bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</sbn:checkAttivita>
				</td>
			</tr>
		</table>
		</div>

	</sbn:navform>
	</div>
</layout:page>