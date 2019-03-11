<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<div style="text-align:center;">
	<div>
		<c:choose>
			<c:when test="${DettaglioTipoServizioForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/confermaOperazione.jsp"></jsp:include>
			</c:when>
			<c:otherwise>
				<sbn:checkAttivita idControllo="GESTIONE">
				<html:submit property="methodDettaglioTipoServizio" >
					<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
				</html:submit>
				</sbn:checkAttivita>
				<html:submit property="methodDettaglioTipoServizio" >
					<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
				</html:submit>
			</c:otherwise>
		</c:choose>
	</div>
</div>