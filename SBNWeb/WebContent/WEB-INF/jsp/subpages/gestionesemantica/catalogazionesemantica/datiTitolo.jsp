<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<table width="100%" border="0">
	<tr>
		<td class="etichetta">
			<strong><bean:message key="catalogazione.bid"
					bundle="gestioneSemanticaLabels" /> </strong>
		</td>
		<td class="etichetta">
			<strong><bean:message key="catalogazione.testoBid"
					bundle="gestioneSemanticaLabels" /> </strong>
		</td>
	</tr>
	<tr>
		<td width="10%">
			<html:text styleId="testoNoBold"
				property="catalogazioneSemanticaComune.bid" size="14"
				readonly="${CatalogazioneSemanticaForm.enable}" />
		</td>
		<td width="100%" align="left">
			<span style="width: 100%;">
				<html:text styleId="testoNoBold" styleClass="expandedLabel"
					property="catalogazioneSemanticaComune.testoBid"
					readonly="${CatalogazioneSemanticaForm.enable}" />
			</span>
		</td>
	</tr>
</table>
