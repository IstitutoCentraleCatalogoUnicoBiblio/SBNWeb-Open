<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<html:xhtml />
<style>
	#dettaglio {
		border-collapse: collapse;
	}

	.btop {
		border-top: 2px solid;
		border-color: red;
		background-color: #FFCC99;
	}

	.bbottom {
		border-bottom: 2px solid;
		border-color: red;
		background-color: #FFCC99;
	}

	.bleft {
		border-left: thick solid;
		border-color: red;
		background-color: #FFCC99;
	}

	.bright {
		border-right: 2px solid;
		border-color: red;
		background-color: #FFCC99;
	}
</style>
<layout:page>
	<sbn:navform action="/servizi/erogazione/DettaglioMovimento.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors bundle="serviziMessages" />
		</div>
		<html:hidden property="updateCombo" />
		<bs:size id="comboSize" name="navForm" property="listaMovimenti" />
		<sbn:disableAll disabled="${navForm.conferma}" checkAttivita="GESTIONE" >
			<c:if test="${(comboSize ge 2) and (not navForm.cambioServizio)}">
				<html:submit property="methodDettaglioMovimentiUte">
					<bean:message key="button.elemPrec"
						bundle="gestioneBibliograficaLabels" />
				</html:submit>
				<html:submit property="methodDettaglioMovimentiUte">
					<bean:message key="button.elemSucc"
						bundle="gestioneBibliograficaLabels" />
				</html:submit>
			</c:if>
			<table id="dettaglio">
				<!-- riga biblioteca erogante-->
				<tr>
					<td width="15%"><bean:message key="servizi.erogazione.movimento.bibErogante"
						bundle="serviziLabels" /></td>
					<td><html:text property="detMov.codBibOperante" readonly="true"
						size="10" /> <bs:write name="navForm"
						property="detMov.descrBib" /></td>
				</tr>

				<!-- dati bib fornitrice ILL -->
				<c:if test="${navForm.detMov.richiestaILL and navForm.detMov.datiILL.richiedente}">
				<tr class="dati_ill">
					<th align="left" colspan="2">
						<bean:message key="servizi.erogazione.dettaglioMovimento.DatiBibFornitrice" bundle="serviziLabels" />
					</th>
				</tr>
				<tr class="dati_ill">
					<td align="right">&nbsp;</td>
					<td>
						<html:text styleId="testoNoBold" property="detMov.datiILL.responderId" size="8" readonly="true" />&nbsp;
						<html:text styleId="testoNoBold" property="detMov.denominazioneBibFornitrice" size="50"	readonly="true" />
					</td>
				</tr>
				<tr class="dati_ill">
					<td align="right"><bean:message key="servizi.erogazione.movimento.email"
						bundle="serviziLabels" /></td>
					<td align="left">
						<html:text property="detMov.datiILL.responder_email" readonly="true" size="30" bundle="serviziLabels" />
						<c:if test="${not empty navForm.detMov.datiILL.responder_email}">
							<a href="mailto://${navForm.detMov.datiILL.responder_email}">
								<html:img altKey="servizi.erogazione.movimento.email" bundle="serviziLabels"
									page="/styles/images/email18.png"  styleClass="mailto" />
							</a>
						</c:if>
					</td>
				</tr>
				</c:if>

				<!-- dati bib richiedente ILL -->
				<c:if test="${navForm.detMov.richiestaILL and navForm.detMov.datiILL.fornitrice}">
				<tr class="dati_ill">
					<th align="left" colspan="2">
						<bean:message key="servizi.erogazione.dettaglioMovimento.DatiBibRichiedente" bundle="serviziLabels" />
					</th>
				</tr>
				<tr class="dati_ill">
					<td align="right">&nbsp;</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="detMov.datiILL.requesterId" size="8" readonly="true" />&nbsp;&nbsp;&nbsp;
						<html:text property="detMov.cognomeNome" readonly="true" size="50" styleClass="l"
						titleKey="servizi.erogazione.listaMovimenti.cognomeNome" bundle="serviziLabels" />
						<c:if test="${not navForm.cambioServizio}">
							&nbsp;<html:submit styleClass="buttonImage" property="methodDettaglioMovimentiUte" bundle="serviziLabels">
								<bean:message key="servizi.bottone.esame.utente" bundle="serviziLabels" />
							</html:submit>
						</c:if>
					</td>
				</tr>
				<!-- riga email-->
				<tr class="dati_ill">
					<td align="right"><bean:message key="servizi.erogazione.movimento.email"
						bundle="serviziLabels" /></td>
					<td align="left">
						<html:text property="detMov.datiILL.requester_email" readonly="true" size="30" bundle="serviziLabels" />
						<c:if test="${not empty navForm.detMov.datiILL.requester_email}">
							<a href="mailto://${navForm.detMov.datiILL.requester_email}">
								<html:img altKey="servizi.erogazione.movimento.email" bundle="serviziLabels"
									page="/styles/images/email18.png"  styleClass="mailto" />
							</a>
						</c:if>
					</td>
				</tr>
				</c:if>

				<!-- dati richiesta ILL -->
				<c:if test="${navForm.detMov.richiestaILL}">
					<tr class="dati_ill">
					<th align="left" colspan="2">
						<bean:message key="servizi.erogazione.dettaglioMovimento.dati.richiesta.ill" bundle="serviziLabels" />
					</th>
					</tr>
					<tr class="dati_ill" id="id_top_dati_ill">
						<td align="right"><bean:message key="servizi.erogazione.codRichILL" bundle="serviziLabels" /></td>
						<td align="left"><html:text property="detMov.datiILL.transactionId" size="16" readonly="true" /></td>
					</tr>
					<!-- riga data inizio e fine richiesta ill-->
					<tr class="dati_ill">
						<td align="right"><bean:message key="servizi.erogazione.dettaglio.movimento.ill.dataInizio"
							bundle="serviziLabels" /></td>
						<td><html:text property="detMov.datiILL.dataInizioString" size="20"
							style="text-align:right;" readonly="true" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <bean:message
							key="servizi.erogazione.dettaglio.movimento.ill.dataFine" bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;<html:text
							property="detMov.datiILL.dataFineString" size="20" style="text-align:right;"
							readonly="true" /></td>
					</tr>
					<!-- riga attività ill attuale-->
					<tr class="dati_ill">
						<td align="right"><bean:message key="servizi.erogazione.ill.attivita" bundle="serviziLabels" /></td>
						<td><html:text property="detMov.datiILL.descrizioneStatoRichiesta" size="70" readonly="true" /></td>
					</tr>
					<!-- supporto & erogazione ILL -->
					<tr class="dati_ill">
						<td align="right">
							<bean:message key="servizi.erogazione.movimento.ill.supplyMediumType" bundle="serviziLabels" />
						</td>
						<td>
						<sbn:disableAll checkAttivita="SUPPORTO_EROGAZIONE_ILL">
							<html:select property="detMov.datiILL.cd_supporto" >
								<html:optionsCollection property="tipiSupportoILL" value="cd_tabellaTrim" label="ds_tabella" />
							</html:select>
						&nbsp;&nbsp;&nbsp;
							<bean:message key="servizi.erogazione.movimento.ill.deliveryService" bundle="serviziLabels" />
						&nbsp;
							<html:select property="detMov.datiILL.cod_erog" styleId="id_erog_ill">
								<html:optionsCollection property="modoErogazioneILL" value="cd_tabellaTrim" label="ds_tabella" />
							</html:select>
						&nbsp;
						</sbn:disableAll>
						<sbn:checkAttivita idControllo="ILL_UPLOAD">
							<button onclick="goThere('${navForm.detMov.datiILL.docDeliveryLink}', '_ill-server')">Carica file</button>
						</sbn:checkAttivita>
						<sbn:checkAttivita idControllo="ILL_DOWNLOAD">
							<button onclick="goThere('${navForm.detMov.datiILL.docDeliveryLink}', '_ill-server')">Scarica file</button>
						</sbn:checkAttivita>
						</td>
					</tr>
					<sbn:disableAll checkAttivita="RINNOVA">
					<tr class="dati_ill">
						<td align="right">
							<bean:message key="servizi.erogazione.dataProroga" bundle="serviziLabels" />
						</td>
						<td>
							<html:text property="detMov.dataProrogaString" size="15" maxlength="10"
								readonly="${navForm.conferma or navForm.detMov.prenotazione}" />
						</td>
					</tr>
					</sbn:disableAll>
					<tr><td>&nbsp;</td></tr>
				</c:if>
				<!-- riga utente-->
				<c:if test="${navForm.detMov.richiestaLocale or navForm.detMov.datiILL.richiedente}">
				<tr>
					<th align="left">
						<bean:message key="servizi.erogazione.dettaglioMovimento.DatiUtente" bundle="serviziLabels" />
					</th>
				</tr>
				<tr>
					<td align="right"><bean:message key="servizi.erogazione.dettaglioMovimento.numeroTessera"
						bundle="serviziLabels" /></td>
					<td align="left">
						<html:text property="detMov.codUte" readonly="true" size="20" titleKey="servizi.erogazione.listaMovimenti.titleCodiceUtente" bundle="serviziLabels" />
						&nbsp;&nbsp;&nbsp;
						<html:text property="detMov.cognomeNome" readonly="true" size="50" styleClass="l"
						titleKey="servizi.erogazione.listaMovimenti.cognomeNome" bundle="serviziLabels" />
						<c:if test="${not navForm.cambioServizio}">
							&nbsp;<html:submit styleClass="buttonImage" property="methodDettaglioMovimentiUte" bundle="serviziLabels">
								<bean:message key="servizi.bottone.esame.utente" bundle="serviziLabels" />
							</html:submit>
						</c:if>
					</td>
				</tr>
				<tr>
					<td align="right"><bean:message key="servizi.erogazione.movimento.email"
						bundle="serviziLabels" /></td>
					<td align="left">
						<html:text property="detMov.email" readonly="true" size="30" bundle="serviziLabels" />
						<c:if test="${not empty navForm.detMov.email}">
							<a href="mailto://${navForm.detMov.email}">
								<html:img altKey="servizi.erogazione.movimento.email" bundle="serviziLabels"
									page="/styles/images/email18.png"  styleClass="mailto" />
							</a>
						</c:if>

					</td>
				</tr>
				</c:if>
				<!-- riga  biblioteca richiedente-->
				<c:choose>
					<c:when test="${navForm.detMov.codBibRichiedente ne null}">
						<tr>
							<td align="right"><bean:message key="servizi.erogazione.movimento.bibRichiedente"
								bundle="serviziLabels" /></td>
							<td align="left"><html:text property="detMov.codBibRichiedente" readonly="true" size="5" />
							<bs:write name="navForm" property="detMov.descrBibRichiedente" /></td>
						</tr>
					</c:when>
					<c:otherwise>
						<!-- riga numero tessera + nomeCognome-->

					</c:otherwise>
				</c:choose>
				<!-- riga documento-->
				<tr>
					<th align="left">
						<div class="flex-v-center">
							<c:if test="${navForm.detMov.richiestaILL}" >
								<input type="button" id="btn_dati_doc"
									class="buttonRimuovi" onclick="toggle('#grp_dati_doc'); toggle_btn('#btn_dati_doc');"
									title="Apri/Chiudi dati documento" />
							</c:if>
							<span>
								<bean:message key="servizi.erogazione.dettaglioMovimento.DatiDocumento"	bundle="serviziLabels" />
							</span>
						</div>
					</th>
				</tr>
				<!-- riga bid + titolo-->
				<tr>
					<td align="right"><bean:message key="servizi.erogazione.listaMovimenti.titoloDocumento"
						bundle="serviziLabels" /></td>
					<td align="left"><html:text property="detMov.bid" size="10" readonly="true"
						bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;&nbsp;<html:text
						property="detMov.titolo" size="80" readonly="true" bundle="serviziLabels" styleId="titolo" /></td>
				</tr>
				<!-- riga esemplare doc / inventario collocazione + eventuale check periodico-->
				<c:choose>
					<c:when test="${navForm.detMov.progrEsempDocLet eq null}">
						<tr>
							<td align="right"><html:text property="detMov.codDocLet" size="5" readonly="true"
								titleKey="servizi.erogazione.listaMovimenti.codiceDocLett" bundle="serviziLabels" /></td>
							<td align="left"><html:text property="detMov.progrEsempDocLet" size="5" readonly="true"
								titleKey="servizi.erogazione.listaMovimenti.progrEsempDocLett"
								bundle="serviziLabels" />
					</c:when>
					<c:otherwise>
						<tr>
							<td align="right"><bean:message key="servizi.erogazione.movimento.invColl"
								bundle="serviziLabels" /></td>
							<td align="left">
								<html:text property="inventario" size="15" readonly="true" bundle="serviziLabels">
								</html:text>&nbsp;&nbsp;/&nbsp;&nbsp;
								<html:text property="detMov.segnatura" size="35" readonly="true" bundle="serviziLabels">
								</html:text>
								<c:if test="${not navForm.cambioServizio}">
									&nbsp;<html:submit styleClass="buttonImage" property="methodDettaglioMovimentiUte" bundle="serviziLabels">
										<bean:message key="servizi.bottone.esame.inv" bundle="serviziLabels" />
									</html:submit>
								</c:if>
					</c:otherwise>
				</c:choose>

				<c:choose>
					<c:when test="${navForm.detMov.natura eq 'S'}">
						<!-- eventuale check periodico sulla stessa riga precedente-->
						&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="servizi.erogazione.movimento.periodico" bundle="serviziLabels" />&nbsp;&nbsp;
						<html:checkbox property="detMov.natura" value="S" disabled="true" />
						</td>
						</tr>
					</c:when>
					<c:otherwise>
						</td>
						</tr>
					</c:otherwise>
				</c:choose>
				<tbody id="grp_dati_doc">
				<c:if test="${navForm.detMov.richiestaILL}">
					<tr class="dati_ill">
						<td colspan="2">
							<sbn:disableAll disabled="true">
								<jsp:include page="/WEB-INF/jsp/subpages/servizi/documenti/dettaglioDoc.jsp" flush="true" />
							</sbn:disableAll>
						</td>
					</tr>
				</c:if>
				</tbody>
				<!-- riga biblioteca fornitrice-->
				<c:choose>
					<c:when test="${navForm.detMov.codBibFornitrice ne null}">
						<tr>
							<td><bean:message key="servizi.erogazione.movimento.bibFornitrice"
								bundle="serviziLabels" />
							</td>
							<td><html:text property="detMov.codBibFornitrice" readonly="true" size="5" />
							<bs:write name="navForm"
								property="detMov.descrBibFornitrice" />
							</td>
						</tr>
					</c:when>
				</c:choose>

				<tr>
				<td>&nbsp;&nbsp;</td>
				</tr>
				<!-- riga richiesta di-->
				<c:choose>
					<c:when test="${not navForm.cambioServizio}">
						<tr>
							<th align="left">
								<div class="flex-v-center">
									<input type="button" id="btn_dati_mov_locale"
											class="buttonRimuovi" onclick="toggle('#grp_dati_mov_locale'); toggle_btn('#btn_dati_mov_locale');"
											title="Apri/Chiudi dettaglio" />
									<span>
										<c:if test="${navForm.detMov.prenotazione}">
											<bean:message
												key="servizi.erogazione.dettaglioMovimento.prenotazioneDel"
												bundle="serviziLabels" />
										</c:if>
										<c:if test="${navForm.detMov.richiesta}">
											<bean:message
												key="servizi.erogazione.dettaglioMovimento.richiestaDel"
												bundle="serviziLabels" />
										</c:if>
									</span>
								</div>
							</th>
							<td align="left"><html:text property="detMov.tipoServizio" size="70" readonly="true" /></td>
						</tr>
						<!-- riga numero richiesta-->
						<tr>
							<td align="right"><bean:message key="servizi.erogazione.dettaglioMovimento.numeroRichiesta"
								bundle="serviziLabels" /></td>
							<td><html:text property="detMov.codRichServ" size="8" readonly="true"
								style="text-align:right;" />&nbsp;&nbsp;&nbsp;&nbsp;del&nbsp;&nbsp; <html:text
								property="detMov.dataRichiestaString" readonly="true" size="20"
								style="text-align:right;" />
							</td>
						</tr>
						<tbody id="grp_dati_mov_locale">
							<!-- riga data inizio e fine previste-->
							<tr>
								<td align="right"><bean:message key="servizi.erogazione.movimento.dataInizioPrevista"
									bundle="serviziLabels" />
								<td><html:text property="detMov.dataInizioPrevString" size="20"
									style="text-align:right;" readonly="true" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <bean:message
									key="servizi.erogazione.movimento.dataFinePrevista" bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;<html:text
									property="detMov.dataFinePrevString" size="20" style="text-align:right;"
									readonly="true" /></td>
							</tr>
							<!-- riga data inizio e fine effettive-->
							<tr>
								<td align="right"><bean:message key="servizi.erogazione.movimento.dataInizioEffettiva"
									bundle="serviziLabels" /></td>
								<td><html:text property="detMov.dataInizioEffString" size="20"
									style="text-align:right;" readonly="true" />
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <bean:message
									key="servizi.erogazione.movimento.dataFineEffettiva" bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;<html:text
									property="detMov.dataFineEffString" size="20" style="text-align:right;"
									readonly="true" /></td>
							</tr>
							<!-- riga stato richiesta-->
							<tr>
								<td align="right"><bean:message
									key="servizi.erogazione.movimento.statoRichiesta" bundle="serviziLabels" /></td>
								<td align="left"><html:text property="detMov.stato_richiesta" size="70" readonly="true" /></td>
							</tr>
							<tr>
								<td align="right"><bean:message
									key="servizi.erogazione.movimento.statoMovimento" bundle="serviziLabels" /></td>
								<td><html:text property="detMov.stato_movimento" size="70" readonly="true" /></td>
							</tr>
						</tbody>
						<!-- riga attività attuale-->
						<c:choose>
						<c:when test="${not navForm.detMov.prenotazione}">
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td align="right" class="btop bleft"
									><bean:message key="servizi.erogazione.movimento.attivitaAttuale"
									bundle="serviziLabels" /></td>
								<td class="btop bright"><html:text property="detMov.attivita" size="70" readonly="true" /></td>
							</tr>

							<!-- riga attività seguente-->
							<c:if test="${not navForm.detMov.nuovo}">
								<tr>
									<td align="right" class="bbottom bleft"><bean:message key="servizi.erogazione.movimento.attivitaSuccessiva"
										bundle="serviziLabels" /></td>
									<td class="bbottom bright"><html:select property="detMov.codAttivitaSucc" disabled="${navForm.conferma}">
										<html:optionsCollection property="listaAttivitaSucc"
											value="passoIter.codAttivita" label="passoIter.descrizione" />
									</html:select></td>
								</tr>
							</c:if>
							<tr><td>&nbsp;</td></tr>
						</c:when>
						<c:otherwise>
							<c:if test="${navForm.detMov.richiestaILL}">
								<!-- riga attività ill attuale -->
								<tr class="dati_ill">
									<td align="right"><bean:message key="servizi.erogazione.ill.attivita" bundle="serviziLabels" /></td>
									<td><html:text property="detMov.datiILL.descrizioneStatoRichiesta" size="70" readonly="true" /></td>
								</tr>
							</c:if>
						</c:otherwise>
						</c:choose>

						<c:if test="${navForm.detMov.natura eq 'S'}">

							<!-- riga periodico (senza check) -->
							<tr>
								<td align="right">
									<bean:message key="servizi.erogazione.movimento.volume"
									bundle="serviziLabels" />
								</td>
								<td>
									<html:text property="detMov.numVolume" size="5" maxlength="30">
									</html:text>&nbsp;&nbsp;

									<bean:message key="servizi.erogazione.movimento.annata" bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;&nbsp;

									<html:text property="detMov.annoPeriodico" size="5" disabled="${!navForm.detMov.nuovo}">
									</html:text>&nbsp;&nbsp;

									<bean:message key="servizi.erogazione.movimento.Fascicolo" bundle="serviziLabels" />&nbsp;&nbsp;

									<html:text property="detMov.numFascicolo" size="5">
									</html:text>&nbsp;&nbsp;&nbsp;&nbsp;

								</td>
							</tr>

						</c:if>

						<!-- riga supporto-->
						<tr>
							<logic:notEmpty name="navForm" property="tipiSupporto">
								<td align="right"><bean:message key="servizi.erogazione.movimento.supporto"
									bundle="serviziLabels" /></td>
								<td>
									<html:select property="detMov.codSupporto" disabled="${navForm.conferma}"
									onchange="validateSubmit('updateCombo', 'supporto');">
									<html:optionsCollection property="tipiSupporto" value="codSupporto"
										label="descrizione" />
								</html:select> <html:hidden property="cambiaSupportoDettMov" value="" />
								&nbsp;&nbsp;&nbsp;

								<bean:message key="servizi.configurazione.moduloRichiesta.intervalloCopia" bundle="serviziLabels" />&nbsp;
								<html:text property="detMov.intervalloCopia" size="20" maxlength="30" styleClass="l" readonly="${navForm.conferma}" />
								&nbsp;&nbsp;&nbsp;
								<bean:message key="servizi.erogazione.movimento.numeroPezzi"
								bundle="serviziLabels" />&nbsp;
								<html:text property="detMov.numPezzi" size="8"
								readonly="${navForm.conferma || not navForm.flgNumPezzi}"
								style="text-align:right;">
								</html:text>

								</td>
							</logic:notEmpty>
							<logic:empty name="navForm" property="tipiSupporto">
								<sbn:disableAll checkAttivita="ILL_RICHIEDENTE">
								<c:if test="${navForm.detMov.richiestaILL and navForm.detMov.datiILL.richiedente}">
									<td align="right">
										<bean:message key="servizi.configurazione.moduloRichiesta.intervalloCopia" bundle="serviziLabels" /></td>
									<td>
										<html:text property="detMov.intervalloCopia" size="20" maxlength="30" styleClass="l" readonly="${navForm.conferma}" />
									</td>
								</c:if>
								<td align="right"><bean:message
									key="servizi.erogazione.movimento.numeroPezzi" bundle="serviziLabels" /></td>
								<td><html:text property="detMov.numPezzi" size="15"
									readonly="${navForm.conferma or not navForm.flgNumPezzi}"
									styleClass="l" /></td>
								</sbn:disableAll>
							</logic:empty>
						</tr>
						<!-- riga modalità erogazione + bib prelievo + bib restituzione-->
						<tr>
							<td align="right"><bean:message key="servizi.erogazione.modErogazione"
								bundle="serviziLabels" /></td>
							<td><html:select property="detMov.cod_erog" disabled="${navForm.conferma}">
								<html:optionsCollection property="modoErogazione" value="codErog"
									label="desModErog" />
							</html:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="servizi.erogazione.movimento.bibPrelievo"
								bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;
								<html:select property="detMov.codBibPrelievo" disabled="${navForm.conferma}">
								<html:optionsCollection property="bibliotechePolo" value="codice"
									label="codice" />
							</html:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message
								key="servizi.erogazione.movimento.bibRestituzione" bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;
								<html:select property="detMov.codBibRestituzione" disabled="${navForm.conferma}">
								<html:optionsCollection property="bibliotechePolo" value="codice"
									label="codice" />
							</html:select></td>
						</tr>
						<!-- riga impMassimo-->
						<tr>
							<td align="right"><bean:message key="servizi.erogazione.movimento.importoMassimo"
								bundle="serviziLabels" /></td>
							<td>
								<sbn:disableAll checkAttivita="ILL_RICHIEDENTE">
									<html:text property="detMov.prezzoMax" size="15" readonly="${navForm.conferma}" style="text-align:right;" />
								</sbn:disableAll>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<bean:message key="servizi.erogazione.movimento.costoServizio"
								bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;<html:text property="detMov.costoServizioString"
								size="15" readonly="true" style="text-align:right;" />
								<c:if test="${navForm.detMov.richiestaILL and navForm.detMov.datiILL.richiedente}">
									&nbsp;&plus;&nbsp;
									<html:text property="detMov.datiILL.importoStr" size="15" readonly="true" style="text-align:right;" />
								</c:if>
							</td>
						</tr>
						<!-- riga dataMassima-->
						<tr>
							<td align="right"><bean:message key="servizi.erogazione.movimento.dataMassima"
								bundle="serviziLabels" /></td>
							<td>
								<sbn:disableAll checkAttivita="DATA_MASSIMA_ILL">
									<html:text property="detMov.dataMaxString" size="12" maxlength="10"
										style="text-align:right;" />
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</sbn:disableAll>
								<c:if test="${navForm.detMov.richiestaLocale}">
									<sbn:disableAll checkAttivita="RINNOVA">
										<bean:message
											key="servizi.erogazione.movimento.dataProroga" bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;<html:text
											property="detMov.dataProrogaString" size="15" maxlength="10"
											readonly="${navForm.conferma or navForm.detMov.prenotazione}"
											style="text-align:right;" />
									</sbn:disableAll>
								</c:if>
							</td>
						</tr>
						<!-- riga note utente-->
						<tr>
							<td align="right"><bean:message key="servizi.erogazione.movimento.noteUtente"
								bundle="serviziLabels" /></td><td><html:textarea property="detMov.noteUtente" rows="2"
								cols="70"
								readonly="${navForm.conferma or navForm.detMov.prenotazione}"></html:textarea></td>
						</tr>
						<!-- settima riga documento-->
						<tr>
							<td align="right"><bean:message
								key="servizi.erogazione.movimento.noteBibliotecario" bundle="serviziLabels" /></td>
							<td align="left"><html:textarea property="detMov.noteBibliotecario" rows="2" cols="70"
								readonly="${navForm.conferma}"></html:textarea></td>
						</tr>
						<c:if test="${navForm.detMov.withPrenotazionePosto}">
							<tr>
								<th align="left"><bean:message key="servizi.erogazione.dettaglioMovimento.DatiPrenotazionePosto" bundle="serviziLabels" /></th>
							</tr>
							<tr>
								<td align="right" width="8%">
									<bean:message key="servizi.sale.sala" bundle="serviziLabels" />
								</td>
								<td width="60%" align="left">
									<html:text name="navForm" property="detMov.prenotazionePosto.posto.sala.descrizione" readonly="true" />&nbsp;il
									<html:text name="navForm" property="detMov.prenotazionePosto.dataInizio" readonly="true" styleClass="date" />&nbsp;
									<bean:message key="servizi.sale.prenotazione.start" bundle="serviziLabels" />&nbsp;
									<html:text name="navForm" property="detMov.prenotazionePosto.orarioInizio" readonly="true" styleClass="time" />&nbsp;
									<bean:message key="servizi.sale.prenotazione.end" bundle="serviziLabels" />&nbsp;
									<html:text name="navForm" property="detMov.prenotazionePosto.orarioFine" readonly="true" styleClass="time" />
								</td>
							</tr>
						</c:if>
					</c:when>
					<c:otherwise>
						<!-- riga note bibliotecario-->
						<tr>
							<td align="right"><bean:message key="servizi.erogazione.servizi" bundle="serviziLabels" /></td>
							<td align="left"><html:select property="codServNuovaRich" disabled="${navForm.conferma}"
								onchange="validateSubmit('updateCombo', 'cambio');">
								<html:optionsCollection property="lstCodiciServizio" value="codTipoServ" label="descrTipoServ" />
							</html:select> &nbsp;&nbsp;&nbsp;&nbsp;
							<noscript><html:submit property="methodDettaglioMovimentiUte">
								<bean:message key="servizi.bottone.confermaServizio" bundle="serviziLabels" />
							</html:submit></noscript>
							</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
						</tr>
						<!-- ITER SERVIZIO ASSOCIATO AL SERVIZIO DI CUI SOPRA -->
						<c:choose>
							<c:when test="${not empty navForm.lstIter}">
								<table class="sintetica">
									<tr class="etichetta" bgcolor="#dde8f0">
										<th class="etichetta" scope="col" style="text-align: center;"><bean:message
											key="servizi.utenti.headerProgressivo" bundle="serviziLabels" /></th>
										<th class="etichetta" scope="col" style="text-align: center;"><bean:message
											key="servizi.erogazione.attivita" bundle="serviziLabels" /></th>
										<th style="width: 3%; text-align: center;">&nbsp;</th>
									</tr>
									<logic:iterate id="item" property="lstIter" name="navForm"
										indexId="riga">
										<sbn:rowcolor var="color" index="riga" />
										<tr>
											<td bgcolor="${color}" class="testoNormale" style="text-align: center;"><bs:write
												name="item" property="progrIter" /></td>
											<td bgcolor="${color}" class="testoNormale"><bs:write name="item"
												property="descrizione" /></td>
											<td bgcolor="${color}" class="testoNoBold"><html:radio
												property="progrIter" value="${item.progrIter}"
												titleKey="servizi.configurazione.controlloIter.selezioneSingola"
												bundle="serviziLabels" /></td>
										</tr>
									</logic:iterate>
								</table>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${navForm.confermaNuovaRichiesta}">
								<br/>
								<table width="100%" border="0">

								<logic:notEmpty name="navForm" property="tipiSupporto">
									<tr>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td align="right" width="15%"><bean:message key="servizi.erogazione.movimento.supporto"
											bundle="serviziLabels" />
										</td>
										<td ><html:select property="detMov.codSupporto" disabled="${navForm.conferma}"
											onchange="validateSubmit('updateCombo', 'supporto');">
											<html:optionsCollection property="tipiSupporto" value="codSupporto" label="descrizione" />
											</html:select>
											<html:hidden property="cambiaSupportoDettMov" value="" />
										</td>
									</tr>
								</logic:notEmpty>
								<logic:notEmpty name="navForm" property="modoErogazione">
								<tr>
								<td align="right" width="15%">
								<bean:message key="servizi.erogazione.modErogazione"
									bundle="serviziLabels" />
								</td>
								<td><html:select property="detMov.cod_erog" disabled="${navForm.conferma}">
									<html:optionsCollection property="modoErogazione" value="codErog"
										label="desModErog" />
									</html:select>
								</td>
								</tr>
								</logic:notEmpty>
								</table>
								<br/>
							</c:when>
						</c:choose>

					</c:otherwise>
				</c:choose>
				<html:hidden property="confermaServizio" value="" />
			</table>
		</sbn:disableAll></div>
		<sbn:checkAttivita idControllo="LISTA_BIB_FORNITRICI">
			<p><bean:message key="ricerca.biblioteca.sintetica.titolo" bundle="amministrazioneSistemaLabels" /></p>
			<table class=sintetica>
				<tr class="etichetta" bgcolor="#dde8f0">
					<th style="width: 8%"><bean:message key="ricerca.biblioteca.cdana" bundle="amministrazioneSistemaLabels" /></th>
					<th style="width: 8%"><bean:message key="ricerca.biblioteca.cdbib" bundle="amministrazioneSistemaLabels" /></th>
				<th><bean:message key="documentofisico.bibliotecaT"	bundle="documentoFisicoLabels" /></th>
				<th style="width: 3%" />
				</tr>
				<logic:iterate id="itemBib" property="detMov.datiILL.bibliotecheFornitrici"
					name="navForm" indexId="riga">
					<sbn:rowcolor var="color" index="riga" />
					<tr bgcolor="${color}">
						<td>
							<bs:write name="itemBib" property="cd_ana_biblioteca" />
						</td>
						<td>
							<bs:write name="itemBib" property="cod_polo" />&nbsp;<bs:write name="itemBib" property="cod_bib" />
						</td>
						<td>
							<bs:write name="itemBib" property="nom_biblioteca" />
						</td>
						<td>
							 <html:radio property="bibSelezionata" value="${itemBib.uniqueId}" disabled="${itemBib.flCanc eq 'S'}"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</sbn:checkAttivita>
		<jsp:include page="/WEB-INF/jsp/subpages/servizi/erogazione/ill/messaggio.jsp" />
		<div id="divFooter"><c:choose>
			<c:when test="${navForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/erogazione/footerDettaglioMovimento.jsp" />
			</c:otherwise>
		</c:choose></div>
	</sbn:navform>
</layout:page>
<script type="text/javascript">
	var ill = ${navForm.detMov.richiestaILL};
</script>
<script type="text/javascript" src='<c:url value="/scripts/servizi/movimento/dettaglio.js" />'></script>
