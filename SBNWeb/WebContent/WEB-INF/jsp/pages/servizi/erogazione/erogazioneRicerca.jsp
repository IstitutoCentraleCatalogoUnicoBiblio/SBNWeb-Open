<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/erogazione/ErogazioneRicerca.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>
			<jsp:include
				page="/WEB-INF/jsp/subpages/servizi/utility/listaBiblioteche.jsp" />
			<br>
			<jsp:include
				page="/WEB-INF/jsp/subpages/servizi/erogazione/folderErogazioneRicerca.jsp" />
			<br>
			<c:choose>
				<c:when test="${ErogazioneRicercaForm.folder eq 'S'}">
					<jsp:include	page="/WEB-INF/jsp/subpages/servizi/erogazione/erogazioneRicercaUteDoc.jsp" />
				</c:when>
				<c:when test="${ErogazioneRicercaForm.folder eq 'L'}">
					<jsp:include	page="/WEB-INF/jsp/subpages/servizi/erogazione/erogazioneListe.jsp" />
				</c:when>
				<c:when test="${ErogazioneRicercaForm.folder eq 'P'}">
					<jsp:include	page="/WEB-INF/jsp/subpages/servizi/erogazione/erogazionePrenotazioni.jsp" />
				</c:when>
				<c:when test="${ErogazioneRicercaForm.folder eq 'PG'}">
					<jsp:include	page="/WEB-INF/jsp/subpages/servizi/erogazione/erogazioneProroghe.jsp" />
				</c:when>
				<c:when test="${ErogazioneRicercaForm.folder eq 'G'}">
					<jsp:include	page="/WEB-INF/jsp/subpages/servizi/erogazione/erogazioneGiacenze.jsp" />
				</c:when>
				<c:when test="${ErogazioneRicercaForm.folder eq 'SL'}">
					<jsp:include	page="/WEB-INF/jsp/subpages/servizi/erogazione/erogazioneSolleciti.jsp" />
				</c:when>
				<c:when test="${ErogazioneRicercaForm.folder eq 'L2'}">
					<jsp:include	page="/WEB-INF/jsp/subpages/servizi/erogazione/erogazioneListe2.jsp" />
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
		</div>
		<br/>

		<c:if test="${not ErogazioneRicercaForm.folder eq 'P'}">
			<div id="divFooterComon">
				<table width="80%" border="0" style="height:40px">
					<tr>
						<td width="15%" class="etichetta">
							<bean:message key="servizi.label.elementiPerBlocco" bundle="serviziLabels" />
						</td>
						<td width="20%" class="testoNormale">
							<html:text styleId="testoNoBold" property="anaMov.elemPerBlocchi" size="5" disabled="false"></html:text>
						</td>
						<td width="15%" class="etichetta">
							<bean:message key="servizi.label.ordinamento" bundle="serviziLabels" />
						</td>
						<td width="20%" class="testoNormale">
							<html:select property="anaMov.tipoOrdinamento" style="width:70px">
								<html:optionsCollection property="anaMov.lstTipiOrdinamento" value="codice" label="descrizione" />
							</html:select>
						</td>
					</tr>
				</table>
			</div>
			<br/>
		</c:if>


		<div id="divFooter">
			<c:choose>
				<c:when test="${ErogazioneRicercaForm.conferma}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${ErogazioneRicercaForm.folder eq 'S'}">
							<jsp:include
								page="/WEB-INF/jsp/subpages/servizi/erogazione/footerErogazioneRicerca.jsp" />
						</c:when>
						<c:when test="${ErogazioneRicercaForm.folder eq 'L'}">
							<jsp:include
								page="/WEB-INF/jsp/subpages/servizi/erogazione/footerErogazioneRicercaListe.jsp" />
						</c:when>
						<c:when test="${ErogazioneRicercaForm.folder eq 'P'}">
							<jsp:include
								page="/WEB-INF/jsp/subpages/servizi/erogazione/footerErogazionePrenotazioni.jsp" />
						</c:when>
						<c:when test="${ErogazioneRicercaForm.folder eq 'PG'}">
							<jsp:include
								page="/WEB-INF/jsp/subpages/servizi/erogazione/footerErogazioneProroghe.jsp" />
						</c:when>
						<c:when test="${ErogazioneRicercaForm.folder eq 'G'}">
							<jsp:include
								page="/WEB-INF/jsp/subpages/servizi/erogazione/footerErogazioneGiacenze.jsp" />
						</c:when>
						<c:when test="${ErogazioneRicercaForm.folder eq 'SL'}">
							<jsp:include
								page="/WEB-INF/jsp/subpages/servizi/erogazione/footerErogazioneSolleciti.jsp" />
						</c:when>
						<c:when test="${ErogazioneRicercaForm.folder eq 'L2'}">
							<jsp:include
								page="/WEB-INF/jsp/subpages/servizi/erogazione/footerErogazioneRicercaListe2.jsp" />
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>
