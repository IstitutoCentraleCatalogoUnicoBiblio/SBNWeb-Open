<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/offerte/offerteRicercaParziale.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
    <!-- colonna contenuto -->
		 <table    width="100%"  align="center"  >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="85%">

			  <table   width="100%" >
			<tr>
		        <td  width="9%" scope="col" class="etichetta" align="left">
		         <bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
		        </td>
		        <td scope="col" width="5%" align="left">
					<html:text styleId="testoNormale" property="codBibl" size="10" readonly="true"></html:text>
		        </td>
				<td  class="etichetta" align="right">
						<bean:message  key="ricerca.label.codOfferta" bundle="acquisizioniLabels" />
				</td>
                   <td scope="col" align="left">
					<html:text styleId="testoNormale" property="offFornitore" size="10" ></html:text>
                </td>
		      </tr>

			<tr>
		        <td scope="col"  class="etichetta" width="17%" align="right">
		         <bean:message  key="ordine.label.tabTitolo" bundle="acquisizioniLabels" />
		        </td>
		        <td scope="col"  class="etichetta" width="17%" align="left">
					<html:text styleId="testoNormale" property="titolo" size="50" ></html:text>
		        </td>

		      </tr>


		      <tr>
		        <td width="8%" class="etichetta" align="right">
		         <bean:message  key="ricerca.label.autore" bundle="acquisizioniLabels" />
		        </td>
		        <td scope="col" colspan="3"  class="etichetta" width="44%" align="left">
					<html:text styleId="testoNormale" property="autore" size="80" maxlength="160" ></html:text>
		        </td>
		      </tr>
		      <tr>
		        <td width="8%" class="etichetta" align="right">
		         <bean:message  key="ricerca.label.classificazione" bundle="acquisizioniLabels" />
		        </td>
		        <td scope="col" colspan="3" class="etichetta" width="44%" align="left">
					<html:text styleId="testoNormale" property="classificazione" size="31" ></html:text>
		        </td>
		      </tr>

		     <tr >
					    <td class="etichetta" valign="top">
					    <bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
					    </td>
                        <td valign="top" colspan="2" scope="col" align="left" >
				 		  <html:text styleId="testoNormale" property="fornitore.codice" size="5"  maxlength="10"></html:text>
				 		  <html:text styleId="testoNormale" property="fornitore.descrizione" size="40"  maxlength="40"></html:text>
                        <html:submit  styleClass="buttonImage" property="methodOfferteRicercaParziale">
							<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
						</html:submit>
                        </td>

            </tr>


                  </table>
		</td>
       	        <!-- FINE tabella corpo COLONNA + LARGA -->


             </tr>
	     </table>

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

	<table align="center"  border="0" style="height:40px" >
            <tr>
             <td align="center">
			<html:submit styleClass="pulsanti" property="methodOfferteRicercaParziale">
				<bean:message key="ricerca.button.cerca" bundle="acquisizioniLabels" />
			</html:submit>
			<!--
			<html:submit styleClass="pulsanti" property="methodOfferteRicercaParziale">
				<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
			</html:submit>
			-->
				<c:choose>
					<c:when test="${offerteRicercaParzialeForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodOfferteRicercaParziale">
							<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
				</c:choose>
			 </td>
		  </tr>
      	  </table>
	  			<!-- fine tabella bottoni -->
     	  </div>
	</sbn:navform>
</layout:page>
