<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<table width="100%">

	<tr>
		<td width="20%"><bean:message key="label.documentofisico.uri.tipoInput"
				bundle="documentoFisicoLabels" /></td>
		<td><html:select property="richiesta.tipoInput">
				<html:optionsCollection property="listaTipoFileInput"
					value="cd_tabellaTrim" label="ds_tabella" />
			</html:select></td>
		<td colspan="3"></td>
	</tr>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>

</table>
