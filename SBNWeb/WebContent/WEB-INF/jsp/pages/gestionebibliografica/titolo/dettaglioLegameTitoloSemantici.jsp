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

	<sbn:navform
		action="/gestionebibliografica/titolo/dettaglioLegameTitoloSemantici.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors /></div>

		<c:choose>
			<c:when
				test="${dettaglioLegameTitoloSemanticiForm.tipoLegame eq 'TI_SO'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.bid" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100" class="testoNormale"><html:text
							property="dettSogGenVO.bidPadre" size="10" readonly="true"></html:text></td>
					</tr>
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.descrizione" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:textarea
							property="dettSogGenVO.descrizionePadre" cols="60" rows="1"
							readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="dettaglio.notaLegame" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:textarea
							property="dettSogGenVO.notaAlLegame" cols="60" rows="1"
							readonly="true"></html:textarea></td>
					</tr>
				</table>

				<hr color="#dde8f0" />

				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.livelloAutorita"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="300" class="testoNormale"><html:text
							property="dettSogGenVO.livAut" size="10" readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="dettaglio.identificativo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:text
							property="dettSogGenVO.cid" size="10" readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="dettaglio.soggettario" bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:text
							property="dettSogGenVO.campoSoggettario" size="10"
							readonly="true"></html:text><html:text
							property="dettSogGenVO.edizioneSoggettario" size="3"
							readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="dettaglio.soggetto"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettSogGenVO.campoStringaSoggetto" cols="60" rows="1"
							readonly="true"></html:textarea></td>
					</tr>
				</table>
				<table border="0">
					<tr>
						<td width="80" class="etichetta"><bean:message
							key="ricerca.dataInserimento"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="80" class="testoNormale"><html:text
							property="dettSogGenVO.dataIns" size="10" readonly="true"></html:text></td>

						<td width="120" class="etichetta"><bean:message
							key="ricerca.dataUltimoAgg" bundle="gestioneBibliograficaLabels" />
						</td>
						<td width="80" class="testoNormale"><html:text
							property="dettSogGenVO.dataAgg" size="10" readonly="true"></html:text></td>
					</tr>
				</table>

			</c:when>
			<c:when
				test="${dettaglioLegameTitoloSemanticiForm.tipoLegame eq 'TI_CL'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.bid" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100" class="testoNormale"><html:text
							property="dettClaGenVO.idPadre" size="10" readonly="true"></html:text></td>
					</tr>
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.descrizione" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:textarea
							property="dettClaGenVO.descrizionePadre" cols="60" rows="1"
							readonly="true"></html:textarea></td>
					</tr>
				</table>

				<hr color="#dde8f0" />

				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="dettaglio.sistema"
							bundle="gestioneBibliograficaLabels" />
						</td>
						<td class="testoNormale">
							<html:text property="dettClaGenVO.dewey.sistema" size="1" readonly="true"></html:text>
						</td>
						<td class="etichetta"><bean:message
							key="dettaglio.edizione"
							bundle="gestioneBibliograficaLabels" />
						&nbsp;<html:text
							property="dettClaGenVO.dewey.edizione" size="2" readonly="true"></html:text>
						</td>
						<td class="etichetta"><bean:message
							key="dettaglio.simbolo"
							bundle="gestioneBibliograficaLabels" />
						&nbsp;<html:text
							property="dettClaGenVO.dewey.simbolo" size="10" readonly="true"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="ricerca.descrizione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="100" colspan="3"><html:textarea
							property="dettClaGenVO.descrizione" cols="60" rows="1"
							readonly="true"></html:textarea></td>
					</tr>

				</table>
			</c:when>

			<c:when
				test="${dettaglioLegameTitoloSemanticiForm.tipoLegame eq 'TI_TH'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.bid" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100" class="testoNormale"><html:text
							property="dettTerThesGenVO.idPadre" size="10" readonly="true"></html:text></td>
					</tr>
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.descrizione" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:textarea
							property="dettTerThesGenVO.descrizionePadre" cols="60" rows="1"
							readonly="true"></html:textarea></td>
					</tr>
				</table>

				<hr color="#dde8f0" />

				<table border="0">
					<tr>
						<td class="etichetta"><bean:message
							key="dettaglio.identificativo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:text
							property="dettTerThesGenVO.identificativo" size="10"
							readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="ricerca.descrizione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:textarea
							property="dettTerThesGenVO.descrizione" cols="60" rows="1"
							readonly="true"></html:textarea></td>
					</tr>

				</table>
			</c:when>

			<c:otherwise>
			</c:otherwise>
		</c:choose></div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodDettLegTitSem">
					<bean:message key="button.annulla"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>

