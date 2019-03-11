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
		<c:choose>
			<c:when test="${!AnaliticaDescrittoreForm.enableIndice}">
				<logic:equal name="AnaliticaDescrittoreForm"
					property="enableCercaIndice" value="true">
					<td align="center"><html:submit property="methodAnaDet">
						<bean:message key="button.cercaIndice"
							bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</logic:equal>

				<bean-struts:size id="comboGestioneSize"
					name="AnaliticaDescrittoreForm" property="comboGestione" />
				<logic:greaterThan name="comboGestioneSize" value="1">
					<td class="etichetta"><bean:message key="button.gestione"
						bundle="gestioneSemanticaLabels" /></td>
					<td><html:select styleClass="testoNormale"
						property="idFunzione">
						<sbn:localOptionsCollection property="comboGestione"
							value="codice" label="descrizione" />
					</html:select> <html:submit property="methodAnaDet">
						<bean:message key="button.conferma"
							bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</logic:greaterThan>

				<td align="center"><html:submit property="methodAnaDet">
					<bean:message key="button.soggetti"
						bundle="gestioneSemanticaLabels" />
				</html:submit></td>

				<logic:equal name="AnaliticaDescrittoreForm" property="enableStampa"
					value="true">
					<td align="center"><html:submit property="methodAnaDet">
						<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</logic:equal>
				<td align="center"><html:submit property="methodAnaDet">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
			</c:when>
			<c:otherwise>
				<td align="center"><html:submit property="methodAnaDet">
					<bean:message key="button.soggetti"
						bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodAnaDet">
					<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodAnaDet">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
			</c:otherwise>
		</c:choose>
	</tr>
</table>





