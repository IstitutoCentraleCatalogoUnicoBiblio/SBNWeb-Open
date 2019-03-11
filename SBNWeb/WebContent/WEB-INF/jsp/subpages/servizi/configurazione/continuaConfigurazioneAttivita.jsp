<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>

<table align="center">
	<tr>
		<html:messages id="msg1" message="true"
			property="servizi.parameter.conferma"
			bundle="serviziLabels">
			<td align="center">
				<html:submit property="${msg1}" titleKey="servizi.configurazione.servizio.bibliotecariDaAutorizzare" bundle="serviziLabels">
					<bean:message key="servizi.bottone.bibliotecari" bundle="serviziLabels" />
				</html:submit>
			</td>
			<td align="center">
				<html:submit property="${msg1}" titleKey="servizi.configurazione.servizio.controlli" bundle="serviziLabels">
					<bean:message key="servizi.bottone.controlloIter" bundle="serviziLabels" />
				</html:submit>
			</td>
			<td align="center">
				<html:submit property="${msg1}" titleKey="servizi.configurazione.servizio.indietro" bundle="serviziLabels">
					<bean:message key="servizi.bottone.chiudi" bundle="serviziLabels" />
				</html:submit>
			</td>
		</html:messages>
	</tr>
</table>