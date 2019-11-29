<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<table width="100%" border="0" cellpadding="1" cellspacing="1">
	<tr>
		<td colspan="7" class="etichetta">&nbsp;</td>
	</tr>
	<sbn:checkAttivita idControllo="DOC_BIB_NUM_DOC">
	<tr>
		<td class="etichetta" width="15%" scope="col" align="left"><bean:message
			key="servizi.documenti.codBib" bundle="serviziLabels" /></td>
		<td scope="col" align="left"><html:text styleId="testoNormale"
			property="documento.codBib" size="4" readonly="true"></html:text></td>
		<td class="etichetta" scope="col" align="right"><bean:message
			key="servizi.documenti.codDoc" bundle="serviziLabels" /></td>
		<td scope="col" align="left"><html:text styleId="testoNormale"
			property="documento.cod_doc_lett" size="5" readonly="true"></html:text>
		</td>
		<td class="etichetta" scope="col" align="left">&nbsp;</td>
	</tr>
	</sbn:checkAttivita>
	<sbn:checkAttivita idControllo="DOC_TIPO_DOCUMENTO">
	<tr>
		<td class="etichetta" scope="col" align="right"><bean:message
		key="servizi.documenti.tipoDoc" bundle="serviziLabels" /></td>
	<td scope="col" colspan="2" align="left"><html:select
		styleClass="testoNormale"
		property="documento.tipo_doc_lett" disabled="true">
		<html:optionsCollection property="listaTipoDocumento"
			value="codice" label="descrizione" />
	</html:select></td>
	</tr>
	<tr>
		<td class="etichetta" scope="col" align="right"><bean:message
			key="servizi.documenti.fonte" bundle="serviziLabels" /></td>
		<td scope="col" colspan="2" align="left">

		<html:hidden property="cambiato" />
		<html:select
			styleClass="testoNormale" property="documento.fonte"
			disabled="true" onchange="validateSubmit('cambiato', 'SI');">
			<html:optionsCollection property="listaTipoFonte" value="codice"
				label="descrizione" />
		</html:select>
		<%--
		<html:select
			styleClass="testoNormale" property="documento.fonte"
			 onchange="validateSubmit('cambiato', 'SI');">
			<html:optionsCollection property="listaTipoFonte" value="codice"
				label="descrizione" />
		</html:select>
		--%>
		</td>

	</tr>
	<c:if test="${navForm.fonteLettore}">
		<tr>
			<td class="etichetta" width="15%" scope="col" align="left"><bean:message
				key="servizi.documenti.utente" bundle="serviziLabels" /></td>
			<td scope="col" align="left"><html:text styleId="testoNormale"
				property="documento.utente" size="20" maxlength="25"
				readonly="true"></html:text></td>
			<td width="10%" scope="col" class="etichetta" align="left"><bean:message
				key="servizi.utenti.cognomeNome" bundle="serviziLabels" /></td>
			<td scope="col" colspan="2" align="left"><html:text
				styleId="testoNormale" property="documento.cognomeNome" size="30"
				readonly="true"></html:text></td>
		</tr>
	</c:if>
	</sbn:checkAttivita>
	<sbn:checkAttivita idControllo="DOC_BID_TITOLO">
		<tr>
			<td class="etichetta" scope="col" align="left"><bean:message
				key="servizi.documenti.bid" bundle="serviziLabels" /></td>
			<td scope="col" align="left"><html:text
				styleId="testoNormale" property="documento.bid" size="11"
				maxlength="10" readonly="${navForm.conferma}"></html:text>
			<!-- <html:submit
			styleClass="buttonImage" property="methodGestioneDoc"
			>
			<bean:message key="ordine.bottone.searchTit" bundle="serviziLabels" />
		</html:submit> --></td>
			<td class="etichetta" scope="col" colspan="2"
				align="right"><bean:message key="servizi.documenti.dataIns"
				bundle="serviziLabels" /> <html:text styleId="testoNormale"
				property="documento.dataIns" size="10" readonly="true"></html:text></td>
			<td class="etichetta" scope="col" colspan="2"
				align="left"><bean:message key="servizi.documenti.dataAgg"
				bundle="serviziLabels" /> <html:text styleId="testoNormale"
				property="documento.dataAgg" size="10" readonly="true"></html:text></td>

		</tr>
		<tr>
			<td class="etichetta" scope="col" align="right"><bean:message
				key="servizi.documenti.tipoRecord" bundle="serviziLabels" /></td>
			<td scope="col" colspan="2" align="left"><html:select
				styleClass="testoNormale" property="documento.tipoRecord">
				<html:optionsCollection property="listaTipoRecord" value="cd_tabellaTrim" label="ds_tabella" />
			</html:select></td>
		</tr>
		<tr>
			<td class="etichetta" scope="col" align="left"><bean:message
				key="servizi.documenti.titolo" bundle="serviziLabels" /></td>
			<td scope="col" colspan="6" align="left"><html:text
				styleId="testoNormale" property="documento.titolo" size="90"
				maxlength="240" /> <sbn:tastiera limit="240"
				property="documento.titolo" name="navForm"
				visible="${!navForm.conferma}" /></td>
		</tr>
	</sbn:checkAttivita>
	<sbn:checkAttivita idControllo="SEGNATURA|FORNITRICE">
		<tr>
			<td class="etichetta" scope="col" align="left"><bean:message
				key="servizi.documenti.segnatura" bundle="serviziLabels" /></td>
			<td scope="col" colspan="6" align="left">
			<sbn:disableAll checkAttivita="SEGNATURA">
				<html:text styleId="testoNormale" property="documento.segnatura" size="90" maxlength="40"/>
			</sbn:disableAll>
			</td>
		</tr>
	</sbn:checkAttivita>

		<tr>
			<td class="etichetta" scope="col" align="left"><bean:message
				key="servizi.documenti.autore" bundle="serviziLabels" /></td>
			<td scope="col" colspan="6" align="left"><html:text
				styleId="testoNormale" property="documento.autore" size="40"
				maxlength="160" ></html:text>
			<sbn:tastiera limit="160" property="documento.autore"
				name="navForm"
				visible="${!navForm.conferma}" /></td>

		</tr>

	<sbn:disableAll checkAttivita="DETTAGLIO_MOVIMENTO" inverted="true">
	<sbn:checkAttivita idControllo="DOC_NATURA">
	<tr>
		<td class="etichetta" scope="col" align="right"><bean:message
			key="servizi.documenti.natura" bundle="serviziLabels" /></td>
		<td scope="col" colspan="2" align="left"><html:select
			styleClass="testoNormale" property="documento.natura"
			>
			<html:optionsCollection property="listaNature" value="cd_tabellaTrim"
				label="ds_tabella" />
		</html:select></td>
	</tr>
	<sbn:checkAttivita idControllo="POSSEDUTO">
	<tr>
		<td class="etichetta" scope="col" align="right"><bean:message
			key="servizi.documenti.codFrui" bundle="serviziLabels" /></td>
		<td scope="col" colspan="2" align="left"><html:select
			styleClass="testoNormale" property="documento.codFruizione"
			>
			<html:optionsCollection property="listaTipoCodFruizione"
				value="codice" label="descrizione" />
		</html:select></td>
	</tr>
	<tr>
		<td class="etichetta" scope="col" align="right"><bean:message
			key="servizi.documenti.codNoDisp" bundle="serviziLabels" /></td>
		<td scope="col" colspan="2" align="left"><html:select
			styleClass="testoNormale" property="documento.codNoDisp"
			>
			<html:optionsCollection property="listaTipoCodNoDisp"
				value="codice" label="descrizione" />
		</html:select></td>
	</tr>
	</sbn:checkAttivita>
	</sbn:checkAttivita>
	</sbn:disableAll>
	<sbn:checkAttivita idControllo="POSSEDUTO">
	<tr>
		<td class="etichetta" scope="col" align="left"><bean:message
			key="servizi.documenti.note" bundle="serviziLabels" /></td>
		<td scope="col" colspan="6" align="left"><html:textarea
			styleId="testoNormale" property="documento.note" rows="1"
			cols="50" readonly="${navForm.conferma}"></html:textarea>
		<sbn:tastiera limit="160" property="documento.note"
			name="navForm"
			visible="${!navForm.conferma}" /></td>
	</tr>
	</sbn:checkAttivita>
	<tr>
		<td class="etichetta" scope="col" align="left">Ente curatore</td>
		<td scope="col" colspan="3" align="left"><html:text
			styleId="testoNormale" property="documento.enteCuratore" size="50" maxlength="50" /></td>
		<td class="etichetta" scope="col" align="right">Data pubb.</td>
		<td scope="col" align="left"><html:text styleId="testoNormale"
			property="documento.dataPubb" size="10" maxlength="20"/></td>
	</tr>
	<tr>
		<td class="etichetta" scope="col" align="left"><bean:message
			key="servizi.documenti.editore" bundle="serviziLabels" /></td>
		<td scope="col" colspan="3" align="left"><html:text
			styleId="testoNormale" property="documento.editore" size="50"
			maxlength="50" readonly="${navForm.conferma}"></html:text></td>
		<td class="etichetta" scope="col" align="right"><bean:message
			key="servizi.documenti.luogo" bundle="serviziLabels" /></td>
		<td scope="col" align="left"><html:text styleId="testoNormale"
			property="documento.luogoEdizione" size="10" maxlength="30"
			readonly="${navForm.conferma}"></html:text></td>
	</tr>

	<tr>
		<td width="10%" scope="col" class="etichetta" align="left"><bean:message
			key="servizi.documenti.paese" bundle="serviziLabels" /></td>
		<td scope="col" align="left"><html:select style="width:100px"
			styleClass="testoNormale" property="documento.paese"
			>
			<html:optionsCollection property="listaPaesi" value="codice"
				label="descrizione" />
		</html:select></td>
		<td width="10%" scope="col" class="etichetta" align="right"><bean:message
			key="servizi.documenti.lingua" bundle="serviziLabels" /></td>
		<td scope="col" align="left"><html:select style="width:100px;"
			styleClass="testoNormale" property="documento.lingua"
			>
			<html:optionsCollection property="listaLingue" value="codice"
				label="descrizione" />
		</html:select></td>
		<td class="etichetta" scope="col" align="right"><bean:message
			key="servizi.documenti.anno" bundle="serviziLabels" /></td>
		<sbn:disableAll checkAttivita="DETTAGLIO_MOVIMENTO" inverted="true">
		<td scope="col" align="left"><html:text styleId="testoNormale"
			property="documento.annoEdizione" size="4" maxlength="4"
			readonly="${navForm.conferma}"></html:text></td>
		</sbn:disableAll>

	</tr>

	<tr>
	<td width="10%" scope="col" class="etichetta" align="left"><bean:message
			key="servizi.documenti.numeroStd" bundle="serviziLabels" /></td>
		<td scope="col" align="left"><html:select style="width:100px"
			styleClass="testoNormale" property="documento.tipoNumStd"
			>
			<html:optionsCollection property="listaTipoNumeroStd" value="cd_tabellaTrim"
				label="ds_tabella" />
		</html:select>
		&nbsp;<html:text
			styleId="testoNormale" property="documento.numeroStd" size="25"
			maxlength="25" readonly="${navForm.conferma}"></html:text></td>
	</tr>
	<tr>
		<td colspan="7" class="etichetta">&nbsp;</td>
	</tr>
	<tr>
		<td class="etichetta" align="left">Volume</td>
		<td class="testo" align="left"><html:text
				property="documento.num_volume" maxlength="30" size="10" /></td>
		<td class="etichetta" align="right">Annata</td>
		<td class="testo" align="left"><html:text
				property="documento.annata" maxlength="10" size="10" /></td>
	</tr>
	<sbn:checkAttivita idControllo="DOC_DATI_SPOGLIO">
		<tr>
			<td class="etichetta" align="left">Fa parte di</td>
			<td class="testo" align="left"><html:text
					property="documento.faParte" maxlength="50" size="50" /></td>
		</tr>

		<tr>
			<td class="etichetta" align="left">Titolo articolo</td>
			<td class="testo" align="left">
				<!--&r_comment_id_art_tit--> <html:text
					property="documento.titoloArticolo" maxlength="50" size="50" />
			</td>
		</tr>
		<tr>
			<td class="etichetta" align="left">Autore articolo</td>
			<td class="testo" align="left">
				<!--&r_comment_id_art_aut--> <html:text
					property="documento.autoreArticolo" maxlength="50" size="50" />
			</td>
		</tr>
		<tr>
			<td class="etichetta" align="left">Fascicolo</td>
			<td class="testo" align="left"><html:text
					property="documento.fascicolo" size="30" maxlength="50" /> <!--&r_comment_id_fascic--></td>
			<td class="etichetta" align="right">Pagine</td>
			<td class="testo" align="left"><html:text
					property="documento.pagine" size="10" maxlength="50" /> <!--&r_comment_id_pagine--></td>
		</tr>
		<tr>
			<td class="etichetta" align="left">Riferimenti</td>
			<td class="testo" align="left"><html:text
					property="documento.fonteRif" maxlength="50" size="50" /> <!--&r_comment_id_ref_sou--></td>
		</tr>
	</sbn:checkAttivita>
	<tr>
		<td colspan="7" class="etichetta">&nbsp;</td>
	</tr>

</table>