<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:hidden property="codice" />

<table border="0">
	<tr>
		<td class="etichetta"><bean:message key="analitica.soggettario"
			bundle="gestioneSemanticaLabels" /></td>
		<td class="etichetta"><html:select styleClass="testoNormale"
			property="ricercaComune.codSoggettario" disabled="true">
			<html:optionsCollection property="listaSoggettari" value="codice" label="descrizione" />
		</html:select></td>
		<td class="etichetta">
			<c:if test="${navForm.treeElementViewSoggetti.dettaglio.gestisceEdizione}">
				<bean:message key="ricerca.edizione" bundle="gestioneSemanticaLabels" />&nbsp;<html:select
				styleClass="testoNormale"
				property="treeElementViewSoggetti.dettaglio.edizioneSoggettario" disabled="true">
				<html:optionsCollection property="listaEdizioni" value="cd_tabellaTrim" label="ds_tabella" />
				</html:select>
			</c:if>
		</td>
		<td class="etichetta"><bean:message
			key="analitica.tipoDiSoggetto" bundle="gestioneSemanticaLabels" />&nbsp;<html:select
			styleClass="testoNormale"
			property="treeElementViewSoggetti.dettaglio.categoriaSoggetto" disabled="true">
			<html:optionsCollection property="listaTipoSoggetto" value="codice"
				label="descrizione" />
		</html:select></td>
	</tr>
	<tr>
		<td class="etichetta"><bean:message key="analitica.stato"
			bundle="gestioneSemanticaLabels" /></td>
		<td class="etichetta"><html:select styleClass="testoNormale"
			property="treeElementViewSoggetti.livelloAutorita" disabled="true">
			<html:optionsCollection property="listaStatoControllo" value="codice"
				label="descrizione" />
		</html:select></td>
		<td colspan="2">
			<logic:notEmpty name="navForm" property="datiCondivisione">
				<bean:message key="gestionesemantica.soggetto.SoggettoIndice" bundle="gestioneSemanticaLabels" />&colon;&nbsp;<bs:write name="navForm" property="datiCondivisione.cidIndice"/>
			</logic:notEmpty>
		</td>
	</tr>
</table>
