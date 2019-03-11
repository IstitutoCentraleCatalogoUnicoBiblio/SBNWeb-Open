<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/erogazione/datiRichiestaILL.do">

		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<sbn:disableAll disabled="${navForm.dati.cod_rich_serv gt 0 or navForm.conferma}" checkAttivita="LAST_FORM">
				<br/>
				<table width="100%" border="0">
					<tr>
						<td width="100px" class="etichetta" align="right">
								<bean:message key="servizi.erogazione.codRichILL" bundle="serviziLabels" />
						</td>
						<td width="150px" align="left"><html:text
								styleId="testoNoBold" property="dati.transactionId" size="16"
								readonly="true" /></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td width="100px" class="etichetta" align="right">
								<bean:message key="servizi.erogazione.codRich" bundle="serviziLabels" />
						</td>
						<td width="150px" align="left"><html:text
								styleId="testoNoBold" property="dati.cod_rich_serv" size="16"
								readonly="true" /></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td width="100px" class="etichetta" align="right">
								<bean:message key="servizi.erogazione.servizi" bundle="serviziLabels" />
						</td>
						<td width="150px" align="left">
							<html:text styleId="testoNoBold" property="dati.descrizioneServizio" size="30" readonly="true" />
						</td>
						<c:if test="${navForm.dati.richiedente}">
							<td class="r">
								<span class="testoBold"><bean:message key="servizi.erogazione.dettaglioMovimento.DatiBibFornitrice" bundle="serviziLabels" /></span>
							</td>
							<td>
								<html:text styleId="testoNoBold" property="dati.responderId" size="8" readonly="true" />&nbsp;
								<html:text styleId="testoNoBold" property="dati.denominazioneBibFornitrice" size="50" readonly="true" />
							</td>
						</c:if>
					</tr>
					<tr>
						<td width="100px" class="etichetta" align="right">
								<bean:message key="servizi.erogazione.stato" bundle="serviziLabels" />
						</td>
						<td width="150px" align="left"><html:text
								styleId="testoNoBold" property="dati.descrizioneStatoRichiesta" size="30"
								readonly="true" /></td>
						<td class="r"><bean:message key="servizi.erogazione.movimento.dataScadenza" bundle="serviziLabels" /></td>
						<td><html:text property="dati.dataScadenzaString" size="8" readonly="true" /></td>
					</tr>
					<tr>
						<td width="100px" class="etichetta" align="right">
								<bean:message key="servizi.erogazione.movimento.ill.supplyMediumType" bundle="serviziLabels" />
						</td>
						<td width="150px" align="left">
							<html:select property="dati.cd_supporto" >
								<html:optionsCollection property="tipiSupportoILL" value="cd_tabellaTrim" label="ds_tabella" />
							</html:select>
						</td>
						<td class="r">
								<bean:message key="servizi.erogazione.movimento.ill.deliveryService" bundle="serviziLabels" />
						</td>
						<td align="left">
							<html:select property="dati.cod_erog" >
								<html:optionsCollection property="modoErogazioneILL" value="cd_tabellaTrim" label="ds_tabella" />
							</html:select>
						</td>
					</tr>
					<tr>
						<td width="100px" class="etichetta" align="right">
							<bean:message key="servizi.erogazione.movimento.importoMassimo"	bundle="serviziLabels" />
						</td>
						<td>
							<html:text property="dati.costoMaxStr" size="15" readonly="true" style="text-align:right;" />
						</td>
						<td class="r">
							<bean:message key="servizi.erogazione.movimento.costoServizio" bundle="serviziLabels" />
						</td>
						<td>
							<html:text property="dati.importoStr" size="15" readonly="true" style="text-align:right;" />
						</td>
					</tr>
				</table>

				<hr/>

				<table width="100%" border="0">
					<tr>
						<th align="left">
							<c:if test="${navForm.dati.fornitrice}">
								<bean:message key="servizi.erogazione.dettaglioMovimento.DatiBibRichiedente" bundle="serviziLabels" />
							</c:if>
							<c:if test="${navForm.dati.richiedente}">
								<bean:message key="servizi.erogazione.dettaglioMovimento.DatiUtente" bundle="serviziLabels" />
							</c:if>
						</th>
					</tr>
					<c:if test="${navForm.dati.fornitrice}">
					<!-- denominazione utente biblioteca  -->
					<tr>
						<td width="100px" class="etichetta" align="right">
								<bean:message key="servizi.utenti.headerBiblioteca"	bundle="serviziLabels" />
						</td>
						<td width="150px" align="left" colspan="2">
							<html:text
								styleId="testoNoBold" property="dati.requesterId" size="8" readonly="true" />&nbsp;
							<html:text
								styleId="testoNoBold" property="dati.codUtenteBibRichiedente" size="16"	readonly="true"/>&nbsp;
							<html:text
								styleId="testoNoBold" property="dati.denominazioneBibRichiedente" size="50"	readonly="true" />&nbsp;
							<html:submit styleClass="buttonImage" property="methodDatiILL" bundle="serviziLabels">
								<bean:message key="servizi.bottone.esame.utente" bundle="serviziLabels" />
							</html:submit>
						</td>

						<td>&nbsp;</td>
					</tr>
					</c:if>

					<c:if test="${navForm.dati.richiedente}">
					<tr>
						<td width="100px" class="etichetta" align="right">
								<bean:message key="servizi.utenti.codUtente"
									bundle="serviziLabels" />
						</td>
						<td width="150px" align="left"><html:text
								styleId="testoNoBold" property="dati.codUtente" size="16"
								readonly="true" /></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td width="100px" class="etichetta" align="right"><bean:message
								key="servizi.erog.Utente" bundle="serviziLabels" /></td>
						<td width="150px" align="left">
							<html:text
								styleId="testoNoBold" property="dati.cognomeNome" style="width: 90%;" maxlength="80" disabled="${navForm.conferma}" />
								&nbsp;<html:submit styleClass="buttonImage"	property="methodDatiILL" bundle="serviziLabels">
											<bean:message key="servizi.bottone.esame.utente" bundle="serviziLabels" />
									</html:submit>
						</td>
					</tr>
					</c:if>
					<tr>
						<td width="60px" class="etichetta" align="right"><bean:message
								key="servizi.utenti.indirizzoRes" bundle="serviziLabels" /></td>
						<td class="testoNoBold"><html:text styleId="testoNoBold"
								property="dati.via" size="75" maxlength="50"
								disabled="${navForm.conferma}" /></td>
						<td width="40px" class="etichetta" align="right"><bean:message
								key="servizi.utenti.capRes" bundle="serviziLabels" /></td>
						<td class="testoNoBold"><html:text styleId="testoNoBold"
								property="dati.cap" size="5" maxlength="5"
								disabled="${navForm.conferma}" /></td>
					</tr>
					<tr>
						<td width="60px" class="etichetta" align="right"><bean:message
								key="servizi.utenti.cittaRes" bundle="serviziLabels" /></td>
						<td class="testoNoBold"><html:text styleId="testoNoBold"
								property="dati.comune" size="75" maxlength="50"
								disabled="${navForm.conferma}" /></td>
						<td width="40px" class="etichetta" align="right"><bean:message
								key="servizi.utenti.provRes" bundle="serviziLabels" /></td>
						<td class="testoNoBold"><html:select property="dati.prov"
								disabled="${navForm.conferma}">
								<html:optionsCollection property="listaProvincia" value="codice"
									label="descrizioneCodice" />
							</html:select></td>
					</tr>

					<tr>
						<td width="60px" class="etichetta" align="right"><bean:message
								key="servizi.utenti.nazRes" bundle="serviziLabels" /></td>
						<td align="left"><html:select property="dati.cd_paese"
								disabled="${navForm.conferma}">
								<html:optionsCollection property="listaPaesi" value="codice"
									label="descrizioneCodice" />
							</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
								key="servizi.erogazione.movimento.email" bundle="serviziLabels" /></td>
						<td><html:text property="dati.requester_email"
								readonly="true" size="30" bundle="serviziLabels" /> <c:if
								test="${not empty navForm.dati.requester_email}">
								<a href="mailto://${navForm.dati.requester_email}"> <html:img
										altKey="servizi.erogazione.movimento.email"
										bundle="serviziLabels" page="/styles/images/email18.png"
										styleClass="mailto" />
								</a>
							</c:if></td>
					</tr>
				</table>
				<br />
				<hr />
				<c:if test="${navForm.movimento.nuovo}">
					<sbn:checkAttivita idControllo="FORNITRICE">
						<div style="float: right;">
							<sbn:bottoniera buttons="pulsantiInventario" />
						</div>
					</sbn:checkAttivita>
				</c:if>
				<c:choose>
					<c:when test="${navForm.dati.inventarioPresente}">
					<!-- dati inventario -->
						<table width="100%" border="0" cellpadding="1" cellspacing="1">
							<tr>
								<th align="left"><bean:message key="servizi.erogazione.dettaglioMovimento.DatiDocumento"
									bundle="serviziLabels" /></th>
							</tr>
							<tr>
								<td align="right"><bean:message	key="servizi.erogazione.listaMovimenti.titoloDocumento"
										bundle="serviziLabels" /></td>
								<td align="left"><html:text property="dati.inventario.bid" size="10"
										readonly="true" bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;&nbsp;<html:text
										property="dati.titolo" size="80" readonly="true"
										bundle="serviziLabels" /></td>
							</tr>
							<tr>
								<td align="right"><bean:message
										key="servizi.erogazione.movimento.invColl"
										bundle="serviziLabels" /></td>
								<td align="left"><html:text property="dati.inventario.chiaveInventario" size="15"
										readonly="true" bundle="serviziLabels">
									</html:text>&nbsp;&nbsp;/&nbsp;&nbsp; <html:text
										property="dati.segnatura" size="35" readonly="true"
										bundle="serviziLabels">
									</html:text></td>
							</tr>
						</table>
					</c:when>
					<c:otherwise>
						<!-- dati documento non sbn -->
						<table width="100%" border="0" cellpadding="1" cellspacing="1">
							<tr>
								<th align="left"><bean:message key="servizi.erogazione.dettaglioMovimento.DatiDocumento"
									bundle="serviziLabels" /></th>
							</tr>
						</table>
						<jsp:include page="/WEB-INF/jsp/subpages/servizi/documenti/dettaglioDoc.jsp" flush="true" />

					</c:otherwise>
				</c:choose>

				<br/>
				<l:notEmpty name="navForm" property="dati.messaggio">
					<hr />
					<table>
						<tr>
							<th align="left"><bean:message
									key="servizi.erogazione.ill.lista.messaggi" bundle="serviziLabels" /></th>
						</tr>
					</table>
					<table class="sintetica">
						<tr class="header">
							<th><bean:message key="servizi.erogazione.ill.messaggio.data" bundle="serviziLabels" /></th>
							<th colspan="2"><bean:message key="servizi.utenti.headerTipo" bundle="serviziLabels" /></th>
							<th><bean:message key="servizi.erogazione.stato" bundle="serviziLabels" /></th>
							<th><bean:message key="servizi.erogazione.ill.condizione" bundle="serviziLabels" /></th>
							<th><bean:message key="servizi.erogazione.ill.messaggio" bundle="serviziLabels" /></th>
						</tr>
						<l:iterate id="msg" property="dati.messaggio" name="navForm" indexId="progr">
							<tr class="row alt-color">
								<td>
									<bs:write name="msg" property="dataMessaggio" format="dd/MM/yyyy" />
								</td>
								<td class="w10em">
									<c:if test="${msg.tipoInvio eq 'INVIATO'}">
										<bean:message key="servizi.erogazione.ill.messaggio.inviato.a" bundle="serviziLabels"/>
									</c:if>
									<c:if test="${msg.tipoInvio eq 'RICEVUTO'}">
										<bean:message key="servizi.erogazione.ill.messaggio.ricevuto.da" bundle="serviziLabels"/>
									</c:if>
								</td>
								<td class="w10em"><bs:write	name="msg" property="isil" /></td>
								<td><bs:write name="msg" property="descrizioneStatoRichiesta" /></td>
								<td><bs:write name="msg" property="descrizioneCondizione" /></td>
								<td><bs:write name="msg" property="note" /></td>
							</tr>
						</l:iterate>
					</table>
				</l:notEmpty>
			</sbn:disableAll>
			<br/>
		</div>

		<sbn:checkAttivita idControllo="SELEZIONE_SERVIZIO_LOCALE">
			<style>
				.modalDialog {
					position: fixed;
					top: 0;
					right: 0;
					bottom: 0;
					left: 0;
					background: rgba(0,0,0,0.8);
					z-index: 99999;
					opacity:1;
					-webkit-transition: opacity 400ms ease-in;
					-moz-transition: opacity 400ms ease-in;
					transition: opacity 400ms ease-in;
					pointer-events: auto;
				}

				.modalDialog > div {
					width: 400px;
					position: relative;
					margin: 10% auto;
					padding: 5px 20px 13px 20px;
					border-radius: 10px;
					background: #FEF1E2;
				}
			</style>
			<div class="modalDialog">
				<div>
					<p>La configurazione di biblioteca prevede più servizi locali legati al servizio ILL della richiesta.</p>
					<p>Scegli il servizio locale:</p>
					<html:select property="servizioLocale.codTipoServ">
						<html:optionsCollection property="serviziLocali" value="codTipoServ" label="descrTipoServ" />
					</html:select>
					<html:submit property="${navButtons}"><bean:message key="servizi.bottone.si" bundle="serviziLabels" /></html:submit>
					<html:submit property="${navButtons}"><bean:message key="servizi.bottone.no" bundle="serviziLabels" /></html:submit>
				</div>
			</div>
		</sbn:checkAttivita>

		<jsp:include page="/WEB-INF/jsp/subpages/servizi/erogazione/ill/messaggio.jsp" />

		<div id="divFooter">
			<table align="center">
				<tr>
					<td><sbn:bottoniera buttons="pulsanti" /></td>
				</tr>
			</table>
		</div>
	</sbn:navform>

</layout:page>