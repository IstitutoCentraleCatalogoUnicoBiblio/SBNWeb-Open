<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/gestionestampe/spese/statisticheTempi.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /><sbn:errors />
		</div>

	 		<!-- TABELLA DI TAG: da eliminare integralmente se non presenti -->
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>

		            <sbn:checkAttivita  idControllo="STAMPASPESE">
						<td style="width: auto; height: 40px;"  class="schedaOff">
						<div align="center">
				            <html:link  action="/gestionestampe/spese/ripartizioneSpese.do" >
								<bean:message  key="ordine.label.statisticheContabili" bundle="acquisizioniLabels" />
				            </html:link>
						</div>
						</td>
					</sbn:checkAttivita>
						<td style="width: auto;" class="schedaOn">
							<div align="center">
								<bean:message  key="ordine.label.statisticheTempi" bundle="acquisizioniLabels" />
				            </div>
						</td>
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
						<html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodStatisticheTempi" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
						</html:submit>
						<bean-struts:write	name="statisticheTempiForm" property="descrBib" />

					</span> </div>
					</td>
				</tr>
		     <tr >
		    			<td  colspan="4" scope="col" align="center">&nbsp;</td>
			</tr>

		     <tr >
                        <td  valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="ricerca.label.annoOrdine" bundle="acquisizioniLabels" />
						</div></td>
						<!--	onchange="this.form.submit();"-->
                        <td  valign="top" scope="col" align="left">
				 		  	<html:text styleId="testoNormale" property="annoOrdine" size="4" ></html:text>
                        </td>
						<td  colspan="2" scope="col" align="center">&nbsp;</td>

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
						<td  colspan="2" scope="col" align="center">&nbsp;</td>

			</tr>

		 <%--    <tr>
                       <td  width="15%" valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="ricerca.label.tipoOrdineBis" bundle="acquisizioniLabels" /></div></td>
                        <td   valign="top" scope="col" ><div align="left">

				          <html:select  styleClass="testoNormale"  property="tipoOrdine" >
						  <html:optionsCollection  property="listaTipoOrdine" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
						<td  colspan="2" scope="col" align="center">&nbsp;</td>


		     </tr> --%>
		     <tr>
						  <td colspan="4"   class="etichetta" align="right">
						  <div align="left" class="etichetta">
				         	<bean:message key="ordine.label.statisticheTempiAccessionamento" bundle="acquisizioniLabels" />
					        <html:radio property="tipoRicerca" value="accessionamento" />
 	                      	&nbsp;
 	                      	<bean:message key="ordine.label.statisticheTempiLavorazione" bundle="acquisizioniLabels" />
	        				<html:radio property="tipoRicerca" value="lavorazione" />
                       	  </div>
                       	  </td>
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
 						<bean-struts:size id="comboSize" name="statisticheTempiForm" property="elencoModelli" />
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
							<html:hidden property="tipoModello" value="${statisticheTempiForm.elencoModelli[0].jrxml}" />
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
				<html:submit styleClass="pulsanti" property="methodStatisticheTempi">
				<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
				</html:submit>

				<html:submit styleClass="pulsanti" property="methodStatisticheTempi">
					<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
				</html:submit>
			</td>
		</tr>
	</table>
  </div>
</sbn:navform>
</layout:page>
