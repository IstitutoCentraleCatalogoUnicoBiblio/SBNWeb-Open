<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/calendario/intervallo.do" styleId="form_calendario">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>
			<html:hidden property="action_index" styleId="action_index"/>
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
						<td><html:text property="modello.dataInizio" maxlength="10" size="12" disabled="true" /></td>
						<td><bean:message key="servizi.calendario.fine"	bundle="serviziLabels" /></td>
						<td><html:text property="modello.dataFine" maxlength="10" size="12" disabled="true" /></td>
					</tr>
					 -->
					<tr>
						<td><bean:message key="servizi.calendario.descrizione" bundle="serviziLabels" /></td>
						<td><html:text property="modello.descrizione" maxlength="160" size="50" disabled="true" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<tr>
						<td><bean:message key="servizi.calendario.inizio"
								bundle="serviziLabels" /></td>
						<td><html:text property="intervallo.dataInizio" maxlength="10" size="12" /></td>
						<td><bean:message key="servizi.calendario.fine"
								bundle="serviziLabels" /></td>
						<td><html:text property="intervallo.dataFine" maxlength="10" size="12" /></td>
					</tr>
					<tr>
						<td><bean:message key="servizi.calendario.descrizione"
								bundle="serviziLabels" /></td>
						<td><html:text property="intervallo.descrizione" maxlength="160"
								size="50" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
					<c:if test="${not navForm.intervallo.base}">
						<tr>
							<td colspan="2">&nbsp;</td>
							<td><bean:message key="servizi.calendario.intervallo.anno" bundle="serviziLabels" /></td>
							<td>
								<html:checkbox property="intervallo.absolute" />
								<html:hidden property="intervallo.absolute" value="false" />
							</td>

						</tr>
					</c:if>
				</table>
				<br/>
				<table class="intervallo" style="float: left;">
					<!-- lunedì -->
					<tr>
						<td class="minimal">
							<a id="lun"></a>
							<html:submit property="methodIntervallo" onclick="javascript:addFascia('lun');" styleClass="buttonAggiungi"
								titleKey="servizi.bottone.aggiungi.fascia" bundle="serviziLabels">
								<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels" />
							</html:submit>
						</td>
						<td class="w10em">
							<bean:message key="periodici.previsioni.giorno.lun"	bundle="periodiciLabels" />
						</td>
						<td>
							<l:notEmpty name="navForm" property="lun">
								<l:iterate id="fasciaLun" name="navForm" property="lun" indexId="idx">
									<tr>
										<td><sbn:anchor name="fasciaLun" property="uniqueId"/></td>
										<td align="right">
											<bs:define id="progr" value="${idx + 1}" />
											<bs:write name="progr" />
										</td>
										<td>dalle&nbsp;<html:text name="fasciaLun" property="start"
												maxlength="5" size="4" indexed="true" />&nbsp;alle&nbsp; <html:text
												name="fasciaLun" property="end" maxlength="5" size="4"
												indexed="true" />
											<html:submit property="methodIntervallo" onclick="javascript:deleteFascia('lun', ${idx} );" styleClass="buttonDelete"
												titleKey="servizi.bottone.rimuovi" bundle="serviziLabels">
												<bean:message key="servizi.bottone.rimuovi"	bundle="serviziLabels" />
											</html:submit>
										</td>

									</tr>
								</l:iterate>
							</l:notEmpty>
						</td>
					</tr>
					<!-- martedì -->
					<tr>
						<td>
							<a id="mar"></a>
							<html:submit property="methodIntervallo" onclick="javascript:addFascia('mar');" styleClass="buttonAggiungi"
								titleKey="servizi.bottone.aggiungi.fascia" bundle="serviziLabels">
								<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels" />
							</html:submit>
						</td>
						<td>
							<bean:message key="periodici.previsioni.giorno.mar"	bundle="periodiciLabels" />
						</td>
						<td>
							<l:notEmpty name="navForm" property="mar">
								<l:iterate id="fasciaMar" name="navForm" property="mar" indexId="idx">
									<tr>
										<td><sbn:anchor name="fasciaMar" property="uniqueId"/></td>
										<td align="right">
											<bs:define id="progr" value="${idx + 1}" />
											<bs:write name="progr" />
										</td>
										<td>dalle&nbsp;<html:text name="fasciaMar" property="start"
												maxlength="5" size="4" indexed="true" />&nbsp;alle&nbsp; <html:text
												name="fasciaMar" property="end" maxlength="5" size="4"
												indexed="true" />
											<html:submit property="methodIntervallo" onclick="javascript:deleteFascia('mar', ${idx} );" styleClass="buttonDelete"
												titleKey="servizi.bottone.rimuovi" bundle="serviziLabels">
												<bean:message key="servizi.bottone.rimuovi"	bundle="serviziLabels" />
											</html:submit>
										</td>

									</tr>
								</l:iterate>
							</l:notEmpty>
						</td>
					</tr>
					<!-- mercoledì -->
					<tr>
						<td>
							<a id="mer"></a>
							<html:submit property="methodIntervallo" onclick="javascript:addFascia('mer');" styleClass="buttonAggiungi"
								titleKey="servizi.bottone.aggiungi.fascia" bundle="serviziLabels">
								<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels" />
							</html:submit>
						</td>
						<td>
							<bean:message key="periodici.previsioni.giorno.mer"	bundle="periodiciLabels" />
						</td>
						<td>
							<l:notEmpty name="navForm" property="mer">
								<l:iterate id="fasciaMer" name="navForm" property="mer" indexId="idx">
									<tr>
										<td><sbn:anchor name="fasciaMer" property="uniqueId"/></td>
										<td align="right">
											<bs:define id="progr" value="${idx + 1}" />
											<bs:write name="progr" />
										</td>
										<td>dalle&nbsp;<html:text name="fasciaMer" property="start"
												maxlength="5" size="4" indexed="true" />&nbsp;alle&nbsp; <html:text
												name="fasciaMer" property="end" maxlength="5" size="4"
												indexed="true" />
											<html:submit property="methodIntervallo" onclick="javascript:deleteFascia('mer', ${idx} );" styleClass="buttonDelete"
												titleKey="servizi.bottone.rimuovi" bundle="serviziLabels">
												<bean:message key="servizi.bottone.rimuovi"	bundle="serviziLabels" />
											</html:submit>
										</td>

									</tr>
								</l:iterate>
							</l:notEmpty>
						</td>
					</tr>
					<!-- giovedì -->
					<tr>
						<td>
							<a id="gio"></a>
							<html:submit property="methodIntervallo" onclick="javascript:addFascia('gio');" styleClass="buttonAggiungi"
								titleKey="servizi.bottone.aggiungi.fascia" bundle="serviziLabels">
								<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels" />
							</html:submit>
						</td>
						<td>
							<bean:message key="periodici.previsioni.giorno.gio"	bundle="periodiciLabels" />
						</td>
						<td>
							<l:notEmpty name="navForm" property="gio">
								<l:iterate id="fasciaGio" name="navForm" property="gio" indexId="idx">
									<tr>
										<td><sbn:anchor name="fasciaGio" property="uniqueId"/></td>
										<td align="right">
											<bs:define id="progr" value="${idx + 1}" />
											<bs:write name="progr" />
										</td>
										<td>dalle&nbsp;<html:text name="fasciaGio" property="start"
												maxlength="5" size="4" indexed="true" />&nbsp;alle&nbsp; <html:text
												name="fasciaGio" property="end" maxlength="5" size="4"
												indexed="true" />
											<html:submit property="methodIntervallo" onclick="javascript:deleteFascia('gio', ${idx} );" styleClass="buttonDelete"
												titleKey="servizi.bottone.rimuovi" bundle="serviziLabels">
												<bean:message key="servizi.bottone.rimuovi"	bundle="serviziLabels" />
											</html:submit>
										</td>

									</tr>
								</l:iterate>
							</l:notEmpty>
						</td>
					</tr>
					<!-- venerdì -->
					<tr>
						<td>
							<a id="ven"></a>
							<html:submit property="methodIntervallo" onclick="javascript:addFascia('ven');" styleClass="buttonAggiungi"
								titleKey="servizi.bottone.aggiungi.fascia" bundle="serviziLabels">
								<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels" />
							</html:submit>
						</td>
						<td>
							<bean:message key="periodici.previsioni.giorno.ven"	bundle="periodiciLabels" />
						</td>
						<td>
							<l:notEmpty name="navForm" property="ven">
								<l:iterate id="fasciaVen" name="navForm" property="ven" indexId="idx">
									<tr>
										<td><sbn:anchor name="fasciaVen" property="uniqueId"/></td>
										<td align="right">
											<bs:define id="progr" value="${idx + 1}" />
											<bs:write name="progr" />
										</td>
										<td>dalle&nbsp;<html:text name="fasciaVen" property="start"
												maxlength="5" size="4" indexed="true" />&nbsp;alle&nbsp; <html:text
												name="fasciaVen" property="end" maxlength="5" size="4"
												indexed="true" />
											<html:submit property="methodIntervallo" onclick="javascript:deleteFascia('ven', ${idx} );" styleClass="buttonDelete"
												titleKey="servizi.bottone.rimuovi" bundle="serviziLabels">
												<bean:message key="servizi.bottone.rimuovi"	bundle="serviziLabels" />
											</html:submit>
										</td>

									</tr>
								</l:iterate>
							</l:notEmpty>
						</td>
					</tr>
					<!-- sabato -->
					<tr>
						<td>
							<a id="sab"></a>
							<html:submit property="methodIntervallo" onclick="javascript:addFascia('sab');" styleClass="buttonAggiungi"
								titleKey="servizi.bottone.aggiungi.fascia" bundle="serviziLabels">
								<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels" />
							</html:submit>
						</td>
						<td>
							<bean:message key="periodici.previsioni.giorno.sab"	bundle="periodiciLabels" />
						</td>
						<td>
							<l:notEmpty name="navForm" property="sab">
								<l:iterate id="fasciaSab" name="navForm" property="sab" indexId="idx">
									<tr>
										<td><sbn:anchor name="fasciaSab" property="uniqueId"/></td>
										<td align="right">
											<bs:define id="progr" value="${idx + 1}" />
											<bs:write name="progr" />
										</td>
										<td>dalle&nbsp;<html:text name="fasciaSab" property="start"
												maxlength="5" size="4" indexed="true" />&nbsp;alle&nbsp; <html:text
												name="fasciaSab" property="end" maxlength="5" size="4"
												indexed="true" />
											<html:submit property="methodIntervallo" onclick="javascript:deleteFascia('sab', ${idx} );" styleClass="buttonDelete"
												titleKey="servizi.bottone.rimuovi" bundle="serviziLabels">
												<bean:message key="servizi.bottone.rimuovi"	bundle="serviziLabels" />
											</html:submit>
										</td>

									</tr>
								</l:iterate>
							</l:notEmpty>
						</td>
					</tr>
					<!-- domenica -->
					<tr>
						<td>
							<a id="dom"></a>
							<html:submit property="methodIntervallo" onclick="javascript:addFascia('dom');" styleClass="buttonAggiungi"
								titleKey="servizi.bottone.aggiungi.fascia" bundle="serviziLabels">
								<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels" />
							</html:submit>
						</td>
						<td>
							<bean:message key="periodici.previsioni.giorno.dom"	bundle="periodiciLabels" />
						</td>
						<td>
							<l:notEmpty name="navForm" property="dom">
								<l:iterate id="fasciaDom" name="navForm" property="dom" indexId="idx">
									<tr>
										<td><sbn:anchor name="fasciaDom" property="uniqueId"/></td>
										<td align="right">
											<bs:define id="progr" value="${idx + 1}" />
											<bs:write name="progr" />
										</td>
										<td>dalle&nbsp;<html:text name="fasciaDom" property="start"
												maxlength="5" size="4" indexed="true" />&nbsp;alle&nbsp; <html:text
												name="fasciaDom" property="end" maxlength="5" size="4"
												indexed="true" />
											<html:submit property="methodIntervallo" onclick="javascript:deleteFascia('dom', ${idx} );" styleClass="buttonDelete"
												titleKey="servizi.bottone.rimuovi" bundle="serviziLabels">
												<bean:message key="servizi.bottone.rimuovi"	bundle="serviziLabels" />
											</html:submit>
										</td>

									</tr>
								</l:iterate>
							</l:notEmpty>
						</td>
					</tr>
				</table>
				<div>
					<html:submit property="methodIntervallo" onclick="javascript:deleteFascia('all', 0);" styleClass="buttonDelete"
						titleKey="servizi.bottone.rimuoviTutto" bundle="serviziLabels">
						<bean:message key="servizi.bottone.rimuovi"	bundle="serviziLabels" />
					</html:submit>
				</div>
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
<script type="text/javascript" src='<c:url value="/scripts/calendario/calendario.js" />'></script>