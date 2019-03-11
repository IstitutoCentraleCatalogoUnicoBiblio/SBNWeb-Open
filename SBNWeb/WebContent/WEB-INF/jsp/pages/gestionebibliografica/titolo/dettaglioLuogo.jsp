<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Autore
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

<html:xhtml />

<layout:page>


	<sbn:navform action="/gestionebibliografica/titolo/dettaglioLuogo.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors
			bundle="gestioneBibliograficaMessages" /></div>

		<c:choose>
			<c:when
				test="${dettaglioLuogoForm.dettLuoComVO.tipoLegame eq 'TI_LU'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message key="ricerca.bid"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="60" class="testoNormale"><html:text
							property="dettLuoComVO.idPadre" size="10" readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="ricerca.descrizione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea property="dettLuoComVO.descrizionePadre"
							cols="60" rows="1" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="dettaglio.codrelaz"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text property="dettLuoComVO.relatorCode" size="10"
							readonly="true" title="${dettaglioLuogoForm.descRelatorCode}"></html:text></td>

					</tr>
					<tr>
						<td class="etichetta"><bean:message key="dettaglio.notaLegame"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea property="dettLuoComVO.notaAlLegame" cols="60"
							rows="1" readonly="true"></html:textarea></td>
					</tr>

				</table>
				<hr color="#dde8f0" />
			</c:when>
			<c:when
				test="${dettaglioLuogoForm.dettLuoComVO.tipoLegame eq 'LU_LU'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="dettaglio.identificativo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="60" class="testoNormale"><html:text
							property="dettLuoComVO.idPadre" size="10" readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="dettaglio.denomLuogo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea property="dettLuoComVO.descrizionePadre"
							cols="60" rows="1" readonly="true"></html:textarea></td>
					</tr>

				</table>
				<hr color="#dde8f0" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>

		<c:choose>
			<c:when test="${dettaglioLuogoForm.tipoProspettazione eq 'DET'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="sintetica.livelloaut" bundle="gestioneBibliograficaLabels" />
						</td>

						<td width="100" class="testoNormale"><html:text
							property="dettLuoComVO.livAut" size="5" readonly="true"
							title="${dettaglioLuogoForm.descLivAut}">
						</html:text></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="dettaglio.identificativo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="60" class="testoNormale"><html:text
							property="dettLuoComVO.lid" size="10" readonly="true"></html:text></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="dettaglio.denomLuogo" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:textarea property="dettLuoComVO.denomLuogo"
							cols="60" rows="1" readonly="true"></html:textarea></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message key="sintetica.forma"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text property="dettLuoComVO.forma" readonly="true"
							title="${dettaglioLuogoForm.descFormaNome}" size="5">
						</html:text></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message key="ricerca.paese"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text property="dettLuoComVO.paese" readonly="true"
							title="${dettaglioLuogoForm.descPaese}" size="5">
						</html:text></td>
					</tr>
				</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="dettaglio.notaInform" bundle="gestioneBibliograficaLabels" /></td>
				<td width="300" class="testoNormale"><html:textarea
					property="dettLuoComVO.notaInformativa" rows="3" cols="70"
					readonly="true"></html:textarea></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="dettaglio.fonti"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:textarea
					property="dettLuoComVO.legamiRepertori" rows="3" cols="70"
					readonly="true"></html:textarea></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="dettaglio.notacatal"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:textarea
					property="dettLuoComVO.notaCatalogatore" rows="3" cols="70"
					readonly="true"></html:textarea></td>
			</tr>

		</table>

			</c:when>
			<c:otherwise>
				<table border="0">
					<tr>
						<td width="60" class="etichetta"><bean:message
							key="sintetica.livelloaut" bundle="gestioneBibliograficaLabels" />
						</td>
						<td width="100"><html:select property="dettLuoComVO.livAut"
							style="width:40px">
							<html:optionsCollection property="listaLivAut" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="dettaglio.identificativo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="60" class="testoNormale"><html:text
							property="dettLuoComVO.lid" size="10" readonly="true"></html:text></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="dettaglio.denomLuogo" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:textarea property="dettLuoComVO.denomLuogo"
							rows="3" cols="70"></html:textarea></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td width="40" class="etichetta"><bean:message
							key="sintetica.forma" bundle="gestioneBibliograficaLabels" /></td>
						<td width="50" class="testoNormale"><html:text
							property="dettLuoComVO.forma" size="5"></html:text></td>
					</tr>
				</table>


				<table border="0">
					<tr>
						<td width="40" class="etichetta"><bean:message key="ricerca.paese"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:select property="dettLuoComVO.paese"
							style="width:40px">
							<html:optionsCollection property="listaPaese" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td width="80" class="etichetta"><bean:message
							key="dettaglio.notaInform" bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<html:textarea property="dettLuoComVO.notaInformativa" rows="3" cols="70"></html:textarea>
							<sbn:tastiera limit="1200" property="dettLuoComVO.notaInformativa" name="dettaglioLuogoForm"></sbn:tastiera>
						</td>
					</tr>
				</table>

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
							styleClass="buttonImageDelLine" property="methodDettaglioLuo"
							alt="Cancella Repertorio">
							<bean:message key="button.canRepertorio"
								bundle="gestioneBibliograficaLabels" />
						</html:submit> <html:submit styleClass="buttonImageNewLine"
							property="methodDettaglioLuo" alt="Inserisci nuova riga">
							<bean:message key="button.insRepertorio"
								bundle="gestioneBibliograficaLabels" />
						</html:submit> <html:submit styleClass="buttonImageHlpRep"
							property="methodDettaglioLuo" alt="Cerca Repertorio">
							<bean:message key="button.hlpRepertorio"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></th>
					</tr>
					<logic:iterate id="item" property="listaRepertoriModificato"
						name="dettaglioLuogoForm" indexId="idxRepert">
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
							<html:textarea property="dettLuoComVO.notaCatalogatore" rows="3" cols="70"></html:textarea>
							<sbn:tastiera limit="1200" property="dettLuoComVO.notaCatalogatore" name="dettaglioLuogoForm"></sbn:tastiera>
						</td>

					</tr>
				</table>

			</c:otherwise>
		</c:choose></div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<c:choose>
					<c:when test="${dettaglioLuogoForm.tipoProspettazione ne 'DET'}">
						<td align="center"><html:submit property="methodDettaglioLuo">
							<bean:message key="button.ok"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
					</c:when>
				</c:choose>

				<td align="center"><html:submit property="methodDettaglioLuo">
					<bean:message key="button.annulla"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>
<script type="text/javascript"
	src='<c:url value="/scripts/bibliografica/dettaglioTitolo.js" />'></script>