<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"      prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" 	   prefix="sbn"%>


<c:if test="${not empty ErogazioneRicercaForm.listaSolleciti}">
	<table align="center">
		<tr>
			<td align="left">
				<html:submit property="methodErogazione" titleKey="servizi.erogazione.aggiornaSolleciti" bundle="serviziLabels">
					<bean:message key="servizi.bottone.aggiorna" bundle="serviziLabels" />
				</html:submit>
				<html:submit property="methodErogazione" titleKey="servizi.erogazione.esaminaSollecito" bundle="serviziLabels">
					<bean:message key="servizi.bottone.esamina" bundle="serviziLabels" />
				</html:submit>
				<html:submit property="methodErogazione" titleKey="servizi.erogazione.stampaSolleciti" bundle="serviziLabels">
					<bean:message key="servizi.bottone.stampa" bundle="serviziLabels" />
				</html:submit>
				<%--
				<html:submit property="methodErogazione" titleKey="servizi.erogazione.dettaglioMovimentiSollecito" bundle="serviziLabels">
					<bean:message key="servizi.bottone.dettaglioMovimento" bundle="serviziLabels" />
				</html:submit>
				--%>
				<sbn:checkAttivita idControllo="SOLLECITI">
					<html:submit property="methodErogazione" titleKey="servizi.erogazione.InvioEmailSolleciti" bundle="serviziLabels">
						<bean:message key="servizi.bottone.invioSolleciti" bundle="serviziLabels" />
					</html:submit>
				</sbn:checkAttivita>
				<html:submit property="methodErogazione" titleKey="servizi.erogazione.ordinaSolleciti" bundle="serviziLabels">
					<bean:message key="servizi.bottone.ordina" bundle="serviziLabels" />
				</html:submit>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td align="right">
				<html:submit property="methodErogazione"
					styleClass="buttonSelezTutti" titleKey="servizi.title.selezionaTutti" bundle="serviziLabels"
					disabled="${ErogazioneRicercaForm.conferma}">
					<bean:message key="servizi.bottone.selTutti" bundle="serviziLabels" />
				</html:submit>
				<html:submit property="methodErogazione"
					styleClass="buttonSelezNessuno" titleKey="servizi.title.selezionaNessuno" bundle="serviziLabels"
					disabled="${ErogazioneRicercaForm.conferma}">
					<bean:message key="servizi.bottone.deselTutti" bundle="serviziLabels" />
				</html:submit>
			</td>
		</tr>
	</table>
</c:if>
