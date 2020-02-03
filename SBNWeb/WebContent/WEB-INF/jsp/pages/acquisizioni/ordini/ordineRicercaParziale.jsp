<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<bean-struts:define id="noinputForn"  value="false"/>
<bean-struts:define id="noinput"  value="false"/>

<logic:notEqual  name="ordineRicercaParzialeForm" property="disabilitaFornitore" value="false">
		<bean-struts:define id="noinputForn"  value="true"/>
</logic:notEqual>

<c:choose>
<c:when test="${ordineRicercaParzialeForm.disabilitaTutto}">
      <script type="text/javascript">
		<bean-struts:define id="noinput"  value="true"/>
 		<bean-struts:define id="noinputForn"  value="true"/>
      </script>
 </c:when>
</c:choose>
<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/ordini/ordineRicercaParziale.do"  >
	<div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
    <table   width="100%"  align="center"  >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">

		  <table   width="100%" border="0">

		     <tr>
                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
                        </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="codiceBibl" size="5" readonly="true" ></html:text>
                        <html:submit   title="elenco" styleClass="buttonImageListaSezione" property="methodOrdineRicercaParziale" disabled="${noinput}" >
							<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
						</html:submit>
						</div>
                        </td>
                        <!--
                        <td  valign="top" scope="col"  ><div align="left" class="etichetta"><bean:message  key="ricerca.label.biblAffil" bundle="acquisizioniLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
				          <html:select  styleClass="testoNormale"  property="biblAffil" style="width:100px">
						  <html:optionsCollection  property="listaBiblAffil" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                        -->
				<c:choose>
					<c:when test="${ordineRicercaParzialeForm.gestSez}">

                        <td valign="top"  scope="col" ><div style="text-align: right;"  class="etichetta"><bean:message  key="ricerca.label.sezione" bundle="acquisizioniLabels" /></div></td>
                        <td scope="col" >
                        <div align="left" valign="top">
              			<html:text styleId="testoNormale" property="sezione" size="7" maxlength="7" readonly="${noinput}" onkeypress="if (event.keyCode==13) return false;"></html:text>
       				    <!--
       				    <html:link action="/acquisizioni/sezioni/sezioniRicercaParziale.do" ><img border="0"  align="middle" alt="ricerca titolo"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        -->
                        <html:submit  title="elenco" styleClass="buttonImageListaSezione" property="methodOrdineRicercaParziale" disabled="${noinput}"  >
							<bean:message  key="ricerca.label.sezione" bundle="acquisizioniLabels" />
						</html:submit>
                        </div>
                        </td>
						<td >&nbsp;</td>
						<td >&nbsp;</td>
			        </c:when>
			    </c:choose>

		     </tr>
		     <tr >
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="ricerca.label.continuativo" bundle="acquisizioniLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="continuativo"  disabled="${noinput}">
						<html:optionsCollection  property="listaContinuativo" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  scope="col">
						<div style="text-align: right;" class="etichetta">
						<bean:message  key="ordine.label.natura" bundle="acquisizioniLabels" />
                        </div>
                        </td>
                        <td  scope="col"><div align="left" class="etichetta">
                        <html:select style="width:40px" styleClass="testoNormale"  onchange="setStato('A')" property="natura" disabled="${noinput}">
						  <html:optionsCollection  property="listaNatura" value="codice" label="descrizioneCodice" />
						  </html:select></div>
						</td>
                        <td valign="top" scope="col" colspan="2" >
                        <!--
						<div align="left" class="etichetta">
						<bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
						<html:select  styleClass="testoNormale"  property="tipoOrdine">
						<html:optionsCollection  property="listaTipoOrdine" value="codice" label="descrizione" />
						</html:select>
                        </div>
                        -->
                        </td>

             </tr>

		     <tr >
                        <td  valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="ricerca.label.annoOrdine" bundle="acquisizioniLabels" />
						</div></td>
						<!--	onchange="this.form.submit();"-->

                        <td  valign="top" scope="col" align="left">
				 		  	<html:text  styleId="testoNormale" property="annoOrdine" size="4"   onkeypress="if (event.keyCode==13) return false;" ></html:text>
                        </td>
                        <td  scope="col">
						<div style="text-align: right;" class="etichetta">
						<bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
                       </div>
                        </td>
                        <td  scope="col">
						<div align="left" class="etichetta">
						<html:select  styleClass="testoNormale"  property="tipoOrdine" >
						<html:optionsCollection  property="listaTipoOrdine" value="codice" label="descrizione" />
						</html:select>
                        </div>
                        </td>
                        <td valign="top" scope="col" colspan="2" ><div align="left" class="etichetta">
                        	<bean:message  key="ordine.label.numero" bundle="acquisizioniLabels" />
                        	<html:text styleId="testoNormale" property="numero" size="5" onkeypress="if (event.keyCode==13) return false;"></html:text>
                        </div></td>
             </tr>

	        <tr  >
	          <td class="etichetta" valign="top" ><bean:message  key="ordine.label.bid" bundle="acquisizioniLabels" /></td>
	          <td class="etichetta" colspan="4" >
					<table border="0" cellpadding="0" cellspacing="0"  >
					<tr>
					<td valign="top" align="left">
					  <html:text styleId="testoNormale"   property="titolo.codice" size="10" readonly="${noinput}" onkeypress="if (event.keyCode==13) return false;" maxlength="10"></html:text>
					</td>
					<td bgcolor="#EBEBE4" valign="top" align="left" >
						<bean-struts:write  name="ordineRicercaParzialeForm" property="titolo.descrizione" />
					</td>
					<td valign="top" align="left">
                        <html:submit  title="ricerca" styleClass="buttonImage" property="methodOrdineRicercaParziale" disabled="${noinput}">
							<bean:message  key="ordine.bottone.searchTit" bundle="acquisizioniLabels" />
						</html:submit>
					</td>

					</tr>
					</table >

