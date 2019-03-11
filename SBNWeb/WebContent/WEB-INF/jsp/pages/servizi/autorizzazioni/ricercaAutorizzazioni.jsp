<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/autorizzazioni/RicercaAutorizzazioni">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<table width="100%" border="0">
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.utenti.bibliotecaUtente"
							bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold"
							property="autRicerca.codBib" size="5" disabled="true"></html:text>
					</td>
					<td class="etichetta"  style="text-align:right;">
						<bean:message key="servizi.autorizzazioni.header.codAut"
							bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold"
							property="autRicerca.codice" size="15"></html:text>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.autorizzazioni.desAutorizzazione"
							bundle="serviziLabels" />
					</td>
					<td colspan="3" align="left">
						<html:text styleId="testoNoBold"
							property="autRicerca.descrizione"></html:text>
					</td>
				</tr>
			</table>
		</div>
		<div id="divFooterCommon">
			<table width="100%" border="0" style="height:40px">
				<tr>
					<td width="80" class="etichetta">
						<bean:message key="documentofisico.elementiBlocco"
							bundle="documentoFisicoLabels" />
					</td>
					<td width="150" class="testoNormale">
						<html:text property="autRicerca.numeroElementiBlocco" size="5"></html:text>
					</td>
					<td width="75" class="etichetta">
						<bean:message key="documentofisico.ordinamento"
							bundle="documentoFisicoLabels" />
					</td>
					<td width="150" class="testoNormale">
						<html:select property="autRicerca.ordinamento">
							<html:optionsCollection property="lstTipiOrdinamento"
								value="codice" label="descrizione" />
						</html:select>
					</td>
				</tr>
			</table>
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center">
						<html:submit property="methodRicAut">
							<bean:message key="servizi.bottone.cerca" bundle="serviziLabels" />
						</html:submit>
						<c:choose>
							<c:when test="${RicercaAutorizzazioniForm.nonTrovato}">
								<sbn:checkAttivita idControllo="GESTIONE">
									<html:submit property="methodRicAut">
										<bean:message key="servizi.bottone.nuovo" bundle="serviziLabels" />
									</html:submit>
								</sbn:checkAttivita>
							</c:when>
						</c:choose>
						<!--
						<html:submit property="methodRicAut">
							<bean:message key="servizi.bottone.annulla"
								bundle="serviziLabels" />
						</html:submit>
						-->
					</td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
