<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput"  value="false"/>
<c:choose>
<c:when test="${inserisciSugBiblForm.disabilitaTutto}">
	<bean-struts:define id="noinput"  value="true"/>
</c:when>
</c:choose>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/suggerimenti/inserisciSugBibl.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>

 			<table   width="100%" border="0">
     		<tr><td  colspan="6" class="etichetta">&nbsp;</td></tr>
		     <tr>
 						<td  class="etichetta" width="15%" scope="col" align="left">
 							<bean:message  key="ricerca.label.codBibl" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left">
							<html:text styleId="testoNormale"  property="datiSuggerimento.codBibl" size="4" readonly="true" ></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodInserisciSugBibl" disabled="${noinput}">
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>

                       </td>
						<td  class="etichetta"  scope="col" align="left">
 							<bean:message  key="buono.label.statoBuono" bundle="acquisizioniLabels" />
						</td>
                        <td  scope="col" align="left">
							<html:select styleClass="testoNormale"  property="datiSuggerimento.statoSuggerimento"  disabled="true">
							<html:optionsCollection  property="listaStatoSuggerimento" value="codice" label="descrizione" />
							</html:select>
                       </td>
						<td  class="etichetta" width="15%" scope="col" align="left">
 							<bean:message  key="ricerca.label.codSugg" bundle="acquisizioniLabels" />
						</td>
                        <td  scope="col" align="left">
							<html:text styleId="testoNormale"  property="datiSuggerimento.codiceSuggerimento" size="2" readonly="true" ></html:text>
                       </td>

			</tr>
		     <tr>
 						<td  class="etichetta"  scope="col" align="left">
 							<bean:message  key="buono.label.dataBuono" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left">
							<html:text styleId="testoNormale"  property="datiSuggerimento.dataSuggerimento" size="10" readonly="${noinput}"></html:text>
                        </td>
 						<td  class="etichetta" scope="col" align="left"><bean:message  key="ricerca.label.nome" bundle="acquisizioniLabels" /></td>
						<td  class="etichetta" scope="col" align="left"><html:text styleId="testoNormale"  property="datiSuggerimento.nominativoBibliotecario" size="30" readonly="true" ></html:text><!--
						<td  class="etichetta" scope="col" align="left">
 							<bean:message  key="ricerca.label.codBibliot" bundle="acquisizioniLabels" />
						</td>
		                <td  scope="col" align="left">
							<html:text styleId="testoNormale"  property="datiSuggerimento.bibliotecario.codice" size="10" readonly="true" ></html:text>

                        </td>

			--></tr>
		     <tr>
 						<td  class="etichetta"  scope="col" align="left">
 							<bean:message  key="ordine.label.bid" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" colspan="5" align="left">
							<table border="0" cellpadding="0" cellspacing="0"  >
							<tr>
							<td valign="top" align="left">
								<html:text styleId="testoNormale"  property="datiSuggerimento.titolo.codice" size="10"  readonly="${noinput}"></html:text>
							</td>
							<td bgcolor="#EBEBE4" valign="top" align="left" >
