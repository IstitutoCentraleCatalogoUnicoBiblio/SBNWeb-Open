<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput"  value="false"/>
<c:choose>
<c:when test="${configurazioneBOForm.disabilitaTutto}">
	<bean-struts:define id="noinput"  value="true"/>
</c:when>
</c:choose>


<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/configurazione/configurazioneBO.do" method="post" enctype="multipart/form-data">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
	<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/configurazione/configurazioneTabBO.jsp" />
  <table     width="100%"  >
       <tr>
          <td >
				  <table>
				     <tr>
                        <td   scope="col" class="etichetta" align="left">
							<bean:message  key="ricerca.label.codBiblImpost" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
  							<html:text styleId="testoNormale" property="datiConfigBO.codBibl" size="3"  readonly="true"></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>
<!--  							<html:text styleId="testoNormale" property="datiConfigBO.denoBibl" size="90" ></html:text>-->
                        </td><!--
                        <td   scope="col" class="etichetta" align="right">
							<bean:message  key="ordine.label.numCodiceBoAutomatica" bundle="acquisizioniLabels" />
                        </td>
                        --><td scope="col" align="left">
<!--							<html:checkbox property="datiConfigBO.numAutomatica" ></html:checkbox>-->
<!--							<html:checkbox property="numAutomatica" disabled="${noinput}"></html:checkbox>-->

                        </td>
       	     		</tr>
				   </table>
		  </td>
       </tr>



       <tr>
          <td >
				  	<table  align="center" width="100%"   style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"  >
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" align="center" width="5%">
							<bean:message  key="ricerca.label.numRiga" bundle="acquisizioniLabels" />
						</td>
						<td scope="col"  align="center">
							<bean:message  key="ordine.label.datiIntestazione" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" width="5%">
						</td>
					</tr>

					<logic:iterate  id="datiIntest"  property="datiConfigBO.listaDatiIntestazione"	name="configurazioneBOForm" indexId="indicePrgDI">
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

						<tr class="testoNormale" bgcolor="${color}">
							<td align="center">
								<bean-struts:write    name="datiIntest" property="codice1" />
							</td>
							<td align="center">
	  							<html:text styleId="testoNormale"  indexed="true" name="datiIntest"  property="codice2" size="100"  readonly="${noinput}" ></html:text>
							</td>
							<td>
							<html:multibox property="selectedDatiIntest" disabled="${noinput}">
								<bean-struts:write name="datiIntest"  property="codice1" />
							</html:multibox>
							</td>
							</tr>
					</logic:iterate>
			</table>
		  </td>
       </tr>
       <tr>
          <td >
				    <table   border="0" style="height:40px"  align="right">
			            <tr>
			             <td >
							<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message key="ricerca.button.aggiungiInt" bundle="acquisizioniLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message key="ricerca.button.cancellaInt" bundle="acquisizioniLabels" />
							</html:submit>
						  </td>
			          </tr>
					  </table>
		  </td>
       </tr>
       <tr>
          <td >
				  	<table  align="center" width="100%"   style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"  >
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" align="center" width="5%">
							<bean:message  key="ricerca.label.numRiga" bundle="acquisizioniLabels" />
						</td>
						<td scope="col"  align="center">
							<bean:message  key="configurazione.label.StampaBO27" bundle="acquisizioniLabels" />
						</td>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="ricerca.label.lingua" bundle="acquisizioniLabels" />
                        </td>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
							&nbsp;
							<bean:message  key="ordine.label.confORD" bundle="acquisizioniLabels" />
                        </td>

						<td scope="col" align="center" width="5%">
						</td>
					</tr>

					<logic:iterate  id="formulaIntroduttiva"   property="formulaIntroduttiva" 	name="configurazioneBOForm" indexId="indicePrgFI">
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

						<tr class="testoNormale" bgcolor="${color}">
							<td align="center">
								<bean-struts:write    name="formulaIntroduttiva" property="codice1" />
							</td>
							<td align="center">
	  							<html:text styleId="testoNormale"  indexed="true" name="formulaIntroduttiva"  property="codice2" size="80"  readonly="${noinput}" ></html:text>
							</td>
	                        <td scope="col" align="left">
								<html:select  styleClass="testoNormale" indexed="true" name="formulaIntroduttiva"  property="codice3"  disabled="${noinput}">
										<html:option value="ITA" key="ITA"  />
										<html:option value="ENG" key="ENG"  />
								</html:select>
	                        </td>
	                        <td scope="col" align="left">
								<html:select  styleClass="testoNormale"  name="formulaIntroduttiva"  property="codice4" indexed="true" disabled="${noinput}">
										<html:option value="A" key="A"  />
										<html:option value="L" key="L"  />
										<html:option value="D" key="D"  />
										<html:option value="C" key="C"  />
										<html:option value="V" key="V"  />
										<html:option value="R" key="R"  />
								</html:select>
	                        </td>

							<td>
							<html:multibox property="selectedFormulaIntroduttiva" disabled="${noinput}" >
								<bean-struts:write name="formulaIntroduttiva"  property="codice1" />
							</html:multibox>
							</td>

							</tr>
					</logic:iterate>
			</table>
		  </td>
       </tr>
       <tr>
        <tr>
          <td >
				    <table   border="0" style="height:40px"  align="right">
			            <tr>
			             <td >
							<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message key="ricerca.button.aggiungiFormIntr" bundle="acquisizioniLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message key="ricerca.button.cancellaFormIntr" bundle="acquisizioniLabels" />
							</html:submit>
						  </td>
			          </tr>
					  </table>
		  </td>
       </tr>
       <!-- FORMULA INTRODUTTIVA ORDINI R -->
       <tr>
          <td >
				  	<table  align="center" width="100%"   style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"  >
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" align="center" width="5%">
							<bean:message  key="ricerca.label.numRiga" bundle="acquisizioniLabels" />
						</td>
						<td scope="col"  align="center">
							<bean:message  key="configurazione.label.StampaBO31" bundle="acquisizioniLabels" />
						</td>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="ricerca.label.lingua" bundle="acquisizioniLabels" />
                        </td>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="ordine.label.tipoLav" bundle="acquisizioniLabels" />
                        </td>

						<td scope="col" align="center" width="5%">
						</td>
					</tr>
					<logic:notEmpty property="datiConfigBO.formulaIntroOrdineR" name="navForm">
					<logic:iterate  id="item" property="datiConfigBO.formulaIntroOrdineR" name="navForm" indexId="idx">
					   <sbn:rowcolor var="color" index="idx" />

						<tr class="testoNormale" bgcolor="${color}">
							<td align="center">
								<bean-struts:write name="item" property="progr" />
							</td>
							<td align="center">
	  							<html:text styleId="testoNormale"  indexed="true" name="item"  property="intro" size="80"  readonly="${noinput}" ></html:text>
							</td>
	                        <td scope="col" align="left">
								<html:select  styleClass="testoNormale" indexed="true" name="item"  property="lang"  disabled="${noinput}">
										<html:option value="ITA" key="ITA"  />
										<html:option value="ENG" key="ENG"  />
								</html:select>
	                        </td>
	                        <td scope="col" align="left">
	                        <html:select  styleClass="testoNormale" name="item" property="cd_tipo_lav" disabled="${noinput}" indexed="true" >
								<html:optionsCollection name="navForm" property="listaTipoLavorazione" value="cd_tabellaTrim" label="ds_tabella" />
							</html:select>

	                        </td>

							<td>
								<html:radio property="selectedFormulaIntroOrdineR" value="${item.repeatableId}" disabled="${noinput}" />
							</td>

							</tr>
					</logic:iterate>
				 </logic:notEmpty>
			</table>
		  </td>
       </tr>
       <tr>
          <td >
				    <table   border="0" style="height:40px"  align="right">
			            <tr>
			             <td >
							<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message key="ricerca.button.aggiungiFormIntrR" bundle="acquisizioniLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message key="ricerca.button.cancellaFormIntrR" bundle="acquisizioniLabels" />
							</html:submit>
						  </td>
			          </tr>
					  </table>
		  </td>
       </tr>
       <tr>
          <td >
				  	<table  align="center" width="100%"   style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"  >
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" align="center" width="5%">
							<bean:message  key="ricerca.label.numRiga" bundle="acquisizioniLabels" />
						</td>
						<td scope="col"  align="center">
							<bean:message  key="configurazione.label.StampaBO21" bundle="acquisizioniLabels" />
						</td>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="ricerca.label.lingua" bundle="acquisizioniLabels" />
                        </td>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
							&nbsp;
							<bean:message  key="ordine.label.confORD" bundle="acquisizioniLabels" />
                        </td>

						<td scope="col" align="center" width="5%">
						</td>
					</tr>

					<logic:iterate  id="testoOggetto"  property="testoOggetto"	name="configurazioneBOForm" indexId="indicePrgOG">
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

						<tr class="testoNormale" bgcolor="${color}">
							<td align="center">
								<bean-struts:write    name="testoOggetto" property="codice1" />
							</td>
							<td align="center">
	  							<html:text styleId="testoNormale"  indexed="true" name="testoOggetto"  property="codice2" size="80"  readonly="${noinput}" ></html:text>
							</td>
	                        <td scope="col" align="left">
								<html:select  styleClass="testoNormale" indexed="true" name="testoOggetto"  property="codice3" disabled="${noinput}" >
										<html:option value="ITA" key="ITA"  />
										<html:option value="ENG" key="ENG"  />
								</html:select>
	                        </td>
	                        <td scope="col" align="left">
								<html:select  styleClass="testoNormale"  name="testoOggetto"  indexed="true" property="codice4" disabled="${noinput}" >
										<html:option value="A" key="A"  />
										<html:option value="L" key="L"  />
										<html:option value="D" key="D"  />
										<html:option value="C" key="C"  />
										<html:option value="V" key="V"  />
										<html:option value="R" key="R"  />
								</html:select>
	                        </td>

							<td>
							<html:multibox property="selectedTestoOggetto" disabled="${noinput}">
								<bean-struts:write name="testoOggetto"  property="codice1" />
							</html:multibox>
							</td>

							</tr>
					</logic:iterate>
			</table>
		  </td>
       </tr>
       <tr>
          <td >
				    <table   border="0" style="height:40px"  align="right">
			            <tr>
			             <td >
							<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message key="ricerca.button.aggiungiTestoOgg" bundle="acquisizioniLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message key="ricerca.button.cancellaTestoOgg" bundle="acquisizioniLabels" />
							</html:submit>
						  </td>
			          </tr>
					  </table>
		  </td>
       </tr>


       <tr>
          <td >
			  	<table  align="center" width="100%"   style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"  >
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" align="center" width="5%">
							<bean:message  key="ricerca.label.numRiga" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message  key="ordine.label.datiFineStampa" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" width="5%"></td>
					</tr>
					<logic:iterate id="datiFineStampa" property="datiConfigBO.listaDatiFineStampa"	name="configurazioneBOForm" indexId="indicePrgFS">

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

						<tr class="testoNormale" bgcolor="${color}">
							<td align="center">
								<bean-struts:write    name="datiFineStampa" property="codice1" />
							</td>
							<td align="center">
	  							<html:text styleId="testoNormale"  indexed="true" name="datiFineStampa" property="codice2" size="80"  readonly="${noinput}" ></html:text>
							</td>

							<td>
							<html:multibox property="selectedDatiFineStampa" disabled="${noinput}">
								<bean-struts:write name="datiFineStampa"   property="codice1" />
							</html:multibox>
							</td>

							</tr>

					</logic:iterate>

                	</table>
		  </td>
       </tr>
       <tr>
          <td >

				    <table   border="0" style="height:40px"  align="right">
			            <tr>
			             <td >
							<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message key="ricerca.button.aggiungiFine" bundle="acquisizioniLabels" />
							</html:submit>
							<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message key="ricerca.button.cancellaFine" bundle="acquisizioniLabels" />
							</html:submit>

						  </td>
			          </tr>
					  </table>
		  </td>
       </tr>


