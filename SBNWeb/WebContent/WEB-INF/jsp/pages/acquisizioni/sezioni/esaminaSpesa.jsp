<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<bean-struts:define id="noinput"  value="false"/>


<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/sezioni/esaminaSpesa.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<table   width="100%" border="0">

		     <tr>
 						<td  class="etichetta" width="20%" scope="col" align="left">
	                        <bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="sezione.codBibl" size="5"  readonly="true"></html:text>
                        </td>
                        <td  class="etichetta">&nbsp;</td>
                        <td  class="etichetta">&nbsp;</td>

			</tr>
		     <tr>
						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.codiceSezione" bundle="acquisizioniLabels" />
						</td>
                        <td colspan="2"  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="sezione.codiceSezione" size="5" readonly="true"></html:text>
                        </td>
                        <td  class="etichetta">&nbsp;</td>
                        <td  class="etichetta">&nbsp;</td>

			</tr>
		     <tr>
						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.nomeSezione" bundle="acquisizioniLabels" />
						</td>
                        <td colspan="2"  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="sezione.descrizioneSezione" size="50" readonly="true"></html:text>
                        </td>
                        <td  class="etichetta">&nbsp;</td>
                        <td  class="etichetta">&nbsp;</td>

			</tr>

		     <tr>
						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.sommaDisp" bundle="acquisizioniLabels" />
						</td>
                        <td colspan="2"  scope="col" align="left">
     					  	<bean-struts:write format="0.00" name="esaminaSpesaForm" property="sezione.sommaDispSezione" />
                        </td>
                        <td  class="etichetta">&nbsp;</td>
                        <td  class="etichetta">&nbsp;</td>

			</tr>
			<c:choose>
				<c:when test="${esaminaSpesaForm.gestBil}">
			     <tr>
	                  <td scope="col"  align="left" class="etichetta">
	                  	<bean:message  key="ordine.label.esercizio" bundle="acquisizioniLabels" />
	                  </td>
	                  <td scope="col" align="left">
	                  <html:text styleId="testoNormale" property="esercizio" size="4" maxlength="4"></html:text>
	                  </td>
	                  <td  class="etichetta">&nbsp;</td>
	                  <td  class="etichetta">&nbsp;</td>
				</tr>
				</c:when>
			</c:choose>

		     <tr>
                        <td scope="col" class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.dataDa" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:text styleId="testoNormale" property="dataDa" size="10" ></html:text>
                        </td>
                        <td scope="col"  class="etichetta"  align="left">
                  			<bean:message  key="ricerca.label.dataA" bundle="acquisizioniLabels" />
							<html:text styleId="testoNormale" property="dataA" size="10" ></html:text>
							<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />
                        </td>
	                  <td  class="etichetta">&nbsp;</td>
			</tr>
   		<tr><td  class="etichetta">&nbsp;</td></tr>
   		<tr><td  class="etichetta">&nbsp;</td></tr>

		     <tr>
                        <td   colspan="4"  align="center">

			 			<html:submit styleClass="pulsanti" property="methodEsaminaSpesa">
							<bean:message key="ricerca.button.elabora" bundle="acquisizioniLabels" />
						</html:submit>
                        </td>
			</tr>

   		<tr><td  class="etichetta">&nbsp;</td></tr>
        </table>
