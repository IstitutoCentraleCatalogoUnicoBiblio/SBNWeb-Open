<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<script type="text/javascript" src='<c:url value="/scripts/servizi/sale/sale.js" />'></script>

<html:xhtml />
<layout:page>
	<script type="text/javascript">
		$(document).ready(function() {
			var len = $("#numPosti").val().length;
			$("#posto_in").attr("maxlength", len);
		});
	</script>
	<sbn:navform action="/servizi/sale/dettaglioSala.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>
			<sbn:disableAll disabled="${navForm.conferma}">
				<div style="display: inline-block;">
					<table style="float: left;">
						<tr>
							<td><bean:message key="servizi.sale.codiceSala"	bundle="serviziLabels" /></td>
							<td><html:text property="sala.codSala" maxlength="2" size="4" disabled="${not navForm.sala.nuovo}" /></td>
							<td colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td><bean:message key="servizi.sale.descrizione" bundle="serviziLabels" /></td>
							<td><html:text property="sala.descrizione" maxlength="160" size="50" /></td>
							<td colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td><bean:message key="servizi.sale.numPostiTotale"	bundle="serviziLabels" /></td>
							<td><html:text property="sala.numeroPosti" maxlength="4" size="4" disabled="${not navForm.sala.nuovo}" styleId="numPosti" /></td>
							<td colspan="2">&nbsp;</td>
						</tr>
					</table>

					<table style="display: inline-block;">
						<tr>
							<td class="right"><bean:message key="servizi.sale.durataFascia" bundle="serviziLabels" /></td>
							<td><html:text property="sala.durataFascia" maxlength="3" size="4" /></td>
							<td colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td class="right"><bean:message key="servizi.sale.maxFascePrenotazione" bundle="serviziLabels" /></td>
							<td><html:text property="sala.maxFascePrenotazione" maxlength="1" size="2" /></td>
							<td colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td class="right"><bean:message key="servizi.sale.prenotabileDaRemoto" bundle="serviziLabels" /></td>
							<td>
								<html:checkbox styleClass="no-margin" property="sala.prenotabileDaRemoto" />
								<html:hidden property="sala.prenotabileDaRemoto" value="false" />
							</td>
							<td colspan="2">&nbsp;</td>
						</tr>
						<sbn:checkAttivita idControllo="ALTRI_UTENTI">
							<tr>
								<td class="right"><bean:message key="servizi.sale.maxUtentiPerPrenotazione" bundle="serviziLabels" /></td>
								<td><html:text property="sala.maxUtentiPerPrenotazione" maxlength="1" size="2" /></td>
								<td colspan="2">&nbsp;</td>
							</tr>
						</sbn:checkAttivita>
					</table>
				</div>
				<!-- POSTI -->
				<l:notEmpty name="navForm" property="sala.gruppi">
					<html:hidden property="action_index" styleId="action_index"/>
					<hr/>
					<h2><bean:message key="servizi.sale.posti" bundle="serviziLabels" /></h2>
					<table class="sintetica" style="width: 50%;">
						<tr>
							<th width="1%" class="header">#</th>
							<th width="7%" class="header">
								<bean:message key="servizi.sale.posti.intervallo" bundle="serviziLabels" />
							</th>
							<th width="7%" class="header">
								<bean:message key="servizi.sale.posti.supporti" bundle="serviziLabels" />
							</th>
							<th width="1%" class="header">
								<html:submit property="methodDettSala" onclick="javascript:addGruppo();" styleClass="buttonAggiungi">
									<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels" />
								</html:submit>
							</th>
						</tr>
						<l:iterate id="gruppo" property="sala.gruppi" name="navForm" indexId="progr">
							<sbn:rowcolor var="color" index="progr" />
							<tr class="row alt-color">
								<td>
									<bs:write name="gruppo" property="gruppo" />
								</td>
								<td>
									<bean:message key="servizi.sale.posti" bundle="serviziLabels" />&nbsp;
									<bean:message key="servizi.utenti.da" bundle="serviziLabels" />&nbsp;
									<html:text name="gruppo" property="posto_da" indexed="true" styleClass="digit_short" styleId="posto_in" />&nbsp;
									<bean:message key="servizi.utenti.a" bundle="serviziLabels" />&nbsp;
									<html:text name="gruppo" property="posto_a" indexed="true" styleClass="digit_short" styleId="posto_in" />
								</td>
								<td>
									<div>
										<ul class="etichetta">
											<l:iterate id="cat_med" property="descrCategorieMediazione" name="gruppo" indexId="cat">
												<li><bs:write name="cat_med" /></li>
											</l:iterate>
										</ul>
									</div>
								</td>
								<td bgcolor="${color}" class="center">
									<html:submit property="methodDettSala" onclick="javascript:deleteGruppo( ${progr} );" styleClass="buttonEdit"
										titleKey="servizi.bottone.supporti" bundle="serviziLabels">
										<bean:message key="servizi.bottone.supporti" bundle="serviziLabels" />
									</html:submit>
									<sbn:checkAttivita idControllo="CANCELLA_GRUPPO_POSTI">
										<html:submit property="methodDettSala" onclick="javascript:deleteGruppo( ${progr} );" styleClass="buttonDelete"
											titleKey="servizi.bottone.rimuovi" bundle="serviziLabels">
											<bean:message key="servizi.bottone.rimuovi"	bundle="serviziLabels" />
										</html:submit>
									</sbn:checkAttivita>
								</td>
							</tr>
						</l:iterate>
					</table>
				</l:notEmpty>

				<!-- CALENDARI -->
				<sbn:checkAttivita idControllo="CALENDARIO_ATTIVO">
					<hr/>
					<h2><bean:message key="servizi.calendario.calendario" bundle="serviziLabels" /></h2>
					<table class="sintetica" style="width: 50%;">
						<tr>
							<th width="3%" class="header">
								<bean:message key="servizi.calendario.descrizione" bundle="serviziLabels" />
							</th>
							<th width="7%" class="header">
								<bean:message key="servizi.calendario.inizio" bundle="serviziLabels" />
							</th>
							<th width="7%" class="header">
								<bean:message key="servizi.calendario.fine" bundle="serviziLabels" />
							</th>
							<th width="1%" class="header" />
						</tr>
						<tr class="row alt-color">
							<td>
								<bs:write name="navForm" property="sala.calendario.descrizione" />
							</td>
							<td>
								<bs:write name="navForm" property="sala.calendario.inizio" format="EEEE dd/MM/yyyy" />
							</td>
							<td>
								<bs:write name="navForm" property="sala.calendario.fine" format="EEEE dd/MM/yyyy" />
							</td>
							<td class="center">
								<html:submit property="methodDettSala" styleClass="buttonDelete"
									titleKey="servizi.bottone.cancella.calendario" bundle="serviziLabels">
									<bean:message key="servizi.bottone.cancella.calendario"	bundle="serviziLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
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
