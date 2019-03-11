<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />

<layout:page>
	<sbn:navform action="/gestionesemantica/soggetto/AnaliticaSoggetto.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<c:if test="${navForm.enableTit}">
				<table width="100%" border="0">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
				</table>
			</c:if>
			<table width="100%" border="0">
				<tr>
					<td>
						<c:choose>
							<c:when test="${!navForm.enableIndice}">

								<bean:message key="label.livRicercaPolo"
									bundle="gestioneBibliograficaLabels" />
								<c:choose>
									<c:when
										test="${navForm.listaCidSelezPresent eq 'SI'}">
										<html:submit property="methodAnaSog">
											<bean:message key="button.elemPrec"
												bundle="gestioneSemanticaLabels" />
										</html:submit>
										<html:submit property="methodAnaSog">
											<bean:message key="button.elemSucc"
												bundle="gestioneSemanticaLabels" />
										</html:submit>
									</c:when>
								</c:choose>

							</c:when>
							<c:otherwise>

								<bean:message key="label.livRicercaIndice"
									bundle="gestioneBibliograficaLabels" />
								<c:choose>
									<c:when
										test="${navForm.listaCidSelezPresent eq 'SI'}">
										<html:submit property="methodAnaSog">
											<bean:message key="button.elemPrec"
												bundle="gestioneSemanticaLabels" />
										</html:submit>
										<html:submit property="methodAnaSog">
											<bean:message key="button.elemSucc"
												bundle="gestioneSemanticaLabels" />
										</html:submit>
									</c:when>
								</c:choose>

							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
			<br>
			<c:choose>
				<c:when test="${!navForm.enableIndice}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/intestazioneAnaliticaPolo.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/intestazioneAnaliticaIndice.jsp" />
				</c:otherwise>
			</c:choose>
			<br>
			<sbn:tree root="treeElementViewSoggetti" divClass="analitica"
				visualCheck="${navForm.visualCheckCattura}"
				propertyRadio="nodoSelezionato" propertyCheck="checkSelezionato"
				imagesPath="/sbn/images/tree/" enableSubmit="true"
				enabled="${!navForm.enableConferma}" />
			<br>
			<!-- Titoli collegati -->
			<table width="100%" border="0">
				<tr>
					<td align="left" class="etichetta" scope="col">
						<bean:message key="analitica.inserito"
							bundle="gestioneSemanticaLabels" />
					</td>
					<td class="etichetta">
						<bean:message key="analitica.il" bundle="gestioneSemanticaLabels" />
					</td>
					<td>
						<html:text styleId="testoNormale"
							property="treeElementViewSoggetti.areaDatiDettaglioOggettiVO.dettaglioSoggettoGeneraleVO.dataIns"
							size="14" maxlength="20" readonly="true"></html:text>
					</td>
					<td class="etichetta">
						<bean:message key="analitica.modificato"
							bundle="gestioneSemanticaLabels" />
					</td>
					<td class="etichetta">
						<bean:message key="analitica.il" bundle="gestioneSemanticaLabels" />
					</td>
					<td>
						<html:text styleId="testoNormale"
							property="treeElementViewSoggetti.areaDatiDettaglioOggettiVO.dettaglioSoggettoGeneraleVO.dataAgg"
							size="14" maxlength="20" readonly="true"></html:text>
					</td>
				</tr>
			</table>
			<table width="100%" border="0">
				<c:choose>
					<c:when test="${!navForm.enableIndice}">
						<tr>
							<td align="center" class="etichetta" scope="col">
								<bean:message key="analitica.titoli"
									bundle="gestioneSemanticaLabels" />
							</td>

							<td align="center" class="etichetta" scope="col">
								<bean:message key="analitica.polo"
									bundle="gestioneSemanticaLabels" />
							</td>

							<td>
								<html:text styleId="testoNormale" property="treeElementViewSoggetti.areaDatiDettaglioOggettiVO.dettaglioSoggettoGeneraleVO.numTitoliPolo"
									size="10" readonly="true"></html:text>
							</td>

							<td align="center" class="etichetta" scope="col">
								<bean:message key="analitica.biblio"
									bundle="gestioneSemanticaLabels" />
							</td>

							<td>
								<html:text styleId="testoNormale" property="treeElementViewSoggetti.areaDatiDettaglioOggettiVO.dettaglioSoggettoGeneraleVO.numTitoliBiblio"
									size="10" readonly="true"></html:text>
							</td>
						</tr>
					</c:when>
					<c:otherwise>

					</c:otherwise>
				</c:choose>
			</table>

		</div>
		<div id="divFooter">
			<c:choose>
				<c:when test="${navForm.enableConferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionesemantica/soggetto/bottonieraAnalitica.jsp" />
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>
