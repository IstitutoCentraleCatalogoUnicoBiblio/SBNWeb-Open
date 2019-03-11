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
	<sbn:navform action="/gestionebibliografica/titolo/sinteticaTitoli.do">
		<div id="divForm">

			<div id="divMessaggio">
			<sbn:errors  />
			<c:if test="${!empty requestScope.SCHEDA_LINK}">
				<html:link page="/downloadBatch.do?FILEID=${requestScope.SCHEDA_LINK.base64Link}">${requestScope.SCHEDA_LINK.nomeFileVisualizzato}</html:link>
 			</c:if>
		</div>

			<c:choose>
				<c:when test="${sinteticaTitoliForm.prospettaDatiOggColl eq 'SI'}">
					<table border="0">
						<tr>
							<td class="etichetta">
								<bean:message key="ricerca.titoloRiferimento"
									bundle="gestioneBibliograficaLabels" />
								:
							</td>
							<td width="20" class="testoNormale">
								<html:text property="idOggColl" size="10" readonly="true"></html:text>
							</td>
							<td width="150" class="etichetta">
								<html:text property="descOggColl" size="50" readonly="true"></html:text>
							</td>
						</tr>
					</table>
					<hr color="#dde8f0" />
				</c:when>
			</c:choose>

			<sbn:blocchi numBlocco="numBlocco" numNotizie="numNotizie"
				parameter="methodSinteticaTit" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe"
				livelloRicerca="${sinteticaTitoliForm.livRicerca}" />


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
						<bean:message key="sintetica.isbdlegami"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta"></th>
					<c:if test="${sinteticaTitoliForm.tipologiaTastiera ne 'ProspettazionePerServizi'
										and sinteticaTitoliForm.tipologiaTastiera ne 'ProspettazionePerMovimentiUtente'}">
						<th class="etichetta"></th>
					</c:if>
				</tr>

				<logic:iterate id="item" property="listaSintetica"
					name="sinteticaTitoliForm" indexId="riga">
					<sbn:rowcolor var="color" index="riga" />
					<tr class="testoNormale" bgcolor="#FEF1E2">
						<td bgcolor="${color}">
							<sbn:anchor name="item" property="progressivo" />
							<sbn:linkbutton index="bid" name="item" value="progressivo"
								key="button.analiticaTit" bundle="gestioneBibliograficaLabels"
								title="Analitica" property="selezRadio" />
						</td>
						<td valign="middle" bgcolor="${color}">
							<html:img page="/images/${item.imageUrl}" style="width:32px; height:32px" />
						</td>
						<td bgcolor="${color}">
							<sbn:linkwrite name="item" property="tipoRecDescrizioneLegami"
								keyProperty="selezRadio" buttonKey="button.analiticaTit"
								bundle="gestioneBibliograficaLabels" title="Analitica" />
						</td>
						<td bgcolor="${color}">
							<html:radio property="selezRadio" value="${item.bid}" />
						</td>
						<c:if test="${sinteticaTitoliForm.tipologiaTastiera ne 'ProspettazionePerServizi' and
								sinteticaTitoliForm.tipologiaTastiera ne 'ProspettazionePerMovimentiUtente' and
								sinteticaTitoliForm.tipologiaTastiera ne 'UtilizzoPerGestioneLegamiEditTitoli'}">
							<td bgcolor="${color}">
								<html:multibox property="selezCheck" value="${item.bid}" />
								<html:hidden property="selezCheck" value="" />
							</td>
						</c:if>
					</tr>
				</logic:iterate>

			</table>



		</div>

		<div id="divFooterCommon">
			<sbn:blocchi numBlocco="numBlocco" numNotizie="numNotizie"
				parameter="methodSinteticaTit" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe" bottom="true" />
		</div>

		<div id="divFooter">
			<c:choose>
				<c:when
					test="${sinteticaTitoliForm.tipologiaTastiera eq 'ConfermaFusione'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestSimili.fusione"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.rinunciaFusione"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>
				<c:when
					test="${sinteticaTitoliForm.tipologiaTastiera eq 'ProspettazioneSimili'
					or sinteticaTitoliForm.tipologiaTastiera eq 'ProspettazioneSimiliPerCattura51'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestSimili.riAggiorna"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestSimili.confermaAgg"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<c:choose>
								<c:when test="${sinteticaTitoliForm.idOggColl eq ''}">
									<td align="center">
										<html:submit property="methodSinteticaTit">
											<bean:message key="button.gestSimili.cattura"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
								<c:otherwise>
									<td align="center">
										<html:submit property="methodSinteticaTit">
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
					test="${sinteticaTitoliForm.tipologiaTastiera eq 'ProspettazioneSimiliSenzaCattura'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestSimili.riAggiorna"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestSimili.confermaAgg"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>

				<c:when
					test="${sinteticaTitoliForm.tipologiaTastiera eq 'ProspettazioneSimiliPerCopiaSpoglio'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestSimili.copiaSpoglio"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>
				<c:when
					test="${sinteticaTitoliForm.tipologiaTastiera eq 'UtilizzoComeSif'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

							<td class="etichetta">
								<bean:message key="label.esamina" bundle="gestioneBibliograficaLabels" />
								<html:select property="esaminaTitSelez">
									<html:optionsCollection property="listaEsaminaTit"
										value="descrizione" label="descrizione" />
								</html:select>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.esamina"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<layout:combo bundle="gestioneBibliograficaLabels"
									label="button.gestione" name="sinteticaTitoliForm"
									button="button.confermaGestisci" property="gestisciTitSelez"
									combo="listaGestisciTit" parameter="methodSinteticaTit" />
							</td>
							<td align="right">
								<html:submit styleClass="buttonSelezTutti"
									property="methodSinteticaTit" title="Seleziona tutto">
									<bean:message key="button.selAllTitoli"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
								<html:submit styleClass="buttonSelezNessuno"
									property="methodSinteticaTit" title="Deseleziona tutto">
									<bean:message key="button.deSelAllTitoli"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>


				<c:when
					test="${sinteticaTitoliForm.tipologiaTastiera eq 'ProspettazionePerIdGestionali'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

							<td class="etichetta">
								<bean:message key="label.esamina" bundle="gestioneBibliograficaLabels" />
								<html:select property="esaminaTitSelez">
									<html:optionsCollection property="listaEsaminaTit"
										value="descrizione" label="descrizione" />
								</html:select>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.esamina"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<layout:combo bundle="gestioneBibliograficaLabels"
									label="button.gestione" name="sinteticaTitoliForm"
									button="button.confermaGestisci" property="gestisciTitSelez"
									combo="listaGestisciTit" parameter="methodSinteticaTit" />
							</td>
							<td align="right">
								<html:submit styleClass="buttonSelezTutti"
									property="methodSinteticaTit" title="Seleziona tutto">
									<bean:message key="button.selAllTitoli"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
								<html:submit styleClass="buttonSelezNessuno"
									property="methodSinteticaTit" title="Deseleziona tutto">
									<bean:message key="button.deSelAllTitoli"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>




				<c:when
					test="${sinteticaTitoliForm.tipologiaTastiera eq 'ProspettazionePerServizi' or
							sinteticaTitoliForm.tipologiaTastiera eq 'ProspettazionePerMovimentiUtente'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestPerServizi.scegli"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>

				<c:when
					test="${sinteticaTitoliForm.tipologiaTastiera eq 'UtilizzoPerGestioneLegamiEditTitoli'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestLegamiEditTitoli.scegli"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestLegamiEditTitoli.cancella"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestLegamiEditTitoli.cercaEditore"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>



				<c:when
					test="${sinteticaTitoliForm.tipologiaTastiera eq 'ProspettazionePerAcquisizioni'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestPerAcquisizioni.scegli"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>


							<!-- Modifica Mantis BUG 3718 almaviva2 27.07.2010 - esamina non deve essere presente
							<td class="etichetta">
								<bean:message key="label.esamina" bundle="gestioneBibliograficaLabels" />
								<html:select property="esaminaTitSelez">
									<html:optionsCollection property="listaEsaminaTit"
										value="descrizione" label="descrizione" />
								</html:select>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.esamina"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							-->


						</tr>
					</table>
				</c:when>

				<c:when
					test="${sinteticaTitoliForm.tipologiaTastiera eq 'ProspettazionePerFusioneOnLine'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestSimiliCondividi.catturaEFondi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.rinunciaFusione"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.annulla"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

							</td>
						</tr>
					</table>
				</c:when>



				<c:when
					test="${sinteticaTitoliForm.tipologiaTastiera eq 'ProspettaSimiliPerCondividi'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestSimiliCondividi.catturaEFondi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestSimiliCondividi.tornaAnaliticaPerCondividi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.annulla"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>


				<c:when
					test="${sinteticaTitoliForm.tipologiaTastiera eq 'ProspettaSimiliPerCondividiNoRadice'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestSimiliCondividi.catturaESpostaLegame"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.gestSimiliCondividi.tornaAnaliticaPerCondividi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaTit">
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
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<c:choose>
								<c:when test="${sinteticaTitoliForm.livRicerca eq 'P'}">
									<td align="center">
										<html:submit property="methodSinteticaTit">
											<bean:message key="button.cercaIndice"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
								<c:when test="${sinteticaTitoliForm.livRicerca eq 'I' and sinteticaTitoliForm.creaDoc eq 'SI'}">
									<td align="center">
										<html:submit property="methodSinteticaTit">
											<bean:message key="button.creaTit"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
							</c:choose>

							<c:choose>
								<c:when test="${sinteticaTitoliForm.creaDocLoc eq 'SI'}">
									<td align="center">
										<html:submit property="methodSinteticaTit">
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
								<html:submit property="methodSinteticaTit">
									<bean:message key="button.esamina"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

							<c:choose>
								<c:when
									test="${sinteticaTitoliForm.prospettazionePerLegami eq 'SI'}">
									<td align="center">
										<html:submit property="methodSinteticaTit">
											<bean:message key="button.gestLegami.lega"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
								<c:otherwise>
									<td align="center">
									<layout:combo bundle="gestioneBibliograficaLabels"
										label="button.gestione" name="sinteticaTitoliForm"
										button="button.confermaGestisci" property="gestisciTitSelez"
										combo="listaGestisciTit" parameter="methodSinteticaTit" />
									</td>

								</c:otherwise>
							</c:choose>

							<td align="right">
								<html:submit styleClass="buttonSelezTutti"
									property="methodSinteticaTit" title="Seleziona tutto">
									<bean:message key="button.selAllTitoli"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
								<html:submit styleClass="buttonSelezNessuno"
									property="methodSinteticaTit" title="Deseleziona tutto">
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
