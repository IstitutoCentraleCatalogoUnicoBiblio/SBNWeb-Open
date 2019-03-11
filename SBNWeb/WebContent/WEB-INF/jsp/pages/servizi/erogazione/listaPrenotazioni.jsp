<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/erogazione/ListaPrenotazioni.do">

		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>


			<c:if test="${not empty ListaPrenotazioniForm.listaPrenotazioni}">
					<span class="etichetta" style="font-weight: bolder;" >
						<bean:message key="servizi.erogazione.listaPrenotazioni" bundle="serviziLabels" />
					</span>
					<hr/>

					<table class="sintetica">
						<tr>
							<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.codRich" bundle="serviziLabels" />
							</th>
							<th width="7%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.movimento.dataRichiesta" bundle="serviziLabels" />
							</th>
							<th width="20%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.utenti.headerUtente" bundle="serviziLabels" />
							</th>
							<th width="30%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.titolo" bundle="serviziLabels" />
							</th>
							<th width="7%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.listaMovimenti.documentoCollocazione" bundle="serviziLabels" />
							</th>
							<%--
							<th width="20%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.statoRichiesta" bundle="serviziLabels" />
							</th>
							--%>
							<th width="7%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.dataInizioPrevista" bundle="serviziLabels" />
							</th>
							<th width="12%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />
							</th>
							<th width="7%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.erogazione.dataMassima" bundle="serviziLabels" />
							</th>
							<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.utenti.headerSelezionata" bundle="serviziLabels" />
							</th>
							<%--<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0" style="text-align:center; font-size: 90%;">
								<bean:message key="servizi.utenti.headerSelezionataMultipla" bundle="serviziLabels" />
							</th> --%>
						</tr>

						<logic:iterate id="listaMov" property="listaPrenotazioni" name="ListaPrenotazioniForm" indexId="progr">
							<sbn:rowcolor var="color" index="progr" />

							<tr>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%; text-align:center;">
									<sbn:anchor name="listaMov" property="progr"/> <sbn:linkbutton
										name="listaMov" property="codSelSing" index="codRichServ"
										value="codRichServ" key="servizi.bottone.esamina"
										bundle="serviziLabels" withAnchor="false" />
								</td>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bean-struts:write name="listaMov" property="dataRichiestaNoOraString" />
								</td>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bean-struts:write name="listaMov" property="cognomeNome"/>
								</td>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;" >
									<bean-struts:write name="listaMov" property="titolo" />
								</td>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bean-struts:write name="listaMov" property="datiDocumento" />
								</td>
								<%--
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bean-struts:write name="listaMov" property="stato_richiesta" />
								</td>
								--%>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bean-struts:write name="listaMov" property="dataInizioEffNoOraString" />
								</td>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bean-struts:write name="listaMov" property="tipoServizio" />
								</td>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bean-struts:write name="listaMov" property="dataFinePrevNoOraString" />
								</td>
								<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<bean-struts:define id="codval">
										<bean-struts:write name="listaMov" property="codRichServ" />
									</bean-struts:define>
									<html:radio property="codSelSing" value="${codval}" style="text-align:center;"
												titleKey="servizi.erogazione.listaMovimenti.selezioneSingola" bundle="serviziLabels" />
								</td>
								<%--<td bgcolor="${color}" class="testoNoBold" style="font-size: 90%;text-align:center;">
									<html:multibox  property="codSel" value="${codval}" style="text-align:center;"
													titleKey="servizi.erogazione.listaMovimenti.selezioneMultipla" bundle="serviziLabels" />
								</td> --%>
							</tr>
						</logic:iterate>
					</table>
			</c:if>


		</div>

		<div id="divFooterCommon">
			<table border="0">
				<tr>
					<td class="etichetta">
						<bean:message key="servizi.label.ordinamento" bundle="serviziLabels" />
					</td>
					<td  class="testoNormale">
						<html:select property="ordinamento">
							<html:optionsCollection property="ordinamentiPrenotazioni" value="codice" label="descrizione" />
						</html:select>
					</td>
				</tr>
			</table>
		</div>
		<br/>

		<div id="divFooter">
			<table align="center">
				<tr>
					<c:choose>
						<c:when test="${ListaPrenotazioniForm.conferma}">
							<jsp:include
								page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
						</c:when>
						<c:otherwise>
							<jsp:include
								page="/WEB-INF/jsp/subpages/servizi/erogazione/footerListaPrenotazioni.jsp" />
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
