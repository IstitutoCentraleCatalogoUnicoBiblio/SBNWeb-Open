<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<table align="center">
	<tr>
		<td align="center">
		<c:choose>
			<c:when test="${ListaUtentiForm.totRighe gt 0}">
					<html:submit property="methodLista">
						<bean:message key="servizi.bottone.esamina" bundle="serviziLabels" />
					</html:submit>
					<sbn:checkAttivita idControllo="GESTIONE">
						<html:submit property="methodLista">
							<bean:message key="servizi.bottone.nuovo" bundle="serviziLabels" />
						</html:submit>
						<html:submit property="methodLista">
							<bean:message key="servizi.bottone.cancella" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="DIRITTI">
						<html:submit property="methodLista">
							<bean:message key="servizi.bottone.rinnovaAut" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<html:submit property="methodLista">
						<bean:message key="servizi.bottone.stampa" bundle="serviziLabels" />
					</html:submit>
				<c:choose>
					<c:when test="${ListaUtentiForm.abilitaCercaInPolo}">
						<html:submit styleClass="pulsanti" property="methodLista">
							<bean:message key="servizi.bottone.cercaInPolo" bundle="serviziLabels" />
						</html:submit>
					</c:when>
				</c:choose>

					<html:submit property="methodLista" styleClass="buttonSelezTutti" titleKey="servizi.title.selezionaTutti" bundle="serviziLabels">
						<bean:message key="servizi.bottone.selTutti" bundle="serviziLabels" />
					</html:submit>
					<html:submit property="methodLista" styleClass="buttonSelezNessuno" titleKey="servizi.title.selezionaNessuno" bundle="serviziLabels">
						<bean:message key="servizi.bottone.deselTutti" bundle="serviziLabels" />
					</html:submit>
					<!--
					<html:submit property="methodLista">
						<bean:message key="servizi.bottone.aggiornaChiaveUtente" bundle="serviziLabels" />
					</html:submit>
					-->
			</c:when>
		</c:choose>
		<html:submit property="methodLista">
			<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
		</html:submit>

		</td>
	</tr>
</table>