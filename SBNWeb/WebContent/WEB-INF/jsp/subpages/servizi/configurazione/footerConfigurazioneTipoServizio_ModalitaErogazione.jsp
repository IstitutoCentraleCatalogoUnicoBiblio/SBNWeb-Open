<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<div style="text-align:center; width:100%;">
	<div>
		<c:choose>

			<c:when test="${ConfigurazioneTipoServizioForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
			</c:when>

			<c:otherwise>


	       		<c:choose>
	       			<c:when test="${ConfigurazioneTipoServizioForm.stringaMessaggioModalitaUltSupp eq '' and
	       			                ConfigurazioneTipoServizioForm.stringaMessaggioNoModalita eq ''}">
						<sbn:checkAttivita idControllo="GESTIONE">
						<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.modalitaErogazione.inserisce"
					 	bundle="serviziLabels">
						<bean:message key="servizi.bottone.nuova"    bundle="serviziLabels" />
						</html:submit>
						&nbsp;&nbsp;
						</sbn:checkAttivita>

					</c:when>

	       		</c:choose>

				<c:choose>
					<c:when test="${not empty ConfigurazioneTipoServizioForm.lstTariffeModalitaErogazione and
									ConfigurazioneTipoServizioForm.stringaMessaggioModalitaUltSupp eq '' and
									ConfigurazioneTipoServizioForm.stringaMessaggioNoModalita eq ''}">
						<sbn:checkAttivita idControllo="GESTIONE">
						<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.modalitaErogazione.cancella"
						 bundle="serviziLabels">
							<bean:message key="servizi.bottone.cancella"  bundle="serviziLabels" />
						</html:submit>
						&nbsp;&nbsp;
						</sbn:checkAttivita>
					</c:when>
				</c:choose>

			<sbn:checkAttivita idControllo="GESTIONE">
				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.salva"
				 bundle="serviziLabels">
					<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
				</html:submit>
				&nbsp;&nbsp;
			</sbn:checkAttivita>

				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.chiude"
				 bundle="serviziLabels">
					<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
				</html:submit>

			</c:otherwise>
		</c:choose>



	</div>
</div>
