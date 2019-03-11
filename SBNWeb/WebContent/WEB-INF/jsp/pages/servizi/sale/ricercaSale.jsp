<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/sale/ricercaSale.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>
			<sbn:checkAttivita idControllo="CAMBIO_BIBLIOTECA">
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/utility/listaBiblioteche.jsp" />
				<br/>
			</sbn:checkAttivita>

			<layout:folder bundle="serviziLabels" name="navForm" folders="folders"
			 	property="currentFolder" parameter="${navButtons}" />
			 <br/>
			<c:if test="${navForm.currentFolder eq 0}">
				<!-- PRENOTAZIONI -->
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/sale/folderPrenotazioniPosti.jsp" flush="true" />
			</c:if>

			<c:if test="${navForm.currentFolder eq 1}">
				<!-- RICERCA SALE -->
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/sale/folderRicercaSale.jsp" />
			</c:if>

			<c:if test="${navForm.currentFolder eq 2}">
				<!-- CATEGORIE MEDIAZIONE -->
				<jsp:include page="/WEB-INF/jsp/subpages/servizi/sale/folderCatMediazione.jsp" />
			</c:if>
		</div>
		<br>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td><sbn:bottoniera buttons="pulsanti" /></td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
