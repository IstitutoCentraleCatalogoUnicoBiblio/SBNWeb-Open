<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<table border="0">
	<tr>
		<td><bean:message key="documentofisico.serieT"
				bundle="documentoFisicoLabels" /></td>
		<td><html:select property="serie"
				disabled="${currentForm.disableSerie}">
				<html:optionsCollection property="listaComboSerie" value="codice"
					label="codice" />
			</html:select></td>
		<td><bean:message key="documentofisico.buonoCaricoT"
				bundle="documentoFisicoLabels" /> <html:text styleId="testoNormale"
				property="buonoCarico" size="15" maxlength="9"></html:text></td>
	</tr>
	<tr>
		<td><bean:message key="documentofisico.dataCaricoT"
				bundle="documentoFisicoLabels" /></td>
		<td><html:text styleId="testoNormale" property="dataCarico"
				size="15" maxlength="10"></html:text></td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><bean:message key="documentofisico.ristampaBuonoCarico"
				bundle="documentoFisicoLabels" /> <html:checkbox
				property="ristampaDaNumBuono"
				disabled="${currentForm.disableRistampaNumBuono}"></html:checkbox> <html:hidden
				property="ristampaDaNumBuono" value="false" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
</table>
