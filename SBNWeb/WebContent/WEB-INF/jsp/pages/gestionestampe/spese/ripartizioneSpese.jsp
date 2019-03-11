<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>


<html:xhtml />
<layout:page>
	    <sbn:navform  action="gestionestampe/spese/ripartizioneSpese.do">

<div id="divForm">
		<div id="divMessaggio"><sbn:errors  /></div>

	 		<!-- TABELLA DI TAG: da eliminare integralmente se non presenti -->
<!-- -->
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>

						<td style="width: auto; height: 40px;"  class="schedaOn">
						<div align="center">
							<bean:message  key="ordine.label.statisticheContabili" bundle="acquisizioniLabels" />
						</div>
						</td>

						 <sbn:checkAttivita  idControllo="STAMPASTAT">
						<td style="width: auto;" class="schedaOff">
							<div align="center">
				            <html:link  action="/gestionestampe/spese/statisticheTempi.do"  >
								<bean:message  key="ordine.label.statisticheTempi" bundle="acquisizioniLabels" />
				            </html:link>
				            </div>
						</td>
						</sbn:checkAttivita>

            	 </tr>
          		 </table>

	<table   width="100%"  align="center" border="0" >
					<!--  biblioteca-->
			<tr>
					<td colspan="4">
					<div class="etichetta"><bean:message
						key="ricerca.label.biblioteca" bundle="gestioneStampeLabels" />
					<html:text disabled="true" styleId="testoNormale" property="codBib"
						size="5" maxlength="3"></html:text> <span disabled="true">
						<html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodRipartizioneSpese" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
						</html:submit>
						<bean-struts:write	name="ripartizioneSpeseForm" property="descrBib" />

					</span> </div>
					</td>
<!--					<td  colspan="2" scope="col" align="center">&nbsp;</td>-->

			</tr>
		     <tr >
		    			<td  colspan="4" scope="col" align="center">&nbsp;</td>
			</tr>
		     <tr >
                        <td width="10%" valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="ricerca.label.annoOrdine" bundle="acquisizioniLabels" />
						</div></td>
						<!--	onchange="this.form.submit();"-->
                        <td width="40%" valign="top" scope="col" align="left">
				 		  	<html:text styleId="testoNormale" property="annoOrdine" size="4" ></html:text>
                        </td>
<!--						<td  colspan="2" scope="col" align="center">&nbsp;</td>-->
  <%--                    <td style="font-style: italic;background:#ffffcc;"   width="5%"  scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="ricerca.label.tipoOrdineBis" bundle="acquisizioniLabels" /></div></td>
                        <td  scope="col" width="55%" ><div align="left" >

				          <html:select  styleClass="testoNormale"  property="tipoOrdine" >
						  <html:optionsCollection  property="listaTipoOrdine" value="codice" label="descrizione" />
						  </html:select>
                        </div></td> --%>

             </tr>

		     <tr>
                        <td scope="col"  align="left" class="etichetta">
                        	<bean:message  key="ricerca.label.dataOrdineDa" bundle="acquisizioniLabels" />
                        </td>
                        <td>
				 		  	<html:text styleId="testoNormale" property="dataOrdineDa" size="10" ></html:text>
							&nbsp;
                        	<bean:message  key="ricerca.label.dataOrdineA" bundle="acquisizioniLabels" />
							&nbsp;
				 		  <html:text styleId="testoNormale" property="dataOrdineA" size="10"></html:text>
                        	<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />
                        </td>
<!--						<td  colspan="2" scope="col" align="center">&nbsp;</td>-->
					    <td class="etichetta" style="font-style: italic;background:#ffffcc;">
					    <bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
					    </td>
                        <td  valign="top"  scope="col" align="left" >
				 		  <html:text styleId="testoNormale" property="codFornitore" size="5"  maxlength="10" ></html:text>
						  <bean-struts:write	name="ripartizioneSpeseForm" property="fornitore" />

                        <html:submit  styleClass="buttonImage" property="methodRipartizioneSpese" >
							<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
						</html:submit>
                        </td>
			</tr>
