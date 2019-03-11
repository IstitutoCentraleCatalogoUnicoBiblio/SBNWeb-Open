<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="gestioneStampeMessages" />
	<sbn:navform action="/gestionestampe/liste/stampaThesauroUtilizza.do">
	<div id="content">
	<table   width="100%"  align="center"  >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">




		 <table   width="100%" border="0">
		     <tr>
                       <td  width="15%" valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroUt.label.codThe" bundle="gestioneStampeLabels" /></div></td>
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
                        <bean:message  key="thesauroUt.label.insDal" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="insDal" size="10"></html:text>

                        </div></td>



                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="thesauroUt.label.insAl" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="insAl" size="10" ></html:text>

                        </div></td>


		     </tr>
		   </table>
		   	  <table   width="100%" border="0">
		     <tr>


                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="thesauroUt.label.aggDal" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="aggDal" size="10" ></html:text>

                        </div></td>


                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="thesauroUt.label.aggAl" bundle="gestioneStampeLabels" />
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
		     	 <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                   <bean:message key="thesauroUt.label.opz" bundle="gestioneStampeLabels" />
		 		</div></td>
                    </tr>
                    <tr>
                             <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroUt.label.opzSmpTit" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzSmpTit" style="width:40px">
						  <html:optionsCollection  property="listaOpzSmpTit" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                         </tr>
                    <tr>
                              <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroUt.label.opzSmpNoteTit" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzSmpNoteTit" style="width:40px">
						  <html:optionsCollection  property="listaOpzSmpNoteTit" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                         </tr>
                    <tr>
                                       <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroUt.label.opzSmpStringa" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzSmpStringa" style="width:40px">
						  <html:optionsCollection  property="listaOpzSmpStringa" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                         </tr>
                    <tr>
                              <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroUt.label.opzSmpNote" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzSmpNote" style="width:40px">
						  <html:optionsCollection  property="listaOpzSmpNote" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
                        </tr>
                    <tr>
                         <td  valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="thesauroUt.label.opzSmpThe" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">
						<html:select  styleClass="testoNormale"  property="opzSmpThe" style="width:40px">
						  <html:optionsCollection  property="listaOpzSmpThe" value="codice" label="descrizione" />
						  </html:select>
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
			<html:submit styleClass="pulsanti" property="cerca0">
				<bean:message key="ricerca.button.cerca" bundle="gestioneStampeLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="crea0">
				<bean:message key="ricerca.button.crea" bundle="gestioneStampeLabels" />
			</html:submit>
			 </td>
		  </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
