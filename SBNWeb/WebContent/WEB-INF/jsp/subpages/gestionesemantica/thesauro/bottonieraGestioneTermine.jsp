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
		<td align="center">
			<html:submit property="methodGestioneThes">
				<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
			</html:submit>
		</td>

		<sbn:checkAttivita idControllo="TRASCINA">
			<c:if test="${GestioneTermineForm.enableTrascina}">
				<td align="center">
					<html:submit property="methodGestioneThes">
						<bean:message key="button.trascina"
							bundle="gestioneSemanticaLabels" />
					</html:submit>
				</td>
			</c:if>
		</sbn:checkAttivita>

		<c:if test="${GestioneTermineForm.modalita eq 'GESTIONE'}">
			<sbn:checkAttivita idControllo="CANCELLA">
				<td align="center">
					<html:submit property="methodGestDes">
						<bean:message key="button.elimina"
							bundle="gestioneSemanticaLabels" />
					</html:submit>
				</td>
			</sbn:checkAttivita>
		</c:if>

		<td align="center">
			<html:submit property="methodGestioneThes">
				<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
			</html:submit>
		</td>
		<td align="center">
			<html:submit property="methodGestioneThes">
				<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
			</html:submit>
		</td>
	</tr>
</table>





