<!--	SBNWeb - Rifacimento ClientServer
		dettaglioTitoloParteFissaVARIAZIONEnatA
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


<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="ricerca.livelloAutorita"
			bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:select
			property="dettTitComVO.detTitoloPFissaVO.livAut"
			style="width:40px">
			<html:optionsCollection property="listaLivAut" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
	</tr>
</table>
<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="ricerca.paese" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100"><html:select
			property="dettTitComVO.detTitoloPFissaVO.paese"
			style="width:47px">
			<html:optionsCollection property="listaPaese" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="ricerca.lingua" bundle="gestioneBibliograficaLabels" /></td>

		<td width="100"><html:select
			property="dettTitComVO.detTitoloPFissaVO.lingua1"
			style="width:55px">
			<html:optionsCollection property="listaLingua" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="100"><html:select
			property="dettTitComVO.detTitoloPFissaVO.lingua2"
			style="width:55px">
			<html:optionsCollection property="listaLingua" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="100"><html:select
			property="dettTitComVO.detTitoloPFissaVO.lingua3"
			style="width:55px">
			<html:optionsCollection property="listaLingua" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
	</tr>
</table>
<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="ricerca.genere" bundle="gestioneBibliograficaLabels" /></td>

		<td width="100"><html:select
			property="dettTitComVO.detTitoloPFissaVO.genere1"
			style="width:40px">
			<html:optionsCollection property="listaGenere" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="100"><html:select
			property="dettTitComVO.detTitoloPFissaVO.genere2"
			style="width:40px">
			<html:optionsCollection property="listaGenere" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="100"><html:select
			property="dettTitComVO.detTitoloPFissaVO.genere3"
			style="width:40px">
			<html:optionsCollection property="listaGenere" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="100"><html:select
			property="dettTitComVO.detTitoloPFissaVO.genere4"
			style="width:40px">
			<html:optionsCollection property="listaGenere" value="codice"
				label="descrizioneCodice" />
		</html:select></td>

	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="ricerca.tipo.Data" bundle="gestioneBibliograficaLabels" /></td>

		<td width="100"><html:select
			property="dettTitComVO.detTitoloPFissaVO.tipoData"
			style="width:40px">
			<html:optionsCollection property="listaTipoData" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="60" class="etichetta"><bean:message
			key="ricerca.dataPubblicazione1"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="40" class="testoNormale"><html:text
			property="dettTitComVO.detTitoloPFissaVO.dataPubbl1" size="15"></html:text></td>
		<td width="60" class="etichetta"><bean:message
			key="ricerca.dataPubblicazione2"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="40" class="testoNormale"><html:text
			property="dettTitComVO.detTitoloPFissaVO.dataPubbl2" size="15"></html:text></td>

		<!--  almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
					è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice -->
			<td width="60" class="etichetta"><bean:message
						key="label.area0.pubblicatoSiNo"
						bundle="gestioneBibliograficaLabels" /></td>
			<td width="100"><html:select
						property="dettTitComVO.detTitoloPFissaVO.pubblicatoSiNo"
						style="width:40px">
						<html:optionsCollection property="listaPubblicatoSiNo" value="codice"
							label="descrizioneCodice" />
					</html:select></td>

	</tr>
</table>

