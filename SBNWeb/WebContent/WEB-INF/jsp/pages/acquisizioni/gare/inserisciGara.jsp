<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput"  value="false"/>

<c:choose>
<c:when test="${inserisciGaraForm.disabilitaTutto}">
	<bean-struts:define id="noinput"  value="true"/>
</c:when>
</c:choose>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/gare/inserisciGara.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
	<table  width="100%" >
             <tr>
               <td  class="etichetta" width="6%" scope="col" align="left">
        			<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
               </td>
               <td scope="col" width="22%" align="left">
				<html:text styleId="testoNormale" name="inserisciGaraForm" property="richOff.codBibl" size="4" readonly="true" ></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodInserisciGara" disabled="${noinput}">
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>

               </td>
               <td  width="24%" scope="col"  align="right">
        			<bean:message  key="buono.label.numero" bundle="acquisizioniLabels" />
               </td>
               <td scope="col" width="10%"  align="left">
					<html:text styleId="testoNormale" name="inserisciGaraForm" property="richOff.codRicOfferta" size="15" readonly="true" ></html:text>
               </td>
               <td width="4%" scope="col"  align="right">
        			<bean:message  key="buono.label.dataBuono" bundle="acquisizioniLabels" />
               </td>
               <td scope="col" width="34%" align="left">
					<html:text styleId="testoNormale" name="inserisciGaraForm" property="richOff.dataRicOfferta" size="15" readonly="${noinput}" ></html:text>
               </td>
             </tr>
              <tr>
                <td class="etichetta"  scope="col" width="6%"  align="left">
         			<bean:message  key="ordine.label.prezzo" bundle="acquisizioniLabels" />
                </td>
                <td class="etichetta"  scope="col" width="22%" align="left">
					<html:text styleId="testoNormale" name="inserisciGaraForm" property="richOff.prezzoIndGaraStr" size="10"  readonly="${noinput}" ></html:text>
                </td>
                <td  scope="col" width="24%" align="right">
         			<bean:message  key="buono.label.numeroCopie" bundle="acquisizioniLabels" />
                </td>
                <td class="etichetta"  scope="col" width="10%"  align="left">
					<html:text styleId="testoNormale" name="inserisciGaraForm" property="richOff.numCopieRicAcq" size="4"  readonly="${noinput}" ></html:text>
                </td>
                <td   scope="col" width="4%"  align="right">
         			<bean:message  key="buono.label.statoBuono" bundle="acquisizioniLabels" />
                </td>
                <td class="etichetta"  scope="col" width="34%"  align="left">
					<html:select styleClass="testoNormale" property="richOff.statoRicOfferta" disabled="true">
					<html:optionsCollection  property="listaStatoRichiestaOfferta" value="codice" label="descrizione" />
					</html:select>
                </td>
                </tr>
                    <tr>
                      <td class="etichetta"  scope="col" width="6%" align="left">
               			<bean:message  key="ordine.label.bid" bundle="acquisizioniLabels" />
                      </td>
                      <td scope="col" colspan="8" align="left">
						<table border="0" cellpadding="0" cellspacing="0"  >
						<tr>
						<td valign="top" align="left">
     						<html:text styleId="testoNormale"  name="inserisciGaraForm" property="richOff.bid.codice" size="10" readonly="${noinput}"  ></html:text>
						</td>
						<td bgcolor="#EBEBE4" valign="top" align="left" >
