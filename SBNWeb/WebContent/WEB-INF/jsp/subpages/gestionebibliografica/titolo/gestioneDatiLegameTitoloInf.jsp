<!--	SBNWeb - Rifacimento ClientServer
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
			key="dettaglio.tipoLegame" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" class="testoNormale"><html:text
			property="areaDatiVarTitoloVO.tipoLegame" size="10" readonly="true"></html:text></td>



		<td width="60" class="etichetta"><bean:message
			key="dettaglio.sequenzaLegame" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" class="testoNormale"><html:text
			property="areaDatiVarTitoloVO.sequenza" size="10"></html:text></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="dettaglio.notaLegame" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" ><html:textarea
			property="areaDatiVarTitoloVO.noteLegame" cols="60" rows="1"></html:textarea></td>
	</tr>
</table>

<hr color="#dde8f0" />

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message key="ricerca.bid"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" class="testoNormale"><html:text
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.bid" size="8"
			readonly="true"></html:text></td>
		<td width="100" class="etichetta"><bean:message key="ricerca.natura"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="100"><html:text
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.natura" size="5"
			readonly="true"
			title="${gestioneLegameTitoloTitoloForm.descNatura}">
		</html:text></td>
		<td width="60" class="etichetta"><bean:message
			key="ricerca.tipoMateriale" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100"><html:text
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.tipoMat" size="5"
			readonly="true"
			title="${gestioneLegameTitoloTitoloForm.descTipoMat}">
		</html:text></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="sintetica.livelloaut" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" class="testoNormale"><html:select
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.livAut"
			style="width:40px">
			<html:optionsCollection property="listaLivAut" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="60" class="etichetta"><bean:message
			key="ricerca.tipoRecord" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100"><html:select
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.tipoRec"
			style="width:40px">
			<html:optionsCollection property="listaTipoRec" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message key="ricerca.paese"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="100"><html:select
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.paese"
			style="width:40px">
			<html:optionsCollection property="listaPaese" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message key="ricerca.lingua"
			bundle="gestioneBibliograficaLabels" /></td>

		<td width="100"><html:select
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.lingua1"
			style="width:40px">
			<html:optionsCollection property="listaLingua1" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="100"><html:select
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.lingua2"
			style="width:40px">
			<html:optionsCollection property="listaLingua2" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="100"><html:select
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.lingua3"
			style="width:40px">
			<html:optionsCollection property="listaLingua3" value="codice"
				label="descrizioneCodice" />
		</html:select></td>

	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message key="ricerca.genere"
			bundle="gestioneBibliograficaLabels" /></td>

		<td width="100"><html:select
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.genere1"
			style="width:40px">
			<html:optionsCollection property="listaGenere1" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="100"><html:select
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.genere2"
			style="width:40px">
			<html:optionsCollection property="listaGenere2" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="100"><html:select
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.genere3"
			style="width:40px">
			<html:optionsCollection property="listaGenere3" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="100"><html:select
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.genere4"
			style="width:40px">
			<html:optionsCollection property="listaGenere4" value="codice"
				label="descrizioneCodice" />
		</html:select></td>

	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="ricerca.tipo.Data" bundle="gestioneBibliograficaLabels" /></td>

		<td width="100"><html:select
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.tipoData"
			style="width:40px">
			<html:optionsCollection property="listaTipoData" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="60" class="etichetta"><bean:message
			key="ricerca.dataPubblicazione1" bundle="gestioneBibliograficaLabels" /></td>
		<td width="40" class="testoNormale"><html:text
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.dataPubbl1" size="15"></html:text></td>
		<td width="60" class="etichetta"><bean:message
			key="ricerca.dataPubblicazione2" bundle="gestioneBibliograficaLabels" /></td>
		<td width="40" class="testoNormale"><html:text
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.dataPubbl2" size="15"></html:text></td>

	</tr>
</table>

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="catalogazione.areaT.titolo" bundle="gestioneBibliograficaLabels" /></td>
		<td width="400" ><html:textarea
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.areaTitTitolo"
			cols="70" rows="1"></html:textarea></td>
	</tr>
	<tr>
		<td class="etichetta"><bean:message key="catalogazione.areaT.edizione"
			bundle="gestioneBibliograficaLabels" /></td>
		<td ><html:textarea
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.areaTitEdiz"
			cols="70" rows="1"></html:textarea></td>
	</tr>
	<tr>
		<td class="etichetta"><bean:message
			key="catalogazione.areaT.numerazione"
			bundle="gestioneBibliograficaLabels" /></td>
		<td ><html:textarea
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.areaTitNumer"
			cols="70" rows="1"></html:textarea></td>
	</tr>
	<tr>
		<td class="etichetta"><bean:message
			key="catalogazione.areaT.pubblicazione"
			bundle="gestioneBibliograficaLabels" /></td>
		<td ><html:textarea
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.areaTitPubbl"
			cols="70" rows="1"></html:textarea></td>
	</tr>
	<tr>
		<td class="etichetta"><bean:message
			key="catalogazione.areaT.descrFisica"
			bundle="gestioneBibliograficaLabels" /></td>
		<td ><html:textarea
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.areaTitDescrFis"
			cols="70" rows="1"></html:textarea></td>
	</tr>
	<tr>
		<td class="etichetta"><bean:message key="catalogazione.areaT.note"
			bundle="gestioneBibliograficaLabels" /></td>
		<td ><html:textarea
			property="areaDatiVarTitoloVO.detTitoloPFissaVO.areaTitNote"
			cols="70" rows="1"></html:textarea></td>
	</tr>
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
			styleClass="buttonImageDelLine" property="methodGestLegTitTit"
			title="Cancella Impronta">
			<bean:message key="button.canImpronta"
				bundle="gestioneBibliograficaLabels" />
		</html:submit> <html:submit styleClass="buttonImageNewLine"
			property="methodGestLegTitTit" title="Inserisci Impronta">
			<bean:message key="button.insImpronta"
				bundle="gestioneBibliograficaLabels" />
		</html:submit></th>
	</tr>
	<logic:iterate id="itemImp"
		property="areaDatiVarTitoloVO.detTitoloPFissaVO.listaImpronte"
		name="gestioneLegameTitoloTitoloForm" indexId="idxImp">
		<tr class="testoNormale">
			<td></td>
			<td bgcolor="#FFCC99"><html:text name="itemImp"
				property="campoUno" indexed="true" /></td>
			<td bgcolor="#FFCC99"><html:text name="itemImp"
				property="campoDue" indexed="true" size="25" /></td>
			<td bgcolor="#FFCC99"><html:text name="itemImp" property="nota"
				indexed="true" size="25" /></td>
			<td bgcolor="#FFCC99"><html:radio property="selezRadioImpronta"
				value="${idxImp}" /></td>
		</tr>
	</logic:iterate>
</table>

