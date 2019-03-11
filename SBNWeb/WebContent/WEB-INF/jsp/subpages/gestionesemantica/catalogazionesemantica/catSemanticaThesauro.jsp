<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<table width="100%" border="0">
	<tr>
		<sbn:blocchi
			numBlocco="catalogazioneSemanticaComune.catalogazioneThesauro.numBlocco"
			numNotizie="catalogazioneSemanticaComune.catalogazioneThesauro.numNotizie"
			parameter="methodCatalogazione"
			totBlocchi="catalogazioneSemanticaComune.catalogazioneThesauro.totBlocchi"
			elementiPerBlocco="catalogazioneSemanticaComune.catalogazioneThesauro.maxRighe"
			livelloRicerca="P" />
	</tr>
</table>

<logic:notEmpty name="CatalogazioneSemanticaForm"
	property="catalogazioneSemanticaComune.catalogazioneThesauro.listaTermini">

	<table class="sintetica">
		<tr>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="sintetica.progr" bundle="gestioneSemanticaLabels" />
			</th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="sintetica.did" bundle="gestioneSemanticaLabels" />
			</th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="gestionesemantica.thesauro.thesauro"
				bundle="gestioneSemanticaLabels" /></th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="sintetica.headerStato"
				bundle="gestioneSemanticaLabels" /></th>

			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="catalogazione.headerLegato"
				bundle="gestioneSemanticaLabels" /></th>

			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="sintetica.termine"
				bundle="gestioneSemanticaLabels" /></th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
		</tr>
		<bean-struts:define id="color" value="#FEF1E2" />
		<logic:iterate id="listaRicerca"
			property="catalogazioneSemanticaComune.catalogazioneThesauro.listaTermini"
			name="CatalogazioneSemanticaForm" indexId="progr">
			<sbn:rowcolor var="color" index="progr" />
			<tr bgcolor="${color}">
				<td ><sbn:anchor name="listaRicerca"
					property="progr" /> <sbn:linkbutton index="did"
					name="listaRicerca" value="progr" key="button.gestione"
					bundle="gestioneSemanticaLabels" title="analitica"
					property="catalogazioneSemanticaComune.catalogazioneThesauro.codSelezionato"
					disabled="${CatalogazioneSemanticaForm.enableSoloEsamina}"/>
				</td>
				<td ><bean-struts:write name="listaRicerca"
					property="did" /></td>
				<td ><bean-struts:write name="listaRicerca"
					property="codThesauro" /></td>
				<td ><bean-struts:write name="listaRicerca"
					property="livelloAutorita" /></td>
				<td >
					<c:choose>
						<c:when test="${listaRicerca.numTitoliBiblio gt 0}">
							<bean:message key="catalogazione.utilizzato.si" bundle="gestioneSemanticaLabels" />
						</c:when>
						<c:otherwise>
							<bean:message key="catalogazione.utilizzato.no" bundle="gestioneSemanticaLabels" />
						</c:otherwise>
					</c:choose>
				</td>
				<td ><bean-struts:write name="listaRicerca"
					property="termine" /></td>
				<td ><html:radio
					property="catalogazioneSemanticaComune.catalogazioneThesauro.codSelezionato"
					value="${listaRicerca.did}" /></td>
				<td ><bean-struts:define id="codval">
					<bean-struts:write name="listaRicerca" property="progr" />
				</bean-struts:define> <html:multibox
					property="catalogazioneSemanticaComune.catalogazioneThesauro.didMultiSelez"
					value="${listaRicerca.did}" /></td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>