<!--			  <html:text styleId="testoNormale"   property="titolo.descrizione" size="40" readonly="true" ></html:text>-->
<!--			  <html:link action="/gestionebibliografica/titolo/interrogazioneTitolo.do"  ><img border="0"   alt="ricerca titolo"  src='<c:url value="/images/lente.GIF" />'/></html:link>-->

			  </td>
	        </tr>

		     <tr >
					    <td class="etichetta" valign="top">
					    <bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
					    </td>
                        <td valign="top" colspan="4" scope="col" align="left" >
				 		  <html:text styleId="testoNormale" property="codFornitore" size="5"  maxlength="10" readonly="${noinputForn}" onkeypress="if (event.keyCode==13) return false;"></html:text>
				 		  <html:text styleId="testoNormale" property="fornitore" size="40"  maxlength="40" readonly="${noinputForn}" onkeypress="if (event.keyCode==13) return false;"></html:text>
       				    <!--
       				     <html:link action="/acquisizioni/fornitori/fornitoriRicercaParziale.do" ><img border="0"  align="middle" alt="ricerca fornitore"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        -->
                        <html:submit  styleClass="buttonImage" property="methodOrdineRicercaParziale" disabled="${noinputForn}">
							<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
						</html:submit>
                        </td>

            </tr>

		     <tr>
                        <td scope="col"  align="left" class="etichetta">
                        	<bean:message  key="ricerca.label.dataOrdineDa" bundle="acquisizioniLabels" />
                        </td>
                        <td>
				 		  	<html:text styleId="testoNormale" property="dataOrdineDa" size="10" readonly="${noinput}" onkeypress="if (event.keyCode==13) return false;"></html:text>
                        </td>
                        <td colspan="2" align="left" class="etichetta">
                        	<bean:message  key="ricerca.label.dataOrdineA" bundle="acquisizioniLabels" />
				 		  <html:text styleId="testoNormale" property="dataOrdineA" size="10" readonly="${noinput}" onkeypress="if (event.keyCode==13) return false;"></html:text>
                        	<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />

                        </td>
			</tr>
