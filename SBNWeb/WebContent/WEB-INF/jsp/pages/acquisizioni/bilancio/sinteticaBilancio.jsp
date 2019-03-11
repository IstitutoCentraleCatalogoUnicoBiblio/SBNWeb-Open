<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/bilancio/sinteticaBilancio.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<c:set var="livelloRicerca" property="livelloRicerca">
		</c:set>

		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaBilancio"	></sbn:blocchi>

	  		<!-- tabella corpo  -->
             <table  width="100%"   >
			 <tr>
                  <td>
				  	<table   width="100%"   style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;" >
				  	<tr class="etichetta" bgcolor="#dde8f0">
				  		<td  scope="col" align="center">
				  		</td>
				  		<!--
				  		<td  title="Codice Polo" scope="col" align="center">
							<bean:message key="buono.label.tabPolo" bundle="acquisizioniLabels" />
				  		</td>
						-->
						<td title="Codice Biblioteca" scope="col" align="center">
							<bean:message key="buono.label.tabBibl" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ordine.label.esercizio" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ordine.label.capitolo" bundle="acquisizioniLabels" />
						</td>
						<td scope="col"  align="center">
							<bean:message key="ricerca.label.budget" bundle="acquisizioniLabels" />
						</td>
						<td scope="col"  align="center" width="25%" >
		                   	<bean:message  key="ricerca.label.impCorto" bundle="acquisizioniLabels" />
		                   	-
		                   	<bean:message  key="ricerca.label.budget" bundle="acquisizioniLabels" />
						</td>

						<td scope="col" align="center"></td>
					</tr>

				<c:choose>
					<c:when test="${sinteticaBilancioForm.risultatiPresenti}">

					<logic:iterate id="elencaBilanci" property="listaBilanci"	name="sinteticaBilancioForm">
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
							<!--
							<td align="center"><bean-struts:write  name="elencaBilanci" property="codPolo"/></td>
							-->
							<td bgcolor="${color}" >
								<sbn:linkbutton index="progressivo" name="elencaBilanci"
									value="progressivo"  key="button.ok"
									bundle="acquisizioniLabels" title="esamina" property="progrForm" />
							</td>
							<td align="center"><bean-struts:write  name="elencaBilanci" property="codBibl"/></td>
							<td align="right"><bean-struts:write  name="elencaBilanci" property="esercizio"/></td>
							<td align="right"><bean-struts:write  name="elencaBilanci" property="capitolo"/></td>
							<td style="text-align: right;" >
								<bean-struts:write name="elencaBilanci" property="budgetDiCapitolo" format="0.00" />
							</td>
							<td>

								<html:select  styleClass="testoNormale"  name="sinteticaBilancioForm"  property="selectedImp"  >
									<html:option value="" />
							<bean-struts:define id="operazioneValue">
							  <bean-struts:write  name="elencaBilanci" property="chiave" />
							</bean-struts:define>

									<html:optionsCollection name="elencaBilanci" property="dettagliBilancio" value="salvaImp(${operazioneValue})" label="dettaglioSintetico" />
								</html:select>

							</td>
							<td>
								<html:multibox property="selectedBilanci">
									<bean-struts:write name="elencaBilanci" property="chiave" />
								</html:multibox>
							</td>
						</tr>
					</logic:iterate>
					</c:when>
				</c:choose>



					</table>
				  </td>
			  </tr>

             </table>
       	        <!-- FINE tabella corpo COLONNA + LARGA -->
	     <!-- FINE  tabella corpo -->
</div>
 		<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaBilancio" bottom="true"></sbn:blocchi>
		</div>

<div id="divFooter">
			<!-- tabella bottoni -->
           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
				<c:choose>
					<c:when test="${sinteticaBilancioForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodSinteticaBilancio">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>

					</c:when>
				<c:otherwise>
					<html:submit styleClass="buttonSelezTuttiR" title="Seleziona tutto" property="methodSinteticaBilancio">
						<bean:message key="ricerca.button.selTutti" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit styleClass="buttonSelezNessunoR" title="Deseleziona tutto" property="methodSinteticaBilancio">
						<bean:message key="ricerca.button.deselTutti" bundle="acquisizioniLabels" />
					</html:submit>

	    		</c:otherwise>

				</c:choose>
				<html:submit styleClass="pulsanti" property="methodSinteticaBilancio">
					<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
				</html:submit>

	 			<html:submit styleClass="pulsanti" property="methodSinteticaBilancio">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
				<c:choose>
					<c:when test="${!sinteticaBilancioForm.LSRicerca}">
		             <sbn:checkAttivita idControllo="CREA">
						<html:submit styleClass="pulsanti" property="methodSinteticaBilancio">
							<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
						</html:submit>
		             </sbn:checkAttivita>

					</c:when>
				</c:choose>
             </td>
          </tr>
      	  </table>
	  			<!-- fine tabella bottoni -->
     	  </div>
	</sbn:navform>
</layout:page>
