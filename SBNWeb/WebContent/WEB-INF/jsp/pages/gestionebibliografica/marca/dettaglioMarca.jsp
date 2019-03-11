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

<html:xhtml />
<layout:page>

	<sbn:navform action="/gestionebibliografica/marca/dettaglioMarca.do"
		enctype="multipart/form-data">

		<div id="divForm">

			<div id="divMessaggio">
				<sbn:errors bundle="gestioneBibliograficaMessages" />
			</div>
			<c:choose>
				<c:when test="${dettaglioMarcaForm.tipoProspettazione eq 'DET'}">
					<c:choose>
						<c:when
							test="${dettaglioMarcaForm.dettMarcaVO.tipoLegame eq 'AU_MA'}">
							<jsp:include
								page="/WEB-INF/jsp/subpages/gestionebibliografica/autore/dettaglioDatiLegameAutoreMar.jsp" />
							<hr color="#dde8f0" />
						</c:when>
						<c:when
							test="${dettaglioMarcaForm.dettMarcaVO.tipoLegame eq 'TI_MA'}">
							<jsp:include
								page="/WEB-INF/jsp/subpages/gestionebibliografica/titolo/dettaglioDatiLegameTitoloMar.jsp" />
							<hr color="#dde8f0" />
						</c:when>
					</c:choose>
				</c:when>
			</c:choose>

			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionebibliografica/marca/dettaglioMarcaDati.jsp" />
			<br>
			<logic:notEmpty name="dettaglioMarcaForm"
				property="dettMarcaVO.listaImmagini">
				<table border="1">
					<tr>
						<logic:iterate id="img" name="dettaglioMarcaForm" indexId="idx"
							property="dettMarcaVO.listaImmagini">
							<c:set var="imgkey">${dettaglioMarcaForm.dettMarcaVO.mid}-${idx}</c:set>
							<td>
								<html:img
									page="/caricaImmagineMarca.do?IMGKEY=${imgkey}"
									width="100" height="100"></html:img>
									<c:choose>
										<c:when test="${dettaglioMarcaForm.tipoProspettazione ne 'DET'}">
											<html:multibox property="selezCheck" value="${idx}" ></html:multibox>
										</c:when>
									</c:choose>
							</td>
						</logic:iterate>
					</tr>
				</table>
					<c:choose>
						<c:when test="${dettaglioMarcaForm.tipoProspettazione ne 'DET'}">
							<html:submit property="methodDettaglioMar"><bean:message key="button.cancelImmagine" bundle="gestioneBibliograficaLabels" />
							</html:submit>
						</c:when>
					</c:choose>
			</logic:notEmpty>
		</div>

		<div id="divFooter">
			<table align="center">
				<tr>
					<c:choose>
						<c:when test="${dettaglioMarcaForm.tipoProspettazione ne 'DET'}">
							<td align="center">
								<html:submit property="methodDettaglioMar">
									<bean:message key="button.ok"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</c:when>
					</c:choose>
					<td align="center">
						<html:submit property="methodDettaglioMar">
							<bean:message key="button.annulla"
								bundle="gestioneBibliograficaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>

	</sbn:navform>
</layout:page>
