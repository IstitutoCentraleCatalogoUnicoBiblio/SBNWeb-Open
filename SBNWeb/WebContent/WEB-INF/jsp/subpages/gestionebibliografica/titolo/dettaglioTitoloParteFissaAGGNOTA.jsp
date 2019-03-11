<!--	SBNWeb - Rifacimento ClientServer
		dettaglioTitoloParteFissaAGGNOTA
		JSP di interrogazione titolo - Area superiore con canali e filtri
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

<c:choose>
	<c:when
		test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'D'}">
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.natura" size="5"
					readonly="true" title="${dettaglioTitoloForm.descNatura}">
				</html:text></td>
				<td width="60" class="etichetta"><bean:message key="ricerca.bid"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.bid" size="10"
					readonly="true"></html:text></td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.livelloAutorita"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="400" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.livAut" size="5"
					readonly="true" title="${dettaglioTitoloForm.descLivAut}">
				</html:text></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message
					key="catalogazione.areaT.titolo"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo" cols="70"
					rows="5" readonly="true"></html:textarea></td>
			</tr>
		</table>
	</c:when>
	<c:when
		test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'P'}">
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.natura" size="5"
					readonly="true" title="${dettaglioTitoloForm.descNatura}">
				</html:text></td>
				<td width="60" class="etichetta"><bean:message key="ricerca.bid"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.bid" size="10"
					readonly="true"></html:text></td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.livelloAutorita"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="400" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.livAut" size="5"
					readonly="true" title="${dettaglioTitoloForm.descLivAut}">
				</html:text></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message
					key="catalogazione.areaT.titolo"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo" cols="70"
					rows="5" readonly="true"></html:textarea></td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
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
				<td width="60" class="etichetta"><bean:message key="ricerca.bid"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.bid" size="10"
					readonly="true"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.livelloAutorita"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.livAut" size="5"
					readonly="true" title="${dettaglioTitoloForm.descLivAut}">
				</html:text></td>
				<td width="60" class="etichetta"><bean:message
					key="ricerca.tipoRecord" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.tipoRec" size="5"
					readonly="true" title="${dettaglioTitoloForm.descTipoRec}">
				</html:text></td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.paese" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.paese" size="5"
					readonly="true" title="${dettaglioTitoloForm.descPaese}">
				</html:text></td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.lingua" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.lingua1" size="5"
					readonly="true" title="${dettaglioTitoloForm.descLingua1}">
				</html:text></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.lingua2" size="5"
					readonly="true" title="${dettaglioTitoloForm.descLingua2}">
				</html:text></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.lingua3" size="5"
					readonly="true" title="${dettaglioTitoloForm.descLingua3}">
				</html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.genere" bundle="gestioneBibliograficaLabels" /></td>

				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.genere1" size="5"
					readonly="true" title="${dettaglioTitoloForm.descGenere1}">
				</html:text></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.genere2" size="5"
					readonly="true" title="${dettaglioTitoloForm.descGenere2}">
				</html:text></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.genere3" size="5"
					readonly="true" title="${dettaglioTitoloForm.descGenere3}">
				</html:text></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.genere4" size="5"
					readonly="true" title="${dettaglioTitoloForm.descGenere4}">
				</html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.tipo.Data" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.tipoData" size="5"
					readonly="true" title="${dettaglioTitoloForm.descTipoData}">
				</html:text></td>
				<td width="60" class="etichetta"><bean:message
					key="ricerca.dataPubblicazione1"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="40" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.dataPubbl1" size="15"
					readonly="true"></html:text></td>
				<td width="60" class="etichetta"><bean:message
					key="ricerca.dataPubblicazione2"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="40" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.dataPubbl2" size="15"
					readonly="true"></html:text></td>
			</tr>
		</table>

		<!--  almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S e Materiale Moderno/Antico-->
			<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'M'
							or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'E'}">
				<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'S'}">
						<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaArea0DETTAGLIO.jsp" />
				</c:if>
			</c:if>


		<c:choose>
			<c:when
				test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'C'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="catalogazione.areaT.titolo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo"
							cols="70" rows="5" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.edizione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitEdiz" cols="70"
							rows="1" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.pubblicazione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitPubbl"
							cols="70" rows="3" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.descrFisica"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitDescrFis"
							cols="70" rows="1" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.note"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote" cols="70"
							rows="7"></html:textarea> <sbn:tastiera limit="1200"
								property="dettTitComVO.detTitoloPFissaVO.areaTitNote"
								name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>


				</table>

			</c:when>
			<c:when
				test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'T'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="catalogazione.areaT.titolo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="400"><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo"
							cols="70" rows="5" readonly="true"></html:textarea></td>
					</tr>
				</table>
			</c:when>
			<c:when
				test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'B'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="catalogazione.areaT.titolo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="400"><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo"
							cols="70" rows="5" readonly="true"></html:textarea></td>
					</tr>
				</table>
			</c:when>

			<c:otherwise>
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="catalogazione.areaT.titolo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo"
							cols="70" rows="5" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.edizione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitEdiz" cols="70"
							rows="1" readonly="true"></html:textarea></td>
					</tr>
					<c:choose>
						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat ne 'E'}">
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.musica"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitMusica"
									cols="70" rows="1" readonly="true"></html:textarea></td>
							</tr>

							<c:choose>
								<c:when
									test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'S'}">
									<tr>
										<td class="etichetta"><bean:message
											key="catalogazione.areaT.numerazione"
											bundle="gestioneBibliograficaLabels" /></td>
										<td><html:textarea
											property="dettTitComVO.detTitoloPFissaVO.areaTitNumer"
											cols="70" rows="1" readonly="true"></html:textarea></td>
									</tr>
								</c:when>
							</c:choose>
						</c:when>
					</c:choose>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.pubblicazione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitPubbl"
							cols="70" rows="3" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.descrFisica"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitDescrFis"
							cols="70" rows="1" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.note"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote" cols="70"
							rows="7"></html:textarea><sbn:tastiera limit="1200"
								property="dettTitComVO.detTitoloPFissaVO.areaTitNote"
								name="dettaglioTitoloForm"></sbn:tastiera>
							</td>
					</tr>
				</table>

			</c:otherwise>
		</c:choose>

		<c:choose>
			<c:when
				test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.presenzaNumSt eq 'SI'}">
				<table border="0">
					<tr>
						<th width="100" class="etichetta"><bean:message
							key="ricerca.num.standard" bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="ricerca.numero" bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="ricerca.tipo" bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
					</tr>
					<logic:iterate id="item"
						property="dettTitComVO.detTitoloPFissaVO.listaNumStandard"
						name="dettaglioTitoloForm">
						<tr class="testoNormale">
							<td></td>
							<td bgcolor="#FFCC99"><bean-struts:write name="item"
								property="campoUno" /></td>
							<td bgcolor="#FFCC99"><bean-struts:write name="item"
								property="campoDue" /></td>
							<td bgcolor="#FFCC99"><bean-struts:write name="item"
								property="nota" /></td>
						</tr>
					</logic:iterate>
				</table>
			</c:when>
			<c:when
				test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.presenzaImpron eq 'SI'}">
				<table border="0">
					<tr>
						<th width="100" class="etichetta"><bean:message
							key="ricerca.impronta" bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="sintetica.progr" bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="ricerca.impronta" bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
					</tr>
					<logic:iterate id="item"
						property="dettTitComVO.detTitoloPFissaVO.listaImpronte"
						name="dettaglioTitoloForm">
						<tr class="testoNormale">
							<td></td>
							<td bgcolor="#FFCC99"><bean-struts:write name="item"
								property="campoUno" /></td>
							<td bgcolor="#FFCC99"><bean-struts:write name="item"
								property="campoDue" /></td>
							<td bgcolor="#FFCC99"><bean-struts:write name="item"
								property="nota" /></td>
						</tr>
					</logic:iterate>
				</table>
			</c:when>
		</c:choose>
	</c:otherwise>
</c:choose>
