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
					key="etichette.label.ricercaPerIntervallo"
					bundle="gestioneStampeLabels" /></div>
				</td>
			</tr>
		</table>
		<table width="100%" align="center">
			<tr>
				<td scope="col" align="left">
				<div class="etichetta"><bean:message key="etichette.label.serie"
					bundle="gestioneStampeLabels" /></div>
				<html:select property="serie">
					<html:optionsCollection property="listaComboSerie" value="codice"
						label="codice" />
				</html:select>
				</td>


				<td scope="col" align="left">
				<div class="etichetta"><bean:message key="etichette.label.daInventario"
					bundle="gestioneStampeLabels" /></div>
				<html:text styleId="testoNormale"
					property="startInventario" size="10"></html:text>
				</td>

				<td scope="col" align="left">
				<div class="etichetta"><bean:message key="etichette.label.aInventario"
					bundle="gestioneStampeLabels" /></div>
				<html:text styleId="testoNormale"
					property="endInventario" size="10"></html:text>
				</td>
			</tr>
		</table>
