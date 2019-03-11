<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput"  value="false"/>
<bean-struts:define id="noinputForn"  value="false"/>

<logic:notEqual  name="esaminaFatturaForm" property="datiFattura.fornitoreFattura.codice" value="">
		<bean-struts:define id="noinputForn"  value="true"/>
</logic:notEqual>

<c:choose>
<c:when test="${esaminaFatturaForm.disabilitaTutto}">
	<bean-struts:define id="noinput"  value="true"/>
	<bean-struts:define id="noinputForn"  value="true"/>
</c:when>
</c:choose>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/fatture/esaminaFattura.do" >
  <div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>

<bean-struts:define id="tipoNC"  value="false"/>
<logic:equal  name="esaminaFatturaForm" property="datiFattura.tipoFattura" value="N">
	<bean-struts:define id="tipoNC"  value="true"/>
</logic:equal>


             <table  width="100%"  align="center">
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="85%">
		<table  width="100%" border="0">
		     <tr>
                        <td  class="etichetta" width="10%" scope="col" align="left">
                  			<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:text styleId="testoNormale" name="esaminaFatturaForm" property="datiFattura.codBibl" size="4" readonly="true" ></html:text>
                        </td>
                        <td  width="10%" scope="col" class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.annoFatt" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:text styleId="testoNormale" name="esaminaFatturaForm" property="datiFattura.annoFattura" size="4" readonly="${noinput}"></html:text>
                        </td>
                        <td width="5%" scope="col" class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.progr" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col"  align="left">
							<html:text styleId="testoNormale" name="esaminaFatturaForm" property="datiFattura.progrFattura" size="4" readonly="true" ></html:text>
                        </td>
		     </tr>
		     <tr>
                        <td  class="etichetta" width="10%" scope="col" align="left">
                  			<bean:message  key="ricerca.label.dataReg" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:text styleId="testoNormale" name="esaminaFatturaForm" property="datiFattura.dataRegFattura" size="10" readonly="${noinput}" ></html:text>
                        </td>
                        <td  width="10%" scope="col" class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
				        <html:select  styleClass="testoNormale"  property="datiFattura.tipoFattura" style="width:40px;" onchange="this.form.submit();" disabled="${noinput}">
							<html:optionsCollection  property="listaTipoFatt" value="codice" label="descrizioneCodice" />
						</html:select>

                        </td>
		     </tr>
		     <tr>
                        <td  class="etichetta" width="10%" scope="col" align="left">
                  			<bean:message  key="ricerca.label.dataFatt" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:text styleId="testoNormale" name="esaminaFatturaForm" property="datiFattura.dataFattura" size="10" readonly="${noinput}"></html:text>
                        </td>
                        <td  width="10%" scope="col" class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.nr" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col"  align="left">
							<html:text styleId="testoNormale" name="esaminaFatturaForm" property="datiFattura.numFattura" size="4" readonly="${noinput}"></html:text>
                        </td>
		     </tr>

		     <tr>
                        <td  class="etichetta" width="10%" scope="col" align="left">
                  			<bean:message  key="buono.label.importo" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:text styleId="testoNormale" name="esaminaFatturaForm" property="datiFattura.importoFatturaStr" size="10" readonly="${noinput}"></html:text>
                        </td>
                        <td  width="10%" scope="col" class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.sconto" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" ><div align="left">
							<html:text styleId="testoNormale" name="esaminaFatturaForm" property="datiFattura.scontoFatturaStr" size="10" readonly="${noinput}"></html:text>
                        </td>
		     </tr>
		     <tr>
                        <td  class="etichetta" width="10%" scope="col" align="left">
                  			<bean:message  key="ordine.label.valuta" bundle="acquisizioniLabels" />
                        </td>
				        <td >
				          <html:select  styleClass="testoNormale"  property="valuta" style="width:50px;" disabled="${noinput}"  onchange="this.form.submit();" >
						  <html:optionsCollection  property="listaValuta" value="codice1" label="codice2" />
						  </html:select>
				        </td>
                        <td  width="10%" scope="col" class="etichetta" align="left">
                  			<bean:message  key="ordine.label.cambio" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col"  align="left">
    						<bean-struts:write format="0.00" name="esaminaFatturaForm" property="datiFattura.cambioFattura" />
                        </td>
                        <td  width="10%" scope="col" class="etichetta" align="left">
                  			<bean:message  key="ordine.label.stato" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="datiFattura.statoFattura"  disabled="true">
						<html:optionsCollection  property="listaStatoFatt" value="codice" label="descrizione" />
						</html:select>
                        </td>
		     </tr>
			 <tr>
                        <td class="etichetta"  scope="col" align="left">
                  			<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" colspan="7" align="left">
					 		<html:text styleId="testoNormale" property="datiFattura.fornitoreFattura.codice" size="5"  maxlength="10" readonly="${noinputForn}"></html:text>
	 						<html:text styleId="testoNormale" property="datiFattura.fornitoreFattura.descrizione" size="45" readonly="${noinputForn}"></html:text>
	                    	<html:submit  styleClass="buttonImage" property="methodEsaminaFattura" disabled="${noinputForn}">
								<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
							</html:submit>
						</td>
             </tr>

		     <tr>
                  <td colspan="8">
				  	<table  align="center" width="100%"  style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;" >

				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" align="center" style="width: 1%;">
                  			<bean:message  key="ricerca.label.numRiga" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" >
                  			<bean:message  key="ricerca.label.impUnitario" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" style="width: 3%;" >
                  			<bean:message  key="ricerca.label.sconto1" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" style="width: 3%;" >
                  			<bean:message  key="ricerca.label.sconto2" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" style="width: 2%;" >
                  			<bean:message  key="ricerca.label.iva" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" colspan="3" align="center" style="width: 30%;">
                  			<bean:message  key="ricerca.button.operazionesuordine" bundle="acquisizioniLabels" />
						</td>
						<c:choose>
						<c:when test="${esaminaFatturaForm.gestBil}">
							<td scope="col" colspan="3" align="center" style="width: 30%;">
	                  			<bean:message  key="buono.label.tabBilancio" bundle="acquisizioniLabels" />
							</td>
						</c:when>
						</c:choose>
						<td scope="col" align="center" style="width: 10%;" >
                  			<bean:message  key="ricerca.label.note" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" >
                  			<bean:message  key="ricerca.label.inven" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" width="2%" align="center" ></td>
					</tr>
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" align="center"></td>
						<td scope="col" align="center"></td>
						<td scope="col" align="center"></td>
						<td scope="col" align="center"></td>
						<td scope="col" align="center" ></td>
						<td scope="col" align="center" >
                  			<bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" >
                  			<bean:message  key="buono.label.anno" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" >
                  			<bean:message  key="ricerca.label.num" bundle="acquisizioniLabels" />
						</td>
						<c:choose>
						<c:when test="${esaminaFatturaForm.gestBil}">
							<td scope="col" align="center" >
	                  			<bean:message  key="ricerca.label.ese" bundle="acquisizioniLabels" />
							</td>
							<td scope="col" align="center" >
	                  			<bean:message  key="ricerca.label.cap" bundle="acquisizioniLabels" />
							</td>
							<td scope="col" align="center" >
	                  			<bean:message  key="ricerca.label.impCorto" bundle="acquisizioniLabels" />
							</td>
						</c:when>
						</c:choose>
						<td scope="col" align="center"></td>
						<td scope="col" align="center"></td>
						<td scope="col" width="2%" align="center"></td>
					</tr>



		<logic:greaterThan name="esaminaFatturaForm" property="numRigheFatt" value="0">
			<logic:iterate id="elencaRigheFatt" property="elencaRigheFatt" name="esaminaFatturaForm" indexId="indice">
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
					<!-- controllo se è stato selezionata l'operazione di inserimento riga (non altre spese) -->
				  	<tr class="testoNormale" bgcolor="${color}">
						<td  scope="col" align="center">
