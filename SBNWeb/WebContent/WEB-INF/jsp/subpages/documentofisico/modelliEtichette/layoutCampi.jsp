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
			key="etichette.label.campiEtichetta" bundle="gestioneStampeLabels" /></div>
		</td>
	</tr>
</table>
<table width="100%" border="0" align="center">
	<tr>
		<td scope="col" align="center"><bean:message
			key="etichette.label.elemento" bundle="gestioneStampeLabels" /></td>
		<td scope="col" align="center"><bean:message
			key="etichette.label.visualizza" bundle="gestioneStampeLabels" /></td>
		<td scope="col" align="center"><bean:message
			key="etichette.label.concatena" bundle="gestioneStampeLabels" /></td>
		<td scope="col" align="center"><bean:message key="etichette.label.X"
			bundle="gestioneStampeLabels" /></td>
		<td scope="col" align="center"><bean:message key="etichette.label.Y"
			bundle="gestioneStampeLabels" /></td>
		<td scope="col" align="center"><bean:message
			key="etichette.label.verticale" bundle="gestioneStampeLabels" /></td>
		<td scope="col" align="center"><bean:message
			key="etichette.label.font" bundle="gestioneStampeLabels" /></td>
		<td scope="col" align="center"><bean:message
			key="etichette.label.dimensione" bundle="gestioneStampeLabels" /></td>
		<td scope="col" align="center"><bean:message
			key="etichette.label.grassetto" bundle="gestioneStampeLabels" /></td>
		<td scope="col" align="center"><bean:message
			key="etichette.label.corsivo" bundle="gestioneStampeLabels" /></td>
		<td scope="col" align="center"><bean:message
			key="etichette.label.posizione" bundle="gestioneStampeLabels" /></td>
	</tr>
	<logic:iterate id="rigaEtichetta"
		property="formattazioneModello.campiEtichetta"
		name="modelliEtichetteGestioneForm" indexId="riga">
		<tr>
			<td scope="col" align="center"><bean:write property="nomeCampo"
				name="rigaEtichetta" /></td>
			<td scope="col" align="center"><html:checkbox indexed="true"
				disabled="${modelliEtichetteGestioneForm.disable}"
				property="presente" name="rigaEtichetta">
			</html:checkbox>
				<c:if test="${not modelliEtichetteGestioneForm.disable}">
					<html:hidden property="presente" value="false" indexed="true" name="rigaEtichetta" />
				</c:if>
			</td>
			<td scope="col" align="center">
			<c:if test="${riga gt 1}">
				<html:checkbox indexed="true" disabled="${modelliEtichetteGestioneForm.disable}"
					property="concatena" name="rigaEtichetta">
				</html:checkbox>
				<c:if test="${not modelliEtichetteGestioneForm.disable}">
					<html:hidden property="concatena" value="false" indexed="true" name="rigaEtichetta" />
				</c:if>
			</c:if>
			</td>
			<td scope="col" align="center"><html:text styleId="testoNormale"
				indexed="true" name="rigaEtichetta"
				disabled="${modelliEtichetteGestioneForm.disable}" property="x"
				size="3"></html:text></td>
			<td scope="col" align="center"><html:text styleId="testoNormale"
				indexed="true" name="rigaEtichetta"
				disabled="${modelliEtichetteGestioneForm.disable}" property="y"
				size="3"></html:text></td>
			<td scope="col" align="center"><html:checkbox indexed="true"
				disabled="${modelliEtichetteGestioneForm.disable}"
				name="rigaEtichetta" property="verticale">
			</html:checkbox>
			<c:if test="${not modelliEtichetteGestioneForm.disable}">
				<html:hidden property="verticale" value="false"  indexed="true" name="rigaEtichetta" />
			</c:if>
			</td>
			<td scope="col" align="center"><html:select styleClass="testoNormale"
				indexed="true" name="rigaEtichetta"
				disabled="${modelliEtichetteGestioneForm.disable}" property="font">
				<html:optionsCollection property="listaFont" value="codice"
					label="descrizione" />
			</html:select></td>
			<td scope="col" align="center"><html:select styleClass="testoNormale"
				indexed="true" name="rigaEtichetta"
				disabled="${modelliEtichetteGestioneForm.disable}" property="punti">
				<html:optionsCollection property="listaPunti" value="codice"
					label="descrizione" />
			</html:select></td>
			<td scope="col" align="center"><html:checkbox indexed="true"
				name="rigaEtichetta"
				disabled="${modelliEtichetteGestioneForm.disable}"
				property="grassetto">
			</html:checkbox>
			<c:if test="${not modelliEtichetteGestioneForm.disable}">
				<html:hidden property="grassetto" value="false"  indexed="true" name="rigaEtichetta" />
			</c:if>
			</td>
			<td scope="col" align="center"><html:checkbox indexed="true"
				name="rigaEtichetta"
				disabled="${modelliEtichetteGestioneForm.disable}"
				property="corsivo">
			</html:checkbox>
			<c:if test="${not modelliEtichetteGestioneForm.disable}">
				<html:hidden property="corsivo" value="false"  indexed="true" name="rigaEtichetta" />
			</c:if>
			</td>
			<td scope="col" align="center"><html:select styleClass="testoNormale"
				indexed="true" name="rigaEtichetta"
				disabled="${modelliEtichetteGestioneForm.disable}"
				property="posizione">
				<html:optionsCollection property="listaPosizioni" value="codice"
					label="descrizione" />
			</html:select></td>
		</tr>
	</logic:iterate>
</table>
