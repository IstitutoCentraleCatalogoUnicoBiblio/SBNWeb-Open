<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<!-- CAMPI PER SCELTA NUOVO SERVIZIO -->
<%--
<l:notEmpty name="navForm" property="lstCodiciServizio">
--%>

<html:hidden property="updateCombo" />
<span class="etichetta" style="font-weight: bolder;" >
	<bean:message key="servizi.erogazione.listaMovimenti.datiNuovaRichiesta" bundle="serviziLabels" />
</span>
<hr/>
<br/>
<table width="100%" border="0">
<sbn:disableAll disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}">
<c:choose>
	<c:when test="${navForm.tipoRicerca eq 'RICERCA_PER_UTENTE'}">
		<sbn:checkAttivita idControllo="DOC_LOCALE">
			<tr>
				<td width="8%">
					<bean:message key="servizi.erog.Inventario" bundle="serviziLabels" />
				</td>
				<td width="60%" align="left">

					<html:select property="movRicerca.codBibInv" titleKey="servizi.erogazione.bibliotecaInventario" bundle="serviziLabels" style="width:38px"
					disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}">
						<html:optionsCollection property="elencoBib" value="codice" label="descrizione"/>
					</html:select>

					<%--
					<html:text	titleKey="servizi.erogazione.listaMovimenti.titleBibliotecaInventario" bundle="serviziLabels"
								styleId="testoNoBold" property="codBibInv"
								size="8" maxlength="3" disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}"></html:text>
					--%>

					&nbsp;&nbsp;&nbsp;
					<html:text	titleKey="servizi.erogazione.codiceSerie" bundle="serviziLabels"
								styleId="testoNoBold"
								property="codSerieInv"
								size="8"  maxlength="3"
								disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}"></html:text>
					&nbsp;&nbsp;&nbsp;
					<html:text	titleKey="servizi.erogazione.codiceInventario" bundle="serviziLabels"
								styleId="testoNoBold"
								property="codInvenInv"
								size="14" maxlength="9"
								disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}"></html:text>
					&nbsp;&nbsp;&nbsp;
					<html:submit styleClass="buttonImage" property="methodListaMovimentiUte"
								 titleKey="servizi.erogazione.ricercaTitolo"  bundle="serviziLabels"
								 disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}">
						<bean:message key="servizi.bottone.hlpinventario" bundle="serviziLabels" />
					</html:submit>
					<sbn:checkAttivita idControllo="RFID">
					&nbsp;&nbsp;&nbsp;RFID&nbsp;<html:text styleId="testoNoBold" property="movRicerca.rfidChiaveInventario" size="18"
						disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}"/>
					</sbn:checkAttivita>
				</td>
			</tr>



			<tr>
				<td width="8%">
					<bean:message key="servizi.erog.Segnatura" bundle="serviziLabels" />
				</td>
				<td width="60%" align="left">

					<html:select property="movRicerca.codBibDocLett" titleKey="servizi.erogazione.bibliotecaSegnatura" bundle="serviziLabels" style="width:38px"
					disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}">
						<html:optionsCollection property="elencoBib" value="codice" label="descrizione"/>
					</html:select>
					&nbsp;&nbsp;&nbsp;

					<html:text styleId="testoNoBold" property="desXRicSeg" size="30"  maxlength="40"
								disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}" />
					&nbsp;&nbsp;&nbsp;
					<html:submit styleClass="buttonImage" property="methodListaMovimentiUte"
								 titleKey="servizi.erogazione.ricercaDocumentiNonSBN"  bundle="serviziLabels"
								 disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}">
						<bean:message key="servizi.bottone.hlpsegnatura" bundle="serviziLabels"/>
					</html:submit>
					&nbsp;&nbsp;&nbsp;
					<html:submit styleClass="buttonDelete" property="methodListaMovimentiUte" titleKey="servizi.erogazione.ripulisceSegnatura" bundle="serviziLabels"
					disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}">
						<bean:message key="servizi.bottone.ripulisciSegnatura"
							bundle="serviziLabels"/>
					</html:submit>

				</td>
			</tr>
		</sbn:checkAttivita>
	</c:when>

	<c:when test="${navForm.tipoRicerca eq 'RICERCA_PER_INVENTARIO' or navForm.tipoRicerca eq 'RICERCA_PER_SEGNATURA'}">

			<tr>
				<td width="8%">
					<bean:message key="servizi.erog.Utente" bundle="serviziLabels" />
				</td>
				<td width="60%" align="left">
					<html:text  titleKey="servizi.erogazione.listaMovimenti.titleCodiceUtente" bundle="serviziLabels"
								styleId="testoNoBold"
								property="codUtente" size="30" maxlength="25"
								disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}">
					</html:text>
					&nbsp;&nbsp;&nbsp;
					<html:submit styleClass="buttonImage" property="methodListaMovimentiUte"
								 titleKey="servizi.erogazione.ricercaUtente"  bundle="serviziLabels"
								 disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}">
						<bean:message key="servizi.bottone.hlputente" bundle="serviziLabels" />
					</html:submit>
				</td>
			</tr>

	</c:when>

