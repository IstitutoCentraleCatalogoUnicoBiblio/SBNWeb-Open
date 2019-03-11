<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/soggetto/GestioneSoggetto.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors />
		</div>
		<logic:notEmpty name="navForm" property="listaStatoControllo">
			<logic:notEmpty name="navForm" property="listaTipoSoggetto">
				<table width="100%" border="0">
					<tr>
						<td><c:choose>
							<c:when test="${navForm.ricercaComune.polo}">
								<bean:message key="label.livRicercaPolo" bundle="gestioneBibliograficaLabels" />
							</c:when>
							<c:otherwise>
								<bean:message key="label.livRicercaIndice" bundle="gestioneBibliograficaLabels" />
							</c:otherwise>
						</c:choose></td>
					</tr>
				</table>
				<table width="100%" border="0">
					<tr>
						<td class="etichetta" scope="col" align="center"><bean:message
							key="gestione.cid" bundle="gestioneSemanticaLabels" /></td>
						<td><html:text styleId="testoNormale" property="cid"
							readonly="true">
						</html:text></td>

						<td class="etichetta"><bean:message
							key="gestione.soggettario" bundle="gestioneSemanticaLabels" /></td>
						<td><html:select styleClass="testoNormale"
							property="ricercaComune.codSoggettario" disabled="true">
							<html:optionsCollection property="listaSoggettari" value="codice"
								label="descrizione" />
						</html:select></td>
					</tr>
					<c:if test="${navForm.dettSogGenVO.gestisceEdizione}">
						<tr>
							<td colspan="2">&nbsp;</td>
							<td><bean:message key="ricerca.edizione" bundle="gestioneSemanticaLabels" /></td>
							<td><html:select styleClass="testoNormale"
									property="dettSogGenVO.edizioneSoggettario" disabled="${navForm.enableConferma}">
									<html:optionsCollection property="listaEdizioni"
										value="cd_tabellaTrim" label="ds_tabella" />
								</html:select></td>
						</tr>
					</c:if>
					<tr>
						<td class="etichetta"><bean:message
							key="gestione.statoDiControllo" bundle="gestioneSemanticaLabels" /></td>
						<td><html:select styleClass="testoNormale"
							disabled="${!navForm.abilita}"
							property="statoControllo">
							<html:optionsCollection property="listaStatoControllo"
								value="codice" label="descrizione" />
						</html:select></td>

						<c:if test="${navForm.ricercaComune.polo}">
							<td class="etichetta"><bean:message
								key="gestione.tipoDiSoggetto" bundle="gestioneSemanticaLabels" /></td>
							<td><html:select styleClass="testoNormale"
								property="tipoSoggetto"
								disabled="${!navForm.abilita}">
								<html:optionsCollection property="listaTipoSoggetto"
									value="codice" label="descrizione" />
							</html:select></td>
						</c:if>
					</tr>
				</table>
				<table width="100%" border="0">
					<tr>
						<td align="center" class="etichetta"><bean:message
							key="gestione.testo" bundle="gestioneSemanticaLabels" /></td>
					</tr>
					<tr>
						<td><html:textarea styleId="testoNormale" property="testo"
							cols="90" rows="6" disabled="${!navForm.abilita}" />
						<sbn:tastiera limit="240" name="GestioneSoggettoForm"
							property="testo" visible="${navForm.abilita}" /></td>
					</tr>
					<tr>
						<td class="etichetta" align="center"><bean:message
							key="gestione.note" bundle="gestioneSemanticaLabels" /></td>
					</tr>
					<tr>
						<td><html:textarea styleId="testoNormale" property="note"
							cols="90" rows="6" disabled="${!navForm.abilita}" />
						<sbn:tastiera limit="240" name="GestioneSoggettoForm"
							property="note" visible="${navForm.abilita}" /></td>
					</tr>
				</table>
				<table width="100%" border="0">
					<tr>
						<td align="left" class="etichetta" scope="col"><bean:message
							key="gestione.inserito" bundle="gestioneSemanticaLabels" /></td>
						<td class="etichetta"><bean:message key="gestione.il"
							bundle="gestioneSemanticaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettSogGenVO.dataIns" size="14" maxlength="20"
							readonly="true"></html:text></td>
						<td class="etichetta"><bean:message key="gestione.modificato"
							bundle="gestioneSemanticaLabels" /></td>
						<td class="etichetta"><bean:message key="gestione.il"
							bundle="gestioneSemanticaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettSogGenVO.dataAgg" size="14" maxlength="20"
							readonly="true"></html:text></td>
					</tr>
				</table>
				<!-- Titoli collegati -->
				<c:if test="${navForm.ricercaComune.polo}">
					<table width="100%" border="0">
						<tr>
							<td align="center" class="etichetta" scope="col"><bean:message
								key="esamina.titoli" bundle="gestioneSemanticaLabels" /></td>

							<td align="center" class="etichetta" scope="col"><bean:message
								key="esamina.polo" bundle="gestioneSemanticaLabels" /></td>

							<td><html:text styleId="testoNormale"
								property="dettSogGenVO.numTitoliPolo" size="10" readonly="true"></html:text></td>

							<td align="center" class="etichetta" scope="col"><bean:message
								key="esamina.biblio" bundle="gestioneSemanticaLabels" /></td>

							<td><html:text styleId="testoNormale"
								property="dettSogGenVO.numTitoliBiblio" size="10"
								readonly="true"></html:text></td>
						</tr>
					</table>
				</c:if>
			</logic:notEmpty>
		</logic:notEmpty>
		</div>
		<div id="divFooter">
		<c:choose>
			<c:when test="${navForm.enableConferma}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/bottonieraGestioneSoggetto.jsp" />
			</c:otherwise>
		</c:choose></div>
	</sbn:navform>
</layout:page>

