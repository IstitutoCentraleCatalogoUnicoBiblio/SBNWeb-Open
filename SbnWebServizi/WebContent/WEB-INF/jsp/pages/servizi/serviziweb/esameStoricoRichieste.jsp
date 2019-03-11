<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/serviziweb/esameStoricoRichieste.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors bundle="serviziWebMessages" />
		</div>
		<br>

		</div>
		<table cellspacing="0" width="100%" border="0">
			<tr>
				<th colspan="4" class="etichetta" align="right"><c:out
					value="${esameStoricoRichiesteForm.biblioSel}">
				</c:out>- <c:out value="${esameStoricoRichiesteForm.ambiente}">
				</c:out> - <bean:message key="servizi.utenti.utenteConn"
					bundle="serviziWebLabels" /> <c:out	value="${esameStoricoRichiesteForm.utenteCon}">
				</c:out>
				<hr>
				</th>
			</tr>

			<tr>
				<!-- Suggerimenti inseriti dal al -->
				<td width="8%"><bean:message key="servizi.utenti.stato.concluse.dal"
					bundle="serviziWebLabels" /> </td>
				<td><html:text
					name="esameStoricoRichiesteForm" property="dal" maxlength="10"></html:text></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td width="8%"><bean:message key="servizi.utenti.stato.inseriti.al"
					bundle="serviziWebLabels" /> </td>
				<td><html:text
					name="esameStoricoRichiesteForm" property="al" maxlength="10"></html:text></td>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<th colspan="4" class="etichetta" align="left"><html:submit
					styleClass="submit" property="paramEsameStorico">
					<bean:message key="servizi.bottone.cerca" bundle="serviziWebLabels" />
				</html:submit>
				<hr>
				</th>
			</tr>


		</table>
		<c:if test="${not empty esameStoricoRichiesteForm.listaRichieste}">
			<table cellspacing="0" width="100%" border="0">
			<tr>
				<th colspan="4" class="etichetta" align="left"><em><strong><bean:message
					key="servizi.documento.risultato.ricerca" bundle="serviziWebLabels" /></strong></em>
				<hr>
				</th>
			</tr>


				<logic:iterate id="item" property="listaRichieste" name="esameStoricoRichiesteForm" indexId="riga">

						<tr>
						    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.servizio" bundle="serviziWebLabels" /></th>
							<!--<td><c:out value="${item.descrTipoServ}"> </c:out></td>	-->
							<td><c:out value="${item.codTipoServ}"> </c:out></td>
						</tr>
						<tr>
						    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.dataInizio" bundle="serviziWebLabels" /></th>
							<td>
								<c:choose>
								<c:when test="${not empty item.dataInizioEffNoOraString}">
									<c:out value="${item.dataInizioEffNoOraString}" />
								</c:when>
								<c:otherwise>
									<c:out value="${item.dataRichiestaNoOraString}" />
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
						    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.dataFine" bundle="serviziWebLabels" /></th>
							<td>
								<c:choose>
								<c:when test="${not empty item.dataFineEffNoOraString}">
									<c:out value="${item.dataFineEffNoOraString}" />
								</c:when>
								<c:otherwise>
									<c:out value="${item.dataFinePrevNoOraString}" />
								</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
						    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.titolo" bundle="serviziWebLabels" /></th>
							<td><c:out value="${item.titolo}"> </c:out></td>
						</tr>

						<tr>
						    <th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.documento.datiDoc" bundle="serviziWebLabels" /></th>
							<td><c:out value="${item.datiInventario}"> </c:out></td>
						</tr>

						<tr>
							<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utente.statoRichiesta" bundle="serviziWebLabels" /></th>
							<td><c:out value="${item.statoRichiesta}"> </c:out></td>
						</tr>

						<c:if test="${not empty item.noteBibliotecario}">
							<tr>
								<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">	<bean:message key="servizi.utenti.biblio.note" bundle="serviziWebLabels" /> </th>
								<td><c:out value="${item.noteBibliotecario}"> </c:out></td>
							</tr>
						</c:if>

						<tr>
							<th colspan="4" class="etichetta" align="left">
								<hr>
							</th>
						</tr>
					</logic:iterate>

			</table>
		</c:if>
	</sbn:navform>
</layout:page>
