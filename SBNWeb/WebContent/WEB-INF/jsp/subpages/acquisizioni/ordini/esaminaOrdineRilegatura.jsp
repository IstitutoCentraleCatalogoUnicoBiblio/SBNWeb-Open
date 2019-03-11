<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="formName" name="org.apache.struts.action.mapping.instance" property="name" type="java.lang.String" />

<c:choose>
<c:when test="${esaminaOrdineModForm.ordineApertoAbilitaInput}">
	<bean-struts:define id="ordineAperto" value="true"/>
</c:when>
<c:otherwise>
	<bean-struts:define id="ordineAperto" value="false"/>
</c:otherwise>
</c:choose>

<c:choose>
<c:when test="${formName eq 'esaminaOrdineModForm'}">
	<bean-struts:define id="faseModifica" value="true"/>
    <html:hidden name="esaminaOrdineModForm" property="submitDinamico" />
</c:when>
<c:otherwise>
	<bean-struts:define id="faseModifica" value="false"/>
    <html:hidden name="esaminaOrdineForm" property="submitDinamico" />
</c:otherwise>
</c:choose>


<bean-struts:define id="noinput"  value="false"/>
<c:choose>
<c:when test="${esaminaOrdineModForm.disabilitaTutto}">
	<bean-struts:define  id="faseModifica"  value="true"/>
	<bean-struts:define id="ordineAperto" value="true"/>
	<bean-struts:define id="noinput"  value="true"/>
</c:when>
</c:choose>

<c:choose>
<c:when test="${esaminaOrdineForm.disabilitaTutto}">
	<bean-struts:define id="faseModifica"  value="true"/>
	<bean-struts:define id="ordineAperto" value="true"/>
	<bean-struts:define id="noinput"  value="true"/>
