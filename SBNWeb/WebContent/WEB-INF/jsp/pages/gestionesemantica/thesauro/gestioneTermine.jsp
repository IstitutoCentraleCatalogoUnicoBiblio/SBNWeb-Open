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
	<sbn:navform action="/gestionesemantica/thesauro/GestioneTermine.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<html:hidden property="codice" /> <c:if
			test="${GestioneTermineForm.modalita eq 'CREA_PER_TRASCINA_TITOLI' or GestioneTermineForm.modalita eq 'CREA_PER_LEGAME_TERMINI'}">
			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionesemantica/thesauro/datiDidPartenza.jsp" />
			<br>
		</c:if> <c:if
			test="${GestioneTermineForm.modalita eq 'CREA_PER_LEGAME_TITOLO'}">
			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
			<br>
		</c:if>

		<table width="100%" border="0">
			<tr>
				<td class="etichetta" scope="col"><bean:message
					key="gestione.did" bundle="gestioneSemanticaLabels" /></td>
				<td><html:text property="dettaglio.did" readonly="true">
				</html:text></td>

				<td class="etichetta"><bean:message
					key="gestionesemantica.thesauro.thesauro"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:select styleClass="testoNormale"
					property="dettaglio.codThesauro"
					disabled="${GestioneTermineForm.enableConferma or (GestioneTermineForm.modalita ne 'CREA' and GestioneTermineForm.modalita ne 'CREA_PER_LEGAME_TITOLO')}">
					<html:optionsCollection property="listaThesauri" value="codice"
						label="descrizione" />
				</html:select></td>
				<td class="etichetta"><bean:message
					key="esamina.statoControllo" bundle="gestioneSemanticaLabels" /></td>
				<td><html:select styleClass="testoNormale"
					property="dettaglio.livAut"
					disabled="${GestioneTermineForm.enableConferma or GestioneTermineForm.modalita eq 'ESAMINA'}">
					<html:optionsCollection property="listaLivelloAutorita"
						value="codice" label="descrizione" />
				</html:select></td>
			</tr>
		</table>
		<table width="100%" border="0">
			<tr>
				<td align="center" class="etichetta"><bean:message
					key="gestione.testo" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale"
					property="dettaglio.testo" cols="90" rows="6"
					disabled="${GestioneTermineForm.enableConferma or GestioneTermineForm.modalita eq 'ESAMINA'}"></html:textarea>
				<sbn:tastiera limit="240" name="GestioneTermineForm"
					property="dettaglio.testo"
					visible="${!GestioneTermineForm.enableConferma and GestioneTermineForm.modalita ne 'ESAMINA'}" /></td>

			</tr>
		</table>
		<table width="100%" border="0">
			<tr>
				<td class="etichetta" align="center"><bean:message
					key="gestione.note" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale"
					property="dettaglio.note" cols="90" rows="6"
					disabled="${GestioneTermineForm.enableConferma or GestioneTermineForm.modalita eq 'ESAMINA'}"></html:textarea>
				<sbn:tastiera limit="240" name="GestioneTermineForm"
					property="dettaglio.note"
					visible="${!GestioneTermineForm.enableConferma and GestioneTermineForm.modalita ne 'ESAMINA'}" /></td>
			</tr>
			<tr>
				<td class="etichetta" align="center"><bean:message
					key="gestione.formaNome" bundle="gestioneSemanticaLabels" /> <html:select
					styleClass="testoNormale" property="dettaglio.formaNome"
					disabled="${!GestioneTermineForm.enableFormaNome}">
					<html:optionsCollection property="listaFormaNome" value="codice"
						label="descrizione" />
				</html:select></td>
			</tr>
		</table>
		<c:if
			test="${GestioneTermineForm.modalita eq 'ESAMINA' or GestioneTermineForm.modalita eq 'GESTIONE'}">
			<table width="100%" border="0">
				<tr>
					<td align="left" class="etichetta" scope="col"><bean:message
						key="gestione.inserito" bundle="gestioneSemanticaLabels" /></td>
					<td class="etichetta"><bean:message key="gestione.il"
						bundle="gestioneSemanticaLabels" /></td>
					<td><html:text styleId="testoNormale"
						property="dettaglio.dataIns" size="14" maxlength="20"
						readonly="true"></html:text></td>
					<td class="etichetta"><bean:message key="gestione.modificato"
						bundle="gestioneSemanticaLabels" /></td>
					<td class="etichetta"><bean:message key="gestione.il"
						bundle="gestioneSemanticaLabels" /></td>
					<td><html:text styleId="testoNormale"
						property="dettaglio.dataAgg" size="14" maxlength="20"
						readonly="true"></html:text></td>
				</tr>
			</table>
		</c:if> <!-- Titoli collegati --> <c:if
			test="${GestioneTermineForm.enableTitoliCollegati}">
			<table width="100%" border="0">
				<tr>
					<td align="center" class="etichetta" scope="col"><bean:message
						key="analitica.thesauro.titoli" bundle="gestioneSemanticaLabels" />
					</td>

					<td align="center" class="etichetta" scope="col"><bean:message
						key="analitica.polo" bundle="gestioneSemanticaLabels" />
					<td><html:text styleId="testoNormale"
						property="dettaglio.numTitoliPolo" size="10" readonly="true"></html:text>
					</td>

					<td align="center" class="etichetta" scope="col"><bean:message
						key="analitica.biblio" bundle="gestioneSemanticaLabels" />
					<td><html:text styleId="testoNormale"
						property="dettaglio.numTitoliBiblio" size="10" readonly="true"></html:text>
					</td>
				</tr>
			</table>
		</c:if></div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td><sbn:bottoniera buttons="listaPulsanti" /></td>
				<td>
				<layout:combo bundle="gestioneSemanticaLabels"
					label="button.esamina" name="GestioneTermineForm"
					button="button.esegui" property="idFunzioneEsamina"
					combo="comboGestioneEsamina" parameter="methodGestioneThes" />
				</td>

			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
