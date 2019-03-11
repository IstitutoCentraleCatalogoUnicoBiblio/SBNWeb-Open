<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/gestionesemantica/thesauro/ListaThesauri.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<jsp:include
			page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" />
		<c:if
			test="${ListaThesauriForm.modalita eq 'CREA_LEGAME_TERMINI'
							or ListaThesauriForm.modalita eq 'TRASCINA_TITOLI'}">
			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionesemantica/thesauro/datiDidPartenza.jsp" />

		</c:if> <c:if test="${ListaThesauriForm.modalita eq 'CREA_LEGAME_TITOLO'}">

			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
			<br>
		</c:if>

		<table border="0">
			<tr>
				<td class="etichetta"><bean:message
					key="gestionesemantica.thesauro.thesauro"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:select styleClass="testoNormale"
					property="ricercaComune.codThesauro" disabled="true">
					<html:optionsCollection property="listaThesauri" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
		</table>
		<sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
			parameter="methodSinteticaSog" totBlocchi="totBlocchi"
			elementiPerBlocco="maxRighe" livelloRicerca="P" /> <logic:notEmpty
			name="ListaThesauriForm" property="output">
			<table class="sintetica">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.progr"
						bundle="gestioneSemanticaLabels" /></th>
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
					<bean:message key="sintetica.titoli"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.titoliBib"
						bundle="gestioneSemanticaLabels" /></th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.termine"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
				</tr>
				<bean-struts:define id="color" value="#FEF1E2" />
				<logic:iterate id="listaRicerca" property="output.risultati"
					name="ListaThesauriForm" offset="${ListaThesauriForm.offset}"
					indexId="progr">
					<sbn:rowcolor var="color" index="progr" />
					<tr bgcolor="${color}">
						<td ><sbn:anchor name="listaRicerca"
							property="progr" /> <sbn:linkbutton index="did"
							name="listaRicerca" value="progr" key="button.analitica"
							bundle="gestioneSemanticaLabels" title="analitica"
							property="codSelezionato"
							withAnchor="false" /></td>
						<td ><sbn:linkbutton index="did"
							name="listaRicerca" value="did" key="button.analitica"
							bundle="gestioneSemanticaLabels" title="analitica"
							property="codSelezionato"
							withAnchor="false" /></td>
						<td ><bean-struts:write
							name="listaRicerca" property="codThesauro" /></td>
						<td ><bean-struts:write
							name="listaRicerca" property="livelloAutorita" /></td>
						<td ><sbn:linkbutton
							bundle="gestioneSemanticaLabels" name="listaRicerca" index="did"
							key="button.polo" property="codSelezionato" title="titoli polo"
							value="numTitoliPolo"
							disabled="${listaRicerca.numTitoliPolo eq '0'}" /></td>
						<td ><sbn:linkbutton
							bundle="gestioneSemanticaLabels" name="listaRicerca" index="did"
							key="button.biblio" property="codSelezionato"
							title="titoli biblio" value="numTitoliBiblio"
							disabled="${listaRicerca.numTitoliBiblio eq '0'}" /></td>
						<td ><bean-struts:write
							name="listaRicerca" property="termine" /></td>
						<td ><html:radio property="codSelezionato"
							value="${listaRicerca.did}" /></td>
						<td ><bean-struts:define id="codval">
							<bean-struts:write name="listaRicerca" property="progr" />
						</bean-struts:define> <c:if test="${ListaThesauriForm.modalita eq 'CERCA'}">
							<html:multibox property="didMultiSelez"
								value="${listaRicerca.did}" />
						</c:if></td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty></div>
		<div id="divFooterCommon"><sbn:blocchi numBlocco="numBlocco"
			numNotizie="totRighe" parameter="methodSinteticaSog"
			totBlocchi="totBlocchi" elementiPerBlocco="maxRighe" bottom="true" />
		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<c:if test="${ListaThesauriForm.modalita ne 'CERCA'}">
					<td align="center"><html:submit property="methodSinteticaThes">
						<bean:message key="button.scegli" bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</c:if>

				<sbn:checkAttivita idControllo="CREA">
					<td align="center"><html:submit property="methodSinteticaThes">
						<bean:message key="button.crea" bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</sbn:checkAttivita>

				<td align="center"><html:submit property="methodSinteticaThes">
					<bean:message key="button.analitica"
						bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodSinteticaThes">
					<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodSinteticaThes">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit></td>

				<td align="center"><html:submit property="methodSinteticaThes"
					styleClass="buttonSelezTutti">
					<bean:message key="button.selTutti"
						bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodSinteticaThes"
					styleClass="buttonSelezNessuno">
					<bean:message key="button.deselTutti"
						bundle="gestioneSemanticaLabels" />
				</html:submit></td>

				<td>
				<layout:combo bundle="gestioneSemanticaLabels"
					label="button.esamina" name="ListaThesauriForm"
					button="button.esegui" property="idFunzioneEsamina"
					combo="comboGestioneEsamina" parameter="methodSinteticaThes" /></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>

