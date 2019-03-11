<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<div style="text-align:center; width:100%;">
	<div>


		<c:choose>

			<c:when test="${ConfigurazioneForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
			</c:when>

			<c:otherwise>
				<sbn:checkAttivita idControllo="GESTIONE">
				<html:submit property="methodConfigurazione" titleKey="servizi.configurazione.modalitaErogazione.inserisce" bundle="serviziLabels">
					<bean:message key="servizi.bottone.nuova"    bundle="serviziLabels" />
				</html:submit>
				</sbn:checkAttivita>
				&nbsp;&nbsp;

				<c:choose>
					<c:when test="${not empty ConfigurazioneForm.lstTariffeModalitaErogazione}">
						<html:submit property="methodConfigurazione" titleKey="servizi.configurazione.modalitaErogazione.esamina" bundle="serviziLabels">
							<bean:message key="servizi.bottone.esamina"  bundle="serviziLabels" />
						</html:submit>
						<sbn:checkAttivita idControllo="GESTIONE">
						&nbsp;&nbsp;
						<html:submit property="methodConfigurazione" titleKey="servizi.configurazione.modalitaErogazione.cancella" bundle="serviziLabels">
							<bean:message key="servizi.bottone.cancella"  bundle="serviziLabels" />
						</html:submit>
						&nbsp;&nbsp;
						</sbn:checkAttivita>
					</c:when>
				</c:choose>

			</c:otherwise>
		</c:choose>



	</div>
</div>
