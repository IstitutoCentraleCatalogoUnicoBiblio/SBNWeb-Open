<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>


<!-- Intestazione Esamina In Polo -->
<table width="100%" border="0">
	<tr>
		<td class="etichetta"><bean:message key="label.livRicercaPolo" bundle="gestioneBibliograficaLabels" /></td>
	</tr>
</table>
<table width="100%" border="0">
	<tr>
		<td class="etichetta" scope="col"><bean:message key="esamina.cid"
			bundle="gestioneSemanticaLabels" /></td>
		<td><html:text property="cid" readonly="true" /></td>
		<td class="etichetta"><bean:message key="esamina.soggettario"
			bundle="gestioneSemanticaLabels" /></td>
		<td><html:select styleClass="testoNormale"
			property="dettSogGenVO.campoSoggettario" disabled="true">
			<html:optionsCollection property="listaSoggettari" value="codice"
				label="descrizione" />
		</html:select></td>
	</tr>

	<c:if test="${navForm.dettSogGenVO.gestisceEdizione}">
		<tr>
			<td colspan="2">&nbsp;</td>
			<td><bean:message key="ricerca.edizione" bundle="gestioneSemanticaLabels" /></td>
			<td><html:select styleClass="testoNormale"
					property="dettSogGenVO.edizioneSoggettario" disabled="true">
					<html:optionsCollection property="listaEdizioni"
						value="cd_tabellaTrim" label="ds_tabella" />
				</html:select></td>
		</tr>
	</c:if>

	<tr>
		<td class="etichetta"><bean:message key="esamina.statoControllo"
			bundle="gestioneSemanticaLabels" /></td>
		<td><html:select styleClass="testoNormale" property="dettSogGenVO.livAut"
			disabled="true">
			<html:optionsCollection property="listaStatoControllo"
				value="codice" label="descrizione" />
		</html:select></td>
		<td class="etichetta"><bean:message key="esamina.tipoDiSoggetto"
			bundle="gestioneSemanticaLabels" /></td>
		<td><html:select styleClass="testoNormale" property="dettSogGenVO.categoriaSoggetto"
			disabled="true">
			<html:optionsCollection property="listaTipoSoggetto" value="codice"
				label="descrizione" />
		</html:select></td>
	</tr>
</table>