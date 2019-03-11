<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/utenti/DettaglioUtentiAna.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<div style="width: 100%">
				<STRONG><bean:message key="servizi.utenti.biblioteca" bundle="serviziLabels" /></STRONG>&nbsp;
				<html:text styleId="testoNoBold" property="biblioteca" size="5"	maxlength="3" disabled="true" />
			</div>
			<br>
			<jsp:include page="/WEB-INF/jsp/subpages/servizi/utenti/folderUtente.jsp" />
			<jsp:include page="/WEB-INF/jsp/subpages/servizi/utenti/datiUtente.jsp" />
			<c:choose>
				<c:when test="${navForm.tipoUtente ne ''}">
					<sbn:disableAll disabled="${navForm.conferma}">
						<c:choose>
							<c:when test="${navForm.folder eq 'A '}">
								<jsp:include page="/WEB-INF/jsp/subpages/servizi/utenti/utentiAna.jsp" />
							</c:when>
							<c:when test="${navForm.folder eq 'U '}">
								<jsp:include page="/WEB-INF/jsp/subpages/servizi/utenti/utentiAut.jsp" />
							</c:when>
							<c:when test="${navForm.folder eq 'UI'}">
								<jsp:include page="/WEB-INF/jsp/subpages/servizi/utenti/utentiAut.jsp" />
							</c:when>
							<c:when test="${navForm.folder eq 'UC'}">
								<jsp:include page="/WEB-INF/jsp/subpages/servizi/utenti/utentiAut.jsp" />
							</c:when>
							<c:when test="${navForm.folder eq 'B '}">
								<jsp:include page="/WEB-INF/jsp/subpages/servizi/utenti/utentiBiPo.jsp" />
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
					</sbn:disableAll>
				</c:when>
			</c:choose>

		</div>
		<c:choose>
			<c:when test="${navForm.tipoUtente ne ''}">
				<div id="divFooter">
					<c:choose>
						<c:when test="${navForm.conferma}">
							<jsp:include
								page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp" />
						</c:when>
						<c:otherwise>
								<jsp:include page="/WEB-INF/jsp/subpages/servizi/utenti/footerDettUtenti.jsp" />
						</c:otherwise>
					</c:choose>
				</div>
			</c:when>
		</c:choose>
	</sbn:navform>
</layout:page>
