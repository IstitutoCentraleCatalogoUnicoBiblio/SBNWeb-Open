<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<table width="100%" border="0">
	<tr>
		<th>
		<div class="testoNormale"><bean:message key="documentofisico.bidDaFile"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th colspan="4">
		<div class="testoNormale"><html:radio property="selezione" value="F"
			onchange="this.form.submit()" /></div>
		</th>
	</tr>
	<c:choose>
		<c:when test="${currentForm.nomeFileAppoggioBid ne null}">
			<tr>
				<td colspan="2"></td>
				<td colspan="5"><bean-struts:write name="currentForm" property="nomeFileAppoggioBid" /></td>
			</tr>
		</c:when>
	</c:choose>
	<tr>
		<td>
		<div class="etichetta"><bean:message key="documentofisico.nomeFileAppoggioT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td colspan="4"><html:file property="fileEsterno" name="currentForm" size="80" />
		<html:messages id="msg1" message="true" property="documentofisico.parameter.bottone"
			bundle="documentoFisicoLabels" /> <html:submit title="Nome File Esterno"
			styleClass="pulsanti" property="${msg1}">
			<bean:message key="documentofisico.caricaFile" bundle="documentoFisicoLabels" />
		</html:submit></td>
	</tr>
	<tr>
		<th>
		<div class="testoNormale"><bean:message key="documentofisico.bidT"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th colspan="4">
		<div class="testoNormale"><html:radio property="selezione" value="N"
			onchange="this.form.submit()" /></div>
		</th>
	</tr>
	<tr>
		<th width="20%" align="center"><bean:message key="schede.label.numIdentificativo"
			bundle="gestioneStampeLabels" /></th>
		<th width="20%" align="center"><bean:message key="schede.label.numIdentificativo"
			bundle="gestioneStampeLabels" /></th>
		<th width="20%" align="center"><bean:message key="schede.label.numIdentificativo"
			bundle="gestioneStampeLabels" /></th>
		<th width="20%" align="center"><bean:message key="schede.label.numIdentificativo"
			bundle="gestioneStampeLabels" /></th>
		<th width="20%" align="center"><bean:message key="schede.label.numIdentificativo"
			bundle="gestioneStampeLabels" /></th>
	</tr>
	<tr>
		<td align="center">01 <html:text styleId="testoNormale"
			property="numIdentificativo01" size="10" maxlength="10"></html:text></td>
		<td align="center">02 <html:text styleId="testoNormale"
			property="numIdentificativo02" size="10" maxlength="10"></html:text></td>
		<td align="center">03 <html:text styleId="testoNormale"
			property="numIdentificativo03" size="10" maxlength="10"></html:text></td>
		<td align="center">04 <html:text styleId="testoNormale"
			property="numIdentificativo04" size="10" maxlength="10"></html:text></td>
		<td align="center">05 <html:text styleId="testoNormale"
			property="numIdentificativo05" size="10" maxlength="10"></html:text></td>
	</tr>
	<tr>
		<td align="center">06 <html:text styleId="testoNormale"
			property="numIdentificativo06" size="10" maxlength="10"></html:text></td>
		<td align="center">07 <html:text styleId="testoNormale"
			property="numIdentificativo07" size="10" maxlength="10"></html:text></td>
		<td align="center">08 <html:text styleId="testoNormale"
			property="numIdentificativo08" size="10" maxlength="10"></html:text></td>
		<td align="center">09 <html:text styleId="testoNormale"
			property="numIdentificativo09" size="10" maxlength="10"></html:text></td>
		<td align="center">10 <html:text styleId="testoNormale"
			property="numIdentificativo10" size="10" maxlength="10"></html:text></td>
	</tr>
	<tr>
		<td align="center">11 <html:text styleId="testoNormale"
			property="numIdentificativo11" size="10" maxlength="10"></html:text></td>
		<td align="center">12 <html:text styleId="testoNormale"
			property="numIdentificativo12" size="10" maxlength="10"></html:text></td>
		<td align="center">13 <html:text styleId="testoNormale"
			property="numIdentificativo13" size="10" maxlength="10"></html:text></td>
		<td align="center">14 <html:text styleId="testoNormale"
			property="numIdentificativo14" size="10" maxlength="10"></html:text></td>
		<td align="center">15 <html:text styleId="testoNormale"
			property="numIdentificativo15" size="10" maxlength="10"></html:text></td>
	</tr>
	<tr>
		<td align="center">16 <html:text styleId="testoNormale"
			property="numIdentificativo16" size="10" maxlength="10"></html:text></td>
		<td align="center">17 <html:text styleId="testoNormale"
			property="numIdentificativo17" size="10" maxlength="10"></html:text></td>
		<td align="center">18 <html:text styleId="testoNormale"
			property="numIdentificativo18" size="10" maxlength="10"></html:text></td>
		<td align="center">19 <html:text styleId="testoNormale"
			property="numIdentificativo19" size="10" maxlength="10"></html:text></td>
		<td align="center">20 <html:text styleId="testoNormale"
			property="numIdentificativo20" size="10" maxlength="10"></html:text></td>
	</tr>
	<tr>
		<td align="center">21 <html:text styleId="testoNormale"
			property="numIdentificativo21" size="10" maxlength="10"></html:text></td>
		<td align="center">22 <html:text styleId="testoNormale"
			property="numIdentificativo22" size="10" maxlength="10"></html:text></td>
		<td align="center">23 <html:text styleId="testoNormale"
			property="numIdentificativo23" size="10" maxlength="10"></html:text></td>
		<td align="center">24 <html:text styleId="testoNormale"
			property="numIdentificativo24" size="10" maxlength="10"></html:text></td>
		<td align="center">25 <html:text styleId="testoNormale"
			property="numIdentificativo25" size="10" maxlength="10"></html:text></td>
	</tr>
	<tr>
		<td align="center">26 <html:text styleId="testoNormale"
			property="numIdentificativo26" size="10" maxlength="10"></html:text></td>
		<td align="center">27 <html:text styleId="testoNormale"
			property="numIdentificativo27" size="10" maxlength="10"></html:text></td>
		<td align="center">28 <html:text styleId="testoNormale"
			property="numIdentificativo28" size="10" maxlength="10"></html:text></td>
		<td align="center">29 <html:text styleId="testoNormale"
			property="numIdentificativo29" size="10" maxlength="10"></html:text></td>
		<td align="center">30 <html:text styleId="testoNormale"
			property="numIdentificativo30" size="10" maxlength="10"></html:text></td>
	</tr>
	<tr>
		<td align="center">31 <html:text styleId="testoNormale"
			property="numIdentificativo31" size="10" maxlength="10"></html:text></td>
		<td align="center">32 <html:text styleId="testoNormale"
			property="numIdentificativo32" size="10" maxlength="10"></html:text></td>
		<td align="center">33 <html:text styleId="testoNormale"
			property="numIdentificativo33" size="10" maxlength="10"></html:text></td>
		<td align="center">34 <html:text styleId="testoNormale"
			property="numIdentificativo34" size="10" maxlength="10"></html:text></td>
		<td align="center">35 <html:text styleId="testoNormale"
			property="numIdentificativo35" size="10" maxlength="10"></html:text></td>
	</tr>
	<tr>
		<td align="center">36 <html:text styleId="testoNormale"
			property="numIdentificativo36" size="10" maxlength="10"></html:text></td>
		<td align="center">37 <html:text styleId="testoNormale"
			property="numIdentificativo37" size="10" maxlength="10"></html:text></td>
		<td align="center">38 <html:text styleId="testoNormale"
			property="numIdentificativo38" size="10" maxlength="10"></html:text></td>
		<td align="center">39 <html:text styleId="testoNormale"
			property="numIdentificativo39" size="10" maxlength="10"></html:text></td>
		<td align="center">40 <html:text styleId="testoNormale"
			property="numIdentificativo40" size="10" maxlength="10"></html:text></td>
	</tr>
</table>
