<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>

<div style="padding: 0 0 0 30%">
			<html:submit property="methodErogazione">
				<bean:message key="servizi.bottone.cerca" bundle="serviziLabels" />
			</html:submit>
			<html:submit property="methodErogazione">
				<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
			</html:submit>
</div>