<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"      prefix="c"%>

<table align="center">
	<tr>
		<c:if test="${not empty ListaPrenotazioniForm.listaPrenotazioni}">
			<td align="left">
				<html:submit property="methodListaPrenotazioni"
					titleKey="servizi.erogazione.esaminaPrenotazione" bundle="serviziLabels">
					<bean:message key="servizi.bottone.esamina" bundle="serviziLabels" />
				</html:submit>
				<!--
				<html:submit property="methodListaPrenotazioni"
					titleKey="servizi.erogazione.cancellaPrenotazione" bundle="serviziLabels">
					<bean:message key="servizi.bottone.cancella" bundle="serviziLabels" />
				</html:submit>
				-->
				<%--<html:submit property="methodListaPrenotazioni"
					titleKey="servizi.erogazione.respingiPrenotazioni" bundle="serviziLabels">
					<bean:message key="servizi.bottone.respingi" bundle="serviziLabels" />
				</html:submit>--%>
				<html:submit property="methodListaPrenotazioni"
					titleKey="servizi.erogazione.ordinaPrenotazioni" bundle="serviziLabels">
					<bean:message key="servizi.bottone.ordina" bundle="serviziLabels" />
				</html:submit>
				<html:submit property="methodListaPrenotazioni"
					titleKey="servizi.erogazione.stampaPrenotazioni" bundle="serviziLabels">
					<bean:message key="servizi.bottone.stampa" bundle="serviziLabels" />
				</html:submit>
				<c:if test="${not empty ListaPrenotazioniForm.chiamante}">
					<html:submit property="methodListaPrenotazioni"
					titleKey="servizi.erogazione.chiudiPrenotazioni" bundle="serviziLabels">
						<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
					</html:submit>
				</c:if>
			</td>
			<%--<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="right">
				<html:submit property="methodListaPrenotazioni"
					styleClass="buttonSelezTutti" titleKey="servizi.title.selezionaTutti" bundle="serviziLabels"
					disabled="${ListaPrenotazioniForm.conferma}">
					<bean:message key="servizi.bottone.selTutti" bundle="serviziLabels" />
				</html:submit>
				<html:submit property="methodListaPrenotazioni"
					styleClass="buttonSelezNessuno" titleKey="servizi.title.selezionaNessuno" bundle="serviziLabels"
					disabled="${ListaPrenotazioniForm.conferma}">
					<bean:message key="servizi.bottone.deselTutti" bundle="serviziLabels" />
				</html:submit>
			</td> --%>
		</c:if>

		<c:if test="${empty ListaPrenotazioniForm.listaPrenotazioni}">
			<c:if test="${not empty ListaPrenotazioniForm.chiamante}">
				<td align="center">
					<html:submit property="methodListaPrenotazioni">
						<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
					</html:submit>
				</td>
			</c:if>
		</c:if>
	</tr>
</table>