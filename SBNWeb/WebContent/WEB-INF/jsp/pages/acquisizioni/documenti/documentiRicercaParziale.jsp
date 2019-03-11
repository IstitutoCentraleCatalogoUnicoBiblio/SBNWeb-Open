<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/documenti/documentiRicercaParziale.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
		  <table   width="100%" border="0"  cellpadding="0" cellspacing="0">
		     <tr>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%">
 							<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" valign="top" align="left">
 							<html:text styleId="testoNormale" property="codBibl" size="3" readonly="true" ></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodDocumentiRicercaParziale" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>

                        </td>
                        <td class="etichetta" colspan="2" scope="col" valign="top" align="left">
 							<bean:message  key="ricerca.label.codUtente" bundle="acquisizioniLabels" />
<!--  							<html:text styleId="testoNormale" property="codUtenteBibl" size="3"  ></html:text>-->
  							<html:text styleId="testoNormale" property="codUtenteProg" size="20" maxlength="25"  ></html:text>
<!-- 						  <html:link action="/servizi/utenti/RicercaUtenti.do" ><img border="0"   alt="ricerca utenti"  src='<c:url value="/images/lente.GIF" />'/></html:link>-->
	                        <html:submit  styleClass="buttonImage" property="methodDocumentiRicercaParziale">
								<bean:message  key="servizi.bottone.hlputente" bundle="acquisizioniLabels" />
							</html:submit>

                        </td>
             </tr>
			<tr>
                        <td scope="col" class="etichetta" align="left">&nbsp;</td>
            </tr>

		     <tr>
                        <td   scope="col" class="etichetta" align="left">
 							<bean:message  key="buono.label.statoSuggerimento" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:select styleClass="testoNormale" property="statoSuggerimento" style="width:40px">
							<html:optionsCollection  property="listaStatoSuggerimento" value="codice" label="descrizioneCodice" />
							</html:select>
                        </td>
                        <td class="etichetta" colspan="2" scope="col" valign="top" align="left">
 							<bean:message  key="ricerca.label.codSugg" bundle="acquisizioniLabels" />
  							<html:text styleId="testoNormale" property="codDoc" size="5"  ></html:text>
						</td>

            </tr>
			<tr>
                        <td scope="col" class="etichetta" align="left">&nbsp;</td>
            </tr>

			<tr>
                        <td scope="col" class="etichetta" align="left">
 							<bean:message  key="ordine.label.tabParoleTitolo" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" colspan="4"  align="left">
  							<html:text styleId="testoNormale" property="titoloDoc" size="50"  ></html:text>
                        </td>
            </tr>
			<tr>
                        <td scope="col" class="etichetta" align="left">&nbsp;</td>
            </tr>
			<tr>
                        <td scope="col" class="etichetta" align="left">
 							<bean:message  key="ricerca.label.dataDa" bundle="acquisizioniLabels" />
                        </td>

                        <td scope="col" colspan="4" class="etichetta" align="left">
  							<html:text styleId="testoNormale" property="dataInizio" size="10"></html:text>
 							<bean:message  key="ricerca.label.dataA" bundle="acquisizioniLabels" />
  							<html:text styleId="testoNormale" property="dataFine" size="10"></html:text>
							<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />
                        </td>

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

			<!-- tabella bottoni -->
           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
             <sbn:checkAttivita idControllo="CERCA">
			<html:submit styleClass="pulsanti" property="methodDocumentiRicercaParziale">
				<bean:message key="ricerca.button.cerca" bundle="acquisizioniLabels" />
			</html:submit>
			</sbn:checkAttivita>

				<c:choose>
					<c:when test="${documentiRicercaParzialeForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodDocumentiRicercaParziale">
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
