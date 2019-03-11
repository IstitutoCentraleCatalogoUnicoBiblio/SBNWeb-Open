<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/gare/sinteticaGara.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<c:set var="livelloRicerca" property="livelloRicerca">
		</c:set>

	<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaGara" ></sbn:blocchi>
  <table  width="100%"   >

              <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%" >
			<table  width="100%" >
                    <tr>
                      <td colspan="7" >
                        <table  align="center" width="100%" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"    >
                          <tr class="etichetta" bgcolor="#dde8f0" >
                            <td scope="col" width="5%" align="center">
                            </td>
                            <td scope="col" width="5%" align="center">
								<bean:message key="ricerca.label.codice" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col" width="12%" align="center">
								<bean:message key="buono.label.dataBuono" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col" width="8%" align="center">
								<bean:message key="ordine.label.prezzo" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col" colspan="1" width="6%" align="center">
								<bean:message key="ricerca.label.copie" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col" colspan="1" width="6%" align="center">
								<bean:message key="buono.label.statoBuono" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col" width="11%" align="center">
								<bean:message key="ordine.label.bid" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col" width="43%" align="center">
								<bean:message key="ordine.label.tabTitolo" bundle="acquisizioniLabels" />
                            </td>
                            <td scope="col" width="5%" align="center">
                            </td>
                          </tr>
					<logic:iterate id="elencaGare" property="listaRichiesteOfferta"	name="sinteticaGaraForm">
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
							<td bgcolor="${color}" >
								<sbn:linkbutton index="progressivo" name="elencaGare"
									value="progressivo"  key="button.ok"
									bundle="acquisizioniLabels" title="esamina" property="progrForm" />
							</td>
							<td align="center"><bean-struts:write  name="elencaGare" property="codRicOfferta"/></td>
							<td align="center"><bean-struts:write  name="elencaGare" property="dataRicOfferta"/></td>
							<td style="text-align: right;"><bean-struts:write format="0.00" name="elencaGare" property="prezzoIndGara"/></td>
							<td align="center"><bean-struts:write  name="elencaGare" property="numCopieRicAcq"/></td>
							<%--<td align="center"><bean-struts:write  name="elencaGare" property="statoRicOfferta"/></td> --%>
							<td align="center"><bean-struts:write  name="elencaGare" property="desStatoRicOfferta"/></td>
							<td align="center"><bean-struts:write  name="elencaGare" property="bid.codice"/></td>
							<td align="left"><bean-struts:write  name="elencaGare" property="bid.descrizione"/></td>

							<td>
								<html:multibox property="selectedRichieste">
									<bean-struts:write name="elencaGare" property="chiave" />
								</html:multibox>
							</td>
						</tr>
					</logic:iterate>

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
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaGara" bottom="true"></sbn:blocchi>
		</div>

 <div id="divFooter">


	<table align="center"   border="0" style="height:40px"  >
              <tr>
             <td align="center">


				<c:choose>
					<c:when test="${sinteticaGaraForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodSinteticaGara">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>

					</c:when>
				<c:otherwise>
					<html:submit styleClass="buttonSelezTuttiR" title="Seleziona tutto" property="methodSinteticaGara">
						<bean:message key="ricerca.button.selTutti" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit styleClass="buttonSelezNessunoR" title="Deseleziona tutto" property="methodSinteticaGara">
						<bean:message key="ricerca.button.deselTutti" bundle="acquisizioniLabels" />
					</html:submit>


	    		</c:otherwise>

				</c:choose>
				<html:submit styleClass="pulsanti" property="methodSinteticaGara">
					<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
				</html:submit>

	 			<html:submit styleClass="pulsanti" property="methodSinteticaGara">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
		<c:choose>
			<c:when test="${!sinteticaGaraForm.LSRicerca}">
		             <sbn:checkAttivita idControllo="CREA">

				<html:submit styleClass="pulsanti" property="methodSinteticaGara">
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
