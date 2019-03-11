<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<table width="100%" border="0">
	<c:choose>
		<c:when test="${currentForm.class.simpleName eq 'StampaEtichetteForm'}">
			<tr>
				<th>
				<div class="testoNormale"><bean:message
					key="documentofisico.inventariAutomatici" bundle="documentoFisicoLabels" /></div>
				</th>
				<th colspan="3">
				<div class="testoNormale"><html:radio property="selezione" value="A"
					onchange="this.form.submit()" /></div>
				</th>
			</tr>
			<tr>
				<td class="etichetta" scope="col" align="left"><bean:message
					key="documentofisico.bibliotecario" bundle="documentoFisicoLabels" /></td>
				<td scope="col" align="left"><html:text styleId="testoNormale"
					property="codBibliotec" size="12" title="Ricerca bibliotecario" name="currentForm"></html:text>
				<html:messages id="msg1" message="true" property="documentofisico.parameter.bottone"
					bundle="documentoFisicoLabels" /> <html:submit title="Ricerca biblioteca"
					styleClass="buttonImage" property="${msg1}">
					<bean:message key="documentofisico.bottone.SIFbibliotecario" bundle="documentoFisicoLabels" />
				</html:submit><bean-struts:write name="currentForm" property="nomeBibliotec" /></td>
				<td>&nbsp;&nbsp;&nbsp;</td>
			</tr>
			<tr>
				<td scope="col" class="etichetta" align="left"><bean:message
					key="documentofisico.dataIngressoDaT" bundle="documentoFisicoLabels" /></td>
				<td scope="col" align="left"><html:text styleId="testoNormale"
					property="dataInizio" size="10"></html:text><bean:message
					key="documentofisico.data.formato" bundle="documentoFisicoLabels" /></td>
				<td scope="col" class="etichetta" align="left"><bean:message
					key="documentofisico.dataIngressoAT" bundle="documentoFisicoLabels" /></td>
				<td scope="col" align="left"><html:text styleId="testoNormale"
					property="dataFine" size="10"></html:text> <bean:message
					key="documentofisico.data.formato" bundle="documentoFisicoLabels" /></td>
			</tr>
		</c:when>
	</c:choose>
	<tr>
		<th>
		<div class="testoNormale"><bean:message key="documentofisico.inventariDaFile"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th colspan="3">
		<div class="testoNormale"><html:radio property="selezione" value="F"
			onchange="this.form.submit()" /></div>
		</th>
	</tr>
	<c:choose>
		<c:when test="${currentForm.nomeFileAppoggioInv ne null}">
			<tr>
				<td colspan="2"></td>
				<td colspan="4"><bean-struts:write name="currentForm" property="nomeFileAppoggioInv" /></td>
			</tr>
		</c:when>
	</c:choose>
	<tr>
		<td>
		<div class="etichetta"><bean:message key="documentofisico.nomeFileAppoggioT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td colspan="3"><html:file property="fileEsterno" name="currentForm" size="80" />
		<html:messages id="msg1" message="true" property="documentofisico.parameter.bottone"
			bundle="documentoFisicoLabels" /> <html:submit title="Nome File Esterno"
			styleClass="pulsanti" property="${msg1}">
			<bean:message key="documentofisico.caricaFile" bundle="documentoFisicoLabels" />
		</html:submit></td>
	</tr>
	<tr>
		<th>
		<div class="testoNormale"><bean:message key="documentofisico.inventari"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th colspan="3">
		<div class="testoNormale"><html:radio property="selezione" value="N"
			onchange="this.form.submit()" /></div>
		</th>
	</tr>
	<tr>
		<table width="80%" border="0" align="center">
			<tr>
				<th width="33%" align="center"><bean:message key="documentofisico.serie"
					bundle="documentoFisicoLabels" />&nbsp;&nbsp;&nbsp;<bean:message
					key="documentofisico.numero" bundle="documentoFisicoLabels" /></th>
				<th width="33%" align="center"><bean:message key="documentofisico.serie"
					bundle="documentoFisicoLabels" />&nbsp;&nbsp;&nbsp;<bean:message
					key="documentofisico.numero" bundle="documentoFisicoLabels" /></th>
				<th width="33%" align="center"><bean:message key="documentofisico.serie"
					bundle="documentoFisicoLabels" />&nbsp;&nbsp;&nbsp;<bean:message
					key="documentofisico.numero" bundle="documentoFisicoLabels" /></th>
			</tr>
			<tr>
				<td align="center">01 <html:select property="serie01">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero01" size="10" maxlength="9"></html:text></td>
				<td align="center">02 <html:select property="serie02">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero02" size="10" maxlength="9"></html:text></td>
				<td align="center">03 <html:select property="serie03">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero03" size="10" maxlength="9"></html:text></td>
			</tr>
			<tr>
				<td align="center">04 <html:select property="serie04">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero04" size="10" maxlength="9"></html:text></td>
				<td align="center">05 <html:select property="serie05">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero05" size="10" maxlength="9"></html:text></td>
				<td align="center">06 <html:select property="serie06">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero06" size="10" maxlength="9"></html:text></td>
			</tr>
			<tr>
				<td align="center">07 <html:select property="serie07">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero07" size="10" maxlength="9"></html:text></td>
				<td align="center">08 <html:select property="serie08">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero08" size="10" maxlength="9"></html:text></td>
				<td align="center">09 <html:select property="serie09">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero09" size="10" maxlength="9"></html:text></td>
			</tr>
			<tr>
				<td align="center">10 <html:select property="serie10">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero10" size="10" maxlength="9"></html:text></td>
				<td align="center">11 <html:select property="serie11">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero11" size="10" maxlength="9"></html:text></td>
				<td align="center">12 <html:select property="serie12">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero12" size="10" maxlength="9"></html:text></td>
			</tr>
			<tr>
				<td align="center">13 <html:select property="serie13">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero13" size="10" maxlength="9"></html:text></td>
				<td align="center">14 <html:select property="serie14">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero14" size="10" maxlength="9"></html:text></td>
				<td align="center">15 <html:select property="serie15">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero15" size="10" maxlength="9"></html:text></td>
			</tr>
			<tr>
				<td align="center">16 <html:select property="serie16">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero16" size="10" maxlength="9"></html:text></td>
				<td align="center">17 <html:select property="serie17">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero17" size="10" maxlength="9"></html:text></td>
				<td align="center">18 <html:select property="serie18">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero18" size="10" maxlength="9"></html:text></td>
			</tr>
			<tr>
				<td align="center">19 <html:select property="serie19">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero19" size="10" maxlength="9"></html:text></td>
				<td align="center">20 <html:select property="serie20">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero20" size="10" maxlength="9"></html:text></td>
				<td align="center">21 <html:select property="serie21">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero21" size="10" maxlength="9"></html:text></td>
			</tr>
			<tr>
				<td align="center">22 <html:select property="serie22">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero22" size="10" maxlength="9"></html:text></td>
				<td align="center">23 <html:select property="serie23">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero23" size="10" maxlength="9"></html:text></td>
				<td align="center">24 <html:select property="serie24">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero24" size="10" maxlength="9"></html:text></td>
			</tr>
			<tr>
				<td align="center">25 <html:select property="serie25">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero25" size="10" maxlength="9"></html:text></td>
				<td align="center">26 <html:select property="serie26">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero26" size="10" maxlength="9"></html:text></td>
				<td align="center">27 <html:select property="serie27">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero27" size="10" maxlength="9"></html:text></td>
			</tr>
			<tr>
				<td align="center">28 <html:select property="serie28">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero28" size="10" maxlength="9"></html:text></td>
				<td align="center">29 <html:select property="serie29">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero29" size="10" maxlength="9"></html:text></td>
				<td align="center">30 <html:select property="serie30">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero30" size="10" maxlength="9"></html:text></td>
			</tr>
			<tr>
				<td align="center">31 <html:select property="serie31">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero31" size="10" maxlength="9"></html:text></td>
				<td align="center">32 <html:select property="serie32">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero32" size="10" maxlength="9"></html:text></td>
				<td align="center">33 <html:select property="serie33">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero33" size="10" maxlength="9"></html:text></td>
			</tr>
			<tr>
				<td align="center">34 <html:select property="serie34">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero34" size="10" maxlength="9"></html:text></td>
				<td align="center">35 <html:select property="serie35">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero35" size="10" maxlength="9"></html:text></td>
				<td align="center">36 <html:select property="serie36">
					<html:optionsCollection property="listaComboSerie" value="codice" label="codice" />
				</html:select> <html:text styleId="testoNormale" property="numero36" size="10" maxlength="9"></html:text></td>
			</tr>
		</table>
	</tr>
	<BR>
</table>
