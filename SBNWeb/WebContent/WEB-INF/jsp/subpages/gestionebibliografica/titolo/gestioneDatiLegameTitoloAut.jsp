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



<c:choose>
      <c:when test="${gestioneLegameTitoloAutoreForm.areaDatiLegameTitoloVO.tipoOperazione eq 'TrascinaLegameAutore'}">

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.relazione" bundle="gestioneBibliograficaLabels" /></td>

				<td width="100" class="testoNormale"><html:text
					property="areaDatiLegameTitoloVO.relatorCodeNew" size="10" readonly="true"></html:text>
				</td>

				<c:if test="${gestioneLegameTitoloAutoreForm.presenzaSpecStrumVoci eq 'SI'}">
					<td width="300" class="testoNormale">
					<bean:message key="dettaglio.legameTitAut.VoceStrumento" bundle="gestioneBibliograficaLabels" />
					<html:text	property="areaDatiLegameTitoloVO.specStrumVociNew" size="10" readonly="true"></html:text>
				</td>
				</c:if>

			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.responsabilita" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale">
					<html:text	property="areaDatiLegameTitoloVO.tipoResponsNew" size="10" readonly="true"></html:text>
				</td>

				<td class="etichetta"><bean:message key="dettaglio.legameTitAut.Facoltativo"
					bundle="gestioneBibliograficaLabels" />: <html:checkbox
					property="areaDatiLegameTitoloVO.superfluoNew" disabled="true"></html:checkbox></td>
				<td class="etichetta"><bean:message key="dettaglio.legameTitAut.Incerto"
					bundle="gestioneBibliograficaLabels" />: <html:checkbox
					property="areaDatiLegameTitoloVO.incertoNew" disabled="true"></html:checkbox></td>

			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message key="ricerca.vid"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="60" class="testoNormale"><html:text
					property="areaDatiLegameTitoloVO.idArrivo" size="10" readonly="true"></html:text></td>
			</tr>
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.descrizione" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" ><html:text
					property="areaDatiLegameTitoloVO.descArrivo" size="60" readonly="true"></html:text></td>
			</tr>
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="dettaglio.notaLegame" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" ><html:text
					property="areaDatiLegameTitoloVO.noteLegameNew" size="60" readonly="true"></html:text></td>
			</tr>

		</table>
      </c:when>

      <c:otherwise>
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.relazione" bundle="gestioneBibliograficaLabels" /></td>

				<td width="100" class="testoNormale"><html:select
					property="areaDatiLegameTitoloVO.relatorCodeNew" style="width:40px" onchange="this.form.submit()">
					<html:optionsCollection property="listaRelatorCode" value="codice"
						label="descrizioneCodice" />
				</html:select></td>

				<c:if test="${gestioneLegameTitoloAutoreForm.presenzaSpecStrumVoci eq 'SI'}">
					<td width="300" class="testoNormale">
					<bean:message key="dettaglio.legameTitAut.VoceStrumento" bundle="gestioneBibliograficaLabels" />
					<html:select property="areaDatiLegameTitoloVO.specStrumVociNew" style="width:40px">
					<html:optionsCollection property="listaSpecStrumVoci" value="codice" label="descrizioneCodice" />
				</html:select></td>
				</c:if>

			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.responsabilita" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:select
					property="areaDatiLegameTitoloVO.tipoResponsNew" style="width:40px">
					<html:optionsCollection property="listaTipoRespons" value="codice"
						label="descrizioneCodice" />
				</html:select></td>

				<td class="etichetta"><bean:message key="dettaglio.legameTitAut.Facoltativo"
					bundle="gestioneBibliograficaLabels" />: <html:checkbox
					property="areaDatiLegameTitoloVO.superfluoNew"></html:checkbox></td>
				<td class="etichetta"><bean:message key="dettaglio.legameTitAut.Incerto"
					bundle="gestioneBibliograficaLabels" />: <html:checkbox
					property="areaDatiLegameTitoloVO.incertoNew"></html:checkbox></td>

			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message key="ricerca.vid"
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

      </c:otherwise>
</c:choose>



