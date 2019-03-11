<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/configurazione/DettaglioServizio.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors bundle="serviziMessages" />
		</div>

		<div id="content"><!-- BIBLIOTECA E TIPO SERVIZIO SELEZIONATO -->
		<br />
		<div>
		<div class="etichetta"
			style="float: left; width: 100px; font-weight: bold;"><bean:message
			key="servizi.utenti.biblioteca" bundle="serviziLabels" />&nbsp;&nbsp;
		</div>
		<div style="float: none;"><html:text styleId="testoNoBold"
			readonly="true" property="biblioteca" size="8"></html:text></div>
		</div>
		<br />
		<div>
		<div class="etichetta"
			style="float: left; width: 100px; font-weight: bold;"><bean:message
			key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />&nbsp;&nbsp;
		</div>
		<div style="float: none;"><html:text styleId="testoNoBold"
			readonly="true" property="desTipoServizio" size="50"></html:text></div>
		</div>
		<br />
		<hr align="left">
		<br />
		<sbn:disableAll checkAttivita="GESTIONE" disabled="${navForm.conferma}">
		<!-- DETTAGLIO SERVIZIO -->
		<div>
		<div class="etichetta" style="float: left; font-weight: bold;">
		<bean:message key="servizi.utenti.titServizio" bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		<div style="float: left;"><html:text
			readonly="${not navForm.nuovo}"
			property="servizio.codServ" size="3" maxlength="3"
			titleKey="servizi.utenti.titCodice" bundle="serviziLabels">
		</html:text>&nbsp;&nbsp;</div>
		<div style="float: none;"><html:text property="servizio.descr"
			size="50" maxlength="255"
			readonly="${navForm.conferma}"
			titleKey="servizi.label.descrizione" bundle="serviziLabels">
		</html:text></div>
		</div>
		<br />
		<br />

		<div style="width: 60%;">
		<div style="width: 50%; float: left;">
		<div>
		<div style="float: right;"><html:text
			property="servizio.numMaxRich" size="2" maxlength="3"
			readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.MaxRichieste"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		<br />
		<div style="float: right;"><html:text
			property="servizio.numMaxMov" size="2" maxlength="3"
			readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.MaxMovimenti"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		<br />
		<div style="float: right;"><html:text
			property="servizio.numMaxRiprod" size="2" maxlength="3"
			readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.MaxRiproduzioneRichiesta"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		<br />
		<div style="float: right;"><html:text
			property="servizio.numMaxPrenPosto" size="2" maxlength="2"
			readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.numMaxPrenPostoUtente"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		<br />
		<div style="float: right;"><html:text
			property="servizio.maxGgDep" size="2" maxlength="3"
			readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.durataDeposito"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		<br />
		<div style="float: right;"><html:text
			property="servizio.maxGgAnt" size="2" maxlength="3"
			readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.MaxDocRiservato"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		</div>
		<br />
		</div>

		<div style="float: none;">
		<div>
		<div style="float: right;"><html:text property="servizio.durMov"
			size="2" maxlength="3" readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.durataServizio"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		<br />
		<div style="float: right;"><html:text
			property="servizio.durMaxRinn1" size="2" maxlength="3"
			readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.durataPrimoRinnovo"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		<br />
		<div style="float: right;"><html:text
			property="servizio.durMaxRinn2" size="2" maxlength="3"
			readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.durataSecondoRinnovo"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		<br />
		<div style="float: right;"><html:text
			property="servizio.durMaxRinn3" size="2" maxlength="3"
			readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.durataTerzoRinnovo"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		</div>
		<br />

		<%--
		<div style="float: right;"><html:text
			property="servizio.NMaxPren" size="2" maxlength="3"
			readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.MaxPrenotazioni"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		<br />
		<div style="float: right;"><html:text
			property="servizio.NMaxGgvalPren" size="2" maxlength="3"
			readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.durataPrenotazione"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		<br />
		--%>

		<div style="float: right;"><html:text
			property="servizio.maxGgCons" size="2" maxlength="3"
			readonly="${navForm.conferma}">
		</html:text></div>
		<div style="text-align: right; float: none;"><bean:message
			key="servizi.configurazione.dettaglioServizio.Tolleranza"
			bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
		<br />
		<sbn:checkAttivita idControllo="SERVIZI_ILL">
			<div style="float: right;"><html:text
				property="servizio.giorniRestituzioneRichiedente" size="2" maxlength="3"
				readonly="${navForm.conferma}">
			</html:text></div>
			<div style="text-align: right; float: none;"><bean:message
				key="servizi.configurazione.dettaglioServizio.giorniRestituzioneRichiedente"
				bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
			<br />
		</sbn:checkAttivita>
		</div>
		</div>
		<br />
		<br />

		<c:choose>
			<c:when test="${navForm.penalita}">


				<div style="width: 70%; text-align: center; font-weight: bold;">
				<bean:message key="servizi.configurazione.dettaglio.penalitaUpper"
					bundle="serviziLabels" /></div>
				<div style="width: 60%;"><br />
				<div style="width: 50%; float: left;">
				<div>
				<div style="float: right;"><html:text
					property="servizio.penalita.ggSosp" size="2" maxlength="3"
					readonly="${navForm.conferma}">
				</html:text></div>
				<div style="text-align: right; float: none;"><bean:message
					key="servizi.configurazione.dettaglioServizio.giorniSospensione"
					bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
				<br />
				</div>
				</div>

				<div style="float: none;">
				<div>
				<div style="float: right;"><html:text
					property="servizio.penalita.coeffSosp" size="2" maxlength="2"
					readonly="${navForm.conferma}">
				</html:text></div>
				<div style="text-align: right; float: none;"><bean:message
					key="servizi.configurazione.dettaglioServizio.coefficienteSospensione"
					bundle="serviziLabels" />:&nbsp;&nbsp;&nbsp;</div>
				<br />
				</div>
				</div>
				</div>

			</c:when>

		</c:choose>

		<sbn:checkAttivita idControllo="PRENOTAZIONE_POSTO">
			<div style="width: 70%; text-align: center; font-weight: bold;">
				<bean:message key="servizi.erogazione.dettaglioMovimento.DatiPrenotazionePosto" bundle="serviziLabels" />
			</div>
			<div style="width: 60%;">
				<br />
				<div style="width: 50%; float: left;">
					<div>
						<div style="float: right;">
							<html:text property="servizio.numGgPrepSupp" size="2" maxlength="2" readonly="${navForm.conferma}"/>
						</div>
						<div style="text-align: right; float: none;">
							<bean:message key="servizi.configurazione.dettaglioServizio.numGgPrepSupp" bundle="serviziLabels" />&colon;&nbsp;&nbsp;&nbsp;
						</div>
						<br />
					</div>
				</div>
				<div style="float: none;">
					<div>
						<div style="float: right;">
							<html:text property="servizio.orarioLimitePrepSupp" size="4" maxlength="5" readonly="${navForm.conferma}"/>&sup1;
						</div>
						<div style="text-align: right; float: none;">
							<bean:message key="servizi.configurazione.dettaglioServizio.orarioLimitePrepSupp" bundle="serviziLabels" />&colon;&nbsp;&nbsp;&nbsp;
						</div>
						<br />
					</div>
				</div>
			</div>
			<div class="msgOK1n">&sup1;&nbsp;</div>
			<div class="msgOK1">
				<bean:message key="servizi.configurazione.dettaglioServizio.orarioLimitePrepSupp.nota" bundle="serviziLabels" />
			</</div>
		</sbn:checkAttivita>

		</sbn:disableAll>
		</div>

		<br />

		<div id="divFooter">
		<div style="text-align: center; width: 100%;">
		<div style="width: 20%; margin: auto auto; text-align: left;"><c:choose>
			<c:when test="${navForm.conferma}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
			</c:when>
			<c:otherwise>
				<sbn:checkAttivita idControllo="GESTIONE">
				<html:submit property="methodDettaglioServizio">
					<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
				</html:submit>
				</sbn:checkAttivita>
				<html:submit property="methodDettaglioServizio">
					<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
				</html:submit>
			</c:otherwise>
		</c:choose></div>
		</div>
		</div>

		</div>
	</sbn:navform>
</layout:page>
