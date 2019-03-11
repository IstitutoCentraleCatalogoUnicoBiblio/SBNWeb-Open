<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="gestioneStampeMessages" />
	<sbn:navform action="/gestionestampe/schede/stampaSchede.do">
	<div id="content">
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
			<HR>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" ><U>
						<bean:message  key="schede.label.selezionePerCollocazione" bundle="gestioneStampeLabels" /></U>
						</div></td>
             </tr>
		</table>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.dallaSezione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dallaSezione" size="10" maxlength="10"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.dallaCollocazione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dallaCollocazione" size="10" maxlength="50"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.specificazioneDallaCollocazione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="specificazioneDallaCollocazione" size="10" maxlength="50"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.allaCollocazione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="allaCollocazione" size="10" maxlength="50"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.specificazioneAllaCollocazione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="specificazioneAllaCollocazione" size="10" maxlength="50"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
             </tr>
		</table>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.serie" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="serie">
						<html:optionsCollection  property="listaSerie" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.dalNumero" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dalNumero" size="10" maxlength="50"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.alNumero" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="alNumero" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		</table>
		  <HR>
		  <table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" ><U>
						<bean:message  key="schede.label.selezionePerInventari" bundle="gestioneStampeLabels" /></U>
						</div></td>
             </tr>
		</table>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left" width="17%"> <div class="etichetta" >
						<bean:message  key="schede.label.selInvSerie" bundle="gestioneStampeLabels" />
						</div></td>
                        <td   valign="top" scope="col" align="left" width="16%"> <div class="etichetta" >
						<bean:message  key="schede.label.selInvNumero" bundle="gestioneStampeLabels" />
						</div></td>
                        <td   valign="top" scope="col" align="left" width="17%"> <div class="etichetta" >
						<bean:message  key="schede.label.selInvSerie" bundle="gestioneStampeLabels" />
						</div></td>
                        <td   valign="top" scope="col" align="left" width="16%"> <div class="etichetta" >
						<bean:message  key="schede.label.selInvNumero" bundle="gestioneStampeLabels" />
						</div></td>
                        <td   valign="top" scope="col" align="left" width="17%"> <div class="etichetta" >
						<bean:message  key="schede.label.selInvSerie" bundle="gestioneStampeLabels" />
						</div></td>
                        <td   valign="top" scope="col" align="left" width="17%"> <div class="etichetta" >
						<bean:message  key="schede.label.selInvNumero" bundle="gestioneStampeLabels" />
						</div></td>
             </tr>
		     <tr>
                        <td  valign="top" scope="col" align="left">01
						<html:select  styleClass="testoNormale"  property="selInvSerie01">
						<html:optionsCollection  property="listaSelInvSerie01" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero01" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">02
						<html:select  styleClass="testoNormale"  property="selInvSerie02">
						<html:optionsCollection  property="listaSelInvSerie02" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero02" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">03
						<html:select  styleClass="testoNormale"  property="selInvSerie03">
						<html:optionsCollection  property="listaSelInvSerie03" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero03" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td  valign="top" scope="col" align="left">04
						<html:select  styleClass="testoNormale"  property="selInvSerie04">
						<html:optionsCollection  property="listaSelInvSerie04" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero04" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">05
						<html:select  styleClass="testoNormale"  property="selInvSerie05">
						<html:optionsCollection  property="listaSelInvSerie05" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero05" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">06
						<html:select  styleClass="testoNormale"  property="selInvSerie06">
						<html:optionsCollection  property="listaSelInvSerie06" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero06" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td  valign="top" scope="col" align="left">07
						<html:select  styleClass="testoNormale"  property="selInvSerie07">
						<html:optionsCollection  property="listaSelInvSerie07" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero07" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">08
						<html:select  styleClass="testoNormale"  property="selInvSerie08">
						<html:optionsCollection  property="listaSelInvSerie08" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero08" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">09
						<html:select  styleClass="testoNormale"  property="selInvSerie09">
						<html:optionsCollection  property="listaSelInvSerie09" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero09" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td  valign="top" scope="col" align="left">10
						<html:select  styleClass="testoNormale"  property="selInvSerie10">
						<html:optionsCollection  property="listaSelInvSerie10" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero10" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">11
						<html:select  styleClass="testoNormale"  property="selInvSerie11">
						<html:optionsCollection  property="listaSelInvSerie11" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero11" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">12
						<html:select  styleClass="testoNormale"  property="selInvSerie12">
						<html:optionsCollection  property="listaSelInvSerie12" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero12" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td  valign="top" scope="col" align="left">13
						<html:select  styleClass="testoNormale"  property="selInvSerie13">
						<html:optionsCollection  property="listaSelInvSerie13" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero13" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">14
						<html:select  styleClass="testoNormale"  property="selInvSerie14">
						<html:optionsCollection  property="listaSelInvSerie14" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero14" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">15
						<html:select  styleClass="testoNormale"  property="selInvSerie15">
						<html:optionsCollection  property="listaSelInvSerie15" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero15" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td  valign="top" scope="col" align="left">16
						<html:select  styleClass="testoNormale"  property="selInvSerie16">
						<html:optionsCollection  property="listaSelInvSerie16" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero16" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">17
						<html:select  styleClass="testoNormale"  property="selInvSerie17">
						<html:optionsCollection  property="listaSelInvSerie17" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero17" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">18
						<html:select  styleClass="testoNormale"  property="selInvSerie18">
						<html:optionsCollection  property="listaSelInvSerie18" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero18" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td  valign="top" scope="col" align="left">19
						<html:select  styleClass="testoNormale"  property="selInvSerie19">
						<html:optionsCollection  property="listaSelInvSerie19" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero19" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">20
						<html:select  styleClass="testoNormale"  property="selInvSerie20">
						<html:optionsCollection  property="listaSelInvSerie20" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero20" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">21
						<html:select  styleClass="testoNormale"  property="selInvSerie21">
						<html:optionsCollection  property="listaSelInvSerie21" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero21" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td  valign="top" scope="col" align="left">22
						<html:select  styleClass="testoNormale"  property="selInvSerie22">
						<html:optionsCollection  property="listaSelInvSerie22" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero22" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">23
						<html:select  styleClass="testoNormale"  property="selInvSerie23">
						<html:optionsCollection  property="listaSelInvSerie23" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero23" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">24
						<html:select  styleClass="testoNormale"  property="selInvSerie24">
						<html:optionsCollection  property="listaSelInvSerie24" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero24" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td  valign="top" scope="col" align="left">25
						<html:select  styleClass="testoNormale"  property="selInvSerie25">
						<html:optionsCollection  property="listaSelInvSerie25" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero25" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">26
						<html:select  styleClass="testoNormale"  property="selInvSerie26">
						<html:optionsCollection  property="listaSelInvSerie26" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero26" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">27
						<html:select  styleClass="testoNormale"  property="selInvSerie27">
						<html:optionsCollection  property="listaSelInvSerie27" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero27" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td  valign="top" scope="col" align="left">28
						<html:select  styleClass="testoNormale"  property="selInvSerie28">
						<html:optionsCollection  property="listaSelInvSerie28" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero28" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">29
						<html:select  styleClass="testoNormale"  property="selInvSerie29">
						<html:optionsCollection  property="listaSelInvSerie29" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero29" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">30
						<html:select  styleClass="testoNormale"  property="selInvSerie30">
						<html:optionsCollection  property="listaSelInvSerie30" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero30" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td  valign="top" scope="col" align="left">31
						<html:select  styleClass="testoNormale"  property="selInvSerie31">
						<html:optionsCollection  property="listaSelInvSerie31" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero31" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">32
						<html:select  styleClass="testoNormale"  property="selInvSerie32">
						<html:optionsCollection  property="listaSelInvSerie32" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero32" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">33
						<html:select  styleClass="testoNormale"  property="selInvSerie33">
						<html:optionsCollection  property="listaSelInvSerie33" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero33" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td  valign="top" scope="col" align="left">34
						<html:select  styleClass="testoNormale"  property="selInvSerie34">
						<html:optionsCollection  property="listaSelInvSerie34" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero34" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">35
						<html:select  styleClass="testoNormale"  property="selInvSerie35">
						<html:optionsCollection  property="listaSelInvSerie35" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero35" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">36
						<html:select  styleClass="testoNormale"  property="selInvSerie36">
						<html:optionsCollection  property="listaSelInvSerie36" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="selInvNumero36" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
          </table>
		  <HR>
		  <table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" ><U>
						<bean:message  key="schede.label.identificativiTitoli" bundle="gestioneStampeLabels" /></U>
						</div></td>
             </tr>
		</table>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left" width="20%"> <div class="etichetta" >
						<bean:message  key="schede.label.numIdentificativo" bundle="gestioneStampeLabels" />
						</div></td>
                        <td   valign="top" scope="col" align="left" width="20%"> <div class="etichetta" >
						<bean:message  key="schede.label.numIdentificativo" bundle="gestioneStampeLabels" />
						</div></td>
                        <td   valign="top" scope="col" align="left" width="20%"> <div class="etichetta" >
						<bean:message  key="schede.label.numIdentificativo" bundle="gestioneStampeLabels" />
						</div></td>
                        <td   valign="top" scope="col" align="left" width="20%"> <div class="etichetta" >
						<bean:message  key="schede.label.numIdentificativo" bundle="gestioneStampeLabels" />
						</div></td>
                        <td   valign="top" scope="col" align="left" width="20%"> <div class="etichetta" >
						<bean:message  key="schede.label.numIdentificativo" bundle="gestioneStampeLabels" />
						</div></td>
             </tr>
             <tr>
                        <td  valign="top" scope="col" align="left">01
 						<html:text styleId="testoNormale" property="numIdentificativo01" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">02
 						<html:text styleId="testoNormale" property="numIdentificativo02" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">03
 						<html:text styleId="testoNormale" property="numIdentificativo03" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">04
 						<html:text styleId="testoNormale" property="numIdentificativo04" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">05
 						<html:text styleId="testoNormale" property="numIdentificativo05" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
             <tr>
                        <td  valign="top" scope="col" align="left">06
 						<html:text styleId="testoNormale" property="numIdentificativo06" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">07
 						<html:text styleId="testoNormale" property="numIdentificativo07" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">08
 						<html:text styleId="testoNormale" property="numIdentificativo08" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">09
 						<html:text styleId="testoNormale" property="numIdentificativo09" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">10
 						<html:text styleId="testoNormale" property="numIdentificativo10" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
             <tr>
                        <td  valign="top" scope="col" align="left">11
 						<html:text styleId="testoNormale" property="numIdentificativo11" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">12
 						<html:text styleId="testoNormale" property="numIdentificativo12" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">13
 						<html:text styleId="testoNormale" property="numIdentificativo13" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">14
 						<html:text styleId="testoNormale" property="numIdentificativo14" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">15
 						<html:text styleId="testoNormale" property="numIdentificativo15" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
             <tr>
                        <td  valign="top" scope="col" align="left">16
 						<html:text styleId="testoNormale" property="numIdentificativo16" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">17
 						<html:text styleId="testoNormale" property="numIdentificativo17" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">18
 						<html:text styleId="testoNormale" property="numIdentificativo18" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">19
 						<html:text styleId="testoNormale" property="numIdentificativo19" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">20
 						<html:text styleId="testoNormale" property="numIdentificativo20" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
             <tr>
                        <td  valign="top" scope="col" align="left">21
 						<html:text styleId="testoNormale" property="numIdentificativo21" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">22
 						<html:text styleId="testoNormale" property="numIdentificativo22" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">23
 						<html:text styleId="testoNormale" property="numIdentificativo23" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">24
 						<html:text styleId="testoNormale" property="numIdentificativo24" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">25
 						<html:text styleId="testoNormale" property="numIdentificativo25" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
             <tr>
                        <td  valign="top" scope="col" align="left">26
 						<html:text styleId="testoNormale" property="numIdentificativo26" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">27
 						<html:text styleId="testoNormale" property="numIdentificativo27" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">28
 						<html:text styleId="testoNormale" property="numIdentificativo28" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">29
 						<html:text styleId="testoNormale" property="numIdentificativo29" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">30
 						<html:text styleId="testoNormale" property="numIdentificativo30" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
             <tr>
                        <td  valign="top" scope="col" align="left">31
 						<html:text styleId="testoNormale" property="numIdentificativo31" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">32
 						<html:text styleId="testoNormale" property="numIdentificativo32" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">33
 						<html:text styleId="testoNormale" property="numIdentificativo33" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">34
 						<html:text styleId="testoNormale" property="numIdentificativo34" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">35
 						<html:text styleId="testoNormale" property="numIdentificativo35" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
             <tr>
                        <td  valign="top" scope="col" align="left">36
 						<html:text styleId="testoNormale" property="numIdentificativo36" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">37
 						<html:text styleId="testoNormale" property="numIdentificativo37" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">38
 						<html:text styleId="testoNormale" property="numIdentificativo38" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">39
 						<html:text styleId="testoNormale" property="numIdentificativo39" size="10" maxlength="50"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left">40
 						<html:text styleId="testoNormale" property="numIdentificativo40" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
          </table>
		  <HR>
		  <table   width="100%" border="0">
          </table>
			</td>
       	        <!-- FINE tabella corpo COLONNA + LARGA -->
             </tr>
	     </table>

           <table align="center" border="0" style="height:40px" >
            <tr >
             <td>
			<html:submit styleClass="pulsanti" property="methodStampaSchede">
				<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodStampaSchede">
				<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
			</html:submit>
			 </td>
		  </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
