<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/configurazione/DettaglioAttivita.do">
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

				<jsp:include page="/WEB-INF/jsp/subpages/servizi/configurazione/gestioneDettagliAttivita.jsp" />
			</div>


			<br/>
			<div id="divFooter">
				<div style="text-align:center; width:100%;">
					<c:choose>
						<c:when test="${navForm.confermaContinuaConfigurazione}">
							<div style="width:20%; margin:auto auto; text-align:left;">
								<jsp:include page="/WEB-INF/jsp/subpages/servizi/configurazione/continuaConfigurazioneAttivita.jsp"></jsp:include>
							</div>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${navForm.conferma}">
									<div style="width:20%; margin:auto auto; text-align:center;">
										<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
									</div>
								</c:when>
								<c:otherwise>
									<div style="width:45%; margin:auto auto; text-align:center;">
										<c:choose>
											<c:when test="${not navForm.nuovo}">
													<html:submit property="methodDettaglioAttivita" titleKey="servizi.configurazione.servizio.bibliotecariDaAutorizzare" bundle="serviziLabels">
														<bean:message key="servizi.bottone.bibliotecari" bundle="serviziLabels" />
													</html:submit>
													<html:submit property="methodDettaglioAttivita" titleKey="servizi.configurazione.servizio.controlli" bundle="serviziLabels">
														<bean:message key="servizi.bottone.controlloIter" bundle="serviziLabels" />
													</html:submit>
											</c:when>
										</c:choose>
										<sbn:checkAttivita idControllo="GESTIONE">
										<html:submit property="methodDettaglioAttivita" titleKey="servizi.configurazione.servizio.salvaAttivita" bundle="serviziLabels">
											<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
										</html:submit>
										</sbn:checkAttivita>
										<html:submit property="methodDettaglioAttivita" titleKey="servizi.configurazione.servizio.indietro" bundle="serviziLabels">
											<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
										</html:submit>
									</div>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</sbn:navform>
</layout:page>
