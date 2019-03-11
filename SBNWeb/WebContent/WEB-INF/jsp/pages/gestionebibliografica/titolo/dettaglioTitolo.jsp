<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Autore
		almaviva2 - Inizio Codifica Agosto 2006
-->


<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>


	<sbn:navform action="/gestionebibliografica/titolo/dettaglioTitolo.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" />
		</div>
		<c:choose>
			<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'DET'}">
				<c:choose>
					<c:when
						test="${dettaglioTitoloForm.dettTitComVO.tipoLegame eq 'TI_TI'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioDatiLegameTitoloTit.jsp" />
						<hr color="#dde8f0" />
					</c:when>
				</c:choose>
			</c:when>

			<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'AGGNOTA'}">
			</c:when>

			<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'SUG'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:text
							property="dettTitComVO.detTitoloPFissaVO.natura" size="5"
							readonly="true" title="${dettaglioTitoloForm.descNatura}">
						</html:text></td>
						<td width="60" class="etichetta"><bean:message
							key="ricerca.tipoMateriale" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:text
							property="dettTitComVO.detTitoloPFissaVO.tipoMat" size="5"
							readonly="true" title="${dettaglioTitoloForm.descTipoMat}">
						</html:text></td>
						<td width="60" class="etichetta"><bean:message
							key="ricerca.tipoRecord" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:text
							property="dettTitComVO.detTitoloPFissaVO.tipoRec" size="5"
							readonly="true" title="${dettaglioTitoloForm.descTipoRec}">
						</html:text></td>
					</tr>
				</table>
			</c:when>


			<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'VARIANAT'}">
				<table border="0">
					<tr>
						<td class="etichetta"><bean:message	key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>

						<td><html:select property="dettTitComVO.detTitoloPFissaVO.natura" style="width:40px" onchange="this.form.submit()">
							<html:optionsCollection property="listaNatura" value="codice" label="descrizioneCodice" />
						</html:select></td>

						<td width="60" class="etichetta"><bean:message
							key="ricerca.tipoMateriale" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:text
							property="dettTitComVO.detTitoloPFissaVO.tipoMat" size="5"
							readonly="true" title="${dettaglioTitoloForm.descTipoMat}">
						</html:text></td>
						<td width="60" class="etichetta"><bean:message
							key="ricerca.tipoRecord" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:text
							property="dettTitComVO.detTitoloPFissaVO.tipoRec" size="5"
							readonly="true" title="${dettaglioTitoloForm.descTipoRec}">
						</html:text></td>
					</tr>
				</table>
			</c:when>



			<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'COPIARET'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:text
							property="dettTitComVO.detTitoloPFissaVO.natura" size="5"
							readonly="true" title="${dettaglioTitoloForm.descNatura}">
						</html:text></td>
						<td width="60" class="etichetta"><bean:message
							key="ricerca.tipoMateriale" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:text
							property="dettTitComVO.detTitoloPFissaVO.tipoMat" size="5"
							readonly="true" title="${dettaglioTitoloForm.descTipoMat}">
						</html:text></td>
						<td width="60" class="etichetta"><bean:message
							key="ricerca.tipoRecord" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:text
							property="dettTitComVO.detTitoloPFissaVO.tipoRec" size="5"
							readonly="true" title="${dettaglioTitoloForm.descTipoRec}">
						</html:text></td>
					</tr>
				</table>
			</c:when>


			<c:otherwise>

				<c:choose>
					<c:when test="${dettaglioTitoloForm.legame51 eq 'SI'}">
						<table border="0">
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.titoloRiferimento"
									bundle="gestioneBibliograficaLabels" />:</td>
								<td width="20" class="testoNormale"><html:text
									property="areaDatiLegameTitoloVO.bidPartenza" size="10" readonly="true"
									></html:text></td>
								<td width="150" class="etichetta"><html:text
									property="areaDatiLegameTitoloVO.descPartenza" size="50" readonly="true"
									></html:text></td>
							</tr>
						</table>
						<hr color="#dde8f0" />
						<table border="0">
							<tr>
								<td width="100" class="etichetta"><bean:message
									key="dettaglio.tipoLegame" bundle="gestioneBibliograficaLabels" /></td>
								<td width="100" class="testoNormale"><html:text
									property="areaDatiLegameTitoloVO.tipoLegameNew" size="10" readonly="true"></html:text></td>
								<td width="60" class="etichetta"><bean:message
									key="dettaglio.sequenzaLegame" bundle="gestioneBibliograficaLabels" /></td>
								<td width="100" class="testoNormale"><html:text
									property="areaDatiLegameTitoloVO.sequenzaNew" size="10"></html:text></td>
							</tr>
						</table>

