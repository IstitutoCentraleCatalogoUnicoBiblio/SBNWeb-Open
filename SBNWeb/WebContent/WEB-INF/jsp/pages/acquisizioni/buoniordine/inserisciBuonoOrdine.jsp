<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput"  value="false"/>

<c:choose>
<c:when test="${navForm.disabilitaTutto}">
	<bean-struts:define id="noinput"  value="true"/>
</c:when>
</c:choose>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/buoniordine/inserisciBuonoOrdine.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
             <table  width="100%"  align="center">
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="85%">

		  <table   width="100%"  border="0" cellpadding="0" cellspacing="0">


		     <tr>
                        <td  width="10%"  scope="col" class="etichetta" align="left">
                        <bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
                        <sbn:disableAll checkAttivita="SCEGLI" inverted="true">
						<html:text styleId="testoNormale" property="buono.codBibl" size="10" readonly="true"></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodInserisciBuonoOrdine" disabled="${noinput}" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>
							</sbn:disableAll>

                        </td>
                        <td scope="col" class="etichetta" align="left">
                        <bean:message  key="buono.label.numBuono" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" colspan="2" align="left">

						<c:choose>
						<c:when test="${navForm.numAuto}">
<!--							<bean-struts:write  name="navForm" property="buono.numBuonoOrdine"/>-->
							<html:text styleId="testoNormale" property="buono.numBuonoOrdine"  size="10" readonly="true"></html:text>

						</c:when>
						<c:otherwise>
							<html:text styleId="testoNormale" property="buono.numBuonoOrdine"  size="10" readonly="${noinput}"></html:text>
						</c:otherwise>
						</c:choose>

                        </td>
                        <td scope="col"  colspan="2" class="etichetta" align="left">
                        <bean:message  key="buono.label.dataBuono" bundle="acquisizioniLabels" />
						<html:text styleId="testoNormale" property="buono.dataBuonoOrdine"  size="10" readonly="${noinput}" ></html:text>
                        </td>
		     </tr>
             <tr><td class="etichettaIntestazione" colspan="8">&nbsp;</td></tr>

 		     <tr>
 		     	 <sbn:disableAll checkAttivita="SCEGLI" inverted="true">
                        <td  valign="top" class="etichetta"  scope="col" align="left">
                        <bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
                        </td>
                        <td  valign="top" scope="col" colspan="6" align="left" style="width:280px;">
					 		<html:text styleId="testoNormale" property="buono.fornitore.codice" size="5"  readonly="${noinput}"></html:text>
							<html:text styleId="testoNormale" property="buono.fornitore.descrizione" size="50"  readonly="${noinput}" ></html:text>
	                    	<html:submit  styleClass="buttonImage" property="methodInserisciBuonoOrdine" disabled="${noinput}">
								<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
							</html:submit>
						</td>
						</sbn:disableAll>
             </tr>
	<c:choose>
		<c:when test="${navForm.gestBil}">

             <tr><td class="etichettaIntestazione" colspan="8">Bilancio:</td></tr>
             <tr><td class="etichetta" colspan="8">&nbsp;</td></tr>
		     <tr>
		      <sbn:disableAll checkAttivita="SCEGLI" inverted="true">
                        <td scope="col" class="etichetta" align="left">
                        <bean:message  key="ordine.label.esercizio" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
						<html:text styleId="testoNormale" property="buono.bilancio.codice1" size="10" readonly="${noinput}" ></html:text>
                        </td>
                        <td scope="col" class="etichetta" align="left">
                        <bean:message  key="ordine.label.capitolo" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" colspan="5" align="left">
						<html:text styleId="testoNormale" property="buono.bilancio.codice2" size="16" readonly="${noinput}" ></html:text>
                        </td>
                        </sbn:disableAll>
		     </tr>
        </c:when>
    </c:choose>

             <tr><td class="etichetta" colspan="8">&nbsp;</td></tr>

		     <tr>
                        <td scope="col" class="etichetta"  align="left">
                        <bean:message  key="buono.label.importo" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
						<html:text styleId="testoNormale" property="buono.importoStr" size="10"    readonly="true" ></html:text>
                        </td>
                        <td scope="col" class="etichetta" align="left">
                        <bean:message  key="buono.label.statoBuono" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" colspan="5" align="left">
                        <html:select  styleClass="testoNormale" property="buono.statoBuonoOrdine" disabled="true">
						<html:optionsCollection  property="listaStatoBuono" value="codice" label="descrizione" />
						</html:select>
                        </td>

		     </tr>
             <tr><td class="etichetta" colspan="8">&nbsp;</td></tr>

		     <tr><td class="etichetta" colspan="8">
                <bean:message  key="buono.label.totOrdini" bundle="acquisizioniLabels" />
				<html:text styleId="testoNormale" property="numOrdini" size="3" readonly="true"></html:text>
		     </td></tr>
             <tr><td class="etichetta" colspan="8">&nbsp;</td></tr>

		     <tr>
                  <td colspan="8">
				  	<table  align="center" width="100%"   style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"  >
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" align="center">
			                <bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
			                <bean:message  key="buono.label.anno" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
			                <bean:message  key="ricerca.label.codice" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
			                <bean:message  key="buono.label.tabStato" bundle="acquisizioniLabels" />
						</td>
						<td scope="col"  align="center"><bean:message  key="ordine.label.tabContinuativo" bundle="acquisizioniLabels" /></td>
						<td scope="col"  align="center">
			                <bean:message  key="ordine.label.bid" bundle="acquisizioniLabels" />
						</td>
						<td scope="col"  align="center">
			                <bean:message  key="ordine.label.tabTitolo" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
			                <bean:message  key="ordine.label.prezzo" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" width="2%" align="center"></td>

					</tr>
					<logic:greaterThan name="navForm" property="numOrdini" value="0">
						<logic:iterate id="elencaBuoni" property="elencaBuoni" 	name="navForm" indexId="indice">
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
					    	<sbn:disableAll checkAttivita="SCEGLI" inverted="true">
							<!-- controllo se è stato selezionata l'operazione di inserimento riga -->
			  				<html:hidden name="navForm" property="eleIns" value="${indice}" />
							<td scope="col" align="center">
								<html:select  style="width:40px;" styleClass="testoNormale"  name="elencaBuoni" indexed="true" property="tipoOrdine" >
								<html:optionsCollection  property="listaTipoOrdine" value="codice" label="descrizione" />
								</html:select>
							</td>
							<td  scope="col" align="center">
							<html:text styleId="testoNormale" property="annoOrdine" size="4" name="elencaBuoni" indexed="true"></html:text>
							</td>
							<td  scope="col" align="center">
							<html:text styleId="testoNormale" property="codOrdine" size="2" name="elencaBuoni" indexed="true" ></html:text>
							</td>
							<td align="center"><bean-struts:write  name="elencaBuoni" property="statoOrdine"/></td>
							<td  scope="col">
							<logic:equal  name="elencaBuoni" property="continuativo" value="true">
								<bean:message  key="acquisizioni.bottone.si" bundle="acquisizioniLabels" />
							</logic:equal>
							<logic:notEqual  name="elencaBuoni" property="continuativo" value="true">
								<bean:message  key="acquisizioni.bottone.no" bundle="acquisizioniLabels" />
							</logic:notEqual>
							</td>
							<td align="center"><bean-struts:write  name="elencaBuoni" property="titolo.codice"/></td>
							<td align="center"><bean-struts:write  name="elencaBuoni" property="titolo.descrizione"/></td>
							<td align="center"><bean-struts:write  name="elencaBuoni" property="prezzoEuroOrdineStr"   format="0.00"/></td>
							<bean-struts:define id="operazioneValue">
							  <bean-struts:write  name="indice" />
							</bean-struts:define>

							<td  class="testoNormale" align="center">
								<html:radio property="radioOrd" value="${operazioneValue}"></html:radio>
							</td>
							</sbn:disableAll>
						</tr></logic:iterate>
					</logic:greaterThan>
				  	<tr class="testoNormale"  >
						<td  colspan="9"  align="center">&nbsp;</td>

					</tr>
					</table>
				  </td>
			  </tr>
			  	<tr class="testoNormale">
					<td style="width:20px;">&nbsp;</td>
				</tr>
              </table>
		  </td>
		  </tr>
      </table>

 </div>
  <div id="divFooter">
		<c:choose>
			<c:when test="${navForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>

			<!-- tabella bottoni -->
           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
             <sbn:checkAttivita idControllo="SCEGLI">
				<html:submit styleClass="pulsanti" property="methodInserisciBuonoOrdine">
					<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
				</html:submit>
	 			<html:submit styleClass="pulsanti" property="methodInserisciBuonoOrdine">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
			</sbn:checkAttivita>


             <sbn:checkAttivita idControllo="SCEGLI" inverted="true">
					<html:submit styleClass="pulsanti" property="methodInserisciBuonoOrdine">
						<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodInserisciBuonoOrdine">
						<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
					</html:submit>
					<!--
					<html:submit styleClass="pulsanti" property="methodInserisciBuonoOrdine">
						<bean:message key="ricerca.button.cancella" bundle="acquisizioniLabels" />
					</html:submit>
					 -->
					<html:submit styleClass="pulsanti" property="methodInserisciBuonoOrdine">
						<bean:message key="ricerca.button.insRiga" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodInserisciBuonoOrdine">
						<bean:message key="ricerca.button.cancRiga" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodInserisciBuonoOrdine">
						<bean:message key="ricerca.button.completa" bundle="acquisizioniLabels" />
					</html:submit>
					<!--
					<html:submit styleClass="pulsanti" property="methodInserisciBuonoOrdine">
						<bean:message key="ricerca.button.stampa" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit styleClass="pulsanti" property="methodInserisciBuonoOrdine">
						<bean:message key="ricerca.button.chiudiBuono" bundle="acquisizioniLabels" />
					</html:submit>
					-->
					<html:submit styleClass="pulsanti" property="methodInserisciBuonoOrdine">
						<bean:message key="ricerca.button.ordine" bundle="acquisizioniLabels" />
					</html:submit>
		 			<html:submit styleClass="pulsanti" property="methodInserisciBuonoOrdine">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>
					<logic:equal  name="navForm" property="visibilitaIndietroLS" value="true">
						<html:submit  styleClass="pulsanti" property="methodInserisciBuonoOrdine">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>
					</logic:equal>
				</sbn:checkAttivita>
             </td>
             </tr>
      	  </table>
	  			<!-- fine tabella bottoni -->
    		</c:otherwise>
		</c:choose>

     	  </div>
	</sbn:navform>
</layout:page>
