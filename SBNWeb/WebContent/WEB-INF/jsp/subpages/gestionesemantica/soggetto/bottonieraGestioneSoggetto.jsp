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
		<logic:equal name="GestioneSoggettoForm" property="enableOk"
			value="true">
			<td align="center"><html:submit property="methodGestSog">
				<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
			</html:submit></td>
		</logic:equal>
		<c:choose>
			<c:when test="${GestioneSoggettoForm.enableFondi}">
				<sbn:checkAttivita idControllo="TRASCINA">
					<td align="center"><html:submit property="methodGestSog">
						<bean:message key="button.fondi" bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</sbn:checkAttivita>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${GestioneSoggettoForm.enableCrea}">
				<sbn:checkAttivita idControllo="CREA">
					<td align="center"><html:submit property="methodGestSog">
						<bean:message key="button.crea" bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</sbn:checkAttivita>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${GestioneSoggettoForm.enableElimina}">
				<sbn:checkAttivita idControllo="CANCELLA">
					<td align="center"><html:submit property="methodGestSog">
						<bean:message key="button.cancella"
							bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</sbn:checkAttivita>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${GestioneSoggettoForm.enableTrascina}">
				<sbn:checkAttivita idControllo="TRASCINA">
					<td align="center"><html:submit property="methodGestSog">
						<bean:message key="button.trascina"
							bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</sbn:checkAttivita>
			</c:when>
		</c:choose>
		<td align="center"><html:submit property="methodGestSog">
			<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
		</html:submit></td>
		<td align="center"><html:submit property="methodGestSog">
			<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
		</html:submit></td>

		<td>
		<layout:combo bundle="gestioneSemanticaLabels"
			label="button.esamina" name="GestioneSoggettoForm"
			button="button.esegui" property="idFunzioneEsamina"
			combo="comboGestioneEsamina" parameter="methodGestSog" /></td>
	</tr>
</table>





