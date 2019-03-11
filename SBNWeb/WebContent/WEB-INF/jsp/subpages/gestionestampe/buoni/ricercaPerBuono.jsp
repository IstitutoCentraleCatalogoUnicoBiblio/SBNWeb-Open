<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<table width="30%">
<BR>
	<tr>
		<td><bean:message key="documentofisico.numBuonoCaricoT"
			bundle="documentoFisicoLabels" /></td>
		<td colspan="4"><html:text styleId="testoNormale" property="numeroBuono" size="10"
			maxlength="9"></html:text></td>
	</tr>
</table>
