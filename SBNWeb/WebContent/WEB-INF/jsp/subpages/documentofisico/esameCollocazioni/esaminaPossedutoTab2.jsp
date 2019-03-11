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
		<div align="center"><bean:message key="documentofisico.sezione"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div align="center"><bean:message key="documentofisico.collocazione"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div align="center"><bean:message key="documentofisico.specificazione"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div align="center"><bean:message key="documentofisico.invColl"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th>
		<div align="center"><bean:message key="documentofisico.consistenza"
			bundle="documentoFisicoLabels" /></div>
		</th>
		<th title="La collocazione è legata ad esemplare">
		<div align="center" class="etichetta">E</div>
		</th>
		<th width="3%">
		<div align="center"></div>
		</th>
	</tr>
	<logic:notEmpty property="listaCollocazioniDelTitolo"
		name="esameCollocEsaminaPossedutoForm">
		<logic:iterate id="item" property="listaCollocazioniDelTitolo" indexId="riga"
			name="esameCollocEsaminaPossedutoForm">
			<sbn:rowcolor var="color" index="riga" />
			<tr bgcolor="#FFCC99">
				<sbn:anchor name="item" property="prg" />
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><sbn:linkbutton name="item"
					property="selectedColl" index="riga" value="prg" title="Esamina Esemplare"
					key="documentofisico.bottone.esEsempl" bundle="documentoFisicoLabels" /></div>
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
				<td bgcolor="${color}" class="testoNormale"><bean-struts:write
					name="item" property="flgEsemplare" /></td>
				<td bgcolor="${color}">
				<div align="center" class="testoNormale"><html:radio
					property="selectedColl" value="${riga}" /></div>
				</td>
			</tr>
		</logic:iterate>
	</logic:notEmpty>
</table>
