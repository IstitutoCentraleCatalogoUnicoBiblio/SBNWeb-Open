<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<html:xhtml />
<layout:page>
	<sbn:navform  action="/servizi/configurazione/DettaglioModalita.do">
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
						<html:text styleId="testoNoBold" readonly="true" property="desTipoServizio" size="50"></html:text>
					</div>
				</div>
				<br/>
				<hr align="left">
				<br/>


				<!-- DETTAGLIO MODALITA' EROGAZIONE -->
				<c:choose>
					<c:when test="${not DettaglioModalitaForm.nuovo}">

						<div>
							<div class="etichetta" style="width:100px; float:left; font-weight:bold;">
								<bean:message key="servizi.erogazione.modErogazione" bundle="serviziLabels" />
							</div>
							<div style="float:left;">
								<html:text readonly="true" property="tariffaModalitaErogazione.codErog" size="3" titleKey="servizi.utenti.titCodice" bundle="serviziLabels"></html:text>&nbsp;&nbsp;
							</div>
							<div style="float:none;">
								<html:text readonly="true" property="tariffaModalitaErogazione.desModErog" size="50" titleKey="servizi.label.descrizione" bundle="serviziLabels"></html:text>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<sbn:disableAll checkAttivita="GESTIONE">
						<div>
							<div class="etichetta" style="width:100px; float:left; font-weight:bold;">
								<bean:message key="servizi.erogazione.modErogazione" bundle="serviziLabels" />
							</div>
							<div style="float:none;">
								<html:select property="tariffaModalitaErogazione.codErog" >
									<html:optionsCollection property="lstModalitaErogazione"
															value="codice"
															label="descrizione" />
								</html:select>
							</div>
						</div>
						</sbn:disableAll>
					</c:otherwise>
				</c:choose>

				<br/>
				<br/>

			</div>


			<br/>
			<div id="divFooter">
				<div style="text-align:center; width:100%;">
					<div style="width:20%; margin:auto auto; text-align:left;">
						<c:choose>
							<c:when test="${DettaglioModalitaForm.conferma}">
								<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
							</c:when>
							<c:otherwise>
							<sbn:checkAttivita idControllo="GESTIONE">
							<html:submit property="methodDettaglioModalita" titleKey="servizi.bottone.salvaModalitaErogazioneServizio" bundle="serviziLabels">
								<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
							</html:submit>
							</sbn:checkAttivita>
							<html:submit property="methodDettaglioModalita" titleKey="servizi.configurazione.servizio.chiude" bundle="serviziLabels">
								<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
							</html:submit>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</sbn:navform>
</layout:page>
