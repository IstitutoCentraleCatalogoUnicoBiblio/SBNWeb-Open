<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/configurazione/DettaglioSupporto.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br/>

			<div id="content">
				<div>
					<div class="etichetta" style="float:left; font-weight:bold;">
						<bean:message key="servizi.utenti.biblioteca" bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;&nbsp;
					</div>
					<div style="float:none;">
						<html:text styleId="testoNoBold" readonly="true" property="supporto.cd_bib" size="8"></html:text>
					</div>
				</div>
				<br/>
				<hr align="left">
				<br/>
				<sbn:disableAll checkAttivita="GESTIONE">
				<div class="etichetta" style="float:none; width:50%; font-weight:bold;">
					<bean:message key="servizi.erogazione.contesto" bundle="serviziLabels" />&nbsp;&nbsp;
					<html:hidden property="updateCombo" />
					<sbn:disableAll checkAttivita="SERVIZI_ILL">
					<html:select property="tipoSvolgimento"  disabled="${DettaglioSupportoForm.conferma or not DettaglioSupportoForm.nuovo}"
						onchange="validateSubmit('updateCombo', 'svolgimento');">
						<html:optionsCollection property="lstSvolgimento" value="cd_tabellaTrim" label="ds_tabella" />
					</html:select>
					</sbn:disableAll>

				</div>
				<br/>
				<div class="etichetta" style="float:left; width:50%; font-weight:bold;">
					<bean:message key="servizi.configurazione.supporti.supporto" bundle="serviziLabels" />&nbsp;&nbsp;
					<c:choose>
						<c:when test="${DettaglioSupportoForm.nuovo}">
							<html:select property="codiceSupporto"  disabled="${DettaglioSupportoForm.conferma}">
								<html:optionsCollection property="lstSupporti" value="cd_tabellaTrim" label="ds_tabella" />
							</html:select>
						</c:when>
						<c:otherwise>
							<html:text readonly="true" property="supporto.descrizione" size="50"></html:text>
						</c:otherwise>
					</c:choose>
				</div>
				<br/>
				<br/>
				<br/>

				<div style="width:100px; float:left; font-weight:bold;">
					<bean:message key="servizi.configurazione.supporti.dettaglio.importo" bundle="serviziLabels" />&nbsp;&nbsp;
				</div>
				<div style="float:none;">
					<html:text property="supporto.costoFisso" size="8" readonly="${DettaglioSupportoForm.conferma}" style="text-align:right;"></html:text>
				</div>
				<br/>

				<div style="width:100px; float:left; font-weight:bold;">
					<bean:message key="servizi.configurazione.supporti.dettaglio.numMaxRiprod" bundle="serviziLabels" />&nbsp;&nbsp;
				</div>
				<div style="float:none;">
					<html:text property="supporto.importoUnitario" size="8" readonly="${DettaglioSupportoForm.conferma}" style="text-align:right;"></html:text>
				</div>
				<br/>
				</sbn:disableAll>
			</div>

			<br/>

			<c:choose>
				<c:when test="${!DettaglioSupportoForm.nuovo}">
					<div style="width:100%;" class="SchedaImg1">
						<div style="width:100%; float:left;">
							<div class="schedaOn" style="text-align: center;">
							<bean:message key="servizi.bottone.modalitaErogazione" bundle="serviziLabels" />
							</div>
						</div>
					</div>
					<br/>
					<br/>
					<jsp:include page="/WEB-INF/jsp/subpages/servizi/configurazione/gestioneModalitaErogazioneSupporti.jsp" />
				</c:when>
			</c:choose>

			<c:choose>
    			<c:when test="${DettaglioSupportoForm.stringaMessaggioSupportiModalita ne ''}">
					<div class="msgWarning1"><bean-struts:write name="DettaglioSupportoForm" property="stringaMessaggioSupportiModalita" />
					</div>
					<br />
				</c:when>
				<c:otherwise>
					<br />
				</c:otherwise>
			</c:choose>

			<div id="divFooter">
				<div style="text-align:center; width:100%;">
					<div>
						<c:choose>
							<c:when test="${DettaglioSupportoForm.conferma}">
								<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
							</c:when>
							<c:otherwise>

								<c:choose>
									<c:when test="${not DettaglioSupportoForm.nuovo}">
										<sbn:checkAttivita idControllo="GESTIONE">
										<html:submit property="methodDettaglioSupporto" titleKey="servizi.configurazione.supporti.modalitaErogazione.inserisce"
								 		bundle="serviziLabels">
										<bean:message key="servizi.bottone.nuova"    bundle="serviziLabels" />
										</html:submit>
										&nbsp;&nbsp;
										</sbn:checkAttivita>
									</c:when>
								</c:choose>

								<c:choose>
									<c:when test="${not empty DettaglioSupportoForm.lstSupportiModalitaErogazione}">
									<sbn:checkAttivita idControllo="GESTIONE">
										<html:submit property="methodDettaglioSupporto" titleKey="servizi.configurazione.supporti.modalitaErogazione.cancella"
										 bundle="serviziLabels">
											<bean:message key="servizi.bottone.cancella"  bundle="serviziLabels" />
										</html:submit>
										&nbsp;&nbsp;
									</sbn:checkAttivita>
									</c:when>
								</c:choose>

								<sbn:checkAttivita idControllo="GESTIONE">
								<html:submit property="methodDettaglioSupporto" titleKey="servizi.configurazione.supporti.salva"
								 		bundle="serviziLabels">
									<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
								</html:submit>
								&nbsp;&nbsp;
								</sbn:checkAttivita>
								<html:submit property="methodDettaglioSupporto" titleKey="servizi.configurazione.servizio.chiude"
								 		bundle="serviziLabels">
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
