<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>


<bs:define id="noinputStampato"  value="false"/>
<bs:define id="noinput"  value="false"/>

<c:choose>
<c:when test="${esaminaOrdineRForm.datiOrdine.stampato}">
	<bs:define id="noinputStampato"  value="false"/>
	<bs:define id="noinput"  value="true"/>
</c:when>
</c:choose>

<c:choose>
<c:when test="${esaminaOrdineRForm.disabilitaTutto}">
	<bs:define id="noinputStampato"  value="true"/>
	<bs:define id="noinput"  value="true"/>
</c:when>
</c:choose>



<html:xhtml />

<layout:page>
	<sbn:navform action="/acquisizioni/ordini/esaminaOrdineR.do">

  <div id="divForm">
	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<table  width="100%"    border="0" >
	     <tr><td class="etichetta" colspan="7">
               <bean:message  key="ricerca.label.totInventari" bundle="acquisizioniLabels" />
			<html:text styleId="testoNormale"   property="numRigheInv" name="esaminaOrdineRForm" size="3" readonly="true" />
	     </td></tr>
		<tr>
         <td colspan="7">
	  	  <table  align="center" width="100%" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" style="width:5%;" align="center">#</td>
						<td scope="col" style="width:5%;" align="center">Vol</td>
						<td align="center"  style="width:10%;"  scope="col" >
		                   	<bean:message  key="ricerca.label.serie" bundle="acquisizioniLabels" />
						</td>

						<td align="center"  style="width:20%;"  scope="col" >
		                   	<bean:message  key="ordine.label.numInv" bundle="acquisizioniLabels" />
						</td>
						<sbn:checkAttivita idControllo="RFID">
								<c:if test="${not noinput}">
									<td align="center" scope="col">RFID</td>
								</c:if>
								</sbn:checkAttivita>
						<td align="center" scope="col" style="width:45%;">
		                   	<bean:message  key="ordine.label.titInv" bundle="acquisizioniLabels" />
						</td>
						<td align="center" style="width:10%;" scope="col">
		                   	<bean:message  key="ordine.label.rilegaturaDataUscita" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" style="width:10%;">
		                   	<bean:message  key="ordine.label.rilegaturaDataRientroPresunta" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" style="width:10%;">
		                   	<bean:message  key="ordine.label.rilegaturaDataRientro" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" style="width: 10%;" >
                  			<bean:message  key="ricerca.label.note" bundle="acquisizioniLabels" />
						</td>

	     				<td scope="col" style="width:5%;" align="center" >&nbsp;</td>

					</tr>
			<logic:greaterThan property="numRigheInv" name="esaminaOrdineRForm" value="0">
				<script>
					setFocusOnLoad('${requestScope.last}');
				</script>
				<logic:iterate id="elencaInventari" property="elencaInventari" name="esaminaOrdineRForm" indexId="indice">
				   <sbn:rowcolor var="color" index="indice" />
						<tr class="testoNormale" bgcolor="${color}">
		<!-- controllo se è stato selezionata l'operazione di inserimento riga -->
							<td align="center" >
								<sbn:anchor name="elencaInventari" property="repeatableId"/>
								<bs:write name="elencaInventari" property="posizione"/>
							</td>
							<td align="center" >
								<bs:write name="elencaInventari" property="volume"/>
							</td>
					<%-- <logic:equal  name="elencaInventari" property="serie" value=""> --%>
							<td align="center" >
								<html:select  styleClass="testoNormale" style="width:40px;" name="elencaInventari" indexed="true" property="serie" disabled="${noinput}">
								<html:optionsCollection  property="listaSerie" value="codSerie" label="codSerie" />
								</html:select>
							</td>
					<%-- </logic:equal>
					<logic:notEqual  name="elencaInventari" property="serie"  value="">
							<td align="center" >
								<html:text styleId="testoNormale"  name="elencaInventari" indexed="true" property="serie" size="3" maxlength="3" disabled="${noinput}" ></html:text>
							</td>
					</logic:notEqual> --%>


							<td align="center" >
								<html:text styleId="testoNormale"  name="elencaInventari" indexed="true" property="numero" size="10" maxlength="9" disabled="${noinput}" />
								<sbn:checkAttivita idControllo="RFID">
								<c:if test="${not noinput}">
									<td><html:text styleId="${indice}" name="elencaInventari" indexed="true" property="rfid" size="18"
									onkeydown="submitOnEnter(event, 'btnCompleta')" /></td>
								</c:if>
								</sbn:checkAttivita>
							</td>

							<td align="center" >
								<bs:write   name="elencaInventari" property="titolo"  />
							</td>
							<td style="text-align: right;" >
								<html:text styleId="testoNormale"  indexed="true" name="elencaInventari" property="dataUscita" size="10" maxlength="10" disabled="${noinput}" ></html:text>
							</td>
							<td style="text-align: right;">
								<html:text styleId="testoNormale" indexed="true" name="elencaInventari" property="dataRientroPresunta" size="10" maxlength="10" disabled="${noinput}" ></html:text>
							</td>
							<td style="text-align: right;">
								<html:text styleId="testoNormale" indexed="true" name="elencaInventari" property="dataRientro" size="10" maxlength="10" disabled="${noinputStampato or not navForm.datiOrdine.spedito}" ></html:text>
							</td>
							<bs:define id="operazioneValue" value="${indice + 1}" />

							<td   scope="col" align="center" width="20%">
								<input type="submit"  class="buttonImageNote"  name="tagNote" value="${indice}" title="note"></input>
									<c:choose>
										<c:when test="${elencaInventari.esisteNota}">
											*
										</c:when>
									</c:choose>

							</td>

							<td  class="testoNormale" align="left">
								<html:multibox styleId="${elencaInventari.repeatableId}" property="radioInv" value="${elencaInventari.repeatableId}" disabled="${noinputStampato}" />
								<html:hidden property="radioInv" value="0" disabled="${noinputStampato}" />
							</td>

						</tr>
						<c:choose>
						<c:when test="${esaminaOrdineRForm.clicNotaPrg == indice }">
							  	<tr class="testoNormale" bgcolor="${color}" >
									<td colspan="8" scope="col" align="center" >
				               			<bean:message  key="ordine.label.noteEtic" bundle="acquisizioniLabels" />
										<c:choose>
										<c:when test="${esaminaOrdineRForm.disabilitaTutto }">
													<html:text styleId="testoNormale" name="elencaInventari" property="note" size="100"  readonly="true" ></html:text>
										</c:when>
										<c:otherwise>
											<html:textarea style="background-color: #FFFFCC;" styleId="testoNormale"  name="elencaInventari" indexed="true"  property="note" rows="1" cols="100" ></html:textarea>
											<sbn:tastiera limit="80" name="esaminaOrdineRForm"  property="elencaInventari[${indice}].note"></sbn:tastiera>
										</c:otherwise>
										</c:choose>
									</td>
								</tr>
						</c:when>
					</c:choose>
			</logic:iterate>
		</logic:greaterThan>
        </table>
		</td>
		</tr>
		<tr>
         <td colspan="7"></td>
		</tr>
      </table>

