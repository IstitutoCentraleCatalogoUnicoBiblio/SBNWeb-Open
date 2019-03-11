<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<link rel="stylesheet" href='<c:url value="/styles/calendario.css" />'
	type="text/css" />

<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="blocco.totRighe"
	parameter="${navButtons}" totBlocchi="blocco.totBlocchi"
	elementiPerBlocco="blocco.maxRighe" disabled="${navForm.conferma}" />
<sbn:disableAll disabled="${navForm.conferma}">
	<table class="sintetica" id="listaPrenotazioniPosti">
		<tr>
			<th width="1%" class="header">#</th>
			<th width="30%" class="header" colspan="2">
				<bean:message key="servizi.utenti.headerUtente" bundle="serviziLabels" />
			</th>
			<th width="30%" class="header" colspan="2">
				<bean:message key="servizi.erogazione.inserimentoRichiesta.documento.sala" bundle="serviziLabels" />
			</th>
			<th width="3%" class="header">
				<bean:message key="servizi.bottone.sale.categorieMediazione" bundle="serviziLabels" />
			</th>
			<th width="3%" class="header">
				<bean:message key="servizi.erogazione.codRich" bundle="serviziLabels" />
			</th>
			<th width="7%" class="header">
				<bean:message key="servizi.erogazione.stato" bundle="serviziLabels" />
			</th>
			<th width="10%" class="header">
				<bean:message key="servizi.calendario.inizio" bundle="serviziLabels" />
			</th>
			<th width="10%" class="header">
				<bean:message key="servizi.calendario.fine" bundle="serviziLabels" />
			</th>
			<th width="1%" class="header" />
		</tr>
		<l:iterate id="pp" name="navForm" property="prenotazioni" indexId="progr">
			<tr class="row alt-color">
				<td>
					<sbn:anchor name="pp" property="progr" />
					<sbn:linkbutton	name="pp" property="selectedPren" index="id_prenotazione"
						value="id_prenotazione" key="servizi.bottone.esamina"
						bundle="serviziLabels" disabled="${navForm.conferma}" withAnchor="false" />
				</td>
				<td><bs:write name="pp" property="utente.cognomeNome" /></td>
				<td width="1%"><bs:write name="pp" property="utente.codUtente" /></td>
				<td>
					<bs:write name="pp" property="posto.sala.descrizione" />&nbsp;
					&lpar;<bs:write name="pp" property="posto.sala.codSala" />&rpar;
				</td>
				<td width="1%"><bs:write name="pp" property="posto.num_posto" /></td>
				<td>
					<c:if test="${empty pp.movimenti}">
						<bs:write name="pp" property="descrizioneCatMediazione" />
					</c:if>
				</td>
				<td>
					<c:if test="${not empty pp.movimenti}">
						<l:iterate id="mov" name="pp" property="movimenti">
							<%-- <c:if test="${mov.attivo}"> --%>
								<p>
									<sbn:linkbutton	name="mov" property="selectedMov" index="idRichiesta"
										value="idRichiesta" key="servizi.bottone.esaminaMov"
										bundle="serviziLabels" disabled="${navForm.conferma}" withAnchor="false" />
								</p>
							<%-- </c:if> --%>
						</l:iterate>
					</c:if>
				</td>
				<td>
					<bs:write name="pp" property="descrizioneStato" format="EEEE dd/MM/yyyy HH:mm" />
				</td>
				<td>
					<bs:write name="pp" property="ts_inizio" format="EEEE dd/MM/yyyy HH:mm" />
				</td>
				<td>
					<bs:write name="pp" property="ts_fine" format="EEEE dd/MM/yyyy HH:mm" />
				</td>
				<td>
					<html:radio property="selectedPren" value="${pp.id_prenotazione}" />
				</td>
			</tr>
		</l:iterate>
	</table>

	<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="blocco.totRighe"
		parameter="${navButtons}" totBlocchi="blocco.totBlocchi"
		elementiPerBlocco="blocco.maxRighe" disabled="${navForm.conferma}" bottom="true" />
	<br/>
	<table>
		<tr>
			<td>
				<bean:message key="servizi.prenotazionePosto.escludiPrenotSenzaSupporto" bundle="serviziLabels"/>
			</td>
			<td>
				<html:checkbox style="margin: 0;" property="ricercaPrenotazioni.escludiPrenotSenzaSupporto" />
				<html:hidden property="ricercaPrenotazioni.escludiPrenotSenzaSupporto" value="false" />
			</td>
			<td align=right>
				<span>Includi prenotazioni</span>
			</td>
			<td align="right">
				<span>non fruite</span>
			</td>
			<td>
				<html:checkbox property="ricercaPrenotazioni.scadute" />
				<html:hidden property="ricercaPrenotazioni.scadute" value="false" />
			</td>
		</tr>
		<tr>
			<td colspan="3"/>
			<td align="right">
				<span>concluse</span>
			</td>
			<td>
				<html:checkbox property="ricercaPrenotazioni.chiuse" />
				<html:hidden property="ricercaPrenotazioni.chiuse" value="false" />
			</td>
		</tr>
		<tr>
			<td colspan="3"/>
			<td align="right">
				<span>respinte</span>
			</td>
			<td>
				<html:checkbox property="ricercaPrenotazioni.respinte" />
				<html:hidden property="ricercaPrenotazioni.respinte" value="false" />
			</td>
		</tr>
		<tr>
			<td><bean:message key="servizi.label.elementiPerBlocco"	bundle="serviziLabels" /></td>
			<td><html:text property="ricercaPrenotazioni.elementiPerBlocco"	maxlength="3" size="4" /></td>
			<td align="right">
				<bean:message key="servizi.label.ordinamento" bundle="serviziLabels" />&nbsp;
				<html:select property="ricercaPrenotazioni.ordinamento">
					<html:optionsCollection property="listaTipoOrdinamento"	label="ds_tabella" value="cd_tabellaTrim" />
				</html:select>
			</td>
		</tr>
	</table>

	<sbn:checkAttivita idControllo="CREA_PRENOTAZIONE">
		<hr/>
		<table>
			<tr>
				<td width="7%" align="right">
					<bean:message key="servizi.erog.Utente" bundle="serviziLabels" />
				</td>
				<td width="50%" align="left">
					<html:text styleId="testoNoBold" property="grigliaCalendario.utente.codUtente"  titleKey="servizi.erogazione.codiceUtente" bundle="serviziLabels" size="30" maxlength="25"></html:text>&nbsp;&nbsp;&nbsp;
					<html:submit styleClass="buttonImage" property="${navButtons}" titleKey="servizi.erogazione.ricercaUtente" bundle="serviziLabels">
						<bean:message key="servizi.bottone.hlputente" bundle="serviziLabels" />
					</html:submit>
				</td>
			</tr>
			<tr>
				<td width="7%" align="right">
					<bean:message key="servizi.bottone.sale.categorieMediazione" bundle="serviziLabels" />
				</td>
				<td width="50%" align="left">
					<html:select styleClass="testoNoBold" property="cd_cat_mediazione" disabled="${navForm.conferma}">
						<html:optionsCollection property="listaCatMediazione" value="cd_cat_mediazione" label="descr" />
					</html:select>
				</td>
			</tr>
		</table>
	</sbn:checkAttivita>
</sbn:disableAll>
