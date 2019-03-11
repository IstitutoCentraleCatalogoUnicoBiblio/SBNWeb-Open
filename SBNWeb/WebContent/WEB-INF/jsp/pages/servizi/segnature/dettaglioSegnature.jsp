<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"  prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/segnature/DettaglioSegnature">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />

			</div>
			<br>
			<table width="80%" border="0">
				<tr>
					<td align="right" class="etichetta">
						<bean:message key="servizi.utenti.codiceBiblioteca" bundle="serviziLabels" />
					</td>
					<td align="left" colspan="2">
						<html:text styleId="testoNoBold" property="dettSegn.codBiblioteca" size="5" disabled="true"></html:text>
					</td>
				</tr>

				<!-- CODICE SEGNATURA -->
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.segnature.header.codSegn" bundle="serviziLabels" />
					</td>
					<td colspan="2">
						<html:text styleId="testoNoBold" property="dettSegn.id"
									disabled="true"></html:text>
					</td>

				</tr>

				<!-- SEGNATURA INIZIO  -->
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.segnature.header.iniSegn" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="dettSegn.segnInizio"
									size="30" maxlength="80" readonly="${DettaglioSegnatureForm.conferma}"></html:text>
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="dettSegn.segnDa" disabled="true" size="50"></html:text>
					</td>
				</tr>

				<!-- SEGNATURA FINE -->
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.segnature.header.finSegn"
							bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="dettSegn.segnFine"
							size="30" maxlength="80" readonly="${DettaglioSegnatureForm.conferma}"></html:text>
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="dettSegn.segnA" disabled="true" size="50"></html:text>
					</td>
				</tr>

				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.segnature.tipFruiz" bundle="serviziLabels" />
					</td>
					<td align="left"  colspan="2">
						<html:select property="dettSegn.codFruizione"
							disabled="${DettaglioSegnatureForm.conferma}">
							<html:optionsCollection property="lstFruizioni" value="codice" label="descrizione" />
						</html:select>
					</td>
				</tr>

				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.segnature.tipIndisp" bundle="serviziLabels" />
					</td>
					<td align="left" colspan="2">
						<html:select property="dettSegn.codIndisp"
							disabled="${DettaglioSegnatureForm.conferma}">
							<html:optionsCollection property="lstIndisp" value="codice" label="descrizione" />
						</html:select>
					</td>
				</tr>

				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.segnature.dataIns" bundle="serviziLabels" />
					</td>
					<td align="left" colspan="2">
						<html:text style="text-align:right;" styleId="testoNoBold" property="dettSegn.dataOraIns" readonly="true"></html:text>
					</td>
				</tr>

				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.segnature.dataAgg" bundle="serviziLabels" />
					</td>
					<td align="left" colspan="2">
						<html:text style="text-align:right;" styleId="testoNoBold" property="dettSegn.dataOraAgg" readonly="true"></html:text>
					</td>
				</tr>
			</table>
		</div>

		<div id="divFooter">
			<c:choose>
				<c:when test="${DettaglioSegnatureForm.conferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/segnature/footerDettSegnature.jsp" />
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>
