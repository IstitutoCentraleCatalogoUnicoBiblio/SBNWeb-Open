<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>


<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionestampe/periodici/stampaListaFascicoli.do"
		enctype="multipart/form-data">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
			<!-- tabella corpo COLONNA + LARGA -->
			<table width="100%" align="center">
				<tr>
					<td valign="top" scope="col" align="left">
						<div class="etichetta">
					<bean:message key="periodici.label.stampa"
							bundle="gestioneStampeLabels" /></div></td>
					<td valign="top" scope="col" align="left" colspan="5"><html:select
							styleClass="testoNormale" property="statoFascicolo">
							<html:optionsCollection property="listaStatoFascicolo"
								value="codice" label="descrizione" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td valign="top" scope="col" align="left">
						<div class="etichetta">
							<bean:message key="periodici.label.annoIniziale"
								bundle="gestioneStampeLabels" />
						</div></td>
					<td valign="top" scope="col" align="left"><html:text
							styleId="testoNormale" property="annoIniziale" size="10"
							maxlength="4"></html:text>
					</td>
					<td valign="top" scope="col" align="left">
						<div class="etichetta">
							<bean:message key="periodici.label.annoFinale"
								bundle="gestioneStampeLabels" />
						</div></td>
					<td valign="top" scope="col" align="left"><html:text
							styleId="testoNormale" property="annoFinale" size="10"
							maxlength="4"></html:text>
					</td>
				</tr>
			</table>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr height="30">
					<c:choose>
						<c:when test="${stampaListaFascicoliForm.folder eq 'Fornitore'}">
							<td width="50%" class="schedaOn">
								<div align="center">Fornitore</div>
							</td>
						</c:when>
						<c:otherwise>
							<td width="50%" class="schedaOff">
								<div align="center">
									<html:submit property="methodStampaFascicoli"
										styleClass="sintButtonLinkDefault">
										<bean:message key="documentofisico.selFornitore"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</div>
							</td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${stampaListaFascicoliForm.folder eq 'Ordine'}">
							<td width="50%" class="schedaOn">
								<div align="center">Ordine</div>
							</td>
						</c:when>
						<c:otherwise>
							<td width="50%" class="schedaOff">
								<div align="center">
									<html:submit property="methodStampaFascicoli"
										styleClass="sintButtonLinkDefault">
										<bean:message key="documentofisico.selOrdine"
											bundle="documentoFisicoLabels" />
									</html:submit>
								</div>
							</td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
			<c:choose>
				<c:when test="${stampaListaFascicoliForm.folder eq 'Fornitore'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/common/documentofisico/selFornitore.jsp" />
				</c:when>
				<c:when test="${stampaListaFascicoliForm.folder eq 'Ordine'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/common/documentofisico/selOrdine.jsp" />
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
			<HR>
			<table width="100%" align="center">
				<tr>
					<td valign="top" scope="col" align="left">
						<div class="etichetta">
							<bean:message key="periodici.label.ordinamento"
								bundle="gestioneStampeLabels" />
						</div>
					</td>
					<td valign="top" scope="col" align="left"><html:select
							styleClass="testoNormale" property="ordinamento">
							<html:optionsCollection property="listaOrdinamento"
								value="codice" label="descrizione" />
						</html:select></td>
					<td valign="top" scope="col" align="left">
						<div class="etichetta">
							<bean:message key="periodici.label.stampaNote"
								bundle="gestioneStampeLabels" />
						</div>
					</td>
					<td valign="top" scope="col" align="left"><html:select
							styleClass="testoNormale" property="stampaNote">
							<html:optionsCollection property="listaStampaNote" value="codice"
								label="descrizione" />
						</html:select></td>
				</tr>
			</table>
			<!-- FINE tabella corpo COLONNA + LARGA -->
			<HR>
			<jsp:include flush="true" page="../common/tipoStampa.jsp" />
			</div>
		<div id="divFooter">
			<table align="center" border="0" style="height: 40px">
				<tr>
					<td><html:submit styleClass="pulsanti"
							property="methodStampaFascicoli">
							<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
						</html:submit> <html:submit styleClass="pulsanti"
							property="methodStampaFascicoli">
							<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
						</html:submit></td>
				</tr>
			</table>

		</div>
	</sbn:navform>
</layout:page>