<!--  almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S e tutti i Materiali-->
<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaArea0VARIAZIONE.jsp" />

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="catalogazione.areaT.titolo"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo"
					cols="70" rows="5"></html:textarea> <sbn:tastiera limit="1200"
					property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo"
					name="dettaglioTitoloForm"></sbn:tastiera></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message
					key="catalogazione.areaT.edizione"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitEdiz" cols="70"
					rows="1"></html:textarea> <sbn:tastiera limit="1200"
					property="dettTitComVO.detTitoloPFissaVO.areaTitEdiz"
					name="dettaglioTitoloForm"></sbn:tastiera></td>
			</tr>

			<c:choose>
				<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'M'}">

					<c:choose>
						<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'c'}">
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.musica"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitMusica"
									cols="70" rows="1"></html:textarea> <sbn:tastiera limit="1200"
									property="dettTitComVO.detTitoloPFissaVO.areaTitMusica"
									name="dettaglioTitoloForm"></sbn:tastiera></td>
							</tr>
						</c:when>
					</c:choose>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.numerazione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNumer"
							cols="70" rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitNumer"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>
				</c:when>
				<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'E'}">
				</c:when>
				<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'U'}">
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.numerazione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNumer"
							cols="70" rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitNumer"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>

					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.musica"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitMusica"
							cols="70" rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitMusica"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>
				</c:when>
				<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'C'}">
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.datiMatematici"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitDatiMatem"
							cols="70" rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitDatiMatem"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.numerazione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNumer"
							cols="70" rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitNumer"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>
				</c:when>
				<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'G'}">
				</c:when>
				<c:otherwise>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.datiMatematici"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitDatiMatem"
							cols="70" rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitDatiMatem"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.numerazione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNumer"
							cols="70" rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitNumer"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>
				</c:otherwise>
			</c:choose>

			<tr>
				<td class="etichetta"><bean:message
					key="catalogazione.areaT.pubblicazione"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitPubbl"
					cols="70" rows="3"></html:textarea> <sbn:tastiera limit="1200"
					property="dettTitComVO.detTitoloPFissaVO.areaTitPubbl"
					name="dettaglioTitoloForm"></sbn:tastiera></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message
					key="catalogazione.areaT.descrFisica"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitDescrFis"
					cols="70" rows="1"></html:textarea> <sbn:tastiera limit="1200"
					property="dettTitComVO.detTitoloPFissaVO.areaTitDescrFis"
					name="dettaglioTitoloForm"></sbn:tastiera></td>
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

			<!--	GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274 -->


			<!--Inizio almaviva2 modifica  BUG 3673 -->
			<c:choose>
				<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'g'}">
					<tr>
						<td class="etichetta"><bean:message key="catalogazione.areaT.note323" bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote323" cols="70"
							rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote323"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>
				</c:when>
				<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'j'}">
					<tr>
						<td class="etichetta"><bean:message key="catalogazione.areaT.note323" bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote323" cols="70"
							rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote323"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>
				</c:when>
				<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'i'}">
					<tr>
						<td class="etichetta"><bean:message key="catalogazione.areaT.note323" bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote323" cols="70"
							rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote323"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>
				</c:when>
			</c:choose>



			<tr>
				<td class="etichetta"><bean:message	key="catalogazione.areaT.note327"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitNote327" cols="70"
					rows="1"></html:textarea> <sbn:tastiera limit="1200"
					property="dettTitComVO.detTitoloPFissaVO.areaTitNote327"
					name="dettaglioTitoloForm"></sbn:tastiera></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="catalogazione.areaT.note330" bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitNote330" cols="70"
					rows="1"></html:textarea> <sbn:tastiera limit="1200"
					property="dettTitComVO.detTitoloPFissaVO.areaTitNote330"
					name="dettaglioTitoloForm"></sbn:tastiera></td>
			</tr>
<!-- queste note non sono ancora gestite dal protocollo rimangono quindi ancora in sospeso
					<tr>
						<td class="etichetta"><bean:message	key="catalogazione.areaT.noteDATA" bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNoteDATA" cols="70"
							rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitNoteDATA"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message	key="catalogazione.areaT.noteORIG" bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNoteORIG" cols="70"
							rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitNoteORIG"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message	key="catalogazione.areaT.noteFILI" bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNoteFILI" cols="70"
							rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitNoteFILI"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message	key="catalogazione.areaT.notePROV"	bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNotePROV" cols="70"
							rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitNotePROV"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
						</tr>
						<tr>
							<td class="etichetta"><bean:message	key="catalogazione.areaT.notePOSS"	bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNotePOSS" cols="70"
							rows="1"></html:textarea> <sbn:tastiera limit="1200"
							property="dettTitComVO.detTitoloPFissaVO.areaTitNotePOSS"
							name="dettaglioTitoloForm"></sbn:tastiera></td>
						</tr>
fine della parte commentata per note in sospeso  -->