<!--
		     <tr>
                       <td  width="15%"  scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="ricerca.label.tipoOrdineBis" bundle="acquisizioniLabels" /></div></td>
                        <td  scope="col" ><div align="left">

				          <html:select  styleClass="testoNormale"  property="tipoOrdine" >
						  <html:optionsCollection  property="listaTipoOrdine" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
						<td  colspan="2" scope="col" align="center">&nbsp;</td>


		     </tr>
		     <tr >
					    <td class="etichetta" >
					    <bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
					    </td>
                        <td valign="top"  scope="col" align="left" >
				 		  <html:text styleId="testoNormale" property="codFornitore" size="5"  maxlength="10" ></html:text>
						  <bean-struts:write	name="ripartizioneSpeseForm" property="fornitore" />

                        <html:submit  styleClass="buttonImage" property="methodRipartizioneSpese" >
							<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
						</html:submit>
                        </td>
						<td  colspan="2" scope="col" align="center">&nbsp;</td>

            </tr>

		     <tr >

                        <td  scope="col">
						<div style="text-align: left;" class="etichetta">
						<bean:message  key="ordine.label.natura" bundle="acquisizioniLabels" />
                        </div>
                        </td>
                        <td  scope="col"><div align="left" class="etichetta">
                        <html:select style="width:40px" styleClass="testoNormale"   property="natura" >
						  <html:optionsCollection  property="listaNatura" value="codice" label="descrizioneCodice" />
						  </html:select></div>
						</td>
						<td  colspan="2" scope="col" align="center">&nbsp;</td>

            </tr>

		     -->
		     <tr >

                        <td   scope="col" ><div style="text-align: left;"  class="etichetta"><bean:message  key="ordine.label.sezione" bundle="acquisizioniLabels" /></div></td>
                        <td scope="col" >
                        <div align="left" >
              			<html:text styleId="testoNormale" property="sezione" size="7" maxlength="7"  ></html:text>
						<bean-struts:write	name="ripartizioneSpeseForm" property="testoListaSezioni" />
                        <html:submit  title="elenco" styleClass="buttonImage" property="methodRipartizioneSpese"   >
							<bean:message  key="ricerca.label.sezione" bundle="acquisizioniLabels" />
						</html:submit>
                        </div>
                        </td>
<!--						<td  colspan="2" scope="col" align="center">&nbsp;</td>-->
                       <td style="font-style: italic;background:#ffffcc;" scope="col">
						<div style="text-align: left;" class="etichetta">
						<bean:message  key="ordine.label.natura" bundle="acquisizioniLabels" />
                        </div>
                        </td>
                        <td  scope="col"><div align="left" class="etichetta">
                        <html:select style="width:40px" styleClass="testoNormale"   property="natura" >
						  <html:optionsCollection  property="listaNatura" value="codice" label="descrizioneCodice" />
						  </html:select></div>
						</td>
            </tr>
		     <tr>
                        <td scope="col"  align="left" class="etichetta">
                        	<bean:message  key="ordine.label.esercizio" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
                        <html:text styleId="testoNormale" property="esercizio" size="4" ></html:text>
                        <!--
                        </td>
                        <td  scope="col" align="left" class="etichetta">
                        -->
                        &nbsp;&nbsp;
                        <bean:message  key="ordine.label.capitolo" bundle="acquisizioniLabels" />
                        <html:text styleId="testoNormale" property="capitolo" size="16" maxlength="16" ></html:text>
                        <!--
                        </td>
                        <td  scope="col" align="left" colspan="3" class="etichetta">
			           	-->
			           	&nbsp;&nbsp;
			           	<bean:message  key="ordine.label.tipoImpegno" bundle="acquisizioniLabels" />
						<html:select style="width:40px" styleClass="testoNormale" property="tipoImpegno" >
						<html:optionsCollection  property="listaTipoImpegno" value="codice" label="descrizioneCodiceACQ" />
						</html:select>
                        <html:submit  styleClass="buttonImage" property="methodRipartizioneSpese" >
							<bean:message  key="ordine.label.bilancio" bundle="acquisizioniLabels" />
						</html:submit>
					  </td>
