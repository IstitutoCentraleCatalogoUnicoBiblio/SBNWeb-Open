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
				<c:when test="${navForm.detMov.prenotazione}">
					<sbn:checkAttivita idControllo="INOLTRO_PRENOTAZIONE">
						<html:submit property="methodDettaglioMovimentiUte"
						     titleKey="servizi.erogazione.prenotazione.inoltro" bundle="serviziLabels">
							<bean:message key="servizi.bottone.inoltro" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="GESTIONE">
					<html:submit property="methodDettaglioMovimentiUte"
					     titleKey="servizi.erogazione.prenotazione.ok" bundle="serviziLabels">
						<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
					</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="RIFIUTA">
					<html:submit property="methodDettaglioMovimentiUte"
					     titleKey="servizi.erogazione.prenotazione.respingi" bundle="serviziLabels">
						<bean:message key="servizi.bottone.respingi" bundle="serviziLabels" />
					</html:submit>
					</sbn:checkAttivita>

					<html:submit property="methodDettaglioMovimentiUte"
				         titleKey="servizi.erogazione.prenotazione.stampa" bundle="serviziLabels">
						<bean:message key="servizi.bottone.stampa" bundle="serviziLabels" />
					</html:submit>

					<html:submit property="methodDettaglioMovimentiUte"
					     titleKey="servizi.erogazione.prenotazione.annulla" bundle="serviziLabels">
						<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
					</html:submit>
				</c:when>

				<c:when test="${navForm.cambioServizio}">

					<c:choose>
						<c:when test="${not navForm.erroreBloccante}">
							<c:choose>
								<c:when test="${navForm.confermaNuovaRichiesta}">
									<html:submit property="methodDettaglioMovimentiUte" disabled="${navForm.flgErrNuovaRichiesta}">
										<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
									</html:submit>
								</c:when>
								<c:otherwise>
									<html:submit property="methodDettaglioMovimentiUte" disabled="${navForm.flgErrNuovaRichiesta}">
										<bean:message key="servizi.bottone.avanti" bundle="serviziLabels" />
									</html:submit>
								</c:otherwise>
							</c:choose>
						</c:when>

					</c:choose>

					<html:submit property="methodDettaglioMovimentiUte">
						<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
					</html:submit>
				</c:when>

				<c:otherwise>
					<sbn:checkAttivita idControllo="OK">
						<html:submit property="methodDettaglioMovimentiUte"  disabled="${navForm.flgErrNuovaRichiesta}"
					     titleKey="servizi.erogazione.dettaglio.movimento.ok" bundle="serviziLabels">
							<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="CANCELLA">
						<html:submit property="methodDettaglioMovimentiUte"
					     titleKey="servizi.erogazione.dettaglio.movimento.cancella" bundle="serviziLabels">
							<bean:message key="servizi.bottone.cancella" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="RIFIUTA">
						<html:submit property="methodDettaglioMovimentiUte"
					     titleKey="servizi.erogazione.dettaglio.movimento.respingi" bundle="serviziLabels">
							<bean:message key="servizi.bottone.respingi" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="CHIEDI_ANNULLAMENTO">
					<html:submit property="${navButtons}"
					     titleKey="servizi.bottone.ill.proponi.annullamento" bundle="serviziLabels">
						<bean:message key="servizi.bottone.ill.proponi.annullamento" bundle="serviziLabels" />
					</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="RINNOVA">
						<html:submit property="methodDettaglioMovimentiUte"
					     titleKey="servizi.erogazione.dettaglio.movimento.rinnova" bundle="serviziLabels">
							<bean:message key="servizi.bottone.rinnova" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="RIFIUTA_PROROGA">
						<html:submit property="methodDettaglioMovimentiUte"
					     titleKey="servizi.erogazione.dettaglio.movimento.respingi" bundle="serviziLabels">
							<bean:message key="servizi.bottone.rifiuta.proroga" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="AVANZA">
						<html:submit property="methodDettaglioMovimentiUte"
					     titleKey="servizi.erogazione.dettaglio.movimento.avanza" bundle="serviziLabels">
							<bean:message key="servizi.bottone.avanza" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="PRENOTAZIONI">
					<html:submit property="methodDettaglioMovimentiUte"
					     titleKey="servizi.erogazione.dettaglio.movimento.prenotazioni" bundle="serviziLabels">
						<bean:message key="servizi.bottone.prenotazioni" bundle="serviziLabels" />
					</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="CAMBIO_SERVIZIO">
						<html:submit property="methodDettaglioMovimentiUte"
					     titleKey="servizi.erogazione.dettaglio.movimento.cambioServizio" bundle="serviziLabels">
							<bean:message key="servizi.bottone.cambiaServ" bundle="serviziLabels" />
						</html:submit>
					</sbn:checkAttivita>
					<sbn:checkAttivita idControllo="RICHIESTA_ILL">
						<html:submit property="methodDettaglioMovimentiUte"
					     titleKey="servizi.erogazione.dettaglio.movimento.datiILL" bundle="serviziLabels">
							<bean:message key="servizi.bottone.mov.dati.ill" bundle="serviziLabels" />
						</html:submit>
						<sbn:checkAttivita idControllo="INOLTRO_ALTRA_BIB_FORNITRICE">
							<html:submit property="methodDettaglioMovimentiUte"
						     titleKey="servizi.bottone.mov.ill.inoltro.bib.forn" bundle="serviziLabels">
								<bean:message key="servizi.bottone.mov.ill.inoltro.bib.forn" bundle="serviziLabels" />
							</html:submit>
						</sbn:checkAttivita>
					</sbn:checkAttivita>
					<html:submit property="methodDettaglioMovimentiUte"
				     titleKey="servizi.erogazione.dettaglio.movimento.stampa" bundle="serviziLabels">
						<bean:message key="servizi.bottone.stampa" bundle="serviziLabels" />
					</html:submit>
					<html:submit property="methodDettaglioMovimentiUte"
					     titleKey="servizi.erogazione.dettaglio.movimento.annulla" bundle="serviziLabels">
						<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
					</html:submit>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>