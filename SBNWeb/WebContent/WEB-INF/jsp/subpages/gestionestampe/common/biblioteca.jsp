<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<table width="100%" align="center">
	<tr>
		<td scope="col" align="left">
		<div class="etichetta"><bean:message key="etichette.label.biblioteca"
			bundle="gestioneStampeLabels" /></div>
		</td>
		<td scope="col" align="left"><html:text styleId="testoNormale"
			property="biblioteca" size="5" disabled="true"></html:text></td>
		<td scope="col" align="left">
		<div style="float:none;"><html:submit property="methodStampaEtichette">
			<bean:message key="button.cambioBiblioteca"
				bundle="gestioneStampeLabels" />
		</html:submit></div>
		<td>
		<td scope="col" align="left">
		<div class="etichetta"><bean:message
			key="etichette.label.bibliotecaPrincipale"
			bundle="gestioneStampeLabels" /></div>
		</td>
	</tr>
</table>