<!--	GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274 -->

	<!--	GESTIONE NOTE AGGIUNTIVE 3672 ; Inizio almaviva2 modifica  27 ottobre 2010 BUG 3672
	spostato dentro la table principale il choose del tipoRec per le risorse elettroniche per questioni di allineamento
	-->


		<!--  Inizio almaviva2 modifica  23.12.2010 Mail Contardi: "devono saltare i controlli inseriti sul tipo record = l (risorsa elettronica).
	 Si prospettavano sempre i campi URI (856), e Note sui requisiti del sistema e sulla risorsa elettronica
	 (non solo in presenza del tipo_record = l.) -->

		<!--	GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274 -->
						<tr>
							<td class="etichetta"><bean:message key="catalogazione.areaT.note336" bundle="gestioneBibliograficaLabels" /></td>
							<td><html:textarea
								property="dettTitComVO.detTitoloPFissaVO.areaTitNote336" cols="70"
								rows="1"></html:textarea> <sbn:tastiera limit="1200"
								property="dettTitComVO.detTitoloPFissaVO.areaTitNote336"
								name="dettaglioTitoloForm"></sbn:tastiera></td>
						</tr>
						<tr>
							<td class="etichetta"><bean:message	key="catalogazione.areaT.note337" bundle="gestioneBibliograficaLabels" /></td>
							<td><html:textarea
								property="dettTitComVO.detTitoloPFissaVO.areaTitNote337" cols="70"
								rows="1"></html:textarea> <sbn:tastiera limit="1200"
								property="dettTitComVO.detTitoloPFissaVO.areaTitNote337"
								name="dettaglioTitoloForm"></sbn:tastiera></td>
						</tr>
		<!--	GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274 -->
						<tr>
							<td class="etichetta"><bean:message	key="dettaglio.uriCopiaElettr" bundle="gestioneBibliograficaLabels" /></td>
							<td><html:textarea property="dettTitComVO.detTitoloPFissaVO.uriDigitalBorn" cols="70" rows="1"></html:textarea></td>
						</tr>

		</table>


<!--  // Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi al link dei documenti
		 su Basi Esterne - Link verso base date digital -->
				<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'N'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'S'}">

					<table border="0">
						<tr>
							<th width="100" class="etichetta"><bean:message
								key="ricerca.linkesterni" bundle="gestioneBibliograficaLabels" /></th>
							<th class="etichetta" bgcolor="#dde8f0"><bean:message
								key="ricerca.linkesterni.bd" bundle="gestioneBibliograficaLabels" /></th>
							<th class="etichetta" bgcolor="#dde8f0"><bean:message
								key="ricerca.linkesterni.id" bundle="gestioneBibliograficaLabels" /></th>
							<th class="etichetta" bgcolor="#dde8f0"><bean:message
								key="ricerca.linkesterni.url" bundle="gestioneBibliograficaLabels" /></th>
								<th width="85" class="etichetta" bgcolor="#dde8f0">
							&nbsp;
							<html:submit
								styleClass="buttonImageDelLine" property="methodDettaglioTit"
								title="Cancella Link esterno">
								<bean:message key="button.canLinkEsterni"
									bundle="gestioneBibliograficaLabels" />
							</html:submit> <html:submit styleClass="buttonImageNewLine"
								property="methodDettaglioTit" title="Inserisci Link esterno">
								<bean:message key="button.insLinkEsterni"
									bundle="gestioneBibliograficaLabels" />
							</html:submit>
							<html:submit styleClass="buttonImageModLine"
										property="methodDettaglioTit" title="Ricalcola URL">
										<bean:message key="button.ricalcolaURL"
											bundle="gestioneBibliograficaLabels" />
									</html:submit>
								</th>
						</tr>

						<logic:iterate id="itemLinkEsterni"	property="dettTitComVO.detTitoloPFissaVO.listaLinkEsterni"
									name="dettaglioTitoloForm" indexId="idxLinkEsterni">
							<tr>
								<td></td>
								<td bgcolor="#FFCC99"><html:select name="itemLinkEsterni"
									property="campoUno" style="width:40px" indexed="true">
									<html:optionsCollection property="listaLinkEsterni"
										value="codice" label="descrizioneCodice" />
								</html:select></td>
								<td bgcolor="#FFCC99"><html:text name="itemLinkEsterni"
									property="campoDue" indexed="true" /></td>

								<td bgcolor="#FFCC99">
									<html:text name="itemLinkEsterni"
											property="nota" indexed="true" size="30" maxlength="30" readonly="true"
											style="color:blue; text-decoration:underline; cursor:pointer"
											onclick="if ($.trim(this.value).length > 0) { window.open(this.value) }" />
								</td>

								<td bgcolor="#FFCC99"><html:radio
									property="selezRadioLinkEsterni" value="${idxLinkEsterni}" /></td>
							</logic:iterate>
						</tr>
					</table>
				</c:if>


