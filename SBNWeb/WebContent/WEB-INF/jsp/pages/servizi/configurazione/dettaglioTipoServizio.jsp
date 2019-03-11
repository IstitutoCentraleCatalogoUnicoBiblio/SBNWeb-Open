<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/servizi/configurazione/DettaglioTipoServizio.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br/>

			<div>
				<div class="etichetta" style="float:left; font-weight:bold;">
					<bean:message key="servizi.utenti.biblioteca" bundle="serviziLabels" />&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="float:none;">
					<html:text styleId="testoNoBold" readonly="true" property="tipoServizio.codBib" size="8"></html:text>
				</div>
			</div>
			<br/>
			<hr align="left">
			<br/>
			<sbn:disableAll checkAttivita="GESTIONE" disabled="${navForm.conferma}">
			<div>
				<bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />&nbsp;&nbsp;
				<c:choose>
					<c:when test="${navForm.nuovo}">
						<html:select property="tipoServizio.codiceTipoServizio">
							<html:optionsCollection property="lstTipiServizio"
								value="codice" label="descrizione" />
						</html:select>
					</c:when>
					<c:otherwise>
						<html:text readonly="true" property="tipoServizio.descrizione" size="50"></html:text>
					</c:otherwise>
				</c:choose>
			</div>
			<br/>

			<div><bean:message
				key="servizi.configurazione.dettaglio.numMaxMov"
				bundle="serviziLabels" />&nbsp;&nbsp; <html:text
				property="tipoServizio.numMaxMov" size="5" maxlength="4"></html:text></div>

			<br />

			<div><bean:message
				key="servizi.configurazione.dettaglio.documentoDisponibile"
				bundle="serviziLabels" />&nbsp; <html:text style="text-align:right;"
				property="tipoServizio.oreRidis" size="2" maxlength="2"></html:text>&nbsp;
			<bean:message
				key="servizi.configurazione.dettaglio.documentoDisponibile.ore"
				bundle="serviziLabels" />&nbsp; <html:text style="text-align:right;"
				property="tipoServizio.ggRidis" size="2" maxlength="2"></html:text>&nbsp;
			<bean:message
				key="servizi.configurazione.dettaglio.documentoDisponibile.gg"
				bundle="serviziLabels" /></div>

			<br />

			<div><bean:message
				key="servizi.configurazione.dettaglio.codaRichieste"
				bundle="serviziLabels" />&nbsp;&nbsp;
				<html:checkbox
				property="tipoServizio.codaRichieste"></html:checkbox><html:hidden property="tipoServizio.codaRichieste" value="false"></html:hidden>&nbsp;
				<bean:message
				key="servizi.configurazione.dettaglio.penalita"
				bundle="serviziLabels" />&nbsp;&nbsp;
				<html:checkbox
				property="tipoServizio.penalita"></html:checkbox><html:hidden property="tipoServizio.penalita" value="false"></html:hidden></div>

			<br />

			<%--
			<div><bean:message
				key="servizi.configurazione.dettaglio.ins_richieste_utente"
				bundle="serviziLabels" />&nbsp;&nbsp;
				<html:checkbox
				property="tipoServizio.ins_richieste_utente"></html:checkbox><html:hidden property="tipoServizio.ins_richieste_utente" value="false"></html:hidden>&nbsp;
				<bean:message
				key="servizi.configurazione.dettaglio.anche_da_remoto"
				bundle="serviziLabels" />&nbsp;&nbsp;
				<html:checkbox
				property="tipoServizio.anche_da_remoto"></html:checkbox><html:hidden property="tipoServizio.anche_da_remoto" value="false"></html:hidden></div>

			<br />
			<br />
			--%>

	   		<c:choose>
				<c:when test="${!navForm.ammInsUtenteParamBiblioteca}">
					<div><bean:message key="servizi.configurazione.dettaglio.ins_richieste_utente" bundle="serviziLabels" />&nbsp;&nbsp;

					<html:hidden property="inserimentoUtente" />
					<html:checkbox property="tipoServizio.ins_richieste_utente" onclick="validateSubmit('inserimentoUtente', 'SI');" ></html:checkbox>
					<html:hidden property="tipoServizio.ins_richieste_utente" value="false"></html:hidden>&nbsp;

	    			<c:choose>
						<c:when test="${navForm.tipoServizio.ins_richieste_utente}">
							<bean:message key="servizi.configurazione.dettaglio.anche_da_remoto" bundle="serviziLabels" />&nbsp;&nbsp;

							<html:checkbox property="tipoServizio.anche_da_remoto"></html:checkbox>
							<html:hidden property="tipoServizio.anche_da_remoto" value="false"></html:hidden>
						</c:when>
					</c:choose>
					</div>

					<br />
					<br />
				</c:when>
				<c:otherwise>
					<br />
				</c:otherwise>
			</c:choose>
			</sbn:disableAll>

			<div id="divFooter">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/configurazione/footerDettaglioTipoServizio.jsp" />
			</div>
		</div>
	</sbn:navform>

</layout:page>
