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
		key="gestionesemantica.thesauro.terminiDiThesauro"
		bundle="gestioneSemanticaLabels" /></td>
	<td colspan="3">
  <html:text
		property="ricercaComune.ricercaThesauro.terminiThes"
		styleId="testoNormale" size="50"></html:text>
   <html:select
		styleClass="testoNormale"
		property="ricercaComune.ricercaPerUnDescrittore">
		<html:optionsCollection property="listaRicercaPerUnTermine"
			value="codice" label="descrizione" />
	</html:select></td>


</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:text
		property="ricercaComune.ricercaThesauro.terminiThes1"
		styleId="testoNormale" size="50"></html:text></td>
	<td class="etichetta"><bean:message
		key="gestionesemantica.thesauro.utilizzati"
		bundle="gestioneSemanticaLabels" />
	<html:checkbox property="ricercaComune.ricercaThesauro.utilizzati"></html:checkbox></td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:text
		property="ricercaComune.ricercaThesauro.terminiThes2"
		styleId="testoNormale" size="50"></html:text></td>
	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:text
		property="ricercaComune.ricercaThesauro.terminiThes3"
		styleId="testoNormale" size="50"></html:text></td>
	<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:text
		property="ricercaComune.ricercaThesauro.terminiThes4"
		styleId="testoNormale" size="50"></html:text></td>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
</tr>
<tr>
	<td>&nbsp;</td>
	<td><html:text
		property="ricercaComune.ricercaThesauro.terminiThes5"
		styleId="testoNormale" size="50"></html:text></td>
	<td>&nbsp;</td>
	<td>&nbsp;</td>
</tr>
