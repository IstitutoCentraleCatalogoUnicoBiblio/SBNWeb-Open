<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"      prefix="c"%>

<div style="text-align:center;">

		<html:submit property="methodGestioneRelazioni" >
			<bean:message key="servizi.bottone.nuovo" bundle="serviziLabels" />
		</html:submit>
		<html:submit property="methodGestioneRelazioni" >
			<bean:message key="servizi.bottone.cancella" bundle="serviziLabels" />
		</html:submit>
		<html:submit property="methodGestioneRelazioni" >
			<bean:message key="servizi.bottone.riattiva" bundle="serviziLabels" />
		</html:submit>
		<html:submit property="methodGestioneRelazioni" >
			<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
		</html:submit>

</div>
