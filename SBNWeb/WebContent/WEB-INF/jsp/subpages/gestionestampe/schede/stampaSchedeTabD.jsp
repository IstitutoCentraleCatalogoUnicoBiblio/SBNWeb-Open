<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


		  <!-- TABELLA DI TAG: da eliminare integralmente se non presenti -->
			<table width="100%"  border="0"  cellpadding="0" cellspacing="0">
              <tr>
  				 <!-- COLONNA TAG OFF: da replicare per le altre colonne   -->
                <td>
					<table width="100%" border="0"  cellpadding="0" cellspacing="0" >
            		  <tr>
             		   <td style="width: 8px;">
              		    <table  border="0" cellpadding="0" cellspacing="0" style="width: 8px;">
               		     <tr>
                  		    	<td style="width: 8px; height: 8px;" class="tagSxTopOff" >&nbsp;</td>
                  		  </tr>
                  		  <tr>
                		      <td class="tagBottomOffA" style="width: 8px; height: 8px;" >&nbsp;</td>
                 		  </tr>
             		    </table>
             		   </td>
     				 <!-- valorizzare correttamente il link del tag -->
              		  <td colspan="3" class="tagBottomOff2" style="width: auto;">
						<bean-struts:define id="varLink" value="A" />
			            <html:link  action="/gestionestampe/schede/stampaSchede.do" paramId="paramLink" paramName="varLink" >
							<bean:message  key="schede.label.parametri" bundle="gestioneStampeLabels" />
			            </html:link>
              		  </td>
              		  <td style="width: 8px;">
               		   <table  border="0" cellpadding="0" cellspacing="0" style="width: 8px;">
                 		   <tr>

                     		 <td style="width: 8px; height: 8px;" class="tagDxTopOff" >&nbsp;</td>
                  		  </tr>
                  		  <tr>

                 		  <td style="width: 8px; height: 8px;" class="tagBottomOffB">&nbsp;</td>
                  		  </tr>
                	</table>
             		   </td>
            		  </tr>
           		 </table>
				</td>
		 		 <!-- fine COLONNA TAG OFF-->
  				 <!-- COLONNA TAG OFF: da replicare per le altre colonne   -->
                <td>
					<table width="100%" border="0"  cellpadding="0" cellspacing="0" >
            		  <tr>
             		   <td style="width: 8px;">
              		    <table  border="0" cellpadding="0" cellspacing="0" style="width: 8px;">
               		     <tr>
                  		    	<td style="width: 8px; height: 8px;" class="tagSxTopOff" >&nbsp;</td>
                  		  </tr>
                  		  <tr>
                		      <td class="tagBottomOffA" style="width: 8px; height: 8px;" >&nbsp;</td>
                 		  </tr>
             		    </table>
             		   </td>
     				 <!-- valorizzare correttamente il link del tag -->
              		  <td colspan="3" class="tagBottomOff2" style="width: auto;">
						<bean-struts:define id="varLink" value="B" />
			            <html:link  action="/gestionestampe/schede/stampaSchede.do" paramId="paramLink" paramName="varLink" >
							<bean:message  key="schede.label.selezionePerCollocazione" bundle="gestioneStampeLabels" />
			            </html:link>
              		  </td>
              		  <td style="width: 8px;">
               		   <table  border="0" cellpadding="0" cellspacing="0" style="width: 8px;">
                 		   <tr>

                     		 <td style="width: 8px; height: 8px;" class="tagDxTopOff" >&nbsp;</td>
                  		  </tr>
                  		  <tr>

                 		  <td style="width: 8px; height: 8px;" class="tagBottomOffB">&nbsp;</td>
                  		  </tr>
                	</table>
             		   </td>
            		  </tr>
           		 </table>
				</td>
		 		 <!-- fine COLONNA TAG OFF-->
  				 <!-- COLONNA TAG OFF: da replicare per le altre colonne   -->
                <td>
					<table width="100%" border="0"  cellpadding="0" cellspacing="0" >
            		  <tr>
             		   <td style="width: 8px;">
              		    <table  border="0" cellpadding="0" cellspacing="0" style="width: 8px;">
               		     <tr>
                  		    	<td style="width: 8px; height: 8px;" class="tagSxTopOff" >&nbsp;</td>
                  		  </tr>
                  		  <tr>
                		      <td class="tagBottomOffA" style="width: 8px; height: 8px;" >&nbsp;</td>
                 		  </tr>
             		    </table>
             		   </td>
     				 <!-- valorizzare correttamente il link del tag -->
              		  <td colspan="3" class="tagBottomOff2" style="width: auto;">
						<bean-struts:define id="varLink" value="C" />
			            <html:link  action="/gestionestampe/schede/stampaSchede.do" paramId="paramLink" paramName="varLink" >
							<bean:message  key="schede.label.selezionePerInventari" bundle="gestioneStampeLabels" />
			            </html:link>
              		  </td>
              		  <td style="width: 8px;">
               		   <table  border="0" cellpadding="0" cellspacing="0" style="width: 8px;">
                 		   <tr>

                     		 <td style="width: 8px; height: 8px;" class="tagDxTopOff" >&nbsp;</td>
                  		  </tr>
                  		  <tr>

                 		  <td style="width: 8px; height: 8px;" class="tagBottomOffB">&nbsp;</td>
                  		  </tr>
                	</table>
             		   </td>
            		  </tr>
           		 </table>
				</td>
		 		 <!-- fine COLONNA TAG OFF-->
				 <!-- COLONNA TAG ON -->
				    <td>
					<table width="100%" border="0"  cellpadding="0" cellspacing="0" >
            		  <tr>
             		   <td style="width: 8px;">
              		    <table  border="0" cellpadding="0" cellspacing="0" style="width: 8px;">
               		     <tr>
                  		    	<td style="width: 8px; height: 8px;" class="tagSxTopOn" >&nbsp;</td>
                  		  </tr>
                  		  <tr>
                		      <td class="tagBottomOnA" style="width: 8px; height: 8px;" >&nbsp;</td>
                 		  </tr>
             		   </table>
             		   </td>
       				 <!-- valorizzare correttamente la voce del tag -->
              		  <td colspan="3" class="tagBottomOn2" style="width: auto;"><bean:message  key="schede.label.identificativiTitoli" bundle="gestioneStampeLabels" /></td>
              		  <td style="width: 8px;">
               		   <table  border="0" cellpadding="0" cellspacing="0" style="width: 8px;">
                 		   <tr>

                     		 <td style="width: 8px; height: 8px;" class="tagDxTopOn" >&nbsp;</td>
                  		  </tr>
                  		  <tr>

                 		  <td style="width: 8px; height: 8px;" class="tagBottomOnB">&nbsp;</td>
                  		  </tr>
                	</table>
             		   </td>
            		  </tr>
           		 </table>
				</td>
			  <!-- FINE COLONNA TAG ON -->
               <!-- COLONNA TAG RESIDUO: specificare la percentuale del WIDTH con un valore numerico o con auto PER LE PAGINE CON POCHI TAG -->
			   <td style="width: auto;" class="tagDxBottomFinale">&nbsp;</td>
               <!-- FINE TAG RESIDUO -->

               <!-- FINE TAG -->
            	 </tr>
           		 </table>
		   <!-- FINE  TABELLA DI TAG -->