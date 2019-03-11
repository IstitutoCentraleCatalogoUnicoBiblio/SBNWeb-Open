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
		<td width="100" class="etichetta"><bean:message
			key="dettaglio.tipoLegame" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" class="testoNormale"><html:text
			property="areaDatiLegameTitoloVO.tipoLegameNew" size="10" readonly="true"></html:text></td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message key="ricerca.vid"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="60" class="testoNormale"><html:text
			property="areaDatiLegameTitoloVO.idArrivo" size="10" readonly="true"></html:text></td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="ricerca.descrizione" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100"><html:textarea
			property="areaDatiLegameTitoloVO.descArrivo" cols="60" rows="1"
			readonly="true"></html:textarea></td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="dettaglio.notaLegame" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100"><html:textarea
			property="areaDatiLegameTitoloVO.noteLegameNew" cols="60" rows="1"></html:textarea></td>
	</tr>
</table>
