<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Tipo materiale Moderno/Antico
		almaviva2 - Inizio Codifica Agosto 2006
-->

<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml/>
<layout:page>



<c:choose>
	<c:when test="${interrogazioneTitoloForm.presenzaLoadFile eq 'SI'}">
		<sbn:navform action="/gestionebibliografica/titolo/interrogazioneTitolo.do" enctype="multipart/form-data">

			<div id="divForm">

				<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" />
				</div>

				<c:choose>
					<c:when test="${interrogazioneTitoloForm.provenienza eq 'NEWLEGAME'}">
						<table border="0">
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.titoloRiferimento"
									bundle="gestioneBibliograficaLabels" />:</td>
								<td width="20" class="testoNormale"><html:text
									property="areaDatiLegameTitoloVO.bidPartenza" size="10" readonly="true"
									></html:text></td>
								<td width="150" class="etichetta"><html:text
									property="areaDatiLegameTitoloVO.descPartenza" size="50" readonly="true"
									></html:text></td>
							</tr>
						</table>
						<hr color="#dde8f0" />
					</c:when>
					<c:when test="${interrogazioneTitoloForm.provenienza eq 'INTERFILTRATA'}">
						<table border="0">
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.titoloRiferimento"
									bundle="gestioneBibliograficaLabels" />:</td>
								<td width="20" class="testoNormale"><html:text
									property="xidDiRicerca" size="10" readonly="true"
									></html:text></td>
								<td width="150" class="etichetta"><html:text
									property="xidDiRicercaDesc" size="50" readonly="true"
									></html:text></td>
							</tr>
						</table>
						<hr color="#dde8f0" />
					</c:when>

				 </c:choose>

				 <jsp:include
					page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitCanaliFiltri.jsp" />


				<c:choose>
					<c:when test="${interrogazioneTitoloForm.provenienza ne 'NEWLEGAME'}">
						<!--
						Dati e tasti per richiesta lettura da file per catalogazione in Indice di Elementi solo locali
						 -->
						<hr color="#dde8f0" />
						<table border="0">
							<tr>
								<td class="etichetta">Carica file:&nbsp; <html:file property="uploadImmagine" />
								</td>
								<td class="etichetta">
									<html:submit property="methodInterrogTit">
										<bean:message key="button.caricaFileIdCatalLocale" bundle="gestioneBibliograficaLabels" />
									</html:submit>
								</td>
							</tr>
						</table>
					</c:when>
				 </c:choose>

				<!--	Segue la parte relativa ai dati specifici dei tipi materiale diversi da Documento (Musica/Grafica/Cartografia) -->
				<!--	almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro -->
		       	<c:choose>
					<c:when
						test="${interrogazioneTitoloForm.interrGener.tipoMateriale eq 'C'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriCartografia.jsp" />
					</c:when>
					<c:when
						test="${interrogazioneTitoloForm.interrGener.tipoMateriale eq 'G'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriGrafica.jsp" />
					</c:when>
					<c:when
						test="${interrogazioneTitoloForm.interrGener.tipoMateriale eq 'U'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriMusica.jsp" />
					</c:when>
					<c:when
						test="${interrogazioneTitoloForm.interrGener.tipoMateriale eq 'H'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriAudiovisivo.jsp" />
					</c:when>
					<c:when
						test="${interrogazioneTitoloForm.interrGener.tipoMateriale eq 'L'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriElettronico.jsp" />
					</c:when>
				</c:choose>
			</div>

			<div id="divFooterCommon"><jsp:include
				page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitParametriGen.jsp" />
			</div>


			<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center"><html:submit property="methodInterrogTit">
						<bean:message key="ricerca.button.cerca" bundle="gestioneBibliograficaLabels" />
					</html:submit></td>

					<c:choose>
						<c:when test="${interrogazioneTitoloForm.provenienza eq 'SERVIZI'}">

							<td align="center"><html:submit property="methodInterrogTit">
								<bean:message key="button.cercaPerServiziILL" bundle="gestioneBibliograficaLabels" />
							</html:submit></td>

							<td align="center"><html:submit property="methodInterrogTit">
								<bean:message key="button.annulla" bundle="gestioneBibliograficaLabels" />
							</html:submit></td>
						</c:when>
						<c:when test="${interrogazioneTitoloForm.provenienza eq 'ACQUISIZIONI'}">
							<td align="center"><html:submit property="methodInterrogTit">
								<bean:message key="button.annulla" bundle="gestioneBibliograficaLabels" />
							</html:submit></td>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${interrogazioneTitoloForm.livRicerca eq 'I' and interrogazioneTitoloForm.creaDoc eq 'SI'}">
									<td align="center"><html:submit property="methodInterrogTit">
												<bean:message key="button.creaTit"	bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
									<td align="center"><html:submit property="methodInterrogTit">
												<bean:message key="button.creaTitLoc" bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
								</c:when>
								<c:when test="${interrogazioneTitoloForm.livRicerca eq 'P' and interrogazioneTitoloForm.creaDocLoc eq 'SI'}">
									<td align="center"><html:submit property="methodInterrogTit">
										<bean:message key="button.creaTitLoc" bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
								</c:when>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>

		</div>
	</sbn:navform>
	</c:when>


	<c:otherwise>
		<sbn:navform action="/gestionebibliografica/titolo/interrogazioneTitolo.do">

			<div id="divForm">

			<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" />
			</div>

			<c:choose>
				<c:when test="${interrogazioneTitoloForm.provenienza eq 'NEWLEGAME'}">
					<table border="0">
						<tr>
							<td class="etichetta"><bean:message
								key="ricerca.titoloRiferimento"
								bundle="gestioneBibliograficaLabels" />:</td>
							<td width="20" class="testoNormale"><html:text
								property="areaDatiLegameTitoloVO.bidPartenza" size="10" readonly="true"
								></html:text></td>
							<td width="150" class="etichetta"><html:text
								property="areaDatiLegameTitoloVO.descPartenza" size="50" readonly="true"
								></html:text></td>
						</tr>
					</table>
					<hr color="#dde8f0" />
				</c:when>
				<c:when test="${interrogazioneTitoloForm.provenienza eq 'INTERFILTRATA'}">
					<table border="0">
						<tr>
							<td class="etichetta"><bean:message
								key="ricerca.titoloRiferimento"
								bundle="gestioneBibliograficaLabels" />:</td>
							<td width="20" class="testoNormale"><html:text
								property="xidDiRicerca" size="10" readonly="true"
								></html:text></td>
							<td width="150" class="etichetta"><html:text
								property="xidDiRicercaDesc" size="50" readonly="true"
								></html:text></td>
						</tr>
					</table>
					<hr color="#dde8f0" />
				</c:when>

			 </c:choose>

			 <jsp:include
				page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitCanaliFiltri.jsp" />

			<!--	Segue la parte relativa ai dati specifici dei tipi materiale diversi da Documento (Musica/Grafica/Cartografia) -->
			 <!--	almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro -->
	       	<c:choose>
				<c:when
					test="${interrogazioneTitoloForm.interrGener.tipoMateriale eq 'C'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriCartografia.jsp" />
				</c:when>
				<c:when
					test="${interrogazioneTitoloForm.interrGener.tipoMateriale eq 'G'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriGrafica.jsp" />
				</c:when>
				<c:when
					test="${interrogazioneTitoloForm.interrGener.tipoMateriale eq 'U'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriMusica.jsp" />
				</c:when>
				<c:when
					test="${interrogazioneTitoloForm.interrGener.tipoMateriale eq 'H'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriAudiovisivo.jsp" />
				</c:when>
				<c:when
					test="${interrogazioneTitoloForm.interrGener.tipoMateriale eq 'L'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitFiltriElettronico.jsp" />
				</c:when>
			</c:choose>
		</div>

		<div id="divFooterCommon"><jsp:include
			page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/interTitParametriGen.jsp" />
		</div>


		<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center"><html:submit property="methodInterrogTit">
						<bean:message key="ricerca.button.cerca" bundle="gestioneBibliograficaLabels" />
					</html:submit></td>

					<c:choose>
						<c:when test="${interrogazioneTitoloForm.provenienza eq 'SERVIZI'}">
							<td align="center"><html:submit property="methodInterrogTit">
								<bean:message key="button.cercaPerServiziILL" bundle="gestioneBibliograficaLabels" />
							</html:submit></td>
							<td align="center"><html:submit property="methodInterrogTit">
								<bean:message key="button.annulla" bundle="gestioneBibliograficaLabels" />
							</html:submit></td>
						</c:when>
						<c:when test="${interrogazioneTitoloForm.provenienza eq 'MOVIMENTI_UTENTE'}">
							<td align="center"><html:submit property="methodInterrogTit">
								<bean:message key="button.annulla" bundle="gestioneBibliograficaLabels" />
							</html:submit></td>
						</c:when>
						<c:when test="${interrogazioneTitoloForm.provenienza eq 'ACQUISIZIONI'}">
							<td align="center"><html:submit property="methodInterrogTit">
								<bean:message key="button.annulla" bundle="gestioneBibliograficaLabels" />
							</html:submit></td>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${interrogazioneTitoloForm.livRicerca eq 'I' and interrogazioneTitoloForm.creaDoc eq 'SI'}">
									<td align="center"><html:submit property="methodInterrogTit">
												<bean:message key="button.creaTit"	bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
									<td align="center"><html:submit property="methodInterrogTit">
												<bean:message key="button.creaTitLoc" bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
								</c:when>
								<c:when test="${interrogazioneTitoloForm.livRicerca eq 'P' and interrogazioneTitoloForm.creaDocLoc eq 'SI'}">
									<td align="center"><html:submit property="methodInterrogTit">
										<bean:message key="button.creaTitLoc" bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
								</c:when>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
		</div>

	</sbn:navform>

	</c:otherwise>
</c:choose>


</layout:page>
