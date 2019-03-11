	<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>



	<table align="center" border="0" style="height:40px">
			<tr>

				<td width="20%">
				<html:submit styleClass="pulsanti" disabled="true"
					property="methodStampaEtichette"
					>
					<bean:message key="button.caricaModello" bundle="gestioneStampeLabels" />
				</html:submit>
				</td>
				<td width="20%">
				<html:submit styleClass="pulsanti"
					property="methodStampaEtichette"
					>
					<bean:message key="button.salvaModello" bundle="gestioneStampeLabels" />
				</html:submit>
				</td>
				<td width="20%">
				</td>
				<td width="20%">
				<html:submit styleClass="pulsanti"
					property="methodStampaEtichette"
					>
					<bean:message key="button.modificaModello" bundle="gestioneStampeLabels" />
				</html:submit>
				</td>

				<td width="20%">
				</td>
				<td width="20%">
				<html:submit styleClass="pulsanti"
					property="methodStampaEtichette">
					<bean:message key="button.accetta" bundle="gestioneStampeLabels" />
				</html:submit>
				</td>
			</tr>
		</table>