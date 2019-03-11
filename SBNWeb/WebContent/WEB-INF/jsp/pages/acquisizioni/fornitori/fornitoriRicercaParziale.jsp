<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<bean-struts:define id="noinput"  value="false"/>
<c:choose>
<c:when test="${fornitoriRicercaParzialeForm.disabilitaTutto}">
      <script type="text/javascript">
		<bean-struts:define id="noinput"  value="true"/>
      </script>
</c:when>
</c:choose>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/fornitori/fornitoriRicercaParziale.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
  		<table   width="100%"  align="center" >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">

					<c:choose>
		         		<c:when test="${fornitoriRicercaParzialeForm.creazLegameTitEdit eq 'SI'}">
							<table border="0">
								<tr>
									<td class="etichetta"><bean:message key="ricerca.titoloRiferimento"
										bundle="gestioneBibliograficaLabels" />:</td>
									<td width="20" class="testoNormale"><html:text
										property="bid" size="10" readonly="true"
										></html:text></td>
									<td width="150" class="etichetta"><html:text
										property="descr" size="50" readonly="true"
										></html:text></td>
								</tr>
							</table>
						</c:when>
					</c:choose>


			 <table   width="100%" border="0"  >

			 <!--  Modifiche Maggio 2013 Da mail Contardi/Scognamiglio contenente manuale di Interrogazione Produzione editoriale
					Per la produzione editoriale la ricerca per codice bib non ha senso
			-->
			<c:choose>
		    	<c:when test="${fornitoriRicercaParzialeForm.editore ne 'SI'}">
				     <tr>
		 						<td  class="etichetta"  scope="col" align="left">
			                        <bean:message  key="ricerca.label.codBibl" bundle="acquisizioniLabels" />
		 						</td>
		                        <td  scope="col" align="left">
		                        	<html:text styleId="testoNormale" property="codBibl" size="5" readonly="true"></html:text>
			                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodFornitoriRicercaParziale" >
										<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
									</html:submit>

		                        </td>
					</tr>
				</c:when>
			</c:choose>


		     <tr>
                        <td  width="10%" scope="col" class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.nomeForn" bundle="acquisizioniLabels" />
                        </td>
                        <td width="30%" scope="col" align="left">
							<html:text styleId="testoNormale" property="nomeForn" size="40" readonly="${noinput}" ></html:text>
							<c:choose>
							<c:when test="${fornitoriRicercaParzialeForm.disabilitaTutto eq false}">
								<sbn:tastiera limit="80" name="fornitoriRicercaParzialeForm"  property="nomeForn"></sbn:tastiera>
							</c:when>
							</c:choose>
                        </td>
						<td class="etichetta">
						 <bean:message key="ricerca.inizio"	bundle="acquisizioniLabels" />
						 <html:radio property="tipoRicerca" value="inizio" />
						 <bean:message	key="ricerca.intero" bundle="acquisizioniLabels" />
						 <html:radio property="tipoRicerca" value="intero" />
						 <bean:message	key="ricerca.parole" bundle="acquisizioniLabels" />
						 <html:radio property="tipoRicerca" value="parole" />
						</td>
             </tr>
		     <tr>
                        <td   scope="col" class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.tipoForn" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:select  styleClass="testoNormale" property="tipoForn" disabled="${noinput}">
							<html:optionsCollection  property="listaTipoForn" value="codice" label="descrizioneCodice" />
							</html:select>
                        </td>

             </tr>
			 <tr>
 						<td  class="etichetta"  scope="col" align="left" >
                  			<bean:message  key="ricerca.label.paese" bundle="acquisizioniLabels" />
 						</td>
                        <td scope="col" align="left">
							<html:select  styleClass="testoNormale" property="paeseForn" style="width:50px" disabled="${noinput}">
							<html:optionsCollection  property="listaPaeseForn" value="codice" label="descrizioneCodice" />
							</html:select>
                        </td>

 						<td  class="etichetta"  scope="col" align="left" >
                  			<bean:message  key="ricerca.label.provincia" bundle="acquisizioniLabels" />
							<html:select  styleClass="testoNormale" property="provinciaForn" disabled="${noinput}">
							<html:optionsCollection  property="listaProvinciaForn" value="codice" label="descrizioneCodice" />
							</html:select>
                        </td>



					<c:choose>
		         		<c:when test="${fornitoriRicercaParzialeForm.editore eq 'SI'}">
	 						<td  class="etichetta"  scope="col" align="left" >
	                  			<bean:message  key="ricerca.label.regione" bundle="acquisizioniLabels" />
								<html:select  styleClass="testoNormale" property="regioneForn" disabled="${noinput}">
								<html:optionsCollection  property="listaRegioneForn" value="codice" label="descrizione" />
								</html:select>
	                        </td>
						</c:when>
					</c:choose>


             </tr>



              <!--  Modifiche Maggio 2013 Da mail Contardi/Scognamiglio contenente manuale di Interrogazione Produzione editoriale
					Per la produzione editoriale Profilo di acquisto non deve essere presente
				-->
			<c:choose>
		    	<c:when test="${fornitoriRicercaParzialeForm.editore ne 'SI'}">
					 <tr>
		 						<td  class="etichetta"  scope="col" align="left">
		                  			<bean:message  key="ricerca.label.profiloAcq" bundle="acquisizioniLabels" />
		 						</td>
		                        <td scope="col" align="left">
									<html:text styleId="testoNormale" property="profAcqForn" size="10" readonly="${noinput}"></html:text>
									<html:text styleId="testoNormale" property="profAcqFornDes" size="20" readonly="${noinput}"></html:text>
			                        <html:submit  styleClass="buttonImage" property="methodFornitoriRicercaParziale">
										<bean:message  key="button.crea.profiliAcquisto" bundle="acquisizioniLabels" />
									</html:submit>

		                        </td>
		             </tr>
                </c:when>
			</c:choose>

		     <tr>
                        <td scope="col"  class="etichetta" align="left">
                  			<bean:message  key="ricerca.label.codiceForn" bundle="acquisizioniLabels" />
                        </td>
                        <!--	onchange="this.form.submit();"-->
                        <td scope="col" align="left">
							<html:text styleId="testoNormale" property="codForn" size="5" ></html:text>
                        </td>

		     </tr>


		     <c:choose>
		     	<c:when test="${fornitoriRicercaParzialeForm.editore eq 'SI'}">
		     		    <tr>
		                        <td scope="col"  class="etichetta" align="left">
		                  			<bean:message  key="ricerca.label.isbnEditore" bundle="acquisizioniLabels" />
		                        </td>
		                        <!--	onchange="this.form.submit();"-->
		                        <td scope="col" align="left">
									<html:text styleId="testoNormale" property="isbnEditore" size="9" ></html:text>
		                        </td>

				       </tr>
		  	   </c:when>
			</c:choose>



		    <!--  Modifiche Maggio 2013 Da mail Contardi/Scognamiglio contenente manuale di Interrogazione Produzione editoriale
				Per la produzione editoriale la ricerca con check per biblio o Polo non ha senso
			-->
			<c:choose>
		    	<c:when test="${fornitoriRicercaParzialeForm.editore ne 'SI'}">
				     <tr>
		                        <td   scope="col" class="etichetta" align="right">
									<bean:message  key="ricerca.label.ricercaLocale" bundle="acquisizioniLabels" />
		                        </td>
		                        <td scope="col" align="left">
		<!--							<html:checkbox property="ricercaLocale" value="true"></html:checkbox>-->
									<html:checkbox property="ricercaLocaleStr"  disabled="${noinput}"></html:checkbox>
		                        <html:hidden property="ricercaLocaleStr" value="false" /></td>
				     </tr>
		      	   </c:when>
			</c:choose>


                  </table>
		</td>
       	        <!-- FINE tabella corpo COLONNA + LARGA -->
       	        <!-- tabella corpo COLONNA + STRETTA -->
       	        <!-- FINE tabella corpo COLONNA + STRETTA -->

             </tr>
	     </table>
   	  </div>
	 <div id="divFooterCommon">

           <table  width="100%"   border="0" style="height:40px" >
		     <tr>
				<td width="80" class="etichetta"><bean:message
					key="ricerca.label.elembloccoshort" bundle="acquisizioniLabels" /></td>
				<td width="150" class="testoNormale"><html:text
					property="elemXBlocchi" size="5" maxlength="4"></html:text></td>
				<td width="75" class="etichetta"><bean:message
					key="ricerca.label.ordinamento" bundle="acquisizioniLabels" /></td>
				<td width="150" class="testoNormale"><html:select
					property="tipoOrdinamSelez" >
					<html:optionsCollection property="listaTipiOrdinam"
						value="codice" label="descrizione" />
				</html:select></td>
		     </tr>
      	  </table>
 </div>
 <div id="divFooter">

           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
             <sbn:checkAttivita idControllo="CERCA">
			<html:submit styleClass="pulsanti" property="methodFornitoriRicercaParziale">
				<bean:message key="ricerca.button.cerca" bundle="acquisizioniLabels" />
			</html:submit>
			</sbn:checkAttivita>

		<c:choose>
			<c:when test="${!fornitoriRicercaParzialeForm.LSRicerca}">
			 <!-- Ottobre 2013: Tasto Crea Editore viene prospettato solo dopo una ricerca a vuoto dell'editore -->
				<c:choose>
			    	<c:when test="${fornitoriRicercaParzialeForm.presenzaTastoCrea eq 'SI'}">
			    	  <sbn:checkAttivita idControllo="CREA">
							<html:submit styleClass="pulsanti" property="methodFornitoriRicercaParziale">
							<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
							</html:submit>
						</sbn:checkAttivita>
			    	</c:when>
				</c:choose>
			</c:when>
		</c:choose>

		<c:choose>
			<c:when test="${fornitoriRicercaParzialeForm.visibilitaIndietroLS}">
				<html:submit styleClass="pulsanti" property="methodFornitoriRicercaParziale">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
			</c:when>
		</c:choose>

             </td>
          </tr>
      	  </table>
     	  </div>
	</sbn:navform>
</layout:page>
