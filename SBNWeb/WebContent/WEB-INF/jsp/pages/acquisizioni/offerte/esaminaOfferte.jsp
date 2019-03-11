<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput"  value="false"/>
<c:choose>
<c:when test="${esaminaOfferteForm.disabilitaTutto}">
	<bean-struts:define id="noinput"  value="true"/>
</c:when>
</c:choose>


<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/offerte/esaminaOfferte.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
<table  width="100%" border="0" >
              <tr>
                <td  class="etichetta" width="9%"  scope="col" align="left">
			         <bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
                </td>
                <td scope="col" width="17%" align="left">
  					<html:text styleId="testoNormale" property="datiOffertaForn.codBibl" size="4" readonly="true"></html:text>
                </td>
                <td  class="etichetta" width="9%" scope="col" align="left">
			         <bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
                </td>
                <td scope="col" width="10%" align="left">
<!--  					<html:text styleId="testoNormale" property="datiOffertaForn.tipoProvenienza.codice" size="10" readonly="true"></html:text>                        -->
					<html:select  styleClass="testoNormale" property="datiOffertaForn.tipoProvenienza.codice" style="width:40px;" disabled="true">
					<html:optionsCollection  property="listaTipoProvenienza" value="codice" label="descrizioneCodice" />
					</html:select>

                </td>
                <td  width="9%" scope="col" class="etichetta" align="left">
			         <bean:message  key="ricerca.label.nr" bundle="acquisizioniLabels" />
                </td>
                <td scope="col" width="17%" align="left">
  					<html:text styleId="testoNormale" property="datiOffertaForn.identificativoOfferta" size="10" readonly="true"></html:text>
                </td>
                <td width="4%" scope="col" class="etichetta" align="left">
			         <bean:message  key="ricerca.label.dataOff" bundle="acquisizioniLabels" />
                </td>
                <td scope="col" width="25%" align="left">
  					<html:text styleId="testoNormale" property="datiOffertaForn.dataOfferta" size="10" readonly="true"></html:text>
                </td>
              </tr>
              <tr>
                <td class="etichetta"  scope="col" width="9%" align="left">
			         <bean:message  key="ordine.label.forn" bundle="acquisizioniLabels" />
                </td>
                <td scope="col" colspan="5" align="left">
  					<html:text styleId="testoNormale" property="datiOffertaForn.fornitore.descrizione" size="40" readonly="true"></html:text>
                </td>
                <td scope="col" class="etichetta">
			         <bean:message  key="buono.label.statoBuono" bundle="acquisizioniLabels" />
                </td>
                <td scope="col">
<!--  					<html:text styleId="testoNormale" property="datiOffertaForn.statoOfferta" size="10" readonly="true"></html:text>                        -->
					<html:select  styleClass="testoNormale" property="datiOffertaForn.statoOfferta" style="width:40px;" disabled="true">
					<html:optionsCollection  property="listaStatoSuggOfferta" value="codice" label="descrizioneCodice" />
					</html:select>

                </td>
              </tr>
              <tr>
                <td class="etichetta" scope="col" width="9%" align="left">
			         <bean:message  key="ordine.label.valuta" bundle="acquisizioniLabels" />
                </td>
                <td scope="col" width="17%" align="left">
  					<html:text styleId="testoNormale" property="datiOffertaForn.valutaOfferta" size="10" readonly="true"></html:text>
                </td>
                <td scope="col" class="etichetta" width="9%" align="left">
			         <bean:message  key="ordine.label.tPrezzo" bundle="acquisizioniLabels" />
                </td>
                <td scope="col"  align="left">
<!--  					<html:text styleId="testoNormale" property="datiOffertaForn.tipoPrezzo" size="10" readonly="true"></html:text>                        -->
					<html:select  styleClass="testoNormale" property="datiOffertaForn.tipoPrezzo" style="width:40px;" disabled="true" >
					<html:optionsCollection  property="listaTipoPrezzoOfferta" value="codice" label="descrizione"  />
					</html:select>

                </td>
                <td scope="col" class="etichetta">
			         <bean:message  key="ordine.label.prezzo" bundle="acquisizioniLabels" />
                </td>
                <td scope="col">
  					<html:text styleId="testoNormale" property="datiOffertaForn.prezzo" size="10" readonly="true"></html:text>
                </td>
                <td scope="col" class="etichetta">
			         <bean:message  key="ordine.label.infPrezzo" bundle="acquisizioniLabels" />
                </td>
                <td scope="col">
  					<html:text styleId="testoNormale" property="datiOffertaForn.informazioniPrezzo" size="10" readonly="true"></html:text>
                </td>
              </tr>
              <tr>
                <td scope="col" class="etichetta" width="9%"  align="left">
			         <bean:message  key="ricerca.label.tData" bundle="acquisizioniLabels" />
                </td>
                <td scope="col" width="17%" align="left">
