<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput"  value="false"/>
<bean-struts:define id="noinputContact"  value="false"/>
<c:choose>
<c:when test="${inserisciFornitoreForm.disabilitaTutto}">
	<bean-struts:define id="noinput"  value="true"/>
	<bean-struts:define id="noinputContact"  value="true"/>
</c:when>
<c:when test="${inserisciFornitoreForm.disabilitaComune}">
	<bean-struts:define id="noinput"  value="true"/>
</c:when>

</c:choose>



<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/fornitori/inserisciFornitore.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
             <table  width="100%"  align="center">
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">
		<table   width="100%" >
		     <tr>
 						<td  colspan="6"></td>
			 </tr>

		     <tr>
 						<td  class="etichetta" width="15%" scope="col" align="left">
	                        <bean:message  key="ricerca.label.codForn" bundle="acquisizioniLabels" />
 						</td>
                        <td colspan="5" scope="col" align="left">
                        	<html:text styleId="testoNormale" property="fornitore.codFornitore" size="5" readonly="true"></html:text>
                        	<html:text styleId="testoNormale"  property="fornitore.nomeFornitore" size="80" maxlength="50" readonly="${noinput}"></html:text>
							<c:choose>
								<c:when test="${inserisciFornitoreForm.disabilitaTutto eq false}">
									<sbn:tastiera limit="80" name="inserisciFornitoreForm"  property="fornitore.nomeFornitore"></sbn:tastiera>
								</c:when>
							</c:choose>

						    <!--  Modifiche Maggio 2013 Da mail Contardi/Scognamiglio contenente manuale di Interrogazione Produzione editoriale
								Per la produzione editoriale la ricerca su Polo non ha senso
							-->
							<c:choose>
						    	<c:when test="${inserisciFornitoreForm.editore ne 'SI'}">
			              			<html:submit styleClass="pulsanti" property="methodInserisciFornitore" disabled="${noinput}">
										<bean:message key="ricerca.button.controllaEsistenza" bundle="acquisizioniLabels" />
									</html:submit>
								</c:when>
							</c:choose>

                       </td>
			 </tr>
		     <tr>
 						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.codBibl" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left">
                        	<html:text styleId="testoNormale" property="datiFornitore.codBibl" size="5" readonly="true"></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodInserisciFornitore" disabled="${noinputContact}">
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>

                        </td>
 						<td  class="etichetta" scope="col" align="left">
	                        <bean:message  key="ricerca.label.unitaorgShort" bundle="acquisizioniLabels" />
 						</td>

		                <td  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="fornitore.unitaOrg" size="80" maxlength="50" readonly="${noinput}"></html:text>
                        </td>

			</tr>
		     <tr>
 						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.indirizzo" bundle="acquisizioniLabels" />
 						</td>
                        <td  colspan="4" scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="fornitore.indirizzo" size="70" maxlength="70" readonly="${noinput}"></html:text>
                        </td>
			</tr>
		     <tr>
 						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.cPostale" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="fornitore.casellaPostale" size="20" maxlength="20" readonly="${noinput}"></html:text>
                        </td>
 						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.citta" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left" colspan="2">
                        	<html:text styleId="testoNormale"  property="fornitore.citta" size="20" maxlength="20" readonly="${noinput}"></html:text>
                       <!--
                        </td>
 						<td  class="etichetta"  scope="col" align="left">
	                   -->
	                   &nbsp;<bean:message  key="ricerca.label.codAvvPostale" bundle="acquisizioniLabels" />
	                   <!--
 						</td>
                        <td  scope="col" align="left">
                        -->
                        &nbsp;<html:text styleId="testoNormale"  property="fornitore.cap" size="10" maxlength="10" readonly="${noinput}"></html:text>
                       </td>

			</tr>
		     <tr>
 						<td  class="etichetta" scope="col" align="left">
	                        <bean:message  key="ricerca.label.telefono" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="fornitore.telefono" size="20" maxlength="20" readonly="${noinput}"></html:text>
                       </td>
 						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.fax" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="fornitore.fax" size="20" maxlength="20" readonly="${noinput}"></html:text>
                       </td>

			</tr>
		     <tr>
 						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.codFiscale" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="fornitore.codiceFiscale" size="18" maxlength="18" readonly="${noinput}"></html:text>
                       </td>
 						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.partitaIVA" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="fornitore.partitaIva" size="18" maxlength="18" readonly="${noinput}"></html:text>
                       </td>
			</tr>

		     <tr>

						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.tipoForn" bundle="acquisizioniLabels" />
						</td>
                        <td  scope="col" align="left">
							<html:select  styleClass="testoNormale" property="fornitore.tipoPartner" style="width:40px;" disabled="${noinput}">
							<html:optionsCollection  property="listaTipoForn" value="codice" label="descrizioneCodice" />
							</html:select>
		               </td>

						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.email" bundle="acquisizioniLabels" />
						</td>
                        <td  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="fornitore.email" size="50" maxlength="50" readonly="${noinput}"></html:text>
                       </td>

			</tr>


		     <tr>
						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.paese" bundle="acquisizioniLabels" />
						</td>
                        <td scope="col" align="left">
							<html:select  styleClass="testoNormale" property="fornitore.paese" style="width:50px;" disabled="${noinput}" >
							<html:optionsCollection  property="listaPaeseForn" value="codice" label="descrizioneCodice" />
							</html:select>
                        </td>

						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.provincia" bundle="acquisizioniLabels" />
						</td>
                        <td scope="col" align="left">
							<html:select  styleClass="testoNormale" property="fornitore.provincia" style="width:50px;" disabled="${noinput}">
							<html:optionsCollection  property="listaProvinciaForn" value="codice" label="descrizioneCodice" />
							</html:select>

							<c:choose>
						    	<c:when test="${inserisciFornitoreForm.editore eq 'SI'}">
									<bean:message  key="ricerca.label.regione" bundle="acquisizioniLabels" />
									<html:select  styleClass="testoNormale" property="fornitore.regione" style="width:50px;" disabled="${noinput}">
									<html:optionsCollection  property="listaRegioneForn" value="codice" label="descrizione" />
									</html:select>
								</c:when>
							</c:choose>

                        </td>
			</tr>
		     <tr >
						<td  colspan="6" class="etichettaIntestazione"  align="left" scope="col" >
	                        <bean:message  key="ordine.label.noteEtic" bundle="acquisizioniLabels" />
						</td>

			</tr>
		     <tr  >

                       <td  colspan="6" scope="col" align="left">
							<html:textarea styleId="testoNormale" property="fornitore.note" rows="1" cols="100"  readonly="${noinputContact}"></html:textarea>
							<c:choose>
								<c:when test="${inserisciFornitoreForm.disabilitaTutto eq false}">
									<sbn:tastiera limit="80" name="inserisciFornitoreForm"  property="fornitore.note"></sbn:tastiera>
								</c:when>
							</c:choose>
                       </td>

			</tr>
          </table>


           <c:choose>
         	<c:when test="${inserisciFornitoreForm.editore eq 'SI'}">

         		<table border="0" bordercolor="#dde8f0">
					<tr>
						<th width="100" class="etichetta"><bean:message
							key="ricerca.label.isbnEditore" bundle="acquisizioniLabels" /></th>
						<th class="etichetta" bgcolor="#dde8f0"><bean:message
							key="ricerca.numero" bundle="gestioneBibliograficaLabels" /></th>
						<th  class="etichetta" bgcolor="#dde8f0">
						&nbsp;
						<html:submit
							styleClass="buttonImageDelLine" property="methodInserisciFornitore" title="Cancella Num.Standard">
							<bean:message key="button.canNumStandard" bundle="gestioneBibliograficaLabels" />
						</html:submit>
						<html:submit styleClass="buttonImageNewLine"
							property="methodInserisciFornitore" title="Inserisci Num.Standard">
							<bean:message key="button.insNumStandard" bundle="gestioneBibliograficaLabels" />
						</html:submit></th>
					</tr>
					<logic:iterate id="itemIsbnEdit" property="fornitore.listaNumStandard" name="inserisciFornitoreForm" indexId="idxIsbnEdit">
						<tr>
							<td></td>
							<td bgcolor="#FFCC99"><html:text name="itemIsbnEdit" property="campoUno" indexed="true" /></td>
							<td bgcolor="#FFCC99"><html:radio property="selezRadioNumStandard" value="${idxIsbnEdit}" /></td>
						</tr>
					</logic:iterate>
				</table>
			</c:when>
			<c:otherwise>


	          <table width="100%" border="0" bgcolor="#FFCC99">
			     <tr  >
							<td  width="15%" class="etichetta"  scope="col" align="left">
		                        <bean:message  key="ordine.label.tipoPag" bundle="acquisizioniLabels" />
							</td>
	                        <td  scope="col" align="left">
	                        	<html:text styleId="testoNormale"  property="datiFornitore.tipoPagamento" size="20" maxlength="50" readonly="${noinputContact}"></html:text>
	                       </td>
	 						<td  class="etichetta"  scope="col" align="left">
		                        <bean:message  key="ordine.label.valuta" bundle="acquisizioniLabels" />
	 						</td>
	                        <td  scope="col" align="left">
								<html:select  styleClass="testoNormale" property="datiFornitore.valuta" style="width:60px;" disabled="${noinputContact}">
									<html:optionsCollection  property="listaValuta" value="codice1" label="codice2"  />
								</html:select>
	                       </td>
							<td  class="etichetta"  scope="col" align="left">
		                        <bean:message  key="ricerca.label.codCliente" bundle="acquisizioniLabels" />
							</td>
	                        <td scope="col" align="left"  >
	                        	<html:text styleId="testoNormale"  property="datiFornitore.codCliente" size="20" maxlength="40" readonly="${noinputContact}"></html:text>
	                        </td>
				</tr>

			     <tr  >
							<td  colspan="6" class="etichettaIntestazione"  align="left" scope="col">
		                        <bean:message  key="ricerca.label.contatto" bundle="acquisizioniLabels" />
							</td>

				</tr>

			     <tr  >
							<td  class="etichetta"  scope="col" align="left">
		                        <bean:message  key="ricerca.label.nome" bundle="acquisizioniLabels" />
							</td>
	                        <td  colspan="5" scope="col" align="left">
	                        	<html:text styleId="testoNormale"  property="datiFornitore.nomContatto" size="50" readonly="${noinputContact}"></html:text>
	                       </td>
				</tr>
			     <tr  >
	 						<td  class="etichetta"  scope="col" align="left">
		                        <bean:message  key="ricerca.label.tel" bundle="acquisizioniLabels" />
	 						</td>
	                        <td  scope="col" align="left">
	                        	<html:text styleId="testoNormale"  property="datiFornitore.telContatto" size="20" readonly="${noinputContact}"></html:text>
	                       </td>

	 						<td  class="etichetta"  scope="col" align="left">
		                        <bean:message  key="ricerca.label.fax" bundle="acquisizioniLabels" />
	 						</td>
	                        <td  scope="col" align="left">
	                        	<html:text styleId="testoNormale"  property="datiFornitore.faxContatto" size="20" readonly="${noinputContact}"></html:text>
	                       </td>

				</tr>




	     		<tr><td  colspan="6" class="etichetta">&nbsp;</td></tr>
						</table>

					</c:otherwise>
				   </c:choose>

				  </td>
			  </tr>

     		<tr><td  class="etichetta">&nbsp;</td></tr>
              </table>
 </div>
  <div id="divFooter">
		<c:choose>
			<c:when test="${inserisciFornitoreForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
	           <table align="center"  border="0" style="height:40px" >
				<tr>
	             <td  valign="top" align="center">
					<html:submit styleClass="pulsanti" property="methodInserisciFornitore">
						<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodInserisciFornitore">
						<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
					</html:submit>
					<!--
					<html:submit styleClass="pulsanti" property="methodInserisciFornitore">
						<bean:message key="ricerca.button.cancella" bundle="acquisizioniLabels" />
					</html:submit>
					-->



					<c:choose>
						 <c:when test="${inserisciFornitoreForm.editore ne 'SI'}">
							<c:choose>
								<c:when test="${inserisciFornitoreForm.gestProf}">
								<html:submit styleClass="pulsanti" property="methodInserisciFornitore">
									<bean:message key="button.crea.profiliAcquisto" bundle="acquisizioniLabels" />
								</html:submit>
								</c:when>
							</c:choose>
							<html:submit styleClass="pulsanti" property="methodInserisciFornitore">
								<bean:message key="button.crea.importaBib" bundle="acquisizioniLabels" />
							</html:submit>
						</c:when>
					</c:choose>

		 			<html:submit styleClass="pulsanti" property="methodInserisciFornitore">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>
					<c:choose>
						<c:when test="${inserisciFornitoreForm.visibilitaIndietroLS}">
							<html:submit styleClass="pulsanti" property="methodInserisciFornitore">
								<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
							</html:submit>
						</c:when>
					</c:choose>
	             </td>
	             </tr>
	      	  </table>
    		</c:otherwise>
		</c:choose>



     	  </div>
	</sbn:navform>
</layout:page>
