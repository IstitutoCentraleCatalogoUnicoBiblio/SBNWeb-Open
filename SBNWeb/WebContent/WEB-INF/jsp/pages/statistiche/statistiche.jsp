<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/statistiche/statistiche.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
		<table width="100%" border="0"  cellpadding="1" cellspacing="1">
		     <tr>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%">
 							<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
                        </td>
                        <td width="25%" scope="col" valign="top" align="left">
 							<html:text styleId="testoNormale" property="codBibl" size="3" readonly="true" ></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodStatistiche" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>
                        </td>
                        <td width="25%"  scope="col" class="etichetta" align="left">&nbsp;</td>
                        <td width="25%"  scope="col" class="etichetta" align="left">&nbsp;</td>
             </tr>
			<tr>
                        <td colspan="4" scope="col" class="etichetta" align="left">&nbsp;</td>
            </tr>

			<tr>
                        <td scope="col" class="etichetta" align="left">
 							<bean:message  key="statistiche.label.selQuery" bundle="statisticheLabels" />
                        </td>
                        <td colspan="3" scope="col"  align="left">
							<html:select  styleClass="testoNormale" property="queryStr" >
							<html:optionsCollection  property="listaQueryStr" value="codice1" label="codice2" />
							</html:select>
						</td>


            </tr>

			<tr>
                        <td colspan="4" scope="col" class="etichetta" align="left">&nbsp;</td>
            </tr>


			<tr>
                        <td scope="col" class="etichetta" align="left">
 							<bean:message  key="statistiche.label.orientamentoPagina" bundle="statisticheLabels" />
                        </td>
                        <td colspan="3" scope="col"  align="left">
							<html:select  styleClass="testoNormale" property="orientamentoPagina" >
							<html:optionsCollection  property="listaOrientamentoPagina" value="codice" label="descrizione" />
							</html:select>
						</td>


            </tr>

			<tr>
                        <td colspan="4" scope="col" class="etichetta" align="left">&nbsp;</td>
            </tr>


		     <tr>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%">
							&nbsp;
                        </td>
                        <td   valign="top" scope="col" class="etichetta" align="left" width="20%"> Sezione titolo
<!-- 							<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />-->
                        </td>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%"> Intestazioni di colonna
<!-- 							<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />-->
                        </td>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%"> Sezione dati