<!--						<td  colspan="2" scope="col" align="center">&nbsp;</td>-->
                        <td  scope="col" style="font-style: italic;background:#ffffcc;">
						<div style="text-align: left;" class="etichetta">
						<bean:message  key="ordine.label.tipoMateriale" bundle="acquisizioniLabels" />
                        </div>
                        </td>
                        <td  scope="col"><div align="left" class="etichetta">
                        <html:select style="width:50px" styleClass="testoNormale"   property="tipoMaterialeInv" >
						  <html:optionsCollection  property="listaTipoMaterialeInv" value="codice" label="descrizioneCodice" />
						  </html:select></div>
						</td>
		     </tr>
		     <!--
		     <tr >

                        <td  scope="col">
						<div style="text-align: left;" class="etichetta">
						<bean:message  key="ordine.label.tipoMateriale" bundle="acquisizioniLabels" />
                        </div>
                        </td>
                        <td  scope="col"><div align="left" class="etichetta">
                        <html:select style="width:50px" styleClass="testoNormale"   property="tipoMaterialeInv" >
						  <html:optionsCollection  property="listaTipoMaterialeInv" value="codice" label="descrizioneCodice" />
						  </html:select></div>
						</td>
						<td  colspan="2" scope="col" align="center">&nbsp;</td>

            </tr>
		     -->
		     <tr >

						<td  colspan="2" scope="col" align="center">&nbsp;</td>

                        <td style="font-style: italic;background:#ffffcc;" scope="col">
						<div style="text-align: left;" class="etichetta">
						<bean:message  key="ordine.label.supporto" bundle="acquisizioniLabels" />
                        </div>
                        </td>
                        <td  scope="col"><div align="left" class="etichetta">
                        <html:select style="width:50px" styleClass="testoNormale"   property="supporto" >
						  <html:optionsCollection property="listaSupporto" value="codice" label="descrizioneCodice" />
						  </html:select></div>
						</td>

            </tr>
		     <tr >

						<td  colspan="2" scope="col" align="center">&nbsp;</td>

                        <td style="font-style: italic;background:#ffffcc;" scope="col">
						<div style="text-align: left;" class="etichetta">
						<bean:message  key="ordine.label.tipoRecord" bundle="acquisizioniLabels" />
                        </div>
                        </td>
                        <td  scope="col"><div align="left" class="etichetta">
                        <html:select style="width:50px" styleClass="testoNormale"   property="tipoRecord" >
						  <html:optionsCollection property="listaTipoRecord" value="codice" label="descrizioneCodice" />
						  </html:select>
						</div>
						</td>

            </tr>
 			<tr >
            			<td  colspan="2" scope="col" align="center">&nbsp;</td>

                        <td style="font-style: italic;background:#ffffcc;" scope="col">
						<div style="text-align: left;" class="etichetta">
						<bean:message  key="button.classific" bundle="acquisizioniLabels" />
                        </div>
                        </td>
                        <td  scope="col"><div align="left" class="etichetta">
                        <html:select  styleClass="testoNormale"   property="rangeDewey" >
						  <html:optionsCollection property="listaRangeDewey" value="codice" label="descrizione" />
						  </html:select>
                        </div>
                        <!--
                         <html:submit  title="elenco" styleClass="buttonImage" property="methodRipartizioneSpese"   >
							<bean:message  key="button.classific" bundle="acquisizioniLabels" />
						</html:submit>
						-->
						<td>
            </tr>
		     <tr>
            			<td  colspan="2" scope="col" align="center">&nbsp;</td>

                        <td style="font-style: italic;background:#ffffcc;" scope="col">
						<div style="text-align: left;" class="etichetta">
		                   	<bean:message  key="ricerca.label.lingua" bundle="acquisizioniLabels" />
                        </div>
                        </td>
                        <td  scope="col"><div align="left" class="etichetta">
							<html:select name="ripartizioneSpeseForm"  style="width:60px;" styleClass="testoNormale"  property="lingue" >
							<html:optionsCollection  property="listaLingue" value="codice" label="descrizioneCodice" />
							</html:select>
                        </div>
						<td>
              </tr>
		     <tr>
            			<td  colspan="2" scope="col" align="center">&nbsp;</td>

                        <td style="font-style: italic;background:#ffffcc;" scope="col">
						<div style="text-align: left;" class="etichetta">
		                   	<bean:message  key="ricerca.label.paese" bundle="acquisizioniLabels" />
                        </div>
                        </td>
                        <td  scope="col"><div align="left" class="etichetta">
							<html:select name="ripartizioneSpeseForm"   styleClass="testoNormale"  property="paesi" >
							<html:optionsCollection  property="listaPaesi" value="codice" label="descrizione" />
							</html:select>
                        </div>
						<td>
              </tr>



		     <tr>
						  <td colspan="4"   class="etichetta" >
						  <div align="left" class="etichetta">
				         	<bean:message key="ordine.label.ordiniNOinv" bundle="acquisizioniLabels" />
					        <html:radio property="ordiniNOinv" value="1" />
                       	  </div>
                       	  </td>
              </tr>
		     <tr >
		    			<td  colspan="4" scope="col" align="center">&nbsp;</td>
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
						</div>
						</td>
                        <td  valign="top" scope="col" align="left">
						<%--html:select  styleClass="testoNormale"  property="provincia"--%>
						<html:select  styleClass="testoNormale"  property="tipoOrdinamSelez" style="width:200px">
						<html:optionsCollection  property="listaTipiOrdinamento" value="codice" label="descrizione" />
						</html:select>
                        </td>

						<bean-struts:size id="comboSize" name="ripartizioneSpeseForm" property="elencoModelli" />
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
							<html:hidden property="tipoModello" value="${ripartizioneSpeseForm.elencoModelli[0].jrxml}" />
							</td>
						</logic:lessThan>
             </tr>

	     </table>
	     	<HR>
			<jsp:include flush="true" page="../common/tipoStampa.jsp" />
			<HR>

</div>
<div id="divFooter">

	<table align="center" border="0" style="height:40px" >
		<tr >
			<td>
				<html:submit styleClass="pulsanti" property="methodRipartizioneSpese">
				<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
				</html:submit>

				<html:submit styleClass="pulsanti" property="methodRipartizioneSpese">
					<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
				</html:submit>


			</td>
		</tr>
	</table>
</div>
</sbn:navform>
</layout:page>
