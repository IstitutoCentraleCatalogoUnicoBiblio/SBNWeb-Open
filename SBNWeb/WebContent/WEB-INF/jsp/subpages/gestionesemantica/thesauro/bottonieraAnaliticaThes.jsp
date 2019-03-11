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
		<logic:equal name="AnaliticaThesauroForm"
			property="enableOk" value="true">

			<html:submit property="methodAnaThes">
				<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
			</html:submit>

		</logic:equal>

		<layout:combo bundle="gestioneSemanticaLabels"
			label="button.gestione" name="AnaliticaThesauroForm"
			button="button.conferma" property="idFunzione" combo="comboGestione"
			parameter="methodAnaThes" />

		<sbn:checkAttivita idControllo="CREA">
			<logic:equal name="AnaliticaThesauroForm" property="enableCrea"
				value="true">

				<html:submit property="methodAnaThes">
					<bean:message key="button.crea" bundle="gestioneSemanticaLabels" />
				</html:submit>

			</logic:equal>
		</sbn:checkAttivita> <logic:equal name="AnaliticaThesauroForm" property="enableStampa"
			value="true">

			<html:submit property="methodAnaThes">
				<bean:message key="button.stampa" bundle="gestioneSemanticaLabels" />
			</html:submit>

		</logic:equal> <html:submit property="methodAnaThes">
			<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />

		</html:submit> <layout:combo bundle="gestioneSemanticaLabels" label="button.esamina"
			name="AnaliticaThesauroForm" button="button.esegui"
			property="idFunzioneEsamina" combo="comboGestioneEsamina"
			parameter="methodAnaThes" /></td>
	</tr>
</table>





