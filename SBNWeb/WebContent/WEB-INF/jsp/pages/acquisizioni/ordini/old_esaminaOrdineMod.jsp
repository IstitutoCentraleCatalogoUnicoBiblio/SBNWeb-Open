<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="acquisizioniMessages" />
	<sbn:navform action="/acquisizioni/ordini/esaminaOrdineMod.do">
  	<div id="divForm">
  	<div id="divMessaggio">
		<div align="center" class="messaggioInfo">&nbsp;</div>
	</div>
  		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineModTabA.jsp" />

      <table  width="100%"  border="0">
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineGenTop.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineSezione.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineFornitore.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineValuta.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineBilancio.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineNoteOrd.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineNoteForn.jsp" />
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineTipoInvio.jsp" />

<!-- GESTIONE DELLA ABILITAZIONE CAMPI DI ABBONAMENTO -->

<logic:equal name="esaminaOrdineModForm" property="esaminaOrdGen.natura" value="S">
		<jsp:include   page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineAbbonamento.jsp" />
</logic:equal>
<logic:notEqual name="esaminaOrdineModForm" property="esaminaOrdGen.natura" value="S">
		<jsp:include   page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineAbbonamentoDisabled.jsp" />
</logic:notEqual>

      </table>

           <!-- tabella bottoni -->
        <table align="center"  border="0" style="height:40px;" cellspacing="0"; cellpadding="0">
		<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineModGenBottom.jsp" />
        <tr>
        <td >
		<logic:equal name="esaminaOrdineModForm" property="enableScorrimento" value="true">
					<html:submit styleClass="pulsanti" property="scorriAvanti0">
						<bean:message key="ricerca.button.scorriAvanti" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="scorriIndietro0">
						<bean:message key="ricerca.button.scorriIndietro" bundle="acquisizioniLabels" />
					</html:submit>
		</logic:equal>
		</td>
		</tr>
      	</table>
	  			<!-- fine tabella bottoni -->

</div>
	</sbn:navform>
</layout:page>
