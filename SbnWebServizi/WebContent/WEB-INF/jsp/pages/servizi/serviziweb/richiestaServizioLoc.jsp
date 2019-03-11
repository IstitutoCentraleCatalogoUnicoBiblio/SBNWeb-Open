<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/serviziweb/richiestaServizioLoc.do">

		<div id="divForm">
		<div id="divMessaggio"><sbn:errors bundle="serviziWebMessages" /></div>
		<br>
		</div>

		<tr>
			<th colspan="4" class="etichetta" align="right"><c:out
				value="${RichiestaServizioLocForm.bibsel}">
			</c:out>- <c:out value="${RichiestaServizioLocForm.ambiente}">
			</c:out>- <bean:message key="servizi.utenti.utenteConn"
				bundle="serviziWebLabels" /> <c:out
				value="${RichiestaServizioLocForm.utenteCon}">
			</c:out></th>
		</tr>

		<tr>
			<td class="etichetta" align="left">
			<hr>
			<c:if test="${RichiestaServizioLocForm.richiesta eq 'PRENOTAZIONE'}">
				<bean:message key="servizi.web.richiestaPrenotazione"
					bundle="serviziWebLabels" />:
		    	</c:if> <c:if
				test="${RichiestaServizioLocForm.richiesta  ne 'PRENOTAZIONE'}">
				<bean:message key="servizi.web.richiestaServiziLoc"
					bundle="serviziWebLabels" />:
				</c:if> <bean:message key="servizi.autoreLogin" bundle="serviziWebLabels" />
			<em><strong><c:out
				value="${RichiestaServizioLocForm.autore}">
			</c:out></strong></em> , <bean:message key="servizi.titoloLogin" bundle="serviziWebLabels" />
			<em><strong><c:out
				value="${RichiestaServizioLocForm.titolo}">
			</c:out></strong></em> del <em><strong><c:out
				value="${RichiestaServizioLocForm.anno}">
			</c:out></strong></em>
			<hr>
			</td>
		</tr>
