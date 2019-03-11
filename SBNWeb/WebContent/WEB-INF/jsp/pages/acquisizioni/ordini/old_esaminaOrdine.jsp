<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/ordini/esaminaOrdine.do">

  <div id="divForm">
	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineTabA.jsp" />

      <table border="0" width="100%">
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineGenTop.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineSezione.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineFornitore.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineValuta.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineBilancio.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineNoteOrd.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineNoteForn.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineTipoInvio.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineAbbonamento.jsp" />
      </table>
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineGenBottom.jsp" />

	</div>

	</sbn:navform>
</layout:page>
