<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform
		action="/gestionesemantica/catalogazionesemantica/CatturaSoggettoDaIndice.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<table width="100%" border="0">
			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
		</table>
		<%-- <table width="100%" border="0">
				<tr>
					<td class="etichetta">
						<bean:message key="sintetica.soggettario"
							bundle="gestioneSemanticaLabels" />
						&nbsp;
						<html:select styleClass="testoNormale"
							property="ricercaComune.codSoggettario" disabled="true">
							<html:optionsCollection property="listaSoggettari" value="codice"
								label="descrizione" />
						</html:select>
					</td>
				</tr>
			</table> --%>
		<table width="100%" border="0">
			<tr>
				<td>
					<c:if test="${not empty navForm.areaDatiPassBiblioSemanticaVO.poloSoggettazioneIndice}">
						<span style="float: right; margin-right: 0.5em;">
							<bean:message key="gestionesemantica.soggetto.poloSoggettazioneIndice"	bundle="gestioneSemanticaLabels" />:&nbsp;
							<bs:write name="navForm" property="areaDatiPassBiblioSemanticaVO.poloSoggettazioneIndice"/>
						</span>
					</c:if>
				<sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
					parameter="methodCatturaSog" totBlocchi="totBlocchi"
					livelloRicerca="I" />
				</td>
			</tr>
		</table>
		<logic:notEmpty name="navForm" property="output">
			<table class="sintetica">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.progr"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.cid" bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.soggettario"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.headerStato"
						bundle="gestioneSemanticaLabels" /></th>
					<!--<td class="etichetta" scope="col" bgcolor="#dde8f0" align="center"><bean:message
						key="sintetica.headerLegato" bundle="gestioneSemanticaLabels" /></td>-->
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.testo"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
					<%--<td class="etichetta" scope="col" bgcolor="#dde8f0"></td>--%>
				</tr>
				<bs:define id="color" value="#FEF1E2" />
				<logic:iterate id="item" property="output.listaSoggetti"
					name="navForm"
					offset="${navForm.offset}" indexId="riga">
					<sbn:rowcolor var="color" index="riga"></sbn:rowcolor>
					<tr bgcolor="${color}">
						<td ><%--<bs:write name="item" property="progr" />--%>
						<sbn:anchor name="item" property="progr" /> <sbn:linkbutton
							index="cid" name="item" value="progr"
							key="button.analitica" bundle="gestioneSemanticaLabels"
							title="analitica" property="codSelezionato" /></td>
						<td ><bs:write
							name="item" property="cid" /></td>
						<td ><bs:write
							name="item" property="codiceSoggettario" />
						<c:if test="${item.gestisceEdizione}">
								&nbsp;<bs:write name="item" property="edizioneSoggettario" />
							</c:if>
						</td>
						<td ><bs:write
							name="item" property="stato" /></td>
						<!--<td bgcolor="${color}" ><bs:write
							name="item" property="indicatore" /></td>-->
						<td ><bs:write
							name="item" property="testo" /></td>
						<td ><html:radio property="codSelezionato"
							value="${item.cid}" /></td>
						<%--<td >
								<bs:define
							id="codval">
							<bs:write name="item" property="progr" />
						</bs:define> <html:multibox property="codSoggetto"
							value="${codval}" /></td>--%>
					</tr>
				</logic:iterate>
			</table>
			<sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
				parameter="methodCatturaSog" totBlocchi="totBlocchi" bottom="true" />
		</logic:notEmpty> <!-- Intestazione livello di ricerca --></div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<!-- Bottoni che risultano abilitati se il livello di ricerca è impostato a Indice -->
				<sbn:checkAttivita idControllo="CATTURA">
					<td align="center"><html:submit property="methodCatturaSog">
						<bean:message key="button.cattura"
							bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</sbn:checkAttivita>
				<td align="center"><html:submit property="methodCatturaSog">
					<bean:message key="button.annulla" bundle="gestioneSemanticaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>

