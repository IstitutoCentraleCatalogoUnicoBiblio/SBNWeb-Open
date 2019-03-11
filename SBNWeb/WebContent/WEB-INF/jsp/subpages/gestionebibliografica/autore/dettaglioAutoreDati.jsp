<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area inferiore con i criteri di ricerca
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

<c:choose>
	<c:when test="${dettaglioAutoreForm.tipoProspettazione eq 'DET'}">

		<c:choose>
			<c:when test="${dettaglioAutoreForm.dettAutoreVO.forma eq 'A'}">
				<table border="0">
					<tr>
						<td width="80" class="etichetta"><bean:message
							key="dettaglio.norme" bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:text property="dettAutoreVO.norme"
							readonly="true"></html:text></td>
						<td class="etichetta"><bean:message key="dettaglio.agenzia"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:text
							property="dettAutoreVO.agenzia" readonly="true"></html:text></td>
						<td class="etichetta"><bean:message key="ricerca.isadn"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:text property="dettAutoreVO.isadn"
							readonly="true"></html:text></td>
					</tr>
				</table>

			</c:when>
		</c:choose>

		<table border="0">
			<tr>
				<td width="80" class="etichetta"><bean:message key="ricerca.vid"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:text property="dettAutoreVO.vid"
					readonly="true" size="10"></html:text></td>
				<td class="etichetta"><bean:message key="sintetica.livelloaut"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:text property="dettAutoreVO.livAut"
					readonly="true" title="${dettaglioAutoreForm.descLivAut}" size="5">
				</html:text></td>
				<td class="etichetta"><bean:message key="sintetica.forma"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:text property="dettAutoreVO.forma" readonly="true"
					title="${dettaglioAutoreForm.descFormaNome}" size="5">
				</html:text></td>
				<td class="etichetta"><bean:message key="sintetica.tiponome"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:text property="dettAutoreVO.tipoNome" readonly="true"
					title="${dettaglioAutoreForm.descTipoNome}" size="5">
				</html:text></td>
				<c:choose>
					<c:when test="${dettaglioAutoreForm.dettAutoreVO.forma eq 'A'}">
						<td class="etichetta"><bean:message key="ricerca.paese"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text property="dettAutoreVO.paese" readonly="true"
							title="${dettaglioAutoreForm.descPaese}" size="5">
						</html:text></td>

					</c:when>
				</c:choose>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="80" class="etichetta"><bean:message key="ricerca.nome"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="400"><html:textarea
					property="dettAutoreVO.nome" rows="3" cols="70" readonly="true"></html:textarea>
				</td>
			</tr>
		</table>

		<c:choose>
			<c:when test="${dettaglioAutoreForm.dettAutoreVO.forma eq 'A'}">
				<table border="0">
					<tr>
						<td width="80" class="etichetta"><bean:message
							key="ricerca.lingua" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:text property="dettAutoreVO.lingua" size="5"
							readonly="true" title="${dettaglioAutoreForm.descLingua1}">
						</html:text></td>
						<td width="40" class="etichetta"><bean:message
							key="sintetica.datazione" bundle="gestioneBibliograficaLabels" /></td>
						<td width="120" class="testoNormale"><html:text
							property="dettAutoreVO.datazioni" readonly="true"></html:text></td>
					</tr>
				</table>
			</c:when>
		</c:choose>

		<table border="0">
			<tr>
				<td width="80" class="etichetta"><bean:message
					key="dettaglio.notaInform" bundle="gestioneBibliograficaLabels" /></td>
				<td width="300" class="testoNormale"><html:textarea
					property="dettAutoreVO.notaInformativa" rows="3" cols="70"
					readonly="true"></html:textarea></td>
			</tr>
			<c:choose>
				<c:when test="${dettaglioAutoreForm.dettAutoreVO.forma eq 'A'}">
					<tr>
						<td class="etichetta"><bean:message key="dettaglio.fonti"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:textarea
							property="dettAutoreVO.legamiRepertori" rows="3" cols="70"
							readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="dettaglio.notacatal"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:textarea
							property="dettAutoreVO.notaCatalogatore" rows="3" cols="70"
							readonly="true"></html:textarea></td>
					</tr>
				</c:when>
			</c:choose>
		</table>
	</c:when>

	<c:otherwise>

		<table border="0">
			<tr>
				<td width="40" class="etichetta"><bean:message key="dettaglio.norme"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:select
						property="dettAutoreVO.norme"
						style="width:75px">
						<html:optionsCollection property="listaNormaCatalografiche" value="descrizione"
							label="descrizione" />
					</html:select></td>

				<td width="40" class="etichetta"><bean:message
					key="dettaglio.agenzia" bundle="gestioneBibliograficaLabels" /></td>

				<td width="240">
				<html:select property="dettAutoreVO.fontePaese"	style="width:40px">
					<html:optionsCollection property="listaPaese" value="codice"
						label="descrizioneCodice" />
				</html:select>
				 <html:text property="dettAutoreVO.fonteAgenzia"
					size="5"></html:text></td>

				<c:choose>
					<c:when test="${dettaglioAutoreForm.flagCondiviso eq true}">
						<td width="40" class="etichetta"><bean:message key="ricerca.isadn"	bundle="gestioneBibliograficaLabels" /></td>
						<td width="120" class="testoNormale"><html:text	property="dettAutoreVO.isadn" size="16"></html:text></td>
					</c:when>
					<c:otherwise>
						<td width="40" class="etichetta"><bean:message key="ricerca.isadn"	bundle="gestioneBibliograficaLabels" /></td>
						<td width="120" class="testoNormale"><html:text	property="dettAutoreVO.isadn" size="16" readonly="true"></html:text></td>
					</c:otherwise>
				</c:choose>

			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="40" class="etichetta"><bean:message key="ricerca.vid"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="70" class="testoNormale"><html:text
					property="dettAutoreVO.vid" size="10" readonly="true"></html:text></td>
				<td width="40" class="etichetta"><bean:message
					key="sintetica.livelloaut" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100">
				<html:select property="dettAutoreVO.livAut" style="width:40px">

				<html:optionsCollection property="listaLivAut" value="codice"
						label="descrizioneCodice" /></html:select></td>

				<td width="40" class="etichetta"><bean:message key="sintetica.forma" bundle="gestioneBibliograficaLabels" /></td>
				<c:choose>
					<c:when test="${dettaglioAutoreForm.tipoProspettazione eq 'AGG'}">
						<td width="50" class="testoNormale"><html:text property="dettAutoreVO.forma" readonly="true" size="5"></html:text></td>
					</c:when>
					<c:when test="${dettaglioAutoreForm.tipoProspettazione eq 'INS'}">
						<td width="50" class="testoNormale"><html:text property="dettAutoreVO.forma" size="5"></html:text></td>
					</c:when>
				</c:choose>


				<td width="40" class="etichetta"><bean:message
					key="sintetica.tiponome" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:select
					property="dettAutoreVO.tipoNome" style="width:40px">
					<html:optionsCollection property="listaTipoNome" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
				<td width="40" class="etichetta"><bean:message key="ricerca.paese"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:select property="dettAutoreVO.paese"
					style="width:47px">
					<html:optionsCollection property="listaPaese" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="40" class="etichetta"><bean:message key="ricerca.nome"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale">
					<html:textarea property="dettAutoreVO.nome" rows="3" cols="70"></html:textarea>
					<sbn:tastiera limit="1200" property="dettAutoreVO.nome" name="dettaglioAutoreForm"></sbn:tastiera>
				</td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="40" class="etichetta"><bean:message key="ricerca.lingua"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:select property="dettAutoreVO.lingua"
					style="width:55px">
					<html:optionsCollection property="listaLingua1" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
				<td width="40" class="etichetta"><bean:message
					key="sintetica.datazione" bundle="gestioneBibliograficaLabels" /></td>
				<td width="120" class="testoNormale"><html:text
					property="dettAutoreVO.datazioni"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="80" class="etichetta"><bean:message
					key="dettaglio.notaInform" bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale">
					<html:textarea property="dettAutoreVO.notaInformativa" rows="3" cols="70"></html:textarea>
					<sbn:tastiera limit="1200" property="dettAutoreVO.notaInformativa" name="dettaglioAutoreForm"></sbn:tastiera>
				</td>
			</tr>
		</table>

		<c:choose>
			<c:when test="${dettaglioAutoreForm.dettAutoreVO.forma eq 'A'}">
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
							styleClass="buttonImageDelLine" property="methodDettaglioAut"
							alt="Cancella Repertorio">
							<bean:message key="button.canRepertorio"
								bundle="gestioneBibliograficaLabels" />
						</html:submit> <html:submit styleClass="buttonImageNewLine"
							property="methodDettaglioAut" alt="Inserisci nuova riga">
							<bean:message key="button.insRepertorio"
								bundle="gestioneBibliograficaLabels" />
						</html:submit> <html:submit styleClass="buttonImageHlpRep"
							property="methodDettaglioAut" alt="Cerca Repertorio">
							<bean:message key="button.hlpRepertorio"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></th>
					</tr>
					<logic:iterate id="item" property="listaRepertoriModificato"
						name="dettaglioAutoreForm" indexId="idxRepert">
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
						<td width="80" class="etichetta"><bean:message
							key="dettaglio.notacatal" bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<html:textarea property="dettAutoreVO.notaCatalogatore" rows="3" cols="70"></html:textarea>
							<sbn:tastiera limit="1200" property="dettAutoreVO.notaCatalogatore" name="dettaglioAutoreForm"></sbn:tastiera>
						</td>

					</tr>
				</table>
			</c:when>
		</c:choose>





	</c:otherwise>

</c:choose>

<table border="0">
	<tr>
		<td width="80" class="etichetta"><bean:message
			key="ricerca.dataInserimento" bundle="gestioneBibliograficaLabels" />
		</td>
		<td width="80" class="testoNormale"><html:text
			property="dettAutoreVO.dataIns" size="10" readonly="true"></html:text></td>

		<td width="120" class="etichetta"><bean:message
			key="ricerca.dataUltimoAgg" bundle="gestioneBibliograficaLabels" />
		</td>
		<td width="80" class="testoNormale"><html:text
			property="dettAutoreVO.dataAgg" size="10" readonly="true"></html:text></td>
	</tr>
</table>
