<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/serviziweb/esameSugAcq.do">
		<div id="divForm">
			<div id="divMessaggio"> <sbn:errors bundle="serviziWebMessages" /></div>
		<br>

		</div>
		<table cellspacing="0" width="100%" border="0">

			<tr>
				<th colspan="4" class="etichetta" align="right">
					<c:out value="${EsameSugAcqForm.biblioSel}"> </c:out>-
					<c:out value="${EsameSugAcqForm.ambiente}"> </c:out> -
					<bean:message key="servizi.utenti.utenteConn" bundle="serviziWebLabels" />
					<c:out value="${EsameSugAcqForm.utenteCon}"> </c:out>
				<hr>
				</th>
			</tr>

			<tr>
				<th colspan="4" class="etichetta" align="right">
					<bean:message key="servizi.documento.criterio.ricerca" bundle="serviziWebLabels" />
				<hr>
				</th>
			</tr>

			<tr>

			</tr>

			<tr>

				<!-- Suggerimenti inseriti dal al -->
				<td><bean:message key="servizi.utenti.stato.inseriti.dal"  bundle="serviziWebLabels" />
				<html:text name="EsameSugAcqForm" property="dal" maxlength="10"></html:text></td>
				<td><bean:message key="servizi.utenti.stato.inseriti.al"  bundle="serviziWebLabels" />
				<html:text name="EsameSugAcqForm" property="al" maxlength="10"></html:text></td>

				<!-- Stato del suggerimento -->
				<c:if test="${not empty EsameSugAcqForm.listaStatoSuggerimento}">
					<td align="left">
						<bean:message key="servizi.utenti.stato.suggerimento" bundle="serviziWebLabels" />

						<html:select styleClass="testoNormale"	property="sugAcq.statoSuggerimentoDocumento">
						<html:optionsCollection name="EsameSugAcqForm" property="listaStatoSuggerimento" value="codice" label="descrizione" />
						</html:select>
					</td>
				</c:if>


			</tr>

			<tr>
				<th colspan="4" class="etichetta" align="left">

					<html:submit styleClass="submit" property="paramEsameSugAcq">
						<bean:message key="servizi.bottone.cerca" bundle="serviziWebLabels" />
					</html:submit>
					<html:submit styleClass="submit" property="paramEsameSugAcq">
						<bean:message key="servizi.bottone.nuova.proposta" bundle="serviziWebLabels" />
					</html:submit>
				<hr>
				</th>
			</tr>


			</table>
			<c:if test="${not empty EsameSugAcqForm.listaSugAcq}">
			<tr>
				<th colspan="4" class="etichetta" align="left">
					<em><strong><bean:message key="servizi.documento.risultato.ricerca"  bundle="serviziWebLabels" /></strong></em>
				<hr>
				</th>
			</tr>
			<table cellspacing="0" width="100%" border="0">

				<logic:iterate id="item" property="listaSugAcq" name="EsameSugAcqForm" indexId="riga">

						<tr>
							<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0"><bean:message key="servizi.utente.autore" bundle="serviziWebLabels" /> </th>
							<td><c:out value="${item.primoAutore}"> </c:out></td>

						</tr>

						<tr>
							<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
								<bean:message key="servizi.utente.titolo"	bundle="serviziWebLabels" />
							</th>
							<td><c:out value="${item.titolo.descrizione}"> </c:out>	</td>
						</tr>

						<tr>
							<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
								<bean:message key="servizi.utente.editore" bundle="serviziWebLabels" />
							</th>
							<td><c:out value="${item.editore}"> </c:out></td>

						</tr>

						<tr>
							<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
								<bean:message key="servizi.utente.stato" bundle="serviziWebLabels" />
							</th>
							<td><c:out value="${item.denoStatoSuggerimento}"> </c:out></td>
						</tr>

						<tr>
							<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
								<bean:message key="servizi.utente.Messaggio" bundle="serviziWebLabels" />
							</th>
							<td><c:out value="${item.noteDocumento}"> </c:out></td>
						</tr>

						<tr>
							<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
								<bean:message key="servizi.utente.natura" bundle="serviziWebLabels" />
							</th>
							<td><c:out value="${item.naturaBid}"> </c:out></td>
						</tr>

						<tr>
							<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
								<bean:message key="servizi.utente.luogoEdizione" bundle="serviziWebLabels" />
							</th>
							<td><c:out value="${item.luogoEdizione}"> </c:out></td>
						</tr>

						<tr>
							<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
								<bean:message key="servizi.utente.dataSugg" bundle="serviziWebLabels" />
							</th>
							<td><c:out value="${item.dataIns}"> </c:out></td>
						</tr>
						<c:if test="${not empty item.msgPerLettore}">
						<tr>
							<th width="4%" class="etichetta" scope="col" bgcolor="#dde8f0">
								<bean:message key="servizi.utenti.biblio.note" bundle="serviziWebLabels" />
							</th>
							<td><c:out value="${item.msgPerLettore}"> </c:out></td>
						</tr>
						</c:if>
						<tr>
							<th colspan="4" class="etichetta" align="left">
								<hr>
							</th>
						</tr>

					</logic:iterate>

			</table>
		</c:if>
	</sbn:navform>
</layout:page>
