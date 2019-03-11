<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>


<table>
	<tr>
		<td><bean:message key="label.documentofisico.uri.tipoInput"
				bundle="documentoFisicoLabels" /></td>

		<td>BID<html:radio property="richiesta.tipoInput" value="1" />&nbsp;
			<bean:message key="label.tipoOutput.inv" bundle="esportaLabels" />
			<html:radio property="richiesta.tipoInput" value="0" />&nbsp; <bean:message
				key="label.documentofisico.uri.shippingManifest"
				bundle="documentoFisicoLabels" /> <html:radio
				property="richiesta.tipoInput" value="5" /></td>
		<td colspan="3"></td>
	</tr>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
	<tr>
		<td><bean:message key="label.documentofisico.uri.prefix"
				bundle="documentoFisicoLabels" /></td>
		<td><html:text property="richiesta.prefisso" size="30"
				maxlength="50" /></td>
		<td colspan="3"></td>
	</tr>
	<tr>
		<td><bean:message key="label.documentofisico.uri.model"
				bundle="documentoFisicoLabels" /></td>
		<td><html:select property="model">
				<html:optionsCollection property="listaTipoModelloURI"
					value="cd_tabellaTrim" label="ds_tabella" />
			</html:select></td>
		<td colspan="3"></td>
	</tr>
	<tr>
		<td width="20%"><bean:message
				key="label.documentofisico.uri.eliminaSpaziUri"
				bundle="documentoFisicoLabels" /></td>
		<td><html:checkbox property="richiesta.eliminaSpaziUri" /> <html:hidden
				property="richiesta.eliminaSpaziUri" value="false" /></td>
		<td colspan="3"></td>
	</tr>
	<tr>
		<td><bean:message key="label.documentofisico.uri.suffix"
				bundle="documentoFisicoLabels" /></td>
		<td><html:text property="richiesta.suffisso" size="30"
				maxlength="50" /></td>
		<td><html:submit property="methodAcqUriCopiaDigitale">
				<bean:message key="button.test" bundle="documentoFisicoLabels" />
			</html:submit></td>
		<td><bs:write name="navForm" property="test" /></td>
		<td></td>
	</tr>

</table>
