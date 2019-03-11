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
		<th><bean:message key="documentofisico.prg" bundle="documentoFisicoLabels" />
		</th>
		<th><bean:message key="documentofisico.biblioteca"
			bundle="documentoFisicoLabels" /></th>
		<th colspan="3"><bean:message key="documentofisico.documento"
			bundle="documentoFisicoLabels" /></th>
		<th><bean:message key="documentofisico.consistenza"
			bundle="documentoFisicoLabels" /></th>
		<th width="3%"></th>
	</tr>
	<logic:iterate id="item" property="listaEsemplReticolo" indexId="idxbid"
		name="esameCollocRicercaEsemplareForm">
		<sbn:rowcolor var="color" index="idxbid" />
		<tr bgcolor="#FFCC99">
			<sbn:anchor name="item" property="prg" />
			<td bgcolor="${color}"><bean-struts:write name="item" property="prg" />
			</td>
			<td bgcolor="${color}"><bean-struts:write name="item" property="codBib" />
			</td>
			<td bgcolor="${color}"><bean-struts:write name="item" property="bid" />
			</td>
			<td bgcolor="${color}"><bean-struts:write name="item" property="codDoc" />
			</td>
			<td bgcolor="${color}"><bean-struts:write name="item"
				property="bidDescr" /></td>
			<td bgcolor="${color}"><bean-struts:write name="item" property="consDoc" />
			</td>
			<td bgcolor="${color}" width="3%"><html:radio
				property="selectedEsemRetic" value="${idxbid}" /></td>
		</tr>
	</logic:iterate>
</table>
