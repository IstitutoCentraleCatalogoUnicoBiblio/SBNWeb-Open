<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="formName" name="org.apache.struts.action.mapping.instance" property="name" type="java.lang.String" />

<c:choose>
<c:when test="${formName eq 'esaminaOrdineModForm'}">
	<bean-struts:define id="abbonamenti">
		<bean-struts:write name="esaminaOrdineModForm" property="disabilitazioneSezioneAbbonamento" />
	</bean-struts:define>
	<bean-struts:define id="noinput"  value="false"/>
	<c:choose>
	<c:when test="${esaminaOrdineModForm.disabilitaTutto}">
		<bean-struts:define id="abbonamenti"  value="true"/>
		<bean-struts:define id="noinput"  value="true"/>
	</c:when>
	</c:choose>
</c:when>
</c:choose>

<c:choose>
<c:when test="${formName eq 'esaminaOrdineForm'}">
	<bean-struts:define id="abbonamenti">
		<bean-struts:write name="esaminaOrdineForm" property="disabilitazioneSezioneAbbonamento" />
	</bean-struts:define>
	<bean-struts:define id="noinput"  value="false"/>
	<c:choose>
	<c:when test="${esaminaOrdineForm.disabilitaTutto}">
		<bean-struts:define id="abbonamenti"  value="true"/>
		<bean-struts:define id="noinput"  value="true"/>
	</c:when>
	</c:choose>
</c:when>
</c:choose>

       <tr>
         <td class="etichettaIntestazione" >
	        <table  border="0" cellspacing="2" cellpadding="2"  >
	              <tr>
	                <td  class="etichettaIntestazione" valign="top"><bean:message  key="ordine.label.datiPeriodico" bundle="acquisizioniLabels" /></td>
	              </tr>
	              <tr>
		         	<td class="etichettaIntestazione" valign="bottom"><bean:message  key="ordine.label.abbonamento" bundle="acquisizioniLabels" /></td>
	              </tr>

	        </table>
         </td>
         <td class="etichetta">
             <table border="2" bordercolor="blue" width="95%"   cellspacing="1" cellpadding="1" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
               <tr>
                 <td class="etichetta" width="15%">
                 	<bean:message  key="ordine.label.annata" bundle="acquisizioniLabels" /></td>
                 <td>
  					<html:text styleId="testoNormale"   property="datiOrdine.annataAbbOrdine" size="10" maxlength="10" readonly="${abbonamenti}"></html:text>
                 </td>
                 <td width="15%"  scope="col" class="etichetta">
                 	<bean:message  key="ordine.label.NVol" bundle="acquisizioniLabels" />
                 </td>
                 <td  scope="col">
  					<html:text styleId="testoNormale"   property="datiOrdine.numVolAbbOrdine"  size="3" readonly="${abbonamenti}"></html:text>
                 </td>
                 <td width="15%" scope="col" class="etichetta" align="left">
                 	<bean:message  key="ordine.label.numFasc" bundle="acquisizioniLabels" />
                 </td>
                 <td scope="col" align="left">
					<html:text styleId="testoNormale"   property="datiOrdine.numFascicoloAbbOrdine"   size="4" maxlength="15" readonly="${abbonamenti}"></html:text>
                 </td>
                 <td rowspan="2" align="center"  valign="center" width="3%" style="border:0;"  >
	                <html:submit  styleClass="buttonImage" property="${navButtons}"
	                	disabled="${abbonamenti}">
						<bean:message  key="ordine.button.scegliFascicolo" bundle="acquisizioniLabels" />
					</html:submit>
			 	 </td>

               </tr>
               <tr>
                 <td width="15%" scope="col" class="etichetta"  align="left">
                 	<bean:message  key="ordine.label.dataAbbIni" bundle="acquisizioniLabels" />
                 </td>
                 <td scope="col" class="etichetta">
  					<html:text styleId="testoNormale"   property="datiOrdine.dataPubblFascicoloAbbOrdine"  size="10"  maxlength="10" readonly="${abbonamenti}"></html:text>
                 </td>
		          <td  width="15%" class="etichetta">
		          	<bean:message  key="ordine.label.dataF" bundle="acquisizioniLabels" />
		          </td>
		          <td  class="testoNormale">
			          <html:text styleId="testoNormale"   property="datiOrdine.dataFineAbbOrdine" size="10" readonly="${abbonamenti}" ></html:text>
		          </td>
                 <td class="etichetta" width="15%" ><bean:message  key="ordine.label.periodo" bundle="acquisizioniLabels" /></td>
                 <td align="left">
					<html:select style="width:40px" styleClass="testoNormale" property="datiOrdine.periodoValAbbOrdine" disabled="${abbonamenti}">
					<html:optionsCollection  property="listaPeriodo" value="codice" label="descrizione" />
					</html:select>
			 	 </td>

          		</tr>
	      		</table>
		   </td>
       </tr>
       <tr>
	 	 <td class="etichettaIntestazione">
				 	<bean:message  key="ordine.label.annoAbbonamento" bundle="acquisizioniLabels" />
		 </td>
         <td class="etichetta">
	       	<html:text styleId="testoNormale"   property="datiOrdine.annoAbbOrdine"  size="4" maxlength="4" readonly="${abbonamenti}"></html:text>
			  &nbsp;&nbsp;
	  	      <bean:message  key="ordine.label.rinnovato" bundle="acquisizioniLabels" />
			  <html:checkbox   property="datiOrdine.rinnovato" disabled="true"></html:checkbox>
          </td>
       </tr>




