<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform
		action="/gestionesemantica/catalogazionesemantica/CatturaClassificazioneDaIndice.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<table width="100%" border="0">

			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
		</table>
		<table width="100%" border="0">
			<tr>
				<sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
					parameter="methodCatturaCla" totBlocchi="totBlocchi"
					livelloRicerca="I"></sbn:blocchi>
			</tr>
		</table>
		<logic:notEmpty name="CatturaClassificazioneDaIndiceForm"
			property="outputLista">
			<table class="sintetica">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center"><bean:message
						key="sintetica.progr" bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="ricerca.sistema"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="ricerca.edizione"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="ricerca.simbolo"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center"><bean:message
						key="sintetica.headerStato" bundle="gestioneSemanticaLabels" /></th>
					<%--<td class="etichetta" scope="col" bgcolor="#dde8f0" align="center"><bean:message
						key="sintetica.headerLegato" bundle="gestioneSemanticaLabels" /></td>--%>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center"><bean:message
						key="sintetica.descrizione" bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
					<%--<td class="etichetta" scope="col" bgcolor="#dde8f0"></td>--%>
				</tr>
				<bean-struts:define id="color" value="#FEF1E2" />
				<logic:iterate id="listaRicerca" property="outputLista.listaClassi"
					name="CatturaClassificazioneDaIndiceForm"
					offset="${CatturaClassificazioneDaIndiceForm.offset}"
					indexId="riga">
					<sbn:rowcolor var="color" index="riga"></sbn:rowcolor>
					<tr>
						<td bgcolor="${color}" ><bean-struts:write
							name="listaRicerca" property="progr" /></td>
						<td bgcolor="${color}" ><bean-struts:write
							name="listaRicerca" property="simboloDewey.sistema" /></td>
						<td bgcolor="${color}" ><bean-struts:write
							name="listaRicerca" property="simboloDewey.edizione" /></td>
						<td bgcolor="${color}" ><bean-struts:write
							name="listaRicerca" property="simboloDewey.simbolo" /></td>
						<td bgcolor="${color}" ><bean-struts:write
							name="listaRicerca" property="livelloAutorita" /></td>
						<%--<td bgcolor="${color}" ><bean-struts:write
							name="listaRicerca" property="indicatore" /></td>--%>
						<td bgcolor="${color}" ><bean-struts:write
							name="listaRicerca" property="parole" /></td>
						<td bgcolor="${color}" ><html:radio
							property="codSelezionato"
							value="${listaRicerca.identificativoClasse}" /></td>
						<td bgcolor="${color}" ><%--<bean-struts:define
							id="codval">
							<bean-struts:write name="listaRicerca" property="progr" />
						</bean-struts:define> <html:multibox property="codClasse"
							value="${codval}" /></td>--%>
					</tr>
				</logic:iterate>

			</table>
		</logic:notEmpty></div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<sbn:checkAttivita idControllo="CATTURA">
					<!-- Bottoni che risultano abilitati se il livello di ricerca è impostato a Indice -->
					<td align="center"><html:submit property="methodCatturaCla">
						<bean:message key="button.cattura"
							bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</sbn:checkAttivita>
				<td align="center"><html:submit property="methodCatturaCla">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>

