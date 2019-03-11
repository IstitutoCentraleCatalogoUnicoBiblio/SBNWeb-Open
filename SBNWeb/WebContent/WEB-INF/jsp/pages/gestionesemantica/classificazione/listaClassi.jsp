<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/gestionesemantica/classificazione/ListaClassi.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<jsp:include
			page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" />
		<table width="100%" border="0">
			<tr>
				<c:choose>
					<c:when test="${navForm.enableTit}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
					</c:when>
					<c:otherwise>

					</c:otherwise>
				</c:choose>

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
					parameter="methodSinteticaCla" totBlocchi="totBlocchi"
					elementiPerBlocco="maxRighe" livelloRicerca="${livelloRicerca}" />
			</tr>
		</table>
		<logic:notEmpty name="navForm" property="output">
			<table class="sintetica">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.progr"
						bundle="gestioneSemanticaLabels" /></th>
					<c:if test="${!navForm.enableIndice}">
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center"><bean:message
							key="sintetica.condiviso" bundle="gestioneSemanticaLabels" /></th>
					</c:if>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="ricerca.sistema"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="ricerca.edizione"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="ricerca.simbolo"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.headerStato"
						bundle="gestioneSemanticaLabels" /></th>
					<c:if test="${!navForm.enableIndice}">
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center"><bean:message
							key="sintetica.titoli" bundle="gestioneSemanticaLabels" /></th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0"
								align="center"><bean:message key="sintetica.titoliBib"
								bundle="gestioneSemanticaLabels" /></th>
					</c:if>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="sintetica.descrizione"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
				</tr>
				<bs:define id="color" value="#FEF1E2" />
				<logic:iterate id="item" property="output.risultati"
					name="navForm" offset="${navForm.offset}"
					indexId="progr">
					<sbn:rowcolor var="color" index="progr" />
					<tr>
						<td bgcolor="${color}" ><sbn:anchor
							name="item" property="progr" /> <sbn:linkbutton
							index="identificativoClasse" name="item" value="progr"
							key="button.dettaglio" bundle="gestioneSemanticaLabels"
							title="esamina" property="codSelezionato" /></td>
						<c:if test="${!navForm.enableIndice}">
							<td bgcolor="${color}" ><bs:write
								name="item" property="condivisoLista" /></td>

						</c:if>
						<td bgcolor="${color}" ><bs:write
							name="item" property="simboloDewey.sistema" /></td>
						<td bgcolor="${color}" ><bs:write
							name="item" property="simboloDewey.edizione" /></td>
						<td bgcolor="${color}" ><bs:write
							name="item" property="simboloDewey.simbolo" /></td>
						<td bgcolor="${color}" ><bs:write
							name="item" property="livelloAutorita" /></td>
						<c:if test="${!navForm.enableIndice}">
							<td bgcolor="${color}" ><!-- <bs:write
									name="item" property="numTitoliPolo" /></td>--> <sbn:linkbutton
								bundle="gestioneSemanticaLabels" name="item"
								index="identificativoClasse" key="button.polo"
								title="titoli polo" property="codSelezionato"
								value="numTitoliPolo"
								disabled="${item.numTitoliPolo eq '0'}"></sbn:linkbutton>
							</td>
							<td bgcolor="${color}" ><!--<bs:write
									name="item" property="numTitoliBiblio" />--> <sbn:linkbutton
									bundle="gestioneSemanticaLabels" name="item"
									index="identificativoClasse" key="button.biblio"
									property="codSelezionato" value="numTitoliBiblio"
									title="titoli biblio"
									disabled="${item.numTitoliBiblio eq '0'}"></sbn:linkbutton>
							</td>
						</c:if>
						<td bgcolor="${color}" ><bs:write
							name="item" property="parole" /></td>
						<td bgcolor="${color}" ><html:radio
							property="codSelezionato"
							value="${item.identificativoClasse}" /></td>
						<td bgcolor="${color}" ><bs:define
							id="codval">
							<bs:write name="item" property="progr" />
						</bs:define>
						<html:multibox property="codClasse"	value="${item.identificativoClasse}" />
						<html:hidden property="codClasse" value="" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty> <sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
			parameter="methodSinteticaCla" totBlocchi="totBlocchi"
			elementiPerBlocco="maxRighe" bottom="true" /> <c:if
			test="${navForm.enableScorrimento}">
			<table align="center" border="1">
				<tr>
					<th class="etichetta"><bean:message key="ricerca.scorrimento"
						bundle="gestioneSemanticaLabels" /></th>
					<th class="etichetta"><bean:message key="ricerca.edizione"
						bundle="gestioneSemanticaLabels" /> <html:select
						styleClass="testoNormale"
						property="ricercaClasse.codEdizioneDewey">
						<html:optionsCollection property="listaEdizioni" value="codice"
							label="descrizione" />
					</html:select></th>
					<th class="etichetta" scope="col"><bean:message
						key="ricerca.simbolo" bundle="gestioneSemanticaLabels" /> <html:text
						styleId="testoNormale" property="ricercaClasse.simbolo"></html:text>
					</th>
				</tr>
			</table>
			<table align="center" border="1">
				<tr>
					<td align="center"><html:submit styleClass="buttonTagAlto"
						property="methodSinteticaCla" title="Alto">
						<bean:message key="button.alto" bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</tr>
			</table>
			<table align="center" border="1">
				<tr>
					<td align="center"><html:submit styleClass="buttonTagSinistra"
						property="methodSinteticaCla" title="Sinistra">
						<bean:message key="button.sinistra"
							bundle="gestioneSemanticaLabels" />
					</html:submit></td>
					<td align="center"><html:submit styleClass="buttonTagRadice"
						property="methodSinteticaCla">
						<bean:message key="button.radice" bundle="gestioneSemanticaLabels" />
					</html:submit></td>
					<td align="center"><html:submit styleClass="buttonTagDestra"
						property="methodSinteticaCla" title="Destra">
						<bean:message key="button.destra" bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</tr>
			</table>
			<table align="center" border="1">
				<tr>
					<td align="center"><html:submit styleClass="buttonTagBasso"
						property="methodSinteticaCla" title="Basso">
						<bean:message key="button.basso" bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</tr>
			</table>
		</c:if></div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<!-- Bottoni che risultano disabilitati se il livello di ricerca è impostato a Indice -->
				<c:choose>
					<c:when test="${!navForm.enableIndice}">
						<c:choose>
							<c:when test="${navForm.enableOkTit}">
								<td align="center"><html:submit
									property="methodSinteticaCla">
									<bean:message key="button.scegli"
										bundle="gestioneSemanticaLabels" />
								</html:submit></td>
								<sbn:checkAttivita idControllo="CREA">
									<logic:equal name="navForm"
										property="enableCreaListaPolo" value="true">
										<td align="center"><html:submit
											property="methodSinteticaCla">
											<bean:message key="button.creaSog"
												bundle="gestioneSemanticaLabels" />
										</html:submit></td>
									</logic:equal>
								</sbn:checkAttivita>
								<td align="center"><html:submit
									property="methodSinteticaCla">
									<bean:message key="button.stampa"
										bundle="gestioneSemanticaLabels" />
								</html:submit></td>
								<td align="center"><html:submit
									property="methodSinteticaCla">
									<bean:message key="button.annulla"
										bundle="gestioneSemanticaLabels" />
								</html:submit></td>
							</c:when>
							<c:otherwise>
								<logic:equal name="navForm" property="enableOk"
									value="true">
									<td align="center"><html:submit
										property="methodSinteticaCla">
										<bean:message key="button.scegli"
											bundle="gestioneSemanticaLabels" />
									</html:submit></td>
								</logic:equal>
								<logic:equal name="navForm" property="enableCercaIndice"
									value="true">
									<td align="center"><html:submit
										property="methodSinteticaCla">
										<bean:message key="button.cercaIndice"
											bundle="gestioneSemanticaLabels" />
									</html:submit></td>
								</logic:equal>
								<sbn:checkAttivita idControllo="CREA">
									<logic:equal name="navForm"
										property="enableCreaListaPolo" value="true">
										<td align="center"><html:submit
											property="methodSinteticaCla">
											<bean:message key="button.creaSog"
												bundle="gestioneSemanticaLabels" />
										</html:submit></td>
									</logic:equal>
								</sbn:checkAttivita>
								<!--
								<td align="center"><html:submit
									property="methodSinteticaCla">
									<bean:message key="button.esamina"
										bundle="gestioneSemanticaLabels" />
								</html:submit></td>
								-->
								<sbn:checkAttivita idControllo="MODIFICA">
									<td align="center"><html:submit
										property="methodSinteticaCla">
										<bean:message key="button.gestione"
											bundle="gestioneSemanticaLabels" />
									</html:submit></td>
								</sbn:checkAttivita>
								<td align="center"><html:submit
									property="methodSinteticaCla">
									<bean:message key="button.stampa"
										bundle="gestioneSemanticaLabels" />
								</html:submit></td>
								<td align="center"><html:submit
									property="methodSinteticaCla">
									<bean:message key="button.annulla"
										bundle="gestioneSemanticaLabels" />
								</html:submit></td>
								<td align="right"><html:submit
									styleClass="buttonSelezTutti" property="methodSinteticaCla"
									title="Seleziona tutto">
									<bean:message key="button.selTutti"
										bundle="gestioneSemanticaLabels" />
								</html:submit> <html:submit styleClass="buttonSelezNessuno"
									property="methodSinteticaCla" title="Deseleziona tutto">
									<bean:message key="button.deselTutti"
										bundle="gestioneSemanticaLabels" />
								</html:submit></td>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<!--
						<td align="center"><html:submit property="methodSinteticaCla">
							<bean:message key="button.esamina"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
						-->
						<sbn:checkAttivita idControllo="CREA">
							<td align="center"><html:submit
								property="methodSinteticaCla">
								<bean:message key="button.importa"
									bundle="gestioneSemanticaLabels" />
							</html:submit></td>
						</sbn:checkAttivita>
						<%--<td align="center"><html:submit property="methodSinteticaCla">
							<bean:message key="button.titoli.indice"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>  --%>
						<td align="center"><html:submit property="methodSinteticaCla">
							<bean:message key="button.stampa"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
						<td align="center"><html:submit property="methodSinteticaCla">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
						<td align="right"><html:submit styleClass="buttonSelezTutti"
							property="methodSinteticaCla" title="Seleziona tutto">
							<bean:message key="button.selTutti"
								bundle="gestioneSemanticaLabels" />
						</html:submit> <html:submit styleClass="buttonSelezNessuno"
							property="methodSinteticaCla" title="Deseleziona tutto">
							<bean:message key="button.deselTutti"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</c:otherwise>
				</c:choose>
				<td>
				<layout:combo bundle="gestioneSemanticaLabels"
					label="button.esamina" name="navForm"
					button="button.esegui" property="idFunzioneEsamina"
					combo="comboGestioneEsamina" parameter="methodSinteticaCla" /></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>

