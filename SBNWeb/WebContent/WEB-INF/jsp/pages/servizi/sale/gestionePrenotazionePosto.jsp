<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<link rel="stylesheet" href='<c:url value="/styles/calendario.css" />'
	type="text/css" />

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/sale/gestionePrenotazionePosto.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>
			<sbn:disableAll disabled="${navForm.conferma}">
			<table>
				<tr>
					<th width="15%" align="left"><bean:message
							key="servizi.erogazione.dettaglioMovimento.DatiUtente"
							bundle="serviziLabels" /></th>
				</tr>
				<tr>
					<td align="right"><bean:message
							key="servizi.erogazione.dettaglioMovimento.numeroTessera"
							bundle="serviziLabels" /></td>
					<td align="left"><html:text
							property="ricerca.utente.codUtente" readonly="true" size="20"
							titleKey="servizi.erogazione.listaMovimenti.titleCodiceUtente"
							bundle="serviziLabels"></html:text>&nbsp;&nbsp;&nbsp;<html:text
							property="ricerca.utente.cognomeNome" readonly="true" size="50"
							style="text-align:left;"
							titleKey="servizi.erogazione.listaMovimenti.cognomeNome"
							bundle="serviziLabels"></html:text></td>
				</tr>
				<tr>
					<td align="right"><bean:message
							key="servizi.erogazione.movimento.email" bundle="serviziLabels" /></td>
					<td align="left"><html:text property="ricerca.utente.email"
							readonly="true" size="30" bundle="serviziLabels" /> <c:if
							test="${not empty navForm.ricerca.utente.email}">
							<a href="mailto://${navForm.ricerca.utente.email}"> <html:img
									altKey="servizi.erogazione.movimento.email"
									bundle="serviziLabels" page="/styles/images/email18.png"
									styleClass="mailto" />
							</a>
						</c:if></td>
				</tr>
				<c:if test="${not empty navForm.descrCatMediazione}">
					<tr>
						<td align="right"><bean:message key="servizi.bottone.sale.categorieMediazione" bundle="serviziLabels" /></td>
						<td align="left"><html:text property="descrCatMediazione" readonly="true" /></td>
					</tr>
				</c:if>
				<sbn:checkAttivita idControllo="DATI_DOCUMENTO">
					<tr>
						<th align="left"><bean:message
								key="servizi.erogazione.dettaglioMovimento.DatiDocumento"
								bundle="serviziLabels" /></th>
					</tr>
					<l:iterate id="mov" name="navForm" property="movimenti">
						<c:if test="${mov.attivo}">
							<!-- riga bid + titolo-->
							<tr>
								<td align="right"><div style="float: left"><bs:write name="mov" property="idRichiesta"/></div>
									<bean:message
										key="servizi.erogazione.listaMovimenti.titoloDocumento"
										bundle="serviziLabels" /></td>
								<td align="left"><html:text  name="mov" property="bid"
										size="10" readonly="true" bundle="serviziLabels"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;<html:text
										 name="mov" property="titolo" size="80" readonly="true"
										bundle="serviziLabels"></html:text></td>
							</tr>
							<!-- riga esemplare doc / inventario collocazione + eventuale check periodico-->
							<c:choose>
								<c:when test="${mov.progrEsempDocLet eq null}">
									<tr>
										<td align="right"><html:text name="mov" property="codDocLet"
												size="5" readonly="true"
												titleKey="servizi.erogazione.listaMovimenti.codiceDocLett"
												bundle="serviziLabels"></html:text></td>
										<td align="left"><html:text name="mov"
												property="progrEsempDocLet" size="5" readonly="true"
												titleKey="servizi.erogazione.listaMovimenti.progrEsempDocLett"
												bundle="serviziLabels"></html:text>
								</c:when>
								<c:otherwise>
									<tr>
										<td align="right"><bean:message
												key="servizi.erogazione.movimento.invColl"
												bundle="serviziLabels" /></td>
										<td align="left"><html:text name="mov" property="inventario"
												size="15" readonly="true" bundle="serviziLabels">
											</html:text>&nbsp;&nbsp;/&nbsp;&nbsp; <html:text name="mov"
												property="segnatura" size="35" readonly="true"
												bundle="serviziLabels">
											</html:text></td>
								</c:otherwise>
							</c:choose>
							<c:if test="${not empty navForm.tipoMateriale}">
								<tr>
									<td align="right"><bean:message
											key="documentofisico.tipoMaterialeT"
											bundle="documentoFisicoLabels" /></td>
									<td align="left"><html:text property="tipoMateriale"
											readonly="true" bundle="serviziLabels"></html:text></td>
								</tr>
							</c:if>
						</c:if>
					</l:iterate>
				</sbn:checkAttivita>
				<sbn:checkAttivita idControllo="DETTAGLIO">
					<tr>
						<th align="left"><bean:message
								key="servizi.erogazione.dettaglioMovimento.DatiPrenotazionePosto"
								bundle="serviziLabels" /></th>
					</tr>
					<tr>
						<td align="right"><bean:message key="servizi.erogazione.dettaglioMovimento.progrRichiesta"
								bundle="serviziLabels" /></td>
						<td width="60%" align="left"><html:text
								property="prenotazione.id_prenotazione" readonly="true" />
						</td>
					</tr>
					<tr>
						<td align="right"><bean:message key="servizi.sale.sala"
								bundle="serviziLabels" /></td>
						<td width="60%" align="left"><html:text
								property="prenotazione.posto.sala.descrizione" readonly="true" />&nbsp;il
							<html:text name="navForm" property="prenotazione.dataInizio"
								readonly="true" styleClass="date" />&nbsp; <bean:message
								key="servizi.sale.prenotazione.start" bundle="serviziLabels" />&nbsp;
							<html:text name="navForm" property="prenotazione.orarioInizio"
								readonly="true" styleClass="time" />&nbsp; <bean:message
								key="servizi.sale.prenotazione.end" bundle="serviziLabels" />&nbsp;
							<html:text name="navForm" property="prenotazione.orarioFine"
								readonly="true" styleClass="time" /></td>
					</tr>
					<tr>
						<td align="right"><bean:message
								key="servizi.erogazione.stato" bundle="serviziLabels" /></td>
						<td><html:text name="navForm"
								property="prenotazione.descrizioneStato" readonly="true" /></td>
					</tr>
				</sbn:checkAttivita>
			</table>

			<sbn:checkAttivita idControllo="ALTRI_UTENTI">
				<hr/>
				<h2><bean:message key="servizi.prenotazionePosto.altriUtenti" bundle="serviziLabels" /></h2>
				<html:hidden property="selectedUtente" styleId="selected_utente" />
				<table class="sintetica" style="width: 50%;">
					<tr>
						<th width="3%" class="header">
							<bean:message key="servizi.utenti.codUtente" bundle="serviziLabels" />
						</th>
						<th width="7%" class="header">
							<bean:message key="servizi.utenti.headerUtente" bundle="serviziLabels" />
						</th>
						<th width="2%" class="header">
							<sbn:checkAttivita idControllo="AGGIUNGI_UTENTE">
								<html:submit property="${navButtons}" styleClass="buttonAggiungi" titleKey="servizi.bottone.prenotazionePosto.aggiungi.utente"
									bundle="serviziLabels">
									<bean:message key="servizi.bottone.prenotazionePosto.aggiungi.utente" bundle="serviziLabels" />
								</html:submit>
							</sbn:checkAttivita>
						</th>

					</tr>
					<l:iterate id="utente" property="prenotazione.altriUtenti" name="navForm" indexId="progr">
						<c:if test="${utente.idUtente ne navForm.prenotazione.utente.idUtente}">
						<tr class="row alt-color">
							<td>
								<bs:write name="utente" property="codUtente" />
							</td>
							<td>
								<bs:write name="utente" property="cognomeNome" />
							</td>
							<td class="center">
								<sbn:checkAttivita idControllo="RIMUOVI_UTENTE">
									<html:submit property="${navButtons}" onclick="selectUtente(${utente.idUtente});" styleClass="buttonDelete"
										titleKey="servizi.bottone.rimuovi" bundle="serviziLabels">
										<bean:message key="servizi.bottone.rimuovi"	bundle="serviziLabels" />
									</html:submit>
								</sbn:checkAttivita>
							</td>
						</tr>
						</c:if>
					</l:iterate>
				</table>
			</sbn:checkAttivita>
			</sbn:disableAll>
			<sbn:checkAttivita idControllo="SPOSTA">
				<h2>Sposta prenotazione&colon;&nbsp;<span id="pp_descr"><bs:write name="navForm" property="prenotazione.descrizionePrenotazione" /></span></h2>
			</sbn:checkAttivita>
			<sbn:checkAttivita idControllo="PRENOTAZIONI_UTENTE">
			<h2>Scegli una prenotazione esistente per associarle il documento, oppure creane una nuova.</h2>
			<table class="sintetica" id="listaPrenotazioniPosti">
				<tr>
					<th width="1%" class="header">#</th>
					<th width="30%" class="header" colspan="2">
						<bean:message key="servizi.erogazione.inserimentoRichiesta.documento.sala" bundle="serviziLabels" />
					</th>
					<th class="header">
						<bean:message key="servizi.utenti.tag.documenti" bundle="serviziLabels" />
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
				<l:iterate id="pp" name="navForm" property="prenotazioniUtente" indexId="progr">
					<tr class="row alt-color">
						<td>
							<sbn:linkbutton	name="pp" property="selectedPren" index="id_prenotazione"
								value="id_prenotazione" key="servizi.bottone.esamina"
								bundle="serviziLabels" disabled="true" withAnchor="true" />
						</td>
						<td>
							<bs:write name="pp" property="posto.sala.descrizione" />&nbsp;
							&lpar;<bs:write name="pp" property="posto.sala.codSala" />&rpar;
						</td>
						<td width="1%"><bs:write name="pp" property="posto.num_posto" /></td>
						<td>
							<div>
								<ul class="etichetta">
									<l:iterate id="mov" property="movimenti" name="pp">
										<c:if test="${mov.attivo}">
											<li><bs:write name="mov" property="titolo" /></li>
										</c:if>
									</l:iterate>
								</ul>
							</div>
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
			</sbn:checkAttivita>

			<sbn:disableAll disabled="${navForm.conferma}">
				<sbn:checkAttivita idControllo="PREV_NEXT">
					<div class="cal_intestazione">
						<sbn:disableAll checkAttivita="PREV">
							<html:submit property="methodGestPrenPosto" styleId="nav_prev">
								<bean:message key="button.elemPrec"
									bundle="gestioneBibliograficaLabels" />
							</html:submit>
						</sbn:disableAll>
						<div id="cal_intestazione_dati">
							<c:if test="${not empty navForm.sala}">
								<h2><bs:write name="navForm" property="sala.sala.descrizione" /></h2>
							</c:if>
							<c:if test="${empty navForm.sala}">
								<h2>&nbsp;</h2>
							</c:if>
							<h2>
								<bs:write name="navForm" property="intestazione" />
							</h2>
							<div id="selection">&nbsp;</div>
						</div>
						<sbn:disableAll checkAttivita="NEXT">
							<html:submit property="methodGestPrenPosto" styleId="nav_next">
								<bean:message key="button.elemSucc"
									bundle="gestioneBibliograficaLabels" />
							</html:submit>
						</sbn:disableAll>
					</div>

				</sbn:checkAttivita>

				<sbn:checkAttivita idControllo="MESE">
					<html:hidden property="selectedDay" />
					<l:notEmpty name="navForm" property="giorni">
						<br />
						<sbn:grigliaCalendario name="navForm" giorni="giorni"
							target="selectedDay" />
					</l:notEmpty>
				</sbn:checkAttivita>

				<sbn:checkAttivita idControllo="SALA">
					<html:hidden property="selectedSala" />
					<br />
					<table style="width: 100%">
						<l:iterate id="slot" name="navForm" property="giorno.slotSala" indexId="progr">
							<tr id="sala-${progr}" class="sala-float" title="${slot.sala.descrizione}"
								onclick="javascript:validateSubmit('selectedSala', ${slot.sala.idSala})">
								<td style="width: 50%">
									<p><span class="testoBold"><bs:write name="slot" property="sala.descrizione" /></span></p>
								</td>
								<td style="width: 50%;">
								<div style="margin-right: 1em;">
									<ul class="little">
										<li>
											<bean:message key="servizi.sale.numPostiTotale"	bundle="serviziLabels" />:&nbsp;
											<bs:write name="slot" property="sala.numeroPosti" />
										</li>
										<li>
											<%-- <bean:message key="servizi.sale.maxDurataPrenotazione" bundle="serviziLabels" />:&nbsp;
											<bs:write name="slot" property="sala.durataPrenotazione" />  --%>
											<bean:message key="servizi.sale.primaDisponibilita" bundle="serviziLabels" />:&nbsp;
											<bs:write name="slot" property="primoSlotDisponibile" />
										</li>
									</ul>
								</div>
								</td>
							</tr>
						</l:iterate>
					</table>
				</sbn:checkAttivita>

				<sbn:checkAttivita idControllo="GIORNO">
					<html:hidden property="selectedFascia" />
					<br />
					<div class="sintetica">
						<l:iterate id="slot" name="navForm" property="sala.slots" indexId="progr">
							<sbn:rowcolor var="color" index="progr" />

							<c:if test="${slot.active}">
								<div id="slot-${progr}" class="slot-float slot-disponibile" title="${slot.descrizione}">
									<p>
										<span id="start"><bs:write name="slot" property="start" /></span>
										<br/>
										<span id="end"><bs:write name="slot" property="end" /></span>
									</p>
								</div>
							</c:if>
							<c:if test="${not slot.active}">
								<div class="slot-float" style="background-color:darkgrey">
									<p>
										<span id="start"><bs:write name="slot" property="start" /></span>
										<br/>
										<span id="end"><bs:write name="slot" property="end" /></span>
									</p>
								</div>
							</c:if>
						</l:iterate>
					</div>
				</sbn:checkAttivita>

			</sbn:disableAll>
			<br>
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td><sbn:bottoniera buttons="pulsanti" /></td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>

<script>
var config = {
		max_slots: ${navForm.maxSlots},
		conferma: ${navForm.conferma}
	}
</script>
<script type="text/javascript"
	src='<c:url value="/scripts/calendario/calendario.js" />'></script>
<script type="text/javascript"
	src='<c:url value="/scripts/calendario/prenotazione.js" />'></script>