<!--
		     <tr>
                        <td scope="col"  align="left" class="etichetta">
                        	<bean:message  key="ricerca.label.dataStampaOrdineDa" bundle="acquisizioniLabels" />
                        </td>
                        <td>
				 		  	<html:text styleId="testoNormale" property="dataStampaOrdineDa" size="10" readonly="${noinput}" onkeypress="if (event.keyCode==13) return false;"></html:text>
                        </td>
                        <td colspan="2" align="left" class="etichetta">
                        	<bean:message  key="ricerca.label.dataStampaOrdineA" bundle="acquisizioniLabels" />
				 		  <html:text styleId="testoNormale" property="dataStampaOrdineA" size="10" readonly="${noinput}" onkeypress="if (event.keyCode==13) return false;"></html:text>
                        	<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />

                        </td>
			</tr>
-->

			<tr>

                        <td scope="col" align="left" class="etichetta">
                        	<bean:message  key="ricerca.label.dataOrdineAbbDa" bundle="acquisizioniLabels" />
                        </td>
                        <td>
				 		  <html:text styleId="testoNormale" property="dataOrdineAbbDa" size="10" readonly="${noinput}" onkeypress="if (event.keyCode==13) return false;"></html:text>
                        </td>
						<td colspan="2" class="etichetta">
						<bean:message  key="ricerca.label.dataOrdineAbbA" bundle="acquisizioniLabels" />
				 		<html:text styleId="testoNormale" property="dataOrdineAbbA" size="10" readonly="${noinput}" onkeypress="if (event.keyCode==13) return false;"></html:text>
                        	<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />

                        </td>

		     </tr>

				<c:choose>
					<c:when test="${ordineRicercaParzialeForm.gestBil}">
				     <tr>
		                        <td scope="col"  align="left" class="etichetta">
		                        	<bean:message  key="ordine.label.esercizio" bundle="acquisizioniLabels" />
		                        </td>
		                        <td scope="col" align="left">
		                        <html:text styleId="testoNormale" property="esercizio" size="4" readonly="${noinput}" onkeypress="if (event.keyCode==13) return false;"></html:text>
		                        </td>
		                        <td  scope="col" align="left" class="etichetta">
		                        <bean:message  key="ordine.label.capitolo" bundle="acquisizioniLabels" />
		                        <html:text styleId="testoNormale" property="capitolo" size="16" maxlength="16" readonly="${noinput}" onkeypress="if (event.keyCode==13) return false;"></html:text>
		                        </td>
		                        <td  scope="col" align="left" colspan="3" class="etichetta">
					           	<bean:message  key="ordine.label.tipoImpegno" bundle="acquisizioniLabels" />
								<html:select style="width:40px" styleClass="testoNormale" property="tipoImpegno" disabled="${noinput}">
								<html:optionsCollection  property="listaTipoImpegno" value="codice" label="descrizioneCodiceACQ" />
								</html:select>
								<!--
							    <html:link action="/acquisizioni/bilancio/bilancioRicercaParziale.do" >
							    <img border="0"   alt="ricerca bilancio"  src='<c:url value="/images/lente.GIF" />'/>
							    </html:link>
		                        -->
		                        <html:submit  styleClass="buttonImage" property="methodOrdineRicercaParziale" disabled="${noinput}">
									<bean:message  key="ordine.label.bilancio" bundle="acquisizioniLabels" />
								</html:submit>
							  </td>

				     </tr>
			        </c:when>
			    </c:choose>

			<tr>
                        <td scope="col"  class="etichetta" align="left">
	                        <bean:message  key="ordine.label.stato" bundle="acquisizioniLabels" />
                        </td>
			  	        <td   scope="col" class="etichetta" colspan="3" >
                        	<bean:message  key="ordine.label.aperto" bundle="acquisizioniLabels" />
							<html:multibox  property="statoArr" value="A" disabled="${noinput}"></html:multibox>
                        	<bean:message  key="ordine.label.chiuso" bundle="acquisizioniLabels" />
							<html:multibox  property="statoArr" value="C"  disabled="${noinput}"></html:multibox>
                        	<bean:message  key="ordine.label.annullato" bundle="acquisizioniLabels" />
							<html:multibox   property="statoArr" value="N"  disabled="${noinput}"></html:multibox>
                        </td>
                        <td scope="col" align="left" class="etichetta">
<!--                        	<bean:message  key="ordine.label.numero" bundle="acquisizioniLabels" />-->
                        </td>
                        <td>
