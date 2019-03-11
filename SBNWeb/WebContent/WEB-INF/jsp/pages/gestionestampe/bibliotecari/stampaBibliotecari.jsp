<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="gestioneStampeMessages" />
	<sbn:navform action="/gestionestampe/bibliotecari/stampaBibliotecari.do">
	<div id="content">
	<table   width="100%"  align="center"  >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">
	 		<table   width="100%"  align="center"  >
        	     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="bibliotecari.label.criteriDiRicerca" bundle="gestioneStampeLabels" />
						</div></td>
    	         </tr>
		     </table>
	 		<table   width="100%"  align="center"  >
        	     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="bibliotecari.label.codiceBiblioteca" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="codiceBiblioteca" size="5"  disabled="true"></html:text>
                        </td>
    	         </tr>
        	     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="bibliotecari.label.cognomeNome" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="cognomeNome" size="25"></html:text>
                        </td>
    	         </tr>
        	     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="bibliotecari.label.ufficioAppartenenza" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="ufficioAppartenenza" size="25"></html:text>
                        </td>
    	         </tr>
		     </table>

				</td>
       	        <!-- FINE tabella corpo COLONNA + LARGA -->
             </tr>
	     </table>
		<HR>
				<!-- TODO gv qui bisognerebbe mettere la combo dinamica per la selezione dei modelli -->
			<jsp:include flush="true" page="../common/tipoStampa.jsp" />
		<HR>
           <table align="center" border="0" style="height:40px" >
            <tr >
             <td>
			<html:submit styleClass="pulsanti" property="methodStampaBibliotecari">
				<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodStampaBibliotecari">
				<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
			</html:submit>
			 </td>
		  </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
