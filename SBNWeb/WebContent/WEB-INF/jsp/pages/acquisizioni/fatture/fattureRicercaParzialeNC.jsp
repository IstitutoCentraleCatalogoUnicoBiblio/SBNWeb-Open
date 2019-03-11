<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput"  value="false"/>
<c:choose>
<c:when test="${fattureRicercaParzialeForm.disabilitaTutto}">
      <script type="text/javascript">
		<bean-struts:define id="noinput"  value="true"/>
      </script>

</c:when>
</c:choose>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/fatture/fattureRicercaParzialeNC.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
        <table   width="100%"  align="center"  >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">

		  <table  border="0"  width="100%" >
		     <tr>
                        <td  width="10%" scope="col" class="etichetta" align="left">
		                   	<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:text styleId="testoNormale" property="codiceBibl" size="3" readonly="true"></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodFattureRicercaParzialeNC" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>
                        </td>
		     </tr>
             <tr><td class="etichettaIntestazione" colspan="7">&nbsp;</td></tr>
		     <tr>
                        <td  width="10%" scope="col" class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.numFatt" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
						<html:text styleId="testoNormale" property="numFatt" size="10" readonly="${noinput}"></html:text>
                        </td>
                        <td scope="col" class="etichetta" align="left">
                  			<bean:message  key="ordine.label.stato" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
						<html:select  styleClass="testoNormale" property="statoFatt" disabled="${noinput}">
						<html:optionsCollection  property="listaStatoFatt" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td scope="col" align="left">&nbsp;</td>
                        <td scope="col" align="left">&nbsp;</td>
                        <td scope="col" align="left">&nbsp;</td>

		     </tr>
		     <tr>
                        <td scope="col" class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.dataDa" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:text styleId="testoNormale" property="dataFattDa" size="10" readonly="${noinput}"></html:text>
                        </td>
                        <td scope="col"  class="etichetta"  align="left">
                  			<bean:message  key="ricerca.label.dataA" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:text styleId="testoNormale" property="dataFattA" size="10" readonly="${noinput}"></html:text>
							<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />
                        </td>
		     </tr>
 		     <tr>
                        <td  scope="col" align="left" class="etichetta" >
						<bean:message  key="buono.label.anno" bundle="acquisizioniLabels" />
						</td>
						<!--	onchange="this.form.submit();"-->
                        <td  valign="top" scope="col" align="left">
				 		  	<html:text styleId="testoNormale" property="annoFatt" size="4" ></html:text>
                        </td>

			            <td scope="col" class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.progr" bundle="acquisizioniLabels" />
			            </td>
                        <td scope="col" align="left">
						<html:text styleId="testoNormale" property="progrFatt" size="8" ></html:text>
                        </td>

		     </tr>

		     <tr>
					 <td class="etichetta"  valign="top" >
                  			<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
					 </td>
                        <td colspan="4"  valign="top"  align="left" >
				 		  <html:text styleId="testoNormale" property="codFornitore" size="5"  maxlength="10" readonly="${noinput}"></html:text>
 						<html:text styleId="testoNormale" property="fornitore" size="45" readonly="${noinput}"></html:text>
                    	<html:submit  styleClass="buttonImage" property="methodFattureRicercaParzialeNC" disabled="${noinput}">
							<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
						</html:submit>
                     </td>

            </tr>
		     <tr>
                        <td scope="col" class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col"  align="left">
						<html:select styleClass="testoNormale" property="tipoFatt" disabled="${noinput}">
						<html:optionsCollection  property="listaTipoFatt" value="codice" label="descrizione" />
						</html:select>
                        </td>
            </tr>
		     <tr><td class="etichettaIntestazione" colspan="7">
	   			<bean:message  key="buono.label.ordine" bundle="acquisizioniLabels" />
		     </td></tr>
		     <tr>
                <td scope="col" class="etichetta" align="left">
           			<bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
                </td>
	                <td scope="col" align="left">
					<html:select  styleClass="testoNormale" property="ordine.codice1" disabled="${noinput}">
					<html:optionsCollection  property="listaTipoOrdine" value="codice" label="descrizione" />
					</html:select>
                    </td>
                    <td scope="col"  class="etichetta" align="left">
           			<bean:message  key="buono.label.anno" bundle="acquisizioniLabels" />
					<html:text styleId="testoNormale" property="ordine.codice2" size="4" readonly="${noinput}"></html:text>
                    </td>
                    <td scope="col" class="etichetta" colspan="3" >
	           			<bean:message   key="buono.label.numero" bundle="acquisizioniLabels" />
						<html:text styleId="testoNormale" property="ordine.codice3" size="4" readonly="${noinput}"></html:text>
							<html:submit  styleClass="buttonImage" property="methodFattureRicercaParzialeNC" disabled="${noinput}">
								<bean:message  key="ricerca.button.ordine" bundle="acquisizioniLabels" />
							</html:submit>
                    </td>
                    <td  colspan="2"  align="left" >


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

            <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
             <sbn:checkAttivita idControllo="CERCA">
			<html:submit styleClass="pulsanti" property="methodFattureRicercaParzialeNC">
				<bean:message key="ricerca.button.cerca" bundle="acquisizioniLabels" />
			</html:submit>
			</sbn:checkAttivita>
		<!--
		<c:choose>
			<c:when test="${!fattureRicercaParzialeNCForm.LSRicerca}">
             <sbn:checkAttivita idControllo="CREA">
				<html:submit styleClass="pulsanti" property="methodFattureRicercaParzialeNC">
					<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
				</html:submit>
			</sbn:checkAttivita>
			</c:when>
		</c:choose>
		-->
			<c:choose>
				<c:when test="${fattureRicercaParzialeNCForm.visibilitaIndietroLS}">
					<html:submit styleClass="pulsanti" property="methodFattureRicercaParzialeNC">
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