<!--   						<html:text styleId="testoNormale"  name="inserisciGaraForm"  property="richOff.bid.descrizione" size="40" readonly="true"  ></html:text>-->
							<bean-struts:write  name="inserisciGaraForm" property="richOff.bid.descrizione" />
						</td>
						<td valign="top" align="left">
			              <html:submit  styleClass="buttonImage" property="methodInserisciGara" disabled="${noinput}">
							<bean:message  key="ordine.bottone.searchTit" bundle="acquisizioniLabels" />
						  </html:submit>
						</td>

						</tr>
						</table >
                      </td>



                    </tr>
                    <tr>
                      <td class="etichetta" scope="col" width="6%" align="left">
               			<bean:message  key="ordine.label.noteEtic" bundle="acquisizioniLabels" />
                      </td>
                      <td scope="col" colspan="7" align="left">
							<html:textarea styleId="testoNormale" property="richOff.noteOrdine" rows="1" cols="100" readonly="${noinput}" ></html:textarea>
							<c:choose>
							<c:when test="${inserisciGaraForm.disabilitaTutto eq false}">
								<sbn:tastiera limit="80" name="inserisciGaraForm"  property="richOff.noteOrdine"></sbn:tastiera>
							</c:when>
							</c:choose>

                      </td>
                    </tr>
                    <tr>
                      <td scope="col" class="etichetta" colspan="8" > </td>
                    </tr>
                    <tr>
				        <td  class="etichetta" colspan="8">
				    		<bean:message  key="ricerca.label.totPartecipanti" bundle="acquisizioniLabels" />
							<html:text styleId="testoNormale" name="inserisciGaraForm" property="numRighePartecipanti" size="3" readonly="true" ></html:text>
				        </td>
                    </tr>

                          <tr class="etichetta" bgcolor="#dde8f0">
                            <td scope="col" colspan="2" align="left" width="40%">
		               			<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col"  align="center">
		               			<bean:message  key="buono.label.statoBuono" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col"  align="center">
		               			<bean:message  key="ordine.label.tipoInvio" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col"  align="center">
		               			<bean:message  key="ordine.label.dataInvio" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col"   align="center">
		               			<bean:message  key="ordine.label.noteEtic" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col"   align="center">
		               			<bean:message  key="ricerca.label.risposta" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col"  align="center"></td>
                          </tr>

		<logic:greaterThan name="inserisciGaraForm" property="numRighePartecipanti" value="0">
			<logic:iterate id="elencaRighePartecipanti" property="elencaRighePartecipanti" name="inserisciGaraForm" indexId="indice">
			   <c:set var="color" >
					<c:choose>
				        <c:when test='${color == "#FFCC99"}'>
				            #FEF1E2
				        </c:when>
				        <c:otherwise>
							#FFCC99
				        </c:otherwise>
				    </c:choose>
			    </c:set>
				<logic:notEqual  name="elencaRighePartecipanti" property="codBibl" value="">
				  	<tr class="testoNormale" bgcolor="${color}">
						<td  scope="col" align="left" width="9%" >
							<bean-struts:write  name="elencaRighePartecipanti" property="fornitore.codice"/>
						</td>

						<td   scope="col" align="left" width="40%">
							<bean-struts:write  name="elencaRighePartecipanti" property="fornitore.descrizione"/>
						</td>
						<td   scope="col" align="center" width="5%"><bean-struts:write  name="elencaRighePartecipanti" property="statoPartecipante"/>
						</td>
						<td   scope="col" align="center" width="5%"><bean-struts:write  name="elencaRighePartecipanti" property="codtipoInvio"/>
						</td>
						<td   scope="col" align="center" width="10%">
							<bean-struts:write  name="elencaRighePartecipanti" property="dataInvioAlFornRicOfferta"/>
						</td>
						<td   scope="col" align="center" width="5%">
							<input type="submit"  class="buttonImageNote"  name="tagNote" value="${indice}" title="note al fornitore"></input>
							<c:choose>
								<c:when test="${elencaRighePartecipanti.esisteNota}">
									*
								</c:when>
							</c:choose>

						</td>
						<td   scope="col" align="center" width="5%">
							<input type="submit"  class="buttonImageRisp"  name="tagRisp" value="${indice}" title="risposta del fornitore"></input>
							<c:choose>
								<c:when test="${elencaRighePartecipanti.esisteRisp}">
									*
								</c:when>
							</c:choose>

						</td>
							<bean-struts:define id="operazioneValue">
							  <bean-struts:write  name="indice" />
							</bean-struts:define>
						<td  class="testoNormale" align="center">
							<html:radio property="radioPartecipante" value="${operazioneValue}" disabled="${noinput}"></html:radio>
						</td>
					</tr>
						<c:choose>
						<c:when test="${inserisciGaraForm.clicNotaPrg == indice }">
							  	<tr class="testoNormale" bgcolor="${color}" >
									<td colspan="8" scope="col" align="center" >
				               			<bean:message  key="ordine.label.noteEtic" bundle="acquisizioniLabels" />
										<c:choose>
										<c:when test="${inserisciGaraForm.disabilitaTutto or inserisciGaraForm.richOff.statoRicOfferta eq '2'}">
											<html:text styleId="testoNormale" name="elencaRighePartecipanti" property="noteAlFornitore" size="100"  readonly="true" ></html:text>
										</c:when>
										<c:otherwise>
											<html:textarea style="background-color: #FFFFCC;" styleId="testoNormale"  name="elencaRighePartecipanti" indexed="true"  property="noteAlFornitore" rows="1" cols="100"   ></html:textarea>
											<sbn:tastiera limit="80" name="inserisciGaraForm"  property="elencaRighePartecipanti[${indice}].noteAlFornitore"></sbn:tastiera>
										</c:otherwise>
										</c:choose>

									</td>
								</tr>
						</c:when>
						<c:when test="${inserisciGaraForm.clicRispPrg == indice }">
							  	<tr class="testoNormale" bgcolor="${color}" >
									<td colspan="8" scope="col" align="center" >
				               			<bean:message  key="ricerca.label.risposta" bundle="acquisizioniLabels" />
										<c:choose>
										<c:when test="${inserisciGaraForm.disabilitaTutto or inserisciGaraForm.richOff.statoRicOfferta eq '2'}">
													<html:text styleId="testoNormale" name="elencaRighePartecipanti" property="msgRispDaFornAGara" size="100"  readonly="true" ></html:text>
										</c:when>
										<c:otherwise>
											<html:textarea style="background-color: #ccff99;" styleId="testoNormale"  name="elencaRighePartecipanti" indexed="true"  property="msgRispDaFornAGara" rows="1" cols="100"   ></html:textarea>
											<sbn:tastiera limit="80" name="inserisciGaraForm"  property="elencaRighePartecipanti[${indice}].msgRispDaFornAGara"></sbn:tastiera>
										</c:otherwise>
										</c:choose>
									</td>
								</tr>
						</c:when>
						</c:choose>

				</logic:notEqual>
				<logic:equal  name="elencaRighePartecipanti" property="codBibl" value="">
						  	<tr class="testoNormale" bgcolor="${color}">
								<td   scope="col" align="left" width="9%">
									<html:text styleId="testoNormale" name="elencaRighePartecipanti" property="fornitore.codice" indexed="true" size="4" readonly="${noinput}" ></html:text>
								</td>
								<td   scope="col" align="left" width="40%">
									<html:text styleId="testoNormale" name="elencaRighePartecipanti" property="fornitore.descrizione" size="40" indexed="true" readonly="true" ></html:text>
								</td>
								<td  scope="col" align="center" width="5%">
									<html:select styleClass="testoNormale" name="elencaRighePartecipanti" style="width:40px;" property="statoPartecipante" indexed="true" disabled="${noinput}" >
									<html:optionsCollection   property="listaStatoPartecipanteGara" value="codice" label="descrizione" />
									</html:select>
								</td>
								<td  scope="col" align="center" width="5%">
									<html:select styleClass="testoNormale" name="elencaRighePartecipanti" style="width:40px;" property="codtipoInvio" indexed="true" disabled="${noinput}" >
									<html:optionsCollection   property="listaTipoInvio" value="codice" label="descrizione" />
									</html:select>
								</td>
								<td   scope="col" align="center" width="10%">
									<html:text styleId="testoNormale" name="elencaRighePartecipanti" property="dataInvioAlFornRicOfferta" indexed="true" size="10" readonly="${noinput}"  ></html:text>
								</td>
								<td   scope="col" align="center" width="5%">
									<!--
									<c:choose>
									<c:when test="${inserisciGaraForm.disabilitaTutto}">
										<input type="submit"  class="buttonImageNote"  name="tagNote" value="${indice}" title="note al fornitore" disabled="disabled"></input>
									</c:when>
									<c:otherwise>
										<input type="submit"  class="buttonImageNote"  name="tagNote" value="${indice}" title="note al fornitore"></input>
									</c:otherwise>
									</c:choose>
									-->
									<input type="submit"  class="buttonImageNote"  name="tagNote" value="${indice}" title="note al fornitore"></input>
									<c:choose>
										<c:when test="${elencaRighePartecipanti.esisteNota}">
											*
										</c:when>
									</c:choose>
								</td>

								<td   scope="col" align="center" width="10%">
								<!--
									<c:choose>
									<c:when test="${inserisciGaraForm.disabilitaTutto}">
										<input type="submit"  class="buttonImageRisp"  name="tagRisp" value="${indice}" title="risposta del fornitore" disabled="disabled"></input>
									</c:when>
									<c:otherwise>
										<input type="submit"  class="buttonImageRisp"  name="tagRisp" value="${indice}" title="risposta del fornitore"></input>
									</c:otherwise>
									</c:choose>

									-->
									<input type="submit"  class="buttonImageRisp"  name="tagRisp" value="${indice}" title="risposta del fornitore"></input>
									<c:choose>
										<c:when test="${elencaRighePartecipanti.esisteRisp}">
											*
										</c:when>
									</c:choose>

								</td>

									<bean-struts:define id="operazioneValue">
									  <bean-struts:write  name="indice" />
									</bean-struts:define>
								<td  class="testoNormale" align="center">
									<html:radio property="radioPartecipante" value="${operazioneValue}" disabled="${noinput}"></html:radio>
								</td>
							</tr>
						<c:choose>
						<c:when test="${inserisciGaraForm.clicNotaPrg == indice }">
							  	<tr class="testoNormale" bgcolor="${color}" >
									<td colspan="8" scope="col" align="center" >
				               			<bean:message  key="ordine.label.noteEtic" bundle="acquisizioniLabels" />
										<c:choose>
										<c:when test="${inserisciGaraForm.disabilitaTutto or inserisciGaraForm.richOff.statoRicOfferta eq '2'}">
											<html:text styleId="testoNormale" name="elencaRighePartecipanti" property="noteAlFornitore" size="100"  readonly="true" ></html:text>
										</c:when>
										<c:otherwise>
											<html:textarea style="background-color: #FFFFCC;" styleId="testoNormale"  name="elencaRighePartecipanti" indexed="true"  property="noteAlFornitore" rows="1" cols="100" ></html:textarea>
											<sbn:tastiera limit="80" name="inserisciGaraForm"  property="elencaRighePartecipanti[${indice}].noteAlFornitore"></sbn:tastiera>
										</c:otherwise>
										</c:choose>

									</td>
								</tr>
						</c:when>
						<c:when test="${inserisciGaraForm.clicRispPrg == indice }">
							  	<tr class="testoNormale" bgcolor="${color}" >
									<td colspan="8" scope="col" align="center" >
				               			<bean:message  key="ricerca.label.risposta" bundle="acquisizioniLabels" />
										<c:choose>
										<c:when test="${inserisciGaraForm.disabilitaTutto or inserisciGaraForm.richOff.statoRicOfferta eq '2'}">
													<html:text styleId="testoNormale" name="elencaRighePartecipanti" property="msgRispDaFornAGara" size="100"  readonly="true" ></html:text>
										</c:when>
										<c:otherwise>
											<html:textarea style="background-color: #ccff99;" styleId="testoNormale"  name="elencaRighePartecipanti" indexed="true"  property="msgRispDaFornAGara" rows="1" cols="100"   ></html:textarea>
											<sbn:tastiera limit="80" name="inserisciGaraForm"  property="elencaRighePartecipanti[${indice}].msgRispDaFornAGara"></sbn:tastiera>
										</c:otherwise>
										</c:choose>

									</td>
								</tr>
						</c:when>

						</c:choose>

				</logic:equal>



			</logic:iterate>
		</logic:greaterThan>

                  </table>
  </div>
  <div id="divFooter">
		<c:choose>
			<c:when test="${inserisciGaraForm.conferma}">
