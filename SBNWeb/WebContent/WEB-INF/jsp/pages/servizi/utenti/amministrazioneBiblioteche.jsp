<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/utenti/AmministrazioneBiblioteche.do">

		<html:submit property="methodAmministrazioneBiblioteche">
			<bean:message key="servizi.bottone.importaDaBiblioteca" bundle="serviziLabels" />
		</html:submit>

	</sbn:navform>
</layout:page>
