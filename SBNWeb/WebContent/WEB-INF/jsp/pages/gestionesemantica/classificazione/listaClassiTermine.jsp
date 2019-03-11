<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/classificazione/listaClassiTermine.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<jsp:include page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" flush="true" />
		<sbn:blocchi numBlocco="bloccoSelezionato"
			numNotizie="classi.totRighe" parameter="methodSintTheCla"
			totBlocchi="classi.totBlocchi" elementiPerBlocco="classi.maxRighe" />
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
						property="selected" disabled="${navForm.conferma}" /></td>
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
		</table>
		<sbn:blocchi numBlocco="bloccoSelezionato"
			numNotizie="classi.totRighe" parameter="methodSintTheCla"
			totBlocchi="classi.totBlocchi" elementiPerBlocco="classi.maxRighe"
			bottom="true" />
		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td><layout:combo bundle="gestioneSemanticaLabels"
					label="catalogazione.legame" name="navForm"
					button="button.conferma" property="idFunzioneLegame"
					combo="comboGestioneLegame" parameter="methodSintTheCla" /></td>

				<sbn:checkAttivita idControllo="RANKING">
					<td><html:submit property="methodSintTheCla"
						styleClass="buttonFrecciaSu" titleKey="servizi.bottone.frecciaSu"
						bundle="serviziLabels">
						<bean:message key="servizi.bottone.frecciaSu"
							bundle="serviziLabels" />
					</html:submit></td>
					<td><html:submit property="methodSintTheCla"
						styleClass="buttonFrecciaGiu" titleKey="servizi.bottone.frecciaGiu"
						bundle="serviziLabels">
						<bean:message key="servizi.bottone.frecciaGiu"
							bundle="serviziLabels" />
					</html:submit></td>
				</sbn:checkAttivita>

				<td align="center"><html:submit property="methodSintTheCla">
					<bean:message key="button.chiudi" bundle="gestioneSemanticaLabels" />
				</html:submit></td>

			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>