<!--				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />-->
				<table align="center">
					<tr>
						<td><html:submit styleClass="pulsanti" property="methodInserisciGara">
								<bean:message key="acquisizioni.bottone.si" bundle="acquisizioniLabels" /></html:submit>
							<html:submit styleClass="pulsanti" property="methodInserisciGara">
								<bean:message key="acquisizioni.bottone.no" bundle="acquisizioniLabels" /></html:submit></td>
					</tr>
				</table>


			</c:when>
			<c:otherwise>
			<table align="center"  border="0" style="height:40px" height="98"  >
              <tr>
              <td align="center">
				<c:choose>
					<c:when test="${!inserisciGaraForm.disabilitaTutto}">

						<html:submit styleClass="pulsanti" property="methodInserisciGara">
							<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodInserisciGara">
							<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodInserisciGara">
							<bean:message key="ricerca.button.insForn" bundle="acquisizioniLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodInserisciGara">
							<bean:message key="ricerca.button.cancForn" bundle="acquisizioniLabels" />
						</html:submit>

						<!--
						<html:submit styleClass="pulsanti" property="methodInserisciGara">
							<bean:message key="ricerca.button.insRiga" bundle="acquisizioniLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodInserisciGara">
							<bean:message key="ricerca.button.cancRiga" bundle="acquisizioniLabels" />
						</html:submit>
			 			-->
			 			<!--
			 			<html:submit styleClass="pulsanti" property="methodInserisciGara">
							<bean:message key="ricerca.label.fornitori" bundle="acquisizioniLabels" />
						</html:submit>
			 			-->
			 			<html:submit  styleClass="pulsanti" property="methodInserisciGara">
							<bean:message key="ricerca.button.suggBibl" bundle="acquisizioniLabels" />
						</html:submit>

			 			<html:submit styleClass="pulsanti" property="methodInserisciGara">
							<bean:message key="ricerca.button.suggLett" bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
				</c:choose>

			 			<html:submit styleClass="pulsanti" property="methodInserisciGara">
							<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
						</html:submit>
					<logic:equal  name="inserisciGaraForm" property="visibilitaIndietroLS" value="true">
						<html:submit  styleClass="pulsanti" property="methodInserisciGara">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>
					</logic:equal>


             </td>
             </tr>
      	  </table>
    		</c:otherwise>
		</c:choose>

     	  </div>
	</sbn:navform>
</layout:page>
