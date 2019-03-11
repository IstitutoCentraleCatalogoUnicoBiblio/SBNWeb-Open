<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionestampe/ordini/stampaOrdini.do">
	<div id="divForm">
		<div id="divMessaggio"><sbn:errors />
	</div>
	<c:choose>
		<c:when test="${navForm.parametri.codAttivita eq 'AZ700'}">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/stampe/parametriShippingManifest.jsp" />
		</c:when>
		<c:otherwise>
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/stampe/parametriStampaOrdini.jsp" />
		</c:otherwise>
	</c:choose>
</div>

<div id="divFooter">

           <table align="center" border="0" style="height:40px" >
            <tr >
             <td>
				<html:submit styleClass="pulsanti" property="methodStampaOrdini">
					<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
				</html:submit>
			<html:submit styleClass="pulsanti" property="methodStampaOrdini">
				<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
			</html:submit>
			<!-- 26.07.10 disabilitazione bottone nascosto -->
			<!--
			<html:submit styleClass="buttonHide" property="methodStampaOrdini">
				<bean:message key="ricerca.button.calcoli" bundle="gestioneStampeLabels" />
			</html:submit>
			 -->
			 </td>
		  </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
