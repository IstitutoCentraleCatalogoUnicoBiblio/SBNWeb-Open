<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/serviziweb/datiDocumento.do">
		<div id="divForm">
			<div id="divMessaggio"> <sbn:errors bundle="serviziWebMessages" /></div>
			<br>
		</div>

		<tr>
			<th colspan="4" class="etichetta" align="right">
				<c:out value="${datiDocumentoForm.biblioSel}"> </c:out>-
				<c:out value="${datiDocumentoForm.ambiente}"> </c:out> -
				<bean:message key="servizi.utenti.utenteConn" bundle="serviziWebLabels" />
				<c:out value="${datiDocumentoForm.utenteCon}"> </c:out>
			</th>
		</tr>
		<hr>
		<br>
		<tr>
			<td  colspan="2" align="center"><bean:message key="servizi.documento.complRich"	bundle="serviziWebLabels" /></td>
		</tr>

		<table style="margin-top:0"  border="1" >

			<c:if test="${not empty datiDocumentoForm.segnatura}">
				<tr>
					<!-- Segnatura -->
					<td  class="etichetta">
						<bean:message key="servizi.documento.segn"	bundle="serviziWebLabels" />
					</td>
					<td align="left">
						<html:text property="segnatura"	disabled="true" styleId="testoNormale" size="30" maxlength="30" readonly="true"></html:text>
					</td>
				</tr>
			</c:if>
			<tr>
				<!-- Autore -->
				<td  class="etichetta">
					<bean:message key="servizi.documento.autore" bundle="serviziWebLabels" />
				</td>
				<td align="left">
					<html:text property="autore" disabled="${datiDocumentoForm.lettura}" styleId="testoNormale" size="30" maxlength="160"></html:text>
				</td>
			</tr>

			<tr>
				<!-- Titolo -->
				<td  class="etichetta" >
					<bean:message key="servizi.documento.titolo" bundle="serviziWebLabels" />
				</td>
				<td align="left">
					<html:text property="titolo" disabled="${datiDocumentoForm.lettura}" styleId="testoNormale" size="60" maxlength="240"></html:text>(*)
					<c:if test="${not datiDocumentoForm.lettura}">
						(**)
					</c:if>
				</td>
			</tr>

			<tr>
				<!-- Tipo Doc -->
				<td  class="etichetta">
					<bean:message key="servizi.documento.headerDocumento" bundle="serviziWebLabels" />
				</td>

				<td align="left">
					<html:select styleClass="testoNormale"	disabled="${datiDocumentoForm.lettura}" property="doc.natura">
					<html:optionsCollection property="listaTipoDoc"	 value="codice" label="descrizione" />
					</html:select>(*)
					<!-- (*) -->
				</td>
			</tr>

			<tr>
				<!-- Luogo edi -->
				<td  class="etichetta">
					<bean:message key="servizi.documento.luogoEdi"	bundle="serviziWebLabels" />
				</td>
				<td align="left">
					<html:text property="luogoEdizione"	disabled="${datiDocumentoForm.lettura}" styleId="testoNormale" size="10" maxlength="30"></html:text>
				</td>
			</tr>

			<tr>
				<!-- Editore -->
				<td  class="etichetta">
					<bean:message key="servizi.documento.editore" bundle="serviziWebLabels" />
				</td>
				<td align="left">
					<html:text property="editore" disabled="${datiDocumentoForm.lettura}" styleId="testoNormale" size="30" maxlength="50"></html:text>
				</td>
			</tr>

			<tr>
				<!-- Anno edi -->
				<td  class="etichetta">
					<bean:message key="servizi.documento.annoEdi" bundle="serviziWebLabels" />
				</td>
				<td align="left">
					<html:text property="annoEdi" styleId="testoNormale" size="4" maxlength="4"></html:text>
					<c:if test="${empty datiDocumentoForm.segnatura}">
					<bean:message key="servizi.documento.numVolume" bundle="serviziWebLabels" />
					<html:text property="numVolume" disabled="${datiDocumentoForm.lettura}" styleId="testoNormale" size="6" maxlength="4"></html:text>
					</c:if>
				</td>
			</tr>
			<c:if test="${empty datiDocumentoForm.segnatura}">
				<tr>
					<!-- Annata (per periodici) -->
					<td  class="etichetta">
						<bean:message key="servizi.documento.annoPeriodici" bundle="serviziWebLabels" />
					</td>
					<td align="left">
						<html:text property="annataPeriodici" disabled="${datiDocumentoForm.lettura}" styleId="testoNormale" size="6" maxlength="4"></html:text>
					</td>
				</tr>
			</c:if>
			<c:if test="${empty datiDocumentoForm.segnatura}">
				<tr>
					<!-- suggerimenti -->
					<td  class="etichetta"><bean:message key="servizi.documento.suggerimenti" bundle="serviziWebLabels" /></td>
					<td align="left"><html:textarea property="suggerimenti" rows="6" cols="55" ></html:textarea></td>
				</tr>
			</c:if>
			<tr>
				<td align="center" colspan="2">

					<html:submit styleClass="submit" property="paramDatiDoc" >
						<bean:message key="servizi.bottone.indietro" bundle="serviziWebLabels" />
					</html:submit>
					<c:if test="${empty datiDocumentoForm.segnatura}">
						<html:submit styleClass="submit" property="paramDatiDoc">
							<bean:message key="servizi.bottone.ok" bundle="serviziWebLabels" />
						</html:submit>
					</c:if>
					<c:if test="${not empty datiDocumentoForm.segnatura}">
						<html:submit styleClass="submit" property="paramDatiDoc">
							<bean:message key="button.avanti" bundle="serviziWebLabels" />
						</html:submit>
					</c:if>
				</td>
			</tr>

			<tr>
				<td  colspan="2" align="center"><bean:message key="servizi.documento.campoObbligatorio"	bundle="serviziWebLabels" />
					<c:if test="${not datiDocumentoForm.lettura}">
						<p style="width: 70%"><bean:message key="servizi.documento.asteriscoTitolo"	bundle="serviziWebLabels" /></p>
					</c:if>
				</td>
			</tr>


		</table>
		<!--
		<c:if test="${empty datiDocumentoForm.segnatura}">
			<tr>
				<td><html:link page="/serviziweb/esameSugAcq.do">Esamina suggerimento di acquisto</html:link></td>
			</tr>
		</c:if>
		-->

	</sbn:navform>
</layout:page>
