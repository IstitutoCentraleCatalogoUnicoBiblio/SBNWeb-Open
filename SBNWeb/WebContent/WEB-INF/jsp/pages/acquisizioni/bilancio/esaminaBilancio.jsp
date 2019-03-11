<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bs:define id="noinput"  value="false"/>

<c:choose>
<c:when test="${esaminaBilancioForm.disabilitaTutto}">
	<bs:define id="noinput"  value="true"/>
</c:when>
</c:choose>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/bilancio/esaminaBilancio.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
<table  align="center" width="100%" border="0">
     <tr>
         <!-- tabella corpo COLONNA + LARGA -->
        <td align="left" valign="top" width="100%">
		<table  width="100%" border="0">
		     <tr><td colspan="7" class="etichettaIntestazione">
               	<bean:message  key="ricerca.label.capitoloBil" bundle="acquisizioniLabels" />

		     </td></tr>

		     <tr>
                        <td  width="10%" scope="col" class="etichetta" align="left">
		                   	<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:text styleId="testoNormale" property="bilancio.codBibl" size="3" readonly="true"></html:text>
                        </td>
                        <td scope="col" class="etichetta" align="left">
		                   	<bean:message  key="ordine.label.esercizio" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col"><div align="left">
							<html:text styleId="testoNormale" property="bilancio.esercizio" size="4" readonly="true" ></html:text>
                        </td>
                        <td scope="col" class="etichetta" align="left">
		                   	<bean:message  key="ordine.label.capitolo" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col"><div align="left" >
							<html:text styleId="testoNormale" property="bilancio.capitolo" size="16" maxlength="16" readonly="true"></html:text>
                        </td>

 		     <tr>
		     <tr><td colspan="7" class="etichetta" >
               	<bean:message  key="ricerca.label.budget" bundle="acquisizioniLabels" />
				<html:text  styleId="testoNormale" property="bilancio.budgetDiCapitoloStr" size="15" readonly="${noinput}" ></html:text>
		     </td></tr>
		</table>
	</td></tr>
	<tr><td>
	  	<table  align="center" width="100%" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0">
	     				<td scope="col" style="width:5%;" align="center" rowspan="3">&nbsp;</td>
						<td align="center"  style="width:10%;" title="Codice impegno" scope="col" >
		                   	<bean:message  key="ricerca.label.impCorto" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" style="width:15%;">
		                   	<bean:message  key="ricerca.label.budget" bundle="acquisizioniLabels" />
						</td>
						<td align="center" style="width:20%;" scope="col">
		                   	<bean:message  key="ricerca.label.impegnato" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" style="width:10%;">
		                   	<bean:message  key="ricerca.label.fatturato" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" style="width:10%;">
		                   	<bean:message  key="ricerca.label.pagato" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col">
		                   	<bean:message  key="ricerca.label.disponibilitaCassa" bundle="acquisizioniLabels" />
						</td>
					</tr>
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td align="center" scope="col" colspan="2">&nbsp;</td>
						<td align="center" scope="col" style="width:10%;">
		                   	<bean:message  key="ricerca.label.impegnatoFatt" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" colspan="2">&nbsp;</td>
						<td align="center" scope="col">
		                   	<bean:message  key="ricerca.label.disponibilitaCompet" bundle="acquisizioniLabels" />
						</td>
					</tr>
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td align="center" scope="col" colspan="2">&nbsp;</td>
						<td align="center" scope="col" style="width:10%;">
		                   	<bean:message  key="ricerca.label.acquisito" bundle="acquisizioniLabels" />
						</td>
						<td align="center" scope="col" colspan="2">&nbsp;</td>
						<td align="center" scope="col">
		                   	<bean:message  key="ricerca.label.disponibilitaCompetAcq" bundle="acquisizioniLabels" />
						</td>
					</tr>
			<logic:greaterThan property="numImpegni" name="esaminaBilancioForm" value="0">
				<logic:iterate id="elencaImpegni" property="elencaImpegni" name="esaminaBilancioForm" indexId="indice">
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
							<bs:define id="operazioneValue">
							  <bs:write  name="indice" />
							</bs:define>
							<td  class="testoNormale" align="left" rowspan="3" >
								<html:radio property="radioImpegno" value="${operazioneValue}" ></html:radio>
							</td>
							<!-- controllo se è stato selezionata l'operazione di inserimento riga -->
								<td align="center" >
									<bs:define id="modificabile" value="true"/>
									<c:if test="${elencaImpegni.impegnato > 0 }">
										<bs:define id="modificabile" value="false"/>
									</c:if>
									<html:select  styleClass="testoNormale" style="width:40px;" name="elencaImpegni" indexed="true" property="impegno" disabled="${noinput or not modificabile}">
										<html:optionsCollection  property="listaTipoImpegno" value="codice" label="descrizione" />
									</html:select>
								</td>
							<td align="center" >
								<html:text styleId="testoNormale"  name="elencaImpegni" property="budgetStr" indexed="true"  size="15" readonly="${noinput}" ></html:text>
							</td>
							<td style="text-align: right;" >
								<bs:write    name="elencaImpegni" property="impegnato" format="0.00" />
							</td>
							<td style="text-align: right;" >
								<bs:write    name="elencaImpegni" property="fatturato" format="0.00" />
							</td>
							<td style="text-align: right;" >
								<bs:write    name="elencaImpegni" property="pagato" format="0.00" />
							</td>
							<td style="text-align: right;">
								<bs:write    name="elencaImpegni" property="dispCassa" format="0.00" />
							</td>

						</tr>
						<tr class="testoNormale" bgcolor="${color}">
							<td align="center" scope="col" colspan="2">&nbsp;</td>
							<td scope="col" style="width:10%;text-align: right;">
								<bs:write    name="elencaImpegni" property="impFatt" format="0.00" />
							</td>
							<td align="center" scope="col" colspan="2">&nbsp;</td>
							<td scope="col" style="text-align: right;">
								<bs:write    name="elencaImpegni" property="dispCompetenza" format="0.00" />
							</td>
						</tr>
						<tr class="testoNormale" bgcolor="${color}">
							<td align="center" scope="col" colspan="2">&nbsp;</td>
							<td scope="col" style="width:10%;text-align: right;">
								<bs:write    name="elencaImpegni" property="acquisito" format="0.00" />
							</td>
							<td align="center" scope="col" colspan="2">&nbsp;</td>
							<td scope="col" style="text-align: right;">
								<bs:write    name="elencaImpegni" property="dispCompetenzaAcq" format="0.00" />
							</td>
						</tr>


			</logic:iterate>
		</logic:greaterThan>
        </table>
	</td>
    </tr>
