<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/ordini/sinteticaOrdine.do">
  	<div id="divMessaggio">
		<div align="center" class="messaggioInfo"><sbn:errors bundle="acquisizioniMessages" /></div>
	</div>

  <div id="divForm">

	<!--

	<div class="testoNormale"><bean:message key="ordine.label.totOrdini" bundle="acquisizioniLabels" />
	     <html:text styleId="testoNormale" property="numOrdini" size="3" readonly="true"></html:text>
	</div>
	<br>
	-->
	<!--

				<table border="0">
				<tr>
					<td width="200" class="etichetta">
						<bean:message key="servizi.sintetica.numBlocco"
							bundle="serviziLabels" />
						:
						<html:text styleId="testoNormale" property="bloccoSelezionato"
							size="5" readonly="${!navForm.abilitaBlocchi}"></html:text>
						<html:submit property="methodSinteticaOrdine"
							disabled="${!navForm.abilitaBlocchi}">
							<bean:message key="servizi.bottone.blocco" bundle="serviziLabels" />
						</html:submit>
					</td>
					<td width="150" class="etichetta">
						<bean:message key="servizi.sintetica.totBlocchi"
							bundle="serviziLabels" />
						:
						<html:text styleId="testoNormale" property="totBlocchi" size="5"
							readonly="true"></html:text>
					</td>
					<td width="250" class="etichetta">
						<bean:message key="servizi.sintetica.totRighe"
							bundle="serviziLabels" />
						:
						<html:text styleId="testoNormale" property="totRighe" size="5"
							readonly="true"></html:text>
					</td>
				</tr>
			</table>
	-->

  				<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaOrdine" ></sbn:blocchi>

  				<table  align="center" width="100%"   style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"  >

				  	<tr  bgcolor="#dde8f0">
						<td class="etichetta"  scope="col" align="center">
						</td>
				  		<!--
						<td class="etichetta" title="Codice Polo" scope="col" align="center">
							<bean:message key="buono.label.tabPolo" bundle="acquisizioniLabels" />
						</td>
						-->
						<td class="etichetta"  scope="col" align="center">
							<bean:message key="buono.label.tabBibl" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta" style="width:5%;"  scope="col" align="center">
							<bean:message key="buono.label.anno" bundle="acquisizioniLabels" />
						</td>

						<td class="etichetta" style="width:5%;"  scope="col" align="center">
							<bean:message key="ordine.label.tabTipo" bundle="acquisizioniLabels" />
						</td>
						<!--
						<td title="Ordina per tipo" style="width:30%;" title="Tipo Ordine" scope="col" align="center">
				 			<html:submit styleClass="bottoneLink" property="methodSinteticaOrdine">
								<bean:message key="ordine.label.tabTipo" bundle="acquisizioniLabels" />
							</html:submit>
						</td>
						-->
						<td class="etichetta"  scope="col" style="width:3%;" align="center">
							<bean:message key="ordine.label.tabNum" bundle="acquisizioniLabels" />
						</td>
						<!--
						<td title="Ordina per numero" style="width:3%;" scope="col" align="center">
				 			<html:submit styleClass="bottoneLink" property="methodSinteticaOrdine">
								<bean:message key="ordine.label.tabNum" bundle="acquisizioniLabels" />
							</html:submit>
						</td>
						-->
						<td class="etichetta"  scope="col" align="center">
								<bean:message key="buono.label.dataBuono" bundle="acquisizioniLabels" />
						</td>
						<!--
						<td title="Ordina per data" scope="col" align="center">
				 			<html:submit styleClass="bottoneLink" property="methodSinteticaOrdine">
								<bean:message key="buono.label.dataBuono" bundle="acquisizioniLabels" />
							</html:submit>
						</td>
						-->

						<td class="etichetta" title="Stampato" style="width:2%;"  scope="col" align="center">
								<bean:message key="buono.label.tabStato" bundle="acquisizioniLabels" />
						</td>

						<td class="etichetta" title="Stato" style="width:5%;"  scope="col" align="center">
								<bean:message key="ordine.label.stato" bundle="acquisizioniLabels" />
						</td>
						<!--
						<td title="Ordina per stato" style="width:5%;" title="Stato Ordine" scope="col" align="center">
				 			<html:submit styleClass="bottoneLink" property="methodSinteticaOrdine">
								<bean:message key="buono.label.tabStato" bundle="acquisizioniLabels" />
							</html:submit>
						</td>
						-->
						<td class="etichetta" style="width:15%;" scope="col" align="center">
							<bean:message key="ordine.label.bid" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta" style="width:30%;" scope="col" align="center">
							<bean:message key="ordine.label.tabTitolo" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta" title="Natura" scope="col" align="center">
							<bean:message key="ordine.label.tabNatura" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta" title="Continuativo"  scope="col" align="center">
							<bean:message key="ordine.label.tabContinuativo" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta" title="Rinnovato" scope="col" align="center">
							<bean:message key="ordine.label.tabRinnovato" bundle="acquisizioniLabels" /></td>
						<td class="etichetta" colspan="2" style="width:30%;"  scope="col" align="center">
								<bean:message key="ordine.label.fornitore" bundle="acquisizioniLabels" />
						</td>
						<!--
						<td title="Ordina per fornitore" colspan="2" style="width:30%;" scope="col"  align="center">
				 			<html:submit styleClass="bottoneLink" property="methodSinteticaOrdine">
								<bean:message key="ordine.label.fornitore" bundle="acquisizioniLabels" />
							</html:submit>
						</td>
						-->
				<c:choose>
					<c:when test="${navForm.gestBil}">
						<td class="etichetta"  scope="col" align="center" colspan="3">
								<bean:message key="buono.label.tabBilancio" bundle="acquisizioniLabels" />
						</td>
			        </c:when>
			    </c:choose>

						<!--
						<td  title="Ordina per bilancio" scope="col" colspan="3" align="center">
				 			<html:submit styleClass="bottoneLink" property="methodSinteticaOrdine">
								<bean:message key="buono.label.tabBilancio" bundle="acquisizioniLabels" />
							</html:submit>
						</td>
						-->
						<!--

						<td class="etichetta" style="width:1%;" scope="col" align="center">
						</td>
						-->
						<td  class="etichetta"  scope="col" align="center" style="width:3%;">
						<!-- <bean:message  key="ordine.label.azione" bundle="acquisizioniLabels" />-->
						</td>

					</tr>

		<c:choose>
			<c:when test="${navForm.risultatiPresenti}">
				<logic:iterate id="item" property="listaOrdini"   name="navForm">

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
		<!--				<td align="center"><bs:write  name="item" property="codPolo"/></td>-->
						<td bgcolor="${color}" >
							<sbn:linkbutton index="progressivo" name="item"
								value="progressivo"  key="button.ok"
								bundle="acquisizioniLabels" title="esamina" property="progrForm" />
						</td>
						<td align="center"><bs:write  name="item" property="codBibl"/></td>
						<td align="center"><bs:write  name="item" property="annoOrdine"/></td>
						<td align="center"><bs:write  name="item" property="tipoOrdine"/></td>
						<td align="center"><bs:write  name="item" property="codOrdine"/></td>
						<td align="center"><bs:write  name="item" property="dataOrdine"/></td>
						<td align="center"><html:checkbox name="item" property="stampato" disabled="true"></html:checkbox></td>
						<td align="center"><bs:write  name="item" property="statoOrdine"/></td>
						<td align="center" style="width:5%;"><bs:write  name="item" property="titolo.codice"/></td>
						<td align="left"><bs:write  name="item" property="titolo.descrizione"/></td>
						<td align="center"><bs:write  name="item" property="naturaOrdine"/></td>
						<td  scope="col">
						<html:checkbox name="item" property="continuativo" disabled="true"></html:checkbox>
						</td>
						<td scope="col"><html:checkbox name="item" property="rinnovato" disabled="true" /></td>
						<td align="center" style="width:5%;"><bs:write  name="item" property="fornitore.codice"/></td>
						<td align="center"><bs:write  name="item" property="fornitore.descrizione"/></td>
				<c:choose>
					<c:when test="${item.gestBil}">
						<td align="center"><bs:write  name="item" property="bilancio.codice1"/></td>
						<td align="center"><bs:write  name="item" property="bilancio.codice2"/></td>
						<td align="center"><bs:write  name="item" property="bilancio.codice3"/></td>
			        </c:when>
			    </c:choose>

