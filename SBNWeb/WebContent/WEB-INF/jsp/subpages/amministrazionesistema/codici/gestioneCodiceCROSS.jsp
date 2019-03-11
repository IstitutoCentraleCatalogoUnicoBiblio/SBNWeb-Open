<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>



<table>
	<tr>
		<td>
			<bean:write name="navForm" property="configCodici.nomeTabellaP" />&nbsp;
			&lpar;<bean:write name="navForm" property="configCodici.tipoTabP" />&rpar;:&nbsp;
		</td>
		<td><html:select property="dettaglio.flag1">
				<html:optionsCollection property="listaCodiciP" value="codice"
					label="descrizione" />
			</html:select></td>
	</tr>
	<tr>
		<td
			><bean:write name="navForm"	property="configCodici.nomeTabellaC" />&nbsp;
			&lpar;<bean:write name="navForm" property="configCodici.tipoTabC" />&rpar;:&nbsp;
		</td>
		<td><html:select property="dettaglio.flag2">
				<html:optionsCollection property="listaCodiciC" value="codice"
					label="descrizione" />
			</html:select></td>
	</tr>

	<tr>
		<td><bean:message key="dettaglio.codici.nuovo.data"
				bundle="amministrazioneSistemaLabels" />&nbsp; <span
			style="font-size: 80%;">(GG-MM-AAAA)</span>&nbsp;:</td>
		<td><html:text property="dettaglio.dataAttivazione" size="12"
				maxlength="10" disabled="true" /></td>
	</tr>
</table>

