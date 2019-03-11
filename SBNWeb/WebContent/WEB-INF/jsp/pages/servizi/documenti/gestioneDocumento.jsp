<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<script type="text/javascript">
function toggle() {
	$("#opacMarcRecord").toggle();
}
</script>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/documenti/GestioneDocumento.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors bundle="serviziMessages" />
		</div>
		<sbn:disableAll disabled="${navForm.conferma}">

			<jsp:include page="/WEB-INF/jsp/subpages/servizi/documenti/dettaglioDoc.jsp" flush="true" />

			<sbn:checkAttivita idControllo="DOC_BIBLIOTECHE">
				<p><bean:message key="ricerca.biblioteca.sintetica.titolo" bundle="amministrazioneSistemaLabels" /></p>
				<table class=sintetica>
					<tr class="etichetta" bgcolor="#dde8f0">
						<th style="width: 8%"><bean:message key="ricerca.biblioteca.cdana" bundle="amministrazioneSistemaLabels" /></th>
						<th style="width: 8%"><bean:message key="ricerca.biblioteca.cdbib" bundle="amministrazioneSistemaLabels" /></th>
					<th><bean:message key="documentofisico.bibliotecaT"	bundle="documentoFisicoLabels" /></th>
					<th style="width: 3%"><bean:message key="servizi.erogazione.ill.prestito" bundle="serviziLabels"/> </th>
					<th style="width: 3%"><bean:message key="servizi.erogazione.ill.riproduzione" bundle="serviziLabels"/> </th>
					<th style="width: 3%" />
					</tr>
					<logic:iterate id="itemBibOpac" property="documento.biblioteche"
						name="navForm" indexId="riga">
						<sbn:rowcolor var="color" index="riga" />
						<tr bgcolor="${color}">
							<td>
								<bs:write name="itemBibOpac" property="cd_ana_biblioteca" />
							</td>
							<td>
								<bs:write name="itemBibOpac" property="cod_polo" />&nbsp;<bs:write name="itemBibOpac" property="cod_bib" />
							</td>
							<td>
								<bs:write name="itemBibOpac" property="nom_biblioteca" />
							</td>
							<td>
								<html:checkbox name="itemBibOpac" property="ill_prestito" disabled="true" />
							</td>
							<td>
								<html:checkbox name="itemBibOpac" property="ill_riproduzione" disabled="true" />
							</td>
							<td>
								<!--
								<html:multibox property="bibSelezionate" value="${itemBib.repeatableId}" />
								<html:hidden property="bibSelezionate" value="0" />
								 -->
								 <html:text name="itemBibOpac" property="priorita" maxlength="1" size="3" indexed="true"
								 	title="${itemBibOpac.cd_ana_biblioteca}"/>
							</td>
						</tr>
					</logic:iterate>
				</table>
			</sbn:checkAttivita>

			<c:if test="${not empty navForm.documento.marcRecord}" >
				<p><a href="#opacMarcRecord" onclick="javascript:toggle()">UNIMARC</a></p>
				<div id="opacMarcRecord">
					<p style="font-family: monospace;">
						<bs:write filter="false" name="navForm" property="documento.formattedMarcRecord" />
					</p>
				</div>
			</c:if>

			<sbn:checkAttivita idControllo="CREA">
			<c:if test="${navForm.documento.posseduto}">
				<hr>
				<table class="sintetica">
					<tr>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">n.</th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.inventario"
							bundle="serviziLabels" /></th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.precisazione" bundle="serviziLabels" /></th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.codNoDispLungo"
							bundle="serviziLabels" /></th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.fonte" bundle="serviziLabels" /></th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="servizi.documenti.cancellato"
							bundle="serviziLabels" /></th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
						<%-- <th class="etichetta" scope="col" bgcolor="#dde8f0"></th> --%>
					</tr>

					<bs:define id="color" value="#FEF1E2" />
					<logic:iterate id="itemEsemplare" property="documento.esemplari"
						name="navForm" indexId="idx">
						<sbn:rowcolor var="color" index="idx" />
						<tr bgcolor="${color}">
							<td class="testoNoBold"><sbn:anchor name="itemEsemplare"
								property="progr" /> <bs:write name="itemEsemplare"
								property="progr" /></td>
							<td class="testoNoBold"><html:text property="inventario"
								size="15" maxlength="12" indexed="true" name="itemEsemplare"
								disabled="${itemEsemplare.cancellato}" /></td>
							<td class="testoNoBold"><html:textarea name="itemEsemplare" property="annata" cols="60" rows="1" indexed="true"  readonly="${itemEsemplare.cancellato}"></html:textarea></td>
							<td class="testoNoBold"><html:select
								styleClass="testoNormale" indexed="true" name="itemEsemplare"
								property="codNoDisp" disabled="${itemEsemplare.cancellato or (not navForm.documento.posseduto) }">
								<html:optionsCollection property="listaTipoCodNoDisp"
									value="codice" label="descrizione" />
							</html:select></td>
							<td class="testoNoBold"><html:select
								styleClass="testoNormale" indexed="true" name="itemEsemplare"
								property="fonte" disabled="true">
								<html:optionsCollection property="listaTipoFonte" value="codice"
									label="descrizione" />
							</html:select></td>
							<td class="testoNoBold"><c:choose>
								<c:when test="${itemEsemplare.cancellato}">
									<bean:message key="servizi.documenti.cancellato.si"
										bundle="serviziLabels" />
								</c:when>
								<c:otherwise>
									<bean:message key="servizi.documenti.cancellato.no"
										bundle="serviziLabels" />
								</c:otherwise>
							</c:choose></td>
							<td class="testoNoBold"><html:radio
								property="codSelezionato" value="${itemEsemplare.progr}" /></td>
							<%--<td class="testoNoBold"><html:multibox
								property="idSelezionati" value="${itemEsemplare.progr}" /></td>  --%>
						</tr><!--
						<tr bgcolor="${color}" >
							<td class="etichetta"  colspan="2">
							<bean:message key="servizi.documenti.precisazione" bundle="serviziLabels" />
							</td>
							<td class="testoNoBold" colspan="4">
							<html:textarea name="itemEsemplare" property="annata" cols="80" rows="3" indexed="true"  readonly="${itemEsemplare.cancellato}"></html:textarea>
							</td>
						</tr>

					--></logic:iterate>
				</table>
			</c:if>
			</sbn:checkAttivita>
		</sbn:disableAll>
		<hr>
		</div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<td><sbn:bottoniera buttons="listaPulsanti" /></td>
				<td>
				<c:choose>
					<c:when test="${navForm.numDoc gt 1 && (navForm.modalita eq 'GESTIONE' or navForm.modalita eq 'GESTIONE_SIF')
						and not navForm.conferma}">
						<html:submit styleClass="pulsanti" property="methodGestioneDoc">
							<bean:message key="servizi.bottone.scorriIndietro" bundle="serviziLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodGestioneDoc">
							<bean:message key="servizi.bottone.scorriAvanti" bundle="serviziLabels" />
						</html:submit>
					</c:when>
				</c:choose>
				<td>

			</tr>
		</table>

		</div>
	</sbn:navform>
</layout:page>
