<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<div style="text-align: center; width: 100%;">
<div>

<c:choose>

	<c:when test="${ConfigurazioneForm.conferma}">
		<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
	</c:when>
	<c:otherwise>
	<sbn:checkAttivita idControllo="GESTIONE">
		<html:submit property="methodConfigurazione" titleKey="servizi.configurazione.supporti.inserisce" bundle="serviziLabels">
			<bean:message key="servizi.bottone.nuovo" bundle="serviziLabels" />
		</html:submit>&nbsp;&nbsp;
	</sbn:checkAttivita>
		<c:choose>
			<c:when test="${not empty ConfigurazioneForm.supportiBiblioteca}">
				<html:submit property="methodConfigurazione" titleKey="servizi.configurazione.supporti.esamina" bundle="serviziLabels">
					<bean:message key="servizi.bottone.esamina" bundle="serviziLabels" />
				</html:submit>
				<sbn:checkAttivita idControllo="GESTIONE">
					&nbsp;&nbsp;
					<html:submit property="methodConfigurazione" titleKey="servizi.configurazione.supporti.cancella" bundle="serviziLabels">
					<bean:message key="servizi.bottone.cancella" bundle="serviziLabels" />
					</html:submit>
				</sbn:checkAttivita>
			</c:when>
		</c:choose>
	</c:otherwise>
</c:choose>
</div>
</div>