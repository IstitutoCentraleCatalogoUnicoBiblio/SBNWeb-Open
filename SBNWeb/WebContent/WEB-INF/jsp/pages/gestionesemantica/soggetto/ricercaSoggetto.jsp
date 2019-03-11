<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/soggetto/RicercaSoggetto.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" />
			<logic:notEmpty name="navForm" property="listaSoggettari">
				<table cellspacing="0" border="0">
					<tr>
						<td width="13%" class="etichetta">
							<bean:message key="gestionesemantica.soggetto.soggettario"
								bundle="gestioneSemanticaLabels" />
						</td>
						<td>
							<html:select styleClass="testoNormale"
								property="ricercaComune.codSoggettario">
								<html:optionsCollection property="listaSoggettari"
									value="codice" label="descrizione" />
							</html:select>
						</td>
						<td width="13%"><bean:message key="ricerca.edizione" bundle="gestioneSemanticaLabels" /></td>
						<td>
							<html:select styleClass="testoNormale" property="ricercaComune.edizioneSoggettario">
								<html:optionsCollection property="listaEdizioni" value="cd_tabellaTrim" label="ds_tabella" />
							</html:select>
						</td>
						<td>
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
					</tr>

				</table>
				<hr color="#dde8f0" />
				<table cellspacing="0" width="100%" border="0">
					<tr>
						<td>
							&nbsp;
						</td>
						<td class="etichetta">
							<bean:message key="gestionesemantica.soggetto.soggetto"
								bundle="gestioneSemanticaLabels" />
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="gestionesemantica.soggetto.testo"
								bundle="gestioneSemanticaLabels" />
						</td>
						<td>
							<html:text styleId="testoNormale"
								property="ricercaComune.ricercaSoggetto.testoSogg" size="50"
								maxlength="80"></html:text>
						</td>
						<td>
							<sbn:tastiera limit="80" name="RicercaSoggettoForm"
								property="ricercaComune.ricercaSoggetto.testoSogg" />
							<%--<html:select styleClass="testoNormale"
								property="ricercaComune.ricercaTipoSogg">
								<html:optionsCollection property="listaRicercaTipo"
									value="codice" label="descrizione" />
							</html:select>--%>
							<bean:message key="ricerca.inizio" bundle="gestioneBibliograficaLabels" />
							<html:radio property="ricercaComune.ricercaTipoSogg" value="STRINGA_INIZIALE" />
							<bean:message key="ricerca.intero" bundle="gestioneBibliograficaLabels" />
							<html:radio property="ricercaComune.ricercaTipoSogg" value="STRINGA_ESATTA" />
							<bean:message key="ricerca.parole" bundle="gestioneBibliograficaLabels" />
							<html:radio property="ricercaComune.ricercaTipoSogg" value="PAROLE" />
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="gestionesemantica.soggetto.cid"
								bundle="gestioneSemanticaLabels" />
						</td>
						<td>
							<html:text property="ricercaComune.ricercaSoggetto.cid"
								styleId="testoNormale" maxlength="10"></html:text>
						</td>
						<!-- Descrittori -->
						<c:choose>
							<c:when test="${!navForm.enableIndice}">
								<jsp:include
									page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/soggetti.jsp" />
							</c:when>
							<c:otherwise>
								<jsp:include
									page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/parole.jsp" />
							</c:otherwise>
						</c:choose>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
						<td class="etichetta">
							<bean:message key="gestionesemantica.soggetto.descrittore"
								bundle="gestioneSemanticaLabels" />
						</td>
					</tr>

					<tr>
						<td class="etichetta">
							<bean:message key="gestionesemantica.soggetto.testoDescrittore"
								bundle="gestioneSemanticaLabels" />
						</td>
						<td>
							<html:text styleId="testoNormale"
								property="ricercaComune.ricercaDescrittore.testoDescr" size="50"
								maxlength="80"></html:text>
						</td>
						<td>
							<sbn:tastiera limit="80" name="RicercaSoggettoForm"
								property="ricercaComune.ricercaDescrittore.testoDescr" />
							<%--<html:select styleClass="testoNormale"
								property="ricercaComune.ricercaTipoDescr">
								<html:optionsCollection property="listaRicercaTipo"
									value="codice" label="descrizione" />
							</html:select>--%>
							<bean:message key="ricerca.inizio" bundle="gestioneBibliograficaLabels" />
							<html:radio property="ricercaComune.ricercaTipoDescr" value="STRINGA_INIZIALE" />
							<bean:message key="ricerca.intero" bundle="gestioneBibliograficaLabels" />
							<html:radio property="ricercaComune.ricercaTipoDescr" value="STRINGA_ESATTA" />

						</td>

					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="gestionesemantica.soggetto.did"
								bundle="gestioneSemanticaLabels" />
						</td>
						<td>
							<html:text property="ricercaComune.ricercaDescrittore.did"
								styleId="testoNormale" maxlength="10"></html:text>
						</td>
						<td>
							&nbsp;
						</td>
						<td>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="gestionesemantica.soggetto.parole"
								bundle="gestioneSemanticaLabels" />
						</td>
						<td>
							<html:text property="ricercaComune.ricercaDescrittore.parole"
								styleId="testoNormale" size="30" maxlength="30"></html:text>
							<sbn:tastiera limit="30" name="RicercaSoggettoForm"
								property="ricercaComune.ricercaDescrittore.parole" />
						</td>
						<td width="57%">
							<html:text property="ricercaComune.ricercaDescrittore.parole1"
								styleId="testoNormale" size="30" maxlength=" "></html:text>
							<sbn:tastiera limit="30" name="RicercaSoggettoForm"
								property="ricercaComune.ricercaDescrittore.parole1" />
						</td>
						<td>
							&nbsp;
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
						<td>
							<html:text property="ricercaComune.ricercaDescrittore.parole2"
								styleId="testoNormale" size="30" maxlength=" "></html:text>
							<sbn:tastiera limit="30" name="RicercaSoggettoForm"
								property="ricercaComune.ricercaDescrittore.parole2" />
						</td>
						<td>
							<html:text property="ricercaComune.ricercaDescrittore.parole3"
								styleId="testoNormale" size="30" maxlength=" "></html:text>
							<sbn:tastiera limit="30" name="RicercaSoggettoForm"
								property="ricercaComune.ricercaDescrittore.parole3" />
						</td>
						<td>
							&nbsp;
						</td>
					</tr>
				</table>
		<!-- 	<hr color="#dde8f0" />
				<table>
					<tr>
						<td colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td class="etichetta">
							<bean:message key="ricerca.livello"
								bundle="gestioneSemanticaLabels" />&nbsp;
						</td>
						<td class="etichetta">
							<bean:message key="ricerca.da" bundle="gestioneSemanticaLabels" />
							<html:select styleClass="testoNormale"
								property="ricercaComune.livelloAutoritaDa">
								<html:optionsCollection property="listaStatoControllo"
									value="codice" label="descrizione" />
							</html:select>
							<bean:message key="ricerca.a" bundle="gestioneSemanticaLabels" />
							<html:select styleClass="testoNormale"
								property="ricercaComune.livelloAutoritaA">
								<html:optionsCollection property="listaStatoControllo"
									value="codice" label="descrizione" />
							</html:select>
						</td>
					</tr>
				</table> -->
				<div id="divFooterCommon">
					<table>
						<tr>
							<td class="etichetta">
								<bean:message key="gestionesemantica.soggetto.elementoPerBlocco"
									bundle="gestioneSemanticaLabels" />
								<html:text styleId="testoNormale"
									property="ricercaComune.elemBlocco" size="10" maxlength="4" />
							</td>
							<td class="etichetta">
								<bean:message key="gestionesemantica.soggetto.ordinamento"
									bundle="gestioneSemanticaLabels" />&nbsp;
								<html:select styleClass="testoNormale"
									property="ricercaComune.ordinamento">
									<html:optionsCollection property="listaOrdinamentoSoggetto"
										value="codice" label="descrizione" />
								</html:select>
							</td>
							<td class="etichetta">
								<bean:message key="gestionesemantica.soggetto.livelloDiRicerca"
									bundle="gestioneSemanticaLabels" />:&nbsp;
							</td>
							<td class="etichetta">
								<bean:message key="gestionesemantica.soggetto.polo"
									bundle="gestioneSemanticaLabels" />
								<html:radio property="ricercaComune.polo" value="true" />
							</td>
							<td class="etichetta">
								<bean:message key="gestionesemantica.soggetto.indice"
									bundle="gestioneSemanticaLabels" />
								<html:radio property="ricercaComune.polo" value="false" />
							</td>
						</tr>
					</table>
				</div>
				<div id="divFooter">
					<!-- BOTTONIERA inserire solo i SOLO td -->
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodRicerca" tabindex="1">
									<bean:message key="gestionesemantica.soggetto.bottone.cerca"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
								<logic:equal name="navForm" property="enableCrea"
									value="true">
									<html:submit property="methodRicerca" tabindex="2">
										<bean:message key="button.crea"
											bundle="gestioneSemanticaLabels" />
									</html:submit>
								</logic:equal>
							</td>
						</tr>
					</table>
				</div>
			</logic:notEmpty>
		</div>
	</sbn:navform>
</layout:page>

