<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<table align="center">
	<tr>
		<!-- BOTTONIERA SOLO td -->

		<td align="center"><html:submit property="methodCancellaLegameTitCla">
			<bean:message key="button.elimina" bundle="gestioneSemanticaLabels" />
		</html:submit></td>

		<td align="center"><html:submit property="methodCancellaLegameTitCla">
			<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
		</html:submit></td>
	</tr>
</table>

