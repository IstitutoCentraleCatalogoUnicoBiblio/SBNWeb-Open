<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/utenti/RicercaUtenti">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<c:choose>
				<c:when test="${navForm.ricerca.ricercaUtentePolo}">
					<jsp:include page="/WEB-INF/jsp/subpages/servizi/utenti/ricercaUtentiPolo.jsp" />
				</c:when>
				<c:otherwise>
					<sbn:checkAttivita idControllo="CAMBIO_BIB">
						<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/listaBiblioteche.jsp" flush="true" />
						<br/>
					</sbn:checkAttivita>
					<jsp:include page="/WEB-INF/jsp/subpages/servizi/utenti/ricercaUtentiBiblio.jsp" flush="true" />
				</c:otherwise>
			</c:choose>
		</div>
		<br/>


		<c:if test="${not navForm.conferma}">
			<jsp:include page="/WEB-INF/jsp/subpages/servizi/utenti/ricercaUtentiFooter.jsp" />
		</c:if>
		<c:if test="${navForm.conferma}">
			<div id="divFooter">
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
			</div>
		</c:if>

	</sbn:navform>
</layout:page>
