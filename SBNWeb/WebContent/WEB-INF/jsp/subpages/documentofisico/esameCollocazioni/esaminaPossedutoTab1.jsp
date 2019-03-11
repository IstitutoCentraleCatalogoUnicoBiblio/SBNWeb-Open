<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<p></p>
<table width="100%" border="0">
	<tr class="etichetta" bgcolor="#dde8f0">
		<th>
		<div class="etichetta"><bean:message key="documentofisico.prg"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div class="etichetta"><bean:message key="documentofisico.sezione"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div class="etichetta"><bean:message key="documentofisico.collocazione"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div class="etichetta"><bean:message key="documentofisico.specificazione"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div class="etichetta"><bean:message key="documentofisico.sequenza"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div class="etichetta"><bean:message key="documentofisico.precInv"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div class="etichetta"><bean:message key="documentofisico.serie"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div class="etichetta"><bean:message key="documentofisico.inventario"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th width="3%">
		<div align="center"></div>
		</th>
	</tr>
	<logic:notEmpty property="listaInventariDelTitolo"
		name="esameCollocEsaminaPossedutoForm">
		<logic:iterate id="item" property="listaInventariDelTitolo"
			name="esameCollocEsaminaPossedutoForm" indexId="riga">
			<sbn:rowcolor var="color" index="riga" />
			<tr class="testoNormale">
				<sbn:anchor name="item" property="prg" />
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><sbn:linkbutton name="item"
					property="selectedInv" index="riga" value="prg" title="Esamina Inventario"
					key="documentofisico.bottone.esInv1" bundle="documentoFisicoLabels" /></div>
				</td>
				<!--<bean-struts:write name="item"	property="prg" /></div></td>-->
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write name="item"
					property="codSez" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write name="item"
					property="codLoc" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write name="item"
					property="specLoc" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write name="item"
					property="seqColl" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write name="item"
					property="precInv" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write name="item"
					property="codSerie" /></div>
				</td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><bean-struts:write name="item"
					property="codInvent" /></div>
				</td>
				<td bgcolor="${color}"><html:radio property="selectedInv" value="${riga}" /></td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
</table>
