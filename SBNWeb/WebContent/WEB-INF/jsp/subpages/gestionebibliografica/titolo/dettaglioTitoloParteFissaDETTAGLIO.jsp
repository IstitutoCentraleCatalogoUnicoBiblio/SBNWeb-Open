<!--	SBNWeb - Rifacimento ClientServer
		dettaglioTitoloParteFissaDETTAGLIO
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
				<td width="100" class="etichetta"><bean:message
					key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.natura" size="5"
					readonly="true" title="${dettaglioTitoloForm.descNatura}">
				</html:text></td>
				<td width="60" class="etichetta"><bean:message key="ricerca.bid"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.bid" size="10"
					readonly="true"></html:text></td>
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
				<td class="etichetta"><bean:message key="ricerca.isadnOLD"
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

						<td><html:text
							property="dettTitComVO.detTitoloPFissaVO.lingua1" size="5"
							readonly="true" title="${dettaglioTitoloForm.descLingua1}">
						</html:text></td>
						<td><html:text
							property="dettTitComVO.detTitoloPFissaVO.lingua2" size="5"
							readonly="true" title="${dettaglioTitoloForm.descLingua2}">
						</html:text></td>
						<td><html:text
							property="dettTitComVO.detTitoloPFissaVO.lingua3" size="5"
							readonly="true" title="${dettaglioTitoloForm.descLingua3}">
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


<!-- Intervento interno Luglio 2016 - almaviva2 - Manutenzione evolutiva per adeguamenti a protocollo 2.03:
     trattamento della nuovo etichetta 231 per titoliUniformi A nella nuova versione -->
	     <c:choose>
			<c:when test="${dettaglioTitoloForm.tipoMateriale ne 'U'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.formaOpera.naturaA" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100" class="testoNormale"><html:text
							property="dettTitComVO.detTitoloPFissaVO.formaOpera231" size="10"
							readonly="true"></html:text></td>
					</tr>
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.dataOpera.naturaA" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100" class="testoNormale"><html:text
							property="dettTitComVO.detTitoloPFissaVO.dataOpera231" size="10"
							readonly="true"></html:text></td>
					</tr>
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.paese" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100"><html:text
							property="dettTitComVO.detTitoloPFissaVO.paese" size="5"
							readonly="true" title="${dettaglioTitoloForm.descPaese}">
						</html:text></td>
					</tr>
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="ricerca.altreCarat.naturaA" bundle="gestioneBibliograficaLabels" /></td>
						<td width="100" class="testoNormale"><html:text
							property="dettTitComVO.detTitoloPFissaVO.altreCarat231" size="10"
							readonly="true"></html:text></td>
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
		test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'V'}">
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.natura" size="5"
					readonly="true" title="${dettaglioTitoloForm.descNatura}">
				</html:text></td>
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

	</c:when>



	<c:when
		test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'B'}">
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.natura" size="5"
					readonly="true" title="${dettaglioTitoloForm.descNatura}">
				</html:text></td>
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
				<td width="400" class="testoNormale"><html:text
					property="dettTitComVO.detTitoloPFissaVO.livAut" size="5"
					readonly="true" title="${dettaglioTitoloForm.descLivAut}">
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
					key="catalogazione.areaT.titolo"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo" cols="70"
					rows="5" readonly="true"></html:textarea></td>
			</tr>
		</table>
	</c:when>

	<c:when
		test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'D'}">
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.natura" size="5"
					readonly="true" title="${dettaglioTitoloForm.descNatura}">
				</html:text></td>
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
	</c:when>
	<c:when
		test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'P'}">
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.natura" size="5"
					readonly="true" title="${dettaglioTitoloForm.descNatura}">
				</html:text></td>
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
	</c:when>


	<c:when
		test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'R'}">
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.natura" size="5"
					readonly="true" title="${dettaglioTitoloForm.descNatura}">
				</html:text></td>
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

		<!--	almaviva2 - Intervento 12.06.2012 nelle raccolte fattizie devono essere presenti le aree di descrFisica e note
		-->
		<table border="0">
			<tr>
				<td class="etichetta"><bean:message
					key="catalogazione.areaT.titolo"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo" cols="70"
					rows="5" readonly="true"></html:textarea></td>
			</tr>

			<tr>
				<td class="etichetta"><bean:message
					key="catalogazione.areaT.descrFisica"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitDescrFis"
					cols="70" rows="1" readonly="true"></html:textarea></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message
					key="catalogazione.areaT.note"
					bundle="gestioneBibliograficaLabels" /></td>
				<td><html:textarea
					property="dettTitComVO.detTitoloPFissaVO.areaTitNote" cols="70"
					rows="7" readonly="true"></html:textarea></td>
			</tr>
		</table>

	</c:when>



	<c:otherwise>
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.natura" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.natura" size="5"
					readonly="true" title="${dettaglioTitoloForm.descNatura}">
				</html:text></td>
				<td width="60" class="etichetta"><bean:message
					key="ricerca.tipoMateriale" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.tipoMat" size="5"
					readonly="true" title="${dettaglioTitoloForm.descTipoMat}">
				</html:text></td>
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
				<td width="60" class="etichetta"><bean:message
					key="ricerca.tipoRecord" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.tipoRec" size="5"
					readonly="true" title="${dettaglioTitoloForm.descTipoRec}">
				</html:text></td>
				<c:if test="${not empty navForm.dettTitComVO.detTitoloPFissaVO.ocn}">
					<td width="60" class="etichetta">
						<bean:message key="label.dettaglio.numero.ocn" bundle="gestioneBibliograficaLabels" />
					</td>
					<td width="100">
						<html:text property="dettTitComVO.detTitoloPFissaVO.ocn" size="17" readonly="true" />
					</td>
				</c:if>
			</tr>
		</table>


