<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="gestioneStampeMessages" />
	<sbn:navform action="/gestionestampe/richiesteill/stampaRichiesteIll.do">
	<div id="content">
	<table   width="100%"  align="center"  >
       	     <tr>
                <td align="left" valign="top" width="100%">
				<table   width="100%" border="0">
        	     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteIll.label.tabulato" bundle="gestioneStampeLabels" />
						</div></td>
    	         </tr>
				</table>
				<table   width="100%" border="0">
        	     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteIll.label.tipiServizio" bundle="gestioneStampeLabels" />
						</div></td>
    	         </tr>
				</table>
			<table>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaLocalizzazioneRich"><bean:message key="richiesteIll.label.localizzazioneRich" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaLocalizzazioneRich" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaLocalizzazioneBibl"><bean:message key="richiesteIll.label.localizzazioneBibl" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaLocalizzazioneBibl" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaStimaCostoILL"><bean:message key="richiesteIll.label.stimaCostoILL" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaStimaCostoILL" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaPrestitoInterbibl"><bean:message key="richiesteIll.label.prestitoInterbibl" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaPrestitoInterbibl" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaPrestitoInterbiblTratt"><bean:message key="richiesteIll.label.prestitoInterbiblTratt" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaPrestitoInterbiblTratt" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaPrestitoMatMult"><bean:message key="richiesteIll.label.prestitoMatMult" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaPrestitoMatMult" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaRiproduzioneRich"><bean:message key="richiesteIll.label.riproduzioneRich" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaRiproduzioneRich" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaRiproduzioneTratt"><bean:message key="richiesteIll.label.riproduzioneTratt" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaRiproduzioneTratt" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaServizioInterbibl"><bean:message key="richiesteIll.label.servizioInterbibl" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaServizioInterbibl" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaServizioSemplice"><bean:message key="richiesteIll.label.servizioSemplice" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaServizioSemplice" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaServizioInterbiblPrecat"><bean:message key="richiesteIll.label.servizioInterbiblPrecat" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaServizioInterbiblPrecat" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaTutte"><bean:message key="richiesteIll.label.tutte" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaTutte" />
		        	</td>
		     </tr>
			</table>
			<HR>
				<table   width="100%" border="0">
        	     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteIll.label.periodoData" bundle="gestioneStampeLabels" />
						</div></td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteIll.label.dal" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dal" size="10" maxlength="10"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteIll.label.al" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="al" size="10" maxlength="10"></html:text>
                        </td>
    	         </tr>
    	        </table>
				<table   width="100%" border="0">
    	         <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteIll.label.statoRichiesta" bundle="gestioneStampeLabels" />
					</div></td>
                    <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="statoRichiesta">
						<html:optionsCollection  property="listaStatoRichiesta" value="codice" label="descrizione" />
						</html:select>
                    </td>
                 </tr>
    	         <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteIll.label.statoCircolazione" bundle="gestioneStampeLabels" />
					</div></td>
                    <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="statoCircolazione">
						<html:optionsCollection  property="listaStatoCircolazione" value="codice" label="descrizione" />
						</html:select>
                    </td>
                 </tr>
    	         <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteIll.label.bibliotecaRichiedente" bundle="gestioneStampeLabels" />
					</div></td>
                    <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="bibliotecaRichiedente">
						<html:optionsCollection  property="listaBibliotecaRichiedente" value="codice" label="descrizione" />
						</html:select>
                    </td>
                 </tr>
				</table>
				<table   width="100%" border="0">
    	         <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteIll.label.codiceUtente" bundle="gestioneStampeLabels" />
					</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="codiceUtente1a" size="10" maxlength="10"></html:text>
                        </td>
					</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="codiceUtente1b" size="6" maxlength="10"></html:text>
                        </td>
			             <td>
						<html:submit styleClass="pulsanti" property="methodStampaRichiesteIll">
							<bean:message key="button.lettore" bundle="gestioneStampeLabels" />
						</html:submit>
						</td>
                 </tr>
    	         <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
					</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="codiceUtente2a" size="10" maxlength="10"></html:text>
                        </td>
					</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="codiceUtente2b" size="6" maxlength="10"></html:text>
                        </td>
						<td>
						<html:submit styleClass="pulsanti" property="methodStampaRichiesteIll">
							<bean:message key="button.documento" bundle="gestioneStampeLabels" />
						</html:submit>
						 </td>
                 </tr>
				</table>

				</td>
             </tr>
	     </table>
	     	<HR>
			<jsp:include flush="true" page="../common/tipoStampa.jsp" />
			<HR>
           <table align="center" border="0" style="height:40px" >
            <tr >
             <td>
			<html:submit styleClass="pulsanti" property="methodStampaRichiesteIll">
				<bean:message key="button.ok" bundle="gestioneStampeLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodStampaRichiesteIll">
				<bean:message key="button.chiudi" bundle="gestioneStampeLabels" />
			</html:submit>
			 </td>
		  </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
