<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<table width="100%" align="center">
			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="etichette.label.ricercaPerInventari"
					bundle="gestioneStampeLabels" /></div>
				</td>
			</tr>
		</table>
<table width="100%" align="center">
	<tr>
		<td scope="col" align="left">
		<div class="etichetta"><bean:message key="etichette.label.serie"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td scope="col" align="left">
		<div class="etichetta"><bean:message key="etichette.label.numero"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td scope="col" align="left">
		<div class="etichetta"><bean:message key="etichette.label.serie"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td scope="col" align="left">
		<div class="etichetta"><bean:message key="etichette.label.numero"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td scope="col" align="left">
		<div class="etichetta"><bean:message key="etichette.label.serie"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td scope="col" align="left">
		<div class="etichetta"><bean:message key="etichette.label.numero"
			bundle="gestioneStampeLabels" /></div>
		</td>
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
