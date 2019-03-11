<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bs:size id="comboSize" name="StampaOnLineForm" property="elencoModelli" />
<l:greaterEqual name="comboSize" value="2">
	<!--Selezione Modello Via Combo-->
	<hr>
	<table width="100%" align="center">
		<tr>
			<td width="15%" scope="col">
			<div align="left" class="etichetta"><bean:message key="utenti.label.modello"
				bundle="gestioneStampeLabels" /></div>
			</td>
			<td align="left"><html:select styleClass="testoNormale" property="tipoModello">
				<html:optionsCollection property="elencoModelli" value="jrxml" label="descrizione" />
			</html:select></td>
		</tr>
	</table>
</l:greaterEqual>
<l:lessThan name="comboSize" value="2">
	<!--Selezione Modello Hidden-->
	<html:hidden property="tipoModello"	value="${StampaOnLineForm.elencoModelli[0].jrxml}" />
</l:lessThan>