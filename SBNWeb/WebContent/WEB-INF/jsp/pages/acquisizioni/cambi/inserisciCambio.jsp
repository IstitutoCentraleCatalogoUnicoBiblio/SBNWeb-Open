<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<bean-struts:define id="noinput"  value="false"/>

<c:choose>
<c:when test="${navForm.conferma}">
	<bean-struts:define id="noinput"  value="true"/>
</c:when>
</c:choose>


<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/cambi/inserisciCambio.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>

		<table   width="100%" border="0">
     		<tr><td  colspan="2" class="etichetta">&nbsp;</td></tr>
		     <tr>
				<td  class="etichetta" width="15%" scope="col" align="left">
					<bean:message  key="ricerca.label.codBibl" bundle="acquisizioniLabels" />
				</td>
                <td  scope="col" align="left">
					<html:text styleId="testoNormale"  property="datiCambio.codBibl" size="4" readonly="true" ></html:text>
					<c:choose>
						<c:when test="${navForm.conferma}">
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodInserisciCambio" disabled="true">
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>

						</c:when>
						<c:otherwise>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodInserisciCambio" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>

			    		</c:otherwise>
					</c:choose>


                </td>
			</tr>
		     <tr>
		          <td class="etichetta" align="left">
						<bean:message  key="ordine.label.valuta" bundle="acquisizioniLabels" />
		          </td>
                  <td  scope="col" align="left">
                  <!--
						<html:text styleId="testoNormale"  property="datiCambio.codValuta" size="3" maxlength="3"></html:text>

				  -->
					<c:choose>
					<c:when test="${navForm.conferma}">
						<html:select styleClass="testoNormale"  disabled="true" property="datiCambio.codValuta" style="width:50px">
						<html:optionsCollection  property="listaValuta" value="codice" label="descrizioneCodice" />
						</html:select>
					</c:when>
					<c:otherwise>
						<html:select styleClass="testoNormale"  property="datiCambio.codValuta" style="width:50px" disabled="${navForm.datiCambio.IDCambio ne 0}" >
						<html:optionsCollection  property="listaValuta" value="codice" label="descrizioneCodice" />
						</html:select>
		    		</c:otherwise>
					</c:choose>
	  	    		<c:if test="${navForm.forzaValutaRif}">
	  	    			<bean:message  key="ricerca.button.valRif" bundle="acquisizioniLabels" />
	  	    			<html:checkbox   property="valRiferChk" disabled="${noinput or navForm.forzaValutaRif}" onchange="this.form.submit();"/>
					</c:if>
		          </td>
             </tr>
             <!--
		     <tr>
		          <td class="etichetta" align="left">
						<bean:message  key="ordine.label.descrValuta" bundle="acquisizioniLabels" />
		          </td>
                  <td  scope="col" align="left">
						<html:text styleId="testoNormale"  property="datiCambio.desValuta" size="30"  ></html:text>
		          </td>
             </tr>
		     -->
		     <tr>
 						<td  class="etichetta"  scope="col" align="left">
							<bean:message  key="ordine.label.cambio" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left">
						<c:choose>
						<c:when test="${navForm.conferma}">
                        	<html:text styleId="testoNormale" readonly="true" property="datiCambio.tassoCambioStr" size="10"  ></html:text>
						</c:when>
						<c:otherwise>
					    	<html:text styleId="testoNormale"  property="datiCambio.tassoCambioStr" size="10" disabled="${navForm.forzaValutaRif}"  ></html:text>
			    		</c:otherwise>
						</c:choose>



                        </td>

			</tr>


		     <tr>
 						<td  class="etichetta"  scope="col" align="left">
							<bean:message  key="ricerca.label.dataVariazione" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" colspan="5" align="left">
							<html:text styleId="testoNormale"  property="datiCambio.dataVariazione" size="10" disabled="true" ></html:text>
                        </td>
 			</tr>



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
				<c:choose>
					<c:when test="${!navForm.valRifer or navForm.datiCambio.IDCambio==0}">
							<html:submit styleClass="pulsanti" property="methodInserisciCambio">
								<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
							</html:submit>
			       </c:when>
				</c:choose>
				<!--
				<html:submit styleClass="pulsanti" property="methodInserisciCambio">
					<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
				</html:submit>

				<html:submit styleClass="pulsanti" property="methodInserisciCambio">
					<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
				</html:submit>

				<html:submit styleClass="pulsanti" property="methodInserisciCambio">
					<bean:message key="ricerca.button.cancella" bundle="acquisizioniLabels" />
				</html:submit>
	 			-->
	 			<html:submit styleClass="pulsanti" property="methodInserisciCambio">
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
