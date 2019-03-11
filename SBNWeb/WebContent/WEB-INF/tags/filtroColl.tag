<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>

<%@ attribute name="name" required="true"%>
<%@ attribute name="mostraColl" required="true"%>
<%@ attribute name="sezione" required="true"%>
<%@ attribute name="collDa" required="true"%>
<%@ attribute name="collA" required="true"%>
<%@ attribute name="specDa" required="true"%>
<%@ attribute name="specA" required="true"%>

<%@ attribute name="mostraSerie" required="true"%>
<%@ attribute name="serie" required="false"%>
<%@ attribute name="listaSerie" required="false"%>
<%@ attribute name="invDa" required="false"%>
<%@ attribute name="invA" required="false"%>

<%@ attribute name="mostraInv" required="true"%>

<%@ attribute name="parameter" required="true"%>


<c:if test="${mostraColl}">
<table>
	<tr>
		<td class="testo">
			&nbsp;
		</td>
		<td class="testo">
			<bean:message key="label.sezione" bundle="esportaLabels" />
		</td>
		<td class="testo">
			<html:text name="${name}" property="${sezione}" />
		</td>
		<td class="testo">
			<html:submit styleClass="buttonImage" property="${parameter}">
				<bean:message key="button.cercasezione" bundle="esportaLabels" />
			</html:submit>
		</td>
	</tr>

	<tr>
		<td class="testo">
			<bean:message key="label.dalla" bundle="esportaLabels" />&nbsp;
		</td>
		<td class="testo">
			<bean:message key="label.collocazione" bundle="esportaLabels" />&nbsp;
		</td>
		<td class="testo" colspan="2">
			<html:text name="${name}" property="${collDa}" style="width: 100%;" />
		</td>
		<td class="testo" width="10%">
			<html:submit styleClass="buttonImage" property="${parameter}">
				<bean:message key="button.cercacollocazioneDa" bundle="esportaLabels" />
			</html:submit>
		</td>
		<td class="testo">
			<bean:message key="label.specificazione" bundle="esportaLabels" />
		</td>
		<td>
			<html:text name="${name}" property="${specDa}" style="width: 100%;" />
		</td>
		<td class="testo" width="15%">
			<html:submit styleClass="buttonImage" property="${parameter}">
				<bean:message key="button.cercaspecificazioneDa" bundle="esportaLabels" />
			</html:submit>
		</td>
	</tr>

	<tr>
		<td class="testo">
			<bean:message key="label.alla" bundle="esportaLabels" />
		</td>
		<td class="testo">
			<bean:message key="label.collocazione" bundle="esportaLabels" />
		</td>
		<td class="testo" colspan="2">
			<html:text name="${name}" property="${collA}" style="width: 100%;" />
		</td>
		<td class="testo">
			<html:submit styleClass="buttonImage" property="${parameter}">
				<bean:message key="button.cercacollocazioneA" bundle="esportaLabels" />
			</html:submit>
		</td>
		<td class="testo">
			<bean:message key="label.specificazione" bundle="esportaLabels" />
		</td>
		<td>
			<html:text name="${name}" property="${specA}" style="width: 100%;" />
		</td>
		<td class="testo">
			<html:submit styleClass="buttonImage" property="${parameter}">
				<bean:message key="button.cercaspecificazioneA" bundle="esportaLabels" />
			</html:submit>
		</td>
	</tr>
</table>
</c:if>

<c:if test="${mostraSerie}">
	<hr />
	<table>
		<tr>
			<td class="testo">Serie</td>
			<td class="testo" width="18%">
				<html:select  styleClass="testoNormale"  name="${name}" property="${serie}" style="width:200px">
					<html:optionsCollection  property="${listaSerie}" value="codice" label="codiceDescrizione" />
				</html:select>
			</td>
			<td class="testo">
				<bean:message key="label.dal" bundle="esportaLabels" />&nbsp;<bean:message
					key="label.nr" bundle="esportaLabels" />
			</td>
			<td class="testo" colspan="2">
				<html:text name="${name}" property="${invDa}" style="width: 100%;"></html:text>
			</td>
			<td class="testo">&nbsp;</td>
			<td class="testo">
				<bean:message key="label.al" bundle="esportaLabels" />&nbsp;<bean:message key="label.nr" bundle="esportaLabels" />
			</td>
			<td class="testo" colspan="2">
				<html:text name="${name}" property="${invA}" style="width: 100%;"></html:text>
			</td>
			<td width="50%">&nbsp;</td>
		</tr>
	</table>
</c:if>