<sbn:disableAll disabled="${RichiestaServizioLocForm.lettura}">
		<table style="margin-top: 0" border="1">
			<tr>
				<td><em><strong><bean:message
					key="servizi.servizio.richiesto" bundle="serviziWebLabels" /></strong></em>:<c:out
					value="${RichiestaServizioLocForm.servizio}">
				</c:out></td>
				<td><em><strong><bean:message
					key="servizi.documento.dataRic" bundle="serviziWebLabels" /></strong></em><c:out
					value="${RichiestaServizioLocForm.dataRic}">
				</c:out></td>
			</tr>

			<sbn:checkAttivita idControllo="DOC_ALTRA_BIBLIOTECA">
				<tr>
					<td  class="etichetta" align="right"><em><strong><bean:message
							key="servizi.utenti.indirizzoRes" bundle="serviziLabels" />:</strong></em></td>
					<td class="testoNoBold"><html:text styleId="testoNoBold"
							property="dati.via" size="75" maxlength="50"
							disabled="true" /></td>
				</tr>
				<tr>
					<td  class="etichetta" align="right"><em><strong><bean:message
							key="servizi.utenti.cittaRes" bundle="serviziLabels" />:</strong></em></td>
					<td class="testoNoBold"><html:text styleId="testoNoBold"
							property="dati.comune" size="75" maxlength="50"
							disabled="true" /></td>
				</tr>
				<tr>
					<td class="etichetta" align="right"><em><strong><bean:message
							key="servizi.utenti.capRes" bundle="serviziLabels" />:</strong></em></td>
					<td class="testoNoBold"><html:text styleId="testoNoBold"
							property="dati.cap" size="5" maxlength="5"
							disabled="true" /></td>
				</tr>
				<tr>
					<td class="etichetta" align="right"><em><strong><bean:message
							key="servizi.utenti.provRes" bundle="serviziLabels" />:</strong></em></td>
					<td class="testoNoBold"><html:select property="dati.prov"
							disabled="true">
							<html:optionsCollection property="listaProvincia" value="cd_tabellaTrim" label="ds_tabella" />
						</html:select></td>
				</tr>
				<tr>
					<td  class="etichetta" align="right"><em><strong><bean:message
							key="servizi.utenti.nazRes" bundle="serviziLabels" />:</strong></em></td>
					<td align="left"><html:select property="dati.cd_paese"
							disabled="true">
							<html:optionsCollection property="listaPaesi" value="cd_tabellaTrim" label="ds_tabella" />
						</html:select></td>
				</tr>
				<tr>
					<td  class="etichetta" align="right"><em><strong><bean:message
							key="servizi.utenti.email" bundle="serviziLabels" />:</strong></em></td>
					<td align="left"><html:text styleId="testoNoBold"
							property="dati.utente_email" size="75" maxlength="50" /></td>
				</tr>
			</sbn:checkAttivita>

			<sbn:checkAttivita idControllo="SUPPORTO_MODALITA_ILL">
				<tr>
					<td><em><strong><bean:message key="servizi.erogazione.movimento.ill.supplyMediumType" bundle="serviziLabels" />:</strong></em></td>
					<td align="left">
						<sbn:disableAll checkAttivita="TIPO_SERVIZIO_ILL">
							<html:select property="nuovaRichiesta.datiILL.cd_supporto" >
								<html:optionsCollection property="tipiSupportoILL" value="cd_tabellaTrim" label="ds_tabella" />
							</html:select>
						</sbn:disableAll>
					</td>
				</tr>
				<tr>
					<td><em><strong><bean:message key="servizi.erogazione.movimento.ill.deliveryService" bundle="serviziLabels" />:</strong></em></td>
					<td align="left">
						<html:select property="nuovaRichiesta.datiILL.cod_erog" >
							<html:optionsCollection property="modoErogazioneILL" value="cd_tabellaTrim" label="ds_tabella" />
							<%-- <html:optionsCollection property="modoErogazione" value="codErog" label="desModErog" /> --%>
						</html:select>
					</td>
				</tr>
			</sbn:checkAttivita>

			<c:if test="${not empty RichiestaServizioLocForm.mostraCampi}">
				<logic:iterate id="item" name="RichiestaServizioLocForm"
					property="mostraCampi">

					<c:if test="${item.campoRichiesta eq 18}">
						<tr>
							<td><em><strong><bean:message
								key="servizi.documento.dataDisponibDocumento"
								bundle="serviziWebLabels" /></strong></em></td>
							<td><html:text name="RichiestaServizioLocForm"
								property="dataDisponibDocumento" disabled="true" maxlength="10"></html:text>
							<c:if test="${item.obbligatorio}"></c:if></td>
						</tr>
					</c:if>

					<c:if test="${item.campoRichiesta eq 16}">
						<tr>
							<td><em><strong><bean:message
								key="servizi.documento.dataPrevRitiroDocumento"
								bundle="serviziWebLabels" /></strong></em></td>
							<td>
							<sbn:disableAll disabled="${navForm.nuovaRichiesta.withPrenotazionePosto}">
								<html:select property="dataPrevRitiroDocumento" name="RichiestaServizioLocForm"
								disabled="${RichiestaServizioLocForm.lettura}">
									<html:optionsCollection property="dataPrevRitDoc" name="RichiestaServizioLocForm"
										label="codice" value="codice" />
								</html:select>
							</sbn:disableAll>
							<%--<html:text name="RichiestaServizioLocForm"
								property="dataPrevRitiroDocumento"
								disabled="${RichiestaServizioLocForm.lettura}" maxlength="10"></html:text>(gg/mm/aaaa)
							--%>
							<c:if test="${item.obbligatorio}">(*)</c:if>
							</td>
						</tr>
					</c:if>

					<c:if test="${item.campoRichiesta eq 15}">
						<tr>
							<td><em><strong><bean:message
								key="servizi.documento.dataLim" bundle="serviziWebLabels" /></strong></em></td>
							<td><html:text name="RichiestaServizioLocForm"
								property="dataLimInteresse"
								disabled="${RichiestaServizioLocForm.lettura}" maxlength="10"></html:text>(gg/mm/aaaa)
							<c:if test="${item.obbligatorio}">(*)</c:if></td>
						</tr>
					</c:if>

					<c:if test="${item.campoRichiesta eq 24}">
						<c:if
							test="${not empty RichiestaServizioLocForm.nuovaRichiesta.codSupporto}">
							<tr>
								<td><em><strong><bean:message
									key="servizi.documento.supporto" bundle="serviziWebLabels" /></strong></em></td>

								<td>
									<html:select property="nuovaRichiesta.codSupporto" disabled="${RichiestaServizioLocForm.lettura}"
									onchange="this.form.cambiaSupportoRicServLoc.value='true'; this.form.submit();" >
										<html:optionsCollection property="tipiSupporto"	value="codSupporto" label="descrizione" />
									</html:select>
									<html:hidden property="cambiaSupportoRicServLoc" value="" /> <c:if
									test="${item.obbligatorio}">(*)</c:if></td>
							</tr>
						</c:if>

					</c:if>

					<c:if test="${item.campoRichiesta eq 22}">
						<tr>
							<td><em><strong><bean:message
								key="servizi.documento.modErog" bundle="serviziWebLabels" /></strong></em></td>
							<td><html:select property="nuovaRichiesta.cod_erog"
								disabled="${RichiestaServizioLocForm.lettura}">
								<html:optionsCollection property="modoErogazione"
									value="codErog" label="desModErog" />
							</html:select> <c:if test="${item.obbligatorio}">(*)</c:if></td>
						</tr>
					</c:if>

					<c:if test="${item.campoRichiesta eq 99}">
						<tr>
							<td><em><strong><bean:message
								key="servizi.documento.annoPeriodico" bundle="serviziWebLabels" /></strong></em></td>
							<td><html:text name="RichiestaServizioLocForm"
								property="annoPeriodico"
								disabled="true" maxlength="4"></html:text>
							<c:if test="${item.obbligatorio}">(*)</c:if></td>
						</tr>
					</c:if>

					<c:if test="${item.campoRichiesta eq 9}">
						<tr>
							<td><em><strong><bean:message
								key="servizi.documento.volInter" bundle="serviziWebLabels" /></strong></em></td>
							<td><html:text name="RichiestaServizioLocForm"
								property="volInter" maxlength="4"
								disabled="${RichiestaServizioLocForm.lettura}"></html:text> <c:if
								test="${item.obbligatorio}">(*)</c:if></td>
						</tr>
					</c:if>

					<c:if test="${item.campoRichiesta eq 10}">
						<tr>
							<td><em><strong><bean:message
								key="servizi.documento.numFasc" bundle="serviziWebLabels" /></strong></em></td>
							<td><html:text name="RichiestaServizioLocForm"
								property="numFasc" maxlength="30"
								disabled="${RichiestaServizioLocForm.lettura}"></html:text> <c:if
								test="${item.obbligatorio}">(*)</c:if></td>
						</tr>
					</c:if>

					<c:if test="${item.campoRichiesta eq 11}">
						<tr>
							<td><em><strong><bean:message
								key="servizi.documento.intCopia" bundle="serviziWebLabels" /></strong></em></td>
							<td><html:text name="RichiestaServizioLocForm"
								property="intcopia"
								disabled="${RichiestaServizioLocForm.lettura}"></html:text>(2-5,7,9-10)
							<c:if test="${item.obbligatorio}">(*)</c:if></td>
						</tr>
					</c:if>

					<c:if test="${item.campoRichiesta eq 13}">
						<tr>
							<td><em><strong><bean:message
								key="servizi.documento.spesa" bundle="serviziWebLabels" /></strong></em></td>
							<td><html:text name="RichiestaServizioLocForm"
								property="spesaMax"
								disabled="${RichiestaServizioLocForm.lettura}"></html:text> <c:if
								test="${item.obbligatorio}">(*)</c:if></td>
						</tr>
					</c:if>

					<c:if test="${item.campoRichiesta eq 12}">
						<tr>
							<td><em><strong><bean:message
								key="servizi.documento.noteUte" bundle="serviziWebLabels" /></strong></em></td>
							<td><html:textarea name="RichiestaServizioLocForm"
								property="noteUte"
								disabled="${RichiestaServizioLocForm.lettura}" rows="6"
								cols="42"></html:textarea> <c:if test="${item.obbligatorio}">(*)</c:if></td>
						</tr>
					</c:if>
