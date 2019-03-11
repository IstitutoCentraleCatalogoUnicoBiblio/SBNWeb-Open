<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

		  <table   width="100%" border="0" >
			<tr>
					<td colspan="4">
					<div class="etichetta"><bean:message
						key="ricerca.label.biblioteca" bundle="gestioneStampeLabels" />
					<html:text disabled="true" styleId="testoNormale" property="codBibl"
						size="5" maxlength="3"></html:text> <span disabled="true">
						<html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodStampaOrdini" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
						</html:submit>
						<bean-struts:write	name="stampaOrdiniForm" property="descrBib" />

					</span> </div>
					</td>
				</tr>
		     <tr>
   						<td colspan="4"  scope="col" align="center">&nbsp;</td>
		     </tr>


		     <tr>
                        <td scope="col"  class="etichetta" align="left" width="15%">
	                        <bean:message  key="ricerca.label.tipoOrdineBis" bundle="acquisizioniLabels" />
                        </td>
			  	        <td   scope="col" class="etichetta" colspan="17" >
                        	<bean:message  key="ordine.label.acquisto" bundle="acquisizioniLabels" />
							<html:multibox  property="tipoArr" value="A" ></html:multibox>
							&nbsp;&nbsp;&nbsp;
                        	<bean:message  key="ordine.label.visioneTrattenuta" bundle="acquisizioniLabels" />
							<html:multibox  property="tipoArr" value="V"  ></html:multibox>
                        	&nbsp;&nbsp;&nbsp;
                        	<bean:message  key="ordine.label.cambio" bundle="acquisizioniLabels" />
							<html:multibox   property="tipoArr" value="C"  ></html:multibox>
                        	&nbsp;&nbsp;&nbsp;
                        	<bean:message  key="ordine.label.dono" bundle="acquisizioniLabels" />
							<html:multibox   property="tipoArr" value="D"  ></html:multibox>
                        	&nbsp;&nbsp;&nbsp;
                        	<bean:message  key="ordine.label.depositoLegale" bundle="acquisizioniLabels" />
							<html:multibox   property="tipoArr" value="L"  ></html:multibox>
                        	&nbsp;&nbsp;&nbsp;
                        	<bean:message  key="ordine.label.rilegatura" bundle="acquisizioniLabels" />
							<html:multibox   property="tipoArr" value="R"  ></html:multibox>
                        </td>
		     </tr>
		     <tr>
   						<td colspan="4"  scope="col" align="center">&nbsp;</td>
		     </tr>

		     <tr>
                        <td scope="col" >
                        <div align="left" class="etichetta">
                        <bean:message  key="ricerca.label.dataOrdineDa" bundle="gestioneStampeLabels" /></div>
                        </td>
                        <td colspan="3">
				 		  <html:text styleId="testoNormale" property="dataOrdineDa" size="10"></html:text>
                         	<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <bean:message  key="ricerca.label.dataOrdineA" bundle="gestioneStampeLabels" />
				 		  <html:text styleId="testoNormale" property="dataOrdineA" size="10"></html:text>
                         	<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />

                        </td>
			</tr>
		     <tr>
   						<td colspan="4" scope="col" align="center">&nbsp;</td>
		     </tr>
		     <tr >
					    <td class="etichetta" valign="top">
					    <bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
					    </td>
                        <td valign="top" colspan="16" scope="col" align="left" >
				 		  <html:text styleId="testoNormale" property="codFornitore" size="5"  maxlength="10" ></html:text>
						  <bean-struts:write	name="stampaOrdiniForm" property="fornitore" />

                        <html:submit  styleClass="buttonImage" property="methodStampaOrdini" >
							<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
						</html:submit>
                        </td>

            </tr>
		     <tr>
   						<td colspan="4" scope="col" align="center">&nbsp;</td>
		     </tr>

              <tr>
						  <td colspan="4"   class="etichetta" align="right">
						  <div align="left" class="etichetta">
				         	<bean:message key="ordine.label.ordineDaStampare" bundle="acquisizioniLabels" />
					        <html:radio property="tipoRicerca" value="stampa" />
 	                      	&nbsp;
 	                      	<bean:message key="ordine.label.ordineDaRistampare" bundle="acquisizioniLabels" />
	        				<html:radio property="tipoRicerca" value="ristampa" />
                       	  </div>
                       	  </td>
              </tr>
              <tr>
						  <td colspan="4"   class="etichetta" align="right">
						  <div align="left" class="etichetta">
				         	<bean:message key="ordine.label.fileUnico" bundle="acquisizioniLabels" />
					        <html:radio property="fileRisultato" value="unico" />
 	                      	&nbsp;
 	                      	<bean:message key="ordine.label.fileDistinti" bundle="acquisizioniLabels" />
	        				<html:radio property="fileRisultato" value="distinti" />
                       	  </div>
                       	  </td>
              </tr>

		     <tr>
   						<td colspan="4" scope="col" align="center">&nbsp;</td>
		     </tr>
		      <tr>
			    <td colspan="4">
			     	<hr color="#dde8f0"/>
			    </td>
			  </tr>

		     <tr>
                        <%-- td   valign="top" scope="col" align="left"> <div class="etichetta" --%>
                        <td  width="15%"  scope="col"  ><div align="left" class="etichetta">
						<bean:message  key="periodici.label.ordinamento" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<%--html:select  styleClass="testoNormale"  property="provincia"--%>
						<html:select  styleClass="testoNormale"  property="tipoOrdinamSelez" style="width:200px">
						<html:optionsCollection  property="listaTipiOrdinamento" value="codice" label="descrizione" />
						</html:select>
                        </td>
						<bean-struts:size id="comboSize" name="stampaOrdiniForm" property="elencoModelli" />
						<logic:greaterEqual name="comboSize" value="2">
							<!--Selezione Modello Via Combo-->
							<td  width="15%"  scope="col"  ><div align="left" class="etichetta">
							<bean:message key="fornitori.label.modello" bundle="gestioneStampeLabels" />
							</div></td>
							<td valign="top" scope="col" align="left">
							<html:select  styleClass="testoNormale"  property="tipoModello" >
								<html:optionsCollection  property="elencoModelli" value="jrxml" label="descrizione" />
							</html:select>
							</td>
						</logic:greaterEqual>
						<logic:lessThan name="comboSize" value="2">
							<!--Selezione Modello Hidden-->
							<td  width="15%"  scope="col"  ><div align="left" class="etichetta">
							&nbsp;
							</div></td>
							<td valign="top" scope="col" align="left">
							&nbsp;
							<html:hidden property="tipoModello" value="${stampaOrdiniForm.elencoModelli[0].jrxml}" />
							</td>
						</logic:lessThan>
		         	        <!-- FINE tabella corpo COLONNA + LARGA -->
             </tr>
	     </table>
	     	<HR>
			<jsp:include flush="true" page="/WEB-INF/jsp/pages/gestionestampe/common/tipoStampa.jsp" />
			<HR>