<tr>
	<td colspan="4">
	<hr color="#dde8f0" ></hr>
	</td>
</tr>
<tr>
	<td>
		<table>
			<tr>
				<td scope="col" class="etichetta" align="left" >
					<bean:message  key="configurazione.label.StampaBO2" bundle="acquisizioniLabels" />
				</td>
				<td scope="col" class="etichetta" align="left" >
					&nbsp;&nbsp;&nbsp;
				</td>
				<td scope="col" align="left">
					<bean:message key="configurazione.label.StampaBO3"	bundle="acquisizioniLabels" />
					<html:radio property="numerazBuono" value="automatico" />
					<bean:message	key="configurazione.label.StampaBO4" bundle="acquisizioniLabels" />
					<html:radio property="numerazBuono" value="manuale" />
				</td>
			</tr>
		</table>
	</td>
</tr>
<tr>
	<td colspan="4">
	<hr color="#dde8f0"></hr>
	</td>
</tr>



<tr><td colspan="4">
<hr color="#dde8f0" ></hr>
</td></tr>
	     <tr>
               <td colspan="4" scope="col" align="left" >
				<bean:message  key="configurazione.label.StampaBO1" bundle="acquisizioniLabels" />
               </td>
   		</tr>
<tr><td colspan="4">
<hr></hr>
</td></tr>

