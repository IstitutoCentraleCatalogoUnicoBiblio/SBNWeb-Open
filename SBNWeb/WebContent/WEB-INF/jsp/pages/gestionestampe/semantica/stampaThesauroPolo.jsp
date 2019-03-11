<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/gestionestampe/semantica/stampaThesauroPolo.do">
	<div id="divForm">
	<div id="divMessaggio"><sbn:errors bundle="gestioneStampeMessages" /><sbn:errors />
	</div>
	<div id="content">
	<table   width="100%"  align="center"  >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">





		 <table   width="100%" border="0">
		     <tr>
                       <td  width="15%" valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroPolo.label.codThe" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">

				          <html:select  styleClass="testoNormale"  property="codThe" style="width:100px">
						  <html:optionsCollection  property="listaCodThe" value="codice" label="descrizione" />
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
                        <bean:message  key="thesauroPolo.label.insDal" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="insDal" size="10"></html:text>

                        </div></td>



                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="thesauroPolo.label.insAl" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="insAl" size="10" ></html:text>

                        </div></td>


		     </tr>
		   </table>
		   	  <table   width="100%" border="0">
		     <tr>


                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="thesauroPolo.label.aggDal" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="aggDal" size="10" ></html:text>

                        </div></td>


                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="thesauroPolo.label.aggAl" bundle="gestioneStampeLabels" />
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

		     	 				<td valign="top" width="25%" scope="col" ><div align="left" class="etichetta">
                				<bean:message key="thesauroPolo.label.opz" bundle="gestioneStampeLabels" />
		 						</div></td>
                  		 </tr>

                    <tr>
                              <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroPolo.label.opzNoteThe" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzNoteThe" style="width:40px">
						  <html:optionsCollection  property="listaOpzNoteThe" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                         </tr>
                    <tr>
                    <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroPolo.label.opzTitoli" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzTitoli" style="width:40px">
						  <html:optionsCollection  property="listaOpzTitoli" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                         </tr>
                    <tr>
                         <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroPolo.label.opzTerminiColl" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzTerminiColl" style="width:40px">
						  <html:optionsCollection  property="listaOpzTerminiColl" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                        </tr>

                        <tr>
                              <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroPolo.label.opzLegamiTitoloDiBiblio" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzLegamiTitoloDiBiblio" style="width:40px">
						  <html:optionsCollection  property="listaOpzLegamiTitoloDiBiblio" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                         </tr>
                         <tr>
                         <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroPolo.label.opzFormeRinvio" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzFormeRinvio" style="width:40px">
						  <html:optionsCollection  property="listaOpzFormeRinvio" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                         </tr>
                    <tr>
                    <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroPolo.label.opzTerminiBiblio" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzTerminiBiblio" style="width:40px">
						  <html:optionsCollection  property="listaOpzTerminiBiblio" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                         </tr>
                    <tr>
                             <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroPolo.label.opzNoteTerminiColl" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzNoteTerminiColl" style="width:40px">
						  <html:optionsCollection  property="listaOpzNoteTerminiColl" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                        </tr>
				  <table   width="100%" border="0">
	   			 	 <tr>
		     			<hr color="#dde8f0"/>

						<bean-struts:size id="comboSize" name="stampaThesauroPoloForm" property="elencoModelli" />
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
							<html:hidden property="tipoModello" value="${stampaThesauroPoloForm.elencoModelli[0].jrxml}" />
							</td>
						</logic:lessThan>

                	</tr>
		  		  </table>



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
			<html:submit styleClass="pulsanti" property="methodStampaThesauroPolo">
				<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodStampaThesauroPolo">
				<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
			</html:submit>
			 </td>
		  </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
