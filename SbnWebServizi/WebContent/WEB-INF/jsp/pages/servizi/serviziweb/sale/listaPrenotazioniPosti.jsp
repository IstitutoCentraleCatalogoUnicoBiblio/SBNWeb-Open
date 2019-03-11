<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<link rel="stylesheet" href='<c:url value="/styles/calendario.css" />' type="text/css" />

<html:xhtml />
<layout:page>
	<sbn:navform action="/serviziweb/listaPrenotazioniPosti.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>

			<sbn:disableAll disabled="${navForm.conferma}">
				<html:hidden property="selectedPren" />
				<table class="sintetica" id="listaPrenotazioniPosti">
					<tr>
						<th width="1%" class="header">#</th>
						<th width="30%" class="header" colspan="2">
							<bean:message key="servizi.erogazione.inserimentoRichiesta.documento.sala" bundle="serviziLabels" />
						</th>
						<th width="7%" class="header">
							<bean:message key="servizi.bottone.sale.categorieMediazione" bundle="serviziLabels" />
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
								<bs:write name="pp" property="descrizioneCatMediazione" />
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
								<html:submit property="${navButtons}" styleClass="button w6em" onclick="validateSubmit('selectedPren', ${pp.id_prenotazione});">
									<bean:message key="servizi.bottone.esamina" bundle="serviziLabels" />
								</html:submit>
								<br/>
								<%--
								<html:submit property="${navButtons}" styleClass="button w6em" onclick="validateSubmit('selectedPren', ${pp.id_prenotazione});">
									<bean:message key="servizi.bottone.annullaOperazione" bundle="serviziLabels" />
								</html:submit>
								--%>
							</td>
						</tr>
					</l:iterate>
				</table>
				<sbn:checkAttivita idControllo="CREA_PRENOTAZIONE">
					<hr/>
					<div>
						<html:submit property="${navButtons}" styleClass="button">
							<bean:message key="servizi.utenti.prenotaPosto"	bundle="serviziWebLabels" />
						</html:submit>
						<span>per</span>
						<html:select styleClass="testoNoBold" property="cd_cat_mediazione" disabled="${navForm.conferma}">
							<html:optionsCollection property="listaCatMediazione" value="cd_cat_mediazione" label="descr" />
						</html:select>
					</div>
				</sbn:checkAttivita>
			</sbn:disableAll>
		</div>
		<c:if test="${navForm.conferma}">
			<div id="divFooter">
				<table align="center">
					<tr>
						<td><sbn:bottoniera buttons="pulsanti" /></td>
					</tr>
				</table>
			</div>
		</c:if>
	</sbn:navform>
</layout:page>