<tr>
	<td>
		<table>
					<!--
				    <tr>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="configurazione.label.StampaBO2" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
						 <bean:message key="configurazione.label.StampaBO3"	bundle="acquisizioniLabels" />
						 <html:radio property="numerazBuono" value="automatico" />
						 <bean:message	key="configurazione.label.StampaBO4" bundle="acquisizioniLabels" />
						 <html:radio property="numerazBuono" value="manuale" />
                        </td>
       	     		</tr>
				    -->
				    <tr>
                        <td   scope="col" class="etichetta" align="left">
							<bean:message  key="configurazione.label.StampaBO5" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<table>
							<tr>
								<td align="left" valign="top">
									 <bean:message key="configurazione.label.StampaBO6"	bundle="acquisizioniLabels" />
								</td>
								<td>
								 <bean:message key="configurazione.label.StampaBO11"	bundle="acquisizioniLabels" />
								 <html:radio property="areaTit" value="si" disabled="true"/>
								 <bean:message	key="configurazione.label.StampaBO12" bundle="acquisizioniLabels" />
								 <html:radio property="areaTit" value="no" disabled="true" />
								</td>
							</tr>
							<tr>
								<td>
									 <bean:message key="configurazione.label.StampaBO7"	bundle="acquisizioniLabels" />
								</td>

								<td>
								 <bean:message key="configurazione.label.StampaBO11"	bundle="acquisizioniLabels" />
								 <html:radio property="areaEdi" value="si" disabled="${noinput}"/>
								 <bean:message	key="configurazione.label.StampaBO12" bundle="acquisizioniLabels" />
								 <html:radio property="areaEdi" value="no"  disabled="${noinput}"/>
								</td>
							</tr>
							<tr>
								<td>
									 <bean:message key="configurazione.label.StampaBO8"	bundle="acquisizioniLabels" />
								</td>

								<td>
								 <bean:message key="configurazione.label.StampaBO11"	bundle="acquisizioniLabels" />
								 <html:radio property="areaNum" value="si" disabled="${noinput}" />
								 <bean:message	key="configurazione.label.StampaBO12" bundle="acquisizioniLabels" />
								 <html:radio property="areaNum" value="no" disabled="${noinput}"/>
								</td>
							</tr>
							<tr>
								<td>
									 <bean:message key="configurazione.label.StampaBO9"	bundle="acquisizioniLabels" />
								</td>

								<td>
								 <bean:message key="configurazione.label.StampaBO11"	bundle="acquisizioniLabels" />
								 <html:radio property="areaPub" value="si" disabled="${noinput}"/>
								 <bean:message	key="configurazione.label.StampaBO12" bundle="acquisizioniLabels" />
								 <html:radio property="areaPub" value="no" disabled="${noinput}"/>
								</td>
							</tr>

	                        </table>
                        </td>
       	     		</tr>
