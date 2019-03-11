<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Tipo materiale Moderno/Antico
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

<html:xhtml/>
<layout:page>

	<sbn:navform
		action="/gestionebibliografica/utility/richiestaAllineamenti.do" enctype="multipart/form-data">


		<div id="divForm">

			<div id="divMessaggio"><sbn:errors /></div>

				<c:choose>
					<c:when test="${richiestaAllineamentiForm.statoAvanzamento eq '0'}">

						<c:choose>
							<c:when test="${richiestaAllineamentiForm.tipoProspettazione eq 'IA000'}">
								<table border="0"  align="center" class="etichetta">
									<tr><td align="center"></td></tr>
									<tr><td align="center">ALLA PRESSIONE DEL TASTO "Prenota" VERRA' INVIATA LA RICHIESTA DI SCHEDULAZIONE</td></tr>
									<tr><td align="center">DEL BATCH DI ALLINEAMENTO DELLA BASE INFORMATIVA LOCALE AL SISTEMA CENTRALE;</td></tr>
									<tr><td align="center">- SE SI IMPOSTA UNA TIPOLOGIA DI MATERIALE L'ALLINEAMENTO DEI DOCUMENTI SARA' LIMITATO AL TIPO MATERIALE RICHIESTO E VERRA' SEGUITO</td></tr>
									<tr><td align="center">DALL'ALLINEAMENTO DEGLI AUTORI, POI DELLE MARCHE;</td></tr>
									<tr><td align="center">- SE SI IMPOSTA UNA DATA DA VERRANNO FILTRATI GLI ALLINEAMENTI PER LA DATA RICHIESTA; SE SI IMPOSTA ANCHE LA DATA A VERRANNO FILTRATI</td></tr>
									<tr><td align="center">GLI ALLINEAMENTI PER L'INTERVALLO DI DATE RICHIESTE;</td></tr>
									<tr><td align="center">- SE SI VALORIZZA IL CAMPO "Identificativo lista allineamento" LA PROCEDURA RICERCA IL FILE RICHIESTO SUL SERVER CENTRALE (INDICE)</td></tr>
									<tr><td align="center">ED ALLINEA SOLO I RETICOLI PRESENTI SU TALE FILE;</td></tr>

									<!--   almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
										// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
										// del flag di avvenuto allineamento.-->
									<sbn:checkAttivita idControllo="allineamentoDaFileLocale">
										<tr><td align="center">- SE SI VALORIZZA IL CAMPO "Identificativo lista allineamento da file locale" LA PROCEDURA RICERCA IL FILE RICHIESTO SUL SERVER</td></tr>
										<tr><td align="center">DEL POLO ED ALLINEA SOLO I RETICOLI PRESENTI SU TALE FILE;</td></tr>
									</sbn:checkAttivita>

									<tr><td align="center"></td></tr>
								</table>

								<table border="0"  align="center" class="etichetta">
									<tr>
										<td>
										</td>
										<td>
											<table border="0">
												<tr>
													<td class="etichetta"><bean:message
														key="ricerca.tipoMateriale" bundle="gestioneBibliograficaLabels" />:</td>

													<td class="etichetta"><bean:message key="ricerca.tabTutti" bundle="gestioneBibliograficaLabels" />
														<html:radio	property="tipoMatSelez" value="*"/></td>
													<td class="etichetta"><bean:message key="ricerca.tabModerno" bundle="gestioneBibliograficaLabels" />
														<html:radio	property="tipoMatSelez" value="M"/></td>
													<td class="etichetta"><bean:message key="ricerca.tabAntico" bundle="gestioneBibliograficaLabels" />
														<html:radio	property="tipoMatSelez" value="E"/></td>
													<td class="etichetta"><bean:message key="ricerca.tabCartografia" bundle="gestioneBibliograficaLabels" />
														<html:radio	property="tipoMatSelez" value="C"/></td>
													<td class="etichetta"><bean:message key="ricerca.tabGrafica" bundle="gestioneBibliograficaLabels" />
														<html:radio	property="tipoMatSelez" value="G"/></td>
													<td class="etichetta"><bean:message key="ricerca.tabMusica" bundle="gestioneBibliograficaLabels" />
														<html:radio	property="tipoMatSelez" value="U"/></td>

													<td class="etichetta"><bean:message key="ricerca.tabAudiovisivo" bundle="gestioneBibliograficaLabels" />
													<html:radio	property="tipoMatSelez" value="H"/></td>
													<td class="etichetta"><bean:message key="ricerca.tabElettronico" bundle="gestioneBibliograficaLabels" />
													<html:radio	property="tipoMatSelez" value="L"/></td>
												</tr>
											</table>
											<table border="0" class="etichetta">
												<tr>
							                        <td class="etichetta"><bean:message  key="label.allineam.dataDa" bundle="gestioneBibliograficaLabels" />
											 		  	<html:text styleId="testoNormale" property="dataAllineaDa" size="10"></html:text>
							                        </td>
							                        <td class="etichetta"><bean:message  key="label.allineam.dataA" bundle="gestioneBibliograficaLabels" />
											 		  <html:text styleId="testoNormale" property="dataAllineaA" size="10"></html:text>
							                        </td>
												</tr>
											</table>
											<table border="0" class="etichetta">
												<tr>
													<td class="etichetta"><bean:message	key="label.allineam.idLista"	bundle="gestioneBibliograficaLabels" />
														<html:text property="idFileAllineamenti" size="20" maxlength="20"></html:text>
													</td>
												</tr>
											</table>

											<!--   almaviva2 Ottobre 2017: migrazione POLO PCM: procedura di allineamento offLine (Indice spento) con caricamento
												// del file lista dal server locale senza inviare ad Indice ne le localizzazioni, ne la richiesta di aggiornamento
												// del flag di avvenuto allineamento.-->

											<sbn:checkAttivita idControllo="allineamentoDaFileLocale">
												<table border="0" class="etichetta">
													<tr>
														<td class="etichetta"><bean:message	key="label.allineamLocale.idLista" bundle="gestioneBibliograficaLabels" />
															<html:text property="idFileLocaleAllineamenti" size="20" maxlength="20"></html:text>
														</td>
														<sbn:checkAttivita idControllo="ALLINEAMENTI_SALVA_XML_INDICE">
															<td class="etichetta">
																<bean:message key="label.allinea.fileLocale.scarica" bundle="gestioneBibliograficaLabels" />
																<html:radio	property="fileXmlTipoOperazione" value="SALVA"/>
															</td>
															<td class="etichetta">
																<bean:message key="label.allinea.fileLocale.elabora" bundle="gestioneBibliograficaLabels" />
																<html:radio	property="fileXmlTipoOperazione" value="ALLINEA"/>
															</td>
														</sbn:checkAttivita>
													</tr>
												</table>
											</sbn:checkAttivita>
										</td>
									</tr>
								</table>

								<table border="0"  align="center" class="etichetta">
									<tr><td align="center"></td></tr>
									<tr><td align="center">
									<table align="center">
										<tr>
											<td align="center">
												<html:submit property="methodRichAllineamenti">
												<bean:message key="button.richiestaBatchAllineamenti" bundle="gestioneBibliograficaLabels" />
											</html:submit></td>
										</tr>
									</table>
									</td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
								</table>
							</c:when>

							<c:when test="${richiestaAllineamentiForm.tipoProspettazione eq 'IA004'}">
								<table border="0"  align="center" class="etichetta">
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">ALLA PRESSIONE DEL TASTO "Prenotazione batch cattura massiva"  VERRA' INVIATA LA RICHIESTA DI SCHEDULAZIONE</td></tr>
									<tr><td align="center">DEL BATCH CHE EFFETTUERA' LA CATTURA DEGLI OGGETTI I CUI BID SONO PRESENTI NELLA LISTA SELEZIONATA;</td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">
										<table align="center">
											<tr>
												<td class="etichetta">Carica file:&nbsp; <html:file property="uploadImmagine" />
												</td>
												<td class="etichetta">
													<html:submit property="methodRichAllineamenti">
														<bean:message key="button.caricaFileIdCatturaMassiva" bundle="gestioneBibliograficaLabels" />
													</html:submit>
												</td>
												<td align="center">
													<html:submit property="methodRichAllineamenti">
													<bean:message key="button.richiestaCatturaMassiva" bundle="gestioneBibliograficaLabels" />
													</html:submit>
												</td>
											</tr>
										</table>
										</td></tr>
								</table>
							</c:when>
							<c:when test="${richiestaAllineamentiForm.tipoProspettazione eq 'IA005'}">
								<table border="0"  align="center" class="etichetta">
								<tr><td align="center"></td></tr>
								<tr><td align="center"></td></tr>
								<tr><td align="center"></td></tr>
								<tr><td align="center">ALLA PRESSIONE DEL TASTO "Allinea Repertori base dati Locale" VERRA' EFFETTUATO L'AGGIORNAMENTO DELL'ARCHIVIO DEI REPERTORI LOCALE</td></tr>
								<tr><td align="center">AL SISTEMA CENTRALE;L'ALLINEAMENTO AVVERRA' IMMEDIATAMENTE</td></tr>
								<tr><td align="center"></td></tr>
								<tr><td align="center"></td></tr>
								<tr><td align="center">
									<c:choose>
										<c:when test="${richiestaAllineamentiForm.statoAvanzamento eq '0'}">
											<table align="center">
												<tr>
													<td align="center">
														<html:submit property="methodRichAllineamenti">
														<bean:message key="button.richiestaAllineaRepertori" bundle="gestioneBibliograficaLabels" />
													</html:submit></td>
												</tr>
											</table>
										</c:when>
									</c:choose>
								</td></tr>
							</table>
							</c:when>
							<c:when test="${richiestaAllineamentiForm.tipoProspettazione eq 'IF010'}">
								<table border="0"  align="center" class="etichetta">
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">ALLA PRESSIONE DEL TASTO "Prenotazione batch fusione massiva"  VERRA' INVIATA LA RICHIESTA DI SCHEDULAZIONE</td></tr>
									<tr><td align="center">DEL BATCH CHE EFFETTUERA' LA FUSIONE DEGLI OGGETTI I CUI IDENTIFICATIVI DI PARTENZA ED ARRIVO DELLA FUSIONE</td></tr>
									<tr><td align="center">SONO PRESENTI O NELLA LISTA SELEZIONATA O NELLA TABELLA AGGIORNATA DALLA FUNZIONE LISTE DI CONFRONTO;</td></tr>
									<tr><td align="center">I CANALI SONO ALTERNATIVI ED E' POSSIBILE UTILIZZARNE UNO ALLA VOLTA</td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">
										<table align="center">
											<tr>
												<td class="etichetta">Carica file:&nbsp; <html:file property="uploadImmagine2" />
												</td>
												<td class="etichetta">
													<html:submit property="methodRichAllineamenti">
														<bean:message key="button.caricaFileIdFusioneMassiva" bundle="gestioneBibliograficaLabels" />
													</html:submit>
												</td>
											</tr>
										</table>
									</td></tr>
									<tr><td align="center">
										<table align="center">
											<tr>
												<td class="etichetta"><bean:message key="ricerca.listeConf.nomeLista"
													bundle="gestioneBibliograficaLabels" />:</td>
												<td class="testoNormale"><html:select
													property="dataListaSelez" style="width:200px">
													<html:optionsCollection property="listaDataLista"
														value="codice" label="descrizione" />
												</html:select></td>
											</tr>
										</table>
									</td></tr>
									<tr>
										<td align="center">
											<html:submit property="methodRichAllineamenti">
											<bean:message key="button.richiestaFusioneMassiva" bundle="gestioneBibliograficaLabels" />
											</html:submit>
										</td>
									</tr>

								</table>
							</c:when>
							<c:otherwise>
								<table border="0"  align="center" class="etichetta">
									<tr><td align="center">=================================================================================================================================</td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">ALLA PRESSIONE DEL TASTO "Inserimento richiesta Allineamenti Polo/Indice BATCH SERALE" VERRA' INVIATA LA RICHIESTA DI SCHEDULAZIONE</td></tr>
									<tr><td align="center">DEL BATCH DI ALLINEAMENTO DELLA BASE INFORMATIVA LOCALE AL SISTEMA CENTRALE;</td></tr>
									<tr><td align="center">- SE SI IMPOSTA UNA TIPOLOGIA DI MATERIALE L'ALLINEAMENTO DEI DOCUMENTI SARA' LIMITATO AL TIPO MATERIALE RICHIESTO E VERRA' SEGUITO</td></tr>
									<tr><td align="center">DALL'ALLINEAMENTO DEGLI AUTORI, POI DELLE MARCHE;</td></tr>
									<tr><td align="center">- SE SI IMPOSTA UNA DATA DA VERRANNO FILTRATI GLI ALLINEAMENTI PER LA DATA RICHIESTA; SE SI IMPOSTA ANCHE LA DATA A VERRANNO FELTRATI</td></tr>
									<tr><td align="center">GLI ALLINEAMENTI PER L'INTERVALLO DI DATE RICHIESTE;</td></tr>
									<tr><td align="center">- SE SI VALORIZZA IL CAMPO "Identificativo lista allineamento" LA PROCEDURA RICERCA IL FILE RICHIESTO SUL SERVER CENTRALE (INDICE)</td></tr>
									<tr><td align="center">ED ALLINEA SOLO I RETICOLI PRESENTI SU TALE FILE;</td></tr>
									<tr><td align="center"></td></tr>
								</table>

								<table border="0"  align="center" class="etichetta">
									<tr>
										<td>
										</td>
										<td>
											<table border="0">
												<tr>
													<td class="etichetta"><bean:message
														key="ricerca.tipoMateriale" bundle="gestioneBibliograficaLabels" />:</td>

													<td class="etichetta"><bean:message key="ricerca.tabTutti" bundle="gestioneBibliograficaLabels" />
														<html:radio	property="tipoMatSelez" value="*"/></td>
													<td class="etichetta"><bean:message key="ricerca.tabModerno" bundle="gestioneBibliograficaLabels" />
														<html:radio	property="tipoMatSelez" value="M"/></td>
													<td class="etichetta"><bean:message key="ricerca.tabAntico" bundle="gestioneBibliograficaLabels" />
														<html:radio	property="tipoMatSelez" value="E"/></td>
													<td class="etichetta"><bean:message key="ricerca.tabCartografia" bundle="gestioneBibliograficaLabels" />
														<html:radio	property="tipoMatSelez" value="C"/></td>
													<td class="etichetta"><bean:message key="ricerca.tabGrafica" bundle="gestioneBibliograficaLabels" />
														<html:radio	property="tipoMatSelez" value="G"/></td>
													<td class="etichetta"><bean:message key="ricerca.tabMusica" bundle="gestioneBibliograficaLabels" />
														<html:radio	property="tipoMatSelez" value="U"/></td>

												</tr>
											</table>

											<table border="0" class="etichetta">
												<tr>
							                        <td class="etichetta"><bean:message  key="label.allineam.dataDa" bundle="gestioneBibliograficaLabels" />
											 		  	<html:text styleId="testoNormale" property="dataAllineaDa" size="10"></html:text>
							                        </td>
							                        <td class="etichetta"><bean:message  key="label.allineam.dataA" bundle="gestioneBibliograficaLabels" />
											 		  <html:text styleId="testoNormale" property="dataAllineaA" size="10"></html:text>
							                        </td>
												</tr>
											</table>

											<table border="0" class="etichetta">
												<tr>
													<td class="etichetta"><bean:message	key="label.allineam.idLista"	bundle="gestioneBibliograficaLabels" />
														<html:text property="idFileAllineamenti" size="20" maxlength="20"></html:text>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>

								<table border="0"  align="center" class="etichetta">
									<tr><td align="center"></td></tr>
									<tr><td align="center">
										<c:choose>
											<c:when test="${richiestaAllineamentiForm.statoAvanzamento eq '0'}">
												<table align="center">
													<tr>
														<td align="center">
															<html:submit property="methodRichAllineamenti">
															<bean:message key="button.richiestaBatchAllineamenti" bundle="gestioneBibliograficaLabels" />
														</html:submit></td>
													</tr>
												</table>
											</c:when>
										</c:choose>
									</td></tr>
									<tr><td align="center">=================================================================================================================================</td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
								</table>

								<table border="0"  align="center" class="etichetta">
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">=================================================================================================================================</td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">ALLA PRESSIONE DEL TASTO "Allinea Repertori base dati Locale" VERRA' EFFETTUATO L'AGGIORNAMENTO DELL'ARCHIVIO DEI REPERTORI LOCALE</td></tr>
									<tr><td align="center">AL SISTEMA CENTRALE;L'ALLINEAMENTO AVVERRA' IMMEDIATAMENTE</td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">
										<c:choose>
											<c:when test="${richiestaAllineamentiForm.statoAvanzamento eq '0'}">
												<table align="center">
													<tr>
														<td align="center">
															<html:submit property="methodRichAllineamenti">
															<bean:message key="button.richiestaAllineaRepertori" bundle="gestioneBibliograficaLabels" />
														</html:submit></td>
													</tr>
												</table>
											</c:when>
										</c:choose>
									</td></tr>
									<tr><td align="center">=================================================================================================================================</td></tr>
								</table>

								<table border="0"  align="center" class="etichetta">
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">=================================================================================================================================</td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">ALLA PRESSIONE DEL TASTO "Prenotazione batch cattura massiva"  VERRA' INVIATA LA RICHIESTA DI SCHEDULAZIONE</td></tr>
									<tr><td align="center">DEL BATCH CHE EFFETTUERA' LA CATTURA DEGLI OGGETTI I CUI BID SONO PRESENTI NELLA LISTA SELEZIONATA;</td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">
										<table align="center">
											<tr>
												<td class="etichetta">Carica file:&nbsp; <html:file property="uploadImmagine" />
												</td>
												<td class="etichetta">
													<html:submit property="methodRichAllineamenti">
														<bean:message key="button.caricaFileIdCatturaMassiva" bundle="gestioneBibliograficaLabels" />
													</html:submit>
												</td>
												<td align="center">
													<html:submit property="methodRichAllineamenti">
													<bean:message key="button.richiestaCatturaMassiva" bundle="gestioneBibliograficaLabels" />
													</html:submit>
												</td>
											</tr>
										</table>
										</td></tr>
									<tr><td align="center">=================================================================================================================================</td></tr>
								</table>

								<table border="0"  align="center" class="etichetta">
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">=================================================================================================================================</td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">ALLA PRESSIONE DEL TASTO "Prenotazione batch fusione massiva"  VERRA' INVIATA LA RICHIESTA DI SCHEDULAZIONE</td></tr>
									<tr><td align="center">DEL BATCH CHE EFFETTUERA' LA FUSIONE DEGLI OGGETTI I CUI IDENTIFICATIVI DI PARTENZA ED ARRIVO DELLA FUSIONE</td></tr>
									<tr><td align="center">SONO PRESENTI NELLA LISTA SELEZIONATA;</td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center"></td></tr>
									<tr><td align="center">
										<table align="center">
											<tr>
												<td class="etichetta">Carica file:&nbsp; <html:file property="uploadImmagine2" />
												</td>
												<td class="etichetta">
													<html:submit property="methodRichAllineamenti">
														<bean:message key="button.caricaFileIdFusioneMassiva" bundle="gestioneBibliograficaLabels" />
													</html:submit>
												</td>
												<td align="center">
													<html:submit property="methodRichAllineamenti">
													<bean:message key="button.richiestaFusioneMassiva" bundle="gestioneBibliograficaLabels" />
													</html:submit>
												</td>
											</tr>
										</table>
										</td></tr>
									<tr><td align="center">=================================================================================================================================</td></tr>
								</table>
							</c:otherwise>
						</c:choose>

					</c:when>
					<c:when test="${richiestaAllineamentiForm.statoAvanzamento eq '1'}">
						<table border="0">
							<tr>
								<td width="300" class="etichetta"><bean:message key="label.allineam.idLista.Doc" bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text property="areaDatiAllineamentoPoloIndiceVO.numListaDaAllineareDocumento" readonly="true"></html:text></td>
								<td>
									<html:submit property="methodRichAllineamenti"><bean:message key="button.allineam.idLista.Doc" bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message key="label.allineam.idLista.Au" bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text property="areaDatiAllineamentoPoloIndiceVO.numListaDaAllineareAutore" readonly="true"></html:text></td>
								<td>
									<html:submit property="methodRichAllineamenti"><bean:message key="button.allineam.idLista.Au" bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message key="label.allineam.idLista.Ma" bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text property="areaDatiAllineamentoPoloIndiceVO.numListaDaAllineareMarca" readonly="true"></html:text></td>
								<td>
									<html:submit property="methodRichAllineamenti"><bean:message key="button.allineam.idLista.Ma" bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message key="label.allineam.idLista.Tu" bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text property="areaDatiAllineamentoPoloIndiceVO.numListaDaAllineareTitUniforme" readonly="true"></html:text></td>
								<td>
									<html:submit property="methodRichAllineamenti"><bean:message key="button.allineam.idLista.Tu" bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message key="label.allineam.idLista.Um" bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text property="areaDatiAllineamentoPoloIndiceVO.numListaDaAllineareTitUniformeMus" readonly="true"></html:text></td>
								<td>
									<html:submit property="methodRichAllineamenti"><bean:message key="button.allineam.idLista.Um" bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
							</tr>
						</table>
					</c:when>
				</c:choose>
		</div>

		<div id="divFooter">

		</div>
	</sbn:navform>
</layout:page>
