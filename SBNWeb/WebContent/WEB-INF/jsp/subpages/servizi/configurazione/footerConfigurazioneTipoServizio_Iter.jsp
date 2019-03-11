<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<div style="text-align: center; width: 100%;">
<div><c:choose>

	<c:when test="${navForm.conferma}">
		<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
	</c:when>

    <c:when test="${navForm.stringaMessaggioNoIter eq ''}">
		<c:choose>

			<c:when test="${not empty navForm.lstIter}">
				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.esaminaAttivita" bundle="serviziLabels">
					<bean:message key="servizi.bottone.esamina" bundle="serviziLabels" />
				</html:submit>
					&nbsp;&nbsp;
				<sbn:checkAttivita idControllo="GESTIONE">
				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.inserisceAttivita" bundle="serviziLabels">
					<bean:message key="servizi.bottone.inserisci" bundle="serviziLabels" />
				</html:submit>
					&nbsp;&nbsp;
				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.aggiungeAttivita" bundle="serviziLabels">
					<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels" />
				</html:submit>
					&nbsp;&nbsp;
				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.cancellaAttivita" bundle="serviziLabels">
					<bean:message key="servizi.bottone.cancella" bundle="serviziLabels" />
				</html:submit>
					&nbsp;&nbsp;
				<html:submit property="methodConfigurazioneTipoServizio"
					styleClass="buttonFrecciaSu" titleKey="servizi.bottone.frecciaSu"
					bundle="serviziLabels">
					<bean:message key="servizi.bottone.frecciaSu" bundle="serviziLabels" />
				</html:submit>
					&nbsp;&nbsp;
					<html:submit property="methodConfigurazioneTipoServizio"
					styleClass="buttonFrecciaGiu" titleKey="servizi.bottone.frecciaGiu"
					bundle="serviziLabels">
					<bean:message key="servizi.bottone.frecciaGiu" bundle="serviziLabels" />
				</html:submit>
					&nbsp;&nbsp;
				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.salva" bundle="serviziLabels">
					<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
				</html:submit>
					&nbsp;&nbsp;
				</sbn:checkAttivita>
				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.indietro" bundle="serviziLabels">
					<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
				</html:submit>
			</c:when>
			<c:otherwise>
				<sbn:checkAttivita idControllo="GESTIONE">
				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.aggiungePrimaAttivita" bundle="serviziLabels">
					<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels" />
				</html:submit>
					&nbsp;&nbsp;
				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.salva" bundle="serviziLabels">
					<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
				</html:submit>
					&nbsp;&nbsp;
				</sbn:checkAttivita>
				<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.indietro" bundle="serviziLabels">
					<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
				</html:submit>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<sbn:checkAttivita idControllo="GESTIONE">
		<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.salva" bundle="serviziLabels">
			<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
		</html:submit>
			&nbsp;&nbsp;
		</sbn:checkAttivita>
		<html:submit property="methodConfigurazioneTipoServizio" titleKey="servizi.configurazione.servizio.indietro" bundle="serviziLabels">
			<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
		</html:submit>
	</c:otherwise>

</c:choose></div>

</div>