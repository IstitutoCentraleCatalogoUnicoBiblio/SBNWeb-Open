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
			property="areaDatiLegameTitoloVO.tipoLegameNew" size="10"
			></html:text></td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="dettaglio.notaLegame" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100"><html:textarea
			property="areaDatiLegameTitoloVO.noteLegameNew" cols="60" rows="1"></html:textarea></td>
	</tr>
</table>
<hr color="#dde8f0" />
<table border="0">
	<tr>
		<td width="40" class="etichetta"><bean:message
			key="sintetica.livelloaut" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100" class="testoNormale"><html:select
			property="areaDatiLegameTitoloVO.livAutIdArrivo" style="width:40px">
			<html:optionsCollection property="listaLivAut" value="codice"
				label="descrizioneCodice" />
		</html:select></td>
		<td width="40" class="etichetta"><bean:message key="sintetica.forma"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="50" class="testoNormale"><html:text
			property="areaDatiLegameTitoloVO.formaIdArrivo" size="5"
			></html:text></td>

		<c:choose>
			<c:when test="${gestioneLegameFraAutorityForm.areaDatiLegameTitoloVO.authorityOggettoPartenza eq 'AU'}">
				<td width="40" class="etichetta"><bean:message
					key="sintetica.tiponome" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:select
					property="areaDatiLegameTitoloVO.tipoNomeArrivo" style="width:40px">
					<html:optionsCollection property="listaTipoNome" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
			</c:when>
		</c:choose>
	</tr>
</table>

<table border="0">
	<tr>
		<c:choose>
			<c:when test="${gestioneLegameFraAutorityForm.areaDatiLegameTitoloVO.authorityOggettoPartenza eq 'AU'}">
				<td width="40" class="etichetta"><bean:message key="ricerca.nome"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:textarea property="areaDatiLegameTitoloVO.descArrivo" rows="3" cols="70"></html:textarea>
					<sbn:tastiera limit="1200" property="areaDatiLegameTitoloVO.descArrivo" name="gestioneLegameFraAutorityForm"></sbn:tastiera>
				</td>
			</c:when>
			<c:when test="${gestioneLegameFraAutorityForm.areaDatiLegameTitoloVO.authorityOggettoPartenza eq 'LU'}">
				<td width="40" class="etichetta"><bean:message key="ricerca.luogo"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:textarea
					property="areaDatiLegameTitoloVO.descArrivo" rows="3" cols="70"></html:textarea></td>
			</c:when>
		</c:choose>
	</tr>
	<tr>
		<td><bean:message
			key="dettaglio.notaInform" bundle="gestioneBibliograficaLabels" /></td>
		<td><html:textarea
			property="areaDatiLegameTitoloVO.notaInformativaIdArrivo" rows="3" cols="70"></html:textarea>
					<sbn:tastiera limit="1200" property="areaDatiLegameTitoloVO.notaInformativaIdArrivo" name="gestioneLegameFraAutorityForm"></sbn:tastiera>
		</td>
	</tr>

</table>

