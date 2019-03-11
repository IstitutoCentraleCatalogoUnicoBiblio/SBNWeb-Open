<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/gestionebibliografica/titolo/analiticaTitolo.do">

		<div id="divForm">

		<div id="divMessaggio">
			<sbn:errors  />
			<c:if test="${!empty requestScope.SCHEDA_LINK}">
				<html:link page="/downloadBatch.do?FILEID=${requestScope.SCHEDA_LINK.base64Link}">${requestScope.SCHEDA_LINK.nomeFileVisualizzato}</html:link>
 			</c:if>
		</div>

		<table width="100%">
			<tr>
				<td width="650" class="etichetta" align="center">
					<c:choose>
						<c:when test="${analiticaTitoloForm.livRicerca eq 'I'}">
							<bean:message key="label.livRicercaIndice" bundle="gestioneBibliograficaLabels" />
							<c:if test="${analiticaTitoloForm.codiceBiblioSbn ne ''}">
								<html:text property="codiceBiblioSbn" size="30" readonly="true"></html:text>
							</c:if>
						</c:when>
						<c:otherwise>
							<bean:message key="label.livRicercaPolo" bundle="gestioneBibliograficaLabels" />
							<c:if test="${analiticaTitoloForm.codiceBiblioSbn ne ''}">
								<html:text property="codiceBiblioSbn" size="30" readonly="true"></html:text>
							</c:if>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${analiticaTitoloForm.visualVaiA ne 'SI'}">
							<c:choose>
								<c:when
									test="${analiticaTitoloForm.listaBidSelezPresent eq 'SI'}">
									<html:submit property="methodAnalitTitolo">
										<bean:message key="button.elemPrec"
											bundle="gestioneBibliograficaLabels" />
									</html:submit>
									<html:submit property="methodAnalitTitolo">
										<bean:message key="button.elemSucc"
											bundle="gestioneBibliograficaLabels" />
									</html:submit>
								</c:when>
							</c:choose>
							<c:choose>
								<c:when
									test="${analiticaTitoloForm.listaBidDaFilePresent eq 'SI'}">
									<html:submit property="methodAnalitTitolo">
										<bean:message key="button.elemSucc"
											bundle="gestioneBibliograficaLabels" />
									</html:submit>
								</c:when>
							</c:choose>

						</c:when>
					</c:choose>
				</td>
			</tr>
		</table>

		<!-- Visualizza reticolo del bid selezionato -->
		<sbn:tree root="treeElementViewTitoli" divClass="analitica"
			visualCheck="${analiticaTitoloForm.visualCheckCattura}"
			propertyRadio="radioItemSelez" propertyCheck="checkItemSelez"
			imagesPath="/sbn/images/tree/" enableSubmit="true" enabled="${analiticaTitoloForm.analiticaAttiva}" />
		<!-- Fine Visualizza reticolo del bid selezionato -->



		<c:choose>
			<c:when test="${analiticaTitoloForm.visualVaiA eq 'SI'}">
				<hr color="#dde8f0" />
				<jsp:include page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interVaiA.jsp" />
				<hr color="#dde8f0" />
			</c:when>
		</c:choose></div>
		<br />
		<div id="divFooter">

		<c:choose>
			<c:when test="${analiticaTitoloForm.visualVaiA eq 'SI'}">
				<table align="center">
					<tr>
					<!--  MANTIS 3372 26.11.2009 almaviva2 -
					il tasto analitica di Indice non si usa in caso di analitica per SIF (inserita ultima condizione dell'IF) -->
						<c:choose>
							<c:when test="${analiticaTitoloForm.livRicerca eq 'P'
									and analiticaTitoloForm.tipoAuthority ne 'PP'
									and analiticaTitoloForm.presenzaTastoAnaliticaDiIndice eq 'SI'}">
								<td align="center"><html:submit property="methodAnalitTitolo">
									<bean:message key="button.analiticaIndice"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
							</c:when>
							<c:when test="${analiticaTitoloForm.gestioneInferiori eq 'SI'}">
								<sbn:checkAttivita idControllo="INFERIORI">
									<td align="center"><html:submit property="methodAnalitTitolo">
										<bean:message key="button.selAllInfer"
											bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
									<td align="center"><html:submit property="methodAnalitTitolo">
										<bean:message key="button.deSelAllInfer"
											bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
								</sbn:checkAttivita>
							</c:when>
						</c:choose>
						<td align="center"><html:submit property="methodAnalitTitolo">
							<bean:message key="button.dettaglio"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
						<td align="center"><html:submit property="methodAnalitTitolo">
							<bean:message key="button.confermaVaiA"	bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
						<td align="center"><html:submit property="methodAnalitTitolo">
							<bean:message key="button.annullaVaiA"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
					</tr>
				</table>
			</c:when>
			<c:when test="${analiticaTitoloForm.visualVaiA eq 'NO'}">

				<c:choose>
					<c:when test="${analiticaTitoloForm.tipoOperazioneConferma eq 'SCAMBIOFORMA'}">
						<table align="center">
							<tr>
								<td align="center"><html:submit property="methodAnalitTitolo">
									<bean:message key="button.confermaScambioForma"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
								<td align="center"><html:submit property="methodAnalitTitolo">
									<bean:message key="button.annullaScambioForma"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
							</tr>
						</table>
					</c:when>
					<c:when test="${analiticaTitoloForm.tipoOperazioneConferma eq 'INVIOINDICE'}">
						<table align="center">
							<tr>
								<td align="center"><html:submit property="methodAnalitTitolo">
									<bean:message key="button.confermaInvioIndice"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
								<td align="center"><html:submit property="methodAnalitTitolo">
									<bean:message key="button.annullaInvioIndice"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
							</tr>
						</table>
					</c:when>

					<c:otherwise>
						<table align="center">
							<tr>
								<c:choose>
									<c:when test="${analiticaTitoloForm.livRicerca eq 'P' and analiticaTitoloForm.presenzaTastoCercaInIndice eq 'SI'
									and analiticaTitoloForm.tipoAuthority ne 'PP'}">
										<td align="center"><html:submit property="methodAnalitTitolo">
											<bean:message key="button.cercaIndice"
												bundle="gestioneBibliograficaLabels" />
										</html:submit></td>
									</c:when>
								</c:choose>

								<c:if test="${analiticaTitoloForm.provenienzaChiamatainSIF eq 'SERVIZI'}">
									<td align="center">
										<html:submit property="methodAnalitTitolo">
											<bean:message key="button.gestPerServizi.scegli" bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:if>

								<td align="center"><html:submit property="methodAnalitTitolo">
									<bean:message key="button.dettaglio"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
								<c:choose>
									<c:when test="${analiticaTitoloForm.presenzaTastoVaiA eq 'SI'}">
										<td align="center"><html:submit property="methodAnalitTitolo">
											<bean:message key="button.vaia"
												bundle="gestioneBibliograficaLabels" />
										</html:submit></td>
									</c:when>
								</c:choose>

								<!-- Inizio Modifica Mantis BUG 3718 almaviva2 27.07.2010 - esamina non deve essere presente per ACQUISIZIONI-->
								<!-- Ulteriore Modifica almaviva2 29.07.2010 - BUG MANTIS 3856 inserito controllo in AND per voce SERVIZI come per sopra-->

								<c:if test="${analiticaTitoloForm.provenienzaChiamatainSIF ne 'ACQUISIZIONI' and analiticaTitoloForm.provenienzaChiamatainSIF ne 'SERVIZI'}">
									<td class="etichetta">
										<bean:message key="label.esamina" bundle="gestioneBibliograficaLabels" />
										<html:select property="esaminaTitSelez">
										<html:optionsCollection property="listaEsaminaTit"
											value="descrizione" label="descrizione" />
										</html:select></td>
									<td align="center"><html:submit property="methodAnalitTitolo">
										<bean:message key="button.esamina"
											bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
								</c:if>

								<!--
								<td class="etichetta">
								<bean:message key="label.esamina" bundle="gestioneBibliograficaLabels" />
								<html:select property="esaminaTitSelez">
									<html:optionsCollection property="listaEsaminaTit"
										value="descrizione" label="descrizione" />
								</html:select></td>
								<td align="center"><html:submit property="methodAnalitTitolo">
									<bean:message key="button.esamina"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
								-->

								<!-- Fine Modifica Mantis BUG 3718 almaviva2 27.07.2010 - esamina non deve essere presente -->



							</tr>
						</table>
					</c:otherwise>
				</c:choose>
			</c:when>
		</c:choose></div>
	</sbn:navform>

</layout:page>
