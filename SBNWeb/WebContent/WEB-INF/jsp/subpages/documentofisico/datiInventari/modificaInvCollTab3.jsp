<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>


<table width="100%" border="0">
	<tr>
		<td>
		<div class="testo"><bean:message
			key="documentofisico.motivoScaricoT" bundle="documentoFisicoLabels" /></div>
		</td>
		<td colspan="3"><html:select property="recInv.codScarico" onchange="this.form.submit()">
			<html:optionsCollection property="listaMotivoScarico" value="codice"
				label="descrizione" />
		</html:select></td>
	</tr>
	<c:choose>
		<c:when
			test="${(modificaInvCollForm.recInv.codSit eq '3'
									 && modificaInvCollForm.recInv.codScarico eq 'T') or (modificaInvCollForm.trasferimento == true)}">
			<tr>
				<!--  verso polo + biblioteca-->
				<td width="15%"><bean:message key="documentofisico.versoLaBiblioT"
					bundle="documentoFisicoLabels" /></td>
				<td colspan="3"><html:text styleId="testoNormale" readonly="true"
					property="recInv.codPoloScar" size="5"></html:text><html:text styleId="testo"
					readonly="true" property="recInv.versoBibDescr" size="70"></html:text><html:submit
					styleClass="buttonImage" property="methodModInvColl"
					title="Ricerca biblioteca">
					<bean:message key="documentofisico.bottone.SIFbibl" bundle="documentoFisicoLabels" />
				</html:submit></td>
			</tr>
		</c:when>
	</c:choose>
	<tr>
		<td>
		<div class="testo"><bean:message
			key="documentofisico.dataScaricoT" bundle="documentoFisicoLabels" /></div>
		</td>
		<td>
		<html:text styleId="testo"
			property="recInv.dataScarico" size="10" maxlength="10"></html:text>
		</td>
		<td>
		<div class="testo"><bean:message
			key="documentofisico.numeroBuonoScaricoT"
			bundle="documentoFisicoLabels" /></div>
		</td>
		<td>
		<html:text styleId="testo"
			property="recInv.numScarico" size="9" maxlength="10"></html:text>
		</td>
	</tr>
	<tr>
		<td>
		<div class="testo"><bean:message
			key="documentofisico.dataDeliberaT" bundle="documentoFisicoLabels" /></div>
		</td>
		<td colspan="3">
		<html:text styleId="testo"
			property="recInv.dataDelibScar" size="10" maxlength="10"></html:text>
		</td>
	</tr>
	<tr>
		<td>
		<div class="testo"><bean:message
			key="documentofisico.testoDeliberaT" bundle="documentoFisicoLabels" /></div>
		</td>
		<td colspan="3">
		<html:textarea styleId="testo" cols="60"
			rows="" property="recInv.deliberaScarico"></html:textarea>
		</td>
	</tr>
</table>
