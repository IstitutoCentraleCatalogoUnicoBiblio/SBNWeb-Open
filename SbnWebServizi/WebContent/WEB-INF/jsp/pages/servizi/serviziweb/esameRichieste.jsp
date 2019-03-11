<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="l" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/serviziweb/esameRichieste.do">
		<div id="divForm">
			<div id="divMessaggio"> <sbn:errors bundle="serviziWebMessages" /></div>

		<table width="100%">
			<tr>
				<th colspan="4" class="etichetta" align="right">
					<c:out value="${navForm.biblioSel}"> </c:out>-
					<c:out value="${navForm.ambiente}"> </c:out> -
					<bean:message key="servizi.utenti.utenteConn" bundle="serviziWebLabels" />
					<c:out value="${navForm.utenteCon}"> </c:out>
					<hr>
				</th>
			</tr>
		</table>
		<br/>
		<layout:folder bundle="serviziWebLabels" name="navForm" folders="folders"
		 	property="currentFolder" parameter="${navButtons}" />

			<table cellspacing="0" width="100%" border="0">

			<c:if test="${navForm.flgInCorso or navForm.flgRespinte or navForm.flgEvase or navForm.flgPrenotazioni}">
				<c:if test="${not empty navForm.listaRichieste}">

					<l:iterate id="item" property="listaRichieste" name="navForm" indexId="riga">

						<tr>
							<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.servizio" bundle="serviziWebLabels" /></th>
							<td><c:out value="${item.tipoServizio}"> </c:out></td>
						</tr>

						<tr>
						    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.dataInizio" bundle="serviziWebLabels" /></th>
							<td>
								<c:choose>
									<c:when test="${item.consegnato}">
									<c:out value="${item.dataInizioEffNoOraString}"> </c:out>

									</c:when>
									<c:otherwise>
									<c:out value="${item.dataRichiestaNoOraString}"> </c:out>

									</c:otherwise>
								</c:choose>
							</td>
						</tr>

						<tr>
						    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.attivita" bundle="serviziWebLabels" /></th>
							<td><c:out value="${item.attivita}"> </c:out></td>
						</tr>

						<tr>
						    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.titolo" bundle="serviziWebLabels" /></th>
							<td><c:out value="${item.titolo}"> </c:out></td>
						</tr>

						<tr>
						    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.datiDocumento" bundle="serviziWebLabels" /></th>
							<td><c:out value="${item.datiDocumento}"> </c:out></td>
						</tr>

						<c:if test="${navForm.flgPrenotazioni}">
							<tr>
							    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.dataDispPrev" bundle="serviziWebLabels" /></th>
								<td><c:out value="${item.dataInizioPrevNoOraString}"> </c:out></td>
							</tr>
						</c:if>

						<c:if test="${navForm.flgRespinte}">
							<tr>
							    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.dataAgg" bundle="serviziWebLabels" /></th>
								<td><c:out value="${item.tsVarNoOraString}"> </c:out></td>
							</tr>
						</c:if>

						<c:if test="${navForm.flgEvase}">
							<tr>
							    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.conclusaIl" bundle="serviziWebLabels" /></th>
								<td><c:out value="${item.dataFineEffString}"> </c:out></td>
							</tr>
						</c:if>

						<c:if test="${navForm.flgInCorso}">
							<tr>
							    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.dataScadenza" bundle="serviziWebLabels" /></th>
								<td><c:out value="${item.dataFinePrevNoOraString}"> </c:out></td>
							</tr>

							<c:if test="${item.codStatoRic eq 'A' or item.codStatoRic eq 'S'}">
								<tr>
								    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.dataProroga" bundle="serviziWebLabels" /></th>
									<td><c:out value="${item.dataProrogaString}"> </c:out></td>
								</tr>
							</c:if>

						</c:if>

						<tr>
							<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.statoRichiesta" bundle="serviziWebLabels" /></th>
							<td><c:out value="${item.stato_richiesta}"> </c:out></td>
						</tr>

						<c:if test="${not empty item.noteBibliotecario}">
							<!--
							<tr>
								<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utenti.biblio.note" bundle="serviziWebLabels" /> </th>
								<td><c:out value="${item.noteBibliotecario}"></c:out></td>
							</tr>
							 -->
							<tr>
								<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utenti.biblio.note" bundle="serviziWebLabels" /> </th>
								<td><sbn:writeUrl name="item" property="noteBibliotecario" /></td>
							</tr>
						</c:if>

						<c:if test="${item.withPrenotazionePosto}">
							<tr>
								<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.prenotazionePosto" bundle="serviziWebLabels" /> </th>
								<td>
									<bs:write name="item" property="prenotazionePosto.posto.sala.descrizione"  />&nbsp;il
									<bs:write name="item" property="prenotazionePosto.dataInizio" />&nbsp;
									<bean:message key="servizi.sale.prenotazione.start" bundle="serviziLabels" />&nbsp;
									<bs:write name="item" property="prenotazionePosto.orarioInizio" />&nbsp;
									<bean:message key="servizi.sale.prenotazione.end" bundle="serviziLabels" />&nbsp;
									<bs:write name="item" property="prenotazionePosto.orarioFine" />
								</td>
							</tr>
						</c:if>

						<tr>

							<c:if test="${item.prorogabile}">
								<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.proroghe" bundle="serviziWebLabels" /></th>
								<td><html:link page="/serviziweb/richiestaProroga.do" paramId="IDMOV" paramName="riga">Proroga</html:link></td>
							</c:if>

						</tr>

						<tr>
							<th colspan="4" class="etichetta" align="left">
								<hr>
							</th>
						</tr>
					</l:iterate>

				</c:if>
			</c:if>
		</table>
		</div>
	</sbn:navform>
</layout:page>