</div>
 <div id="divFooter">
		<c:choose>
			<c:when test="${esaminaOrdineRForm.conferma}">
<!--				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />-->
				<table align="center">
					<tr>
						<td><html:submit styleClass="pulsanti" property="methodEsaminaOrdineR">
								<bean:message key="acquisizioni.bottone.si" bundle="acquisizioniLabels" /></html:submit>
							<html:submit styleClass="pulsanti" property="methodEsaminaOrdineR">
								<bean:message key="acquisizioni.bottone.no" bundle="acquisizioniLabels" /></html:submit></td>
					</tr>
				</table>

			</c:when>
			<c:otherwise>
           <!-- tabella bottoni -->

           <table align="center"  border="0" style="height:40px;" cellspacing="0" cellpadding="0">
           <tr>
           <td style="text-align: center;">
	           <bean:message key="ordine.label.dataInv" bundle="acquisizioniLabels" />&nbsp;
	           <html:select  styleClass="testoNormale" name="navForm" property="tipoData" disabled="${esaminaOrdineRForm.disabilitaTutto}" >
					<html:optionsCollection  property="listaTipoData" value="codice" label="descrizione" />
				</html:select>&nbsp;
				<html:text property="data" maxlength="10" size="10" disabled="${esaminaOrdineRForm.disabilitaTutto}" />&nbsp;
				<html:submit styleClass="pulsanti" property="methodEsaminaOrdineR">
					<bean:message key="ricerca.label.rilegaturaData" bundle="acquisizioniLabels" />
				</html:submit>
           </td>
           </tr>
            <tr>
             <td>
				<c:choose>
					<c:when test="${!esaminaOrdineRForm.disabilitaTutto}">
						<c:choose>
							<c:when test="${!esaminaOrdineRForm.datiOrdine.stampato}">
								<html:submit styleClass="pulsanti" property="methodEsaminaOrdineR">
									<logic:equal name="esaminaOrdineRForm" property="lockVol" value="true">
										<bean:message key="ordine.button.unlockVolume" bundle="acquisizioniLabels" />
									</logic:equal>
									<logic:equal name="esaminaOrdineRForm" property="lockVol" value="false">
										<bean:message key="ordine.button.lockVolume" bundle="acquisizioniLabels" />
									</logic:equal>
								</html:submit>
								<html:submit styleClass="pulsanti" property="methodEsaminaOrdineR">
									<bean:message key="ricerca.button.aggRiga" bundle="acquisizioniLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" property="methodEsaminaOrdineR">
									<bean:message key="ricerca.button.insRiga" bundle="acquisizioniLabels" />
								</html:submit>
								<html:submit styleClass="pulsanti" property="methodEsaminaOrdineR">
									<bean:message key="ricerca.button.cancRiga" bundle="acquisizioniLabels" />
								</html:submit>
								<html:submit property="methodEsaminaOrdineR"
									styleClass="buttonFrecciaSu"
									titleKey="servizi.bottone.frecciaSu" bundle="serviziLabels">
									<bean:message key="servizi.bottone.frecciaSu" bundle="serviziLabels" />
								</html:submit>
								<html:submit property="methodEsaminaOrdineR"
									styleClass="buttonFrecciaGiu"
									titleKey="servizi.bottone.frecciaGiu" bundle="serviziLabels">
									<bean:message key="servizi.bottone.frecciaGiu"
										bundle="serviziLabels" />
								</html:submit>
								<html:submit styleId="btnCompleta" styleClass="pulsanti" property="methodEsaminaOrdineR">
									<bean:message key="ricerca.button.completa" bundle="acquisizioniLabels" />
								</html:submit>
							</c:when>
						</c:choose>
						<html:submit styleClass="buttonSelezTutti"
							property="methodEsaminaOrdineR" title="Seleziona tutto">
							<bean:message key="button.selAllTitoli"
								bundle="gestioneBibliograficaLabels" />
						</html:submit>
						<html:submit styleClass="buttonSelezNessuno"
							property="methodEsaminaOrdineR" title="Deseleziona tutto">
							<bean:message key="button.deSelAllTitoli"
								bundle="gestioneBibliograficaLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodEsaminaOrdineR">
							<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
						</html:submit>
						<!-- <html:submit styleClass="pulsanti" property="methodEsaminaOrdineR">
							<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
						</html:submit> -->
					</c:when>
				</c:choose>
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineR">
				<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
			</html:submit>

             </td>
			 </tr>
      	  </table>
      	  </c:otherwise>
		</c:choose>

</div>

	</sbn:navform>
</layout:page>

