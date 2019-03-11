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
		<c:if test="${navForm.uteRic.parametro ne 'E'}">
			<sbn:checkAttivita idControllo="SCEGLI">
				<html:submit property="methodDettaglio">
					<bean:message key="servizi.bottone.scegli" bundle="serviziLabels" />
				</html:submit>
			</sbn:checkAttivita>
			<c:choose>
				<c:when test="${navForm.folder eq navForm.folderAutorizzazioni}">
					<sbn:checkAttivita idControllo="DIRITTI">
						<html:submit property="methodDettaglio">
							<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
				</c:when>
				<c:otherwise>
				<sbn:checkAttivita idControllo="GESTIONE">
					<html:submit property="methodDettaglio">
						<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
					</html:submit>
				</sbn:checkAttivita>
				</c:otherwise>
			</c:choose>

			<c:if test="${navForm.folder eq navForm.folderAnagrafica}">
				<c:if test="${not navForm.uteAna.nuovoUte}">
					<sbn:checkAttivita idControllo="RESET_PASSWORD">
						<html:submit property="methodDettaglio">
							<bean:message key="servizi.bottone.resetPwd"
								bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="CANCELLA">
						<html:submit property="methodDettaglio">
							<bean:message key="servizi.bottone.cancella"
								bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>

				</c:if>
				<html:submit property="methodDettaglio">
					<bean:message key="servizi.bottone.materie" bundle="serviziLabels" />
				</html:submit>

				<logic:greaterThan name="navForm" property="numUtenti" value="1">
					<html:submit styleClass="pulsanti" property="methodDettaglio">
						<bean:message key="servizi.bottone.scorriIndietro"
							bundle="serviziLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodDettaglio">
						<bean:message key="servizi.bottone.scorriAvanti"
							bundle="serviziLabels" />
					</html:submit>
				</logic:greaterThan>

			</c:if>

			<c:if test="${not navForm.uteAna.nuovoUte}">
				<!-- tck 3916
					<html:submit property="methodDettaglio">
						<bean:message key="servizi.bottone.cancella" bundle="serviziLabels" />
					</html:submit>
				-->
				<c:if test="${not navForm.uteAna.professione.ente}">
					<html:submit property="methodDettaglio">
						<bean:message key="servizi.bottone.tesserino"
							bundle="serviziLabels" />
					</html:submit>
				</c:if>
			</c:if>

			<c:if test="${navForm.folder eq navForm.folderAutorizzazioni}">
				<sbn:checkAttivita idControllo="DIRITTI">
					<html:submit property="methodDettaglio">
						<bean:message key="servizi.bottone.inserisciServizio2"
							bundle="serviziLabels" />
					</html:submit>
					<html:submit property="methodDettaglio">
						<bean:message key="servizi.bottone.cancellaServizio2"
							bundle="serviziLabels" />
					</html:submit>
				</sbn:checkAttivita>
			</c:if>
			<c:choose>
				<c:when
					test="${navForm.tipoUtente eq 'E' and navForm.uteAna.nuovoUte}">
					<sbn:checkAttivita idControllo="GESTIONE">
					<html:submit styleClass="pulsanti" property="methodDettaglio">
						<bean:message key="servizi.bottone.importaBib"
							bundle="serviziLabels" />
					</html:submit>
					</sbn:checkAttivita>
				</c:when>
			</c:choose>
			<!--					<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />-->
		</c:if> <html:submit property="methodDettaglio">
			<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
		</html:submit></td>
	</tr>
</table>
