<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="gestioneStampeMessages" />
	<sbn:navform action="/gestionestampe/liste/stampaDescrittoriInutilizzati.do">
	<div id="content">
	<table   width="100%"  align="center"  >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">

		 		<table   width="100%" border="0">
                 <tr>

               <td class="navigazione3" >&nbsp;</td>
		     </tr>

 </table><table   width="100%" border="0">
                 <tr>

               <td class="navigazione3" >&nbsp;</td>
		     </tr>

 </table>



		 		  		<table width="100%" border="0">
	<tr>
		<td width="17%" class="etichetta">
		 <table   width="100%" border="0">
		     <tr>
                       <td width="15%" class="etichetta" align="right"><div align="left" class="etichetta">
                        	<bean:message key="descrittriIn.label.codSogg" bundle="gestioneStampeLabels" />
                       </div></td>


		     </tr>
		   </table>
		</td>

		<td width="53%" class="etichetta">

		 <table   width="100%" border="0">
		     <tr>
                       <td width="15%" class="etichetta" align="right"><div align="left" class="etichetta">
                       	<bean:message key="descrittriIn.label.SoggFI" bundle="gestioneStampeLabels" />

                       </div></td>
						  <td width="30%" class="etichetta" align="right"><div align="left" class="etichetta">

        <html:radio property="tipoRicerca" value="SoggFI" />
                       </div></td>

		     </tr>
		     	     <tr>
                       <td width="15%" class="etichetta" align="right"><div align="left" class="etichetta">
                       <bean:message key="descrittriIn.label.SoggPA" bundle="gestioneStampeLabels" />

                       </div></td>
						  <td width="30%" class="etichetta" align="right"><div align="left" class="etichetta">

       <html:radio property="tipoRicerca" value="SoggPA" />
                       </div></td>

		     </tr>
		          	     <tr>
                       <td width="15%" class="etichetta" align="right"><div align="left" class="etichetta">
                      <bean:message key="descrittriIn.label.SoggLCL" bundle="gestioneStampeLabels" />

                       </div></td>
						  <td width="30%" class="etichetta" align="right"><div align="left" class="etichetta">

       <html:radio property="tipoRicerca" value="SoggLCL" />
                       </div></td>

		     </tr>

		     		          	     <tr>
                       <td width="15%" class="etichetta" align="right"><div align="left" class="etichetta">
                     	<bean:message key="descrittriIn.label.SoggNLBR" bundle="gestioneStampeLabels" />

                       </div></td>
						  <td width="30%" class="etichetta" align="right"><div align="left" class="etichetta">

      <html:radio property="tipoRicerca" value="SoggNLBR" />
                       </div></td>

		     </tr>




		   </table>













		</td>

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