<!--  /	// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// ai repertori cartacei - Riferimento a repertorio cartaceol -->
				<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'N'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'S'}">

					<table border="0">
						<tr>
							<th width="100" class="etichetta"><bean:message
								key="ricerca.ReperCartaceo" bundle="gestioneBibliograficaLabels" /></th>
							<th class="etichetta" bgcolor="#dde8f0"><bean:message
								key="ricerca.ReperCartaceo.aut.tit" bundle="gestioneBibliograficaLabels" /></th>
							<th class="etichetta" bgcolor="#dde8f0"><bean:message
								key="ricerca.ReperCartaceo.data" bundle="gestioneBibliograficaLabels" /></th>
							<th class="etichetta" bgcolor="#dde8f0"><bean:message
								key="ricerca.ReperCartaceo.posizione" bundle="gestioneBibliograficaLabels" /></th>
								<th width="85" class="etichetta" bgcolor="#dde8f0">
							&nbsp;
							<html:submit
								styleClass="buttonImageDelLine" property="methodDettaglioTit"
								title="Cancella Repertorio cartaceo">
								<bean:message key="button.canReperCartaceo"
									bundle="gestioneBibliograficaLabels" />
							</html:submit> <html:submit styleClass="buttonImageNewLine"
								property="methodDettaglioTit" title="Inserisci Repertorio cartaceo">
								<bean:message key="button.insReperCartaceo"
									bundle="gestioneBibliograficaLabels" />
							</html:submit></th>
						</tr>

						<logic:iterate id="itemReperCartaceo"	property="dettTitComVO.detTitoloPFissaVO.listaReperCartaceo"
									name="dettaglioTitoloForm" indexId="idxReperCartaceo">
							<tr>
								<td></td>
								<td bgcolor="#FFCC99"><html:text name="itemReperCartaceo"
									property="campoUno" indexed="true" /></td>

								<td bgcolor="#FFCC99"><html:text name="itemReperCartaceo"
									property="campoDue" indexed="true" /></td>

								<td bgcolor="#FFCC99"><html:text name="itemReperCartaceo"
									property="nota" indexed="true" /></td>
								<td bgcolor="#FFCC99"><html:radio
									property="selezRadioReperCartaceo" value="${idxReperCartaceo}" /></td>
							</logic:iterate>
						</tr>
					</table>
				</c:if>




