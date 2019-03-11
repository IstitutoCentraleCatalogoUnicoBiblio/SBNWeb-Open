<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<br>
<br>
		<div>
				<div >
					<!-- STATO MOVIMENTO -->
					<div >
						<div style="float:left; width:100px">
							<bean:message key="servizi.erogazione.statoMovimento" bundle="serviziLabels" />
						</div>
						<div style="float:none;">
							<html:select property="anaMov.codStatoMov" >
								<html:optionsCollection property="lstStatiMovimento"
									value="codice" label="descrizione" />
							</html:select>
						</div>
						<br/>
					</div>
					<!-- STATO RICHIESTA -->
					<div>
						<div style="float:left; width:100px">
							<bean:message key="servizi.erogazione.statoRichiesta" bundle="serviziLabels" />
						</div>
						<div style="float:none;">
							<html:select property="anaMov.codStatoRic" >
								<html:optionsCollection property="lstStatiRichiesta"
									value="codice" label="descrizione" />
							</html:select>
						</div>
						<br/>
					</div>
					<div >
						<div style="float:left; width:100px">
							<bean:message key="servizi.erogazione.contesto" bundle="serviziLabels" />&nbsp;&nbsp;
						</div>
						<div style="float:none;">
						<!-- LOCALE o ILL -->

								<html:select property="anaMov.flSvolg"
										onchange="this.form.daChiamare.value='CAMBIA_CONTESTO'; this.form.submit();" >
									<html:optionsCollection property="svolgimentiSelezionati" value="codice" label="descrizione"/>
								</html:select>

							&nbsp;&nbsp;&nbsp;&nbsp;
							<noscript>
								<html:submit property="methodErogazione">
									<bean:message key="servizi.bottone.cambiaContesto" bundle="serviziLabels" />
								</html:submit>
							</noscript>
						</div>
						<div style="float:none; vertical-align: middle; font-style: italic;">
						<noscript><bean:message key="servizi.erogazione.descCambioContesto" bundle="serviziLabels" />
							</noscript>
						</div>
					</div>
					<br/>
					<!-- TIPO SERVIZIO -->
					<div>
						<div style="float:left; width:100px">
							<bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />&nbsp;&nbsp;
						</div>
						<div style="float:none;">
							<html:select property="anaMov.codTipoServ"  onchange="this.form.daChiamare.value='CAMBIA_TIPO_SERVIZIO'; this.form.submit();">
								<html:optionsCollection property="lstTipiServizio"
									value="codice" label="descrizione" />
							</html:select>
						</div>
						<div style="vertical-align: middle; font-style: italic;">
							<noscript>
								<br/>
								<html:submit property="methodErogazione">
									<bean:message key="servizi.bottone.cambiaTipoServizio" bundle="serviziLabels" />
								</html:submit>
								<bean:message key="servizi.erogazione.descCambioTipoServizio" bundle="serviziLabels" />
								<br/>
							</noscript>
						</div>
					</div>
					<br/>
					<!-- ATTIVITA' -->
					<div>
						<div style="float:left; width:100px">
							<bean:message key="servizi.erogazione.attivita" bundle="serviziLabels" />
						</div>
						<div style="float:left;">
							<html:select property="anaMov.codAttivita" >
								<html:optionsCollection property="iterServizioVO"
									value="codAttivita" label="descrizione" />
							</html:select>
						</div>
						<div style="float:none;">
							<c:choose><c:when test="${not empty ErogazioneRicercaForm.iterServizioVO}">
								&nbsp;&nbsp;&nbsp;&nbsp;
								<bean:message key="servizi.erogazione.movimento.attivitaAttuale" bundle="serviziLabels" />
								<html:radio property="anaMov.attivitaAttuale" value="true"/>
								&nbsp;&nbsp;
								<bean:message key="servizi.erogazione.movimento.attivitaSuccessiva" bundle="serviziLabels" />
								<html:radio property="anaMov.attivitaAttuale" value="false"/>
							</c:when></c:choose>
						</div>
						<br/>
					</div>

				<!-- MOD EROGAZIONE -->
				<div>
					<div style="float:left; width:100px">
						<bean:message key="servizi.erogazione.modErogazione" bundle="serviziLabels" />
					</div>
					<div style="float:none;">
						<html:select property="anaMov.cod_erog" >
							<html:optionsCollection property="tariffeErogazioneVO"
								value="codErog" label="desModErog" />
						</html:select>
					</div>
					<br/>
				</div>
				<!-- RANGE DATE -->
				<div>
					<div style="float:left; width:100px; vertical-align: middle">
						<span><bean:message key="servizi.erogazione.dataFinePrevista" bundle="serviziLabels" />&nbsp;</span>
					</div>
					<div style="float:none; vertical-align: middle">
						<bean:message key="servizi.documenti.dataDa" bundle="serviziLabels" />
						&nbsp;<html:text styleId="testoNoBold" property="anaMov.dataFinePrev_da" size="10"  maxlength="10" />
						&nbsp;<bean:message key="servizi.documenti.dataA" bundle="serviziLabels" />&nbsp;
						<html:text styleId="testoNoBold" property="anaMov.dataFinePrev_a" size="10"  maxlength="10" />
					</div>
					<br/>
				</div>
				<!-- FILTRO SEZIONE -->
				<div>
					<div style="float:left; width:100px">
						<bean:message key="documentofisico.sezioneT" bundle="documentoFisicoLabels" />
					</div>
					<div style="float:none;">
						<html:text disabled="${navForm.conferma}" styleId="testoNormale"
							property="anaMov.filtroColl.codSezione" size="15" maxlength="10" />
						<html:submit title="Lista Sezioni" styleClass="buttonImageListaSezione"
							property="${navButtons}">
							<bean:message key="documentofisico.lsSez" bundle="documentoFisicoLabels" />
						</html:submit>
						<html:submit styleClass="buttonCleanCampi" property="${navButtons}" titleKey="servizi.erogazione.ripulisceFiltro" bundle="serviziLabels">
							<bean:message key="servizi.bottone.ripulisciSegnatura" bundle="serviziLabels"/>
						</html:submit>
						<c:if test="${navForm.anaMov.filtroColl.sezioneSpazio}">
							<span class="little">&lt;<bean:message key="documentofisico.sezione.spazio" bundle="documentoFisicoLabels" />&gt;</span>
						</c:if>
					</div>
					<br/>
				</div>
				<hr/>
				<div>
					<div style="float:left; width:100px">
						<bean:message key="servizi.label.elementiPerBlocco" bundle="serviziLabels" />&nbsp;
					</div>
					<div style="float:left;">
						<html:text  styleId="testoNoBold" property="anaMov.elemPerBlocchi"
							size="5"  maxlength="3" />
					</div>
					<div style="margin-left: 10px; float: left;">
						<bean:message key="servizi.label.ordinamento" bundle="serviziLabels" />:&nbsp;
						<html:select property="anaMov.tipoOrdinamento">
							<html:optionsCollection property="ordinamentiMov" value="codice" label="descrizione" />
						</html:select>
					</div>
				</div>
				<br/>
			</div>
		</div>
		<html:hidden property="daChiamare" value="" />
