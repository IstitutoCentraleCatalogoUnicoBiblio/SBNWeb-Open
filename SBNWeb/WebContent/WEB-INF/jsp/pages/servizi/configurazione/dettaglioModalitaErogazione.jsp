<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<html:xhtml />
<layout:page>
	<sbn:navform  action="/servizi/configurazione/DettaglioModalitaErogazione.do">
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

				<hr align="left">
				<br/>
				<div>
					<div class="etichetta" style="float:none; width:50%; font-weight:bold;">
					<bean:message key="servizi.erogazione.contesto" bundle="serviziLabels" />&nbsp;&nbsp;
					<html:hidden property="updateCombo" />
					<sbn:disableAll checkAttivita="SERVIZI_ILL">
					<html:select property="tipoSvolgimento"  disabled="${DettaglioModalitaErogazioneForm.conferma or not DettaglioModalitaErogazioneForm.nuovo}"
						onchange="validateSubmit('updateCombo', 'svolgimento');">
						<html:optionsCollection property="lstSvolgimento" value="cd_tabellaTrim" label="ds_tabella" />
					</html:select>
					</sbn:disableAll>
				</div>
				<br/>
				<!-- DETTAGLIO MODALITA' EROGAZIONE -->
				<c:choose>
					<c:when test="${not DettaglioModalitaErogazioneForm.nuovo}">

						<div>
							<div class="etichetta" style="width:50%; float:left; font-weight:bold;">
								<bean:message key="servizi.erogazione.modErogazione" bundle="serviziLabels" />

								<html:text readonly="true" property="tariffaModalitaErogazione.codErog" size="3" titleKey="servizi.utenti.titCodice" bundle="serviziLabels"></html:text>&nbsp;&nbsp;

								<html:text readonly="true" property="tariffaModalitaErogazione.desModErog" size="50" titleKey="servizi.label.descrizione" bundle="serviziLabels"></html:text>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<sbn:disableAll checkAttivita="GESTIONE">
						<div>
							<div class="etichetta" style="width:50%; float:left; font-weight:bold;">
								<bean:message key="servizi.erogazione.modErogazione" bundle="serviziLabels" />

								<html:select property="tariffaModalitaErogazione.codErog" >
									<html:optionsCollection property="lstModalitaErogazione" value="cd_tabellaTrim" label="ds_tabella" />
								</html:select>
							</div>
						</div>
						</sbn:disableAll>
					</c:otherwise>
				</c:choose>

				<br/>
				<br/>
				<sbn:disableAll checkAttivita="GESTIONE">
				<div>
					<div class="etichetta"  style="width:100px; float:left; font-weight:bold;">
						<bean:message key="servizi.label.tariffaBase" bundle="serviziLabels" />
					</div>
					<div style="float:none;">
						<html:text readonly="${DettaglioModalitaErogazioneForm.conferma}" property="tariffaModalitaErogazione.tarBase" size="8" style="text-align:right;"></html:text>
					</div>
				</div>

				<br/>
				<div>
					<div class="etichetta"  style="width:100px; float:left; font-weight:bold;">
						<bean:message key="servizi.label.costoUnitario" bundle="serviziLabels" />
					</div>
					<div style="float:none;">
						<html:text readonly="${DettaglioModalitaErogazioneForm.conferma}" property="tariffaModalitaErogazione.costoUnitario" size="8" style="text-align:right;"></html:text>
					</div>
				</div>
				</sbn:disableAll>
			</div>


			<br/>
			<div id="divFooter">
				<div style="text-align:center; width:100%;">
					<div style="width:20%; margin:auto auto; text-align:left;">
						<c:choose>
							<c:when test="${DettaglioModalitaErogazioneForm.conferma}">
								<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
							</c:when>
							<c:otherwise>
							<sbn:checkAttivita idControllo="GESTIONE">
							<html:submit property="methodDettaglioModalitaErogazione" titleKey="servizi.bottone.salvaModalitaErogazione" bundle="serviziLabels">
								<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
							</html:submit>
							</sbn:checkAttivita>
							<html:submit property="methodDettaglioModalitaErogazione" titleKey="servizi.configurazione.servizio.chiude" bundle="serviziLabels">
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