<!--						<html:text styleId="testoNormale" indexed="true"  name="elencaRigheFatt" property="rigaFattura" size="1" readonly="true" ></html:text>-->
							<html:text styleId="testoNormale" indexed="true"  name="elencaRigheFatt" property="progrRigaFattura" size="1" readonly="true" ></html:text>
						</td>
						<td  scope="col" align="center">
							<html:text styleId="testoNormale"  indexed="true" name="elencaRigheFatt" property="importoRigaFatturaStr" size="5" readonly="${noinput}"></html:text>
						</td>
						<td  scope="col" align="center">
							<html:text styleId="testoNormale"  indexed="true" name="elencaRigheFatt" property="sconto1RigaFatturaStr" size="2" readonly="${noinput}"></html:text>
						</td>
						<td  scope="col" align="center">
							<html:text styleId="testoNormale" indexed="true" name="elencaRigheFatt" property="sconto2RigaFatturaStr" size="2" readonly="${noinput}"></html:text>
						</td>
						<td scope="col" align="center">
							<html:select  styleClass="testoNormale"  indexed="true" name="elencaRigheFatt"  property="codIvaRigaFattura" style="width:40px;" disabled="${noinput}">
							<html:optionsCollection  property="listaIva" value="codice" label="descrizioneCodice" />
							</html:select>
						</td>
						<logic:equal name="elencaRigheFatt" property="codPolo"  value="*" >
							<td scope="col" align="center">
								<html:select  styleClass="testoNormale" indexed="true" name="elencaRigheFatt"  property="ordine.codice1" style="width:40px;" disabled="true">
								<html:optionsCollection  property="listaTipoOrdine" value="codice" label="descrizioneCodice" />
								</html:select>
							</td>
							<td  scope="col" align="center">
								<html:text styleId="testoNormale" name="elencaRigheFatt" indexed="true" property="ordine.codice2" size="4" readonly="true" ></html:text>
							</td>
							<td  scope="col" align="center">
								<html:text styleId="testoNormale" name="elencaRigheFatt" indexed="true" property="ordine.codice3" size="4" readonly="true" ></html:text>
								<input type="submit"  class="buttonImage"  name="bottoneOrdine" value="${indice}" title="ricerca ordini" disabled="true"></input>
							</td>
						</logic:equal>
						<logic:notEqual  name="elencaRigheFatt" property="codPolo"  value="*" >
							<td scope="col" align="center">
								<html:select  styleClass="testoNormale" indexed="true" name="elencaRigheFatt"  property="ordine.codice1" style="width:40px;" disabled="${noinput}">
								<html:optionsCollection  property="listaTipoOrdine" value="codice" label="descrizioneCodice" />
								</html:select>
							</td>
							<td  scope="col" align="center">
								<html:text styleId="testoNormale" name="elencaRigheFatt" indexed="true" property="ordine.codice2" size="4" readonly="${noinput}" ></html:text>
							</td>
							<td  scope="col" align="center">
								<html:text styleId="testoNormale" name="elencaRigheFatt" indexed="true" property="ordine.codice3" size="4" readonly="${noinput}" ></html:text>
								<c:choose>
								<c:when test="${esaminaFatturaForm.disabilitaTutto}">
									<input type="submit"  class="buttonImage"  name="bottoneOrdine" value="${indice}" title="ricerca ordini" disabled="disabled"></input>
								</c:when>
								<c:otherwise>
									<input type="submit"  class="buttonImage"  name="bottoneOrdine" value="${indice}" title="ricerca ordini" ></input>
								</c:otherwise>
								</c:choose>
							</td>
						</logic:notEqual>
						<c:choose>
						<c:when test="${esaminaFatturaForm.gestBil}">
							<td  scope="col" align="center">
								<html:text styleId="testoNormale" name="elencaRigheFatt" indexed="true" property="bilancio.codice1" size="4" readonly="${noinput}" ></html:text>
							</td>
							<td  scope="col" align="center">
								<html:text styleId="testoNormale" name="elencaRigheFatt" indexed="true" property="bilancio.codice2" size="4" maxlength="16" readonly="${noinput}" ></html:text>
							</td>
							<td  scope="col" align="center">
								<html:select  styleClass="testoNormale" name="elencaRigheFatt" indexed="true"  property="bilancio.codice3" style="width:40px;" disabled="${noinput}">
								<html:optionsCollection  property="listaTipoImpegno" value="codice" label="descrizioneCodice" />
								</html:select>
								<c:choose>
								<c:when test="${esaminaFatturaForm.disabilitaTutto}">
									<input type="submit"   class="buttonImage"  name="bottoneBilancio" value="${indice}" title="ricerca bilancio"  disabled="disabled"></input>
								</c:when>
								<c:otherwise>
									<input type="submit"   class="buttonImage"  name="bottoneBilancio" value="${indice}" title="ricerca bilancio"></input>
								</c:otherwise>
								</c:choose>
							</td>
						</c:when>
						</c:choose>
						<td  scope="col" align="center">
						  <bean-struts:define id="varLink" name="elencaRigheFatt" property="rigaFattura"  />
							<c:choose>
							<c:when test="${esaminaFatturaForm.disabilitaTutto}">
								<input type="submit"  class="buttonImageNote"  name="tagNote" value="${varLink}" title="note della fattura" disabled="disabled"></input>
							</c:when>
							<c:otherwise>
								<input type="submit"  class="buttonImageNote"  name="tagNote" value="${varLink}" title="note della fattura" ></input>
							</c:otherwise>
							</c:choose>
						</td>
						<td  scope="col" align="center">
