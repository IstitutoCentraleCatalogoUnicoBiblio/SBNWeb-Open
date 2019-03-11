<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>

<table align="center">
	<tr>
		<td align="center"><c:choose>
			<c:when test="${!AnaliticaSoggettoForm.enableIndice}">

				<logic:equal name="AnaliticaSoggettoForm" property="enableOk"
					value="true">
					<div style="float:left; display:inline;">
					<html:submit property="methodAnaSog">
						<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
					</html:submit>
					</div>
				</logic:equal>
				<sbn:checkAttivita idControllo="CREA">
					<logic:equal name="AnaliticaSoggettoForm" property="enableCrea"
						value="true">
						<div style="float:left; display:inline;">
						<html:submit property="methodAnaSog">
							<bean:message key="button.crea" bundle="gestioneSemanticaLabels" />
						</html:submit>
						</div>
					</logic:equal>
				</sbn:checkAttivita>

				<sbn:checkAttivita idControllo="JAVASCRIPT_ENABLED">

					<layout:combo bundle="gestioneSemanticaLabels"
						label="button.gestione" name="AnaliticaSoggettoForm"
						button="button.conferma" property="idFunzione"
						combo="comboGestione" parameter="methodAnaSog" />

				</sbn:checkAttivita>
				<sbn:checkAttivita idControllo="JAVASCRIPT_DISABLED">
					<layout:combo bundle="gestioneSemanticaLabels"
						label="button.gestione" name="AnaliticaSoggettoForm"
						button="button.conferma" property="idFunzione"
						combo="comboGestioneNonFiltrata" parameter="methodAnaSog" />
				</sbn:checkAttivita>

				<logic:equal name="AnaliticaSoggettoForm" property="enableStampa"
					value="true">
					<div style="float:left; display:inline;">
					<html:submit property="methodAnaSog">
						<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
					</html:submit>
					</div>
				</logic:equal>

				<div style="float:left; display:inline;">
				<html:submit property="methodAnaSog">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit>
				</div>
				<sbn:checkAttivita idControllo="JAVASCRIPT_ENABLED">
					<layout:combo bundle="gestioneSemanticaLabels"
						label="button.esamina" name="AnaliticaSoggettoForm"
						button="button.esegui" property="idFunzioneEsamina"
						combo="comboGestioneEsamina" parameter="methodAnaSog" />
				</sbn:checkAttivita>

				<sbn:checkAttivita idControllo="JAVASCRIPT_DISABLED">
					<layout:combo bundle="gestioneSemanticaLabels"
						label="button.esamina" name="AnaliticaSoggettoForm"
						button="button.esegui" property="idFunzioneEsamina"
						combo="comboGestioneEsaminaNonFiltrata" parameter="methodAnaSog" />
				</sbn:checkAttivita>

			</c:when>
			<c:otherwise>

				<sbn:checkAttivita idControllo="IMPORTA">
					<div style="float:left; display:inline;">
					<html:submit property="methodAnaSog">
						<bean:message key="button.importa"
							bundle="gestioneSemanticaLabels" />
					</html:submit>
					</div>
				</sbn:checkAttivita>

				<sbn:checkAttivita idControllo="GESTIONE">
					<div style="float:left; display:inline;">
					<html:submit property="methodAnaSog">
						<bean:message key="button.gestione"
							bundle="gestioneSemanticaLabels" />
					</html:submit>
					</div>
				</sbn:checkAttivita>

				<sbn:checkAttivita idControllo="CANCELLA">
					<logic:equal name="AnaliticaSoggettoForm"
						property="enableNumIndice" value="false">
						<div style="float:left; display:inline;">
						<html:submit property="methodAnaSog">
							<bean:message key="button.elimina"
								bundle="gestioneSemanticaLabels" />
						</html:submit>
						</div>
					</logic:equal>
				</sbn:checkAttivita>

				<logic:equal name="AnaliticaSoggettoForm" property="enableStampa"
					value="true">
					<div style="float:left; display:inline;">
					<html:submit property="methodAnaSog">
						<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
					</html:submit>
					</div>
				</logic:equal>

				<div style="float:left; display:inline;">
				<html:submit property="methodAnaSog">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit>
				</div>

				<sbn:checkAttivita idControllo="JAVASCRIPT_ENABLED">
					<layout:combo bundle="gestioneSemanticaLabels"
						label="button.esamina" name="AnaliticaSoggettoForm"
						button="button.esegui" property="idFunzioneEsamina"
						combo="comboGestioneEsamina" parameter="methodAnaSog" />
				</sbn:checkAttivita>
				<sbn:checkAttivita idControllo="JAVASCRIPT_DISABLED">
					<layout:combo bundle="gestioneSemanticaLabels"
						label="button.esamina" name="AnaliticaSoggettoForm"
						button="button.esegui" property="idFunzioneEsamina"
						combo="comboGestioneEsaminaNonFiltrata" parameter="methodAnaSog" />
				</sbn:checkAttivita>

			</c:otherwise>
		</c:choose></td>
	</tr>
</table>
