<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/erogazione/ListaMovimenti.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>
			<c:choose>
				<c:when
					test="${navForm.tipoRicerca eq 'RICERCA_PER_UTENTE'}">
					<jsp:include	page="/WEB-INF/jsp/subpages/servizi/erogazione/headerUteListaMovimenti.jsp" />
					<br/>
				</c:when>
				<c:when
					test="${navForm.tipoRicerca eq 'RICERCA_PER_INVENTARIO'}">
					<jsp:include	page="/WEB-INF/jsp/subpages/servizi/erogazione/headerInvListaMovimenti.jsp" />
					<br/>
				</c:when>
				<c:when
					test="${navForm.tipoRicerca eq 'RICERCA_PER_SEGNATURA'}">
					<jsp:include	page="/WEB-INF/jsp/subpages/servizi/erogazione/headerSegListaMovimenti.jsp" />
					<br/>
				</c:when>
			</c:choose>

			<c:choose>
			<c:when test="${empty navForm.chiamante}">
				<!--  GESTIONE SOLLECITI  -->
				<c:choose>
					<c:when test="${navForm.solleciti eq 'S'  and not navForm.mostraSolleciti}">
						<div>
							<bean:message key="message.servizi.erogazione.sollecitiPresenti" bundle="serviziMessages" />
						</div>
						<html:submit property="methodListaMovimentiUte">
								<bean:message key="servizi.bottone.mostraSolleciti" bundle="serviziLabels" />
						</html:submit>
					</c:when>
					<c:when test="${navForm.solleciti eq 'S' and navForm.mostraSolleciti}">
							<jsp:include	page="/WEB-INF/jsp/subpages/servizi/erogazione/listaSolleciti.jsp" />
							<br/>
							<html:submit property="methodListaMovimentiUte">
								<bean:message key="servizi.bottone.nascondiSolleciti" bundle="serviziLabels" />
							</html:submit>
					</c:when>
				</c:choose>
				<br/>
				<!--  FINE GESTIONE SOLLECITI  -->
			</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${not empty navForm.listaMovRicUte}">
				<!--  LISTA MOVIMENTI  -->
					<c:if test="${empty navForm.chiamante and navForm.tipoRicerca ne 'RICERCA_LISTE'}">
					<span class="etichetta" style="font-weight: bolder;" >

					<%--
						<bean:message key="servizi.erogazione.listaMovimenti" bundle="serviziLabels" />
						<c:choose>
							<c:when test="${navForm.movRicerca.movimentiInCorso}">
								&nbsp;<bean:message key="servizi.erogazione.richiesta.movimenti.attivi" bundle="serviziLabels" />
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${navForm.movRicerca.movimentiAnnullati}">
								&nbsp;<bean:message key="servizi.erogazione.richiesta.movimenti.annullati" bundle="serviziLabels" />
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${navForm.movRicerca.movimentiChiusi}">
								&nbsp;<bean:message key="servizi.erogazione.richiesta.movimenti.chiusi" bundle="serviziLabels" />
							</c:when>
						</c:choose>
					--%>

						<bean:message
						key="servizi.erogazione.richieste"
						bundle="serviziLabels" />&nbsp;&nbsp;
						<bean:message
						key="servizi.erogazione.richieste.incorso"
						bundle="serviziLabels" />&nbsp;
						<html:checkbox
						property="movRicerca.richiesteInCorso"></html:checkbox>
						<html:hidden property="movRicerca.richiesteInCorso" value="false"></html:hidden>&nbsp;&nbsp;
						<bean:message
						key="servizi.erogazione.richieste.respinte"
						bundle="serviziLabels" />&nbsp;
						<html:checkbox
						property="movRicerca.richiesteRespinte"></html:checkbox>
						<html:hidden property="movRicerca.richiesteRespinte" value="false"></html:hidden>&nbsp;&nbsp;
						<bean:message
						key="servizi.erogazione.richieste.evase"
						bundle="serviziLabels" />&nbsp;
						<html:checkbox
						property="movRicerca.richiesteEvase"></html:checkbox>
						<html:hidden property="movRicerca.richiesteEvase" value="false"></html:hidden>
					</span>
					</c:if>

					<hr/>
					<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
						elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
						parameter="methodListaMovimentiUte" />

					<%-- le successive istruzioni commentate riportavano il significato dei colori nella sintetica movimenti
					<table cellspacing="2" cellpadding="2" border="0" style="font-size: 90%;">
						<tr>
							<td style="width:15pt;">
								<table style="width:100%; height:90%; background-color: #00008B;"><tr><td></td></tr></table>
							</td>
							<td>richieste con solleciti</td>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td style="width:15pt;">
								<table style="width:100%; height:90%; background-color: #FF0000;"><tr><td></td></tr></table>
							</td>
							<td>richieste scadute non sollecitate</td>
						</tr>
					</table>
					--%>

					<table class="sintetica">
						<tr>
							<%--
							<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								n.
							</th>
							--%>
							<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.codRich" bundle="serviziLabels" />
							</th>
							<th width="12%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />
							</th>
							<th width="11%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.attivita" bundle="serviziLabels" />
							</th>
							<%--
							<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.tipoRecord" bundle="serviziLabels" />
							</th>
							--%>
							<c:choose>
								<c:when test="${navForm.tipoRicerca eq 'RICERCA_PER_INVENTARIO'}">
									<th width="20%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
										<bean:message key="servizi.utenti.headerUtente" bundle="serviziLabels" />
									</th>
								</c:when>
								<c:when test="${navForm.tipoRicerca eq 'RICERCA_PER_SEGNATURA'}">
									<th width="20%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
										<bean:message key="servizi.utenti.headerUtente" bundle="serviziLabels" />
									</th>
								</c:when>
								<c:when test="${navForm.tipoRicerca eq 'RICERCA_LISTE'}">
									<th width="20%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
										<bean:message key="servizi.utenti.headerUtente" bundle="serviziLabels" />
									</th>
								</c:when>
							</c:choose>
							<c:choose>
								<c:when test="${navForm.tipoRicerca eq 'RICERCA_PER_UTENTE'}">
									<th width="30%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
										<bean:message key="servizi.erogazione.titolo" bundle="serviziLabels" />
									</th>
								</c:when>
								<c:when test="${navForm.tipoRicerca eq 'RICERCA_LISTE'}">
									<th width="30%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
										<bean:message key="servizi.erogazione.titolo" bundle="serviziLabels" />
									</th>
								</c:when>
							</c:choose>
							<th width="7%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.listaMovimenti.documentoCollocazione" bundle="serviziLabels" />
							</th>
							<th width="6%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.dataInizio" bundle="serviziLabels" />
							</th>
							<th width="10%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.statoRichiesta" bundle="serviziLabels" />
							</th>
							<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.utenti.headerSelezionataMultipla" bundle="serviziLabels" />
							</th>
							<%--
							<th width="6%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.dataFine" bundle="serviziLabels" />
							</th>
							--%>
							<!-- ROX 14.04.10  TCK 3388
							<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.utenti.headerSelezionata" bundle="serviziLabels" />
							</th>
							--%>
						</tr>
						<logic:iterate id="listaMov" property="listaMovRicUte" name="navForm" indexId="idx">
							<sbn:rowcolor var="color" index="idx" />

							<%-- le successive istruzioni commentate rimpostavano i colori nella sintetica movimenti
							<c:choose>
							<c:when test="${listaMov.scaduto}">
								<tr style="color: #FF0000;">
							</c:when>
							<c:when test="${listaMov.esisteSollecito}">
								<tr style="color: #00008B;">
							</c:when>
							<c:otherwise>
							<tr>
							</c:otherwise>
							</c:choose>
							--%>
							<!-- al posto delle precedenti c'è bisogno della successiva istruzione -->
							<tr>
								<%--
								<td bgcolor="${color}" >
									<sbn:anchor name="listaMov" property="progr"/> <sbn:linkbutton
										name="listaMov" property="codSelMovSing" index="codRichServ"
										value="progr" key="servizi.bottone.esamina"
										bundle="serviziLabels" withAnchor="false" />
								</td>
								--%>

								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%; text-align:center;">
									<sbn:anchor name="listaMov" property="progr"/>
									<sbn:linkbutton	name="listaMov" property="codSelMov" index="codRichServ"
										value="codRichServ" key="servizi.bottone.esamina"
										bundle="serviziLabels" disabled="${not empty navForm.chiamante}" withAnchor="false" />
								</td>

								<%--
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%; text-align:center;">
									<bs:write name="listaMov" property="codRichServ" />
								</td>
								--%>

								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bs:write name="listaMov" property="tipoServizio" />
								</td>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bs:write name="listaMov" property="ultimaAttivita" />
								</td>
								<%--
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%; text-align:center;">
									<bs:write name="listaMov" property="flSvolg" />
								</td>
								--%>
								<c:choose>
									<c:when test="${navForm.tipoRicerca eq 'RICERCA_PER_INVENTARIO'}">
								   		<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
											<bs:write name="listaMov" property="cognomeNome"/>
										</td>
									</c:when>
									<c:when test="${navForm.tipoRicerca eq 'RICERCA_PER_SEGNATURA'}">
								   		<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
											<bs:write name="listaMov" property="cognomeNome"/>
										</td>
									</c:when>
									<c:when test="${navForm.tipoRicerca eq 'RICERCA_LISTE'}">
								   		<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
											<bs:write name="listaMov" property="cognomeNome"/>
										</td>
									</c:when>
								</c:choose>
								<c:choose>
									<c:when test="${navForm.tipoRicerca eq 'RICERCA_PER_UTENTE'}">
										<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;" >
											<bs:write name="listaMov" property="titolo" />
										</td>
									</c:when>
									<c:when test="${navForm.tipoRicerca eq 'RICERCA_LISTE'}">
										<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;" >
											<bs:write name="listaMov" property="titolo" />
										</td>
									</c:when>
								</c:choose>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bs:write name="listaMov" property="datiDocumento" />
								</td>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<c:choose>
										<c:when test="${listaMov.consegnato}">
											<bs:write name="listaMov" property="dataInizioEffNoOraString" />
										</c:when>
										<c:otherwise>
											<bs:write name="listaMov" property="dataRichiestaNoOraString" />
										</c:otherwise>
									</c:choose>
								</td>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bs:write name="listaMov" property="stato_richiesta" />
								</td>
								<%--
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bs:write name="listaMov" property="dataFinePrevNoOraString" />
								</td>
								--%>
								<!-- ROX 14.04.10  TCK 3388
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<html:radio property="codSelMovSing" value="${listaMov.codRichServ}" style="text-align:center;"
												titleKey="servizi.erogazione.listaMovimenti.selezioneSingola" bundle="serviziLabels"
												disabled="${not empty navForm.chiamante}" />
								-->
								</td>
									<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
										<html:multibox  property="codSelMov" value="${listaMov.codRichServ}" style="text-align:center;"
														titleKey="servizi.erogazione.listaMovimenti.selezioneMultipla" bundle="serviziLabels"
														disabled="${not empty navForm.chiamante}" />
										<html:hidden property="codSelMov" value="0" disabled="${not empty navForm.chiamante}" />
									</td>
							</tr>
						</logic:iterate>
					</table>

					<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
						elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
						parameter="methodListaMovimentiUte" bottom="true" />


					<br/>
					<!--  FINE LISTA MOVIMENTI  -->
				</c:when>
				<c:otherwise>
					<div style="font-style: oblique;">
						<bean:message key="message.servizi.erogazione.movimentiNonPresenti" bundle="serviziMessages" />
					</div>
					<br/>
				</c:otherwise>
			</c:choose>


			<c:if test="${empty navForm.chiamante and navForm.tipoRicerca ne 'RICERCA_LISTE'}">
				<sbn:checkAttivita idControllo="GESTIONE">
					<!-- GESTIONE CAMPI PER INSERIMENTO NUOVA RICHIESTA-->
					<jsp:include flush="true" page="/WEB-INF/jsp/subpages/servizi/erogazione/nuovaRichiesta.jsp" />
				</sbn:checkAttivita>
			</c:if>
		</div>
		<br/>


		<div id="divFooter">
			<table align="center">
				<tr>
					<c:choose>
						<c:when test="${navForm.conferma}">
							<jsp:include flush="true"
								page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
						</c:when>
						<c:otherwise>
							<jsp:include flush="true"
								page="/WEB-INF/jsp/subpages/servizi/erogazione/footerListaMovimenti.jsp" />
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
		</div>

	</sbn:navform>
</layout:page>
