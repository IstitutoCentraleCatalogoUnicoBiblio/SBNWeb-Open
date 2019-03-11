<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<sbn:checkAttivita idControllo="MESSAGGIO">
	<hr />
	<sbn:anchor name="navForm" property="messaggio.uniqueId"/>
	<table>
		<tr>
			<th align="left">
				<bean:message key="servizi.erogazione.ill.messaggio" bundle="serviziLabels" />
			</th>
			<td>
				<span class="testoBold"><bs:write scope="session" name="UTENTE_KEY" property="biblioteca" /></span>
			</td>
		</tr>
		<tr>
			<td width="10%" scope="col" class="etichetta" align="left"><bean:message
					key="servizi.erogazione.ill.messaggio.data" bundle="serviziLabels" />&colon;</td>
			<td><html:text property="messaggio.dataMessaggioStr" size="10"
					readonly="true" /></td>
		</tr>
		<tr>
			<td width="10%" scope="col" class="etichetta" align="left"><bean:message
					key="servizi.erogazione.dettaglio.movimento.ill.bib.fornitrice"
					bundle="serviziLabels" />&colon;</td>
			<td><html:text property="messaggio.isil"
					size="10" readonly="true" /></td>
		</tr>
		<tr>
			<td width="10%" scope="col" class="etichetta" align="left"><bean:message
					key="servizi.erogazione.movimento.statoRichiesta"
					bundle="serviziLabels" />&colon;</td>
			<td><html:text property="messaggio.descrizioneStatoRichiesta"
					size="70" readonly="true" /></td>
		</tr>
		<sbn:checkAttivita idControllo="CONDIZIONE">
			<tr>
				<td width="10%" scope="col" class="etichetta" align="left"><bean:message
						key="servizi.erogazione.ill.condizione" bundle="serviziLabels" />&colon;</td>
				<td>
					<html:select property="messaggio.condizione">
						<html:optionsCollection property="messaggio.listaTipoCondizione" label="ds_tabella" value="cd_tabellaTrim" />
					</html:select>
				</td>
			</tr>
		</sbn:checkAttivita>
		<tr>
			<td width="10%" scope="col" class="etichetta" align="left"><bean:message
					key="servizi.erogazione.ill.messaggio" bundle="serviziLabels" />&colon;</td>
			<td><html:textarea property="messaggio.note" rows="2" cols="70" /></td>
		</tr>
	</table>
</sbn:checkAttivita>