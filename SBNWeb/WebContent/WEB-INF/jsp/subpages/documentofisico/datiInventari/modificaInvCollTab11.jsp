<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<table width="100%" border="0">
	<tr>
		<td width="20%"><bean:message key="documentofisico.tipoDigitalizzazioneT"
			bundle="documentoFisicoLabels" /></td>
		<td><html:select property="recInv.digitalizzazione">
			<html:optionsCollection property="listaTipoDigit" value="codice" label="descrizione" />
		</html:select></td>
	</tr>
	<tr>
		<td><bean:message key="documentofisico.rifTecaDigitaleT"
			bundle="documentoFisicoLabels" /></td>
		<td><html:select property="recInv.rifTecaDigitale">
			<html:optionsCollection property="listaTecaDigitale" value="codice" label="descrizione" />
		</html:select></td>
	</tr>
	<tr>
		<td><bean:message key="documentofisico.dispDaRemotoT"
			bundle="documentoFisicoLabels" /></td>
		<td><html:select property="recInv.dispDaRemoto">
			<html:optionsCollection property="listaDispDaRemoto" value="codice"
				label="descrizione" />
		</html:select></td>
	</tr>
	<tr>
		<td>
		<div class="testo"><bean:message key="documentofisico.datiPerAccessoDaRemotoT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td><html:textarea cols="80" rows="5" property="recInv.idAccessoRemoto"></html:textarea></td>
		<%--<td class="testo"><html:text styleId="testo" property="recInv.idAccessoRemoto"
			size="100" maxlength="80"></html:text></td>	--%></tr>
</table>