<tr><td colspan="4">
<hr></hr>
</td></tr>
<tr>

				     <tr>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="configurazione.label.StampaBO10" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							 <bean:message key="configurazione.label.StampaBO11"	bundle="acquisizioniLabels" />
							 <html:radio property="logo" value="si" disabled="${noinput}"/>
							 <bean:message	key="configurazione.label.StampaBO12" bundle="acquisizioniLabels" />
							 <html:radio property="logo" value="no" disabled="${noinput}"/>
                        </td>
       	     		</tr>
				     <tr>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="configurazione.label.StampaBO26" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							(&nbsp;<bean-struts:write  name="configurazioneBOForm" property="imgLogo"/>&nbsp;)&nbsp;
<!-- 						<html:text styleId="testoNormale"   name="configurazioneBOForm" property="imgLogo" size="80"  readonly="${noinput}" ></html:text>-->
							<html:file property="fileIdList" name="configurazioneBOForm" size="80"  disabled="${noinput}"/>
							<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message key="button.caricaImmagineFirma" bundle="acquisizioniLabels" />
							</html:submit>

                        </td>
       	     		</tr>
<tr><td colspan="4">
<hr></hr>
</td></tr>
<tr>

				     <tr>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="configurazione.label.StampaBO29" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							 <bean:message key="configurazione.label.StampaBO11"	bundle="acquisizioniLabels" />
							 <html:radio property="prezzo" value="si" disabled="${noinput}"/>
							 <bean:message	key="configurazione.label.StampaBO12" bundle="acquisizioniLabels" />
							 <html:radio property="prezzo" value="no" disabled="${noinput}" />
                        </td>
       	     		</tr>
				     <tr>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="configurazione.label.StampaBO14" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							 <bean:message key="configurazione.label.StampaBO11"	bundle="acquisizioniLabels" />
							 <html:radio property="numProt" value="si" disabled="${noinput}"/>
							 <bean:message	key="configurazione.label.StampaBO12" bundle="acquisizioniLabels" />
							 <html:radio property="numProt" value="no" disabled="${noinput}" />
                        </td>
       	     		</tr>
				     <tr>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="configurazione.label.StampaBO15" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							 <bean:message key="configurazione.label.StampaBO11"	bundle="acquisizioniLabels" />
							 <html:radio property="dataProt" value="si" disabled="${noinput}"/>
							 <bean:message	key="configurazione.label.StampaBO12" bundle="acquisizioniLabels" />
							 <html:radio property="dataProt" value="no" disabled="${noinput}"/>
                        </td>
       	     		</tr>
				     <tr>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="configurazione.label.StampaBO16" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
   						<bean:message  key="configurazione.label.StampaBO17" bundle="acquisizioniLabels" />

						 <bean:message key="configurazione.label.StampaBO18"	bundle="acquisizioniLabels" />
						 <html:radio property="indicazioneRinnovo" value="originario" disabled="${noinput}"/>
						 <bean:message	key="configurazione.label.StampaBO19" bundle="acquisizioniLabels" />
						 <html:radio property="indicazioneRinnovo" value="precedente" disabled="${noinput}"/>
						 <bean:message	key="configurazione.label.StampaBO20" bundle="acquisizioniLabels" />
						 <html:radio property="indicazioneRinnovo" value="nessuno" disabled="${noinput}"/>

                        </td>
       	     		</tr>
