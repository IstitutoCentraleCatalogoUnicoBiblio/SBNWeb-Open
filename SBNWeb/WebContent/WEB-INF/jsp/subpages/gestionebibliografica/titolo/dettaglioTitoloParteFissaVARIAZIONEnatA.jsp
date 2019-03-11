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
		<c:choose>
			<c:when test="${dettaglioTitoloForm.tipoMateriale ne 'U'}">
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
			</c:when>
		</c:choose>
	</table>
	<table border="0">
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="dettaglio.norme" bundle="gestioneBibliograficaLabels" /></td>
			<td  width="100"><html:select
						property="dettTitComVO.detTitoloPFissaVO.norme"
						style="width:75px">
						<html:optionsCollection property="listaNormaCatalografiche" value="descrizione"
							label="descrizione" />
					</html:select></td>



			<td class="etichetta"><bean:message key="dettaglio.agenzia"
				bundle="gestioneBibliograficaLabels" /></td>

			<td width="240"><html:select
				property="dettTitComVO.detTitoloPFissaVO.fontePaese"
				style="width:47px">
				<html:optionsCollection property="listaPaese" value="codice"
					label="descrizioneCodice" />
			</html:select> <html:text
				property="dettTitComVO.detTitoloPFissaVO.fonteAgenzia" size="5"></html:text></td>
		</tr>
	</table>
	<table border="0">
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="ricerca.isadnOLD" bundle="gestioneBibliograficaLabels" /></td>
			<td class="testoNormale"><html:text
				property="dettTitComVO.detTitoloPFissaVO.isadn" size="5" readonly="true"></html:text></td>

		</tr>

		<tr>
			<td class="etichetta"><bean:message
				key="catalogazione.areaT.titolo"
				bundle="gestioneBibliograficaLabels" />
			</td>

			<c:choose>
				<c:when test="${dettaglioTitoloForm.tipoMateriale eq 'U'}">
					<td><html:textarea property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo" cols="70"
								rows="5" readonly="true"></html:textarea></td>
				</c:when>
				<c:otherwise>
					<td><html:textarea
						property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo" cols="70" rows="5"></html:textarea>
						 <sbn:tastiera limit="1200" property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo" name="dettaglioTitoloForm"></sbn:tastiera>
					</td>
				</c:otherwise>
			</c:choose>
		</tr>
	</table>

	<c:choose>
		<c:when test="${dettaglioTitoloForm.tipoMateriale ne 'U'}">
			<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.formaOpera.naturaA" bundle="gestioneBibliograficaLabels" /></td>

						<td width="200"><html:select
						property="dettTitComVO.detTitoloPFissaVO.formaOpera231"
						style="width:110px">
						<html:optionsCollection property="listaFormaOpera231" value="codice"
							label="descrizioneCodice" />
					</html:select></td>
					</tr>

					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.dataOpera.naturaA" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100" class="testoNormale"><html:text
							property="dettTitComVO.detTitoloPFissaVO.dataOpera231" size="10"></html:text></td>
					</tr>
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.paese" bundle="gestioneBibliograficaLabels" /></td>

						<td width="100"><html:select
							property="dettTitComVO.detTitoloPFissaVO.paese"
							style="width:55px">
							<html:optionsCollection property="listaPaese" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.altreCarat.naturaA" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100" class="testoNormale"><html:text
							property="dettTitComVO.detTitoloPFissaVO.altreCarat231" size="10"></html:text></td>
					</tr>
				</table>
		</c:when>
	</c:choose>



	<table border="0">
		<tr>
			<th width="100" class="etichetta"><bean:message
				key="dettaglio.fonti" bundle="gestioneBibliograficaLabels" /></th>
			<th width="50" class="etichetta" bgcolor="#dde8f0"><bean:message
				key="sifRepertori.siNo" bundle="gestioneBibliograficaLabels" /></th>
			<th width="100" class="etichetta" bgcolor="#dde8f0"><bean:message
				key="sifRepertori.sigl" bundle="gestioneBibliograficaLabels" /></th>
			<th width="200" class="etichetta" bgcolor="#dde8f0"><bean:message
				key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
			<th width="85" class="etichetta" bgcolor="#dde8f0">
			&nbsp;
			<html:submit
				styleClass="buttonImageDelLine" property="methodDettaglioTit"
				alt="Cancella Repertorio">
				<bean:message key="button.canRepertorio"
					bundle="gestioneBibliograficaLabels" />
			</html:submit> <html:submit styleClass="buttonImageNewLine"
				property="methodDettaglioTit" alt="Inserisci nuova riga">
				<bean:message key="button.insRepertorio"
					bundle="gestioneBibliograficaLabels" />
			</html:submit> <html:submit styleClass="buttonImageHlpRep"
				property="methodDettaglioTit" alt="Cerca Repertorio">
				<bean:message key="button.hlpRepertorio"
					bundle="gestioneBibliograficaLabels" />
			</html:submit></th>
		</tr>
		<logic:iterate id="item" property="listaRepertoriModificato"
			name="dettaglioTitoloForm" indexId="idxRepert">
			<tr class="testoNormale">
				<td></td>
				<td bgcolor="#FFCC99"><html:text name="item" property="campoUno"
					indexed="true" /></td>
				<td bgcolor="#FFCC99"><html:text name="item" property="campoDue"
					indexed="true" /></td>
				<td bgcolor="#FFCC99"><html:text name="item" property="nota"
					indexed="true" size="25" /></td>
				<td bgcolor="#FFCC99"><html:radio property="selezRadioRepertori"
					value="${idxRepert}" /></td>
			</tr>
		</logic:iterate>
	</table>

	<table border="0">
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="dettaglio.notaInform" bundle="gestioneBibliograficaLabels" /></td>
			<td><html:textarea
				property="dettTitComVO.detTitoloPFissaVO.noteInformative"
				cols="70" rows="1"></html:textarea></td>
		</tr>
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="dettaglio.notacatal" bundle="gestioneBibliograficaLabels" /></td>
			<td><html:textarea
				property="dettTitComVO.detTitoloPFissaVO.noteCatalogatore"
				cols="70" rows="1"></html:textarea></td>
		</tr>
	</table>