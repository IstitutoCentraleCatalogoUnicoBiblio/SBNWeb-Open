<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<layout:page>
	<sbn:navform
		action="/gestionesemantica/catalogazionesemantica/VariaLegameSoggettoTitolo.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!-- INIZIO PAGINA --> <jsp:include
			page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
		<table width="100%" border="0">
			<tr>
				<!-- Intestazione livello di ricerca -->
				<td class="etichetta" scope="col"><bean:message
					key="label.livRicercaPolo" bundle="gestioneBibliograficaLabels" /></td>
			</tr>
			<tr>
				<td align="center" class="etichetta"><bean:message
					key="crea.notaAlLegame" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:textarea styleId="testoNormale"
					property="notaAlLegame" cols="90" rows="6"></html:textarea> <sbn:tastiera
					limit="90" name="VariaLegameSoggettoTitoloForm"
					property="notaAlLegame" /></td>
			</tr>
		</table>
		<jsp:include page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/intestazioneSoggetto.jsp" flush="true" />
		<table width="100%" border="0">
			<tr>
				<td align="center" class="etichetta"><bean:message
					key="gestione.testo" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:text styleId="testoNormale" property="dettaglio.testo"
					size="138" readonly="true"></html:text></td>
			</tr>
			<tr>
				<td class="etichetta" align="center"><bean:message
					key="gestione.note" bundle="gestioneSemanticaLabels" /></td>
			</tr>
			<tr>
				<td><html:text styleId="testoNormale" property="dettaglio.note"
					size="138" readonly="true"></html:text></td>
			</tr>
		</table>

		<table width="100%" border="0">
			<tr>
				<td align="left" class="etichetta" scope="col"><bean:message
					key="esamina.inserito" bundle="gestioneSemanticaLabels" /></td>
				<td class="etichetta"><bean:message key="esamina.il"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale"
					property="dettaglio.dataIns" size="14" maxlength="20" readonly="true"></html:text></td>
				<td class="etichetta"><bean:message key="esamina.modificato"
					bundle="gestioneSemanticaLabels" /></td>
				<td class="etichetta"><bean:message key="esamina.il"
					bundle="gestioneSemanticaLabels" /></td>
				<td><html:text styleId="testoNormale" property="dettaglio.dataAgg"
					size="14" maxlength="20" readonly="true"></html:text></td>
			</tr>

		</table>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit
					property="methodVariaLegameTitSog">
					<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<%--<td align="center"><html:submit property="methodVariaLegameTitSog">
					<bean:message key="button.invioIndice"
						bundle="gestioneSemanticaLabels" />
				</html:submit></td>--%>
				<td align="center"><html:submit
					property="methodVariaLegameTitSog">
					<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit
					property="methodVariaLegameTitSog">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
		</div>
	</sbn:navform>
</layout:page>