<!--                        	<html:text styleId="testoNormale" property="numero" size="5"></html:text>-->
                        </td>
                        <!--
                        <td   scope="col"  >
							<html:select multiple="true" styleClass="testoNormale" size="3"  property="statoArr" >
							<html:optionsCollection  property="listaStato" value="codice" label="descrizione" />
							</html:select>
						</td>
		    			 -->
		     </tr>
			<tr>

                <td  align="left" class="etichetta">
                      <bean:message  key="ordine.label.tipoInvio" bundle="acquisizioniLabels" />
               </td>
               <td>
		        	<html:select style="width:40px" styleClass="testoNormale" property="tipoInvio" disabled="${noinput}">
					<html:optionsCollection  property="listaTipoInvio" value="codice" label="descrizioneCodice" />
					</html:select>
   				</td>
	  	    	<td  class="etichetta" >
	  	    		<bean:message  key="ordine.label.stampato" bundle="acquisizioniLabels" />
						<html:select  styleClass="testoNormale"  property="stampatoStr"  disabled="${noinput}">
						<html:optionsCollection  property="listaContinuativo" value="codice" label="descrizione" />
						</html:select>


<!--					<html:checkbox   property="stampato" disabled="${noinput}"></html:checkbox>-->
<!--
					 <bean:message key="configurazione.label.StampaBO11"	bundle="acquisizioniLabels" />
					 <html:radio property="stampato" value="false" disabled="${noinput}"/>
					 <bean:message	key="configurazione.label.StampaBO12" bundle="acquisizioniLabels" />
					 <html:radio property="stampato" value="false" disabled="${noinput}"/>
-->
				</td>
	  	    	<td  class="etichetta" >
					<bean:message  key="ordine.label.rinnovato" bundle="acquisizioniLabels" />
						<html:select  styleClass="testoNormale"  property="rinnovatoStr"  disabled="${noinput}">
						<html:optionsCollection  property="listaContinuativo" value="codice" label="descrizione" />
						</html:select>

<!--					<html:checkbox   property="rinnovato" disabled="${noinput}"></html:checkbox>-->
<!--
					 <bean:message key="configurazione.label.StampaBO11"	bundle="acquisizioniLabels" />
					 <html:radio property="rinnovato" value="true" disabled="${noinput}"/>
					 <bean:message	key="configurazione.label.StampaBO12" bundle="acquisizioniLabels" />
					 <html:radio property="rinnovato" value="false" disabled="${noinput}"/>
-->
	  			</td>

		     </tr>
 <!-- Inventario collegato -->
			<tr>
			 <td align="left" class="etichetta">
                  <bean:message key="ordine.label.invcoll" bundle="acquisizioniLabels" />
               </td>
               <td class="etichetta" colspan="3">
		        	RFID&nbsp;<html:text styleId="testoNoBold" property="rfidChiaveInventario" size="18" />
   				</td>

			</tr>
		</table>
		</td>
       	        <!-- FINE tabella corpo COLONNA + LARGA -->
             </tr>
	     </table>
</div>

<div id="divFooterCommon">
            <table  width="100%"   border="0" style="height:40px" >
		     <tr>
				<td width="80" class="etichetta"><bean:message
					key="ricerca.label.elembloccoshort" bundle="acquisizioniLabels" /></td>
				<td width="150" class="testoNormale"><html:text	property="elemXBlocchi" size="5" maxlength="4" onkeypress="if (event.keyCode==13) return false;"></html:text></td>
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
            <tr >
             <td>
             <sbn:checkAttivita idControllo="CERCA">
			<html:submit  styleClass="pulsanti" property="methodOrdineRicercaParziale" >
				<bean:message key="ricerca.button.cerca" bundle="acquisizioniLabels" />
			</html:submit>
			</sbn:checkAttivita>
		<c:choose>
			<c:when test="${!ordineRicercaParzialeForm.LSRicerca}">
             <sbn:checkAttivita idControllo="CREA">
				<html:submit  styleClass="pulsanti" property="methodOrdineRicercaParziale">
					<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
				</html:submit>
			</sbn:checkAttivita>

			</c:when>
		</c:choose>


		<c:choose>
			<c:when test="${ordineRicercaParzialeForm.visibilitaIndietroLS}">
				<html:submit  styleClass="pulsanti" property="methodOrdineRicercaParziale">
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
