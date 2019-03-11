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
	<tr bgcolor="#dde8f0">
		<th><bean:message key="documentofisico.prg" bundle="documentoFisicoLabels" />
		</th>
		<th class="etichetta" colspan="2">
		<div align="center"><bean:message key="documentofisico.titoloColl"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th class="etichetta" width="6%">
		<div align="center"><bean:message key="documentofisico.sezione"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th class="etichetta" width="9%">
		<div align="center"><bean:message key="documentofisico.collocazione"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th class="etichetta" width="10%">
		<div align="center"><bean:message key="documentofisico.specificazione"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th class="etichetta" width="14%">
		<div align="center"><bean:message key="documentofisico.invColl"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th class="etichetta" width="9%">
		<div align="center"><bean:message key="documentofisico.consistenza"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th class="etichetta" width="7%">
		<div align="center"></div>
		</th>
	</tr>
	<logic:notEmpty property="listaCollReticolo" name="vaiAInserimentoCollForm">
		<logic:iterate id="item" property="listaCollReticolo" indexId="idxsez"
			name="vaiAInserimentoCollForm">
			<sbn:rowcolor var="color" index="idxsez" />
			<tr bgcolor="#FFCC99">
				<td bgcolor="${color}"><sbn:anchor name="item" property="prg" />
					<bean-struts:write name="item" property="prg" /></td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write
					name="item" property="bid" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write
					name="item" property="descrBid" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write
					name="item" property="codSez" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write
					name="item" property="codColloc" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write
					name="item" property="specColloc" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write
					name="item" property="totInv" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write
					name="item" property="consistenza" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><html:radio
					property="selectedColl" value="${idxsez}" /></div>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
</table>
