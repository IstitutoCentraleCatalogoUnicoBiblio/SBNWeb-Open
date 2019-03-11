<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<layout:page>
	<sbn:navform
		action="/gestionesemantica/catalogazionesemantica/GestioneLegameTitoloTermine.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<!-- INIZIO PAGINA -->
			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
			<table width="100%" border="0">
				<tr>
					<!-- Intestazione livello di ricerca -->
					<td class="etichetta" scope="col">
						Ricerca eseguita sulla base dati Locale
					</td>
				</tr>
				<tr>
					<td align="center" class="etichetta">
						<bean:message key="crea.notaAlLegameTh"
							bundle="gestioneSemanticaLabels" />
					</td>
				</tr>
				<tr>
					<td>
						<html:textarea styleId="testoNormale" property="notaLegame"
							cols="90" rows="6"
							disabled="${navForm.modalita eq 'CANCELLA'}" />
						<sbn:tastiera limit="90" name="navForm"
							property="notaLegame" visible="${navForm.modalita ne 'CANCELLA'}" />
					</td>
				</tr>
			</table>
			<table width="100%" border="0">
				<tr>
					<td class="etichetta" scope="col">
						<bean:message key="esamina.did" bundle="gestioneSemanticaLabels" />
					</td>
					<td>
						<html:text property="dettaglio.did" readonly="true">
						</html:text>
					</td>
					<td class="etichetta">
						<bean:message key="gestionesemantica.thesauro.thesauro"
							bundle="gestioneSemanticaLabels" />
					</td>
					<td>
						<html:select styleClass="testoNormale"
							property="dettaglio.codThesauro" disabled="true">
							<html:optionsCollection property="listaThesauri" value="codice"
								label="descrizione" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="etichetta">
						<bean:message key="esamina.statoControllo"
							bundle="gestioneSemanticaLabels" />
					</td>
					<td>
						<html:select styleClass="testoNormale" property="dettaglio.livAut"
							disabled="true">
							<html:optionsCollection property="listaLivelloAutorita"
								value="codice" label="descrizione" />
						</html:select>
					</td>
					<td class="etichetta">
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
			</table>
			<table width="100%" border="0">
				<tr>
					<td align="center" class="etichetta">
						<bean:message key="gestione.testo"
							bundle="gestioneSemanticaLabels" />
					</td>
				</tr>
				<tr>
					<td>
						<html:text styleClass="expandedLabel" property="dettaglio.testo"
							readonly="true"></html:text>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="center">
						<bean:message key="gestione.note" bundle="gestioneSemanticaLabels" />
					</td>
				</tr>
				<tr>
					<td>
						<html:text styleClass="expandedLabel" property="dettaglio.note"
							readonly="true"></html:text>
					</td>
				</tr>
			</table>
			<c:if test="${navForm.modalita eq 'MODIFICA'}">
				<table width="100%" border="0">
					<tr>
						<td align="left" class="etichetta" scope="col">
							<bean:message key="esamina.inserito"
								bundle="gestioneSemanticaLabels" />
						</td>
						<td class="etichetta">
							<bean:message key="esamina.il" bundle="gestioneSemanticaLabels" />
						</td>
						<td>
							<html:text styleId="testoNormale" property="dettaglio.dataIns"
								size="14" maxlength="20" readonly="true"></html:text>
						</td>
						<td class="etichetta">
							<bean:message key="esamina.modificato"
								bundle="gestioneSemanticaLabels" />
						</td>
						<td class="etichetta">
							<bean:message key="esamina.il" bundle="gestioneSemanticaLabels" />
						</td>
						<td>
							<html:text styleId="testoNormale" property="dettaglio.dataAgg"
								size="14" maxlength="20" readonly="true"></html:text>
						</td>
					</tr>

				</table>
			</c:if>
			<c:if test="${navForm.modalita eq 'CREA' and not empty navForm.classi.risultati}">
			<hr />
			<h3><bean:message key="semantica.classi.thes" bundle="gestioneSemanticaLabels" /></h3>
			<table class="sintetica">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="catalogazione.progr"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="ricerca.sistema" bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="ricerca.edizione"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="ricerca.simbolo" bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="catalogazione.headerStato"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="catalogazione.parole"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
				</tr>
				<bs:define id="color" value="#FEF1E2" />
				<logic:iterate id="item" property="classi.risultati" name="navForm"	indexId="riga">
					<sbn:rowcolor var="color" index="riga" />
					<tr bgcolor="${color}">
						<td ><sbn:anchor name="item"	property="progr" />
						<sbn:linkbutton index="repeatableId"
							name="item" value="progr" key="button.gestione"
							bundle="gestioneSemanticaLabels" title="gestione"
							property="selected" /></td>
						<td ><bs:write name="item"
							property="simboloDewey.sistema" /></td>
						<td ><bs:write name="item"
							property="simboloDewey.edizione" /></td>
						<td ><bs:write name="item"
							property="simboloDewey.simbolo" /></td>
						<td ><bs:write name="item"
							property="livelloAutorita" /></td>
						<td ><bs:write name="item"
							property="parole" /></td>
						<td ><html:radio
							name="navForm" property="selected"
							value="${item.repeatableId}" /></td>
					</tr>
				</logic:iterate>
				<c:set var="riga" value="${riga + 1}" />
				<sbn:rowcolor var="color" index="riga" />
				<tr bgcolor="${color}">
					<td>&nbsp;</td>
					<td  colspan="5">
						<bean:message key="thesauro.legame.tit.nessuna.classe" bundle="gestioneSemanticaLabels" />
					</td>
					<td >
						<html:radio	name="navForm" property="selected" value="${navForm.classi.repeatableId}" />
					</td>
				</tr>
			</table>
			<br />
			</c:if>
			<div id="divFooter">
				<table align="center">
					<tr>
						<td>
							<sbn:bottoniera buttons="listaPulsanti" />
						</td>
					</tr>
				</table>
			</div>
		</div>
	</sbn:navform>
</layout:page>