<c:if test="${mostraInv}">
<table width="100%" border="0"><!--
	<tr>
		<th width="2%" class="etichetta" scope="col">&nbsp;</th>
		<th width="9%" class="etichetta" scope="col"><bean:message
			key="documentofisico.serie" bundle="documentoFisicoLabel" /></th>
		<th width="18%" class="etichetta" scope="col"><bean:message
			key="documentofisico.numero" bundle="documentoFisicoLabel" /></th>
		<th width="2%" class="etichetta" scope="col">&nbsp;</th>
		<th width="9%" class="etichetta" scope="col"><bean:message
			key="documentofisico.serie" bundle="documentoFisicoLabel" /></th>
		<th width="18%" class="etichetta" class="etichetta" scope="col"><bean:message
			key="documentofisico.numero" bundle="documentoFisicoLabel" /></th>
		<th width="2%" class="etichetta" scope="col">&nbsp;</th>
		<th width="9%" class="etichetta" scope="col"><bean:message
			key="documentofisico.serie" bundle="documentoFisicoLabel" /></th>
		<th width="18%" class="etichetta" scope="col"><bean:message
			key="documentofisico.numero" bundle="documentoFisicoLabel" /></th>
	</tr>
	--><tr>
		<th scope="col" align="left">
		<div class="etichetta"><bean:message key="documentofisico.serie"
			bundle="documentoFisicoLabel" /></div>
		</th>
		<th scope="col" align="left">
		<div class="etichetta"><bean:message key="documentofisico.numero"
			bundle="documentoFisicoLabel" /></div>
		</th>
		<th scope="col" align="left">
		<div class="etichetta"><bean:message key="documentofisico.serie"
			bundle="documentoFisicoLabel" /></div>
		</th>
		<th scope="col" align="left">
		<div class="etichetta"><bean:message key="documentofisico.numero"
			bundle="documentoFisicoLabel" /></div>
		</th>
		<th scope="col" align="left">
		<div class="etichetta"><bean:message key="documentofisico.serie"
			bundle="documentoFisicoLabel" /></div>
		</th>
		<th scope="col" align="left">
		<div class="etichetta"><bean:message key="documentofisico.numero"
			bundle="documentoFisicoLabel" /></div>
		</th>
	</tr>
	<tr>
		<td scope="col" align="left">01 <html:select property="serie01">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero01" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">02 <html:select property="serie02">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero02" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">03 <html:select property="serie03">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero03" size="10"></html:text></div>
		</td>
	</tr>
	<tr>
		<td scope="col" align="left">04 <html:select property="serie04">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero04" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">05 <html:select property="serie05">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero05" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">06 <html:select property="serie06">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero06" size="10"></html:text></div>
		</td>
	</tr>
	<tr>
		<td scope="col" align="left">07 <html:select property="serie07">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero07" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">08 <html:select property="serie08">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero08" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">09 <html:select property="serie09">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero09" size="10"></html:text></div>
		</td>
	</tr>
	<tr>
		<td scope="col" align="left">10 <html:select property="serie10">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero10" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">11 <html:select property="serie11">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero11" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">12 <html:select property="serie12">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero12" size="10"></html:text></div>
		</td>
	</tr>
	<tr>
		<td scope="col" align="left">13 <html:select property="serie13">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero13" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">14 <html:select property="serie14">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero14" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">15 <html:select property="serie15">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero15" size="10"></html:text></div>
		</td>
	</tr>
	<tr>
		<td scope="col" align="left">16 <html:select property="serie16">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero16" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">17 <html:select property="serie17">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero17" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">18 <html:select property="serie18">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero18" size="10"></html:text></div>
		</td>
	</tr>
	<tr>
		<td scope="col" align="left">19 <html:select property="serie19">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero19" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">20 <html:select property="serie20">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero20" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">21 <html:select property="serie21">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero21" size="10"></html:text></div>
		</td>
	</tr>
	<tr>
		<td scope="col" align="left">22 <html:select property="serie22">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero22" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">23 <html:select property="serie23">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero23" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">24 <html:select property="serie24">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero24" size="10"></html:text></div>
		</td>
	</tr>
	<tr>
		<td scope="col" align="left">25 <html:select property="serie25">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero25" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">26 <html:select property="serie26">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero26" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">27 <html:select property="serie27">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero27" size="10"></html:text></div>
		</td>
	</tr>
	<tr>
		<td scope="col" align="left">28 <html:select property="serie28">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero28" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">29 <html:select property="serie29">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero29" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">30 <html:select property="serie30">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero30" size="10"></html:text></div>
		</td>
	</tr>
	<tr>
		<td scope="col" align="left">31 <html:select property="serie31">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero31" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">32 <html:select property="serie32">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero32" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">33 <html:select property="serie33">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero33" size="10"></html:text></div>
		</td>
	</tr>
	<tr>
		<td scope="col" align="left">34 <html:select property="serie34">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero34" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">35 <html:select property="serie35">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero35" size="10"></html:text></div>
		</td>
		<td scope="col" align="left">36 <html:select property="serie36">
			<html:optionsCollection property="listaComboSerie" value="codice"
				label="codice" />
		</html:select></td>
		<td scope="col" align="left">
		<div class="etichetta"><html:text styleId="testoNormale"
			property="numero36" size="10"></html:text></div>
		</td>
	</tr>
</table>
</c:if>
