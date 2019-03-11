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
		<th colspan="2"><bean:message key="documentofisico.titoloColl"
			bundle="documentoFisicoLabels" /></th>
		<th><bean:message key="documentofisico.natura"
			bundle="documentoFisicoLabels" /></th>
		<th width="3%"></th>
	</tr>
	<logic:iterate id="item" property="titoliEsempl" indexId="idxbid"
		name="esameCollocRicercaEsemplareForm">
		<sbn:rowcolor var="color" index="idxbid" />
		<tr bgcolor="#FFCC99">
			<sbn:anchor name="item" property="bid" />
			<td bgcolor="${color}"><bean-struts:write name="item" property="bid" />
			</td>
			<td bgcolor="${color}"><bean-struts:write name="item" property="isbd" />
			</td>
			<td bgcolor="${color}"><bean-struts:write name="item" property="natura" />
			</td>
			<td bgcolor="${color}"><html:radio property="selectedTit"
				value="${idxbid}" /></td>
		</tr>
	</logic:iterate>
</table>