</c:choose>

<l:notEmpty name="navForm" property="lstCodiciServizio">
<tr>
	<td width="8%">
		<bean:message key="servizi.erogazione.servizi" bundle="serviziLabels" />
	</td>
	<td width="60%" align="left">
		<html:select property="codTipoServNuovaRich" disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}" onchange="validateSubmit('updateCombo', 'tipoSrv');">
			<html:optionsCollection property="lstCodiciServizio" value="codTipoServ" label="descrTipoServ" />
		</html:select>
	</td>
</tr>

</l:notEmpty>
<sbn:checkAttivita idControllo="DOC_ALTRA_BIBLIOTECA">
			<tr>
				<td width="8%">
					<bean:message key="servizi.erog.docAltraBiblioteca" bundle="serviziLabels" />
				</td>
				<td width="60%" align="left">
					<html:text styleId="testoNoBold" property="titoloDocAltraBib" bundle="serviziLabels" maxlength="70" size="70" disabled="true"/>&nbsp;&nbsp;&nbsp;
					<c:if test="${not (navForm.conferma or navForm.confermaNuovaRichiesta) }">
						<html:submit styleClass="buttonImage" property="methodListaMovimentiUte" bundle="serviziLabels">
							<bean:message key="servizi.bottone.hlpdocaltrabib" bundle="serviziLabels" />
						</html:submit>
						<c:if test="${not empty navForm.titoloDocAltraBib}">
							<html:submit styleClass="buttonDelete" property="methodListaMovimentiUte" titleKey="servizi.erogazione.ripulisceSegnatura"
								bundle="serviziLabels">
								<bean:message key="servizi.bottone.ripulisciSegnatura" bundle="serviziLabels"/>
							</html:submit>
						</c:if>
					</c:if>
				</td>
			</tr>
</sbn:checkAttivita>
<sbn:checkAttivita idControllo="PERIODICO">
	<tr>
		<td width="8%">
			<bean:message key="servizi.erogazione.movimento.annata"	bundle="serviziLabels" />
		</td>
		<td width="60%" align="left">
			<html:text property="nuovaRichiesta.annoPeriodico" size="5" maxlength="4" />&nbsp;&nbsp;
			<%-- <bean:message key="servizi.erogazione.movimento.Fascicolo" bundle="serviziLabels" />&nbsp;&nbsp;
			<html:text property="nuovaRichiesta.numFascicolo" size="5" maxlength="30" /> --%>
		</td>
	</tr>
</sbn:checkAttivita>

<sbn:checkAttivita idControllo="PRENOTAZIONE_POSTO">
	<tr>
		<td width="8%">
			<bean:message key="servizi.erogazione.movimento.prenotazione.posto"	bundle="serviziLabels" />
		</td>
		<td width="60%" align="left">
			<html:text property="nuovaRichiesta.prenotazionePosto.posto.sala.descrizione" readonly="true" />&nbsp;il
			<html:text name="navForm" property="nuovaRichiesta.prenotazionePosto.dataInizio" readonly="true" styleClass="date" />&nbsp;
			<bean:message key="servizi.sale.prenotazione.start" bundle="serviziLabels" />&nbsp;
			<html:text name="navForm" property="nuovaRichiesta.prenotazionePosto.orarioInizio" readonly="true" styleClass="time" />&nbsp;
			<bean:message key="servizi.sale.prenotazione.end" bundle="serviziLabels" />&nbsp;
			<html:text name="navForm" property="nuovaRichiesta.prenotazionePosto.orarioFine" readonly="true" styleClass="time" />&nbsp;
			<html:submit styleClass="buttonDelete"
				property="methodListaMovimentiUte"
				titleKey="servizi.erogazione.ripulisceSegnatura"
				bundle="serviziLabels"
				disabled="${navForm.conferma or navForm.confermaNuovaRichiesta}">
					<bean:message key="servizi.bottone.ripulisciSegnatura" bundle="serviziLabels" />
			</html:submit>
		</td>
	</tr>
</sbn:checkAttivita>
</sbn:disableAll>

