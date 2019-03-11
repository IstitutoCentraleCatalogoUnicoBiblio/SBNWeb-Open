<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<script type="text/javascript" src='<c:url value="/scripts/calendario/calendario.js" />'></script>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/calendario/calendario.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>
			<sbn:disableAll disabled="${navForm.conferma}">
				<table>
					<c:if test="${navForm.modello.tipo eq 'MEDIAZIONE'}">
						<tr>
							<td><bean:message key="servizi.sale.posti.catMediazione" bundle="serviziLabels" /></td>
							<td>
								<html:select property="modello.cd_cat_mediazione" disabled="true">
									<html:optionsCollection property="listaCategorieMediazione" value="cd_tabellaTrim" label="ds_tabella" />
								</html:select>
							</td>
							<td colspan="2">&nbsp;</td>
						</tr>
					</c:if>
					<!--
					<tr>
						<td><bean:message key="servizi.calendario.inizio" bundle="serviziLabels" /></td>
						<td><html:text property="modello.dataInizio" maxlength="10" size="12" /></td>
						<td><bean:message key="servizi.calendario.fine"	bundle="serviziLabels" /></td>
						<td><html:text property="modello.dataFine" maxlength="10" size="12" /></td>
					</tr>
					-->
					<tr>
						<td><bean:message key="servizi.calendario.descrizione" bundle="serviziLabels" /></td>
						<td><html:text property="modello.descrizione" maxlength="160" size="50" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
				</table>
				<hr/>
				<html:hidden property="selected" styleId="selected_intervallo" />
				<table class="sintetica" style="width: 50%;">
					<tr>
						<th width="1%" class="header">#</th>
						<th width="3%" class="header">
							<bean:message key="servizi.calendario.descrizione" bundle="serviziLabels" />
						</th>
						<th width="7%" class="header">
							<bean:message key="servizi.calendario.inizio" bundle="serviziLabels" />
						</th>
						<th width="7%" class="header">
							<bean:message key="servizi.calendario.fine" bundle="serviziLabels" />
						</th>
						<th width="3%" class="header">
							<bean:message key="servizi.calendario.intervallo.anno" bundle="serviziLabels" />
						</th>
						<th width="2%" class="header">
							<html:submit property="${navButtons}" styleClass="buttonAggiungi" titleKey="servizi.bottone.nuovo.intervallo"
								bundle="serviziLabels">
								<bean:message key="servizi.bottone.nuovo.intervallo" bundle="serviziLabels" />
							</html:submit>
						</th>

					</tr>
					<l:iterate id="item" property="modello.intervalli" name="navForm" indexId="progr">
						<tr class="row alt-color">
							<td>
								<sbn:linkbutton
									name="item" property="selected" index="uniqueId"
									value="id" key="servizi.bottone.esamina"
									bundle="serviziLabels" withAnchor="false" />
							</td>
							<td>
								<bs:write name="item" property="descrizione" />
							</td>
							<c:if test="${not item.absolute}">
								<td>
									<bs:write name="item" property="inizio" format="dd MMMM" />
								</td>
								<td>
									<bs:write name="item" property="fine" format="dd MMMM" />
								</td>
								<td>&nbsp;</td>
							</c:if>
							<c:if test="${item.absolute}">
								<td>
									<bs:write name="item" property="inizio" format="EEEE dd/MM/yyyy" />
								</td>
								<td>
									<bs:write name="item" property="fine" format="EEEE dd/MM/yyyy" />
								</td>
								<td>
									<%-- <html:checkbox name="item" property="absolute" disabled="true" /> --%>
									<bean:message key="label.yes" />
								</td>
							</c:if>
							<td class="center">
								<%-- <html:radio property="selected" value="${item.uniqueId}" style="text-align:center;"
											titleKey="servizi.erogazione.movimenti.selezioneSingola" bundle="serviziLabels" /> --%>

								<html:submit property="${navButtons}" onclick="selectIntervallo(${item.uniqueId});" styleClass="buttonEdit"
										titleKey="servizi.bottone.esamina" bundle="serviziLabels">
										<bean:message key="servizi.bottone.esamina" bundle="serviziLabels" />
								</html:submit>
								<c:if test="${not item.base}">
									<html:submit property="${navButtons}" onclick="selectIntervallo(${item.uniqueId});" styleClass="buttonDelete"
										titleKey="servizi.bottone.rimuovi" bundle="serviziLabels">
										<bean:message key="servizi.bottone.elimina.intervallo"	bundle="serviziLabels" />
									</html:submit>
								</</c:if>
							</td>
						</tr>
					</l:iterate>
				</table>
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
