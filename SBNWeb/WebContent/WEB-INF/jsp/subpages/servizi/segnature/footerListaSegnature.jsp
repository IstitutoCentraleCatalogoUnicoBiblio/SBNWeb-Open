<%@ taglib uri="http://struts.apache.org/tags-html-el"       prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el"       prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"            prefix="c"%>


<table align="center">
	<tr>
		<td align="center">
			<c:if test="${not empty ListaSegnatureForm.listaSegnature}">
				<html:submit property="methodListaSegna">
					<bean:message key="servizi.bottone.esamina" bundle="serviziLabels" />
				</html:submit>
			</c:if>

			<html:submit property="methodListaSegna">
				<bean:message key="servizi.bottone.nuovo" bundle="serviziLabels" />
			</html:submit>

			<c:if test="${not empty ListaSegnatureForm.listaSegnature}">
				<html:submit property="methodListaSegna">
					<bean:message key="servizi.bottone.cancella" bundle="serviziLabels" />
				</html:submit>
			</c:if>

			<html:submit property="methodListaSegna">
				<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
			</html:submit>
			<html:submit property="methodListaSegna" styleClass="buttonSelezTutti" titleKey="servizi.title.selezionaTutti" bundle="serviziLabels">
				<bean:message key="servizi.bottone.selTutti" bundle="serviziLabels" />
			</html:submit>
			<html:submit property="methodListaSegna" styleClass="buttonSelezNessuno" titleKey="servizi.title.selezionaNessuno" bundle="serviziLabels">
				<bean:message key="servizi.bottone.deselTutti" bundle="serviziLabels" />
			</html:submit>

		</td>
	</tr>
</table>


