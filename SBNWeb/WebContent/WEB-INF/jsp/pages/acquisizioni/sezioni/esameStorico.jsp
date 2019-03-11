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
	<sbn:navform action="/acquisizioni/sezioni/esameStorico.do">
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
     					  	<bean-struts:write format="0.00" name="esameStoricoForm" property="sezione.sommaDispSezione" />
                        </td>
                        <td  class="etichetta">&nbsp;</td>
                        <td  class="etichetta">&nbsp;</td>

			</tr>
		     <tr>
						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.budget" bundle="acquisizioniLabels" />
						</td>
                        <td scope="col" align="left">
     					  	<bean-struts:write format="0.00" name="esameStoricoForm" property="sezione.budgetSezione" />
                        </td>
                       <td  class="etichetta">&nbsp;</td>
                       <td  class="etichetta">&nbsp;</td>
			</tr>
		     <tr>
                        <td scope="col" align="left">
	                        <bean:message  key="ricerca.label.dataFineValidita" bundle="acquisizioniLabels" />
                        </td>
						<td scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="sezione.dataVal" size="10" readonly="true"></html:text>
                        </td>
                      <td  class="etichetta">&nbsp;</td>
                       <td  class="etichetta">&nbsp;</td>
			</tr>

			</table>
<br>
<br>
<logic:greaterThan property="numBilanci" name="esameStoricoForm" value="0">
	  	<table  align="center" width="30%" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0">
                        <td scope="col" align="left" style="width:10%;">
		                   	<bean:message  key="buono.label.dataBuono" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left" style="width:10%;">
		                   	<bean:message  key="buono.label.variazioneBudget" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left" style="width:10%;">
		                   	<bean:message  key="buono.label.budgetPrecedente" bundle="acquisizioniLabels" />
                        </td>

                   </tr>

			<logic:iterate id="elencaVariazioni" property="sezione.righeEsameStoria" name="esameStoricoForm" indexId="indice1">

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
								<bean-struts:write    name="elencaVariazioni" property="codice1"  />
	                        </td>
	                        <td scope="col" align="left" >
								<bean-struts:write    name="elencaVariazioni" property="codice2"  />
	                        </td>
	                        <td scope="col" align="left" >
								<bean-struts:write    name="elencaVariazioni" property="codice3"  />
	                        </td>

	                    </tr>
			</logic:iterate>
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
			 			<html:submit styleClass="pulsanti" property="methodEsameStorico">
							<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
						</html:submit>
		             </td>
		             </tr>
		      	  </table>


     	  </div>
	</sbn:navform>
</layout:page>
