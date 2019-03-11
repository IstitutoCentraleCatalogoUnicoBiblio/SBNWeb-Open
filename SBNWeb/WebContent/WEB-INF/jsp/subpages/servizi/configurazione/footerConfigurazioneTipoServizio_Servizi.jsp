<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<div style="text-align:center; width:100%;">
	<div>


		<c:choose>

			<c:when test="${navForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
			</c:when>

			<c:otherwise>
				<sbn:checkAttivita idControllo="GESTIONE">
				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.inserisceDiritto" bundle="serviziLabels">
					<bean:message key="servizi.bottone.nuovo"    bundle="serviziLabels" />
				</html:submit>
				&nbsp;&nbsp;
				</sbn:checkAttivita>

				<c:choose>
					<c:when test="${not empty navForm.lstServizi}">
						<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.esaminaDiritto" bundle="serviziLabels">
							<bean:message key="servizi.bottone.esamina"  bundle="serviziLabels" />
						</html:submit>
						&nbsp;&nbsp;

						<sbn:checkAttivita idControllo="GESTIONE">
							<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.cancellaDiritto" bundle="serviziLabels">
								<bean:message key="servizi.bottone.cancella"  bundle="serviziLabels" />
							</html:submit>
							&nbsp;&nbsp;
						</sbn:checkAttivita>

					</c:when>
				</c:choose>

			<sbn:checkAttivita idControllo="GESTIONE">
				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.salva" bundle="serviziLabels">
					<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
				</html:submit>
				&nbsp;&nbsp;
			</sbn:checkAttivita>

				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.chiude" bundle="serviziLabels">
					<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
				</html:submit>

			</c:otherwise>
		</c:choose>



	</div>
</div>