<!--
					<c:if test="${item.campoRichiesta eq 25}">
						<tr>
							<td><em><strong><bean:message
								key="servizi.documento.sala" bundle="serviziWebLabels" /></strong></em></td>
							<td><html:text name="RichiestaServizioLocForm"
								property="sala" disabled="${RichiestaServizioLocForm.lettura}"
								maxlength="4"></html:text> <c:if test="${item.obbligatorio}">(*)</c:if></td>
						</tr>
					</c:if>

					<c:if test="${item.campoRichiesta eq 26}">
						<tr>
							<td><em><strong><bean:message
								key="servizi.documento.posto" bundle="serviziWebLabels" /></strong></em></td>
							<td><html:text name="RichiestaServizioLocForm"
								property="posto" disabled="${RichiestaServizioLocForm.lettura}"
								maxlength="4"></html:text> <c:if test="${item.obbligatorio}">(*)</c:if></td>
						</tr>
					</c:if>
-->
				<c:if test="${item.campoRichiesta eq 27}">
					<tr>
					 	<td><em><strong><bean:message key="servizi.erogazione.inserimentoRichiesta.documento.copyright"  bundle="serviziLabels" /></strong></em></td>
					 	<td>
					 		<html:checkbox name="RichiestaServizioLocForm" disabled="${RichiestaServizioLocForm.lettura}" property="copyright" />
					 		<html:hidden name="RichiestaServizioLocForm" property="copyright" value="false" />
					 		<c:if test="${item.obbligatorio}">(*)</c:if>
					 	</td>
					</tr>
				</c:if>
				</logic:iterate>
			</c:if>
			<sbn:checkAttivita idControllo="TARIFFA">
				<tr>
					<td><em><strong><bean:message
						key="servizi.documento.tariffa" bundle="serviziWebLabels" /></strong></em></td>
					<td><html:text name="RichiestaServizioLocForm"
						property="tariffa" disabled="true"></html:text> <c:if
						test="${item.obbligatorio}">(*)</c:if></td>
				</tr>
			</sbn:checkAttivita>
			<c:if test="${navForm.nuovaRichiesta.withPrenotazionePosto}">
				<tr>
					<td><em><strong><bean:message key="servizi.prenotazionePosto" bundle="serviziWebLabels" />&colon;</strong></em></td>
					<td>
						<bean:message key="servizi.sale.sala" bundle="serviziLabels" />&nbsp;
						<html:text name="navForm" property="nuovaRichiesta.prenotazionePosto.posto.sala.descrizione" readonly="true" />&nbsp;il
						<html:text name="navForm" property="nuovaRichiesta.prenotazionePosto.dataInizio" readonly="true" styleClass="date" />&nbsp;
						<bean:message key="servizi.sale.prenotazione.start" bundle="serviziLabels" />&nbsp;
						<html:text name="navForm" property="nuovaRichiesta.prenotazionePosto.orarioInizio" readonly="true" styleClass="time" />&nbsp;
						<bean:message key="servizi.sale.prenotazione.end" bundle="serviziLabels" />&nbsp;
						<html:text name="navForm" property="nuovaRichiesta.prenotazionePosto.orarioFine" readonly="true" styleClass="time" />
					</td>
				</tr>
			</c:if>
		</table>
