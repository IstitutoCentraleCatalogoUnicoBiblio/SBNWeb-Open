<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<div style="text-align: center;">
	<div>

		<c:choose>

			<c:when test="${navForm.conferma}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
			</c:when>

			<c:otherwise>
				<sbn:checkAttivita idControllo="GESTIONE">
					<html:submit property="methodConfigurazione" titleKey="servizi.configurazione.salvaParametriBiblioteca" bundle="serviziLabels">
						<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
					</html:submit>
					<sbn:checkAttivita idControllo="MODELLO_SOLLECITO">
						<html:submit property="methodConfigurazione" titleKey="servizi.bottone.modello.sollecito" bundle="serviziLabels">
							<bean:message key="servizi.bottone.modello.sollecito" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<html:submit property="methodConfigurazione" titleKey="servizi.bottone.calendario" bundle="serviziLabels">
						<bean:message key="servizi.bottone.calendario" bundle="serviziLabels" />
					</html:submit>
				</sbn:checkAttivita>
			</c:otherwise>

		</c:choose>

	</div>
</div>