<!--	Inizio intervento almaviva2 BUG 3288 - 28 ottobre 2009
        Ulteriore intervento per inserire una intera riga per visualizzarla tutta 23.11.2009-->
						<table border="0">
							<tr>
								<c:choose>
									<c:when test="${dettaglioTitoloForm.areaDatiLegameTitoloVO.naturaBidArrivo eq 'N' and
													dettaglioTitoloForm.areaDatiLegameTitoloVO.naturaBidPartenza eq 'S'}">
										<td width="35" class="etichetta"><bean:message key="dettaglio.sici"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="areaDatiLegameTitoloVO.siciNew" size="80"></html:text></td>
									</c:when>
								</c:choose>
								<c:choose>
									<c:when test="${dettaglioTitoloForm.areaDatiLegameTitoloVO.naturaBidArrivo eq 'S' and
													dettaglioTitoloForm.areaDatiLegameTitoloVO.naturaBidPartenza eq 'N'}">
										<td width="35" class="etichetta"><bean:message key="dettaglio.sici"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="areaDatiLegameTitoloVO.siciNew" size="80"></html:text></td>
									</c:when>
								</c:choose>

							</tr>
						</table>
<!--	Fine intervento almaviva2 BUG 3288 - 28 ottobre 2009  -->
						<table border="0">
							<tr>
								<td width="100" class="etichetta"><bean:message
									key="dettaglio.notaLegame" bundle="gestioneBibliograficaLabels" /></td>
								<td width="100" ><html:textarea
									property="areaDatiLegameTitoloVO.noteLegameNew" cols="60" rows="1"></html:textarea></td>
							</tr>
						</table>
						<hr color="#dde8f0" />
					</c:when>
				</c:choose>

				<!--almaviva2 - agosto 2016 - gestione dei legami fra natura A e sua forma variante natura V con tipo legame 431 (A08V)   -->
				<c:choose>
					<c:when test="${dettaglioTitoloForm.legameTitUniRinvio eq 'SI'}">
						<table border="0">
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.titoloRiferimento"
									bundle="gestioneBibliograficaLabels" />:</td>
								<td width="20" class="testoNormale"><html:text
									property="areaDatiLegameTitoloVO.bidPartenza" size="10" readonly="true"
									></html:text></td>
								<td width="150" class="etichetta"><html:text
									property="areaDatiLegameTitoloVO.descPartenza" size="50" readonly="true"
									></html:text></td>
							</tr>
						</table>
						<hr color="#dde8f0" />
						<table border="0">
							<tr>
								<td width="100" class="etichetta"><bean:message
									key="dettaglio.tipoLegame" bundle="gestioneBibliograficaLabels" /></td>
								<td width="100" class="testoNormale"><html:text
									property="areaDatiLegameTitoloVO.tipoLegameNew" size="10" readonly="true"></html:text></td>
							</tr>
						</table>

						<table border="0">
							<tr>
								<td width="100" class="etichetta"><bean:message
									key="dettaglio.notaLegame" bundle="gestioneBibliograficaLabels" /></td>
								<td width="100" ><html:textarea
									property="areaDatiLegameTitoloVO.noteLegameNew" cols="60" rows="1"></html:textarea></td>
							</tr>
						</table>
						<hr color="#dde8f0" />
					</c:when>
				</c:choose>

				<table border="0">
					<tr>
						<td class="etichetta"><bean:message	key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>
						<td><html:select property="dettTitComVO.detTitoloPFissaVO.natura" style="width:40px" onchange="this.form.submit()">
							<html:optionsCollection property="listaNatura" value="codice" label="descrizioneCodice" />
						</html:select></td>
						<c:choose>
							<c:when test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'C'}">
							</c:when>

							<c:when test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'R'}">
							</c:when>

							<c:when test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'D'}">
							</c:when>
							<c:when test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'P'}">
							</c:when>
							<c:when test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'T'}">
							</c:when>
							<c:when test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'B'}">
							</c:when>
							<c:when test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'A'}">

								<td class="etichetta"><bean:message	key="ricerca.tipoMateriale" bundle="gestioneBibliograficaLabels" /></td>
								<td><html:select property="dettTitComVO.detTitoloPFissaVO.tipoMat" style="width:40px" onchange="this.form.submit()">
									<html:optionsCollection property="listaTipoMat" value="codice" label="descrizioneCodice" />
								</html:select></td>
							</c:when>

							<c:when test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'V'}">
							</c:when>

							<c:otherwise>
								<td class="etichetta"><bean:message
									key="ricerca.tipoMateriale"
									bundle="gestioneBibliograficaLabels" /></td>

