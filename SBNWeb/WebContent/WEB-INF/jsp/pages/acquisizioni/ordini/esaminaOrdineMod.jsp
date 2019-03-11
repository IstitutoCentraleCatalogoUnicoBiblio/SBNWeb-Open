<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bs:define id="noinput"  value="false"/>

<c:choose>
<c:when test="${esaminaOrdineModForm.ordineApertoAbilitaInput}">
	<bs:define id="noinput"  value="true"/>
</c:when>
</c:choose>


<c:choose>
<c:when test="${esaminaOrdineModForm.disabilitaTutto}">
	<bs:define id="noinput"  value="true"/>
</c:when>
</c:choose>





<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/ordini/esaminaOrdineMod.do">
  	<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>

	<!--
	<bs:define id="ordineAperto">
		<bs:write name="navForm" property="ordineApertoAbilitaInput" />
	</bs:define>
	-->
	<bs:define id="faseModifica" >
		<bs:write name="navForm" property="operazioneModifica" />
	</bs:define>
		<logic:equal  name="navForm" property="scegliTAB" value="A">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineModTabA.jsp" />
		      <table border="0"  width="100%" cellpadding="2" cellspacing="2">
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

		</logic:equal>
		<logic:equal  name="navForm" property="scegliTAB" value="L">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineModTabL.jsp" />
		      <table  width="100%"   border="0" cellpadding="2" cellspacing="2">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineGenTop.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineFornitore.jsp" />
				<tr>
		          <td class="etichetta" align="right" valign="top"><bean:message  key="ordine.label.regTrib" bundle="acquisizioniLabels" /></td>
		          <td valign="top" >
					<html:text styleId="testoNormale"   property="datiOrdine.regTribOrdine" size="40" maxlength="50"  readonly="${noinput}"></html:text>
		          </td>
		        </tr>
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineNoteOrd.jsp" />
				<jsp:include   page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineAbbonamento.jsp" />
		      </table>
		</logic:equal>

		<logic:equal  name="navForm" property="scegliTAB" value="D">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineModTabD.jsp" />
		      <table  width="100%"   border="0" cellpadding="2" cellspacing="2">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineGenTop.jsp" />
		        <tr>
			    <td class="etichetta" valign="top" >
			    	<bean:message  key="ordine.label.donatore" bundle="acquisizioniLabels" />
			    </td>
		        <td scope="col" align="left" valign="top" >
		 		  <html:text styleId="testoNormale" property="datiOrdine.fornitore.codice" size="5"  maxlength="10" readonly="${noinput}"></html:text>
		 		  <html:text styleId="testoNormale"   property="datiOrdine.fornitore.descrizione" size="30"  maxlength="50" readonly="${noinput}"></html:text>
	        	  <html:submit  styleClass="buttonImage" property="methodEsaminaOrdineMod" disabled="${noinput}">
				  <bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
				  </html:submit>
		          &nbsp;&nbsp;
		          <bean:message  key="ordine.label.continuativo" bundle="acquisizioniLabels" />
                  <html:checkbox   property="contin" onchange="this.form.submitDinamico.value='true'; this.form.submit();" disabled="${noinput}"></html:checkbox>
		        </td>
		        </tr>
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineNoteOrd.jsp" />
				<jsp:include   page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineAbbonamento.jsp" />
		      </table>

		</logic:equal>

		<logic:equal  name="navForm" property="scegliTAB" value="V">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineModTabV.jsp" />
		      <table  width="100%"   border="0" cellpadding="2" cellspacing="2">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineGenTop.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineSezione.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineFornitore.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineValuta.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineBilancio.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineNoteOrd.jsp" />
				<!-- GESTIONE DELLA ABILITAZIONE CAMPI DI ABBONAMENTO -->
				<jsp:include   page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineAbbonamento.jsp" />
		      </table>

		</logic:equal>

		<logic:equal  name="navForm" property="scegliTAB" value="C">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineModTabC.jsp" />
		      <table  width="100%"   border="0" cellpadding="2" cellspacing="2">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineGenTop.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineFornitore.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineNoteOrd.jsp" />
				<!-- GESTIONE DELLA ABILITAZIONE CAMPI DI ABBONAMENTO -->
				<jsp:include   page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineAbbonamento.jsp" />
		      </table>

		</logic:equal>
		<logic:equal  name="navForm" property="scegliTAB" value="R">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineModTabR.jsp" />
			  <table  width="100%"   border="0" cellpadding="2" cellspacing="2">
			  <sbn:disableAll checkAttivita="STAMPATO" inverted="true">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineRilGenTop.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineRilFornitore.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineBilancio.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineValuta.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineNoteForn.jsp" />
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineRilTipoInvio.jsp" />
			</sbn:disableAll>
				<sbn:checkAttivita idControllo="SPEDITO">
					<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineRilSpedizione.jsp" />
				</sbn:checkAttivita>
		      </table>
		<logic:greaterThan property="numRigheInv" name="navForm" value="0">
		<br>
	  	  <table  align="center" width="100%" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				     <tr><td class="etichetta" colspan="5">
			               <bean:message  key="ricerca.label.totInventariAssociati" bundle="acquisizioniLabels" />
						<html:text styleId="testoNormale"   property="numRigheInv" name="navForm" size="3" readonly="true"></html:text>
				     </td></tr>
					<tr>
				  	<tr class="etichetta" bgcolor="#dde8f0">

						<td align="center"  style="width:10%;"  scope="col" >
		                   	<bean:message  key="ricerca.label.serie" bundle="acquisizioniLabels" />
						</td>

						<td align="center"  style="width:20%;"  scope="col" >
		                   	<bean:message  key="ordine.label.numInv" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" style="width:45%;">
		                   	<bean:message  key="ordine.label.titInv" bundle="acquisizioniLabels" />
						</td>
						<td align="center" style="width:10%;" scope="col">
		                   	<bean:message  key="ordine.label.rilegaturaDataUscita" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" style="width:10%;">
		                   	<bean:message  key="ordine.label.rilegaturaDataRientroPresunta" bundle="acquisizioniLabels" />
						</td>

						<td align="center" scope="col" style="width:10%;">
		                   	<bean:message  key="ordine.label.rilegaturaDataRientro" bundle="acquisizioniLabels" />
						</td>
						<!--
						<td scope="col" align="center" style="width: 10%;" >
                  			<bean:message  key="ricerca.label.note" bundle="acquisizioniLabels" />
						</td>

	     				<td scope="col" style="width:5%;" align="center" >&nbsp;</td>
						-->
					</tr>

				<logic:iterate id="elencaInventari" property="elencaInventari" name="navForm" indexId="indice">
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
		<!-- controllo se è stato selezionata l'operazione di inserimento riga -->
							<td align="center" >
