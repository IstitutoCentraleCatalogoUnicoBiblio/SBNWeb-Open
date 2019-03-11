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

	</tr>
</table>

<!--  almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S e tutti i Materiali-->
<!-- almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro-->
<!-- almaviva2 Evolutiva Dicembre 2014 per Gestione Area0 anche per gli spogli (Natura N) -->
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




	<!-- Inizio modifica BUG MANTIS 3680 seconda nota - almaviva2 19.04.2010
	<c:choose>
		<c:when
			test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat ne 'E'}">

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
					key="catalogazione.areaT.datiMatematici"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitDatiMatem"
					cols="70" rows="1"></html:textarea> <sbn:tastiera limit="1200"
					property="dettTitComVO.detTitoloPFissaVO.areaTitDatiMatem"
					name="dettaglioTitoloForm"></sbn:tastiera></td>
			</tr>
		</c:when>
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
	  Fine modifica BUG MANTIS 3680 seconda nota - almaviva2 19.04.2010
	-->





	<!-- Inizio modifica Segnalazione IBIMUS per aree da inserire - almaviva2 07.07.2010 -->
	<c:choose>
		<c:when
			test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'U'}">
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
		</c:when>
	</c:choose>
	<!-- Fine modifica Segnalazione IBIMUS per aree da inserire - almaviva2 07.07.2010 -->

	<!-- Inizio modifica Segnalazione MANTIS 3820 per aree da inserire - almaviva2 08.07.2010 -->
	<c:choose>
		<c:when
			test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'C'}">
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
					key="catalogazione.areaT.datiMatematici"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitDatiMatem"
					cols="70" rows="1"></html:textarea> <sbn:tastiera limit="1200"
					property="dettTitComVO.detTitoloPFissaVO.areaTitDatiMatem"
					name="dettaglioTitoloForm"></sbn:tastiera></td>
			</tr>
		</c:when>
	</c:choose>
	<!-- Fine modifica Segnalazione MANTIS 3820 per aree da inserire - almaviva2 08.07.2010 -->


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



	<!-- Inizio modifica BUG MANTIS 3680 seconda nota - almaviva2 19.04.2010 -->
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
	<!-- Fine modifica BUG MANTIS 3680 seconda nota - almaviva2 19.04.2010 -->



</table>


<table border="0">
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
											onclick="window.open(this.value)" />
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





<!-- Inizio modifica Dicembre 2014 Anche gli Spogli hanno un Num. Standard solo di tipo L (LASTRA) -->
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
		</logic:iterate>
	</tr>
</table>







