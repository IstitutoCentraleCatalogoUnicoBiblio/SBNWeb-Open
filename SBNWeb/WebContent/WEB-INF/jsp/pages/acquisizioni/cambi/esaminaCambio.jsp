<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<c:choose>
<c:when test="${navForm.disabilitaTutto}">
	<bean-struts:define id="noinput"  value="true"/>
</c:when>
</c:choose>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/cambi/esaminaCambio.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
				  <html:hidden name="navForm" property="posizioneScorrimento" />

		<table   width="100%" border="0">
     		<tr><td  colspan="2" class="etichetta">&nbsp;</td></tr>
		     <tr>
				<td  class="etichetta" width="15%" scope="col" align="left">
					<bean:message  key="ricerca.label.codBibl" bundle="acquisizioniLabels" />
				</td>
                <td  scope="col" align="left">
					<html:text styleId="testoNormale"  property="datiCambio.codBibl" size="4" readonly="true" ></html:text>
                </td>
			</tr>
		     <tr>
		          <td class="etichetta" align="left">
						<bean:message  key="ordine.label.valuta" bundle="acquisizioniLabels" />
		          </td>
                  <td  scope="col" align="left">
						<html:text styleId="testoNormale"  property="datiCambio.codValuta" size="3" readonly="true" ></html:text><!--
						<html:select styleClass="testoNormale"   property="datiCambio.codValuta" style="width:50px">
						<html:optionsCollection  property="listaValuta" value="codice" label="descrizione" />
						</html:select>
		          -->
		          <c:if test="${navForm.valRifer}">
	  	    		<bean:message  key="ricerca.button.valRif" bundle="acquisizioniLabels" />
		  	    		<sbn:disableAll disabled="true">
							<html:checkbox  property="valRiferChk"  onchange="this.form.submit();" disabled="${noinput}"/>
						</sbn:disableAll>
					</c:if>

		          </td>
             </tr>
		     <tr>
		          <td class="etichetta" align="left">
						<bean:message  key="ordine.label.descrValuta" bundle="acquisizioniLabels" />
		          </td>
                  <td  scope="col" align="left">
						<html:text styleId="testoNormale"  property="datiCambio.desValuta" size="30" readonly="true" ></html:text>
		          </td>
             </tr>


		     <tr>
 						<td  class="etichetta"  scope="col" align="left">
							<bean:message  key="ordine.label.cambio" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left">
						<c:choose>
							<c:when test="${navForm.conferma or navForm.valRifer}">
                        		<html:text styleId="testoNormale" readonly="true" property="datiCambio.tassoCambioStr" size="10"  ></html:text>

							</c:when>
							<c:otherwise>
						    	<html:text styleId="testoNormale"  property="datiCambio.tassoCambioStr" size="10"  ></html:text>
				    		</c:otherwise>
						</c:choose>

                        </td>

			</tr>

		<c:choose>
			<c:when test="${!navForm.valRifer}">
		     <tr>
 						<td  class="etichetta"  scope="col" align="left">
							<bean:message  key="ricerca.label.dataVariazione" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" colspan="5" align="left">
							<html:text styleId="testoNormale"  property="datiCambio.dataVariazione" size="10" readonly="true" ></html:text>
                        </td>
 			</tr>
	       </c:when>
		</c:choose>






     		<tr><td  colspan="2" class="etichetta">&nbsp;</td></tr>

		</table>
 </div>
 <div id="divFooter">

				<!-- tabella bottoni -->
		<c:choose>
			<c:when test="${navForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>

	           <table align="center"  border="0" style="height:40px" >
	             <tr>
	             <td  valign="top" >
			<logic:equal name="navForm" property="enableScorrimento" value="true">
						<html:submit styleClass="pulsanti" property="methodEsaminaCambio">
							<bean:message key="ricerca.button.scorriIndietro" bundle="acquisizioniLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodEsaminaCambio">
							<bean:message key="ricerca.button.scorriAvanti" bundle="acquisizioniLabels" />
						</html:submit>
			</logic:equal>

		<logic:equal name="navForm" property="disabilitaTutto" value="false">
             <sbn:checkAttivita idControllo="GESTIONE">
				<c:choose>
					<c:when test="${!navForm.valRifer}">
						<html:submit styleClass="pulsanti" property="methodEsaminaCambio">
							<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
						</html:submit>
			       </c:when>
				</c:choose>

				<!--
				<html:reset styleClass="pulsanti">
					<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
				</html:reset>

				-->
				<html:submit styleClass="pulsanti" property="methodEsaminaCambio">
					<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
				</html:submit>
				<html:submit styleClass="pulsanti" property="methodEsaminaCambio">
					<bean:message key="ricerca.button.cancella" bundle="acquisizioniLabels" />
				</html:submit>
             </sbn:checkAttivita>

			</logic:equal>

				<html:submit styleClass="pulsanti" property="methodEsaminaCambio">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
		        </td>
	            </tr>
	      	  </table>
    		</c:otherwise>
		</c:choose>

	  			<!-- fine tabella bottoni -->
     	  </div>
	</sbn:navform>
</layout:page>
