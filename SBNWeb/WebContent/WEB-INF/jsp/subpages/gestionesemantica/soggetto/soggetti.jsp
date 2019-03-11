<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>


<tr>
	<td class="etichetta"><bean:message
		key="gestionesemantica.soggetto.descrittoriDiSoggetto"
		bundle="gestioneSemanticaLabels" /></td>
	<td colspan="3"><html:text
		property="ricercaComune.ricercaSoggetto.descrittoriSogg" maxlength="80"
		styleId="testoNormale" size="50"></html:text><html:select
		styleClass="testoNormale"
		property="ricercaComune.posizioneDescrittore">
		<html:optionsCollection property="listaRicercaPerUnDescrittore"
			value="codice" label="descrizione" />
	</html:select></td>


</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:text
		property="ricercaComune.ricercaSoggetto.descrittoriSogg1" maxlength="80"
		styleId="testoNormale" size="50"></html:text></td>
	<td class="etichetta">
		<bean:message key="gestionesemantica.soggetto.utilizzati" bundle="gestioneSemanticaLabels" />
		<html:checkbox property="ricercaComune.ricercaSoggetto.utilizzati" />
		<html:hidden property="ricercaComune.ricercaSoggetto.utilizzati" value="false" />
	</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:text
		property="ricercaComune.ricercaSoggetto.descrittoriSogg2" maxlength="80"
		styleId="testoNormale" size="50"></html:text></td>
	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:text
		property="ricercaComune.ricercaSoggetto.descrittoriSogg3" maxlength="80"
		styleId="testoNormale" size="50"></html:text></td>
	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

</tr>


