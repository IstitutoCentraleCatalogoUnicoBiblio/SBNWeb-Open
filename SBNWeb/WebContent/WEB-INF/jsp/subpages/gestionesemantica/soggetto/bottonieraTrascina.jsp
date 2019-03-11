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
		<td align="center"><html:submit property="methodTraTit">
			<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
		</html:submit></td>
		<td align="center"><html:submit property="methodTraTit">
			<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
		</html:submit></td>
		<td align="center"><html:submit property="methodTraTit">
			<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
		</html:submit></td>
		<td align="center"><html:submit property="methodTraTit" styleClass="buttonSelezTutti">
			<bean:message key="button.selTutti" bundle="gestioneSemanticaLabels" />
		</html:submit></td>
		<td align="center"><html:submit property="methodTraTit" styleClass="buttonSelezNessuno">
			<bean:message key="button.deselTutti"
				bundle="gestioneSemanticaLabels" />
		</html:submit></td>
	</tr>
</table>