<!--
						<td align="center">
						<c:choose>
					        <c:when test='${item.tipoVariazione=="M"}'>
								<img border="0"  align="middle" alt="oggetto modificato"  src='<c:url value="/styles/images/pennino.jpg" />'/>
					        </c:when>
					        <c:otherwise>
								<bs:write  name="item" property="tipoVariazione"/>
					        </c:otherwise>
					    </c:choose>

						</td>
-->
						<td>
							<html:multibox name="navForm" property="selectedOrdini" >
								<bs:write name="item" property="chiave"  />
							</html:multibox>
						</td>

						<!--
						<bs:write  name="item" property="tipoVariazione"/>
						<td>
							<html:multibox property="selectedOrdini">
								<bs:write name="item" property="codOrdine" />
							</html:multibox>
						</td>
						-->

					</tr>
				</logic:iterate>
			</c:when>
		</c:choose>
	</table>

</div>
 		<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaOrdine" bottom="true" ></sbn:blocchi>
		</div>
 <div id="divFooter">
           <table  align="center"  border="0" style="height:40px" >

            <tr>
               <td align="center">
			<c:choose>
				<c:when test="${navForm.visibilitaIndietroLS}">
					<html:submit styleClass="pulsanti" property="methodSinteticaOrdine">
						<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
					</html:submit>
				</c:when>
				<c:otherwise>
					<html:submit styleClass="buttonSelezTuttiR" title="Seleziona tutto" property="methodSinteticaOrdine">
						<bean:message key="ricerca.button.selTutti" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="buttonSelezNessunoR" title="Deseleziona tutto" property="methodSinteticaOrdine">
						<bean:message key="ricerca.button.deselTutti" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodSinteticaOrdine">
						<bean:message key="ricerca.button.operazionesuordine" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodSinteticaOrdine">
						<bean:message key="ricerca.button.stampa" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodSinteticaOrdine">
						<bean:message key="ricerca.button.stampaBollettario" bundle="acquisizioniLabels" />
					</html:submit>

				</c:otherwise>
			</c:choose>


			<html:submit styleClass="pulsanti" property="methodSinteticaOrdine">
				<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodSinteticaOrdine">
				<bean:message key="ricerca.button.listaInventariOrdine" bundle="acquisizioniLabels" />
			</html:submit>

			<!--
			<html:submit styleClass="pulsanti" property="methodSinteticaOrdine">
				<bean:message key="ricerca.button.controllaordine" bundle="acquisizioniLabels" />
			</html:submit>
             -->
			<html:submit styleClass="pulsanti" property="methodSinteticaOrdine">
				<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
			</html:submit>
		<c:choose>
			<c:when test="${!navForm.LSRicerca}">
             <sbn:checkAttivita idControllo="CREA">
			<html:submit styleClass="pulsanti" property="methodSinteticaOrdine">
				<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
			</html:submit>
             </sbn:checkAttivita>

			</c:when>
		</c:choose>
			<!--
			<c:choose>
				<c:when test="${navForm.provenienzaVAIA}">
				</c:when>
			</c:choose>
			 -->
			 </td>
          </tr>
      	  </table>
	</div>




	</sbn:navform>
</layout:page>

