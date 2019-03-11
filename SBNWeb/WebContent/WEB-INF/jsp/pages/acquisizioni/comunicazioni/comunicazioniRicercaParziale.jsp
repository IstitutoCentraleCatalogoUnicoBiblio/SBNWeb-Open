<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<logic:equal  name="comunicazioniRicercaParzialeForm" property="tipoDocumento" value="O">
	<bean-struts:define id="noinputFatt"  value="true"/>
	<bean-struts:define id="noinputOrd"  value="false"/>
</logic:equal>
<logic:equal  name="comunicazioniRicercaParzialeForm" property="tipoDocumento" value="F">
	<bean-struts:define id="noinputFatt"  value="false"/>
	<bean-struts:define id="noinputOrd"  value="true"/>
</logic:equal>
<logic:equal  name="comunicazioniRicercaParzialeForm" property="tipoDocumento" value="">
	<bean-struts:define id="noinputFatt"  value="true"/>
	<bean-struts:define id="noinputOrd"  value="true"/>
</logic:equal>

<bean-struts:define id="noinput"  value="false"/>
<c:choose>
<c:when test="${comunicazioniRicercaParzialeForm.disabilitaTutto}">
      <script type="text/javascript">
		<bean-struts:define id="noinput"  value="true"/>
      </script>

