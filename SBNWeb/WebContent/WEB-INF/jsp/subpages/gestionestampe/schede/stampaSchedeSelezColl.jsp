	<table   width="100%"  align="center"  >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">

		  <table   width="100%" border="0">
		     <tr>
                    <td valign="top" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="ricerca.label.codiceBibl" bundle="gestioneStampeLabels" />
                    </div></td>
                        <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="codiceBibl" size="5" disabled="true"></html:text>
                    </div></td>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaPiuInventari"><bean:message key="schede.label.stampaPiuInventari" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="chkStampaPiuInventari" property="chkStampaPiuInventari" />
		        	</td>
		     </tr>
		  </table>
		  <table   width="100%" border="0">
		     <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.status" bundle="gestioneStampeLabels" />
					</div></td>
                    <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="status">
						<html:optionsCollection  property="listaStatus" value="codice" label="descrizione" />
						</html:select>
                    </td>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.natura" bundle="gestioneStampeLabels" />
					</div></td>
                    <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="natura">
						<html:optionsCollection  property="listaNatura" value="codice" label="descrizione" />
						</html:select>
                    </td>
             </tr>
		  </table>
		  <HR>
		  <table   width="100%" border="0">
		     <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" ><U>
						<bean:message  key="schede.label.parametri" bundle="gestioneStampeLabels" /></U>
					</div></td>
             </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="stampaTitNonPoss"><bean:message key="schede.label.stampaTitNonPoss" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td align="left">
	       				<html:checkbox styleId="chkStampaTitNonPoss" property="chkStampaTitNonPoss" />
		        	</td>
		     </tr>
		     <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.cataloghiAttivi" bundle="gestioneStampeLabels" />
					</div></td>
             </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="catAttAutori"><bean:message key="schede.label.catAttAutori" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
	       				<html:checkbox styleId="chkCatAttAutori" property="chkCatAttAutori" />
		        	</div></td>
                        <td><div  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="tipoAutore">
						<html:optionsCollection  property="listaTipoAutore" value="codice" label="descrizione" />
						</html:select>
                        </div></td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="catAttTopografico"><bean:message key="schede.label.catAttTopografico" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td><div align="left">
	       				<html:checkbox styleId="chkCatAttTopografico" property="chkCatAttTopografico" />
		        	</div></td>
			   	    <td><div align="left" class="etichetta">
	             		<label for="catAttSoggetti"><bean:message key="schede.label.catAttSoggetti" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
	       				<html:checkbox styleId="chkCatAttSoggetti" property="chkCatAttSoggetti" />
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
	             		<label for="catAttTitoli"><bean:message key="schede.label.catAttTitoli" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
	       				<html:checkbox styleId="chkCatAttTitoli" property="chkCatAttTitoli" />
		        	</div></td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="catAttClassificazioni"><bean:message key="schede.label.catAttClassificazioni" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
	       				<html:checkbox styleId="chkCatAttClassificazioni" property="chkCatAttClassificazioni" />
		        	</div></td>
			   	    <td><div align="left" class="etichetta">
	             		<label for="catAttEditori"><bean:message key="schede.label.catAttEditori" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
	       				<html:checkbox styleId="chkCatAttEditori" property="chkCatAttEditori" />
		        	</div></td>
			   	    <td><div align="left" class="etichetta">
	             		<label for="catAttPossessori"><bean:message key="schede.label.catAttPossessori" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
	       				<html:checkbox styleId="chkCatAttPossessori" property="chkCatAttPossessori" />
		        	</div></td>
		     </tr>
		  </table>
		  <table   width="100%" border="0">
		     <tr>
                    <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.collScheda" bundle="gestioneStampeLabels" />
					</div></td>
             </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="collSchPrincipale"><bean:message key="schede.label.collSchPrincipale" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td><div align="left">
	       				<html:checkbox styleId="chkCollSchPrincipale" property="chkCollSchPrincipale" />
		        	</div></td>
			   	    <td><div align="left" class="etichetta">
	             		<bean:message key="schede.label.nCopie" bundle="gestioneStampeLabels" />
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
			 	    <html:text styleId="testoNormale" property="numCollSchPrincipale" size="10" maxlength="10"></html:text>
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
	             		<label for="collSchTopografico"><bean:message key="schede.label.collSchTopografico" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td><div align="left">
	       				<html:checkbox styleId="chkCollSchTopografico" property="chkCollSchTopografico" />
		        	</div></td>
			   	    <td><div align="left" class="etichetta">
	             		<bean:message key="schede.label.nCopie" bundle="gestioneStampeLabels" />
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
			 	    <html:text styleId="testoNormale" property="numCollSchTopografico" size="10" maxlength="10"></html:text>
			 	    </div></td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="collSchSoggetti"><bean:message key="schede.label.collSchSoggetti" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td><div align="left">
	       				<html:checkbox styleId="chkCollSchSoggetti" property="chkCollSchSoggetti" />
		        	</div></td>
			   	    <td><div align="left" class="etichetta">
	             		<bean:message key="schede.label.nCopie" bundle="gestioneStampeLabels" />
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
			 	    <html:text styleId="testoNormale" property="numCollSchSoggetti" size="10" maxlength="10"></html:text>
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
	             		<label for="collSchTitoli"><bean:message key="schede.label.collSchTitoli" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td><div align="left">
	       				<html:checkbox styleId="chkCollSchTitoli" property="chkCollSchTitoli" />
		        	</div></td>
			   	    <td><div align="left" class="etichetta">
	             		<bean:message key="schede.label.nCopie" bundle="gestioneStampeLabels" />
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
			 	    <html:text styleId="testoNormale" property="numCollSchTitoli" size="10" maxlength="10"></html:text>
			 	    </div></td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="collSchClassificazioni"><bean:message key="schede.label.collSchClassificazioni" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td><div align="left">
	       				<html:checkbox styleId="chkCollSchClassificazioni" property="chkCollSchClassificazioni" />
		        	</div></td>
			   	    <td><div align="left" class="etichetta">
	             		<bean:message key="schede.label.nCopie" bundle="gestioneStampeLabels" />
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
			 	    <html:text styleId="testoNormale" property="numCollSchClassificazioni" size="10" maxlength="10"></html:text>
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
	             		<label for="collSchRichiami"><bean:message key="schede.label.collSchRichiami" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td><div align="left">
	       				<html:checkbox styleId="chkCollSchRichiami" property="chkCollSchRichiami" />
		        	</div></td>
			   	    <td><div align="left" class="etichetta">
	             		<bean:message key="schede.label.nCopie" bundle="gestioneStampeLabels" />
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
			 	    <html:text styleId="testoNormale" property="numCollSchRichiami" size="10" maxlength="10"></html:text>
			 	    </div></td>
		     </tr>
             <tr>
			   	    <td><div align="left" class="etichetta">
	             		<label for="collSchEditori"><bean:message key="schede.label.collSchEditori" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td><div align="left">
	       				<html:checkbox styleId="chkCollSchEditori" property="chkCollSchEditori" />
		        	</div></td>
			   	    <td><div align="left" class="etichetta">
	             		<bean:message key="schede.label.nCopie" bundle="gestioneStampeLabels" />
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
			 	    <html:text styleId="testoNormale" property="numCollSchEditori" size="10" maxlength="10"></html:text>
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
	             		<label for="collSchPossessori"><bean:message key="schede.label.collSchPossessori" bundle="gestioneStampeLabels" /> </label>
			 	    </div></td>
			        <td><div align="left">
	       				<html:checkbox styleId="chkCollSchPossessori" property="chkCollSchPossessori" />
		        	</div></td>
			   	    <td><div align="left" class="etichetta">
	             		<bean:message key="schede.label.nCopie" bundle="gestioneStampeLabels" />
			 	    </div></td>
			   	    <td><div align="left" class="etichetta">
			 	    <html:text styleId="testoNormale" property="numCollSchPossessori" size="10" maxlength="10"></html:text>
			 	    </div></td>
		     </tr>
            </table>
     </td>
   </tr>
</table>
