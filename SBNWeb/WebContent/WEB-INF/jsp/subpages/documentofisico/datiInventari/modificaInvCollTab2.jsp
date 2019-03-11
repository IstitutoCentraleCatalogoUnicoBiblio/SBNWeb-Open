<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<c:choose>
	<c:when test="${modificaInvCollForm.ordine}">
		<table width="100%" border="0">
			<tr>
				<td>
				<div class="testo"><bean:message key="documentofisico.ordineT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td>
				<div class="testo"><html:text styleId="testo"
							disabled="${modificaInvCollForm.disable}"
					property="recInv.codBibO" size="5" maxlength="3"></html:text>
				</td>
				<td>
					<html:text styleId="testo"
							disabled="${modificaInvCollForm.disable}"
					property="recInv.codTipoOrd" size="5" maxlength="1"></html:text>
				</td>
				<td>
					<html:text styleId="testo"
							disabled="${modificaInvCollForm.disable}"
					property="recInv.annoOrd" size="15" maxlength="9"></html:text>
				</td>
				<td>
					<html:text styleId="testo"
							disabled="${modificaInvCollForm.disable}"
					property="recInv.codOrd" size="15" maxlength="9"></html:text>
				</td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
				<div class="testo"><bean:message key="documentofisico.numFatturaT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td>
				<html:text styleId="testo" property="numFattura"
							disabled="${modificaInvCollForm.disableNumFattura}"
					size="9" maxlength="15"></html:text></td>
				<td>
				<div class="testo"><bean:message key="documentofisico.dataFatturaT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td colspan="2">
				<html:text styleId="testo" property="dataFattura"
							disabled="${modificaInvCollForm.disableDataFattura}"
					size="15" maxlength="12"></html:text></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>
				<div class="testo"><bean:message key="documentofisico.fornitoreT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td colspan="5">
				<div class="testo"><html:text styleId="testo"
							disabled="${modificaInvCollForm.disable}"
					property="descrFornitore" size="50" maxlength="40"></html:text></div>
				</td>
			</tr>
		</table>
		<hr color="#dde8f0" />
	</c:when>
</c:choose>

<table width="100%" border="0">
	<tr>
		<td>
			<div class="testo">
				<bean:message key="documentofisico.motivoCaricoT"
					bundle="documentoFisicoLabels" />
			</div>
		</td>
		<td colspan="5"><html:select property="recInv.codCarico"
				disabled="${modificaInvCollForm.disableCodCarico}">
				<html:optionsCollection property="listaMotivoCarico" value="codice"
					label="descrizione" />
			</html:select></td>
	</tr>
	<tr>
		<td>
			<div class="testo">
				<bean:message key="documentofisico.dataCaricoT"
					bundle="documentoFisicoLabels" />
			</div></td>
		<td><html:text styleId="testo" property="recInv.dataCarico"
				disabled="${modificaInvCollForm.disableDataCarico}" size="15"
				maxlength="12"></html:text>
		</td>
		<td>
			<div class="testo">
				<bean:message key="documentofisico.numBuonoCaricoT"
					bundle="documentoFisicoLabels" />
			</div></td>
		<td><%--<html:select property="recInv.numCarico"
				disabled="${modificaInvCollForm.disableNumCarico}">
				<html:optionsCollection property="listaBuoniCarico" value="codice"
					label="descrizione" />
			</html:select>--%><html:text styleId="testo" property="recInv.numCarico"
				disabled="${modificaInvCollForm.disableNumCarico}" size="15"
				maxlength="9"></html:text>
		</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
</table>
