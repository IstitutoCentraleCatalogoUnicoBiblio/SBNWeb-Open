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
		<td colspan="4" class="testoNormale">
 			<html:text styleId="testoNormale" property="codBibOrd" size="5" disabled="true"></html:text>
            <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodInterrogPerGestionaliTit">
					<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
			</html:submit><bs:write name="interrogazioneTitoloPerGestionaliForm" property="descrBibOrd" />
		</td>
	</tr>
	<tr>
		<td width="130" class="etichetta"><bean:message key="ricerca.gestionali.tipoOrdine" bundle="gestioneBibliograficaLabels" />:</td>
		<td width="60" class="testoNormale">
			<html:select property="codTipoOrdine" style="width:36px">
			<html:optionsCollection property="listaTipoOrdine" value="codice" label="descrizioneCodice" />
			</html:select>
		</td>
		<td width="130" class="etichetta"><bean:message	key="ricerca.gestionali.annoOrdine" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" class="testoNormale"><html:text	property="annoOrdine" size="10"></html:text></td>
		<td></td>
		<td class="etichetta"><bean:message	key="ricerca.gestionali.numeroOrdine" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" class="testoNormale"><html:text	property="numeroOrdine" size="10"></html:text></td>
	</tr>
</table>