</sbn:disableAll>
		<sbn:checkAttivita idControllo="DOC_ALTRA_BIBLIOTECA">
			<hr>
			<strong>Se la richiesta sarà accettata il documento sarà consegnato alla sede della tua biblioteca.</strong>
		</sbn:checkAttivita>

		<c:if test="${empty RichiestaServizioLocForm.richiesta}">
			<tr>
				<td class="etichetta" align="left">
				<hr>
				<bean:message key="servizi.web.richiestaServiziLoc.dtiObbligatori"
					bundle="serviziWebLabels" />
				<hr>
				</td>
			</tr>
		</c:if>

		<jsp:include page="/WEB-INF/jsp/pages/servizi/serviziweb/infoPassword.jsp" flush="true" />

		<html:submit styleClass="submit" property="paramRichiestaServizio">
			<bean:message key="servizi.bottone.indietro"
				bundle="serviziWebLabels" />
		</html:submit>

		<c:if test="${empty RichiestaServizioLocForm.tariffa}">
			<c:if test="${RichiestaServizioLocForm.richiesta eq 'PRENOTAZIONE'}">

				<html:submit styleClass="submit" property="paramRichiestaServizio">
					<bean:message key="servizi.bottone.ok" bundle="serviziWebLabels" />
				</html:submit>
			</c:if>
			<c:if test="${empty RichiestaServizioLocForm.richiesta}">
				<html:submit styleClass="submit" property="paramRichiestaServizio">
					<bean:message key="button.avanti" bundle="serviziWebLabels" />
				</html:submit>
			</c:if>
		</c:if>

		<c:if test="${not empty RichiestaServizioLocForm.tariffa}">
			<html:submit styleClass="submit" property="paramRichiestaServizio">
				<bean:message key="servizi.documento.insRich"
					bundle="serviziWebLabels" />
			</html:submit>
		</c:if>

	</sbn:navform>
</layout:page>
