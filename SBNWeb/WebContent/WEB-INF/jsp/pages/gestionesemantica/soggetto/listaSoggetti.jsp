<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/soggetto/ListaSoggetti.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<jsp:include
			page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" />

		<table width="100%" border="0">
			<c:choose>
				<c:when test="${navForm.enableTit}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
				</c:when>
				<c:otherwise>

				</c:otherwise>
			</c:choose>
		</table>
		<c:set var="livelloRicerca">
			<c:choose>
				<c:when test="${!navForm.enableIndice}">
				           P
				        </c:when>
				<c:otherwise>
							I
				        </c:otherwise>
			</c:choose>
		</c:set> <sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
			parameter="methodSinteticaSog" totBlocchi="totBlocchi"
			elementiPerBlocco="maxRighe" livelloRicerca="${livelloRicerca}" /> <logic:notEmpty
			name="navForm" property="output">
			<table class="sintetica">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.progr"
						bundle="gestioneSemanticaLabels" /></th>
					<c:choose>
						<c:when test="${!navForm.enableIndice}">
							<th class="etichetta" scope="col" bgcolor="#dde8f0"
								align="center"><bean:message key="sintetica.condiviso"
								bundle="gestioneSemanticaLabels" /></th>
						</c:when>
						<c:otherwise>

						</c:otherwise>
					</c:choose>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.cid" bundle="gestioneSemanticaLabels" />
					</th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.soggettario"
						bundle="gestioneSemanticaLabels" /></th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.headerStato"
						bundle="gestioneSemanticaLabels" /></th>
					<c:choose>
						<c:when test="${!navForm.enableIndice}">
							<th class="etichetta" scope="col" bgcolor="#dde8f0"
								align="center"><bean:message key="sintetica.titoli"
								bundle="gestioneSemanticaLabels" /></th>
							<th class="etichetta" scope="col" bgcolor="#dde8f0"
								align="center"><bean:message key="sintetica.titoliBib"
								bundle="gestioneSemanticaLabels" /></th>
						</c:when>
						<c:otherwise>

						</c:otherwise>
					</c:choose>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.testo"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
				</tr>
				<bs:define id="color" value="#FEF1E2" />
				<logic:iterate id="item" property="output.risultati"
					name="navForm" offset="${navForm.offset}"
					indexId="riga">
					<sbn:rowcolor var="color" index="riga" />
					<tr bgcolor="${color}">
						<td ><sbn:anchor name="item"
							property="progr" /> <sbn:linkbutton index="cid"
							name="item" value="progr" key="button.analitica"
							bundle="gestioneSemanticaLabels" title="analitica"
							property="codSelezionato"
							withAnchor="false" /></td>
						<c:choose>
							<c:when test="${!navForm.enableIndice}">
								<td ><bs:write
									name="item" property="condivisoLista" /></td>
							</c:when>
							<c:otherwise>

							</c:otherwise>
						</c:choose>
						<td ><sbn:linkbutton index="cid"
							name="item" value="cid" key="button.analitica"
							bundle="gestioneSemanticaLabels" title="analitica"
							property="codSelezionato"
							withAnchor="false" /></td>

						<td >
							<bs:write name="item" property="codiceSoggettario" />
							<c:if test="${item.gestisceEdizione}">
								&nbsp;<bs:write name="item" property="edizioneSoggettario" />
							</c:if>
						</td>

						<td ><bs:write
							name="item" property="stato" /></td>
						<c:choose>
							<c:when test="${!navForm.enableIndice}">

								<td ><!-- <bs:write
									name="item" property="numTitoliPolo" /></td>--> <sbn:linkbutton
									bundle="gestioneSemanticaLabels" name="item"
									index="cid" key="button.polo" property="codSelezionato"
									title="titoli polo" value="numTitoliPolo"
									disabled="${item.numTitoliPolo eq '0'}" /></td>
								<td ><!--<bs:write
									name="item" property="numTitoliBiblio" />--> <sbn:linkbutton
									bundle="gestioneSemanticaLabels" name="item"
									index="cid" key="button.biblio" property="codSelezionato"
									title="titoli biblio" value="numTitoliBiblio"
									disabled="${item.numTitoliBiblio eq '0'}" /></td>
							</c:when>
							<c:otherwise>

							</c:otherwise>
						</c:choose>
						<td ><bs:write
							name="item" property="testo" /></td>
						<td width="3%" ><html:radio
							property="codSelezionato" value="${item.cid}" /></td>
						<td width="3%" >
							<html:multibox property="codSoggetto" value="${item.cid}" />
							<html:hidden property="codSoggetto" value="" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty></div>
		<div id="divFooterCommon"><sbn:blocchi numBlocco="numBlocco"
			numNotizie="totRighe" parameter="methodSinteticaSog"
			totBlocchi="totBlocchi" elementiPerBlocco="maxRighe" bottom="true"></sbn:blocchi>
		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<!-- Bottoni che risultano disabilitati se il livello di ricerca è impostato a Indice -->
				<c:choose>
					<c:when test="${!navForm.enableIndice}">
						<logic:equal name="navForm" property="enableOk"
							value="true">
							<td align="center"><html:submit
								property="methodSinteticaSog">
								<bean:message key="button.scegli"
									bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</logic:equal>

						<logic:equal name="navForm" property="enableCercaIndice"
							value="true">
							<td align="center"><html:submit
								property="methodSinteticaSog">
								<bean:message key="button.cercaIndice"
									bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</logic:equal>
						<sbn:checkAttivita idControllo="CREA">
							<td align="center"><html:submit
								property="methodSinteticaSog">
								<bean:message key="button.crea" bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</sbn:checkAttivita>
						<logic:equal name="navForm" property="enableEsamina"
							value="true">
							<td align="center"><html:submit
								property="methodSinteticaSog">
								<bean:message key="button.analitica"
									bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</logic:equal>
						<logic:equal name="navForm" property="enableStampa"
							value="true">
							<td align="center"><html:submit
								property="methodSinteticaSog">
								<bean:message key="button.stampa"
									bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</logic:equal>
						<td align="center"><html:submit property="methodSinteticaSog">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
						<td align="right"><html:submit styleClass="buttonSelezTutti"
							property="methodSinteticaSog" title="Seleziona tutto">
							<bean:message key="button.selTutti"
								bundle="gestioneSemanticaLabels" />
						</html:submit> <html:submit styleClass="buttonSelezNessuno"
							property="methodSinteticaSog" title="Deseleziona tutto">
							<bean:message key="button.deselTutti"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<sbn:checkAttivita idControllo="IMPORTA">
							<td align="center"><html:submit
								property="methodSinteticaSog">
								<bean:message key="button.importa"
									bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</sbn:checkAttivita>
						<sbn:checkAttivita idControllo="CREA">
							<td align="center"><html:submit
								property="methodSinteticaSog">
								<bean:message key="button.crea" bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</sbn:checkAttivita>
						<logic:equal name="navForm" property="enableEsamina"
							value="true">
							<td align="center"><html:submit
								property="methodSinteticaSog">
								<bean:message key="button.analitica"
									bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</logic:equal>

						<logic:equal name="navForm" property="enableStampa"
							value="true">
							<td align="center"><html:submit
								property="methodSinteticaSog">
								<bean:message key="button.stampa"
									bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</logic:equal>
						<td align="center"><html:submit property="methodSinteticaSog">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
						<td align="right"><html:submit styleClass="buttonSelezTutti"
							property="methodSinteticaSog" title="Seleziona tutto">
							<bean:message key="button.selTutti"
								bundle="gestioneSemanticaLabels" />
						</html:submit> <html:submit styleClass="buttonSelezNessuno"
							property="methodSinteticaSog" title="Deseleziona tutto">
							<bean:message key="button.deselTutti"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>

				<td>
				<layout:combo bundle="gestioneSemanticaLabels"
					label="button.esamina" name="navForm"
					button="button.esegui" property="idFunzioneEsamina"
					combo="comboGestioneEsamina" parameter="methodSinteticaSog" />
				</td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>