</table>
	     <!-- FINE  tabella corpo -->
  </div>
  <div id="divFooter">
		<c:choose>
			<c:when test="${esaminaBilancioForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>

			<!-- tabella bottoni -->
           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
		<logic:equal name="esaminaBilancioForm" property="enableScorrimento" value="true">
					<html:submit styleClass="pulsanti" property="methodEsaminaBilancio">
						<bean:message key="ricerca.button.scorriIndietro" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodEsaminaBilancio">
						<bean:message key="ricerca.button.scorriAvanti" bundle="acquisizioniLabels" />
					</html:submit>
		</logic:equal>
		<c:choose>
			<c:when test="${!esaminaBilancioForm.disabilitaTutto}">
             <sbn:checkAttivita idControllo="GESTIONE">

					<html:submit styleClass="pulsanti" property="methodEsaminaBilancio">
						<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodEsaminaBilancio">
						<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodEsaminaBilancio">
						<bean:message key="ricerca.button.cancella" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodEsaminaBilancio">
						<bean:message key="ricerca.button.insRiga" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodEsaminaBilancio">
						<bean:message key="ricerca.button.cancRiga" bundle="acquisizioniLabels" />
					</html:submit>
             </sbn:checkAttivita>

				</c:when>
			</c:choose>

		 			<html:submit styleClass="pulsanti" property="methodEsaminaBilancio">
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
