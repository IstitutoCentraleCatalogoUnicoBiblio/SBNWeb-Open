<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<layout:page>
	<sbn:navform
		action="/gestionesemantica/catalogazionesemantica/CatalogazioneSemantica.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!-- INIZIO PAGINA --> <jsp:include
			page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
		<br>
		<jsp:include
			page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/folderCatalogazione.jsp" />

		<c:choose>
			<c:when
				test="${CatalogazioneSemanticaForm.catalogazioneSemanticaComune.folder eq 'FOLDER_SOGGETTI'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/catSemanticaSoggetti.jsp" />
			</c:when>
			<c:when
				test="${CatalogazioneSemanticaForm.catalogazioneSemanticaComune.folder eq 'FOLDER_CLASSI'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/catSemanticaClassi.jsp" />
			</c:when>
			<c:when
				test="${CatalogazioneSemanticaForm.catalogazioneSemanticaComune.folder eq 'FOLDER_THESAURO'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/catSemanticaThesauro.jsp" />
			</c:when>
			<c:when
				test="${CatalogazioneSemanticaForm.catalogazioneSemanticaComune.folder eq 'FOLDER_ABSTRACT'}">
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/catSemanticaAbstract.jsp" />
			</c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose></div>
		<div id="divFooter"><c:choose>
			<c:when test="${CatalogazioneSemanticaForm.enableSoloEsamina}">

				<table align="center">
					<tr>
						<td align="center"><html:submit
							property="methodCatalogazione">
							<bean:message key="button.chiudi"
								bundle="gestioneSemanticaLabels" />
						</html:submit></td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<jsp:include
					page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/footerSoggettazione.jsp" />
			</c:otherwise>
		</c:choose></div>
		<!-- FINE PAGINA -->
	</sbn:navform>
</layout:page>