<!-- 							<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />-->
                        </td>
             </tr>

			<tr>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%">
				           	<bean:message  key="statistiche.label.tipoCarattere" bundle="statisticheLabels" />
                        </td>

                        <td style="border-style:solid; border-width:thin"  scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="tipoCarattereTit" >
							<html:optionsCollection  property="listaTipoCarattereStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin"  scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="tipoCarattereInt" >
							<html:optionsCollection  property="listaTipoCarattereStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin"  scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="tipoCarattereDat" >
							<html:optionsCollection  property="listaTipoCarattereStr" value="codice" label="descrizione" />
							</html:select>
                        </td>

            </tr>

			<tr>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%">
				           	<bean:message  key="statistiche.label.stileCarattere" bundle="statisticheLabels" />
                        </td>

                        <td style="border-style:solid; border-width:thin"  scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="stileCarattereTit" >
							<html:optionsCollection  property="listaStileCarattereStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin"  scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="stileCarattereInt" >
							<html:optionsCollection  property="listaStileCarattereStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin"  scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="stileCarattereDat" >
							<html:optionsCollection  property="listaStileCarattereStr" value="codice" label="descrizione" />
							</html:select>
                        </td>

            </tr>

			<tr>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%">
				           	<bean:message  key="statistiche.label.dimensioneCarattere" bundle="statisticheLabels" />
                        </td>

                        <td style="border-style:solid; border-width:thin"  scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="dimensioneCarattereTit" >
							<html:optionsCollection  property="listaDimensioneCarattereStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin"  scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="dimensioneCarattereInt" >
							<html:optionsCollection  property="listaDimensioneCarattereStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin"  scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="dimensioneCarattereDat" >
							<html:optionsCollection  property="listaDimensioneCarattereStr" value="codice" label="descrizione" />
							</html:select>
                        </td>

            </tr>
			<tr>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%">
				           	<bean:message  key="statistiche.label.bordoCella" bundle="statisticheLabels" />
                        </td>

                        <td style="border-style:solid; border-width:thin" scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="bordiTit" >
							<html:optionsCollection  property="listaBordiStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin" scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="bordiInt" >
							<html:optionsCollection  property="listaBordiStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin" scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="bordiDat" >
							<html:optionsCollection  property="listaBordiStr" value="codice" label="descrizione" />
							</html:select>
                        </td>

            </tr>
			<tr>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%">
				           	<bean:message  key="statistiche.label.spessoreBordo" bundle="statisticheLabels" />
                        </td>

                        <td style="border-style:solid; border-width:thin"  scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="spessoreBordoTit" >
							<html:optionsCollection  property="listaSpessoreBordoStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin"  scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="spessoreBordoInt" >
							<html:optionsCollection  property="listaSpessoreBordoStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin"  scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="spessoreBordoDat" >
							<html:optionsCollection  property="listaSpessoreBordoStr" value="codice" label="descrizione" />
							</html:select>
                        </td>

            </tr>

			<tr>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%">
				           	<bean:message  key="statistiche.label.orientamentoFoglio" bundle="statisticheLabels" />
                        </td>

                        <td style="border-style:solid; border-width:thin" scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="orientamentoTit" >
							<html:optionsCollection  property="listaOrientamentoStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin" scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="orientamentoInt" >
							<html:optionsCollection  property="listaOrientamentoStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin" scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="orientamentoDat" >
							<html:optionsCollection  property="listaOrientamentoStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
            </tr>

			<tr>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%">
				           	<bean:message  key="statistiche.label.coloreBackgroundIntestazioni" bundle="statisticheLabels" />
                        </td>

                        <td style="border-style:solid; border-width:thin" scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="colorIntestazioniBckTit" >
							<html:optionsCollection  property="listaColorIntestazioniBckStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin" scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="colorIntestazioniBckInt" >
							<html:optionsCollection  property="listaColorIntestazioniBckStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin" scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="colorIntestazioniBckDat" >
							<html:optionsCollection  property="listaColorIntestazioniBckStr" value="codice" label="descrizione" />
							</html:select>
                        </td>

            </tr>



			<tr>
                        <td  valign="top" scope="col" class="etichetta" align="left" width="20%">
				           	<bean:message  key="statistiche.label.allineamento" bundle="statisticheLabels" />
                        </td>

                        <td style="border-style:solid; border-width:thin" scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="allineamentoTit" >
							<html:optionsCollection  property="listaAllineamentoStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin" scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="allineamentoInt" >
							<html:optionsCollection  property="listaAllineamentoStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
                        <td style="border-style:solid; border-width:thin" scope="col" class="etichetta" align="left">
							<html:select  styleClass="testoNormale" property="allineamentoDat" >
							<html:optionsCollection  property="listaAllineamentoStr" value="codice" label="descrizione" />
							</html:select>
                        </td>
            </tr>
           	<tr>
                        <td colspan="4" scope="col" class="etichetta" align="left">&nbsp;</td>
            </tr>
		</table>
</div>
   	  <!--
	 <div id="divFooterCommon">
			 tabella bottoni
           <table  width="100%"   border="0" style="height:40px" >
		     <tr>
				<td width="80" class="etichetta"><bean:message
					key="ricerca.label.elembloccoshort" bundle="acquisizioniLabels" /></td>
				<td width="150" class="testoNormale"><html:text
					property="elemXBlocchi" size="5"></html:text></td>

		     </tr>
      	  </table>
   	  	</div>
   	  	-->
		 <div id="divFooter">

			<!-- tabella bottoni -->
           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
             <sbn:checkAttivita idControllo="CERCA">
			<html:submit styleClass="pulsanti" property="methodStatistiche">
				<bean:message key="statistiche.label.creaExcel" bundle="statisticheLabels" />
			</html:submit>
			</sbn:checkAttivita>

             </td>
          </tr>
      	  </table>
	  			<!-- fine tabella bottoni -->
     	  </div>
	</sbn:navform>
</layout:page>
