<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<div style="width: 100%"><STRONG><bean:message
	key="servizi.utenti.biblioteca" bundle="serviziLabels" /></STRONG>&nbsp;<html:text
	styleId="testoNoBold" property="biblioteca" size="5" maxlength="3"
	disabled="true"></html:text>&nbsp;<html:submit
	property="${navButtons}">
	<bean:message key="servizi.bottone.cambioBiblioteca"
		bundle="serviziLabels" />
</html:submit></div>

