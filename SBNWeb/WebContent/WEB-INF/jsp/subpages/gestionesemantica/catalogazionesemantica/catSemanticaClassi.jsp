<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%--<table width="100%" border="0">
	<tr>
		<td class="etichetta"><bean:message key="soggettazione.sistema"
			bundle="gestioneSemanticaLabels" /> <html:select
			styleClass="testoNormale"
			property="catalogazioneSemanticaComune.catalogazioneClassificazione.codClassificazione">
			<html:optionsCollection property="listaSistemiClassificazione"
				value="codice" label="codice" />
		</html:select></td>
		<td class="etichetta"><bean:message key="ricerca.edizione"
			bundle="gestioneSemanticaLabels" /> <html:select
			styleClass="testoNormale"
			property="catalogazioneSemanticaComune.catalogazioneClassificazione.codEdizione">
			<html:optionsCollection property="listaEdizioni" value="codice"
				label="descrizione" />
		</html:select> <html:submit property="methodCatalogazione">
			<bean:message key="button.aggiornaDati"
				bundle="gestioneSemanticaLabels" />
		</html:submit></td>
	</tr>
</table>
--%>
<table width="100%" border="0">
	<tr>
		<c:set var="livelloRicerca">
			<c:choose>
				<c:when test="${!CatalogazioneSemanticaForm.enableIndice}">
				           P
				        </c:when>
				<c:otherwise>
							I
				        </c:otherwise>
			</c:choose>
		</c:set>
		<sbn:blocchi
			numBlocco="catalogazioneSemanticaComune.catalogazioneClassificazione.numBlocco"
			numNotizie="catalogazioneSemanticaComune.catalogazioneClassificazione.numNotizie"
			parameter="methodCatalogazione"
			totBlocchi="catalogazioneSemanticaComune.catalogazioneClassificazione.totBlocchi"
			elementiPerBlocco="catalogazioneSemanticaComune.catalogazioneClassificazione.maxRighe"
			livelloRicerca="${livelloRicerca}" />
	</tr>
</table>

<logic:notEmpty name="CatalogazioneSemanticaForm"
	property="catalogazioneSemanticaComune.catalogazioneClassificazione.listaClassi">
	<table class="sintetica">
		<tr>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="catalogazione.progr"
				bundle="gestioneSemanticaLabels" /></th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
				<bean:message key="ricerca.sistema"
					bundle="gestioneSemanticaLabels" /></th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
				<bean:message key="ricerca.edizione"
					bundle="gestioneSemanticaLabels" /></th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
				<bean:message key="ricerca.simbolo"
					bundle="gestioneSemanticaLabels" /></th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="catalogazione.headerStato"
				bundle="gestioneSemanticaLabels" /></th>
			<%--<c:if test="${!CatalogazioneSemanticaForm.enableIndice}">
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
				<bean:message key="catalogazione.headerLegato"
					bundle="gestioneSemanticaLabels" /></th>
			</c:if>--%>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="catalogazione.parole"
				bundle="gestioneSemanticaLabels" /></th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
		</tr>
		<bean-struts:define id="color" value="#FEF1E2" />
		<logic:iterate id="listaRicerca"
			property="catalogazioneSemanticaComune.catalogazioneClassificazione.listaClassi"
			name="CatalogazioneSemanticaForm" indexId="progr">
			<sbn:rowcolor var="color" index="progr" />
			<tr bgcolor="${color}">

				<td ><sbn:anchor name="listaRicerca"
					property="progr" /> <sbn:linkbutton index="identificativoClasse"
					name="listaRicerca" value="progr" key="button.gestione"
					bundle="gestioneSemanticaLabels" title="gestione"
					property="catalogazioneSemanticaComune.catalogazioneClassificazione.codNotazioneSelezionato"
					disabled="${CatalogazioneSemanticaForm.enableSoloEsamina}"/>
				</td>
				<td ><bean-struts:write name="listaRicerca"
					property="simboloDewey.sistema" /></td>
				<td ><bean-struts:write name="listaRicerca"
					property="simboloDewey.edizione" /></td>
				<td ><bean-struts:write name="listaRicerca"
					property="simboloDewey.simbolo" /></td>
				<td ><bean-struts:write name="listaRicerca"
					property="livelloAutorita" /></td>
				<%--<c:if test="${!CatalogazioneSemanticaForm.enableIndice}">
					<td ><bean-struts:write name="listaRicerca"
						property="indicatore" /></td>
				</c:if>--%>
				<td ><bean-struts:write name="listaRicerca"
					property="parole" /></td>
				<td ><html:radio
					name="CatalogazioneSemanticaForm"
					property="catalogazioneSemanticaComune.catalogazioneClassificazione.codNotazioneSelezionato"
					value="${listaRicerca.identificativoClasse}" /></td>
			</tr>
		</logic:iterate>
	</table>

</logic:notEmpty>


