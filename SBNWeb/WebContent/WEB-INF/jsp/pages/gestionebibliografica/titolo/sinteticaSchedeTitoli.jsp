<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Autore
		almaviva2 - Inizio Codifica Agosto 2006
-->

<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionebibliografica/titolo/sinteticaSchedeTitoli.do">
		<div id="divForm">

			<div id="divMessaggio">
			<sbn:errors  />
			<c:if test="${!empty requestScope.SCHEDA_LINK}">
				<html:link page="/downloadBatch.do?FILEID=${requestScope.SCHEDA_LINK.base64Link}">${requestScope.SCHEDA_LINK.nomeFileVisualizzato}</html:link>
 			</c:if>
		</div>

			<table border="0">
				<tr>
					<td class="etichetta"><bean:message key="ricerca.listeConf.statoLavorRecordDesc" bundle="gestioneBibliograficaLabels" /> :</td>
					<td ><html:text property="statoLavorRecordDesc" readonly="true" ></html:text></td>
					<td class="etichetta"><bean:message key="ricerca.listeConf.statoConfrontoDesc" bundle="gestioneBibliograficaLabels" /> :</td>
					<td ><html:text property="statoConfrontoDesc" readonly="true" ></html:text></td>
				</tr>
			</table>



			<c:if test="${sinteticaSchedeTitoliForm.areaDatiPassCiclicaVO.messaggisticaDiLavorazione ne ''}">
				<table border="0">
					<tr>
						<td ><html:textarea property="areaDatiPassCiclicaVO.messaggisticaDiLavorazione"  cols="150" rows="2" readonly="true"></html:textarea></td>
					</tr>
				</table>
			</c:if>


			<table border="0">
				<tr>
					<td class="etichetta">
						<bean:message key="ricerca.titoloRiferimentoLocale" bundle="gestioneBibliograficaLabels" /> :
					</td>
				</tr>
			</table>

			<table class="sintetica">
				<tr bgcolor="#dde8f0">
					<th class="etichetta">
						<bean:message key="sintetica.tipol"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sinteticaSchede.descLegami" bundle="gestioneBibliograficaLabels" />
					</th>
				</tr>

				<logic:iterate id="item" property="areaDatiPassCiclicaVO.listaSchedaIdLocale"
					name="sinteticaSchedeTitoliForm" indexId="riga">
					<sbn:rowcolor var="color" index="riga" />
					<tr class="testoNormale" bgcolor="#FEF1E2">
						<td valign="middle" bgcolor="${color}">
							<html:img page="/images/${item.imageUrl}" />
						</td>
						<td bgcolor="${color}">
							<sbn:linkwrite name="item" property="tipoRecDescrizioneLegami"
								keyProperty="selezRadio" buttonKey="button.analiticaTit"
								bundle="gestioneBibliograficaLabels" title="Analitica" />
						</td>
					</tr>
				</logic:iterate>

			</table>




			<c:if test="${sinteticaSchedeTitoliForm.areaDatiPassCiclicaVO.statoConfronto ne 'N' or
							sinteticaSchedeTitoliForm.numNotizie > 0}">

					<hr color="#dde8f0" />

				<sbn:blocchi numBlocco="numBlocco" numNotizie="numNotizie"
					parameter="methodSinteticaSchedeTit" totBlocchi="totBlocchi"
					elementiPerBlocco="maxRighe"
					livelloRicerca="${sinteticaSchedeTitoliForm.livRicerca}" />

				<table class="sintetica">
					<tr bgcolor="#dde8f0">
						<th class="etichetta">
							<bean:message key="sintetica.progr"
								bundle="gestioneBibliograficaLabels" />
						</th>
						<th class="etichetta">
							<bean:message key="sintetica.tipol"
								bundle="gestioneBibliograficaLabels" />
						</th>
						<th class="etichetta">
							<bean:message key="sinteticaSchede.descLegami"
								bundle="gestioneBibliograficaLabels" />
						</th>
						<th class="etichetta"></th>
					</tr>

					<logic:iterate id="item" property="listaSintetica"
						name="sinteticaSchedeTitoliForm" indexId="riga">
						<sbn:rowcolor var="color" index="riga" />
						<tr class="testoNormale" bgcolor="#FEF1E2">
							<td bgcolor="${color}">
								<sbn:anchor name="item" property="progressivo" />
								<sbn:linkbutton index="bid" name="item" value="progressivo"
									key="button.analiticaTit" bundle="gestioneBibliograficaLabels"
									title="Analitica" property="selezRadio" />
							</td>
							<td valign="middle" bgcolor="${color}">
								<html:img page="/images/${item.imageUrl}" />
							</td>
							<td bgcolor="${color}">
								<sbn:linkwrite name="item" property="tipoRecDescrizioneLegami"
									keyProperty="selezRadio" buttonKey="button.analiticaTit"
									bundle="gestioneBibliograficaLabels" title="Analitica" />
							</td>
							<td bgcolor="${color}">
								<html:radio property="selezRadio" value="${item.bid}" />
							</td>
						</tr>
					</logic:iterate>

				</table>
 			</c:if>

		</div>

		<div id="divFooterCommon">
			<sbn:blocchi numBlocco="numBlocco" numNotizie="numNotizie"
				parameter="methodSinteticaSchedeTit" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe" bottom="true" />
		</div>

		<div id="divFooter">
			<c:choose>
				<c:when
					test="${sinteticaSchedeTitoliForm.tipologiaTastiera eq 'ConfermaFusione'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.gestSimili.fusione"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.rinunciaFusione"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>
				<c:when
					test="${sinteticaSchedeTitoliForm.tipologiaTastiera eq 'ProspettazioneSimili'
					or sinteticaSchedeTitoliForm.tipologiaTastiera eq 'ProspettazioneSimiliPerCattura51'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.gestSimili.riAggiorna"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.gestSimili.confermaAgg"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<c:choose>
								<c:when test="${sinteticaSchedeTitoliForm.idOggColl eq ''}">
									<td align="center">
										<html:submit property="methodSinteticaSchedeTit">
											<bean:message key="button.gestSimili.cattura"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
								<c:otherwise>
									<td align="center">
										<html:submit property="methodSinteticaSchedeTit">
											<bean:message key="button.gestSimili.fusione"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:otherwise>
							</c:choose>
						</tr>
					</table>
				</c:when>

				<c:when
					test="${sinteticaSchedeTitoliForm.tipologiaTastiera eq 'ProspettazioneSimiliSenzaCattura'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.gestSimili.riAggiorna"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.gestSimili.confermaAgg"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>


				<c:when
					test="${sinteticaSchedeTitoliForm.tipologiaTastiera eq 'ProspettazionePerFusioneOnLine'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.gestSimiliCondividi.catturaEFondi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.rinunciaFusione"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.annulla"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

							</td>
						</tr>
					</table>
				</c:when>



				<c:when
					test="${sinteticaSchedeTitoliForm.tipologiaTastiera eq 'ProspettaSimiliPerCondividi'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.gestSimiliCondividi.catturaEFondi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.gestSimiliCondividi.tornaAnaliticaPerCondividi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.annulla"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>

				<c:when
					test="${sinteticaSchedeTitoliForm.tipologiaTastiera eq 'ProspettaListeDiConfronto'}">
					<table align="center">
						<tr>

							<c:if test="${sinteticaSchedeTitoliForm.areaDatiPassCiclicaVO.statoConfronto ne 'N'
							and sinteticaSchedeTitoliForm.areaDatiPassCiclicaVO.statoLavorRecord ne '4'}">
								<td align="center">
									<html:submit property="methodSinteticaSchedeTit">
										<bean:message key="button.gestSimiliCondividi.catturaEFondi"
											bundle="gestioneBibliograficaLabels" />
									</html:submit>
								</td>
								<td align="center">
									<html:submit property="methodSinteticaSchedeTit">
										<bean:message key="button.salvaSuFile"
											bundle="gestioneBibliograficaLabels" />
									</html:submit>
								</td>
							</c:if>

							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.listeConf.successivo"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.annulla"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<c:if test="${sinteticaSchedeTitoliForm.areaDatiPassCiclicaVO.statoLavorRecord ne '3'}">
								<td align="center">
									<html:submit property="methodSinteticaSchedeTit">
										<bean:message key="button.escludiOggettoDoc"
											bundle="gestioneBibliograficaLabels" />
									</html:submit>
								</td>
							</c:if>
						</tr>
					</table>
				</c:when>

				<c:when
					test="${sinteticaSchedeTitoliForm.tipologiaTastiera eq 'ProspettaSimiliPerCondividiNoRadice'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.gestSimiliCondividi.catturaESpostaLegame"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.gestSimiliCondividi.tornaAnaliticaPerCondividi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.annulla"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>

				<c:otherwise>
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<c:choose>
								<c:when test="${sinteticaSchedeTitoliForm.livRicerca eq 'P'}">
									<td align="center">
										<html:submit property="methodSinteticaSchedeTit">
											<bean:message key="button.cercaIndice"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
								<c:when test="${sinteticaSchedeTitoliForm.livRicerca eq 'I' and sinteticaSchedeTitoliForm.creaDoc eq 'SI'}">
									<td align="center">
										<html:submit property="methodSinteticaSchedeTit">
											<bean:message key="button.creaTit"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
							</c:choose>

							<c:choose>
								<c:when test="${sinteticaSchedeTitoliForm.creaDocLoc eq 'SI'}">
									<td align="center">
										<html:submit property="methodSinteticaSchedeTit">
											<bean:message key="button.creaTitLoc"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
							</c:choose>

							<td class="etichetta">
								<bean:message key="label.esamina" bundle="gestioneBibliograficaLabels" />
								<html:select property="esaminaTitSelez">
									<html:optionsCollection property="listaEsaminaTit"
										value="descrizione" label="descrizione" />
								</html:select>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaSchedeTit">
									<bean:message key="button.esamina"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

							<c:choose>
								<c:when
									test="${sinteticaSchedeTitoliForm.prospettazionePerLegami eq 'SI'}">
									<td align="center">
										<html:submit property="methodSinteticaSchedeTit">
											<bean:message key="button.gestLegami.lega"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
								<c:otherwise>
									<td align="center">
									<layout:combo bundle="gestioneBibliograficaLabels"
										label="button.gestione" name="sinteticaSchedeTitoliForm"
										button="button.confermaGestisci" property="gestisciTitSelez"
										combo="listaGestisciTit" parameter="methodSinteticaSchedeTit" />
									</td>
								</c:otherwise>
							</c:choose>

							<td align="right">
								<html:submit styleClass="buttonSelezTutti"
									property="methodSinteticaSchedeTit" title="Seleziona tutto">
									<bean:message key="button.selAllTitoli"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
								<html:submit styleClass="buttonSelezNessuno"
									property="methodSinteticaSchedeTit" title="Deseleziona tutto">
									<bean:message key="button.deSelAllTitoli"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>

</layout:page>
