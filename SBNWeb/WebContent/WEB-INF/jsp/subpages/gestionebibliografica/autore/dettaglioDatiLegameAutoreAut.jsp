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
		<td width="100" class="etichetta"><bean:message key="ricerca.vid"
			bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text property="dettAutoreVO.vidPadre"
			readonly="true"></html:text></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="ricerca.descrizione" bundle="gestioneBibliograficaLabels" /></td>
		<td><html:textarea property="dettAutoreVO.nominativoPadre"
			rows="1" readonly="true"></html:textarea></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="dettaglio.tipoLegame"
			bundle="gestioneBibliograficaLabels" /></td>
			<td><html:text property="appoTipoLegameCastor"
						readonly="true" title="${dettaglioAutoreForm.descTipoLegameCastor}" size="5">
			</html:text></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="dettaglio.notaLegame" bundle="gestioneBibliograficaLabels" /></td>
		<td><html:textarea
			property="dettAutoreVO.notaAlLegame" rows="1" readonly="true"></html:textarea>
		</td>
	</tr>
</table>
