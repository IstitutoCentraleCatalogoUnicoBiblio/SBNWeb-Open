<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>


<html:xhtml />
<layout:page>
	<!-- sbn:errors bundle="gestioneStampeMessages" /> -->
	<sbn:navform action="/gestionestampe/servizi/stampaServizi.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<html:hidden property="daChiamare" value="" />
		<table width="100%" align="center">
			<table width="100%">
				<tr>
					<td class="etichetta" width="10%"><bean:message
						key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" /></td>
					<td><html:text disabled="true" styleId="testoNormale" property="codBib" size="5"
						maxlength="3"></html:text> <html:submit disabled="false" title="Lista Biblioteche"
						styleClass="buttonImageListaSezione" property="methodStampaServizi">
						<bean:message key="gestionestampe.lsBib" bundle="documentoFisicoLabels" />
					</html:submit><bs:write name="navForm" property="descrBib" /></td>
				</tr>
			</table>
			<BR>
			<table width="100%">
				<c:choose>
					<c:when test="${navForm.tipoDiStampa eq 'ServiziCorrenti'}">
						<tr>
							<!-- LOCALE o ILL -->
							<td><bean:message key="servizi.erogazione.contesto" bundle="serviziLabels" /></td>
							<td><html:select property="anaMov.flSvolg"
								onchange="this.form.daChiamare.value='CAMBIA_CONTESTO'; this.form.submit();">
								<html:optionsCollection property="svolgimentiSelezionati" value="codice"
									label="descrizione" />
							</html:select></td>
							<noscript><html:submit property="methodStampaServizi">
								<bean:message key="servizi.bottone.cambiaContesto" bundle="serviziLabels" />
							</html:submit></noscript>
							<noscript><bean:message key="servizi.erogazione.descCambioContesto"
								bundle="serviziLabels" /></noscript>
						</tr>
						<!-- TIPO SERVIZIO -->
						<tr>
							<td><bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />&nbsp;&nbsp;</td>
							<td><html:select property="anaMov.codTipoServ"
								onchange="this.form.daChiamare.value='CAMBIA_TIPO_SERVIZIO'; this.form.submit();">
								<html:optionsCollection property="lstTipiServizio" value="codice"
									label="descrizione" />
							</html:select></td>
							<noscript><br />
							<html:submit property="methodStampaServizi">
								<bean:message key="servizi.bottone.cambiaTipoServizio" bundle="serviziLabels" />
							</html:submit> <bean:message key="servizi.erogazione.descCambioTipoServizio"
								bundle="serviziLabels" /> <br />
							</noscript>
						</tr>
						<!-- MOD EROGAZIONE -->
						<tr>
							<td><bean:message key="servizi.erogazione.modErogazione"
								bundle="serviziLabels" /></td>
							<td><html:select property="anaMov.cod_erog">
								<html:optionsCollection property="tariffeErogazioneVO" value="codErog"
									label="desModErog" />
							</html:select></td>
						</tr>
						<!-- ATTIVITA' -->
						<tr>
							<td><bean:message key="servizi.erogazione.attivita" bundle="serviziLabels" /></td>
							<td><html:select property="anaMov.codAttivita">
								<html:optionsCollection property="iterServizioVO" value="codAttivita"
									label="descrizione" />
							</html:select>
							<!--<bean:message key="servizi.erogazione.movimento.attivitaAttuale"
								bundle="serviziLabels" /> <html:radio property="anaMov.attivitaAttuale"
								value="true" /> -->
							</td>
						</tr>
						<!-- STATO MOVIMENTO -->
						<tr>
							<td><bean:message key="servizi.erogazione.statoMovimento"
								bundle="serviziLabels" /></td>
							<td><html:select property="anaMov.codStatoMov">
								<html:optionsCollection property="lstStatiMovimento" value="codice"
									label="descrizione" />
							</html:select></td>
						</tr>
						<!-- STATO RICHIESTA -->
						<tr>
							<td><bean:message key="servizi.erogazione.statoRichiesta"
								bundle="serviziLabels" /></td>
							<td><html:select property="anaMov.codStatoRic">
								<html:optionsCollection property="lstStatiRichiesta" value="codice"
									label="descrizione" />
							</html:select></td>
						</tr>
						<!-- RANGE DATE -->
						<tr>
							<td><span><bean:message key="servizi.stampa.correnti.data" bundle="serviziLabels" />&nbsp;</span></td>
							<td><bean:message key="servizi.documenti.dataDa" bundle="serviziLabels" />&nbsp;
							<html:text styleId="testoNoBold" property="dataDa" size="10"
								maxlength="10" /> &nbsp;<bean:message key="servizi.documenti.dataA"
								bundle="serviziLabels" />&nbsp; <html:text styleId="testoNoBold" property="dataA"
								size="10" maxlength="10" /></td>
						</tr>
						<!-- inventario -->
						<tr>
							<td><bean:message key="servizi.erog.Inventario" bundle="serviziLabels" />
							<td><html:select property="anaMov.codBibInv"
								titleKey="servizi.erogazione.bibliotecaInventario" bundle="serviziLabels"
								style="width:38px">
								<html:optionsCollection property="elencoBib" value="codice" label="descrizione" />
							</html:select> &nbsp;&nbsp;&nbsp; <html:text styleId="testoNoBold" property="anaMov.codSerieInv"
								titleKey="servizi.erogazione.codiceSerie" bundle="serviziLabels" size="8"
								maxlength="3"></html:text> &nbsp;&nbsp;&nbsp; <html:text styleId="testoNoBold"
								property="anaMov.codInvenInv" titleKey="servizi.erogazione.codiceInventario"
								bundle="serviziLabels" size="14" maxlength="9"></html:text>&nbsp;&nbsp;&nbsp; <html:messages
								id="msg1" message="true" property="documentofisico.parameter.bottone"
								bundle="serviziLabels" /> <html:submit
								titleKey="servizi.erogazione.ricercaTitolo" styleClass="buttonImage"
								property="${msg1}">
								<bean:message key="servizi.bottone.hlpinventario" bundle="serviziLabels" />
							</html:submit></td>
						</tr>
						<!-- collocazione -->
						<tr>
							<td><bean:message key="servizi.erog.Segnatura" bundle="serviziLabels" /></td>
							<td><html:select property="anaMov.codBibDocLett"
								disabled="${not empty navForm.anaMov.codBibDocLett}"
								titleKey="servizi.erogazione.bibliotecaSegnatura" bundle="serviziLabels"
								style="width:38px">
								<html:optionsCollection property="elencoBib" value="codice" label="descrizione" />
							</html:select> &nbsp;&nbsp;&nbsp; <html:text styleId="testoNoBold" property="segnaturaRicerca"
								size="30" maxlength="40">
							</html:text> &nbsp;&nbsp;&nbsp; <html:messages id="msg1" message="true"
								property="documentofisico.parameter.bottone" bundle="serviziLabels" /> <html:submit
								titleKey="servizi.erogazione.ricercaDocumentiNonSBN" styleClass="buttonImage"
								property="${msg1}">
								<bean:message key="servizi.bottone.hlpsegnatura" bundle="serviziLabels" />
							</html:submit> &nbsp;&nbsp;&nbsp; <html:messages id="msg1" message="true"
								property="documentofisico.parameter.bottone" bundle="documentoFisicoLabels" /> <html:submit
								styleClass="buttonCleanCampi" property="${msg1}"
								titleKey="servizi.erogazione.ripulisceSegnatura" bundle="serviziLabels">
								<bean:message key="servizi.bottone.ripulisciSegnatura" bundle="serviziLabels" />
							</html:submit> &nbsp;&nbsp;&nbsp;</td>
						</tr>
					</c:when>
					<c:otherwise>
					<!-- STAMPA STORICO -->
						<tr>
							<td><span><bean:message key="servizi.stampa.correnti.data" bundle="serviziLabels" />&nbsp;</span></td>
							<td><bean:message key="servizi.documenti.dataDa" bundle="serviziLabels" />&nbsp;
							<html:text styleId="testoNoBold" property="dataDa" size="10"
								maxlength="10" /> &nbsp;<bean:message key="servizi.documenti.dataA"
								bundle="serviziLabels" />&nbsp; <html:text styleId="testoNoBold" property="dataA"
								size="10" maxlength="10" /></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
		</table>
		<HR>
		<jsp:include flush="true" page="../common/tipoStampa.jsp" />
		<HR>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center" border="0" style="height: 40px">
			<tr>
				<c:choose>
						<c:when test="${navForm.conferma}">
							<td><html:submit styleClass="pulsanti" property="methodStampaServizi">
								<bean:message key="documentofisico.bottone.si" bundle="documentoFisicoLabels" />
							</html:submit> <html:submit styleClass="pulsanti" property="methodStampaServizi">
								<bean:message key="documentofisico.bottone.no" bundle="documentoFisicoLabels" />
							</html:submit></td>
						</c:when>
					<c:when test="${navForm.disable == false}">
						<td><html:submit property="methodStampaServizi">
							<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
						</html:submit><html:submit property="methodStampaServizi">
							<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<td><html:submit property="methodStampaServizi">
							<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
