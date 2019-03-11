<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/soggetto/SinteticaDescrittori.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
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
			</c:set>
			<sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
				parameter="methodSinteticaDes" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe" livelloRicerca="${livelloRicerca}" />

			<table class="sintetica">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="sintetica.progr"
							bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="sintetica.did" bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="sintetica.soggettario"
							bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
							<bean:message key="sintetica.headerStato"
								bundle="gestioneSemanticaLabels" />
					</th>
					<c:if test="${!navForm.enableIndice}">
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
							<bean:message key="sintetica.sogg"
								bundle="gestioneSemanticaLabels" />
						</th>
						<%--<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
							<bean:message key="sintetica.utilizzati"
								bundle="gestioneSemanticaLabels" />
						</th>--%>
					</c:if>
					<th class="etichetta" scope="col" bgcolor="#dde8f0">
						<bean:message key="sintetica.termine"
							bundle="gestioneSemanticaLabels" />
					</th>
					<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0"></th>
					<th width="3%" class="etichetta" scope="col" bgcolor="#dde8f0"></th>
				</tr>
				<bs:define id="color" value="#FEF1E2" />
				<logic:iterate id="item"
					property="outputDescrittori.risultati"
					name="navForm" indexId="riga">
					<sbn:rowcolor var="color" index="riga" />
					<tr bgcolor="${color}">
						<td >
							<sbn:anchor name="item" property="progr" />
							<sbn:linkbutton index="did" name="item" value="progr"
								key="button.analitica" bundle="gestioneSemanticaLabels"
								title="analitica" property="codSoggettoRadio" />
						</td>
						<td >
							<sbn:linkbutton index="did" name="item" value="did"
								key="button.analitica" bundle="gestioneSemanticaLabels"
								title="analitica" property="codSoggettoRadio" />
						</td>
						<td >
							<bs:write name="item" property="codiceSoggettario" />
							<c:if test="${item.gestisceEdizione}">
								&nbsp;<bs:write name="item" property="edizioneSoggettario" />
							</c:if>
						</td>
						<td >
							<bs:write name="item"
								property="livelloAutorita" />
						</td>
						<c:choose>
							<c:when test="${!navForm.enableIndice}">
								<td >
									<sbn:linkbutton bundle="gestioneSemanticaLabels"
										name="item" index="did" key="button.soggetti"
										property="codSoggettoRadio" title="soggetti" value="soggetti"
										disabled="${item.soggetti eq '0'}" />
								</td>
								<%--<td >
									<sbn:linkbutton bundle="gestioneSemanticaLabels"
										name="item" index="did" key="button.utilizzati"
										property="codSoggettoRadio" title="utilizzati"
										value="utilizzati"
										disabled="${item.utilizzati eq '0' or item.utilizzati ne '0'}" />
								</td>--%>
							</c:when>
							<c:otherwise>

							</c:otherwise>
						</c:choose>
						<td >
							<%--<bs:write name="item" property="termine" filter="false" />--%>
							<sbn:linkwrite bundle="gestioneSemanticaLabels"
								buttonKey="button.analitica" keyProperty="codSoggettoRadio"
								name="item" property="termineWithLinks" />
						</td>
						<td >
							<html:radio property="codSoggettoRadio"
								value="${item.did}" />
						</td>
						<td >
							<html:multibox property="codSoggetto" value="${item.did}" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</div>
		<div id="divFooterCommon">
			<sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
				parameter="methodSinteticaDes" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe" bottom="true"></sbn:blocchi>
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<!-- Bottoni che risultano disabilitati se il livello di ricerca è impostato a Indice -->
					<c:choose>
						<c:when test="${!navForm.enableIndice}">

							<logic:equal name="navForm"
								property="enableCercaIndice" value="true">
								<td align="center">
									<html:submit property="methodSinteticaDes">
										<bean:message key="button.cercaIndice"
											bundle="gestioneSemanticaLabels" />
									</html:submit>
								</td>
							</logic:equal>

							<logic:equal name="navForm"
								property="enableEsamina" value="true">
								<td align="center">
									<html:submit property="methodSinteticaDes">
										<bean:message key="button.analitica"
											bundle="gestioneSemanticaLabels" />
									</html:submit>
								</td>
							</logic:equal>
							<logic:equal name="navForm"
								property="enableStampa" value="true">
								<td align="center">
									<html:submit property="methodSinteticaSog">
										<bean:message key="button.stampa"
											bundle="gestioneSemanticaLabels" />
									</html:submit>
								</td>
							</logic:equal>
							<td align="center">
								<html:submit property="methodSinteticaDes">
									<bean:message key="button.annulla"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</td>
							<td align="right">
								<html:submit styleClass="buttonSelezTutti"
									property="methodSinteticaDes" title="Seleziona tutto">
									<bean:message key="button.selTutti"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
								<html:submit styleClass="buttonSelezNessuno"
									property="methodSinteticaDes" title="Deseleziona tutto">
									<bean:message key="button.deselTutti"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</td>
						</c:when>
						<c:otherwise>
							<td align="center">
								<html:submit property="methodSinteticaDes">
									<bean:message key="button.soggetti"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaDes">
									<bean:message key="button.analitica"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</td>
							<logic:equal name="navForm"
								property="enableStampa" value="true">
								<td align="center">
									<html:submit property="methodSinteticaSog">
										<bean:message key="button.stampa"
											bundle="gestioneSemanticaLabels" />
									</html:submit>
								</td>
							</logic:equal>
							<td align="center">
								<html:submit property="methodSinteticaDes">
									<bean:message key="button.annulla"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</td>
							<td align="right">
								<html:submit styleClass="buttonSelezTutti"
									property="methodSinteticaDes" title="Seleziona tutto">
									<bean:message key="button.selTutti"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
								<html:submit styleClass="buttonSelezNessuno"
									property="methodSinteticaDes" title="Deseleziona tutto">
									<bean:message key="button.deselTutti"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>