<!--							<html:text styleId="testoNormale"  property="datiSuggerimento.titolo.descrizione" size="50" readonly="true" ></html:text>-->
								<bean-struts:write  name="inserisciSugBiblForm" property="datiSuggerimento.titolo.descrizione" />
		                        <html:submit  styleClass="buttonImage" property="methodInserisciSugBibl">
									<bean:message  key="ordine.bottone.searchTit" bundle="acquisizioniLabels" />
								</html:submit>
							</td>
							</tr>
							</table >
                       </td>
 			</tr>
    	     <tr>
 						<td  class="etichetta" scope="col" align="left">
 							<bean:message  key="ordine.label.sezione" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" colspan="5" align="left">
							<html:text styleId="testoNormale"  property="datiSuggerimento.sezione.codice" size="10"  readonly="${noinput}"></html:text>
							<html:text styleId="testoNormale"  property="datiSuggerimento.sezione.descrizione" size="50" readonly="true" ></html:text>
                        <html:submit  styleClass="buttonImageListaSezione" property="methodInserisciSugBibl" disabled="${noinput}">
							<bean:message  key="ricerca.label.sezione" bundle="acquisizioniLabels" />
						</html:submit>

                       </td>
			</tr>
			</table>
	<hr>
			<table   width="100%" border="0">

		     <tr>
						<td   class="etichetta"   align="left" scope="col">
 							<bean:message  key="ordine.label.noteSugger" bundle="acquisizioniLabels" />
						</td>
                        <td  colspan="5" scope="col" align="left">
							<c:choose>
								<c:when test="${inserisciSugBiblForm.datiSuggerimento.codiceSuggerimento ne ''}">
									<html:textarea styleId="testoNormale" property="datiSuggerimento.noteSuggerimento" rows="1" cols="50" readonly="true"></html:textarea>
								</c:when>
							<c:otherwise>
								<html:textarea styleId="testoNormale" property="datiSuggerimento.noteSuggerimento" rows="1" cols="50" readonly="${noinput}"></html:textarea>
				    		</c:otherwise>
							</c:choose>

							<c:choose>
							<c:when test="${inserisciSugBiblForm.disabilitaTutto eq false}">
								<sbn:tastiera limit="80" property="datiSuggerimento.noteSuggerimento" name="inserisciSugBiblForm"></sbn:tastiera>
							</c:when>
							</c:choose>

                       </td>

			</tr>
		     <tr>
						<td   class="etichetta"   align="left" scope="col">
 							<bean:message  key="ordine.label.noteFornitori" bundle="acquisizioniLabels" />
						</td>
                        <td  colspan="5" scope="col" align="left">

							<c:choose>
								<c:when test="${inserisciSugBiblForm.datiSuggerimento.codiceSuggerimento ne ''}">
								<html:textarea styleId="testoNormale" property="datiSuggerimento.noteFornitore" rows="1" cols="50"  readonly="${noinput}"></html:textarea>
								</c:when>
							<c:otherwise>
								<html:textarea styleId="testoNormale" property="datiSuggerimento.noteFornitore" rows="1" cols="50"  readonly="true"></html:textarea>
				    		</c:otherwise>
							</c:choose>

							<c:choose>
							<c:when test="${inserisciSugBiblForm.disabilitaTutto eq false}">
								<sbn:tastiera limit="80" property="datiSuggerimento.noteFornitore" name="inserisciSugBiblForm"></sbn:tastiera>
							</c:when>
							</c:choose>

                       </td>

			</tr>

		     <tr>
						<td   class="etichetta"   align="left" scope="col">
 							<bean:message  key="ordine.label.noteBibliot" bundle="acquisizioniLabels" />
						</td>
                        <td  colspan="5" scope="col" align="left">
							<c:choose>
								<c:when test="${inserisciSugBiblForm.datiSuggerimento.codiceSuggerimento ne ''}">
									<html:textarea styleId="testoNormale" property="datiSuggerimento.noteBibliotecario" rows="1" cols="50"  readonly="${noinput}" ></html:textarea>
								</c:when>
							<c:otherwise>
								<html:textarea styleId="testoNormale" property="datiSuggerimento.noteBibliotecario" rows="1" cols="50"  readonly="true" ></html:textarea>
				    		</c:otherwise>
							</c:choose>


							<c:choose>
							<c:when test="${inserisciSugBiblForm.disabilitaTutto eq false}">
								<sbn:tastiera limit="80" property="datiSuggerimento.noteBibliotecario" name="inserisciSugBiblForm"></sbn:tastiera>
							</c:when>
							</c:choose>

                       </td>

			</tr>

     		<tr><td  colspan="6" class="etichetta">&nbsp;</td></tr>
			</table>

 </div>
 <div id="divFooter">
		<c:choose>
			<c:when test="${inserisciSugBiblForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
				<!-- tabella bottoni -->

		         <table align="center"  border="0" style="height:40px" >
		             <tr>
		             <td>
				<logic:equal name="inserisciSugBiblForm" property="disabilitaTutto" value="false">

						<html:submit styleClass="pulsanti" property="methodInserisciSugBibl">
							<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodInserisciSugBibl">
							<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
						</html:submit>
						<c:choose>
							<c:when test="${inserisciSugBiblForm.datiSuggerimento.codiceSuggerimento ne ''}">
								<html:submit styleClass="pulsanti" property="methodInserisciSugBibl">
									<bean:message key="ricerca.button.accetta" bundle="acquisizioniLabels" />
								</html:submit>

								<html:submit styleClass="pulsanti" property="methodInserisciSugBibl">
									<bean:message key="ricerca.button.rifiuta" bundle="acquisizioniLabels" />
								</html:submit>
							</c:when>
						</c:choose>
				</logic:equal>

			 			<html:submit styleClass="pulsanti" property="methodInserisciSugBibl">
							<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
						</html:submit>
					<logic:equal  name="inserisciSugBiblForm" property="visibilitaIndietroLS" value="true">
						<html:submit  styleClass="pulsanti" property="methodInserisciSugBibl">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>
					</logic:equal>
		             </td>
		             </tr>
		      	  </table>
			  			<!-- fine tabella bottoni -->
    		</c:otherwise>
		</c:choose>


	     <!-- FINE  tabella corpo -->
     	  </div>
	</sbn:navform>
</layout:page>