<c:choose>
	<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'M'}">
		<table border="0" bordercolor="#dde8f0">
			<tr>
				<th width="100" class="etichetta"><bean:message
					key="ricerca.num.standard" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.numero" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.tipo" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
				<th width="85" class="etichetta" bgcolor="#dde8f0">
				&nbsp;
				<html:submit
					styleClass="buttonImageDelLine" property="methodDettaglioTit"
					title="Cancella Num.Standard">
					<bean:message key="button.canNumStandard"
						bundle="gestioneBibliograficaLabels" />
				</html:submit> <html:submit styleClass="buttonImageNewLine"
					property="methodDettaglioTit" title="Inserisci Num.Standard">
					<bean:message key="button.insNumStandard"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></th>
			</tr>

			<logic:iterate id="itemNumStd"
					property="dettTitComVO.detTitoloPFissaVO.listaNumStandard"
					name="dettaglioTitoloForm" indexId="idxNumStd">
				<tr>
					<td></td>
					<td bgcolor="#FFCC99"><html:text name="itemNumStd"
						property="campoUno" indexed="true" /></td>
					<td bgcolor="#FFCC99"><html:select name="itemNumStd"
						property="campoDue" style="width:40px" indexed="true">
						<html:optionsCollection property="listaTipiNumStandard"
							value="codice" label="descrizioneCodice" />
					</html:select></td>
					<td bgcolor="#FFCC99"><html:text name="itemNumStd"
						property="nota" indexed="true" size="30" maxlength="30"/></td>
					<td bgcolor="#FFCC99"><html:radio
						property="selezRadioNumStandard" value="${idxNumStd}" /></td>
			</tr>
			</logic:iterate>
		</table>
	</c:when>
	<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'E'}">
		<table border="0" bordercolor="#dde8f0">
			<tr>
				<th width="100" class="etichetta"><bean:message
					key="ricerca.impronta" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="sintetica.progr" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.impronta" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
				<th width="85" class="etichetta" bgcolor="#dde8f0">
				&nbsp;
				<html:submit
					styleClass="buttonImageDelLine" property="methodDettaglioTit"
					title="Cancella Impronta">
					<bean:message key="button.canImpronta"
						bundle="gestioneBibliograficaLabels" />
				</html:submit> <html:submit styleClass="buttonImageNewLine"
					property="methodDettaglioTit" title="Inserisci Impronta">
					<bean:message key="button.insImpronta"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></th>
			</tr>

			<logic:iterate id="itemImp"
				property="dettTitComVO.detTitoloPFissaVO.listaImpronte"
				name="dettaglioTitoloForm" indexId="idxImp">
				<tr class="testoNormale">
					<td></td>
					<td bgcolor="#FFCC99"><html:text name="itemImp"
						property="campoUno" indexed="true" readonly="true" size="3"/></td>
					<td bgcolor="#FFCC99"><html:text name="itemImp"
						property="campoDue" indexed="true" size="35" /></td>
					<td bgcolor="#FFCC99"><html:text name="itemImp" property="nota"
						indexed="true" size="35" /></td>
					<td bgcolor="#FFCC99"><html:radio property="selezRadioImpronta"
						value="${idxImp}" /></td>
				</tr>
			</logic:iterate>
		</table>
	</c:when>
	<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'U'}">
		<table border="0" bordercolor="#dde8f0">
			<tr>
				<th width="100" class="etichetta"><bean:message
					key="ricerca.num.standard" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.numero" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.tipo" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
				<th width="85" class="etichetta" bgcolor="#dde8f0">
				&nbsp;
				<html:submit
					styleClass="buttonImageDelLine" property="methodDettaglioTit"
					title="Cancella Num.Standard">
					<bean:message key="button.canNumStandard"
						bundle="gestioneBibliograficaLabels" />
				</html:submit> <html:submit styleClass="buttonImageNewLine"
					property="methodDettaglioTit" title="Inserisci Num.Standard">
					<bean:message key="button.insNumStandard"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></th>
			</tr>
			<tr>
				<logic:iterate id="itemNumStd"
					property="dettTitComVO.detTitoloPFissaVO.listaNumStandard"
					name="dettaglioTitoloForm" indexId="idxNumStd">
					<td></td>
					<td bgcolor="#FFCC99"><html:text name="itemNumStd"
						property="campoUno" indexed="true" /></td>
					<td bgcolor="#FFCC99"><html:select name="itemNumStd"
						property="campoDue" style="width:40px" indexed="true">
						<html:optionsCollection property="listaTipiNumStandard"
							value="codice" label="descrizioneCodice" />
					</html:select></td>
					<td bgcolor="#FFCC99"><html:text name="itemNumStd"
						property="nota" indexed="true" size="30" maxlength="30"/></td>
					<td bgcolor="#FFCC99"><html:radio
						property="selezRadioNumStandard" value="${idxNumStd}" /></td>
			</tr>
			</logic:iterate>
		</table>
		<table border="0" bordercolor="#dde8f0">
			<tr>
				<th width="100" class="etichetta"><bean:message
					key="ricerca.impronta" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="sintetica.progr" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.impronta" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
				<th width="85" class="etichetta" bgcolor="#dde8f0">
				&nbsp;
				<html:submit
					styleClass="buttonImageDelLine" property="methodDettaglioTit"
					title="Cancella Impronta">
					<bean:message key="button.canImpronta"
						bundle="gestioneBibliograficaLabels" />
				</html:submit> <html:submit styleClass="buttonImageNewLine"
					property="methodDettaglioTit" title="Inserisci Impronta">
					<bean:message key="button.insImpronta"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></th>
			</tr>
			<logic:iterate id="itemImp"
				property="dettTitComVO.detTitoloPFissaVO.listaImpronte"
				name="dettaglioTitoloForm" indexId="idxImp">
				<tr class="testoNormale">
					<td></td>
					<td bgcolor="#FFCC99"><html:text name="itemImp"
						property="campoUno" indexed="true" readonly="true" size="3"/></td>
					<td bgcolor="#FFCC99"><html:text name="itemImp"
						property="campoDue" indexed="true" size="35" /></td>
					<td bgcolor="#FFCC99"><html:text name="itemImp" property="nota"
						indexed="true" size="35" /></td>
					<td bgcolor="#FFCC99"><html:radio property="selezRadioImpronta"
						value="${idxImp}" /></td>
				</tr>
			</logic:iterate>
		</table>
	</c:when>
	<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'C'}">
		<table border="0" bordercolor="#dde8f0">
			<tr>
				<th width="100" class="etichetta"><bean:message
					key="ricerca.num.standard" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.numero" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.tipo" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
				<th width="85" class="etichetta" bgcolor="#dde8f0">
				&nbsp;
				<html:submit
					styleClass="buttonImageDelLine" property="methodDettaglioTit"
					title="Cancella Num.Standard">
					<bean:message key="button.canNumStandard"
						bundle="gestioneBibliograficaLabels" />
				</html:submit> <html:submit styleClass="buttonImageNewLine"
					property="methodDettaglioTit" title="Inserisci Num.Standard">
					<bean:message key="button.insNumStandard"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></th>
			</tr>
			<tr>
				<logic:iterate id="itemNumStd"
					property="dettTitComVO.detTitoloPFissaVO.listaNumStandard"
					name="dettaglioTitoloForm" indexId="idxNumStd">
					<td></td>
					<td bgcolor="#FFCC99"><html:text name="itemNumStd"
						property="campoUno" indexed="true" /></td>
					<td bgcolor="#FFCC99"><html:select name="itemNumStd"
						property="campoDue" style="width:40px" indexed="true">
						<html:optionsCollection property="listaTipiNumStandard"
							value="codice" label="descrizioneCodice" />
					</html:select></td>
					<td bgcolor="#FFCC99"><html:text name="itemNumStd"
						property="nota" indexed="true" size="30" maxlength="30"/></td>
					<td bgcolor="#FFCC99"><html:radio
						property="selezRadioNumStandard" value="${idxNumStd}" /></td>
			</tr>
			</logic:iterate>
		</table>
				<table border="0" bordercolor="#dde8f0">
			<tr>
				<th width="100" class="etichetta"><bean:message
					key="ricerca.impronta" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="sintetica.progr" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.impronta" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
				<th width="85" class="etichetta" bgcolor="#dde8f0">
				&nbsp;
				<html:submit
					styleClass="buttonImageDelLine" property="methodDettaglioTit"
					title="Cancella Impronta">
					<bean:message key="button.canImpronta"
						bundle="gestioneBibliograficaLabels" />
				</html:submit> <html:submit styleClass="buttonImageNewLine"
					property="methodDettaglioTit" title="Inserisci Impronta">
					<bean:message key="button.insImpronta"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></th>
			</tr>

			<logic:iterate id="itemImp"
				property="dettTitComVO.detTitoloPFissaVO.listaImpronte"
				name="dettaglioTitoloForm" indexId="idxImp">
				<tr class="testoNormale">
					<td></td>
					<td bgcolor="#FFCC99"><html:text name="itemImp"
						property="campoUno" indexed="true" readonly="true" size="3"/></td>
					<td bgcolor="#FFCC99"><html:text name="itemImp"
						property="campoDue" indexed="true" size="35" /></td>
					<td bgcolor="#FFCC99"><html:text name="itemImp" property="nota"
						indexed="true" size="35" /></td>
					<td bgcolor="#FFCC99"><html:radio property="selezRadioImpronta"
						value="${idxImp}" /></td>
				</tr>
			</logic:iterate>
		</table>

	</c:when>
	<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'G'}">
		<table border="0" bordercolor="#dde8f0">
			<tr>
				<th width="100" class="etichetta"><bean:message
					key="ricerca.num.standard" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.numero" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.tipo" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
				<th width="85" class="etichetta" bgcolor="#dde8f0">
				&nbsp;
				<html:submit
					styleClass="buttonImageDelLine" property="methodDettaglioTit"
					title="Cancella Num.Standard">
					<bean:message key="button.canNumStandard"
						bundle="gestioneBibliograficaLabels" />
				</html:submit> <html:submit styleClass="buttonImageNewLine"
					property="methodDettaglioTit" title="Inserisci Num.Standard">
					<bean:message key="button.insNumStandard"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></th>
			</tr>
			<tr>
				<logic:iterate id="itemNumStd"
					property="dettTitComVO.detTitoloPFissaVO.listaNumStandard"
					name="dettaglioTitoloForm" indexId="idxNumStd">
					<td></td>
					<td bgcolor="#FFCC99"><html:text name="itemNumStd"
						property="campoUno" indexed="true" /></td>
					<td bgcolor="#FFCC99"><html:select name="itemNumStd"
						property="campoDue" style="width:40px" indexed="true">
						<html:optionsCollection property="listaTipiNumStandard"
							value="codice" label="descrizioneCodice" />
					</html:select></td>
					<td bgcolor="#FFCC99"><html:text name="itemNumStd"
						property="nota" indexed="true" size="30" maxlength="30"/></td>
					<td bgcolor="#FFCC99"><html:radio
						property="selezRadioNumStandard" value="${idxNumStd}" /></td>
			</tr>
			</logic:iterate>
		</table>
				<table border="0" bordercolor="#dde8f0">
			<tr>
				<th width="100" class="etichetta"><bean:message
					key="ricerca.impronta" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="sintetica.progr" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.impronta" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
				<th width="85" class="etichetta" bgcolor="#dde8f0">
				&nbsp;
				<html:submit
					styleClass="buttonImageDelLine" property="methodDettaglioTit"
					title="Cancella Impronta">
					<bean:message key="button.canImpronta"
						bundle="gestioneBibliograficaLabels" />
				</html:submit> <html:submit styleClass="buttonImageNewLine"
					property="methodDettaglioTit" title="Inserisci Impronta">
					<bean:message key="button.insImpronta"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></th>
			</tr>

			<logic:iterate id="itemImp"
				property="dettTitComVO.detTitoloPFissaVO.listaImpronte"
				name="dettaglioTitoloForm" indexId="idxImp">
				<tr class="testoNormale">
					<td></td>
					<td bgcolor="#FFCC99"><html:text name="itemImp"
						property="campoUno" indexed="true" readonly="true" size="3"/></td>
					<td bgcolor="#FFCC99"><html:text name="itemImp"
						property="campoDue" indexed="true" size="35" /></td>
					<td bgcolor="#FFCC99"><html:text name="itemImp" property="nota"
						indexed="true" size="35" /></td>
					<td bgcolor="#FFCC99"><html:radio property="selezRadioImpronta"
						value="${idxImp}" /></td>
				</tr>
			</logic:iterate>
		</table>

	</c:when>

	<c:otherwise>
		<table border="0" bordercolor="#dde8f0">
			<tr>
				<th width="100" class="etichetta"><bean:message
					key="ricerca.num.standard" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.numero" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.tipo" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta" bgcolor="#dde8f0"><bean:message
					key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
				<th width="85" class="etichetta" bgcolor="#dde8f0">
				&nbsp;
				<html:submit
					styleClass="buttonImageDelLine" property="methodDettaglioTit"
					title="Cancella Num.Standard">
					<bean:message key="button.canNumStandard"
						bundle="gestioneBibliograficaLabels" />
				</html:submit> <html:submit styleClass="buttonImageNewLine"
					property="methodDettaglioTit" title="Inserisci Num.Standard">
					<bean:message key="button.insNumStandard"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></th>
			</tr>
			<tr>
				<logic:iterate id="itemNumStd"
					property="dettTitComVO.detTitoloPFissaVO.listaNumStandard"
					name="dettaglioTitoloForm" indexId="idxNumStd">
					<td></td>
					<td bgcolor="#FFCC99"><html:text name="itemNumStd"
						property="campoUno" indexed="true" /></td>
					<td bgcolor="#FFCC99"><html:select name="itemNumStd"
						property="campoDue" style="width:40px" indexed="true">
						<html:optionsCollection property="listaTipiNumStandard"
							value="codice" label="descrizioneCodice" />
					</html:select></td>
					<td bgcolor="#FFCC99"><html:text name="itemNumStd"
						property="nota" indexed="true" size="30" maxlength="30"/></td>
					<td bgcolor="#FFCC99"><html:radio
						property="selezRadioNumStandard" value="${idxNumStd}" /></td>
			</tr>
			</logic:iterate>
		</table>

	</c:otherwise>



</c:choose>