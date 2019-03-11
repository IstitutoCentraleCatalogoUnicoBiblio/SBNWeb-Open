<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<table align="center">
	<tr>
		<td align="center">
			<html:submit property="methodListaAnagAut">
				<bean:message key="servizi.bottone.esamina" bundle="serviziLabels" />
			</html:submit>

			<sbn:checkAttivita idControllo="GESTIONE">
				<html:submit property="methodListaAnagAut">
					<bean:message key="servizi.bottone.nuovo" bundle="serviziLabels" />
				</html:submit>
				<html:submit property="methodListaAnagAut">
					<bean:message key="servizi.bottone.cancella" bundle="serviziLabels" />
				</html:submit>
			</sbn:checkAttivita>
			<html:submit property="methodListaAnagAut">
				<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
			</html:submit>
		</td>
		<td>
			<html:submit property="methodListaAnagAut" styleClass="buttonSelezTutti" titleKey="servizi.title.selezionaTutti" bundle="serviziLabels">
				<bean:message key="servizi.bottone.selTutti" bundle="serviziLabels" />
			</html:submit>
			<html:submit property="methodListaAnagAut" styleClass="buttonSelezNessuno" titleKey="servizi.title.selezionaNessuno" bundle="serviziLabels">
				<bean:message key="servizi.bottone.deselTutti" bundle="serviziLabels" />
			</html:submit>
		</td><!--



		<td>
			<div align="right">
				<html:submit property="methodListaAnagAut">
					<bean:message key="servizi.bottone.selTutti" bundle="serviziLabels" />
				</html:submit>
				<html:submit property="methodListaAnagAut">
					<bean:message key="servizi.bottone.deselTutti" bundle="serviziLabels" />
				</html:submit>
			</div>

		</td>
	--></tr>
</table>

