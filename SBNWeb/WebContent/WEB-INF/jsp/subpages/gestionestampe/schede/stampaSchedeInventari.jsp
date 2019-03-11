<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<table width="100%"
	style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
	<tr>
		<td valign="top" scope="col" align="left">
		<div class="etichetta"><bean:message key="schede.label.selInvSerie"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td valign="top" scope="col" align="left">
		<div class="etichetta"><bean:message key="schede.label.selInvNumero"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td valign="top" scope="col" align="left">
		<div class="etichetta"><bean:message key="schede.label.selInvSerie"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td valign="top" scope="col" align="left">
		<div class="etichetta"><bean:message key="schede.label.selInvNumero"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td valign="top" scope="col" align="left">
		<div class="etichetta"><bean:message key="schede.label.selInvSerie"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td valign="top" scope="col" align="left">
		<div class="etichetta"><bean:message key="schede.label.selInvNumero"
			bundle="gestioneStampeLabels" /></div>
		</td>
	</tr>
	<tr>
		<td valign="top" scope="col" align="left">01 <html:select styleClass="testoNormale"
			property="selInvSerie01">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero01" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">02 <html:select styleClass="testoNormale"
			property="selInvSerie02">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero02" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">03 <html:select styleClass="testoNormale"
			property="selInvSerie03">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero03" size="10" maxlength="50"></html:text></td>
	</tr>
	<tr>
		<td valign="top" scope="col" align="left">04 <html:select styleClass="testoNormale"
			property="selInvSerie04">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero04" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">05 <html:select styleClass="testoNormale"
			property="selInvSerie05">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero05" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">06 <html:select styleClass="testoNormale"
			property="selInvSerie06">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero06" size="10" maxlength="50"></html:text></td>
	</tr>
	<tr>
		<td valign="top" scope="col" align="left">07 <html:select styleClass="testoNormale"
			property="selInvSerie07">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero07" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">08 <html:select styleClass="testoNormale"
			property="selInvSerie08">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero08" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">09 <html:select styleClass="testoNormale"
			property="selInvSerie09">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero09" size="10" maxlength="50"></html:text></td>
	</tr>
	<tr>
		<td valign="top" scope="col" align="left">10 <html:select styleClass="testoNormale"
			property="selInvSerie10">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero10" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">11 <html:select styleClass="testoNormale"
			property="selInvSerie11">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero11" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">12 <html:select styleClass="testoNormale"
			property="selInvSerie12">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero12" size="10" maxlength="50"></html:text></td>
	</tr>
	<tr>
		<td valign="top" scope="col" align="left">13 <html:select styleClass="testoNormale"
			property="selInvSerie13">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero13" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">14 <html:select styleClass="testoNormale"
			property="selInvSerie14">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero14" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">15 <html:select styleClass="testoNormale"
			property="selInvSerie15">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero15" size="10" maxlength="50"></html:text></td>
	</tr>
	<tr>
		<td valign="top" scope="col" align="left">16 <html:select styleClass="testoNormale"
			property="selInvSerie16">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero16" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">17 <html:select styleClass="testoNormale"
			property="selInvSerie17">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero17" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">18 <html:select styleClass="testoNormale"
			property="selInvSerie18">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero18" size="10" maxlength="50"></html:text></td>
	</tr>
	<tr>
		<td valign="top" scope="col" align="left">19 <html:select styleClass="testoNormale"
			property="selInvSerie19">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero19" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">20 <html:select styleClass="testoNormale"
			property="selInvSerie20">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero20" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">21 <html:select styleClass="testoNormale"
			property="selInvSerie21">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero21" size="10" maxlength="50"></html:text></td>
	</tr>
	<tr>
		<td valign="top" scope="col" align="left">22 <html:select styleClass="testoNormale"
			property="selInvSerie22">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero22" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">23 <html:select styleClass="testoNormale"
			property="selInvSerie23">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero23" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">24 <html:select styleClass="testoNormale"
			property="selInvSerie24">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero24" size="10" maxlength="50"></html:text></td>
	</tr>
	<tr>
		<td valign="top" scope="col" align="left">25 <html:select styleClass="testoNormale"
			property="selInvSerie25">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero25" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">26 <html:select styleClass="testoNormale"
			property="selInvSerie26">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero26" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">27 <html:select styleClass="testoNormale"
			property="selInvSerie27">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero27" size="10" maxlength="50"></html:text></td>
	</tr>
	<tr>
		<td valign="top" scope="col" align="left">28 <html:select styleClass="testoNormale"
			property="selInvSerie28">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero28" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">29 <html:select styleClass="testoNormale"
			property="selInvSerie29">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero29" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">30 <html:select styleClass="testoNormale"
			property="selInvSerie30">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero30" size="10" maxlength="50"></html:text></td>
	</tr>
	<tr>
		<td valign="top" scope="col" align="left">31 <html:select styleClass="testoNormale"
			property="selInvSerie31">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero31" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">32 <html:select styleClass="testoNormale"
			property="selInvSerie32">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero32" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">33 <html:select styleClass="testoNormale"
			property="selInvSerie33">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero33" size="10" maxlength="50"></html:text></td>
	</tr>
	<tr>
		<td valign="top" scope="col" align="left">34 <html:select styleClass="testoNormale"
			property="selInvSerie34">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero34" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">35 <html:select styleClass="testoNormale"
			property="selInvSerie35">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero35" size="10" maxlength="50"></html:text></td>
		<td valign="top" scope="col" align="left">36 <html:select styleClass="testoNormale"
			property="selInvSerie36">
			<html:optionsCollection property="listaSelInvSerie" value="codice"
				label="codiceDescrizione" />
		</html:select></td>
		<td valign="top" scope="col" align="left"><html:text styleId="testoNormale"
			property="selInvNumero36" size="10" maxlength="50"></html:text></td>
	</tr>
</table>
