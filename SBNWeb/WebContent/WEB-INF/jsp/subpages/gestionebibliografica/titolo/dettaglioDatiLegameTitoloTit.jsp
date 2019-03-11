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
			property="dettTitComVO.campoTipoLegame" size="5" readonly="true"></html:text></td>


		<!--  almaviva2 - agosto 2016 - gestione dei legami fra natura A e altra natura A con tipo legame 531 (A01A, A02A, A03A, A04A)-->
		<c:if test="${dettaglioTitoloForm.dettTitComVO.campoTipoLegameConNature ne 'A01A'
					&& dettaglioTitoloForm.dettTitComVO.campoTipoLegameConNature ne 'A02A'
					&& dettaglioTitoloForm.dettTitComVO.campoTipoLegameConNature ne 'A04A'
					&& dettaglioTitoloForm.dettTitComVO.campoTipoLegameConNature ne 'A10A'}">
			<c:if test="${dettaglioTitoloForm.dettTitComVO.campoTipoLegame eq '01' || dettaglioTitoloForm.dettTitComVO.campoTipoLegame eq '51'}">
					<td width="60" class="etichetta"><bean:message
						key="dettaglio.sequenzaLegame" bundle="gestioneBibliograficaLabels" /></td>
					<td width="100" class="testoNormale"><html:text
						property="dettTitComVO.campoSequenza" size="5" readonly="true"></html:text></td>
			</c:if>
		</c:if>


<!-- Modifica almaviva2 05.03.2010 - BUG 3608 - sostituito con l'if sopra
		<c:choose>
			<c:when test="${dettaglioTitoloForm.dettTitComVO.campoSequenza ne ''}">
				<td width="60" class="etichetta"><bean:message
					key="dettaglio.sequenzaLegame" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text
					property="dettTitComVO.campoSequenza" size="5" readonly="true"></html:text></td>
			</c:when>
		</c:choose>
-->

	<!-- Inizio BUG MANTIS 3288 Ulteriore intervento per inserire una intera riga per il SICI per visualizzarla tutta 23.11.2009-->

		<c:choose>
			<c:when	test="${dettaglioTitoloForm.dettTitComVO.campoSottoTipoLegame ne ''}">
				<td width="110" class="etichetta"><bean:message
					key="dettaglio.sottoTipoLegame"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text
					property="dettTitComVO.campoSottoTipoLegame" size="30"
					readonly="true" title="${dettaglioTitoloForm.descSottoTipoLegame}"></html:text></td>
			</c:when>
		</c:choose>
	</tr>
</table>

<table border="0">
	<tr>
		<c:choose>
			<c:when test="${dettaglioTitoloForm.dettTitComVO.campoSici ne ''}">
				<td width="100" class="etichetta"><bean:message key="dettaglio.sici"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text
					property="dettTitComVO.campoSici" size="80" readonly="true"></html:text></td>
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
			property="dettTitComVO.idPadre" size="10" readonly="true"></html:text></td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="ricerca.descrizione" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100"><html:textarea
			property="dettTitComVO.nominativoPadre" cols="60" rows="1"
			readonly="true"></html:textarea></td>
	</tr>
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="dettaglio.notaLegame" bundle="gestioneBibliograficaLabels" /></td>
		<td width="100"><html:textarea
			property="dettTitComVO.campoNotaAlLegame" cols="60" rows="1"
			readonly="true"></html:textarea></td>
	</tr>
</table>


