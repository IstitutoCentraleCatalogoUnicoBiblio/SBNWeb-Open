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