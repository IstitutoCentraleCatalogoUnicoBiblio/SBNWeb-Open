<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<table align="center">
	<tr>
		<c:choose>
		<c:when test="${not empty navForm.chiamante
							  and (navForm.chiamante eq 'DOCUMENTO_FISICO'
							  		or navForm.chiamante eq 'DOCUMENTO_NO_SBN') }">
			<td align="left">
				<html:submit property="methodListaMovimentiUte">
					<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
				</html:submit>
			</td>
		</c:when>

		<c:otherwise>
			<td align="left">
				<sbn:checkAttivita idControllo="NUOVA_RICHIESTA">

					<c:choose>
						<c:when test="${navForm.confermaNuovaRichiesta}">
							<html:submit property="methodListaMovimentiUte" disabled="${navForm.flgErrNuovaRichiesta}">
								<bean:message key="servizi.bottone.nuovarichiesta" bundle="serviziLabels" />
							</html:submit>
						</c:when>
						<c:otherwise>
							<html:submit property="methodListaMovimentiUte" disabled="${navForm.flgErrNuovaRichiesta}">
								<bean:message key="servizi.bottone.avanti" bundle="serviziLabels" />
							</html:submit>
						</c:otherwise>
					</c:choose>

				</sbn:checkAttivita>
				<c:choose>
					<c:when test="${not empty navForm.listaMovRicUte}">

						<!-- cambio del tooltip del tasto Aggiorna -->
						<c:choose>
							<c:when test="${navForm.tipoRicerca ne 'RICERCA_LISTE'}">
								<html:submit property="methodListaMovimentiUte" titleKey="servizi.erogazione.aggiornaMovimentiNuovaRichiesta" bundle="serviziLabels">
									<bean:message key="servizi.bottone.aggiorna" bundle="serviziLabels" />
								</html:submit>
								<!-- ROX 14.04.10  TCK 3676 -->
								<html:submit property="methodListaMovimentiUte" titleKey="servizi.erogazione.servInCorsoPrenotazione" bundle="serviziLabels">
									<bean:message key="servizi.bottone.dettaglioMovimento" bundle="serviziLabels" />
								</html:submit>
							</c:when>
							<c:otherwise>
								<html:submit property="methodListaMovimentiUte" titleKey="servizi.erogazione.aggiornaMovimenti" bundle="serviziLabels">
									<bean:message key="servizi.bottone.aggiorna" bundle="serviziLabels" />
								</html:submit>
							</c:otherwise>
						</c:choose>

						<html:submit property="methodListaMovimentiUte" titleKey="servizi.erogazione.esaminaMovimenti" bundle="serviziLabels">
							<bean:message key="servizi.bottone.esamina" bundle="serviziLabels" />
						</html:submit>
						<%--
							<html:submit property="methodListaMovimentiUte" titleKey="servizi.erogazione.stampaMovimenti" bundle="serviziLabels">
								<bean:message key="k" bundle="serviziLabels" />
							</html:submit>

							<html:submit property="methodListaMovimentiUte" titleKey="servizi.erogazione.cancellaMovimenti" bundle="serviziLabels">
								<bean:message key="servizi.bottone.cancella" bundle="serviziLabels" />
							</html:submit>
							<html:submit property="methodListaMovimentiUte" titleKey="servizi.erogazione.rifiutaMovimenti" bundle="serviziLabels">
								<bean:message key="servizi.bottone.rifiuta" bundle="serviziLabels" />
							</html:submit>
						--%>
					</c:when>
				</c:choose>
				<html:submit property="methodListaMovimentiUte" titleKey="servizi.erogazione.chiudiMovimenti" bundle="serviziLabels">
					<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
				</html:submit>
			</td>

			<c:choose>
				<c:when test="${not empty navForm.listaMovRicUte}">
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td align="right">
						<html:submit property="methodListaMovimentiUte"
							styleClass="buttonSelezTutti" titleKey="servizi.title.selezionaTutti" bundle="serviziLabels"
							disabled="${navForm.conferma}">
							<bean:message key="servizi.bottone.selTutti" bundle="serviziLabels" />
						</html:submit>
						<html:submit property="methodListaMovimentiUte"
							styleClass="buttonSelezNessuno" titleKey="servizi.title.selezionaNessuno" bundle="serviziLabels"
							disabled="${navForm.conferma}">
							<bean:message key="servizi.bottone.deselTutti" bundle="serviziLabels" />
						</html:submit>
					</td>
				</c:when>
			</c:choose>

		</c:otherwise>
		</c:choose>
	</tr>
</table>
