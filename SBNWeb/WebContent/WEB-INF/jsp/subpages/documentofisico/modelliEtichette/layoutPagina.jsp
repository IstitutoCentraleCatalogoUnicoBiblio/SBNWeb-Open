<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>



<table width="100%" align="center">
	<tr>
		<td scope="col" align="left">
		<div class="etichetta"><bean:message
			key="pagina.label.formattazionePagina" bundle="gestioneStampeLabels" /></div>
		</td>
	</tr>
</table>
<table width="100%" align="center">
	<tr>
		<td>
		<table width="200" align="center">
			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message key="pagina.label.larghPagina"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td><html:text styleId="testoNormale"
					property="formattazioneModello.larghezzaPagina"
					disabled="${modelliEtichetteGestioneForm.disable}" size="5"></html:text></td>
			</tr>

			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message key="pagina.label.altPagina"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td><html:text styleId="testoNormale"
					property="formattazioneModello.altezzaPagina"
					disabled="${modelliEtichetteGestioneForm.disable}" size="5"></html:text></td>
			</tr>
		</table>
		</td>
		<td>
		<table width="160" align="center">
			<tr>
				<td></td>
				<td><html:text styleId="testoNormale"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.margineSup" size="3"></html:text></td>
				<td></td>
			</tr>
			<tr>
				<td><html:text styleId="testoNormale"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.margineSin" size="3"></html:text></td>
				<td><bean:message key="pagina.label.margini"
					bundle="gestioneStampeLabels" /></td>
				<td><html:text styleId="testoNormale"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.margineDes" size="3"></html:text></td>

			</tr>
			<tr>
				<td></td>
				<td><html:text styleId="testoNormale"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.margineInf" size="3"></html:text></td>
				<td></td>

			</tr>
		</table>
		</td>
		<td>
		<table width="100" align="center">
			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="pagina.label.unitaDiMisura" bundle="gestioneStampeLabels" /></div>
				</td>
			<tr>
				<td><html:radio styleId="radioFormato"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.unitaDiMisura" value="cm" /> <label
					for="radioFormato"><bean:message key="pagina.label.cm"
					bundle="gestioneStampeLabels" /></label></td>
			</tr>
			<tr>
				<td><html:radio styleId="radioFormato"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.unitaDiMisura" value="mm" /> <label
					for="radioFormato"><bean:message key="pagina.label.mm"
					bundle="gestioneStampeLabels" /></label></td>
			</tr>
			<tr>
				<td><html:radio styleId="radioFormato"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.unitaDiMisura" value="inch" /> <label
					for="radioFormato"><bean:message key="pagina.label.inch"
					bundle="gestioneStampeLabels" /></label></td>
			</tr>
			<tr>
				<td><html:radio styleId="radioFormato"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.unitaDiMisura" value="punti" /> <label
					for="radioFormato"><bean:message key="pagina.label.punti"
					bundle="gestioneStampeLabels" /></label></td>
			</tr>
		</table>
		</td>
	</tr>

</table>

