<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/buoniordine/sinteticaBuonoOrdine.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>

		<c:set var="livelloRicerca" property="livelloRicerca">
		</c:set>

		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaBuonoOrdine"	 ></sbn:blocchi>


             <table  width="100%"  align="center" >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">
				  <table  width="100%" >
					 <!--
		 		     <tr><td  class="etichetta" colspan="7"><bean:message key="buono.label.totBuoni" bundle="acquisizioniLabels" />
			 		     <html:text styleId="testoNormale" property="numBuoni" size="3" readonly="true"></html:text>
		 		     </td></tr>
					 -->
					 <tr>
		                  <td >
						  	<table  align="center" width="100%"   style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"  >
						  	<tr class="etichetta" bgcolor="#dde8f0">
						  		<td   scope="col" align="center">
						  		</td>

							<!--
						  		<td  title="Codice Polo" scope="col" align="center">
									<bean:message key="buono.label.tabPolo" bundle="acquisizioniLabels" />
						  		</td>
							-->
								<td  title="Codice Biblioteca" scope="col" align="center">
									<bean:message key="buono.label.tabBibl" bundle="acquisizioniLabels" />
								</td>
								<td scope="col" align="center">
								<!--
									<html:link href="sinteticaBuonoOrdine.do?PROVA=1">
										<bean:message key="buono.label.tabNum" bundle="acquisizioniLabels" />
									</html:link>

								<a title="Ordina per num buono" href="SinteticaBuonoOrdine.htm">
								</a>
								-->
									<bean:message key="buono.label.tabNum" bundle="acquisizioniLabels" />
								</td>
								<td scope="col" align="center">
								<!--
									<a title="Ordina per data" href="SinteticaBuonoOrdine_data.htm">
										<bean:message key="buono.label.dataBuono" bundle="acquisizioniLabels" />
									</a>
								-->
									<bean:message key="buono.label.dataBuono" bundle="acquisizioniLabels" />

								</td>
								<td scope="col" align="center">
									<bean:message key="ordine.label.stato" bundle="acquisizioniLabels" />
								</td>
								<td scope="col" colspan="2" width="20%" align="center">
									<bean:message key="buono.label.tabBilancio" bundle="acquisizioniLabels" />
								</td>
								<td scope="col" colspan="2" width="40%" align="center">
								<!--
									<a title="Ordina per fornitore" href="SinteticaBuonoOrdine_forn.htm">
										<bean:message key="ordine.label.fornitore" bundle="acquisizioniLabels" />
									</a>
								-->
									<bean:message key="ordine.label.fornitore" bundle="acquisizioniLabels" />
								</td>
								<td scope="col" align="center">
								</td>
							</tr>
						<c:choose>
							<c:when test="${navForm.risultatiPresenti}">
							<logic:iterate id="elencaBuoni" property="listaBuoniOrdine"	name="navForm">
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
									<td align="center"><bean-struts:write  name="elencaBuoni" property="codPolo"/></td>
								-->
									<td bgcolor="${color}" >
										<sbn:linkbutton index="progressivo" name="elencaBuoni"
											value="progressivo"  key="button.ok"
											bundle="acquisizioniLabels" title="esamina" property="progrForm" />
									</td>
									<td align="center"><bean-struts:write  name="elencaBuoni" property="codBibl"/></td>
									<td align="center"><bean-struts:write  name="elencaBuoni" property="numBuonoOrdine"/></td>
									<td align="center"><bean-struts:write  name="elencaBuoni" property="dataBuonoOrdine"/></td>
									<td align="center"><bean-struts:write  name="elencaBuoni" property="statoBuonoOrdine"/></td>
									<td align="center"><bean-struts:write  name="elencaBuoni" property="bilancio.codice1"/></td>
									<td align="center"><bean-struts:write  name="elencaBuoni" property="bilancio.codice2"/></td>
									<td align="center"><bean-struts:write  name="elencaBuoni" property="fornitore.codice"/></td>
									<td align="center"><bean-struts:write  name="elencaBuoni" property="fornitore.descrizione"/></td>
									<td>
										<c:choose>
											<c:when test="${navForm.parametri.tipoOperazione eq 'GESTIONE'}">
												<html:multibox property="selectedBuoni">
													<bean-struts:write name="elencaBuoni" property="chiave" />
												</html:multibox>
											</c:when>
											<c:otherwise>
												<html:radio property="selectedBuoni" value="${elencaBuoni.chiave}" />
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</logic:iterate>
							</c:when>
						</c:choose>

							</table>
						  </td>
					  </tr>

		             </table>
			</td>
       	        <!-- FINE tabella corpo COLONNA + LARGA -->
             </tr>
	     </table>
	</div>
 		<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaBuonoOrdine" bottom="true" ></sbn:blocchi>
		</div>
	<div id="divFooter">

			<!-- tabella bottoni -->
           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td>
				<c:choose>
					<c:when test="${navForm.visibilitaIndietroLS}">
					<sbn:checkAttivita idControllo="SCEGLI">
						<html:submit styleClass="pulsanti" property="methodSinteticaBuonoOrdine">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>
					</sbn:checkAttivita>
					</c:when>
					<c:otherwise>
						<html:submit styleClass="buttonSelezTuttiR" title="Seleziona tutto" property="methodSinteticaBuonoOrdine">
							<bean:message key="ricerca.button.selTutti" bundle="acquisizioniLabels" />
						</html:submit>

						<html:submit styleClass="buttonSelezNessunoR" title="Deseleziona tutto" property="methodSinteticaBuonoOrdine" >
							<bean:message key="ricerca.button.deselTutti" bundle="acquisizioniLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti" property="methodSinteticaBuonoOrdine">
							<bean:message key="ricerca.button.stampa" bundle="acquisizioniLabels" />
						</html:submit>

		    		</c:otherwise>

				</c:choose>
				<html:submit styleClass="pulsanti" property="methodSinteticaBuonoOrdine">
					<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
				</html:submit>

	 			<html:submit styleClass="pulsanti" property="methodSinteticaBuonoOrdine">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
				<c:choose>
					<c:when test="${!navForm.LSRicerca}">
		             <sbn:checkAttivita idControllo="CREA">
						<html:submit styleClass="pulsanti" property="methodSinteticaBuonoOrdine">
							<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
						</html:submit>
		             </sbn:checkAttivita>

					</c:when>
				</c:choose>
             </td>
          </tr>
      	  </table>
   	  </div>


	</sbn:navform>
</layout:page>
