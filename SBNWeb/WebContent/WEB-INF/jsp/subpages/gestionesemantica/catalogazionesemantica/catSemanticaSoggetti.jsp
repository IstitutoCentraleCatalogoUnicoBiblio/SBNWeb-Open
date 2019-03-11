<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%--<table width="100%" border="0">
	<tr>
		<td class="etichetta">
			<bean:message key="sintetica.soggettario"
				bundle="gestioneSemanticaLabels" />
			<html:select styleClass="testoNormale"
				property="catalogazioneSemanticaComune.catalogazioneSoggetto.codSoggettario">
				<html:optionsCollection property="listaSoggettari" value="codice"
					label="descrizione" />
			</html:select>
			<html:submit property="methodCatalogazione">
				<bean:message key="button.aggiornaDati"
					bundle="gestioneSemanticaLabels" />
			</html:submit>
		</td>
	</tr>
</table>
 --%>
<table width="100%" border="0">
	<tr>
		<c:set var="livelloRicerca">
			<c:choose>
				<c:when test="${!navForm.enableIndice}">
				           P
				        </c:when>
				<c:otherwise>
							I
				        </c:otherwise>
			</c:choose>
		</c:set>
		<sbn:blocchi
			numBlocco="catalogazioneSemanticaComune.catalogazioneSoggetto.numBlocco"
			numNotizie="catalogazioneSemanticaComune.catalogazioneSoggetto.numNotizie"
			parameter="methodCatalogazione"
			totBlocchi="catalogazioneSemanticaComune.catalogazioneSoggetto.totBlocchi"
			elementiPerBlocco="catalogazioneSemanticaComune.catalogazioneSoggetto.maxRighe"
			livelloRicerca="${livelloRicerca}" />
	</tr>
</table>

<logic:notEmpty name="navForm"
	property="catalogazioneSemanticaComune.catalogazioneSoggetto.listaSoggetti">
	<table class="sintetica">
		<tr>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="catalogazione.progr"
				bundle="gestioneSemanticaLabels" /></th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="catalogazione.cid"
				bundle="gestioneSemanticaLabels" /></th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="catalogazione.soggettario"
				bundle="gestioneSemanticaLabels" /></th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="catalogazione.headerStato"
				bundle="gestioneSemanticaLabels" /></th>
			<c:if test="${!navForm.enableIndice}">
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
				<bean:message key="catalogazione.headerMaxLivAut"
					bundle="gestioneSemanticaLabels" /></th>
			</c:if>
			<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
			<bean:message key="catalogazione.testo"
				bundle="gestioneSemanticaLabels" /></th>
			<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
		</tr>
		<bs:define id="color" value="#FEF1E2" />
		<logic:iterate id="item"
			property="catalogazioneSemanticaComune.catalogazioneSoggetto.listaSoggetti"
			name="navForm" indexId="progr">
			<sbn:rowcolor var="color" index="progr" />
			<tr bgcolor="${color}">
				<td ><sbn:anchor name="item"
					property="progr" /> <sbn:linkbutton index="cid"
					name="item" value="progr" key="button.gestione"
					bundle="gestioneSemanticaLabels" title="gestione"
					property="catalogazioneSemanticaComune.catalogazioneSoggetto.codSelezionato"
					disabled="${navForm.enableSoloEsamina}"/>
				</td>
				<td ><bs:write name="item"
					property="cid" /></td>
				<td ><bs:write name="item"
					property="codiceSoggettario" />
					<c:if test="${item.gestisceEdizione}">
						&nbsp;<bs:write name="item" property="edizioneSoggettario" />
					</c:if>
				</td>
				<td ><bs:write name="item"
					property="stato" /></td>
				<c:if test="${!navForm.enableIndice}">
					<%--<td ><c:choose>
						<c:when test="${item.numTitoliBiblio gt 0}">
							<bean:message key="catalogazione.utilizzato.si"
								bundle="gestioneSemanticaLabels" />
						</c:when>
						<c:otherwise>
							<bean:message key="catalogazione.utilizzato.no"
								bundle="gestioneSemanticaLabels" />
						</c:otherwise>
					</c:choose></td> --%>
					<td ><bs:write name="item"
						property="maxLivAutLegame" /></td>
				</c:if>
				<td ><bs:write name="item"
					property="testo" /></td>
				<td ><html:radio
					name="navForm"
					property="catalogazioneSemanticaComune.catalogazioneSoggetto.codSelezionato"
					value="${item.cid}" /></td>
			</tr>
		</logic:iterate>
	</table>
			<sbn:blocchi
			numBlocco="catalogazioneSemanticaComune.catalogazioneSoggetto.numBlocco"
			numNotizie="catalogazioneSemanticaComune.catalogazioneSoggetto.numNotizie"
			parameter="methodCatalogazione"
			totBlocchi="catalogazioneSemanticaComune.catalogazioneSoggetto.totBlocchi"
			elementiPerBlocco="catalogazioneSemanticaComune.catalogazioneSoggetto.maxRighe"
			bottom="true"/>

</logic:notEmpty>


