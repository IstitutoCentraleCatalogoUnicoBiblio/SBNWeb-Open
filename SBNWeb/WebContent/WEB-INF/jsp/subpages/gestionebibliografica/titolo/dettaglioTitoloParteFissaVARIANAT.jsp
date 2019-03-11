<!--	SBNWeb - Rifacimento ClientServer
		dettaglioTitoloParteFissaVARIANAT
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

<c:choose>
	<c:when
		test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'A'}">

		<table border="0">
			<tr>
				<td width="80" class="etichetta"><bean:message
					key="dettaglio.norme" bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.norme" readonly="true"></html:text></td>
				<td class="etichetta"><bean:message key="dettaglio.agenzia"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.fontePaese"
					readonly="true"></html:text></td>
				<td class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.fonteAgenzia"
					readonly="true"></html:text></td>
				<td class="etichetta"><bean:message key="ricerca.isadn"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.isadn" readonly="true"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<c:choose>
				<c:when test="${dettaglioTitoloForm.tipoMateriale ne 'U'}">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.lingua" bundle="gestioneBibliograficaLabels" /></td>

						<td width="100"><html:text
							property="dettTitComVO.detTitoloPFissaVO.lingua1"
							readonly="true" title="${dettaglioTitoloForm.descLingua1}">
						</html:text></td>
					</tr>

				</c:when>
			</c:choose>
		</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.livelloAutorita"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="400" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.livAut" size="5"
					readonly="true" title="${dettaglioTitoloForm.descLivAut}">
				</html:text></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message
					key="catalogazione.areaT.titolo"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo" cols="70"
					rows="5" readonly="true"></html:textarea></td>
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
			</tr>
			<logic:iterate id="item" property="listaRepertoriModificato"
				name="dettaglioTitoloForm">
				<tr class="testoNormale">
					<td></td>
					<td bgcolor="#FFCC99"><bean-struts:write name="item" property="campoUno"/></td>
					<td bgcolor="#FFCC99"><bean-struts:write name="item" property="campoDue"/></td>
					<td bgcolor="#FFCC99"><bean-struts:write name="item" property="nota"/></td>
				</tr>
			</logic:iterate>
		</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="dettaglio.notaInform" bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.noteInformative"
					cols="70" rows="1" readonly="true" ></html:textarea></td>
			</tr>
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="dettaglio.notacatal" bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.noteCatalogatore"
					cols="70" rows="1" readonly="true" ></html:textarea></td>
			</tr>
		</table>
	</c:when>

	<c:when
		test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'B'}">
		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message key="ricerca.bid"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.bid" size="10"
					readonly="true"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.livelloAutorita"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.livAut" size="5"
					readonly="true" title="${dettaglioTitoloForm.descLivAut}">
				</html:text></td>
			</tr>
		</table>


		<table border="0">
			<tr>
				<td width="80" class="etichetta"><bean:message
					key="dettaglio.norme" bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.norme" readonly="true"></html:text></td>
				<td class="etichetta"><bean:message key="dettaglio.agenzia"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.fontePaese"
					readonly="true"></html:text></td>
				<td class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.fonteAgenzia"
					readonly="true"></html:text></td>
				<td class="etichetta"><bean:message key="ricerca.isadn"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.isadn" readonly="true"></html:text></td>
			</tr>
		</table>



		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.paese" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.paese" size="5"
					readonly="true" title="${dettaglioTitoloForm.descPaese}">
				</html:text></td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.lingua" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.lingua1" size="5"
					readonly="true" title="${dettaglioTitoloForm.descLingua1}">
				</html:text></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.lingua2" size="5"
					readonly="true" title="${dettaglioTitoloForm.descLingua2}">
				</html:text></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.lingua3" size="5"
					readonly="true" title="${dettaglioTitoloForm.descLingua3}">
				</html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.genere" bundle="gestioneBibliograficaLabels" /></td>

				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.genere1" size="5"
					readonly="true" title="${dettaglioTitoloForm.descGenere1}">
				</html:text></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.genere2" size="5"
					readonly="true" title="${dettaglioTitoloForm.descGenere2}">
				</html:text></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.genere3" size="5"
					readonly="true" title="${dettaglioTitoloForm.descGenere3}">
				</html:text></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.genere4" size="5"
					readonly="true" title="${dettaglioTitoloForm.descGenere4}">
				</html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.tipo.Data" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.tipoData" size="5"
					readonly="true" title="${dettaglioTitoloForm.descTipoData}">
				</html:text></td>
				<td width="60" class="etichetta"><bean:message
					key="ricerca.dataPubblicazione1"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="40" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.dataPubbl1" size="15"
					readonly="true"></html:text></td>
				<td width="60" class="etichetta"><bean:message
					key="ricerca.dataPubblicazione2"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="40" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.dataPubbl2" size="15"
					readonly="true"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="catalogazione.areaT.titolo"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="400"><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo"
					cols="70" rows="5" readonly="true"></html:textarea></td>
			</tr>
		</table>
	</c:when>
</c:choose>
