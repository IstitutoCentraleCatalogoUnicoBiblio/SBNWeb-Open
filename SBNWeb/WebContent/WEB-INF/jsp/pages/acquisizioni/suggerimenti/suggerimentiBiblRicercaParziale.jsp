<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/suggerimenti/suggerimentiBiblRicercaParziale.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>

		  <table   width="100%" border="0">
		     <tr><td colspan="4">&nbsp;</td></tr>

		     <tr>
                        <td  width="10%" scope="col" class="etichetta" align="left">
 							<bean:message  key="ricerca.label.codBibl" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
 							<html:text styleId="testoNormale" property="codBibl" size="3" readonly="true" ></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodSuggerimentiBiblRicercaParziale" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>

                        </td>
                        <td class="etichetta" scope="col" align="left">
 							<bean:message  key="ricerca.label.codSugg" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
 							<html:text styleId="testoNormale" property="codSuggerim" size="5" ></html:text>
                        </td>
             </tr>

		     <tr>
                        <td class="etichetta" scope="col" align="left">
 							<bean:message  key="ricerca.label.bibliotecario" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
						  <html:text styleId="testoNormale"   property="codBibliotec" size="12"  ></html:text>
						  <bean-struts:write  name="suggerimentiBiblRicercaParzialeForm" property="nomeBibliotec" />
 						  <html:link action="/amministrazionesistema/ricercaBibliotecario.do" ><img border="0"   alt="ricerca bibliotecari"  src='<c:url value="/images/lente.GIF" />'/></html:link>
 						  <!--
                        <html:submit  styleClass="buttonImage" property="methodSuggerimentiBiblRicercaParziale">
							<bean:message  key="ricerca.label.sezione" bundle="acquisizioniLabels" />
						</html:submit>

                        -->
                        </td>
                        <td  width="10%" scope="col" class="etichetta" align="left">
 							<bean:message  key="buono.label.statoBuono" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:select styleClass="testoNormale" property="statoSuggerimento" style="width:40px">
							<html:optionsCollection  property="listaStatoSuggerimento" value="codice" label="descrizioneCodice" />
							</html:select>
                        </td>
            </tr>
			<tr>
                        <td scope="col" class="etichetta" align="left">
 							<bean:message  key="ricerca.label.dataDa" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
		    				<html:text styleId="testoNormale" property="dataInizio" size="10" ></html:text>
                        </td>
                        <td scope="col" class="etichetta" align="left">
 							<bean:message  key="ricerca.label.dataA" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
		    				<html:text styleId="testoNormale" property="dataFine" size="10" ></html:text>
							<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />
                        </td>

			 </tr>

		     <tr>
                        <td class="etichetta" scope="col" align="left">
 							<bean:message  key="ricerca.label.codBid" bundle="acquisizioniLabels" />
                        </td>
                        <td colspan="3" scope="col" align="left">
							<table border="0" cellpadding="0" cellspacing="0"  >
							<tr>
							<td valign="top" align="left">
							  <html:text styleId="testoNormale"   property="titolo.codice" size="10" ></html:text>
							</td>
							<td bgcolor="#EBEBE4" valign="top" align="left" >
<!--						  <html:text styleId="testoNormale"   property="titolo.descrizione" size="40" readonly="true" ></html:text>-->
								<bean-struts:write  name="suggerimentiBiblRicercaParzialeForm" property="titolo.descrizione" />
							</td>
							<td valign="top" align="left">
	                        <html:submit  styleClass="buttonImage" property="methodSuggerimentiBiblRicercaParziale">
								<bean:message  key="ordine.bottone.searchTit" bundle="acquisizioniLabels" />
							</html:submit>
							</td>

							</tr>
							</table >
                        </td>
            </tr>

		     <tr><td colspan="4">&nbsp;</td></tr>

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

			<!-- tabella bottoni -->
           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
             <sbn:checkAttivita idControllo="CERCA">
			<html:submit styleClass="pulsanti" property="methodSuggerimentiBiblRicercaParziale">
				<bean:message key="ricerca.button.cerca" bundle="acquisizioniLabels" />
			</html:submit>
			</sbn:checkAttivita>

		<c:choose>
			<c:when test="${!suggerimentiBiblRicercaParzialeForm.LSRicerca}">
             <sbn:checkAttivita idControllo="CREA">
				<html:submit styleClass="pulsanti" property="methodSuggerimentiBiblRicercaParziale">
					<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
				</html:submit>
			</sbn:checkAttivita>
			</c:when>
		</c:choose>

				<c:choose>
					<c:when test="${suggerimentiBiblRicercaParzialeForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodSuggerimentiBiblRicercaParziale">
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
