<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/suggerimenti/sinteticaSugBibl.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<c:set var="livelloRicerca" property="livelloRicerca">
		</c:set>
		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaSugBibl"	 ></sbn:blocchi>

	  		<!-- tabella corpo  -->
             <table  width="100%"  align="center" >
			 <tr>
                  <td colspan="7">
				  	<table  align="center" width="100%"  style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" align="center">
						</td>
						<td scope="col" align="center">
 							<bean:message  key="buono.label.tabBibl" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
 							<bean:message  key="ordine.label.sugg" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" >
 							<bean:message  key="buono.label.tabStato" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
 							<bean:message  key="buono.label.dataBuono" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
 							<bean:message  key="ordine.label.bid" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
 							<bean:message  key="ordine.label.tabTitolo" bundle="acquisizioniLabels" />
						</td><!--
						<td scope="col" colspan="2" align="center">
 							<bean:message  key="ricerca.label.codBibliot" bundle="acquisizioniLabels" />
						</td>
						-->
						<td scope="col"  align="center">
 							<bean:message  key="ricerca.label.bibliotecario" bundle="acquisizioniLabels" />
						</td>

						<td scope="col"  align="center">
 							<bean:message  key="ordine.label.sezione" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center"></td>
					</tr>
				<logic:iterate id="elencaSuggerimenti" property="listaSuggerimenti"	name="sinteticaSugBiblForm">
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
							<sbn:linkbutton index="progressivo" name="elencaSuggerimenti"
								value="progressivo"  key="button.ok"
								bundle="acquisizioniLabels" title="esamina" property="progrForm" />
						</td>
						<td align="center"><bean-struts:write  name="elencaSuggerimenti" property="codBibl"/></td>
						<td align="center"><bean-struts:write  name="elencaSuggerimenti" property="codiceSuggerimento"/></td>
						<td align="center"><bean-struts:write  name="elencaSuggerimenti" property="statoSuggerimento"/></td>
						<td align="center"><bean-struts:write  name="elencaSuggerimenti" property="dataSuggerimento"/></td>
						<td align="center"><bean-struts:write  name="elencaSuggerimenti" property="titolo.codice"/></td>
						<td align="left"><bean-struts:write  name="elencaSuggerimenti" property="titolo.descrizione"/></td>
						<td align="center"><bean-struts:write  name="elencaSuggerimenti" property="nominativoBibliotecario"/></td>
<!--						<td align="center"><bean-struts:write  name="elencaSuggerimenti" property="bibliotecario.descrizione"/></td>-->
						<td align="center"><bean-struts:write  name="elencaSuggerimenti" property="sezione.codice"/></td>
						<td>
							<html:multibox property="selectedSuggerimenti">
								<bean-struts:write name="elencaSuggerimenti" property="chiave" />
							</html:multibox>
						</td>
					</tr>
				</logic:iterate>
			</table>
				  </td>
			  </tr>

             </table>
	     <!-- FINE  tabella corpo -->
 </div>
 		<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaSugBibl" bottom="true"></sbn:blocchi>
		</div>

 <div id="divFooter">

			<!-- tabella bottoni -->
           <table align="center" border="0" style="height:40px" >
            <tr>
             <td >


				<c:choose>
					<c:when test="${sinteticaSugBiblForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodSinteticaSugBibl">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>

					</c:when>
				<c:otherwise>
					<html:submit styleClass="buttonSelezTuttiR" title="Seleziona tutto" property="methodSinteticaSugBibl">
						<bean:message key="ricerca.button.selTutti" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit styleClass="buttonSelezNessunoR" title="Deseleziona tutto" property="methodSinteticaSugBibl">
						<bean:message key="ricerca.button.deselTutti" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit styleClass="pulsanti" property="methodSinteticaSugBibl">
						<bean:message key="ricerca.button.accetta" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit styleClass="pulsanti" property="methodSinteticaSugBibl">
						<bean:message key="ricerca.button.rifiuta" bundle="acquisizioniLabels" />
					</html:submit>


	    		</c:otherwise>
				</c:choose>

				<html:submit styleClass="pulsanti" property="methodSinteticaSugBibl">
					<bean:message key="ricerca.button.stampa" bundle="acquisizioniLabels" />
				</html:submit>

				<html:submit styleClass="pulsanti" property="methodSinteticaSugBibl">
					<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
				</html:submit>

	 			<html:submit styleClass="pulsanti" property="methodSinteticaSugBibl">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
		<c:choose>
			<c:when test="${!sinteticaSugBiblForm.LSRicerca}">
		             <sbn:checkAttivita idControllo="CREA">

				<html:submit styleClass="pulsanti" property="methodSinteticaSugBibl">
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
