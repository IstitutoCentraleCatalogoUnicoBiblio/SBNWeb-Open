<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform
		action="/gestionesemantica/soggetto/AnaliticaDescrittore.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<table width="100%" border="0">
				<tr>
					<td><c:choose>
							<c:when test="${!navForm.enableIndice}">
								<bean:message key="label.livRicercaPolo"
									bundle="gestioneBibliograficaLabels" />
							</c:when>
							<c:otherwise>
								<bean:message key="label.livRicercaIndice"
									bundle="gestioneBibliograficaLabels" />
							</c:otherwise>
						</c:choose> <c:choose>
							<c:when test="${navForm.listaDidSelezPresent eq 'SI'}">
								<html:submit property="methodAnaDet">
									<bean:message key="button.elemPrec"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
								<html:submit property="methodAnaDet">
									<bean:message key="button.elemSucc"
										bundle="gestioneSemanticaLabels" />
								</html:submit>
							</c:when>
						</c:choose></td>
				</tr>
			</table>
			<br>
			<table>
				<tr>
					<td class="etichetta"><bean:message
							key="analitica.soggettario" bundle="gestioneSemanticaLabels" /></td>
					<td><html:select styleClass="testoNormale"
							property="treeElementViewSoggetti.dettaglio.campoSoggettario"
							disabled="true">
							<html:optionsCollection property="listaSoggettari" value="codice"
								label="descrizione" />
						</html:select></td>
					<td class="etichetta"><c:if
							test="${navForm.treeElementViewSoggetti.dettaglio.gestisceEdizione}">
							<bean:message key="ricerca.edizione"
								bundle="gestioneSemanticaLabels" />&nbsp;<html:select
								styleClass="testoNormale"
								property="treeElementViewSoggetti.dettaglio.edizioneSoggettario"
								disabled="true">
								<html:optionsCollection property="listaEdizioni"
									value="cd_tabellaTrim" label="ds_tabella" />
							</html:select>
						</c:if></td>
					<td><bean:message key="esamina.categoria.did"
							bundle="gestioneSemanticaLabels" />&nbsp; <html:select
							styleClass="testoNormale"
							property="treeElementViewSoggetti.dettaglio.categoriaTermine"
							disabled="true">
							<html:optionsCollection property="listaCategoriaTermine"
								value="cd_tabellaTrim" label="ds_tabella" />
						</html:select></td>
				</tr>
				<tr>
					<td class="etichetta"><bean:message key="analitica.stato"
							bundle="gestioneSemanticaLabels" /></td>
					<td><html:select styleClass="testoNormale"
							property="treeElementViewSoggetti.livelloAutorita"
							disabled="true">
							<html:optionsCollection property="listaStatoControllo"
								value="codice" label="descrizione" />
						</html:select></td>
				</tr>
			</table>
			<br>
			<sbn:tree root="treeElementViewSoggetti" divClass="analitica"
				visualCheck="${navForm.visualCheckCattura}"
				propertyRadio="nodoSelezionato" propertyCheck="checkSelezionato"
				enableSubmit="true" imagesPath="/sbn/images/tree/"
				enabled="${!navForm.enableConferma}" />
			<br>
			<table width="100%" border="0">
				<tr>
					<td align="left" class="etichetta" scope="col"><bean:message
							key="analitica.inserito" bundle="gestioneSemanticaLabels" /></td>
					<td class="etichetta"><bean:message key="analitica.il"
							bundle="gestioneSemanticaLabels" /></td>
					<td><html:text styleId="testoNormale"
							property="treeElementViewSoggetti.areaDatiDettaglioOggettiVO.dettaglioDescrittoreGeneraleVO.dataIns"
							size="14" maxlength="20" readonly="true"></html:text></td>
					<td class="etichetta"><bean:message key="analitica.modificato"
							bundle="gestioneSemanticaLabels" /></td>
					<td class="etichetta"><bean:message key="analitica.il"
							bundle="gestioneSemanticaLabels" /></td>
					<td><html:text styleId="testoNormale"
							property="treeElementViewSoggetti.areaDatiDettaglioOggettiVO.dettaglioDescrittoreGeneraleVO.dataAgg"
							size="14" maxlength="20" readonly="true"></html:text></td>
				</tr>
			</table>
			<br>
			<table width="100%" border="0">
				<c:choose>
					<c:when test="${!navForm.enableIndice}">
						<tr>

							<td align="center" class="etichetta" scope="col"><bean:message
									key="esamina.soggetti" bundle="gestioneSemanticaLabels" /></td>

							<td><html:text styleId="testoNormale" property="numSoggetti"
									size="10" readonly="true"></html:text></td>

							<td align="center" class="etichetta" scope="col"><bean:message
									key="esamina.utilizzati" bundle="gestioneSemanticaLabels" /></td>

							<td><html:text styleId="testoNormale"
									property="numUtilizzati" size="10" readonly="true"></html:text></td>
						</tr>
					</c:when>
					<c:otherwise>

					</c:otherwise>
				</c:choose>
			</table>
		</div>

		<div id="divFooter">
			<table align="center">
				<tr>
					<c:choose>
						<c:when test="${navForm.enableConferma}">
							<jsp:include
								page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/confermaOperazione.jsp" />
						</c:when>
						<c:otherwise>
							<jsp:include
								page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/bottonieraAnaliticaDescrittore.jsp" />
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