<!--  					<html:text styleId="testoNormale" property="datiOffertaForn.tipoData" size="10" readonly="true"></html:text>                        -->
					<html:select  styleClass="testoNormale" property="datiOffertaForn.tipoData" style="width:40px;" disabled="true">
					<html:optionsCollection  property="listaTipoDataOfferta" value="codice" label="descrizione" />
					</html:select>

                </td>
                <td scope="col"  class="etichetta" width="9%" align="left">
			         <bean:message  key="buono.label.dataBuono" bundle="acquisizioniLabels" />
                </td>
                <td scope="col" align="left">
  					<html:text styleId="testoNormale" property="datiOffertaForn.data" size="10" readonly="true"></html:text>
                </td>
                <td scope="col" class="etichetta">
			         <bean:message  key="ricerca.label.numStd" bundle="acquisizioniLabels" />
                </td>
                <td scope="col">
  					<html:text styleId="testoNormale" property="datiOffertaForn.numeroStandard" size="10" readonly="true"></html:text>
                </td>
              </tr>
              <tr>
                <td scope="col" class="etichetta">
			         <bean:message  key="ricerca.label.paese" bundle="acquisizioniLabels" />
                </td>
                <td scope="col">
<!--  					<html:text styleId="testoNormale" property="datiOffertaForn.paese" size="10" readonly="true"></html:text>                        -->
					<html:select  styleClass="testoNormale" property="datiOffertaForn.paese.codice" style="width:40px;" disabled="true">
					<html:optionsCollection  property="listaPaesi" value="codice" label="descrizioneCodice" />
					</html:select>
                </td>
                <td scope="col" class="etichetta">
			         <bean:message  key="ricerca.label.lingua" bundle="acquisizioniLabels" />
                </td>

                <td scope="col">
<!--  					<html:text styleId="testoNormale" property="datiOffertaForn.lingua" size="10" readonly="true"></html:text>                        -->
					<html:select  styleClass="testoNormale" property="datiOffertaForn.lingua.codice" style="width:50px;" disabled="true">
					<html:optionsCollection  property="listaLingue" value="codice" label="descrizioneCodice" />
					</html:select>

                </td>
              </tr>
              <tr>
                <td  class="etichetta" colspan="4">
			         <bean:message  key="ricerca.label.codStd" bundle="acquisizioniLabels" />
  					<html:text styleId="testoNormale" property="datiOffertaForn.codiceStandard" size="10" readonly="true"></html:text>
                </td>
                <td  class="etichetta">
			         <bean:message  key="ordine.label.bid" bundle="acquisizioniLabels" />
                </td>
                <td  class="etichetta" colspan="3">
  					<html:text styleId="testoNormale" property="datiOffertaForn.bid.codice" size="10" readonly="${noinput}"></html:text>
<!--			  		<html:link action="/gestionebibliografica/titolo/interrogazioneTitolo.do" ><img border="0"   alt="ricerca titolo"  src='<c:url value="/images/lente.GIF" />'/></html:link>-->
			              <html:submit  styleClass="buttonImage" property="methodEsaminaOfferte">
							<bean:message  key="ordine.bottone.searchTit" bundle="acquisizioniLabels" />
						  </html:submit>

                </td>
              </tr>

            </table>

    <hr width="0" size="0">
    <hr width="0" size="0">

		<logic:equal  name="esaminaOfferteForm" property="scegliTAB" value="">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/offerte/esaminaOffTabAut.jsp" />
		</logic:equal>
		<logic:equal  name="esaminaOfferteForm" property="scegliTAB" value="AUT">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/offerte/esaminaOffTabAut.jsp" />
		</logic:equal>
		<logic:equal  name="esaminaOfferteForm" property="scegliTAB" value="SER">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/offerte/esaminaOffTabSerie.jsp" />
		</logic:equal>
		<logic:equal  name="esaminaOfferteForm" property="scegliTAB" value="SOG">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/offerte/esaminaOffTabSogg.jsp" />
		</logic:equal>
		<logic:equal  name="esaminaOfferteForm" property="scegliTAB" value="CLA">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/offerte/esaminaOffTabClass.jsp" />
		</logic:equal>
		<logic:equal  name="esaminaOfferteForm" property="scegliTAB" value="NOT">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/offerte/esaminaOffTabNote.jsp" />
		</logic:equal>
		<logic:equal  name="esaminaOfferteForm" property="scegliTAB" value="ISB">
			<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/offerte/esaminaOffTabIsbd.jsp" />
		</logic:equal>

</div>
 <div id="divFooter">
		<c:choose>
			<c:when test="${esaminaOfferteForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
		         <table align="center"  border="0" style="height:40px" >
		             <tr>
		             <td>
						<logic:equal name="esaminaOfferteForm" property="enableScorrimento" value="true">
									<html:submit styleClass="pulsanti" property="methodEsaminaOfferte">
										<bean:message key="ricerca.button.scorriIndietro" bundle="acquisizioniLabels" />
									</html:submit>
									<html:submit styleClass="pulsanti" property="methodEsaminaOfferte">
										<bean:message key="ricerca.button.scorriAvanti" bundle="acquisizioniLabels" />
									</html:submit>
						</logic:equal>
				<c:choose>
					<c:when test="${!esaminaOfferteForm.disabilitaTutto}">

						<html:submit styleClass="pulsanti" property="methodEsaminaOfferte">
							<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti" property="methodEsaminaOfferte">
							<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
						</html:submit>
			 			<html:submit styleClass="pulsanti" property="methodEsaminaOfferte">
							<bean:message key="ricerca.button.accetta" bundle="acquisizioniLabels" />
						</html:submit>

					</c:when>
				</c:choose>

			 			<html:submit styleClass="pulsanti" property="methodEsaminaOfferte">
							<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
						</html:submit>
		             </td>
		             </tr>
		      	  </table>
			  			<!-- fine tabella bottoni -->
    		</c:otherwise>
		</c:choose>
     	  </div>
	</sbn:navform>
</layout:page>
