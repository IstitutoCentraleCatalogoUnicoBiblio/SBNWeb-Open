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
			key="etichette.label.formattazioneEtichetta"
			bundle="gestioneStampeLabels" /></div>
		</td>
	</tr>
</table>



<table width="100%" align="center">
	<tr>
		<td>
		<table width="200" align="center">
			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="etichette.label.bordiEtichetta" bundle="gestioneStampeLabels" /></div>
				</td>
				<td><html:checkbox
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.bordiEtichetta"></html:checkbox>
					<c:if test="${not modelliEtichetteGestioneForm.disable}">
						<html:hidden property="formattazioneModello.bordiEtichetta" value="false"/>
					</c:if>
					</td>
			</tr>

			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="etichette.label.larghEtichetta" bundle="gestioneStampeLabels" /></div>
				</td>
				<td><html:text styleId="testoNormale"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.larghezzaEtichetta" size="5"></html:text></td>
			</tr>

			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="etichette.label.altEtichetta" bundle="gestioneStampeLabels" />
				</div>
				</td>
				<td><html:text styleId="testoNormale"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.altezzaEtichetta" size="5"></html:text></td>
			</tr>
		</table>
		</td>
		<td>
		<table width="160" align="center">

			<tr>
				<td></td>
				<td scope="col" align="center"><html:text styleId="testoNormale"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.margineSupEtichetta" size="3"></html:text></td>
				<td></td>
			</tr>
			<tr>
				<td scope="col" align="center"><html:text styleId="testoNormale"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.margineSinEtichetta" size="3"></html:text></td>
				<td scope="col" align="center"><bean:message
					key="etichette.label.margini" bundle="gestioneStampeLabels" /></td>
				<td scope="col" align="center"><html:text styleId="testoNormale"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.margineDesEtichetta" size="3"></html:text></td>

			</tr>
			<tr>
				<td></td>
				<td scope="col" align="center"><html:text styleId="testoNormale"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.margineInfEtichetta" size="3"></html:text></td>
				<td></td>

			</tr>


		</table>
		</td>





		<td>
		<table width="120" align="center">
			<tr>
				<td>
				<div class="etichetta"><bean:message
					key="etichette.label.spazioEtichetta" bundle="gestioneStampeLabels" /></div>
				</td>
			</tr>
		</table>
		<table width="120" align="center">
			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="etichette.label.spazioEtichettaX"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td><html:text styleId="testoNormale"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.spaziaturaEtichetteX" size="3"></html:text></td>
			</tr>

			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message
					key="etichette.label.spazioEtichettaY"
					bundle="gestioneStampeLabels" /></div>
				</td>
				<td><html:text styleId="testoNormale"
					disabled="${modelliEtichetteGestioneForm.disable}"
					property="formattazioneModello.spaziaturaEtichetteY" size="3"></html:text></td>
			</tr>


		</table>
		</td>
	</tr>

</table>