<br>
<br>
<logic:greaterThan property="numBilanci" name="esaminaSpesaForm" value="0">
	  	<table  align="center" width="70%" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0">
                        <td scope="col" align="left" style="width:10%;">
		                   	<bean:message  key="ordine.label.esercizio" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left" style="width:10%;">
		                   	<bean:message  key="ordine.label.capitolo" bundle="acquisizioniLabels" />
                        </td>
						<td align="center"  style="width:10%;" title="Codice impegno" scope="col"  >
		                   	<bean:message  key="ricerca.label.impCorto" bundle="acquisizioniLabels" />
						</td>
						<td align="center" style="width:20%;" scope="col">
		                   	<bean:message  key="ricerca.label.impegnato" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" style="width:20%;">
		                   	<bean:message  key="ricerca.label.acquisito" bundle="acquisizioniLabels" />
						</td>
					</tr>

			<logic:iterate id="elencaBilanci" property="listaBilanci" name="esaminaSpesaForm" indexId="indice1">

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
	                        <td scope="col" align="left">
								<bean-struts:write    name="elencaBilanci" property="esercizio"  />
	                        </td>
	                        <td scope="col" align="left" >
								<bean-struts:write    name="elencaBilanci" property="capitolo"  />
	                        </td>
							<td align="center" >
								<bean-struts:write    name="elencaBilanci" property="impegno"  />
	                        </td>
							<td style="text-align: right;" >
								<bean-struts:write    name="elencaBilanci" property="impegnato" format="0.00" />
							</td>
							<td scope="col" style="width:10%;text-align: right;">
								<bean-struts:write    name="elencaBilanci" property="acquisito" format="0.00" />
							</td>
						</tr>
			</logic:iterate>
				  	<tr class="etichetta" bgcolor="#dde8f0">
                        <td scope="col" colspan="3" style="text-align: right;">
		                   	<bean:message  key="acquisizioni.bottone.totale" bundle="acquisizioniLabels" />
                        </td>
						<td align="center" scope="col">
							<bean-struts:write    name="esaminaSpesaForm" property="sezione.ordinato" format="0.00" />
						</td>
						<td align="center" scope="col" >
							<bean-struts:write    name="esaminaSpesaForm" property="sezione.acquisito" format="0.00" />
						</td>
					</tr>
        </table>
</logic:greaterThan>
<br>
<br>
<logic:greaterThan property="numTipologie" name="esaminaSpesaForm" value="0">
	  	<table  align="center" width="70%" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td align="center" colspan="3" style="width:10%;" title="Codice impegno" scope="col"  >
		                   	<bean:message  key="buono.label.tipologia" bundle="acquisizioniLabels" />
						</td>
						<td align="center" style="width:20%;" scope="col">
		                   	<bean:message  key="ricerca.label.impegnato" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" style="width:20%;">
		                   	<bean:message  key="ricerca.label.acquisito" bundle="acquisizioniLabels" />
						</td>
					</tr>

			<logic:iterate id="elencaTipologie" property="listaTipologie" name="esaminaSpesaForm" indexId="indice1">
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
							<td align="center" colspan="3" >
								<bean-struts:write    name="elencaTipologie" property="tipologia"  />
	                        </td>
							<td style="text-align: right;" >
								<bean-struts:write    name="elencaTipologie" property="impegnato" format="0.00" />
							</td>
							<td scope="col" style="width:10%;text-align: right;">
								<bean-struts:write    name="elencaTipologie" property="acquisito" format="0.00" />
							</td>
						</tr>
			</logic:iterate>
				  	<tr class="etichetta" bgcolor="#dde8f0">
                        <td scope="col" colspan="3" style="text-align: right;">
		                   	<bean:message  key="acquisizioni.bottone.totale" bundle="acquisizioniLabels" />
                        </td>
						<td align="center" scope="col" style="text-align: right;">
							<bean-struts:write    name="esaminaSpesaForm" property="sezione.ordinato" format="0.00" />
						</td>
						<td align="center" scope="col" style="text-align: right;">
							<bean-struts:write    name="esaminaSpesaForm" property="sezione.acquisito" format="0.00" />
						</td>
					</tr>
        </table>
</logic:greaterThan>

<br>
<br>

</div>
<br>
<br>
 <div id="divFooter">

				<!-- tabella bottoni -->

		           <table align="center"  border="0" style="height:40px" >
					<tr>
		             <td  valign="top" align="center">
			 			<html:submit styleClass="pulsanti" property="methodEsaminaSpesa">
							<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
						</html:submit>
		             </td>
		             </tr>
		      	  </table>


     	  </div>
	</sbn:navform>
</layout:page>