<!--	almaviva2 - BUG MANTIS 3683 - 12.04.2010

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
-->


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


					<!--  almaviva2 agosto 2017 - gestione nuovo campo che indica se il documento (M,W,S)
					è stato pubblicato (valore cancelletto SI, valore 1 NO - default in caso di null= SI) evolutiva indice -->
				<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'S'}">
						<td width="60" class="etichetta"><bean:message
							key="label.area0.pubblicatoSiNo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="40" class="testoNormale"><html:text
							property="dettTitComVO.detTitoloPFissaVO.pubblicatoSiNo" size="15"
							readonly="true" title="${dettaglioTitoloForm.descPubblicatoSiNo}"></html:text></td>
				</c:if>

			</tr>
		</table>

		<c:choose>
			<c:when
				test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'C'}">
				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="catalogazione.areaT.titolo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="400"><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo"
							cols="70" rows="5" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.edizione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitEdiz" cols="70"
							rows="1" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.pubblicazione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitPubbl"
							cols="70" rows="3" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.descrFisica"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitDescrFis"
							cols="70" rows="1" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.note"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote" cols="70"
							rows="7" readonly="true"></html:textarea></td>
					</tr>
				</table>

			</c:when>
			<c:when
				test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'T'}">
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

			<c:when
				test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'R'}">
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

			<c:when
				test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'B'}">
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

			<c:otherwise>

				<!--  almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S e tutti i Materiali-->
				<!--  almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro -->
				<!--  almaviva2 Evolutiva Dicembre 2014 per Gestione Area0 anche per gli spogli (Natura N) -->
				<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'N'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'S'}">
						<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioTitoloParteFissaArea0DETTAGLIO.jsp" />
				</c:if>

				<table border="0">
					<tr>
						<td width="100" class="etichetta"><bean:message
							key="catalogazione.areaT.titolo"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="400"><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo"
							cols="70" rows="5" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.edizione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitEdiz" cols="70"
							rows="1" readonly="true"></html:textarea></td>
					</tr>


