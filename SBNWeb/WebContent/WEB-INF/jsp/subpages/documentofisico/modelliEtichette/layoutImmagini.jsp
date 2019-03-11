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
			key="etichette.label.immaginiEtichetta" bundle="gestioneStampeLabels" /></div>
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
			key="etichette.label.verticale" bundle="gestioneStampeLabels" /></td>

		<td scope="col" align="center"><bean:message key="etichette.label.X"
			bundle="gestioneStampeLabels" /></td>
		<td scope="col" align="center"><bean:message key="etichette.label.Y"
			bundle="gestioneStampeLabels" /></td>

		<td scope="col" align="center"><bean:message
			key="etichette.label.dimensioneOrizzontale"
			bundle="gestioneStampeLabels" /></td>
		<td scope="col" align="center"><bean:message
			key="etichette.label.dimensioneVerticale"
			bundle="gestioneStampeLabels" /></td>
	</tr>

	<logic:iterate id="rigaImmagine" name="modelliEtichetteGestioneForm"
		property="formattazioneModello.campiImmagine">
		<tr>
			<td scope="col" align="center"><bean:write name="rigaImmagine"
				property="nomeImmagine" /></td>
			<td scope="col" align="center"><html:checkbox indexed="true"
				disabled="${modelliEtichetteGestioneForm.disable}"
				name="rigaImmagine" property="presente">
			</html:checkbox>
			<c:if test="${not modelliEtichetteGestioneForm.disable}">
				<html:hidden property="presente" value="false" indexed="true" name="rigaImmagine" />
			</c:if>
			</td>
			<td scope="col" align="center"><html:checkbox indexed="true"
				disabled="${modelliEtichetteGestioneForm.disable}"
				name="rigaImmagine" property="verticale">
			</html:checkbox>
			<c:if test="${not modelliEtichetteGestioneForm.disable}">
				<html:hidden property="verticale" value="false" indexed="true" name="rigaImmagine" />
			</c:if>
			</td>
			<td scope="col" align="center"><html:text indexed="true"
				disabled="${modelliEtichetteGestioneForm.disable}"
				styleId="testoNormale" name="rigaImmagine"
				property="x" size="3">
			</html:text></td>
			<td scope="col" align="center"><html:text indexed="true"
				disabled="${modelliEtichetteGestioneForm.disable}"
				styleId="testoNormale" name="rigaImmagine" property="y" size="3">
			</html:text></td>
			<td scope="col" align="center"><html:text indexed="true"
				disabled="${modelliEtichetteGestioneForm.disable}"
				styleId="testoNormale" name="rigaImmagine"
				property="dimensioneVerticale" size="3">
			</html:text></td>
			<td scope="col" align="center"><html:text indexed="true"
				disabled="${modelliEtichetteGestioneForm.disable}"
				styleId="testoNormale" name="rigaImmagine"
				property="dimensioneOrizzontale" size="3">
			</html:text></td>

		</tr>

	</logic:iterate>




</table>
