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
	<tr>
		<td width="30%"><bean:message
			key="documentofisico.formatoEtichettaT" bundle="documentoFisicoLabels" /></td>
		<td width="70%"><html:text disabled="false" property="codModello"
			size="50" maxlength="30"></html:text><html:submit
			property="methodParBiblio">
			<bean:message key="documentofisico.lsModelli"  bundle="documentoFisicoLabels" />
		</html:submit></td>
	</tr>
	<tr>
		<td><bean:message key="documentofisico.utilizzoSerieT"	bundle="documentoFisicoLabels" /></td>
		<td><html:checkbox property="utilizzoSerie"></html:checkbox><html:hidden
			property="utilizzoSerie" value="false" /></td>
	</tr>
	<tr>
		<td colspan="2"><jsp:include flush="true"
			page="/WEB-INF/jsp/pages/gestionestampe/common/tipoStampa.jsp" /></td>
	</tr>
</table>

