<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"      prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn"     prefix="sbn"%>

<table align="center">
	<tr>
		<td align="left">
			<html:submit property="methodErogazione" titleKey="servizi.erogazione.aggiornaPrenotazioni" bundle="serviziLabels">
				<bean:message key="servizi.bottone.aggiorna" bundle="serviziLabels" />
			</html:submit>
			<sbn:checkAttivita idControllo="PRENOTAZIONI">
				<html:submit property="methodErogazione" titleKey="servizi.erogazione.esaminaPrenotazione" bundle="serviziLabels">
					<bean:message key="servizi.bottone.esamina" bundle="serviziLabels" />
				</html:submit>
				<html:submit property="methodErogazione" titleKey="servizi.erogazione.servInCorsoPrenotazione" bundle="serviziLabels">
					<bean:message key="servizi.bottone.dettaglioMovimento" bundle="serviziLabels" />
				</html:submit>
				<html:submit property="methodErogazione" titleKey="servizi.erogazione.stampaPrenotazioni" bundle="serviziLabels">
					<bean:message key="servizi.bottone.stampa" bundle="serviziLabels" />
				</html:submit>
				<%--<html:submit property="methodErogazione" titleKey="servizi.erogazione.respingiPrenotazioni" bundle="serviziLabels">
					<bean:message key="servizi.bottone.respingi" bundle="serviziLabels" />
				</html:submit>--%>
				<html:submit property="methodErogazione" titleKey="servizi.erogazione.ordinaPrenotazioni" bundle="serviziLabels">
					<bean:message key="servizi.bottone.ordina" bundle="serviziLabels" />
				</html:submit>
			</sbn:checkAttivita>
		</td>
	</tr>
</table>

