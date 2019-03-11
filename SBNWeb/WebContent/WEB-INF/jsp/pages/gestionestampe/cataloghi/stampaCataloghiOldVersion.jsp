<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:errors bundle="gestioneStampeMessages" />
	<sbn:navform action="/gestionestampe/cataloghi/stampaCataloghi.do">
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
 		     </tr>
		  </table>
			<HR>
		  <table   width="90%" border="0">
     				<tr>
     	 				<td valign="top" scope="col" >
     	 				<div align="left" class="etichetta">
                			<bean:message key="cataloghi.label.materiale" bundle="gestioneStampeLabels" />
		 				</div>
		 				</td>
    					<td valign="top" scope="col" >
     					<div align="left" class="etichetta">
	     					<bean:message key="cataloghi.label.libroModerno" bundle="gestioneStampeLabels" />
	     					<html:radio property="materiale" value="cataloghi.label.libroModerno" />
						</div>
						</td>
     					<td valign="top" scope="col" >
     					<div align="left" class="etichetta">
	     					<bean:message key="cataloghi.label.libroAntico" bundle="gestioneStampeLabels" />
	     					<html:radio property="materiale" value="cataloghi.label.libroAntico" />
						</div>
						</td>
     					<td valign="top" scope="col" >
     					<div align="left" class="etichetta">
	     					<bean:message key="cataloghi.label.tutto" bundle="gestioneStampeLabels" />
	     					<html:radio property="materiale" value="cataloghi.label.tutto" />
						</div>
						</td>
					</tr>
		  </table>
		  <table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.catalogoPer" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="catalogoPer">
						<html:optionsCollection  property="listaCatalogoPer" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.ordinatoPer" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="ordinatoPer">
						<html:optionsCollection  property="listaOrdinatoPer" value="codice" label="descrizione" />
						</html:select>
                        </td>
             </tr>
		</table>
			<HR>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" ><U>
						<bean:message  key="cataloghi.label.titoli" bundle="gestioneStampeLabels" /></U>
						</div></td>
             </tr>
		  </table>
		  <table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.dalTitolo" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dalTitolo" size="10" maxlength="10"></html:text>
                        </td>
                         <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.alTitolo" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="alTitolo" size="10" maxlength="10"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.natura" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="natura">
						<html:optionsCollection  property="listaNatura" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.status" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="status">
						<html:optionsCollection  property="listaStatus" value="codice" label="descrizione" />
						</html:select>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.lingua" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="lingua">
						<html:optionsCollection  property="listaLingua" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.paese" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="paese">
						<html:optionsCollection  property="listaPaese" value="codice" label="descrizione" />
						</html:select>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.genere" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="genere">
						<html:optionsCollection  property="listaGenere" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.tipoData" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="tipoData">
						<html:optionsCollection  property="listaTipoData" value="codice" label="descrizione" />
						</html:select>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.dallaData" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dallaData" size="10" maxlength="10"></html:text>
                        </td>
                         <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.allaData" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="allaData" size="10" maxlength="10"></html:text>
                        </td>
             </tr>
		  </table>
			<HR>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" ><U>
						<bean:message  key="cataloghi.label.autori" bundle="gestioneStampeLabels" /></U>
						</div></td>
             </tr>
		</table>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.vid" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="vid" size="10" maxlength="10"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.dallAutore" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dallAutore" size="10" maxlength="50"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.allAutore" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="allAutore" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.tipo" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="tipo">
						<html:optionsCollection  property="listaTipo" value="codice" label="descrizione" />
						</html:select>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.responsabilita" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="responsabilita">
						<html:optionsCollection  property="listaResponsabilita" value="codice" label="descrizione" />
						</html:select>
                        </td>
             </tr>
		</table>
			<HR>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" ><U>
						<bean:message  key="cataloghi.label.collocazione" bundle="gestioneStampeLabels" /></U>
						</div></td>
             </tr>
		</table>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.dallaSezione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dallaSezione" size="10" maxlength="10"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.dallaCollocazione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dallaCollocazione" size="10" maxlength="50"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.specificazioneDallaCollocazione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="specificazioneDallaCollocazione" size="10" maxlength="50"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.allaCollocazione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="allaCollocazione" size="10" maxlength="50"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.specificazioneAllaCollocazione" bundle="gestioneStampeLabels" />
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
						<bean:message  key="cataloghi.label.serie" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="serie">
						<html:optionsCollection  property="listaSerie" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.dalNumero" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dalNumero" size="10" maxlength="50"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.alNumero" bundle="gestioneStampeLabels" />
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
						<bean:message  key="cataloghi.label.soggetti" bundle="gestioneStampeLabels" /></U>
						</div></td>
             </tr>
		</table>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.soggettario" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="soggettario">
						<html:optionsCollection  property="listaSoggettario" value="codice" label="descrizione" />
						</html:select>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.dalCid" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dalCid" size="10" maxlength="10"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.alCid" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="alCid" size="10" maxlength="10"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.dalSoggetto" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dalSoggetto" size="10" maxlength="10"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.alSoggetto" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="alSoggetto" size="10" maxlength="10"></html:text>
                        </td>
            </tr>
		</table>
			<HR>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" ><U>
						<bean:message  key="cataloghi.label.classificazione" bundle="gestioneStampeLabels" /></U>
						</div></td>
             </tr>
		</table>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.sistema" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="sistema">
						<html:optionsCollection  property="listaSistema" value="codice" label="descrizione" />
						</html:select>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.dalSimbolo" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dalSimbolo" size="10" maxlength="10"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.alSimbolo" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="alSimbolo" size="10" maxlength="10"></html:text>
                        </td>
            </tr>
		</table>
			<HR>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" ><U>
						<bean:message  key="cataloghi.label.marche" bundle="gestioneStampeLabels" /></U>
						</div></td>
             </tr>
		</table>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.mid" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="mid" size="10" maxlength="10"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.repertorio" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="repertorio">
						<html:optionsCollection  property="listaRepertorio" value="codice" label="descrizione" />
						</html:select>
                        </td>
            </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.citazioneDa" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="citazioneDa" size="10" maxlength="10"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.citazioneA" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="citazioneA" size="10" maxlength="10"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.dallaParola" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dallaParola" size="10" maxlength="10"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.allaParola" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="allaParola" size="10" maxlength="10"></html:text>
                        </td>
            </tr>
		</table>
			<HR>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" ><U>
						<bean:message  key="cataloghi.label.possessori" bundle="gestioneStampeLabels" /></U>
						</div></td>
             </tr>
		</table>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.pid" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="pid" size="10" maxlength="10"></html:text>
                        </td>
            </tr>
		    <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.dalPossessore" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dalPossessore" size="10" maxlength="10"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.alPossessore" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="alPossessore" size="10" maxlength="10"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.tipo" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="tipo">
						<html:optionsCollection  property="listaTipo" value="codice" label="descrizione" />
						</html:select>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="cataloghi.label.possessoreResponsabilita" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="possessoreResponsabilita">
						<html:optionsCollection  property="listaPossessoreResponsabilita" value="codice" label="descrizione" />
						</html:select>
                        </td>
             </tr>
		</table>
			<HR>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" ><U>
						<bean:message  key="cataloghi.label.datiDiStampa" bundle="gestioneStampeLabels" /></U>
						</div></td>
             </tr>
		</table>

		  <table   width="90%" border="0">
     				<tr>
     	 				<td valign="top" scope="col" >
     	 				<div align="left" class="etichetta">
                			<bean:message key="cataloghi.label.stampaITitoli" bundle="gestioneStampeLabels" />
		 				</div>
		 				</td>
    					<td valign="top" scope="col" >
     					<div align="left" class="etichetta">
	     					<bean:message key="cataloghi.label.tutteLeBiblioteche" bundle="gestioneStampeLabels" />
	     					<html:radio property="stampaITitoli" value="cataloghi.label.tutteLeBiblioteche" />
						</div>
						</td>
     					<td valign="top" scope="col" >
     					<div align="left" class="etichetta">
	     					<bean:message key="cataloghi.label.soloBiblioSel" bundle="gestioneStampeLabels" />
	     					<html:radio property="stampaITitoli" value="cataloghi.label.soloBiblioSel" />
						</div>
						</td>
					</tr>
		</table>
		<table  align="left" border="0" class="etichetta">
		        <tr>
		          <td>
		          	<label for="datiStampa1"><bean:message key="cataloghi.label.intestazioneTitoloautorePrinc" bundle="gestioneStampeLabels" /> </label>
		          </td>
		          <td align="left">
	       				<html:checkbox styleId="chkDatiStampa1" property="chkDatiStampa1" />
	       		  </td>
	       		</tr>
		        <tr>
		          <td>
            		<label for="datiStampa2"><bean:message key="cataloghi.label.stampaTitoloCollana" bundle="gestioneStampeLabels" /> </label>
		          </td>
		          <td align="left">
	       				<html:checkbox styleId="chkDatiStampa2" property="chkDatiStampa2" />
	       			</td>
	       		</tr>
		        <tr>
		          <td>
             		<label for="datiStampa3"><bean:message key="cataloghi.label.stampaTitoliAnalitici" bundle="gestioneStampeLabels" /> </label>
		          </td>
		          <td align="left">
	       				<html:checkbox styleId="chkDatiStampa3" property="chkDatiStampa3" />
	       			</td>
	       		</tr>
		        <tr>
		          <td>
             		<label for="datiStampa4"><bean:message key="cataloghi.label.stampaDatiCollocazione" bundle="gestioneStampeLabels" /> </label>
		          </td>
		          <td align="left">
	       				<html:checkbox styleId="chkDatiStampa4" property="chkDatiStampa4" />
	       			</td>
	       		</tr>
		        <tr>
		          <td>
             		<label for="datiStampa5"><bean:message key="cataloghi.label.ordinamentoTitoloSuperiore" bundle="gestioneStampeLabels" /> </label>
		          </td>
		          <td align="left">
	       				<html:checkbox styleId="chkDatiStampa5" property="chkDatiStampa5" />
		        </td>
		        </tr>
		    </table>
			</td>
       	        <!-- FINE tabella corpo COLONNA + LARGA -->
             </tr>
	     </table>
			<HR>
				<jsp:include flush="true" page="../common/tipoStampa.jsp" />
			<HR>

           <table align="center" border="0" style="height:40px" >
            <tr >
             <td>
			<html:submit styleClass="pulsanti" property="methodStampaCataloghi">
				<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodStampaCataloghi">
				<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
			</html:submit>
			 </td>
		  </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