<sbn:checkAttivita idControllo="ILL_BIB_FORNITRICE">

	<l:iterate id="itemBib" property="bibliotecaFornitrice" name="navForm"
		indexId="idx">
		<tr>
			<td>
				<bean:message key="servizi.erogazione.ill.bib.fornitrice" bundle="serviziLabels"/>
			</td>
			<td width="150px" align="left" colspan="2"><html:text
					styleId="testoNoBold" property="cd_ana_biblioteca" name="itemBib"
					size="8" readonly="true" indexed="true" />&nbsp; <html:text
					styleId="testoNoBold" property="nom_biblioteca" name="itemBib"
					size="50" readonly="true" indexed="true" />
				<c:if test="${itemBib.priorita eq 0 and not (navForm.conferma or navForm.confermaNuovaRichiesta) }">
					&nbsp; <html:submit
					styleClass="buttonImage"
					property="${sessionScope.NAVIGATION_INSTANCE.cache.currentElement.mapping.parameter}"
					disabled="${navForm.conferma}" >
					<bean:message key="servizi.bottone.cambioBiblioteca" bundle="serviziLabels" /></html:submit>
				 </c:if>
			</td>
		</tr>

	</l:iterate>
</sbn:checkAttivita>

<sbn:checkAttivita idControllo="SUPPORTO_MODALITA_ILL">
<tr>
	<td width="8%"><bean:message key="servizi.erogazione.movimento.ill.supplyMediumType" bundle="serviziLabels" /></td>
	<td width="60%" align="left">
		<sbn:disableAll checkAttivita="TIPO_SERVIZIO_ILL">
			<html:select property="nuovaRichiesta.datiILL.cd_supporto" >
				<html:optionsCollection property="tipiSupportoILL" value="cd_tabellaTrim" label="ds_tabella" />
			</html:select>
		</sbn:disableAll>
	</td>
</tr>
<tr>
	<td width="8%"><bean:message key="servizi.erogazione.movimento.ill.deliveryService" bundle="serviziLabels" /></td>
	<td width="60%" align="left">
		<html:select property="nuovaRichiesta.datiILL.cod_erog" >
			<html:optionsCollection property="modoErogazioneILL" value="cd_tabellaTrim" label="ds_tabella" />
			<%-- <html:optionsCollection property="modoErogazione" value="codErog" label="desModErog" /> --%>
		</html:select>
	</td>
</tr>
</sbn:checkAttivita>

<l:notEmpty name="navForm" property="tipiSupporto">
	<tr>
		<td width="8%">
			<bean:message key="servizi.erogazione.movimento.supporto" bundle="serviziLabels" />
		</td>
			<td width="60%" align="left">
			<html:select property="nuovaRichiesta.codSupporto" onchange="this.form.cambiaSupportoListaMov.value='true'; this.form.submit();" >
				<html:optionsCollection property="tipiSupporto" value="codSupporto" label="descrizione" />
			</html:select>
			<html:hidden property="cambiaSupportoListaMov" value="" />
		</td>
	</tr>
</l:notEmpty>

<l:notEmpty name="navForm" property="modoErogazione">
	<tr>
		<td width="8%">
			<bean:message key="servizi.erogazione.modErogazione" bundle="serviziLabels" />
		</td>
		<td width="60%" align="left">
			<html:select property="nuovaRichiesta.cod_erog" >
				<html:optionsCollection property="modoErogazione" value="codErog" label="desModErog" />
			</html:select>
		</td>
	</tr>
</l:notEmpty>

<sbn:checkAttivita idControllo="INTERVALLO_COPIE">
	<tr>
		<td width="8%">
			<bean:message key="servizi.configurazione.moduloRichiesta.intervalloCopia" bundle="serviziLabels" />
		</td>
		<td width="60%" align="left">
			<html:text property="nuovaRichiesta.intervalloCopia" size="20" maxlength="30" styleClass="l" />
		</td>
	</tr>
</sbn:checkAttivita>

</table>

<%--
<l:empty name="navForm" property="modoErogazione">
	<c:choose>
		<c:when test="${navForm.confermaNuovaRichiesta}">
			<br/>
			<br/>
		</c:when>
	</c:choose>
</l:empty>
 --%>
<%--
</l:notEmpty>
--%>

<l:empty name="navForm" property="lstCodiciServizio">

<hr/>

<br/>
<span class="etichetta" style="font-weight: bolder;" >
	<bean:message key="servizi.erogazione.listaMovimenti.noNuovaRichiesta" bundle="serviziLabels" />
</span>
<br/>

</l:empty>
<script type="text/javascript" src='<c:url value="/scripts/servizi/movimento/nuovaRichiesta.js" />'></script>