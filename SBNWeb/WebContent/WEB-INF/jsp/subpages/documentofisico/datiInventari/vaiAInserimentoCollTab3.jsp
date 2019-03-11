<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<br>
<table width="100%" border="0">
	<tr>
		<td width="20%" class="etichettaIntestazione">Titoli presenti</td>
		<td width="12%">&nbsp;</td>
		<td width="15%">&nbsp;</td>
		<td width="19%">&nbsp;</td>
		<td width="3%">&nbsp;</td>
		<td width="31%">&nbsp;</td>
	</tr>
</table>
<table width="100%" border="0">
	<tr bgcolor="#dde8f0">
		<th><bean:message key="documentofisico.prg" bundle="documentoFisicoLabels" />
		</th>
		<th colspan="2" class="etichetta">
		<div align="center"><bean:message key="documentofisico.titoloColl"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th width="9%" class="etichetta">
		<div align="center"><bean:message key="documentofisico.natura"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th width="3%" class="etichetta">
		<div align="center"></div>
		</th>
	</tr>
	<logic:iterate id="item" property="collLiv" indexId="idxbid"
		name="vaiAInserimentoCollForm">
		<sbn:rowcolor var="color" index="idxbid" />
		<tr bgcolor="#FFCC99">
			<sbn:anchor name="item" property="prg" />
			<td bgcolor="${color}"><bean-struts:write name="item" property="prg" />
			</td>
			<td bgcolor="${color}" width="8%" class="testoNormale">
			<div align="center"><bean-struts:write name="item" property="bid" /></div>
			</td>
			<td bgcolor="${color}" width="80% class="testoNormale"">
			<div align="center"><bean-struts:write name="item" property="isbd" /></div>
			</td>
			<td bgcolor="${color}" class="testoNormale">
			<div align="center"><bean-struts:write name="item" property="natura" /></div>
			</td>
			<td bgcolor="${color}" class="testoNormale">
			<div align="center"><html:radio property="selectedTit"
				value="${idxbid}" /></div>
			</td>
		</tr>
	</logic:iterate>
</table>
