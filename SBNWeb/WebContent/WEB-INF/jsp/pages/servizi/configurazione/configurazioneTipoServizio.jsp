<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform
		action="/servizi/configurazione/ConfigurazioneTipoServizio.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors bundle="serviziMessages" />
		</div>
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
				readonly="true" property="tipoServizio.descrizione" size="50"></html:text>
			</div>
		</div>
		<br />
		<br />

		<sbn:disableAll disabled="${navForm.conferma}" checkAttivita="GESTIONE">

		<sbn:checkAttivita idControllo="SERVIZIO_ILL">
			<div>
				<bean:message key="servizi.configurazione.servizio.servizioIsoIll" bundle="serviziLabels" />&nbsp;
				<html:select property="tipoServizio.codServizioILL" >
					<html:optionsCollection property="tipiServizioILL" value="cd_tabellaTrim" label="ds_tabella" />
				</html:select>
			</div>
			<br/>
		</sbn:checkAttivita>

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

		<jsp:include
			page="/WEB-INF/jsp/subpages/servizi/configurazione/folderConfigurazioneTipoServizio.jsp" />
		<br />
		<br />
		<c:choose>
			<c:when test="${navForm.folder eq 'S'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/configurazione/gestioneServizi.jsp" />
			</c:when>
			<c:when test="${navForm.folder eq 'I'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/configurazione/gestioneIter.jsp" />
			</c:when>
			<c:when test="${navForm.folder eq 'M'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/configurazione/gestioneModalita.jsp" />
			</c:when>
			<c:when test="${navForm.folder eq 'R'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/configurazione/gestioneModuloRichiesta.jsp" />
			</c:when>
		</c:choose>
		</sbn:disableAll>
		</div>
		<br />

		<div id="divFooter"><c:choose>
			<c:when test="${navForm.folder eq 'S'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/configurazione/footerConfigurazioneTipoServizio_Servizi.jsp" />
			</c:when>
			<c:when test="${navForm.folder eq 'I'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/configurazione/footerConfigurazioneTipoServizio_Iter.jsp" />
			</c:when>
			<c:when test="${navForm.folder eq 'M'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/configurazione/footerConfigurazioneTipoServizio_ModalitaErogazione.jsp" />
			</c:when>
			<c:when test="${navForm.folder eq 'R'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/configurazione/footerConfigurazioneTipoServizio_ModuloRichiesta.jsp" />
			</c:when>
		</c:choose></div>

	</sbn:navform>
</layout:page>