<!--	Inizio modifica almaviva2 del 2010.11.15 BUG MANTIS 3987 - sistemazione aree titolo Dati Matematici, Numerazione e Musica -->
<!--	Modifica almaviva2 del 2010.12.02 BUG MANTIS 3987 - rimane solo controllo per tipoRecord/Natura; si elimina quello per tipoMateriale -->
					<c:choose>
						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'e'
									|| dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'f'}">
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.datiMatematici"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitDatiMatem"
									cols="70" rows="1" readonly="true"></html:textarea></td>
							</tr>
						</c:when>
						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'C'
								||dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'G'}">
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.datiMatematici"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitDatiMatem"
									cols="70" rows="1" readonly="true"></html:textarea></td>
							</tr>
						</c:when>
					</c:choose>

					<c:choose>
						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'c'
									|| dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'd'}">
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.musica"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitMusica"
									cols="70" rows="1" readonly="true"></html:textarea></td>
							</tr>
						</c:when>
						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'U'}">
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.musica"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitMusica"
									cols="70" rows="1" readonly="true"></html:textarea></td>
							</tr>
						</c:when>
					</c:choose>

					<c:choose>
						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat ne 'E'
								&& dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'S'}">
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.numerazione"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitNumer"
								cols="70" rows="1" readonly="true"></html:textarea></td>
							</tr>
						</c:when>
					</c:choose>




					<!--<c:choose>
						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat eq 'C'}">
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.datiMatematici"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitDatiMatem"
									cols="70" rows="1" readonly="true"></html:textarea></td>
							</tr>
						</c:when>
					</c:choose>

					<c:choose>
						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoMat ne 'E'}">
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.musica"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitMusica"
									cols="70" rows="1" readonly="true"></html:textarea></td>
							</tr>
							<c:choose>
								<c:when
									test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'S'}">
									<tr>
										<td class="etichetta"><bean:message
											key="catalogazione.areaT.numerazione"
											bundle="gestioneBibliograficaLabels" /></td>
										<td><html:textarea
											property="dettTitComVO.detTitoloPFissaVO.areaTitNumer"
											cols="70" rows="1" readonly="true"></html:textarea></td>
									</tr>
								</c:when>
							</c:choose>
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.datiMatematici"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitDatiMatem"
									cols="70" rows="1" readonly="true"></html:textarea></td>
							</tr>
						</c:when>
					</c:choose>-->


<!--	Fine modifica almaviva2 del 2010.11.15 BUG MANTIS 3987 -->




					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.pubblicazione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitPubbl"
							cols="70" rows="3" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.descrFisica"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitDescrFis"
							cols="70" rows="1" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.note"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote" cols="70"
							rows="7" readonly="true"></html:textarea></td>
					</tr>


<!--	GESTIONE NOTE AGGIUNTIVE 3204 ; Inizio almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274 -->


					<!--Inizio almaviva2 modifica  BUG 3673 -->
					<c:choose>
						<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'g'}">
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.note323"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitNote323" cols="70"
									rows="1" readonly="true"></html:textarea></td>
							</tr>
						</c:when>
						<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'j'}">
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.note323"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitNote323" cols="70"
									rows="1" readonly="true"></html:textarea></td>
							</tr>
						</c:when>
						<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'i'}">
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.note323"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitNote323" cols="70"
									rows="1" readonly="true"></html:textarea></td>
							</tr>
						</c:when>
					</c:choose>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.note327"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote327" cols="70"
							rows="1" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.note330"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote330" cols="70"
							rows="1" readonly="true"></html:textarea></td>
					</tr>

<!-- queste note non sono ancora gestite dal protocollo rimangono quindi ancora in sospeso
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.noteDATA"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitNoteDATA" cols="70"
									rows="1" readonly="true"></html:textarea></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.noteORIG"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitNoteORIG" cols="70"
									rows="1" readonly="true"></html:textarea></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.noteFILI"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitNoteFILI" cols="70"
									rows="1" readonly="true"></html:textarea></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.notePROV"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitNotePROV" cols="70"
									rows="1" readonly="true"></html:textarea></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="catalogazione.areaT.notePOSS"
									bundle="gestioneBibliograficaLabels" /></td>
								<td><html:textarea
									property="dettTitComVO.detTitoloPFissaVO.areaTitNotePOSS" cols="70"
									rows="1" readonly="true"></html:textarea></td>
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
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.note336"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote336" cols="70"
							rows="1" readonly="true"></html:textarea></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="catalogazione.areaT.note337"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea
							property="dettTitComVO.detTitoloPFissaVO.areaTitNote337" cols="70"
							rows="1" readonly="true"></html:textarea></td>
					</tr>
