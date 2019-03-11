<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<table width="40%">
	<tr>
		<td><bean:message key="documentofisico.annoFatturaT" bundle="documentoFisicoLabels" /></td>
		<td colspan="4"><html:text styleId="testoNormale" property="annoFattura" size="15"
			maxlength="12"></html:text></td>
	</tr>
	<BR>
	<tr>
		<td><bean:message key="documentofisico.numFatturaT" bundle="documentoFisicoLabels" /></td>
		<td><html:text styleId="testoNormale" property="numFattura" size="25"
			maxlength="20"></html:text></td>
	</tr>
</table>