<tr><td colspan="4">
<hr></hr>
</td></tr>
				     <tr>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="configurazione.label.StampaBO30" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							 <bean:message key="configurazione.label.StampaBO11"	bundle="acquisizioniLabels" />
							 <html:radio property="firmaDigit" value="si" disabled="${noinput}"/>
							 <bean:message	key="configurazione.label.StampaBO12" bundle="acquisizioniLabels" />
							 <html:radio property="firmaDigit" value="no" disabled="${noinput}"/>
                        </td>
       	     		</tr>

				     <tr>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="configurazione.label.StampaBO25" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
<!--						<html:text styleId="testoNormale"   name="configurazioneBOForm" property="imgFirma" size="80"  readonly="${noinput}" ></html:text>-->
							(&nbsp;<bean-struts:write  name="configurazioneBOForm" property="imgFirma"/>&nbsp;)&nbsp;
							<html:file property="fileIdListFirma" name="configurazioneBOForm" size="80" disabled="${noinput}"/>
							<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" disabled="${noinput}">
								<bean:message key="button.caricaImmagineFirma" bundle="acquisizioniLabels" />
							</html:submit>



                        </td>
       	     		</tr>
<tr><td colspan="4">
<hr></hr>
</td></tr>
<tr>

				     <tr>
                        <td   scope="col" class="etichetta" align="left" >
							<bean:message  key="configurazione.label.StampaBO24" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							 <bean:message key="configurazione.label.StampaBO11"	bundle="acquisizioniLabels" />
							 <html:radio property="ristampa" value="si" disabled="${noinput}"/>
							 <bean:message	key="configurazione.label.StampaBO12" bundle="acquisizioniLabels" />
							 <html:radio property="ristampa" value="no" disabled="${noinput}"/>
                        </td>
       	     		</tr>
       	</table>

	</td>
</tr>
<tr>
	<td colspan="4">
	<hr color="#dde8f0"></hr>
	</td>
</tr>
<!-- tolto da qui-->
</table>



 </div>
 <div id="divFooter">
		<c:choose>
			<c:when test="${configurazioneBOForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>

		        <table   border="0" style="height:40px"  align="center">
					<tr>
		            <td scope="col">
             <sbn:checkAttivita idControllo="GESTIONE">

					<html:submit styleClass="pulsanti" property="methodConfigurazioneBO" >
						<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodConfigurazioneBO">
						<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
					</html:submit>
             </sbn:checkAttivita>

					<!--
					<html:submit styleClass="pulsanti" property="methodConfigurazioneBO">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>
					 -->
					 </td>
					 </tr>
				</table>
    		</c:otherwise>
		</c:choose>
   </div>
 </sbn:navform>
</layout:page>