<!--	GESTIONE NOTE AGGIUNTIVE 3204 ; Fine almaviva2 modifica riportata in Polo il 26 ottobre 2009 BUG 3274 -->
					<tr>
						<td class="etichetta"><bean:message	key="dettaglio.uriCopiaElettr" bundle="gestioneBibliograficaLabels" /></td>
						<td><html:textarea property="dettTitComVO.detTitoloPFissaVO.uriDigitalBorn" cols="70" rows="1" readonly="true"></html:textarea></td>
					</tr>

				</table>

			</c:otherwise>
		</c:choose>


<!--  // Marzo 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi al link dei documenti
		 su Basi Esterne - Link verso base date digital -->
				<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'N'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'S'}">
					<c:choose>
						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.presenzaLinkEsterni eq 'SI'}">
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
								</tr>
								<logic:iterate id="item"
									property="dettTitComVO.detTitoloPFissaVO.listaLinkEsterni"
									name="dettaglioTitoloForm">
									<tr class="testoNormale">
										<td></td>
										<td bgcolor="#FFCC99"><bean-struts:write name="item" property="campoUno" /> - <bean-struts:write name="item" property="descCampoDue" /></td>
										<td bgcolor="#FFCC99"><bean-struts:write name="item" property="campoDue" /></td>
										<td bgcolor="#FFCC99">
										<a href="javascript:window.open('<bean-struts:write name="item" property="nota"/>')">
													<bean-struts:write name="item" property="nota"/></a>
										</td>
									</tr>
								</logic:iterate>
							</table>
						</c:when>
					</c:choose>
				</c:if>

<!--  	// Giugno 2016: Evolutiva almaviva2 Il trattamento del tag 321 che contiene i dati relativi
	// ai repertori cartacei - Riferimento a repertorio cartaceo -->
				<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'N'
									or dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'S'}">
					<c:choose>
						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.presenzaReperCartaceo eq 'SI'}">
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
								</tr>
								<logic:iterate id="item"
									property="dettTitComVO.detTitoloPFissaVO.listaReperCartaceo"
									name="dettaglioTitoloForm">
									<tr class="testoNormale">
										<td></td>
										<td bgcolor="#FFCC99"><bean-struts:write name="item" property="campoUno" /></td>
										<td bgcolor="#FFCC99"><bean-struts:write name="item" property="campoDue" /></td>
										<td bgcolor="#FFCC99"><bean-struts:write name="item" property="nota" /></td>
									</tr>
								</logic:iterate>
							</table>
						</c:when>
					</c:choose>
				</c:if>




		<c:choose>
			<c:when
				test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.presenzaNumSt eq 'SI'}">
				<table border="0">
					<tr>
						<th width="100" class="etichetta"><bean:message
							key="ricerca.num.standard" bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="ricerca.numero" bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="ricerca.tipo" bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
					</tr>
					<logic:iterate id="item"
						property="dettTitComVO.detTitoloPFissaVO.listaNumStandard"
						name="dettaglioTitoloForm">
						<tr class="testoNormale">
							<td></td>
							<td bgcolor="#FFCC99"><bean-struts:write name="item"
								property="campoUno" /></td>
							<td bgcolor="#FFCC99"><bean-struts:write name="item"
								property="campoDue" /></td>
							<td bgcolor="#FFCC99"><bean-struts:write name="item"
								property="nota" /></td>
						</tr>
					</logic:iterate>
				</table>
			</c:when>
		</c:choose>

		<c:choose>
			<c:when
				test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.presenzaImpron eq 'SI'}">
				<table border="0">
					<tr>
						<th width="100" class="etichetta"><bean:message
							key="ricerca.impronta" bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="sintetica.progr" bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="ricerca.impronta" bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></th>
					</tr>
					<logic:iterate id="item"
						property="dettTitComVO.detTitoloPFissaVO.listaImpronte"
						name="dettaglioTitoloForm">
						<tr class="testoNormale">
							<td></td>
							<td bgcolor="#FFCC99"><bean-struts:write name="item"
								property="campoUno" /></td>
							<td bgcolor="#FFCC99"><bean-struts:write name="item"
								property="campoDue" /></td>
							<td bgcolor="#FFCC99"><bean-struts:write name="item"
								property="nota" /></td>
						</tr>
					</logic:iterate>
				</table>
			</c:when>
		</c:choose>
	</c:otherwise>
</c:choose>


