<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<p></p>
<table width="100%" border="0">
	<tr bgcolor="#dde8f0">
		<th>
		<div class="etichetta"><bean:message key="documentofisico.prg"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div align="center"><bean:message key="documentofisico.biblioteca"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div align="center"><bean:message key="documentofisico.codDoc"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div align="center"><bean:message key="documentofisico.consistenza"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th width="3%">
		<div align="center"></div>
		</th>
	</tr>
	<logic:notEmpty property="listaEsemplariDelTitolo"
		name="esameCollocEsaminaPossedutoForm">
		<logic:iterate id="item" property="listaEsemplariDelTitolo" indexId="riga"
			name="esameCollocEsaminaPossedutoForm">
			<sbn:rowcolor var="color" index="riga" />
			<tr bgcolor="#FFCC99">
				<sbn:anchor name="item" property="prg" />
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write
					name="item" property="prg" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write
					name="item" property="codBib" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write
					name="item" property="codDoc" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write
					name="item" property="consDoc" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><html:radio
					property="selectedEsem" value="${riga}" /></div>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
</table>
