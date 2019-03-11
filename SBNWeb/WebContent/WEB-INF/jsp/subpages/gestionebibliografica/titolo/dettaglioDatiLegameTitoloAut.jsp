<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area superiore con canali e filtri
		almaviva2 - Inizio Codifica Agosto 2006
-->

<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message key="ricerca.bid"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="60" class="testoNormale"><html:text property="dettAutoreVO.vidPadre"
			size="10" readonly="true"></html:text></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="ricerca.descrizione" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100"><html:textarea property="dettAutoreVO.nominativoPadre"
			cols="60" rows="1" readonly="true"></html:textarea></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="dettaglio.codrelaz" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" class="testoNormale"><html:text property="dettAutoreVO.relatorCode"
			size="10" readonly="true"></html:text></td>

		<td width="300" class="testoNormale">
		<bean:message key="dettaglio.legameTitAut.VoceStrumento" bundle="gestioneBibliograficaLabels" />
		<html:text property="dettAutoreVO.specStrumVoci" size="10" readonly="true" title="${dettaglioAutoreForm.descSpecStrumVoci}"></html:text>
		</td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="ricerca.responsabilita" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" class="testoNormale"><html:text
			property="dettAutoreVO.responsabilita" size="10" readonly="true"></html:text></td>

		<td class="etichetta"><bean:message key="dettaglio.legameTitAut.Facoltativo"
			bundle="gestioneBibliograficaLabels" />: <html:checkbox
			property="dettAutoreVO.superfluo" disabled="true"></html:checkbox></td>
		<td class="etichetta"><bean:message key="dettaglio.legameTitAut.Incerto"
			bundle="gestioneBibliograficaLabels" />: <html:checkbox
			property="dettAutoreVO.incerto" disabled="true"></html:checkbox></td>

	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="dettaglio.notaLegame" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100"><html:textarea
			property="dettAutoreVO.notaAlLegame" cols="60" rows="1" readonly="true"></html:textarea>
		</td>
	</tr>
</table>