<!--								<html:text styleId="testoNormale"  name="elencaInventari" indexed="true"  property="serie" size="3" maxlength="3" readonly="true" ></html:text>-->
								<bs:write  name="elencaInventari" property="serie"/>
							</td>

							<td align="center" >
<!--								<html:text styleId="testoNormale"  name="elencaInventari" indexed="true" property="numero"  size="10" readonly="true" ></html:text>-->
								<bs:write  name="elencaInventari" property="numero"/>

							</td>

							<td align="center" >
								<bs:write   name="elencaInventari" property="titolo"  />
							</td>
							<td style="text-align: right;" >
<!--								<html:text styleId="testoNormale"  indexed="true" name="elencaInventari" property="dataUscita" size="10" readonly="true" ></html:text>-->
								<bs:write  name="elencaInventari" property="dataUscita"/>

							</td>
							<td style="text-align: right;">
<!--								<html:text styleId="testoNormale" indexed="true" name="elencaInventari" property="dataRientro" size="10" readonly="true" ></html:text>-->
								<bs:write  name="elencaInventari" property="dataRientroPresunta"/>
							</td>

							<td style="text-align: right;">
<!--								<html:text styleId="testoNormale" indexed="true" name="elencaInventari" property="dataRientro" size="10" readonly="true" ></html:text>-->
								<bs:write  name="elencaInventari" property="dataRientro"/>
							</td>
							<bs:define id="operazioneValue">
							  <bs:write  name="indice" />
							</bs:define>
							<!--

							<td   scope="col" align="center" width="20%">
								<input type="submit"  class="buttonImageNote"  name="tagNote" value="${indice}" title="note"></input>
							</td>

							<td  class="testoNormale" align="left">
								<html:radio  property="radioInv"  value="${operazioneValue}" ></html:radio>
							</td>
							-->
						</tr>
			</logic:iterate>
        </table>
		</logic:greaterThan>

		</logic:equal>
	<br>
</div>
 <div id="divFooter">
		<c:choose>
			<c:when test="${navForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:when test="${navForm.esaminaOrdine}">
				<table align="center">
					<tr>
						<td><sbn:bottoniera buttons="pulsanti" /></td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<!-- tabella bottoni  -->
		        <table align="center"  border="0" style="height:40px;" cellspacing="0"; cellpadding="0">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/ordini/esaminaOrdineModGenBottom.jsp" />

		      	</table>
				<!--	 fine tabella bottoni    -->
    		</c:otherwise>
		</c:choose>

	</div>
	</sbn:navform>
</layout:page>
