<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/configurazione/DettaglioControlloIter.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>


			<div id="content">
				<!-- BIBLIOTECA E TIPO SERVIZIO SELEZIONATO -->
				<br/>
				<div>
					<div class="etichetta" style="float:left; width:100px; font-weight:bold;">
						<bean:message key="servizi.utenti.biblioteca" bundle="serviziLabels" />&nbsp;&nbsp;
					</div>
					<div style="float:none;">
						<html:text styleId="testoNoBold" readonly="true" property="biblioteca" size="8"></html:text>
					</div>
				</div>
				<br/>
				<div>
					<div class="etichetta" style="float:left; width:100px; font-weight:bold;">
						<bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />&nbsp;&nbsp;
					</div>
					<div style="float:none;">
						<html:text styleId="testoNoBold" readonly="true" property="tipoServizioVO.descrizione" size="50"></html:text>
					</div>
				</div>
				<br/>
				<hr align="left">
				<br/>
				<div>
					<!-- ATTIVITA' SELEZIONATA -->
					<div class="etichetta" style="float:left; width:100px;">
						<bean:message key="servizi.erogazione.attivita" bundle="serviziLabels" />&nbsp;&nbsp;
					</div>
					<div style="float:none;">
						<html:text styleId="testoNoBold" readonly="true" property="iterServizioVO.descrizione" size="50"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<br/>
				<sbn:disableAll checkAttivita="GESTIONE">
				<!-- CODICE CONTROLLO -->
				<div>
					<div class="etichetta" style="float:left; width:100px;">
						<bean:message key="servizi.configurazione.controlloIter.codiceControllo" bundle="serviziLabels" />&nbsp;&nbsp;
					</div>
					<div style="float:left;">
							<c:choose>
								<c:when test="${DettaglioControlloIterForm.nuovo}">
									<html:select property="conrolloScelto">
										<html:optionsCollection property="lstCodiciControllo"
											value="codiceDescrizione" label="descrizione" />
									</html:select>&nbsp;&nbsp;
								</c:when>
								<c:otherwise>
										<html:text styleId="testoNoBold" readonly="true" property="controlloIterVO.descControllo" size="50"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</c:otherwise>
							</c:choose>
					</div>
					<!-- BLOCCANTE -->
					<div style="float:none;">
						<bean:message key="servizi.bottone.bloccante" bundle="serviziLabels" />&nbsp;&nbsp;
						<html:checkbox property="bloccante" disabled="${DettaglioControlloIterForm.conferma}" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
				</div>
				<br/>

				<!-- TESTO DEL MESSAGGIO -->
				<div>
					<div class="etichetta" style="float:left; width:100px;">
						<bean:message key="servizi.configurazione.dettaglioAttivita.messaggio" bundle="serviziLabels" />&nbsp;&nbsp;
					</div>
					<div style="float:none;">
						<html:text styleId="testoNoBold" disabled="${DettaglioControlloIterForm.conferma}" property="controlloIterVO.messaggio" size="80" maxlength="255"></html:text>
					</div>
				</div>
				</sbn:disableAll>
			</div>
			<br/>


			<br/>
			<div id="divFooter">
				<div style="text-align:center; width:100%;">
					<div style="width:20%; margin:auto auto; text-align:left;">
						<c:choose>
							<c:when test="${DettaglioControlloIterForm.conferma}">
								<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
							</c:when>
							<c:otherwise>
								<sbn:checkAttivita idControllo="GESTIONE">
								<html:submit property="methodDettaglioControlloIter" titleKey="servizi.configurazione.servizio.salvaControllo" bundle="serviziLabels">
									<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
								</html:submit>
								</sbn:checkAttivita>
								<html:submit property="methodDettaglioControlloIter" titleKey="servizi.configurazione.servizio.indietro" bundle="serviziLabels">
									<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
								</html:submit>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</sbn:navform>
</layout:page>
