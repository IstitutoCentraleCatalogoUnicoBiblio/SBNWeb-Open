<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<html:xhtml />
<layout:page>

<sbn:navform action="/acquisizioni/ordini/operazioneSuOrdine.do">

  <div id="divForm">
  	<div id="divMessaggio">
		<div align="center" class="messaggioInfo"><sbn:errors bundle="acquisizioniMessages" /></div>
	</div>
	<br><br>
 	<table width="100%"   cellpadding="0" cellspacing="0" border="0" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">

       <tr class="etichetta" bgcolor="#dde8f0">
          <td colspan="2" width="20%" class="etichetta" align="center">
          <bean:message  key="ordine.label.azione" bundle="acquisizioniLabels" />
          </td>
        </tr>


		<logic:iterate id="tipologieOperazionenuovo" property="tipoOperazionenuovo" name="navForm">
		   <c:set var="color" >
				<c:choose>
			        <c:when test='${color == "#FFCC99"}'>
			            #FEF1E2
			        </c:when>
			        <c:otherwise>
						#FFCC99
			        </c:otherwise>
			    </c:choose>
		    </c:set>

	       <tr class="testoNormale" bgcolor="${color}">
    	   <td class="testoNormale" align="left">
				<bean-struts:define id="operazioneValuenuovo">
					<bean-struts:write name="tipologieOperazionenuovo" property="codice" />
				</bean-struts:define>
				<bean-struts:define id="operazioneValuenuovodes">
					<bean-struts:write name="tipologieOperazionenuovo" property="descrizione"  />
				</bean-struts:define>

				<bean:message  key="${operazioneValuenuovo}" bundle="acquisizioniLabels" />
	        </td>
			<td width="5%" >
				<sbn:disableAll disabled="${navForm.conferma}">
					<html:radio property="selectedTipoOperazione" value="${operazioneValuenuovo}" />
				</sbn:disableAll>
			</td>
 			</tr>

		</logic:iterate>

     </table>

		<c:choose>
			<c:when test="${navForm.conferma}">
				<table align="center">
					<tr>
						<td><html:submit styleClass="pulsanti" property="methodOperazioneSuOrdine">
								<bean:message key="acquisizioni.bottone.si" bundle="acquisizioniLabels" /></html:submit>
							<html:submit styleClass="pulsanti" property="methodOperazioneSuOrdine">
								<bean:message key="acquisizioni.bottone.no" bundle="acquisizioniLabels" /></html:submit></td>
					</tr>
				</table>

<!--				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />-->
			</c:when>
			<c:otherwise>
		      <table align="center"  border="0" style="height:40px" cellspacing="0"; cellpadding="0">
		          <tr>
		           <td >
					<c:choose>
						<c:when test="${!navForm.disabilitaConferma}">
							<html:submit styleClass="pulsanti" property="methodOperazioneSuOrdine">
								<bean:message key="button.ok" bundle="acquisizioniLabels" />
							</html:submit>
						</c:when>
					</c:choose>
					<html:submit styleClass="pulsanti" property="methodOperazioneSuOrdine">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>

		<!--
					<html:submit styleClass="pulsanti" property="indietro9">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>
		 -->
		           </td>
			 	</tr>
		    	</table>
    		</c:otherwise>
		</c:choose>


	</div>
	</sbn:navform>
</layout:page>
