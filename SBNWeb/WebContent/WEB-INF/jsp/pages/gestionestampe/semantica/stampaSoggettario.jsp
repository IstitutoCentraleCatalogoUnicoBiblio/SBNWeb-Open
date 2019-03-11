<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="gestioneStampeMessages" />
	<sbn:navform action="/gestionestampe/soggettario/stampaSoggettario.do">
	<div id="content">
	<table   width="100%"  align="center"  >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">




		 <table   width="100%" border="0">
		     <tr>
                       <td  width="15%" valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="descrittori.label.codSogg" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">

				          <html:select  styleClass="testoNormale"  property="codSogg" style="width:100px">
						  <html:optionsCollection  property="listaCodSogg" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>


		     </tr>
		   </table>

		   <table   width="100%" border="0">
                 <tr>

               <td class="navigazione3" >&nbsp;</td>
		     </tr>

 </table>

		  	  <table   width="100%" border="0">
		     <tr>
                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="soggettario.label.insDal" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="insDal" size="10"></html:text>

                        </div></td>



                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="soggettario.label.insAl" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="insAl" size="10" ></html:text>

                        </div></td>


		     </tr>
		   </table>
		   	  <table   width="100%" border="0">
		     <tr>


                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="soggettario.label.aggDal" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="aggDal" size="10" ></html:text>

                        </div></td>


                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="soggettario.label.aggAl" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="aggAl" size="10" ></html:text>

                        </div></td>


		     </tr>

		   </table>

		   <table   width="100%" border="0">
                 <tr>

               <td class="navigazione3" >&nbsp;</td>
		     </tr>

 </table>
					  <table   width="100%" border="0">
	      <tr>
		     	<hr color="#dde8f0"/>
		  </tr>
		  </table>



	  <table   width="100%" border="0">
		     	<tr>
 				<td valign="top" width="50%" scope="col" ><div align="left" class="etichetta">
                    <table   width="100%" border="0">
	     				 <tr>

		     	 				<td valign="top" width="50%" scope="col" ><div align="left" class="etichetta">
                				<bean:message key="soggettario.label.opz" bundle="gestioneStampeLabels" />
		 						</div></td>
                  		 </tr>

						  <tr>
                          	   <td  width="20%" valign="top" scope="col"  ><div align="left" class="etichetta">
                        		<bean:message  key="soggettario.label.opzMan" bundle="gestioneStampeLabels" /></div></td>
                       			 <td  valign="top" scope="col" ><div align="left">
								<html:select  styleClass="testoNormale"  property="opzMan" style="width:40px">
								  <html:optionsCollection  property="listaOpzMan" value="codice" label="descrizione" />
								  </html:select>
                     	 		  </div></td>
                     		   </tr>
                    <tr>
                              <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="soggettario.label.opzColl" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzColl" style="width:40px">
						  <html:optionsCollection  property="listaOpzColl" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                         </tr>

                    <tr>
                              <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="soggettario.label.opzLegame" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzLegame" style="width:40px">
						  <html:optionsCollection  property="listaOpzLegame" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                        </tr>

                      </table>

                        </div></td>

                        <td valign="top" scope="col"><div align="left">
              		<table   width="100%" border="0">
		     <tr>
              			 <tr>
                         <td width="50%" valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="soggettario.label.opzBibl" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzBibl" style="width:40px">
						  <html:optionsCollection  property="listaOpzBibl" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                      </tr>

                      <tr>
                         <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="soggettario.label.opzNote" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzNote" style="width:40px">
						  <html:optionsCollection  property="listaOpzNote" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                      </tr>

                        <tr>
                         <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="soggettario.label.opzStringa" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzStringa" style="width:40px">
						  <html:optionsCollection  property="listaOpzStringa" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                      </tr>





		  </table>



                        </div></td>



		     </tr>

		   </table>





       	        <!-- FINE tabella corpo COLONNA + LARGA -->
             </tr>
	     </table>
	     	<HR>
			<jsp:include flush="true" page="../common/tipoStampa.jsp" />
			<HR>

           <table align="center" border="0" style="height:40px" >
            <tr >
             <td>
			<html:submit styleClass="pulsanti" property="methodStampaSoggettario">
				<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodStampaSoggettario">
				<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
			</html:submit>
			 </td>
		  </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
