<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<table>
	<tr>
		<td><bean:message key="servizi.sale.codiceSala"
				bundle="serviziLabels" /></td>
		<td><html:text property="ricerca.codSala" maxlength="2"
				size="5" /></td>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td><bean:message key="servizi.sale.descrizione"
				bundle="serviziLabels" /></td>
		<td><html:text property="ricerca.descrizione" maxlength="160"
				size="50" /></td>
		<td colspan="2">&nbsp;</td>
	</tr>
<!--
	<tr>
		<td><bean:message key="servizi.sale.salaConPostiLiberi"
				bundle="serviziLabels" /></td>
		<td>
			<html:checkbox property="ricerca.conPostiLiberi" />
			<html:hidden property="ricerca.conPostiLiberi" value="false" />
		</td>

	</tr>
-->
</table>

<hr>
<table>
	<tr>
		<td><bean:message key="servizi.label.elementiPerBlocco"
				bundle="serviziLabels" />:</td>
		<td><html:text property="ricerca.elementiPerBlocco"
				maxlength="3" size="4" /></td>
	</tr>
</table>