<!-- 						  <html:link action="/acquisizioni/fatture/*.do" ><img border="0"   alt="legami inventario"  src='<c:url value="/images/inv.GIF" />'/></html:link>-->
<!--							<input type="submit"  class="buttonImageInv"  name="tagInv" value="" title="legami inventario"></input>-->
             <sbn:checkAttivita idControllo="GESTIONE">
								<!-- tck 3714-->
							  <logic:equal name="elencaRigheFatt" property="codPolo"  value="*" >
<!--										<input type="submit"  class="buttonImageInv"  name="bottoneInventari" value="${indice}" title="ricerca inventari" disabled="disabled"></input>-->
							   </logic:equal>
							   <logic:notEqual  name="elencaRigheFatt" property="codPolo"  value="*" >

									<c:choose>
									<c:when test="${esaminaFatturaForm.disabilitaTutto}">
										<input type="submit"  class="buttonImageInv"  name="bottoneInventari" value="${indice}" title="ricerca inventari" disabled="disabled"></input>
									</c:when>
									<c:otherwise>
										<input type="submit"  class="buttonImageInv"  name="bottoneInventari" value="${indice}" title="ricerca inventari" ></input>
									</c:otherwise>
									</c:choose>
							   </logic:notEqual>

             </sbn:checkAttivita>

						</td>
							<bean-struts:define id="operazioneValue">
							  <bean-struts:write  name="indice" />
							</bean-struts:define>
						<td  class="testoNormale" align="center">
							<html:radio property="radioRFatt" value="${operazioneValue}"></html:radio>
						</td>
					</tr>
		<c:choose>
			<c:when test="${tipoNC}">
				  	<tr class="testoNormale" bgcolor="${color}">
						<td  scope="col" align="center" colspan="5">
 							<bean:message  key="ricerca.label.fattura" bundle="acquisizioniLabels" />
                  			&nbsp;(<bean:message  key="buono.label.anno" bundle="acquisizioniLabels" />
                  			&nbsp;<bean:message  key="ricerca.label.progr" bundle="acquisizioniLabels" />
                  			&nbsp;<bean:message  key="ricerca.label.numR" bundle="acquisizioniLabels" />)
						</td>
						<td  scope="col" align="center">
							<html:text styleId="testoNormale" name="elencaRigheFatt" indexed="true" property="fattura.codice1" size="4" readonly="${noinput}" ></html:text>
						</td>
						<td  scope="col" align="center">
							<html:text styleId="testoNormale" name="elencaRigheFatt" indexed="true" property="fattura.codice2" size="4" readonly="${noinput}" ></html:text>
						</td>
						<td  scope="col" align="center">
							<html:text styleId="testoNormale" name="elencaRigheFatt" indexed="true" property="fattura.codice3" size="4" readonly="${noinput}" ></html:text>
							<c:choose>
							<c:when test="${esaminaFatturaForm.disabilitaTutto}">
	  							  <input type="submit"  class="buttonImage"  name="bottoneFattura" value="${indice}" title="ricerca fattura" disabled="disabled"></input>
							</c:when>
							<c:otherwise>
	  							  <input type="submit"  class="buttonImage"  name="bottoneFattura" value="${indice}" title="ricerca fattura"></input>
							</c:otherwise>
							</c:choose>

						</td>
						<c:choose>
						<c:when test="${esaminaFatturaForm.gestBil}">
							<td  scope="col" align="center" colspan="3">
							</td>
						</c:when>
						</c:choose>
						<td  scope="col" align="center" colspan="2">
						</td>
						<td  scope="col" align="center" >
						</td>

					</tr>

			</c:when>
		</c:choose>

					<c:choose>
						<c:when test="${esaminaFatturaForm.clicNotaPrg == varLink }">
							  	<tr class="testoNormale" bgcolor="${color}" >
									<td colspan="14" scope="col" align="center" >
									  <bean-struts:define id="varLink"  value=""  />
			                  			<bean:message  key="ricerca.label.note" bundle="acquisizioniLabels" />
										<html:textarea styleId="testoNormale"  name="elencaRigheFatt" indexed="true"  property="noteRigaFattura" rows="1" cols="100"   ></html:textarea>
										<sbn:tastiera limit="80" name="esaminaFatturaForm"  property="elencaRigheFatt[${indice}].noteRigaFattura"></sbn:tastiera>
									</td>
								</tr>
						</c:when>
					</c:choose>
			</logic:iterate>
		</logic:greaterThan>

					</table>
				  </td>
			  </tr>
              </table>
		</td>
        </tr>
        </table>
	     <!-- FINE  tabella corpo -->
	 </div>

  <div id="divFooter">
		<c:choose>
			<c:when test="${esaminaFatturaForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>

			<!-- tabella bottoni -->
           <table align="left"  border="0" width="100%" >

            <tr>
             <td align="center">
				<c:choose>
					<c:when test="${!esaminaFatturaForm.disabilitaTutto}">
             <sbn:checkAttivita idControllo="GESTIONE">

					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
					</html:submit>
					<!--
					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.insRiga" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.altreSpese" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.cancRiga" bundle="acquisizioniLabels" />
					</html:submit>
             		-->
					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.controllaordine" bundle="acquisizioniLabels" />
					</html:submit>

             </sbn:checkAttivita>

					<c:choose>
						<c:when test="${tipoNC}">
             <sbn:checkAttivita idControllo="GESTIONE">

								<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
									<bean:message key="ricerca.button.contabilizzaNotaCredito" bundle="acquisizioniLabels" />
								</html:submit>
								<!--
			                   	<html:submit  styleClass="pulsanti" property="methodEsaminaFattura">
									<bean:message  key="ricerca.label.fattura" bundle="acquisizioniLabels" />
								</html:submit>
								-->
			 </sbn:checkAttivita>

						</c:when>
						<c:otherwise>
             <sbn:checkAttivita idControllo="GESTIONE">
					 			<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
									<bean:message key="ricerca.label.listaNoteC" bundle="acquisizioniLabels" />
								</html:submit>

					 			<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
									<bean:message key="ricerca.label.ordinePagamento" bundle="acquisizioniLabels" />
								</html:submit>
             </sbn:checkAttivita>
			    		</c:otherwise>
					</c:choose>
				</c:when>
				</c:choose>
				<!--
				<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.controllaordine" bundle="acquisizioniLabels" />
					</html:submit>

				<logic:equal name="esaminaFatturaForm" property="datiFattura.statoFattura" value="2">
							<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
								<bean:message key="ricerca.button.ordinePagam" bundle="acquisizioniLabels" />
							</html:submit>
				</logic:equal>

				-->
				<c:choose>
					<c:when test="${!esaminaFatturaForm.disabilitaTutto}">

             <sbn:checkAttivita idControllo="GESTIONE">

					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.cancella" bundle="acquisizioniLabels" />
					</html:submit>
					<!--
		 			<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="buono.label.tabBilancio" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.ordine" bundle="acquisizioniLabels" />
					</html:submit>
					-->
					<!--
		 			<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.label.listaNoteC" bundle="acquisizioniLabels" />
					</html:submit>

					-->
             </sbn:checkAttivita>

					</c:when>
				</c:choose>
					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.stampa" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.label.listaInv" bundle="acquisizioniLabels" />
					</html:submit>


             </td>
             </tr>
            <tr>
             <td align="center" >
				<c:choose>
					<c:when test="${!esaminaFatturaForm.disabilitaTutto}">
             <sbn:checkAttivita idControllo="GESTIONE">
					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.insRiga" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.altreSpese" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.cancRiga" bundle="acquisizioniLabels" />
					</html:submit>
             	</sbn:checkAttivita>
				</c:when>
				</c:choose>
					<logic:equal name="esaminaFatturaForm" property="enableScorrimento" value="true">
								<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
									<bean:message key="ricerca.button.scorriIndietro" bundle="acquisizioniLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
									<bean:message key="ricerca.button.scorriAvanti" bundle="acquisizioniLabels" />
								</html:submit>
					</logic:equal>

		 			<html:submit styleClass="pulsanti" property="methodEsaminaFattura">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>

             </td>
			</tr>

      	  </table>
	  			<!-- fine tabella bottoni -->
    		</c:otherwise>
		</c:choose>
	  			<!-- fine tabella bottoni -->

     	  </div>
	</sbn:navform>
</layout:page>
