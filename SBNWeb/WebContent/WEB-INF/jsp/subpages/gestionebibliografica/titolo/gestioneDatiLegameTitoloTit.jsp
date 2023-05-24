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
		<td class="testoNormale">
			<html:select property="areaDatiLegameTitoloVO.tipoLegameNew" style="width:100px" onchange="this.form.submit()">
			<html:optionsCollection property="listaTipoLegame" value="codice" label="descrizioneCodice"/>
		</html:select></td>

		<c:choose>
			<c:when test="${gestioneLegameTitoloTitoloForm.presenzaSottoTipoD eq 'SI'}">
				<td width="110" class="etichetta"><bean:message
					key="dettaglio.sottoTipoLegame" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:select
					property="areaDatiLegameTitoloVO.sottoTipoLegameNew" style="width:60px">
					<html:optionsCollection property="listaSottonatureD"
						value="codice" label="descrizioneCodice" />
				</html:select></td>
			</c:when>
		</c:choose>

		<noscript>
			<td align="center"><html:submit property="methodGestLegTitTit">
				<bean:message key="ricerca.button.aggiornaCanali" bundle="gestioneBibliograficaLabels" />
			</html:submit></td>
		</noscript>
		<c:choose>
			<c:when test="${gestioneLegameTitoloTitoloForm.presenzaNumSequenza eq 'SI'}">
				<td width="60" class="etichetta"><bean:message
					key="dettaglio.sequenzaLegame" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text
					property="areaDatiLegameTitoloVO.sequenzaNew" size="10" maxlength="10"></html:text></td>
			</c:when>
		</c:choose>

	<!-- Inizio BUG MANTIS 3288 Ulteriore intervento per inserire una intera riga per il SICI per visualizzarla tutta 23.11.2009-->
	</tr>
</table>

<table border="0">
	<tr>
		<c:choose>
			<c:when test="${gestioneLegameTitoloTitoloForm.areaDatiLegameTitoloVO.naturaBidArrivo eq 'N' and
							gestioneLegameTitoloTitoloForm.areaDatiLegameTitoloVO.naturaBidPartenza eq 'S'}">
				<td width="35" class="etichetta"><bean:message key="dettaglio.sici"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:text
					property="areaDatiLegameTitoloVO.siciNew" size="80"></html:text></td>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${gestioneLegameTitoloTitoloForm.areaDatiLegameTitoloVO.naturaBidArrivo eq 'S' and
							gestioneLegameTitoloTitoloForm.areaDatiLegameTitoloVO.naturaBidPartenza eq 'N'}">
				<td width="35" class="etichetta"><bean:message key="dettaglio.sici"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:text
					property="areaDatiLegameTitoloVO.siciNew" size="80"></html:text></td>
			</c:when>
		</c:choose>

	</tr>
</table>
	<!-- Fine BUG MANTIS 3288 Ulteriore intervento per inserire una intera riga per il SICI per visualizzarla tutta 23.11.2009-->


<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message key="ricerca.bid"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="60" class="testoNormale"><html:text
			property="areaDatiLegameTitoloVO.idArrivo" size="10" readonly="true"></html:text></td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="ricerca.descrizione" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" ><html:textarea
			property="areaDatiLegameTitoloVO.descArrivo" cols="60" rows="1"
			readonly="true"></html:textarea></td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="dettaglio.notaLegame" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" ><html:textarea
			property="areaDatiLegameTitoloVO.noteLegameNew" cols="60" rows="1"></html:textarea></td>
	</tr>
</table>


