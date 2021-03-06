<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="gestioneStampeMessages" />
	<sbn:navform action="/gestionestampe/richiesteloc/stampaRichiesteLoc.do">
	<div id="content">
	<table   width="100%"  align="center"  >
       	     <tr>
                <td align="left" valign="top" width="100%">
				<table   width="100%" border="0">
        	     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteLoc.label.tabulato" bundle="gestioneStampeLabels" />
						</div></td>
    	         </tr>
				</table>
				<table   width="100%" border="0">
        	     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteLoc.label.tipiServizio" bundle="gestioneStampeLabels" />
						</div></td>
    	         </tr>
				</table>
			<table>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaConsultazioneAnt"><bean:message key="richiesteLoc.label.consultazioneAnt" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaConsultazioneAnt" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaConsultazioneMan"><bean:message key="richiesteLoc.label.consultazioneMan" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaConsultazioneMan" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaConsultazioneMag"><bean:message key="richiesteLoc.label.consultazioneMag" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaConsultazioneMag" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaConsultazioneAudio"><bean:message key="richiesteLoc.label.consultazioneAudio" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaConsultazioneAudio" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaLocalizzazioneRich"><bean:message key="richiesteLoc.label.localizzazioneRich" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaLocalizzazioneRich" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaLocalizzazioneBibl"><bean:message key="richiesteLoc.label.localizzazioneBibl" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaLocalizzazioneBibl" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaLocalizzazione"><bean:message key="richiesteLoc.label.localizzazione" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaLocalizzazione" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaStimaCostoILL"><bean:message key="richiesteLoc.label.stimaCostoILL" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaStimaCostoILL" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaStimaCosti"><bean:message key="richiesteLoc.label.stimaCosti" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaStimaCosti" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaPrestitoInterbibl"><bean:message key="richiesteLoc.label.prestitoInterbibl" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaPrestitoInterbibl" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaPrestitoInterbiblTratt"><bean:message key="richiesteLoc.label.prestitoInterbiblTratt" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaPrestitoInterbiblTratt" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaPrestitoMatMag"><bean:message key="richiesteLoc.label.prestitoMatMag" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaPrestitoMatMag" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaPrestitoNotturno"><bean:message key="richiesteLoc.label.prestitoNotturno" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaPrestitoNotturno" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaPrestitoMatMult"><bean:message key="richiesteLoc.label.prestitoMatMult" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaPrestitoMatMult" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaPrestitoMatScaf"><bean:message key="richiesteLoc.label.prestitoMatScaf" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaPrestitoMatScaf" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaRiproduzioneRich"><bean:message key="richiesteLoc.label.riproduzioneRich" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaRiproduzioneRich" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaRiproduzioneTratt"><bean:message key="richiesteLoc.label.riproduzioneTratt" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaRiproduzioneTratt" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaRiproduzioneMan"><bean:message key="richiesteLoc.label.riproduzioneMan" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaRiproduzioneMan" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaRiproduzione"><bean:message key="richiesteLoc.label.riproduzione" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaRiproduzione" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaRiproduzioneAudio"><bean:message key="richiesteLoc.label.riproduzioneAudio" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaRiproduzioneAudio" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaServizioInterbibl"><bean:message key="richiesteLoc.label.servizioInterbibl" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaServizioInterbibl" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaAccessoSala"><bean:message key="richiesteLoc.label.accessoSala" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaAccessoSala" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaServizioSemplice"><bean:message key="richiesteLoc.label.servizioSemplice" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaServizioSemplice" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaServizioInterbiblPrecat"><bean:message key="richiesteLoc.label.servizioInterbiblPrecat" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaServizioInterbiblPrecat" />
		        	</td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaTutte"><bean:message key="richiesteLoc.label.tutte" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="testoNormale" property="chkStampaTutte" />
		        	</td>
		     </tr>
			</table>
			<HR>
				<table   width="100%" border="0">
				<tr>
    					<td valign="top" scope="col" >
     					<div align="left" class="etichetta">
	     					<bean:message key="richiesteLoc.label.locali" bundle="gestioneStampeLabels" />
	     					<html:radio property="materiale" value="richiesteLoc.label.locali" />
						</div>
						</td>
     					<td valign="top" scope="col" >
     					<div align="left" class="etichetta">
	     					<bean:message key="richiesteLoc.label.Ill" bundle="gestioneStampeLabels" />
	     					<html:radio property="materiale" value="richiesteLoc.label.Ill" />
						</div>
						</td>
				</tr>
        	     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteLoc.label.periodoData" bundle="gestioneStampeLabels" />
						</div></td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteLoc.label.dal" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dal" size="10" maxlength="10"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteLoc.label.al" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="al" size="10" maxlength="10"></html:text>
                        </td>
    	         </tr>
    	        </table>
				<table   width="100%" border="0">
    	         <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteLoc.label.attivita" bundle="gestioneStampeLabels" />
					</div></td>
                    <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="attivita">
						<html:optionsCollection  property="listaAttivita" value="codice" label="descrizione" />
						</html:select>
                    </td>
                 </tr>
    	         <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteLoc.label.statoMovimenti" bundle="gestioneStampeLabels" />
					</div></td>
                    <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="statoMovimenti">
						<html:optionsCollection  property="listaStatoMovimenti" value="codice" label="descrizione" />
						</html:select>
                    </td>
                 </tr>
    	         <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteLoc.label.statoRichiesta" bundle="gestioneStampeLabels" />
					</div></td>
                    <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="statoRichiesta">
						<html:optionsCollection  property="listaStatoRichiesta" value="codice" label="descrizione" />
						</html:select>
                    </td>
                 </tr>
				</table>
				<table   width="100%" border="0">
    	         <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="richiesteLoc.label.codiceUtente" bundle="gestioneStampeLabels" />
					</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="codiceUtente1a" size="10" maxlength="10"></html:text>
                        </td>
					</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="codiceUtente1b" size="6" maxlength="10"></html:text>
                        </td>
			             <td>
						<html:submit styleClass="pulsanti" property="methodStampaRichiesteLoc">
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
						<html:submit styleClass="pulsanti" property="methodStampaRichiesteLoc">
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
			<html:submit styleClass="pulsanti" property="methodStampaRichiesteLoc">
				<bean:message key="button.ok" bundle="gestioneStampeLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodStampaRichiesteLoc">
				<bean:message key="button.chiudi" bundle="gestioneStampeLabels" />
			</html:submit>
			 </td>
		  </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