</c:when>
</c:choose>




        <tr>
		  <td  class="etichetta"><bean:message  key="ordine.label.numero" bundle="acquisizioniLabels" /></td>
		  <td ><html:text styleId="testoNormale"  property="datiOrdine.codOrdine" size="2"  readonly="true"></html:text></td>
          <td  class="etichetta"><bean:message  key="ordine.label.data" bundle="acquisizioniLabels" /></td>
          <td ><html:text styleId="testoNormale"  property="datiOrdine.dataOrdine" size="10" readonly="${ordineAperto}"></html:text></td>
          <td  class="etichetta" align="right"><bean:message  key="ordine.label.stato" bundle="acquisizioniLabels" />
          <html:select  disabled="true"  styleClass="testoNormale"  property="datiOrdine.statoOrdine"    style="width:40px">
		  <html:optionsCollection property="listaStato" value="codice" label="descrizioneCodice" />
		  </html:select>
          </td>
          <td width="9%" class="etichetta" align="right"> </td>
          <td width="12%" class="testoNormale"></td>
        </tr>

        <tr>
	    <td class="etichetta" valign="top" ><bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" /></td>
        <td  colspan="4" scope="col" align="left" valign="top" >
 		  <html:text styleId="testoNormale" property="datiOrdine.fornitore.codice" size="5"  maxlength="10" readonly="${noinput}"></html:text>
 		  <html:text styleId="testoNormale"   property="datiOrdine.fornitore.descrizione" size="30"  maxlength="50" readonly="${noinput}"></html:text>
		<c:choose>
		<c:when test="${formName eq 'esaminaOrdineModForm'}">
	        <html:submit  styleClass="buttonImage" property="methodEsaminaOrdineMod" disabled="${noinput}">
				<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
			</html:submit>
		</c:when>
		<c:otherwise>
	        <html:submit  styleClass="buttonImage" property="methodEsaminaOrdine"  disabled="${noinput}">
				<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
			</html:submit>
		</c:otherwise>
		</c:choose>

		</td>
          <td>&nbsp;</td>
          <td valign="top" class="etichetta"></td>
          <td valign="top"></td>
        </tr>

		<tr >
          <td class="etichettaIntestazione" valign="top">
          <bean:message  key="ordine.label.bilancio" bundle="acquisizioniLabels" />
          </td>
          <td  class="etichetta" valign="top">
          <bean:message  key="ordine.label.esercizio" bundle="acquisizioniLabels" />
          </td>
          <td  class="testoNormale" valign="top">
			<html:text styleId="testoNormale"  property="datiOrdine.bilancio.codice1" size="4" readonly="${ordineAperto}" ></html:text>
          </td>
          <td class="etichetta" valign="top"><bean:message  key="ordine.label.capitolo" bundle="acquisizioniLabels" /></td>
          <td  class="testoNormale" valign="top" >
			<html:text styleId="testoNormale"   property="datiOrdine.bilancio.codice2" maxlength="16"  size="2" readonly="${ordineAperto}" ></html:text>
          </td>
          <td  class="etichetta" colspan="3" valign="top"><bean:message  key="ordine.label.tipoImpegno" bundle="acquisizioniLabels" />
			<html:select style="width:40px" styleClass="testoNormale" property="datiOrdine.bilancio.codice3" value="4" disabled="true">
			<html:optionsCollection  property="listaTipoImpegno" value="codice" label="descrizioneCodice" />
			</html:select>
		<c:choose>
		<c:when test="${formName eq 'esaminaOrdineModForm'}">
            <html:submit  styleClass="buttonImage" property="methodEsaminaOrdineMod" disabled="${ordineAperto}">
				<bean:message  key="ordine.label.bilancio" bundle="acquisizioniLabels" />
			</html:submit>
		</c:when>
		<c:otherwise>
            <html:submit  styleClass="buttonImage" property="methodEsaminaOrdine" disabled="${ordineAperto}">
				<bean:message  key="ordine.label.bilancio" bundle="acquisizioniLabels" />
			</html:submit>
		</c:otherwise>
		</c:choose>
		  </td>
        </tr>

		<tr>
         <td colspan="7">
	  	  <table  align="center" width="100%" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0">
	     				<td scope="col" style="width:5%;" align="center" >&nbsp;</td>
						<td align="center"  style="width:10%;" title="Codice impegno" scope="col" >
		                   	<bean:message  key="ordine.label.numInv" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" style="width:15%;">
		                   	<bean:message  key="ordine.label.titInv" bundle="acquisizioniLabels" />
						</td>
						<td align="center" style="width:20%;" scope="col">
		                   	<bean:message  key="ordine.label.rilegaturaDataUscita" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" style="width:10%;">
		                   	<bean:message  key="ordine.label.rilegaturaDataRientro" bundle="acquisizioniLabels" />
						</td><!--
						<td align="center" scope="col" style="width:10%;">
		                   	<bean:message  key="ricerca.label.pagato" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col">
		                   	<bean:message  key="ricerca.label.disponibilitaCassa" bundle="acquisizioniLabels" />
						</td>
					--></tr>
			<logic:greaterThan property="numRigheInv" name="esaminaOrdineForm" value="0">

				<logic:iterate id="elencaInventari" property="elencaInventari" name="esaminaOrdineForm" indexId="indice">
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
							<bean-struts:define id="operazioneValue">
							  <bean-struts:write  name="indice" />
							</bean-struts:define>

							<td  class="testoNormale" align="left">
								<html:radio name="esaminaOrdineForm" indexed="true" property="radioInv"  value="${operazioneValue}" ></html:radio>
							</td>

				<logic:notEqual  name="elencaInventari" property="codice" value="">
							<td align="center" >
								<bean-struts:write   name="elencaInventari" property="descrizione"  />
							</td>

							<td align="center" >
								<bean-struts:write   name="elencaInventari" property="codice"  />

							</td>
							<td style="text-align: right;" >
								<bean-struts:write   name="elencaInventari" property="codice"  />
							</td>
							<td style="text-align: right;">
								<bean-struts:write   name="elencaInventari" property="codice"  />
							</td><!--
							<td style="text-align: right;" >
								<bean-struts:write    name="elencaInventari" property="codice"  />
							</td>
							<td style="text-align: right;">
								<bean-struts:write    name="elencaInventari" property="codice" />
							</td>

				</logic:notEqual>
				<!-- controllo se è stato selezionata l'operazione di inserimento riga -->

				<logic:equal  name="elencaInventari"   property="codice" value="">

							<td align="center" >
								<html:text styleId="testoNormale"  name="elencaInventari" indexed="true" property="descrizione" size="1" readonly="true" ></html:text>
							</td>

							<td align="center" >
								<html:text styleId="testoNormale"  name="elencaInventari" indexed="true"  property="codice" size="10" readonly="${noinput}" ></html:text>
							</td>
							<td style="text-align: right;" >
								<bean-struts:write    name="elencaInventari" property="codice"  />
							</td>
							<td style="text-align: right;">
								<bean-struts:write    name="elencaInventari" property="codice" />
							</td><!--
							<td style="text-align: right;" >
								<bean-struts:write    name="elencaInventari" property="codice"  />
							</td>
							<td style="text-align: right;">
								<bean-struts:write    name="elencaInventari" property="codice" />
							</td>
				</logic:equal>

						--></tr>
			</logic:iterate>
		</logic:greaterThan>
        </table>
		</td>
		</tr>

		<tr>
         <td colspan="7" align="center">

<!--              		<input type="submit" property="prova"  value="Inserisci" title="Rilegatura"></input>-->
					<c:choose>
					<c:when test="${formName eq 'esaminaOrdineModForm'}">
						<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
							<bean:message key="ricerca.button.insRiga" bundle="acquisizioniLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
							<bean:message key="ricerca.button.cancRiga" bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
					<c:otherwise>
						<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
							<bean:message key="ricerca.button.insRiga" bundle="acquisizioniLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
							<bean:message key="ricerca.button.cancRiga" bundle="acquisizioniLabels" />
						</html:submit>
					</c:otherwise>
					</c:choose>
		</td>
		</tr>



