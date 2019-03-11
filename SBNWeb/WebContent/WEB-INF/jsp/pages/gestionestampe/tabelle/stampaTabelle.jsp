<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="gestioneStampeMessages" />
	<sbn:navform action="/gestionestampe/tabelle/stampaTabelle.do">
	<div id="content">
	<table   width="100%"  align="center"  >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">

		  <table   width="100%" border="0">
		     <tr>
                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="ricerca.label.codiceBibl" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="codiceBibl" size="5" disabled="true"></html:text>
                        </div></td>

                        <td  valign="top" scope="col"  ><div align="left" class="etichetta"><bean:message  key="ricerca.label.biblAffil" bundle="acquisizioniLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
				          <html:select  styleClass="testoNormale"  property="biblAffil" style="width:100px">
						  <html:optionsCollection  property="listaBiblAffil" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                        <td valign="top"  scope="col"><div align="left" class="etichetta"><bean:message  key="ricerca.label.sezione" bundle="acquisizioniLabels" /></div></td>
                        <td scope="col" ><div align="left" valign="top">
              			<html:text styleId="testoNormale" property="sezione" size="4" maxlength="4"></html:text>
       				     <html:link action="/acquisizioni/sezioni/sezioniRicercaParziale.do" ><img border="0"  align="middle" alt="ricerca titolo"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </div></td>
						<td class="navigazione3" >&nbsp;</td>
						<td class="navigazione3" >&nbsp;</td>
		     </tr>
		     <tr >
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="ricerca.label.continuativo" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="continuativo">
						<html:optionsCollection  property="listaContinuativo" value="codice" label="descrizione" />
						</html:select>
                        </td>
					    <td class="etichetta" valign="top"><bean:message  key="ordine.label.fornitore" bundle="gestioneStampeLabels" /></td>
                        <td valign="top" colspan="2" scope="col" align="left" style="width:50px;">
				 		  <html:text styleId="testoNormale" property="fornitore" size="20"  maxlength="20"></html:text>
       				     <html:link action="/acquisizioni/fornitori/fornitoriRicercaParziale.do" ><img border="0"  align="middle" alt="ricerca fornitore"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
                        <td valign="top" scope="col" colspan="2" ><div align="left" class="etichetta"><bean:message  key="ordine.label.natura" bundle="gestioneStampeLabels" />
				          <html:select style="width:40px" styleClass="testoNormale" onchange='setStato("A")' property="natura">
						  <html:optionsCollection  property="listaNatura" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>

                    </tr>
		     <tr>
                        <td scope="col" ><div align="left" class="etichetta"><bean:message  key="ricerca.label.dataOrdineDa" bundle="gestioneStampeLabels" /></div></td>
                        <td>
				 		  <html:text styleId="testoNormale" property="dataOrdineDa" size="10"></html:text>
                        </td>
                        <td colspan="2" ><div align="left" class="etichetta"><bean:message  key="ricerca.label.dataOrdineA" bundle="gestioneStampeLabels" />
				 		  <html:text styleId="testoNormale" property="dataOrdineA" size="10"></html:text>
                        </div></td>

                        <td  scope="col">
						<div align="left" class="etichetta">
						<bean:message  key="ricerca.label.tipoOrdine" bundle="gestioneStampeLabels" />
                       </div>
                        </td>
                        <td  scope="col">
						<div align="left" class="etichetta">
						<html:select  styleClass="testoNormale"  property="tipoOrdine">
						<html:optionsCollection  property="listaTipoOrdine" value="codice" label="descrizione" />
						</html:select>
                        </div>
                        </td>

			</tr>
			<tr>

                        <td scope="col"  ><div align="left" class="etichetta"><bean:message  key="ricerca.label.dataOrdineAbbDa" bundle="gestioneStampeLabels" /></td>
                        <td>
				 		  <html:text styleId="testoNormale" property="dataOrdineAbbDa" size="10"></html:text>
                        </td>
						<td colspan="2" class="etichetta"><bean:message  key="ricerca.label.dataOrdineAbbA" bundle="gestioneStampeLabels" />
				 		  <html:text styleId="testoNormale" property="dataOrdineAbbA" size="10"></html:text>
                        </div></td>
                        <td scope="col"  class="etichetta"><div align="left"><bean:message  key="ordine.label.stato" bundle="gestioneStampeLabels" /></td>
                        <td   scope="col"  >
						<html:select  styleClass="testoNormale"  property="stato">
						<html:optionsCollection  property="listaStato" value="codice" label="descrizione" />
						</html:select>
						</div></td>


		     </tr>
			<tr>
                        <td  > <div align="left" class="etichetta"><bean:message  key="ordine.label.tipoInvio" bundle="gestioneStampeLabels" /></td>
                        <td>
			        	<html:select style="width:40px" styleClass="testoNormale" property="tipoInvio">
						<html:optionsCollection  property="listaTipoInvio" value="codice" label="descrizione" />
						</html:select>
          				</div></td>

                        <td scope="col"  ><div align="left" class="etichetta"><bean:message  key="ordine.label.numero" bundle="gestioneStampeLabels" /></td>
                        <td>
                        <html:text styleId="testoNormale" property="numero" size="5"></html:text>
                        </td>
		     </tr>

		     <tr>
                        <td scope="col" ><div align="left" class="etichetta"><bean:message  key="ordine.label.esercizio" bundle="gestioneStampeLabels" /></div></td>
                        <td scope="col"><div align="left">
                        <html:text styleId="testoNormale" property="esercizio" size="5"></html:text>
                        </div></td>
                        <td  scope="col"><div align="left" class="etichetta"><bean:message  key="ordine.label.capitolo" bundle="gestioneStampeLabels" /></div></td>
                        <td  scope="col"><div align="left">
                        <html:text styleId="testoNormale" property="capitolo" size="5"></html:text>
                        </div></td>

		     </tr>

		      <tr >
                        <td  valign="top" scope="col" ><div class="etichetta" align="left">
						<bean:message  key="ricerca.label.elemblocco" bundle="gestioneStampeLabels" /></td>
						<td>
						<html:text styleId="testoNormale" property="elemBlocco" size="10"
						maxlength="10"></html:text></div></td>
		     </tr>

            </table>
		</span></td>
       	        <!-- FINE tabella corpo COLONNA + LARGA -->
             </tr>
	     </table>
	     	<HR>
			<jsp:include flush="true" page="../common/tipoStampa.jsp" />
			<HR>

           <table align="center" border="0" style="height:40px" >
            <tr >
             <td>
			<html:submit styleClass="pulsanti" property="methodStampaTabelle">
				<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodStampaTabelle">
				<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
			</html:submit>
			 </td>
		  </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
