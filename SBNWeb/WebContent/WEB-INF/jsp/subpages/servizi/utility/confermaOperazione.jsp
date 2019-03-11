<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<table align="center">
	<tr>
		<html:messages id="msg1" message="true"
			property="servizi.parameter.conferma"
			bundle="serviziLabels">
			<td align="center"><html:submit property="${msg1}">
				<bean:message key="servizi.bottone.si" bundle="serviziLabels" />
			</html:submit></td>
			<td align="center"><html:submit property="${msg1}">
				<bean:message key="servizi.bottone.no" bundle="serviziLabels" />
			</html:submit></td>
		</html:messages>
	</tr>
</table>





