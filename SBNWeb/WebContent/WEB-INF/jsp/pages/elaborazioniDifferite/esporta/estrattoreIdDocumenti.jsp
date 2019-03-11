<!--	SBNWeb - Rifacimento ClientServer
		JSP per il servizio di Estrazione dati
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

	<sbn:navform action="/elaborazioniDifferite/esporta/estrattoreIdDocumenti.do"
		enctype="multipart/form-data">

		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<table width="100%" border="0" bgcolor="#FEF1E2">
			<tr>
				<td><bean:message key="label.listabiblioteche" bundle="esportaLabels" /></td>
				<td><html:text property="elencoBiblio" disabled="true" />&nbsp; <html:submit
					styleClass="buttonImageListaSezione" property="methodEstrattoreIdDoc">
					<bean:message key="button.cercabiblioteche" bundle="esportaLabels" /></html:submit>
					<c:choose>
						<c:when test="${navForm.tastoCancBib}">
							<html:submit property="methodEstrattoreIdDoc"> <bean:message key="button.tutteLeBiblio" bundle="esportaLabels" />
							</html:submit>
						</c:when>
					</c:choose>
				</td>
			</tr>
		</table>


		<!-- begin MENU NAVIGAZIONE TABs -->
		<table width="100%" border="0" bgcolor="#FEF1E2">
			<c:choose>
				<c:when test="${navForm.tipoProspettazione eq 'DATI_CATALOGRAFICI'}">
					<table width="100%" border="0" class="SchedaImg1">
						<tr>
							<td width="86" class="schedaOn" align="center">
							<div align="center">Dati Catalografici</div>
							</td>
							<td width="86" class="schedaOff" align="center"><input type="submit"
								name=methodEstrattoreIdDoc value="Posseduto" class="sintButtonLinkDefault"></td>
						</tr>
					</table>
				</c:when>
				<c:when test="${navForm.tipoProspettazione eq 'POSSEDUTO'}">
					<table width="100%" border="0" class="SchedaImg1">
						<tr>
							<td width="86" class="schedaOff" align="center"><input type="submit"
								name="methodEstrattoreIdDoc" value="Dati Catalografici" class="sintButtonLinkDefault">
							</td>
							<td width="86" class="schedaOn" align="center">
							<div align="center">Posseduto</div>
							</td>
						</tr>
					</table>
				</c:when>

			</c:choose>

			<!-- end MENU NAVIGAZIONE TABs -->
			<!-- begin campi EXPORT -->
			<!-- begin parte comune a tutti i tabs -->
			<!-- end parte comune a tutti i tabs -->
			<c:choose>
				<c:when test="${navForm.tipoProspettazione eq 'DATI_CATALOGRAFICI'}">
					<table class="sintetica" width="100%">
						<tr>
							<td><!-- begin FILTRI X ESTRAZIONE LISTA DOCUMENTI DI INTERESSE DA DB --> <!-- PERIODO TEMPORALE DI INTERESSE + LIVELLO AUTHORITA' + FLAG DI CONDIVISIONE-->
							<div></div>
							<table align="center" border="0" width="95%">
								<tr>
									<td class="testo"><bean:message key="label.solodocumenticondivisi" bundle="esportaLabels" />
									<html:checkbox name="navForm"
										property="esporta.soloDocCondivisi" /> <html:hidden name="navForm"
										property="esporta.soloDocCondivisi" value="false" /></td>
									<td class="testo"><bean:message key="label.solodocumentilocali"	bundle="esportaLabels" />
										<html:checkbox name="navForm"
										property="esporta.soloDocLocali" /> <html:hidden name="navForm"
										property="esporta.soloDocLocali" value="false" /></td>
									<td class="testo">&nbsp;</td>
									<td class="testo">&nbsp;</td>
								</tr>

								<tr>
									<td class="testo">&nbsp;</td>
								</tr>
							</table>

							<table align="center" border="0" width="95%">
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
							</table>
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
											<td class="testo" height="23" width="70%">&nbsp;</td>
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
											<div align="center">
												<bean:message key="label.linguaNonItaliano" bundle="esportaLabels" />
													<html:checkbox name="navForm"
														property="esporta.soloLinguaNoItaliano" /> <html:hidden name="navForm"
														property="esporta.soloLinguaNoItaliano" value="false" />	</div>
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
											<div align="center">
												<bean:message key="label.paeseNonItalia" bundle="esportaLabels" />
													<html:checkbox name="navForm"
														property="esporta.soloPaeseNoItalia" /> <html:hidden name="navForm"
														property="esporta.soloPaeseNoItalia" value="false" />
											</div>
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
										<html:optionsCollection property="listaTipoData" value="codice" label="descrizioneCodice" />
									</html:select></td>
									<td>&nbsp;<bean:message key="label.data1" bundle="esportaLabels" />&nbsp;<bean:message
										key="label.dal" bundle="esportaLabels" /></td>
									<td width="16%"><html:text property="esporta.aaPubbFrom" size="5"
										maxlength="4" /></td>
									<td width="2%"><bean:message key="label.al" bundle="esportaLabels" />&nbsp;</td>
									<td width="53%"><html:text property="esporta.aaPubbTo" size="5"	maxlength="4" /></td>
								</tr>
							</table>

							<table align="center" border="0" width="95%">
								<tr>
									<td><bean:message key="label.dataIns" bundle="esportaLabels" /></td>
									<td></td>
									<td><bean:message key="label.dal" bundle="esportaLabels" /></td>
									<td><html:text property="dataInsFrom" size="10" maxlength="10" /></td>
									<td><bean:message key="label.al" bundle="esportaLabels" />&nbsp;</td>
									<td><html:text property="dataInsTo" size="10" maxlength="10" /></td>

									<td class="etichetta" scope="col" align="left">
										<bean:message key="label.bibliotecario" bundle="esportaLabels" /></td>
									<td scope="col" align="left">
										<html:text styleId="testoNormale" property="codBibliotecIns" size="12"
											title="Ricerca bibliotecario" name="navForm"></html:text>
										<html:messages id="msg1" message="true" property="documentofisico.parameter.bottone" bundle="documentoFisicoLabels" />
										<html:submit title="Ricerca biblioteca"	styleClass="buttonImage" property="${msg1}">
											<bean:message key="documentofisico.bottone.SIFbibliotecarioIns" bundle="documentoFisicoLabels" />
										</html:submit>
										<bs:write name="navForm" property="nomeBibliotecIns" />
									</td>
								</tr>

								<tr>
									<td><bean:message key="label.dataAgg" bundle="esportaLabels" /></td>
									<td></td>
									<td><bean:message key="label.dal" bundle="esportaLabels" /></td>
									<td><html:text property="dataAggFrom" size="10" maxlength="10" /></td>
									<td><bean:message key="label.al" bundle="esportaLabels" />&nbsp;</td>
									<td><html:text property="dataAggTo" size="10" maxlength="10" /></td>

									<td class="etichetta" scope="col" align="left">
										<bean:message key="label.bibliotecario" bundle="esportaLabels" /></td>
									<td scope="col" align="left">
										<html:text styleId="testoNormale" property="codBibliotecAgg" size="12"
											title="Ricerca bibliotecario" name="navForm"></html:text>
										<html:messages id="msg1" message="true" property="documentofisico.parameter.bottone" bundle="documentoFisicoLabels" />
										<html:submit title="Ricerca biblioteca"	styleClass="buttonImage" property="${msg1}">
											<bean:message key="documentofisico.bottone.SIFbibliotecarioAgg" bundle="documentoFisicoLabels" />
										</html:submit>
										<bs:write name="navForm" property="nomeBibliotecAgg" />
									</td>

								</tr>
							</table>

						</tr>
					</table>
					<p>&nbsp;</p>
		</c:when>

		<c:when test="${navForm.tipoProspettazione eq 'POSSEDUTO'}">
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
				<p class="etichetta"></p>
				<table class="sintetica" width="100%">
					<tr>
						<td class="testo"">
						<strong><bean:message key="label.filtriSuInv" bundle="esportaLabels" /></strong></td>
						<td></td>
					</tr>
					<tr>
						<td><bean:message key="documentofisico.tipoFruizioneT" bundle="documentoFisicoLabels" /></td>
						<td colspan="3"><html:select property="esporta.codTipoFruizione">
							<html:optionsCollection property="listaCodTipoFruizione" value="codice" label="descrizione" />
						</html:select></td>
					</tr>
					<tr>
						<td><bean:message key="documentofisico.nonDisponibilePerT" bundle="documentoFisicoLabels" /></td>
						<td colspan="3"><html:select property="esporta.codNoDispo">
							<html:optionsCollection property="listaCodNoDispo" value="codice" label="descrizione" />
						</html:select></td>
					</tr>
					<tr>
						<td><bean:message key="documentofisico.riproducibilitaT" bundle="documentoFisicoLabels" /></td>
						<td colspan="3"><html:select property="esporta.codRip">
							<html:optionsCollection property="listaCodRiproducibilita" value="codice" label="descrizione" />
						</html:select></td>
					</tr>
					<tr>
						<td><bean:message key="documentofisico.statoConservazioneT"	bundle="documentoFisicoLabels" /></td>
						<td colspan="4"><html:select property="esporta.codiceStatoConservazione">
							<html:optionsCollection property="listaCodStatoConservazione" value="codice" label="descrizione" />
						</html:select></td>
					</tr>

					<tr>
						<td><bean:message key="documentofisico.tipoDigitalizzazioneT" bundle="documentoFisicoLabels" /></td>
						<td colspan="3">
							<html:select property="esporta.digitalizzati">
								<html:option value="" />
								<html:option value="S" key="label.si" bundle="esportaLabels" />
								<html:option value="N" key="label.no" bundle="esportaLabels" />

							</html:select>

							<bean:message key="label.esporta.escludi.altre.copie.digit" bundle="esportaLabels" />
							<html:checkbox property="esporta.escludiDigit" /><html:hidden property="esporta.escludiDigit" value="false" />
						</td>
						<td>
							<html:select property="esporta.tipoDigit">
								<html:option value="C" key="label.tipoDigit.completa" bundle="esportaLabels" />
								<html:option value="P" key="label.tipoDigit.parziale" bundle="esportaLabels" />
								<html:option value="T" key="label.tipoDigit.tutti" bundle="esportaLabels" />
							</html:select>
						</td>
					</tr>
				</table>

			</sbn:disableAll>

		</c:when>
		</c:choose>
	</table>
			<table align="left" border="0" width="55%">
				<tr>
					<td class="testoNormale"><bean:message key="label.tipoOutput"
							bundle="esportaLabels" />:</td>
					<td class="testoNormale"><bean:message
							key="label.tipoOutput.bid" bundle="esportaLabels" />&nbsp;<html:radio
							property="esporta.tipoOutputJSP" value="BID" /> <bean:message
							key="label.tipoOutput.inv" bundle="esportaLabels" />&nbsp;<html:radio
							property="esporta.tipoOutputJSP" value="INV" /></td>
				</tr>
			</table>

			<div id="divFooter">
		<table align="center">

			<tr>
				<td align="center"><html:submit property="methodEstrattoreIdDoc">
					<bean:message key="button.prenotaEstr" bundle="esportaLabels" />
				</html:submit></td>
			</tr>

		</table>
		</div>

		</div>
		<!-- end campi EXPORT -->

	</sbn:navform>
</layout:page>