</c:when>
</c:choose>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/comunicazioni/comunicazioniRicercaParziale.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>

		  <table    width="100%"  align="center"  >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">
		  <table   width="100%" border="0"  >
		     <tr><td colspan="7">&nbsp;</td></tr>
		     <tr>
                        <td  scope="col" class="etichetta" align="left">
		                   	<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
                        </td>
						<td style="width:2%;">&nbsp;</td>
                        <td scope="col" align="left">
							<html:text styleId="testoNormale" property="codBibl" size="3" readonly="true"></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodComunicazioniRicercaParziale" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>
                        </td>
		     </tr>
             <tr><td class="etichettaIntestazione" colspan="7">&nbsp;</td></tr>

		     <tr>
 						<td  class="etichetta" scope="col" align="left">
 							<bean:message  key="ricerca.label.tipoDoc" bundle="acquisizioniLabels" />
 						</td>
						<td style="width:2%;">&nbsp;</td>
						<td  class="etichetta" scope="col" align="left">
							<html:select styleClass="testoNormale" property="tipoDocumento" onchange="this.form.submit();" disabled="${noinput}" >
							<html:optionsCollection  property="listaTipoDocumento" value="codice" label="descrizione" />
							</html:select>
						</td>
						<td  class="etichetta" colspan="2" align="left">
 							<bean:message  key="ricerca.label.direzione" bundle="acquisizioniLabels" />
							<html:select styleClass="testoNormale" property="direzioneComunicazione" onchange="this.form.submit();" disabled="${noinput}">
							<html:optionsCollection  property="listaDirezioneComunicazione" value="codice" label="descrizione" />
							</html:select>
						</td>
						<td  class="etichetta" width="15%" scope="col" align="left">
						Stato
						</td>
                        <td  scope="col" align="left">
							<html:select styleClass="testoNormale"  property="statoComunicazione" >
							<html:optionsCollection  property="listaStatoComunicazione" value="codice" label="descrizione" />
							</html:select>
                        </td>

               </tr>
		     <tr>
 						<td  class="etichetta" scope="col" align="left">
 							<bean:message  key="ricerca.label.tipoMsg" bundle="acquisizioniLabels" />
 						</td>
						<td >&nbsp;</td>
						<td  class="etichetta" scope="col" align="left">
							<html:select styleClass="testoNormale" property="tipoMessaggio"  disabled="${noinput}">
							<html:optionsCollection  property="listaTipoMessaggio" value="codice" label="descrizione" />
							</html:select>
						</td>
						<td  class="etichetta" colspan="2" align="left">
 							<bean:message  key="ricerca.label.codMsg" bundle="acquisizioniLabels" />
							<html:text styleId="testoNormale" property="codMessaggio" size="5" ></html:text>
                        </td>

               </tr>
		     <tr>
 						<td  class="etichetta" scope="col" align="left">
 							<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
 						</td>
						<td >&nbsp;</td>
                        <td  scope="col" colspan="4" align="left">
							<html:text styleId="testoNormale" property="codFornitore" size="5" readonly="${noinput}"></html:text>
							<html:text styleId="testoNormale" property="fornitore" size="50" readonly="${noinput}"></html:text>
	                    	<html:submit  styleClass="buttonImage" property="methodComunicazioniRicercaParziale" disabled="${noinput}">
								<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
							</html:submit>

                       </td>

			</tr>
			<tr>
                        <td scope="col"  class="etichetta" align="left">
 							<bean:message  key="ricerca.label.dataDa" bundle="acquisizioniLabels" />
 						</td>
						<td >&nbsp;</td>
                        <td scope="col" colspan="2" class="etichetta" align="left">
							<html:text styleId="testoNormale" property="dataInizio" size="10" readonly="${noinput}"></html:text>
 							<bean:message  key="ricerca.label.dataA" bundle="acquisizioniLabels" />
							<html:text styleId="testoNormale" property="dataFine" size="10" readonly="${noinput}"></html:text>
							<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />
                        </td>

			 </tr>

		     <tr>
 					<td  class="etichettaIntestazione"  scope="col" align="left">
 							<bean:message  key="ricerca.button.operazioneSuOrdine" bundle="acquisizioniLabels" />
 					</td>
						<td align="right"><bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" /></td>
                        <td scope="col"  class="etichetta" align="left">
							<html:select styleClass="testoNormale" property="tipoOrdine" disabled="${noinputOrd}">
							<html:optionsCollection  property="listaTipoOrdine" value="codice" label="descrizione" />
							</html:select>
                        </td>
 						<td  class="etichetta"  scope="col" align="left">
 							<bean:message  key="buono.label.anno" bundle="acquisizioniLabels" />
							<html:text styleId="testoNormale" property="annoOrdine" size="4" readonly="${noinputOrd}"></html:text>
                        </td>
 						<td  class="etichetta"  scope="col" align="left">
 							<bean:message  key="ricerca.label.codice" bundle="acquisizioniLabels" />
							<html:text styleId="testoNormale" property="codiceOrdine" size="4" readonly="${noinputOrd}"></html:text>
	                    	<html:submit  styleClass="buttonImage" property="methodComunicazioniRicercaParziale" disabled="${noinputOrd}">
								<bean:message  key="ricerca.button.ordine" bundle="acquisizioniLabels" />
							</html:submit>
	                    </td>


 			</tr>
		     <tr>
	 					<td  class="etichettaIntestazione"  scope="col" align="left">
	 							<bean:message  key="ricerca.label.fattura" bundle="acquisizioniLabels" />
	 					</td>
						<td align="right" ><bean:message  key="buono.label.anno" bundle="acquisizioniLabels" /></td>
 						<td  class="etichetta"  scope="col" align="left">
 							<html:text styleId="testoNormale" property="annoFattura" size="4" readonly="${noinputFatt}"></html:text>
	                    </td>
 						<td  class="etichetta"  scope="col" align="left">
 							<bean:message  key="ricerca.label.progr" bundle="acquisizioniLabels" />
							<html:text styleId="testoNormale" property="progressivoFattura" size="4" readonly="${noinputFatt}" ></html:text>
	                    	<html:submit  styleClass="buttonImage" property="methodComunicazioniRicercaParziale" disabled="${noinputFatt}">
								<bean:message  key="ricerca.label.fattura" bundle="acquisizioniLabels" />
							</html:submit>
	                    </td>
 			</tr>
		     <tr><td colspan="7">&nbsp;</td></tr>

                  </table>
		</td>
       	        <!-- FINE tabella corpo COLONNA + LARGA -->
             </tr>
	     </table>
			<!-- tabella bottoni -->
   	  </div>
	 <div id="divFooterCommon">
			<!-- tabella bottoni -->
           <table  width="100%"   border="0" style="height:40px" >
		     <tr>
				<td width="80" class="etichetta"><bean:message
					key="ricerca.label.elembloccoshort" bundle="acquisizioniLabels" /></td>
				<td width="150" class="testoNormale"><html:text
					property="elemXBlocchi" size="5"></html:text></td>
				<td width="75" class="etichetta"><bean:message
					key="ricerca.label.ordinamento" bundle="acquisizioniLabels" /></td>
				<td width="150" class="testoNormale"><html:select
					property="tipoOrdinamSelez" >
					<html:optionsCollection property="listaTipiOrdinam"
						value="codice" label="descrizione" />
				</html:select></td>
		     </tr>
      	  </table>
   	  	</div>
		 <div id="divFooter">

           <table align="center" border="0" style="height:40px" >
            <tr>
             <td >
             <sbn:checkAttivita idControllo="CERCA">
				<html:submit styleClass="pulsanti" property="methodComunicazioniRicercaParziale">
					<bean:message key="ricerca.button.cerca" bundle="acquisizioniLabels" />
				</html:submit>
			</sbn:checkAttivita>

		<c:choose>
			<c:when test="${!comunicazioniRicercaParzialeForm.LSRicerca}">
             <sbn:checkAttivita idControllo="CREA">
				<html:submit styleClass="pulsanti" property="methodComunicazioniRicercaParziale">
					<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
				</html:submit>
			</sbn:checkAttivita>

			</c:when>
		</c:choose>


				<c:choose>
					<c:when test="${comunicazioniRicercaParzialeForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodComunicazioniRicercaParziale">
							<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
				</c:choose>
             </td>
          </tr>
      	  </table>
     	  </div>
	</sbn:navform>
</layout:page>