<!--	Inizio modifica almaviva2 9.11.2009 BUG MANTIS 3313 per antico non si può fare cambio tipo materiale -->
								<c:choose>
									<c:when test="${not dettaglioTitoloForm.cambioNaturaAmmesso}">
										<td><html:text property="dettTitComVO.detTitoloPFissaVO.tipoMat" size="5"
												readonly="true" title="${dettaglioTitoloForm.descTipoMat}">
										</html:text></td>
									</c:when>
									<c:otherwise>
										<td><html:select property="dettTitComVO.detTitoloPFissaVO.tipoMat" style="width:40px" onchange="this.form.submit()">
											<html:optionsCollection property="listaTipoMat" value="codice" label="descrizioneCodice" />
										</html:select></td>
									</c:otherwise>
								</c:choose>
<!--	Fine modifica almaviva2 9.11.2009 BUG MANTIS 3313 per antico non si può fare cambio tipo materiale -->

								<td class="etichetta"><bean:message
									key="ricerca.tipoRecord" bundle="gestioneBibliograficaLabels" /></td>
								<td><html:select property="dettTitComVO.detTitoloPFissaVO.tipoRec" style="width:40px" onchange="this.form.submit()">
									<html:optionsCollection property="listaTipoRec" value="codice" label="descrizioneCodice" />
								</html:select></td>

							</c:otherwise>
						</c:choose>

						<td class="etichetta">
						<noscript>
							<html:submit property="methodDettaglioTit">
								<bean:message key="ricerca.button.aggiornaCanali" bundle="gestioneBibliograficaLabels" />
							</html:submit>
							</noscript>
						</td>

					</tr>
				</table>
				<hr color="#dde8f0" />
			</c:otherwise>
		</c:choose>


		<jsp:include
			page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissa.jsp" />


<!--	SBNWeb -Marzo 2016: Evolutiva almaviva2 Il trattamento di Personaggi, Interpreti e dati di rappresentazione
		viene esteso anche al Materiale Moderno e Antico -->

		<c:choose>
			<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'M'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloModAnt.jsp" />
			</c:when>
			<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'E'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloModAnt.jsp" />
			</c:when>
			<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'C'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloCartografia.jsp" />
			</c:when>
			<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'G'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloGrafica.jsp" />
			</c:when>
			<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'U'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloMusica.jsp" />
			</c:when>
			<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'H'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloAudiovisivo.jsp" />
			</c:when>
			<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'L'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloElettronico.jsp" />
			</c:when>

			<c:otherwise>

			</c:otherwise>
		</c:choose></div>

		<div id="divFooter">

			<c:choose>
				<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'COPIARET'}">
					<table align="center">
						<tr>
							<c:choose>
								<c:when test="${dettaglioTitoloForm.tipoProspettazione ne 'DET'}">
									<td align="center"><html:submit property="methodDettaglioTit">
										<bean:message key="copiaReticolo.button.okLocale"
											bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
									<td align="center"><html:submit property="methodDettaglioTit">
										<bean:message key="copiaReticolo.button.okIndice"
											bundle="gestioneBibliograficaLabels" />
									</html:submit></td>

								</c:when>
							</c:choose>
							<td align="center"><html:submit property="methodDettaglioTit">
								<bean:message key="button.annulla"
									bundle="gestioneBibliograficaLabels" />
							</html:submit></td>
						</tr>
					</table>
				</c:when>
				<c:otherwise>
					<table align="center">
						<tr>
							<c:choose>
								<c:when test="${dettaglioTitoloForm.tipoProspettazione ne 'DET'}">
									<td align="center"><html:submit property="methodDettaglioTit">
										<bean:message key="button.ok"
											bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
								</c:when>
							</c:choose>
							<td align="center"><html:submit property="methodDettaglioTit">
								<bean:message key="button.annulla"
									bundle="gestioneBibliograficaLabels" />
							</html:submit></td>

							<c:choose>
								<c:when test="${dettaglioTitoloForm.tipoProspettazione ne 'DET'}">
									<c:choose>
										<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'H'}">
											<c:choose>
												<c:when test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M' or
														dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W'}">
													<td align="center" width="350" class="etichetta"><bean:message
														key="label.numeroSupporti"
														bundle="gestioneBibliograficaLabels" />
													<html:text
														property="numSupporti" size="2"></html:text>
													<html:submit property="methodDettaglioTit">
														<bean:message key="button.calcolaArea5"
															bundle="gestioneBibliograficaLabels" />
													</html:submit></td>
												</c:when>
											</c:choose>
										</c:when>
									</c:choose>
								</c:when>
							</c:choose>

						</tr>
					</table>
				</c:otherwise>
			</c:choose>

		</div>

	</sbn:navform>
</layout:page>
<script type="text/javascript"
	src='<c:url value="/scripts/bibliografica/dettaglioTitolo.js" />'></script>