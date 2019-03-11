<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area Musica
		almaviva2 - Inizio Codifica Agosto 2006
-->
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>


<table border="0">

	<tr>
		<td width="130" class="etichetta"><bean:message key="ricerca.gestionali.codBib" bundle="gestioneBibliograficaLabels" />:</td>
		<td><html:text disabled="true" styleId="testoNormale" property="codBib" size="5"
			maxlength="3"></html:text> <html:submit title="Lista Biblioteche"
			styleClass="buttonImageListaSezione" disabled="false"
			property="methodInterrogPerGestionaliTit">
			<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
		</html:submit><bs:write name="interrogazioneTitoloPerGestionaliForm" property="descrBib" /></td>
	</tr>

	<tr>
		<td width="130" class="etichetta"><bean:message	key="ricerca.gestionali.serieInventario" bundle="gestioneBibliograficaLabels" /></td>
		<td><html:select property="serieInventario">
			<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
		</html:select></td>
		<td></td>
		<td class="etichetta"><bean:message	key="ricerca.gestionali.numeroInventario" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" class="testoNormale"><html:text	property="numeroInventario" size="10"></html:text></td>
	</tr>
</table>
