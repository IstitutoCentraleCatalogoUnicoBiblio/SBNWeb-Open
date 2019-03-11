<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/configurazione/Configurazione.do">



		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<sbn:disableAll disabled="${navForm.conferma}">
			<br/>
			<jsp:include
				page="/WEB-INF/jsp/subpages/servizi/utility/listaBiblioteche.jsp" />
			<br/>
			<jsp:include
				page="/WEB-INF/jsp/subpages/servizi/configurazione/folderConfigurazione.jsp" />
			<br/>

			<br/>

			<c:choose>
				<c:when test="${navForm.folder eq 'T'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/configurazione/gestioneTipiServizio.jsp" />
				</c:when>

				<c:when test="${navForm.folder eq 'E'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/configurazione/gestioneModalitaErogazione.jsp" />
				</c:when>

				<c:when test="${navForm.folder eq 'S'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/configurazione/gestioneSupporti.jsp" />
				</c:when>

				<c:when test="${navForm.folder eq 'P'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/configurazione/gestioneParametri.jsp" />
				</c:when>

				<c:when test="${navForm.folder eq 'M'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/configurazione/gestioneModalitaPagamento.jsp" />
				</c:when>
			</c:choose>
		 </sbn:disableAll>
		</div>
		<br/>

		<br/>
		<div id="divFooter">
			<c:choose>
				<c:when test="${navForm.folder eq 'T'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/configurazione/footerConfigurazione_TipiServizio.jsp" />
				</c:when>
				<c:when test="${navForm.folder eq 'E'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/configurazione/footerConfigurazione_ModalitaErogazione.jsp" />
				</c:when>
				<c:when test="${navForm.folder eq 'S'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/configurazione/footerConfigurazione_Supporti.jsp" />
				</c:when>
				<c:when test="${navForm.folder eq 'P'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/configurazione/footerConfigurazione_ParametriBiblioteca.jsp" />
				</c:when>
				<c:when test="${navForm.folder eq 'M'}">
					<jsp:include
						page="/WEB-INF/jsp/subpages/servizi/configurazione/footerConfigurazione_ModalitaPagamento.jsp" />
				</c:when>
			</c:choose>
		</div>


	</sbn:navform>
</layout:page>
