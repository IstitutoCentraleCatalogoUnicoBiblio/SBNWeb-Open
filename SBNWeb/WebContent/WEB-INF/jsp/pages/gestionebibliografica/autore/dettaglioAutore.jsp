<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Autore
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

	<sbn:navform action="/gestionebibliografica/autore/dettaglioAutore.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" />
		</div>
		<c:choose>
			<c:when test="${dettaglioAutoreForm.tipoProspettazione eq 'DET'}">
				<c:choose>
					<c:when
						test="${dettaglioAutoreForm.dettAutoreVO.tipoLegame eq 'TI_AU'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioDatiLegameTitoloAut.jsp" />
						<hr color="#dde8f0" />
					</c:when>
					<c:when
						test="${dettaglioAutoreForm.dettAutoreVO.tipoLegame eq 'AU_AU'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/gestionebibliografica/autore/dettaglioDatiLegameAutoreAut.jsp" />
						<hr color="#dde8f0" />
					</c:when>
					<c:when
						test="${dettaglioAutoreForm.dettAutoreVO.tipoLegame eq 'MA_AU'}">
						<jsp:include
							page="/WEB-INF/jsp/subpages/gestionebibliografica/marca/dettaglioDatiLegameMarcaAut.jsp" />
						<hr color="#dde8f0" />
					</c:when>
				</c:choose>
			</c:when>
		</c:choose> <jsp:include
			page="/WEB-INF/jsp/subpages/gestionebibliografica/autore/dettaglioAutoreDati.jsp" />
		</div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<c:choose>
					<c:when test="${dettaglioAutoreForm.tipoProspettazione ne 'DET'}">
						<td align="center"><html:submit property="methodDettaglioAut">
							<bean:message key="button.ok"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${dettaglioAutoreForm.aggiornaFlagCondiviso eq 'SI'}">
								<td align="center"><html:submit property="methodDettaglioAut">
									<bean:message key="button.ok"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
				<td align="center"><html:submit property="methodDettaglioAut">
					<bean:message key="button.annulla"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>
