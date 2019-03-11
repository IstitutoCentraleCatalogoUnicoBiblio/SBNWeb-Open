<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<%@ page
	import="it.iccu.sbn.web.actionforms.elaborazioniDifferite.esporta.EsportaForm"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/elaborazioniDifferite/esporta/esporta.do"
		enctype="multipart/form-data">

		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!-- Inizio Modifica PER STAMPA CATALOGHI Ordinata in base alla richiesta Utente -->
		<table width="100%" border="0" bgcolor="#FEF1E2">
			<tr>
				<td><c:choose>
					<c:when test="${esportaForm.codAttivita eq 'ZG200'}">

						<table width="80%" border="0">
							<tr>
							<td class="etichetta" width="10%">
							<bean:message key="documentofisico.bibliotecaT"	bundle="documentoFisicoLabels" /></td>
								<td><html:text disabled="true" styleId="testoNormale" property="codBib" size="5"
									maxlength="3"></html:text> <html:submit disabled="false" title="Lista Biblioteche"
									styleClass="buttonImageListaSezione" property="methodMap_esporta">
									<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
								</html:submit><bs:write name="esportaForm" property="descrBib" /></td>
							</tr>

						</table>

						<table align="center" border="1" cellspacing="0" width="95%">
							<tr>
								<td class="etichetta"><div align="center"><strong><bean:message key="label.stampaTitolettoTipoCatalogo" bundle="esportaLabels" /></strong></div></td>
								<td class="etichetta"><div align="center"><strong><bean:message	key="cataloghi.label.datiDiStampa" bundle="gestioneStampeLabels" /></strong></div></td>
							</tr>
							<tr>
								<td>
									<table border="0" width="100%">
										<tr>
											<td class="etichetta"><bean:message key="label.stampaTipoCatalogo" bundle="esportaLabels" />:</td>
											<td><html:select property="esporta.tipoCatalogo" style="width:200px">
												<html:optionsCollection property="listaTipoCatalogo" value="codice"	label="descrizioneCodice" /></html:select></td>
										</tr>
										<tr>
											<td><bean:message key="label.estremoSupRicercaDa" bundle="esportaLabels" />:</td>
											<td><html:text property="esporta.catalogoSelezDa" size="80" maxlength="80"></html:text></td>
										</tr>
										<tr>
											<td><bean:message key="label.estremoInfRicercaA" bundle="esportaLabels" />:</td>
											<td><html:text property="esporta.catalogoSelezA" size="80" maxlength="80"></html:text></td>
										</tr>
										<tr>
											<td></td>
											<td>
												<bean:message key="gestionesemantica.soggetto.soggettario" bundle="gestioneSemanticaLabels" />
													<html:select styleClass="testoNormale" property="esporta.codSoggettario">
													<html:optionsCollection property="listaSoggettari" value="codice" label="descrizione" />
													</html:select>
												<bean:message key="soggettazione.sistema" bundle="gestioneSemanticaLabels" />
													<html:select styleClass="testoNormale" property="esporta.codSistemaClassificazione">
													<html:optionsCollection property="listaSistemiClassificazione" value="codice" label="codice" />
													</html:select>
											</td>
										</tr>
									</table>
								</td>
								<td>
									<table border="0" width="100%">
										<tr>
											<td align="left" class="etichetta"><bean:message
												key="cataloghi.label.intestazioneTitoloautorePrinc" bundle="gestioneStampeLabels" /></td>
											<td><html:checkbox property="intestTitoloAdAutore"></html:checkbox> <html:hidden
												property="intestTitoloAdAutore" value="false" /></td>
										</tr>
				<!--
										<tr>
											<td align="left" class="etichetta"><bean:message
												key="cataloghi.label.stampaTitoloCollana" bundle="gestioneStampeLabels" /></td>
											<td><html:checkbox property="titoloCollana"></html:checkbox> <html:hidden
												property="titoloCollana" value="false" /></td>
										</tr>
				-->
										<tr>
											<td align="left" class="etichetta"><bean:message
												key="cataloghi.label.stampaTitoliAnalitici" bundle="gestioneStampeLabels" /></td>
											<td><html:checkbox property="titoliAnalitici"></html:checkbox> <html:hidden
												property="titoliAnalitici" value="false" /></td>
										</tr>
										<tr>
											<td align="left" class="etichetta"><bean:message
												key="cataloghi.label.stampaDatiCollocazione" bundle="gestioneStampeLabels" /></td>
											<td><html:checkbox property="datiCollocazione"></html:checkbox> <html:hidden
												property="datiCollocazione" value="false" /></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</c:when>
					<c:otherwise>
						<table border="0" width="100%">
							<tr>
								<td class="testo" colspan="3">&nbsp;</td>
							</tr>
							<tr>
								<td class="testo" width="15%"><bean:message key="label.etichetteelenco"
									bundle="esportaLabels" /></td>
								<td width="30%"><html:select property="esporta.codScaricoSelez" style="width:200px">
									<html:optionsCollection property="listaCodScarico" value="cd_tabellaTrim"
										label="ds_tabella" />
								</html:select></td>
								<td class="testo" align="right"><logic:notEmpty name="esportaForm"
									property="extractionTime">
									<bean:message key="label.ultimaEstrazioneDB" bundle="esportaLabels" />&nbsp;
											<bs:write name="esportaForm" property="extractionTime" />
								</logic:notEmpty></td>
							</tr>
							<tr>
								<td><bean:message key="label.listabiblioteche" bundle="esportaLabels" /></td>
								<td><html:text property="elencoBiblio" disabled="true" />&nbsp; <html:submit
									styleClass="buttonImageListaSezione" property="methodMap_esporta">
									<bean:message key="button.cercabiblioteche" bundle="esportaLabels" />
								</html:submit> <c:choose>
									<c:when test="${esportaForm.tastoCancBib}">
										<html:submit property="methodMap_esporta">
											<bean:message key="button.tutteLeBiblio" bundle="esportaLabels" />
										</html:submit>
									</c:when>
								</c:choose></td>
								<td class="testo" align="right"><logic:notEmpty name="esportaForm"
									property="extractionTime">
									<bean:message key="label.checkUltimaEstrazioneDB" bundle="esportaLabels" />&nbsp;
										<bean:message key="label.si" bundle="esportaLabels" />
									<html:radio name="esportaForm" property="esporta.exportDB" value="true" />
									<bean:message key="label.no" bundle="esportaLabels" />
									<html:radio name="esportaForm" property="esporta.exportDB" value="false" />
									<%--<html:checkbox name="esportaForm" property="esporta.exportDB" />
										<html:hidden name="esportaForm" property="esporta.exportDB"	value="false" />--%>
								</logic:notEmpty></td>
							</tr>
						</table>
					</c:otherwise>
				</c:choose> <br />
		</table>
		<!-- begin MENU NAVIGAZIONE TABs -->
		<table width="100%" border="0" bgcolor="#FEF1E2">
			<c:choose>
				<c:when test="${esportaForm.tipoProspettazione eq 'DATI_CATALOGRAFICI'}">
					<table width="100%" border="0" class="SchedaImg1">
						<tr>
							<td width="86" class="schedaOn" align="center">
							<div align="center">Dati Catalografici</div>
							</td>
							<td width="86" class="schedaOff" align="center"><input type="submit"
								name="methodMap_esporta" value="Posseduto" class="sintButtonLinkDefault"></td>
						</tr>
					</table>
				</c:when>
				<c:when test="${esportaForm.tipoProspettazione eq 'POSSEDUTO'}">
					<table width="100%" border="0" class="SchedaImg1">
						<tr>
							<td width="86" class="schedaOff" align="center"><input type="submit"
								name="methodMap_esporta" value="Dati Catalografici" class="sintButtonLinkDefault">
							</td>
							<td width="86" class="schedaOn" align="center">
							<div align="center">Posseduto</div>
							</td>
						</tr>
					</table>
				</c:when>
				<%-- almaviva5_20100407 disattivato, per ora
					<c:when
						test="${esportaForm.tipoProspettazione eq 'CLASSI'}">

						<table width="100%" border="0" class="SchedaImg1">
							<tr>
								<td width="86" class="schedaOff" align="center"><input
									type="submit" name="methodMap_esporta"
									value="Dati Catalografici" class="sintButtonLinkDefault">
								</td>
								<td width="86" class="schedaOff" align="center"><input
									type="submit" name="methodMap_esporta" value="Posseduto"
									class="sintButtonLinkDefault"></td>
								<td width="86" class="schedaOn" align="center">
								<div align="center">Classificazioni</div>
								</td>
							</tr>
						</table>

					</c:when>
					 --%>
			</c:choose>
			<!-- end MENU NAVIGAZIONE TABs -->
			<!-- begin campi EXPORT -->
			<!-- begin parte comune a tutti i tabs -->
			<!-- end parte comune a tutti i tabs -->
			<c:choose>
				<c:when test="${esportaForm.tipoProspettazione eq 'DATI_CATALOGRAFICI'}">
					<!-- richiesta conversione con estrazione da archivio -->
					<table class="sintetica" width="100%">
						<tr>
							<td><!-- begin FILTRI X ESTRAZIONE LISTA DOCUMENTI DI INTERESSE DA DB --> <!-- PERIODO TEMPORALE DI INTERESSE + LIVELLO AUTHORITA' + FLAG DI CONDIVISIONE-->
							<div></div>
							<table align="center" border="0" width="95%">
								<tr>
									<td class="schedaOff" colspan="4"><bean:message
										key="label.schedalistadocarchivio" bundle="esportaLabels" /> <span
										class="testo"></span></td>
									<td colspan="5" width="80%"><html:radio property="esporta.tipoEstrazioneJSP"
										value="ARCHIVIO" /></td>
								</tr>
								<c:if test="${esportaForm.codAttivita eq 'IE001'}">
									<tr>
										<td class="testo"><bean:message key="label.soloaggiornamenti" bundle="esportaLabels" /></td>
										<td>&nbsp;<bean:message key="label.dal" bundle="esportaLabels" />&nbsp;</td>
										<td><html:text property="esporta.varFrom"></html:text></td>
										<td align="right">&nbsp;<bean:message key="label.al" bundle="esportaLabels" />&nbsp;</td>
										<td width="50%"><html:text property="esporta.varTo"></html:text></td>
										<td>&nbsp;</td>
									</tr>
								</c:if>
								<tr>
									<td class="testo"><bean:message key="label.anchetitolilivello01"
										bundle="esportaLabels" /></td>
									<td class="testo"><html:checkbox name="esportaForm"
										property="esporta.ancheTitoli01" /> <html:hidden name="esportaForm"
										property="esporta.ancheTitoli01" value="false" /></td>
									<td class="testo">&nbsp;</td>
									<td class="testo"><bean:message key="label.solodocumenticondivisi" bundle="esportaLabels" /></td>
									<td><html:checkbox name="navForm"
										property="esporta.soloDocCondivisi" /> <html:hidden name="navForm"
										property="esporta.soloDocCondivisi" value="false" /></td>
									<td class="testo">&nbsp;</td>
								</tr>
								<tr>
								<!-- almaviva2 - giugno 2018 - MANUTENZIONE EVOLUTIVA
									Intervento su Esporta Documenti: inserimento del check per esportare solo i	documenti Posseduti
									variazione conseguente delle SELECT di estrazione e passaggio successivo all'eseguibile
									dell'argomento --esportaSoloInventariCollocati -->
									<td class="testo"><bean:message key="label.solodocumentiposseduti"
										bundle="esportaLabels" /></td>
									<td class="testo"><html:checkbox name="esportaForm"
										property="esporta.soloDocPosseduti" /> <html:hidden name="esportaForm"
										property="esporta.soloDocPosseduti" value="false" /></td>

									<td class="testo">&nbsp;</td>
									<td class="testo"><bean:message key="label.solodocumentilocali"
										bundle="esportaLabels" /></td>
									<td class="testo"><html:checkbox name="esportaForm"
										property="esporta.soloDocLocali" /> <html:hidden name="esportaForm"
										property="esporta.soloDocLocali" value="false" /></td>
									<td class="testo">&nbsp;</td>
								</tr>
							</table>

							<!-- Inizio Modifica PER STAMPA CATALOGHI viene eliminata la possibilità di filtre per titolo
							inserendo in alto la possibiliotà di filtrare per TipoCatalogo
							<table align="center" border="0" width="95%">
								<c:if test="${esportaForm.codAttivita eq 'ZG200'}">
									<tr>
										<td class="testoNormale"><bean:message key="label.descTitoloDa"
											bundle="esportaLabels" />:</td>
										<td class="testoNormale"><html:text property="esporta.descTitoloDa"
											size="75" maxlength="80"></html:text></td>
									</tr>
									<tr>
										<td class="testoNormale"><bean:message key="label.descTitoloA"
											bundle="esportaLabels" />:</td>
										<td class="testoNormale"><html:text property="esporta.descTitoloA"
											size="75" maxlength="80"></html:text></td>
									</tr>
								</c:if>
							</table>
							Fine Modifica PER STAMPA CATALOGHI Ordinata in base alla richiesta Utente -->


							<!-- tabelle FILTRI MATERIALE, TIPO RECORD, NATURA, LINGUA, PAESE -->
							<table align="center" border="1" cellspacing="1" width="95%">
								<tr>
									<td class="testo" width="10%">
									<div align="center"><strong><bean:message key="label.specificita"
										bundle="esportaLabels" /></strong></div>
									</td>
									<td class="testo" width="10%">
									<div align="center"><strong><bean:message key="label.tiporecord"
										bundle="esportaLabels" /></strong></div>
									</td>
									<td class="testo" width="10%">
									<div align="center"><strong><bean:message key="label.natura"
										bundle="esportaLabels" /></strong></div>
									</td>
									<td class="testo" width="10%">
									<div align="center"><strong><bean:message key="label.lingua"
										bundle="esportaLabels" /></strong></div>
									</td>
									<td class="testo" width="10%">
									<div align="center"><strong><bean:message key="label.paese"
										bundle="esportaLabels" /></strong></div>
									</td>
								</tr>
								<tr>
									<td class="testo">
									<div align="center">
									<table border="0" width="90%">
										<tr>
											<td class="testo" height="23" width="70%"><bean:message
												key="label.moderno" bundle="esportaLabels" /></td>
											<td>
											<div align="center"><html:multibox property="esporta.materiali" value="M" /></div>
											</td>
										</tr>
										<tr>
											<td class="testo" height="23" width="70%"><bean:message key="label.antico"
												bundle="esportaLabels" /></td>
											<td>
											<div align="center"><html:multibox property="esporta.materiali" value="E" /></div>
											</td>
										</tr>
										<tr>
											<td class="testo" height="23" width="70%"><bean:message key="label.musica"
												bundle="esportaLabels" /></td>
											<td>
											<div align="center"><html:multibox property="esporta.materiali" value="U" /></div>
											</td>
										</tr>
										<tr>
											<td class="testo" height="23" width="70%"><bean:message
												key="label.grafico" bundle="esportaLabels" /></td>
											<td>
											<div align="center"><html:multibox property="esporta.materiali" value="G" /></div>
											</td>
										</tr>
										<tr>
											<td class="testo" height="23" width="70%"><bean:message
												key="label.cartografico" bundle="esportaLabels" /></td>
											<td>
											<div align="center"><html:multibox property="esporta.materiali" value="C" /></div>
											</td>
										</tr>

										<tr>
											<td class="testo" height="23" width="70%"><bean:message
												key="label.audiovisivo" bundle="esportaLabels" /></td>
											<td>
											<div align="center"><html:multibox property="esporta.materiali" value="H" /></div>
											</td>
										</tr>
										<tr>
											<td class="testo" height="23" width="70%"><bean:message
												key="label.elettronico" bundle="esportaLabels" /></td>
											<td>
											<div align="center"><html:multibox property="esporta.materiali" value="L" /></div>
											</td>
										</tr>

									</table>
									</div>
									</td>
									<td class="testo">
									<div align="center">
									<table border="0" width="90%">
										<tr>
											<td height="23" width="70%">
											<div align="center"><html:select property="esporta.tipoRecord1"
												style="width:200px">
												<html:optionsCollection property="listaTipoRecord" value="codice"
													label="descrizioneCodice" />
											</html:select></div>
											</td>
										</tr>
										<tr>
											<td height="23" width="70%">
											<div align="center"><html:select property="esporta.tipoRecord2"
												style="width:200px">
												<html:optionsCollection property="listaTipoRecord" value="codice"
													label="descrizioneCodice" />
											</html:select></div>
											</td>
										</tr>
										<tr>
											<td height="23" width="70%">
											<div align="center"><html:select property="esporta.tipoRecord3"
												style="width:200px">
												<html:optionsCollection property="listaTipoRecord" value="codice"
													label="descrizioneCodice" />
											</html:select></div>
											</td>
										</tr>
										<tr>
											<td height="23" width="70%">
											<div align="center"></div>
											</td>
										</tr>
										<tr>
											<td height="23" width="70%">
											<div align="center"></div>
											</td>
										</tr>
									</table>
									</div>
									</td>
									<td class="testo">
									<div align="center">
									<table border="0" width="90%">
										<tr>
											<td class="testo" height="23" width="70%"><bean:message
												key="label.monografie" bundle="esportaLabels" /></td>
											<td>
											<div align="center"><html:multibox property="esporta.nature" value="M" /></div>
											</td>
										</tr>
										<tr>
											<td class="testo" height="23" width="70%"><bean:message
												key="label.periodici" bundle="esportaLabels" /></td>
											<td>
											<div align="center"><html:multibox property="esporta.nature" value="S" /></div>
											</td>
										</tr>
										<tr>
											<td class="testo" height="23" width="70%"><bean:message
												key="label.collane" bundle="esportaLabels" /></td>
											<td>
											<div align="center"><html:multibox property="esporta.nature" value="C" /></div>
											</td>
										</tr>
										<tr>
											<td class="testo" height="23" width="70%"><bean:message key="label.spogli"
												bundle="esportaLabels" /></td>
											<td>
											<div align="center"><html:multibox property="esporta.nature" value="N" /></div>
											</td>
										</tr>
										<tr>
											<td class="testo" height="23" width="70%"><bean:message key="label.raccolte"
												bundle="esportaLabels" /></td>
											<td>
											<div align="center"><html:multibox property="esporta.nature" value="R" /></div>
											</td>
										</tr>
									</table>
									</div>
									</td>
									<td class="testo">
									<div align="center">
									<table border="0" width="90%">
										<tr>
											<td height="23" width="70%">
											<div align="center"><html:select property="esporta.lingua1"
												style="width:200px">
												<html:optionsCollection property="listaLingue" value="codice"
													label="descrizioneCodice" />
											</html:select></div>
											</td>
										</tr>
										<tr>
											<td height="23" width="70%">
											<div align="center"><html:select property="esporta.lingua2"
												style="width:200px">
												<html:optionsCollection property="listaLingue" value="codice"
													label="descrizioneCodice" />
											</html:select></div>
											</td>
										</tr>
										<tr>
											<td height="23" width="70%">
											<div align="center"><html:select property="esporta.lingua3"
												style="width:200px">
												<html:optionsCollection property="listaLingue" value="codice"
													label="descrizioneCodice" />
											</html:select></div>
											</td>
										</tr>
										<tr>
											<td height="23" width="70%">
											<div align="center"></div>
											</td>
										</tr>
										<tr>
											<td height="23" width="70%">
											<div align="center"></div>
											</td>
										</tr>
									</table>
									</div>
									</td>
									<td class="testo">
									<div align="center">
									<table border="0" width="90%">
										<tr>
											<td height="23" width="70%">
											<div align="center"><html:select property="esporta.paese1"
												style="width:200px">
												<html:optionsCollection property="listaPaese" value="codice"
													label="descrizioneCodice" />
											</html:select></div>
											</td>
										</tr>
										<tr>
											<td height="23" width="70%">
											<div align="center"><html:select property="esporta.paese2"
												style="width:200px">
												<html:optionsCollection property="listaPaese" value="codice"
													label="descrizioneCodice" />
											</html:select></div>
											</td>
										</tr>
										<tr>
											<td height="23" width="70%">
											<div align="center"></div>
											</td>
										</tr>
										<tr>
											<td height="23" width="70%">
											<div align="center"></div>
											</td>
										</tr>
										<tr>
											<td height="23" width="70%">
											<div align="center"></div>
											</td>
										</tr>
									</table>
									</div>
									</td>
								</tr>
							</table>
							<!-- TIPO DATA di pubblicazione + RANGE DATA1 -->
							<table align="center" border="0" width="95%">
								<tr>
									<td colspan="6">&nbsp;</td>
								</tr>
								<tr>
									<td width="3%"><bean:message key="label.tipodata" bundle="esportaLabels" /></td>
									<td width="22%"><html:select property="esporta.tipoData" style="width:200px">
										<html:optionsCollection property="listaTipoData" value="codice"
											label="descrizioneCodice" />
									</html:select></td>
									<td>&nbsp;<bean:message key="label.data1" bundle="esportaLabels" />&nbsp;<bean:message
										key="label.dal" bundle="esportaLabels" /></td>
									<td width="16%"><html:text property="esporta.aaPubbFrom" size="5"
										maxlength="4" /></td>
									<td width="2%"><bean:message key="label.al" bundle="esportaLabels" />&nbsp;</td>
									<td width="53%"><html:text property="esporta.aaPubbTo" size="5"
										maxlength="4" /></td>
								</tr>
								<c:if test="${esportaForm.codAttivita eq 'IE001'}">
									<%-- almaviva5_20100407
										<tr>
											<td width="3%"><bean:message key="label.ateneo"
												bundle="esportaLabels" /></td>
											<td width="16%" colspan="5"><html:select
												property="esporta.ateneo" style="width:80px">
												<html:optionsCollection property="listaBiblioAteneo"
													value="codice" label="descrizioneCodice" />
											</html:select></td>
										</tr>
										<tr>
											<td colspan="6">&nbsp;</td>
										</tr>
										<tr>
											<td colspan="6"><bean:message
												key="label.listabiblioteche" bundle="esportaLabels" />
											&nbsp;<html:text property="elencoBiblio" disabled="true" />
											&nbsp;<html:submit styleClass="buttonImageListaSezione"
												property="methodMap_esporta">
												<bean:message key="button.cercabiblioteche"
													bundle="esportaLabels" />
											</html:submit></td>
										</tr>
										--%>
								</c:if>
							</table>
							<!-- end FILTRI X ESTRAZIONE LISTA BID DA DB--></td>

						</tr>
					</table>

					<p>&nbsp;</p>
					<!-- richiesta conversione con lista da FILE -->
					<table class="sintetica" width="100%">
						<tr>
							<td>
							<table align="center" border="0" width="95%">
								<tr>
									<td class="schedaOff" colspan="2"><bean:message
										key="label.caricalistaetichettefile" bundle="esportaLabels" /></td>
									<td width="80%"><html:radio property="esporta.tipoEstrazioneJSP"
										value="FILE" /></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message key="label.selezionafile"
										bundle="esportaLabels" /></td>
									<td class="etichetta"><html:file property="fileIdList"
										value="esporta.fileIdListPathNameLocal" size="60" /></td>
									<td class="etichetta"><html:submit property="methodMap_esporta">
										<bean:message key="button.caricafile" bundle="esportaLabels" />
									</html:submit></td>
								</tr>
								<c:if test="${esportaForm.codAttivita eq 'IE001'}">
									<tr><td colspan="3">&nbsp;</td></tr>
									<tr>
										<td colspan="2" class="testoNormale"><bean:message key="label.IE001.tipoOutput"
												bundle="esportaLabels" />:</td>
										<td class="testoNormale"><bean:message
												key="label.tipoOutput.bid" bundle="esportaLabels" />&nbsp;<html:radio
												property="esporta.tipoInputJSP" value="BID" /> <bean:message
												key="label.tipoOutput.inv" bundle="esportaLabels" />&nbsp;<html:radio
												property="esporta.tipoInputJSP" value="INV" /></td>
									</tr>
								</c:if>

							</table>
							</td>
						</tr>
					</table>
			</td>
			</tr>
		</table>
		</c:when>
		<c:when test="${esportaForm.tipoProspettazione eq 'POSSEDUTO'}">
			<sbn:disableAll checkAttivita="COUNT_BIBLIOTECHE">
				<!-- begin ESTRAZIONE PER PER INTERVALLO DI COLLOCAZIONI -->
				<table class="sintetica" width="100%" border="0">
					<!-- begin SELEZIONE RANGE DI COLLOCAZIONE -->
					<tr>
						<td>
						<table align="left" border="0" width="35%">
							<tr>
								<td class="schedaOff"><bean:message
									key="label.schedalistadoccollocazione" bundle="esportaLabels" /> </td>
								<td>
								<html:radio property="esporta.tipoEstrazioneJSP"
									value="COLLOCAZIONI" onchange="this.form.submit()"/>
							</tr>
						</table>
						<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selColloc.jsp" /></td>
					</tr>
				</table>
				<!-- end FILTRI X DATA DI VARIAZIONE DELLA COLLOCAZIONE -->
				<p class="etichetta"></p>
				<!-- begin PREPARA LISTA PER INTERVALLO DI INVENTARI-->
				<table class="sintetica" width="100%">
					<!-- begin SELEZIONE RANGE DI SERIE INVENTARIALE -->
					<tr>
						<td>
						<table align="left" border="0" width="35%">
							<tr>
								<td class="schedaOff"><bean:message
									key="label.schedalistadocserieinv" bundle="esportaLabels" /></td>
								<td><html:radio property="esporta.tipoEstrazioneJSP"
									value="SERIE_INVENTARIALE"  onchange="this.form.submit()"/></td>
							</tr>
						</table>
						<table align="left" border="0" width="100%">
							<tr>
								<td class="testo">&nbsp;</td>
							</tr>
							<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" />
						</table>
						</td>
					</tr>
					<!-- end FILTRI X DATA DI VARIAZIONE DELLA COLLOCAZIONE -->
				</table>
			</sbn:disableAll>
			<p class="etichetta"></p>
		</c:when> <%-- almaviva5_20100407
		<c:when test="${esportaForm.tipoProspettazione eq 'CLASSI'}">

			<table width="100%" border="0">
				<tr class="testo">
					<td width="25%">
					<div align="right"><strong><bean:message
						key="label.sistema" bundle="esportaLabels" /></strong></div>
					</td>
					<td width="75%"><html:select
						property="esporta.classSistemaSelez" style="width:40px">
						<html:optionsCollection property="listaClassSistema"
							value="codice" label="descrizioneCodice" />
					</html:select></td>
				</tr>
				<tr class="testo">
					<td width="25%">
					<div align="right"><strong><bean:message
						key="label.dalsimbolo" bundle="esportaLabels" /></strong></div>
					</td>
					<td width="75%"><html:text property="esporta.classDalSimbolo"></html:text></td>
				</tr>
				<tr class="testo">
					<td width="25%">
					<div align="right"><strong><bean:message
						key="label.alsimbolo" bundle="esportaLabels" /></strong></div>
					</td>
					<td width="75%"><html:text property="esporta.classAlSimbolo"></html:text></td>
				</tr>
			</table>
		</c:when>
		 --%> </c:choose> <c:choose>
			<c:when test="${esportaForm.codAttivita eq 'ZG200'}">
				<HR>
				<jsp:include flush="true"
					page="/WEB-INF/jsp/pages/gestionestampe/common/tipoStampa.jsp" />
				<HR>
			</c:when>
		</c:choose>

		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodMap_esporta">
					<bean:message key="button.prenota" bundle="esportaLabels" />
				</html:submit></td>
			</tr>

		</table>
		</div>

		</div>
		<!-- end campi EXPORT -->

	</sbn:navform>
</layout:page>
