<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/cambi/sinteticaCambi.do" >

  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<c:set var="livelloRicerca" property="livelloRicerca">
		</c:set>

		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaCambi"	 ></sbn:blocchi>

		  <table  width="100%" border="0">
		  		<tr>
                  <td colspan="6">
				  	<table  align="center" width="100%"  style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"  >
				  	<tr  bgcolor="#dde8f0">
						<td class="etichetta" scope="col" align="center" style="width:10%;" >
						</td>
						<td class="etichetta" scope="col" align="center" style="width:10%;" >
 							<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" colspan="2" align="center">
								<bean:message key="ordine.label.valuta" bundle="acquisizioniLabels" /><!--
				 			<html:submit styleClass="bottoneLink" property="methodSinteticaCambi">
							</html:submit>
						--></td>
						<td  class="etichetta" scope="col"  align="center" >
							<bean:message  key="ordine.label.cambio" bundle="acquisizioniLabels" />
						</td>
						<td  scope="col" align="center" style="width:15%;">
								<bean:message key="ricerca.label.dataVariazione" bundle="acquisizioniLabels" />
						<!--
				 			<html:submit styleClass="bottoneLink" property="methodSinteticaCambi">
							</html:submit>
						-->
						</td>
						<!--
						<td  class="etichetta"  scope="col" align="center" style="width:5%;">
						 <bean:message  key="ordine.label.azione" bundle="acquisizioniLabels" />
						</td>
						-->
						<td class="etichetta" scope="col" align="center"></td>
					</tr>

				<c:choose>
					<c:when test="${sinteticaCambiForm.risultatiPresenti}">
						<logic:iterate id="elencaCambi" property="listaCambi" name="sinteticaCambiForm"  indexId="numElemento" >
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
								<sbn:linkbutton index="progressivo" name="elencaCambi"
									value="progressivo"  key="button.ok"
									bundle="acquisizioniLabels" title="esamina" property="progrForm" />
							</td>

								<td align="center">
									<bean-struts:write  name="elencaCambi" property="codBibl"/>
								</td>
								<td align="center" style="width:10%;">
									<bean-struts:write  name="elencaCambi" property="codValuta"/>
								</td>
								<td align="center">
									<bean-struts:write  name="elencaCambi" property="desValuta"/>
								</td>
								<td  style="text-align: right;">
									<bean-struts:write format="0.00########"  name="elencaCambi" property="tassoCambio"/>
								</td>
								<td align="center" >
								<bean-struts:write  name="elencaCambi" property="dataVariazione"/>
								</td>
								<!--
								<td align="center">
								<c:choose>
							        <c:when test='${elencaCambi.tipoVariazione=="M"}'>
										<img border="0"  align="middle" alt="oggetto modificato"  src='<c:url value="/styles/images/pennino.jpg" />'/>
							        </c:when>
							        <c:otherwise>
									<bean-struts:write  name="elencaCambi" property="tipoVariazione"/>
							        </c:otherwise>
							    </c:choose>

								</td>
								-->
								<td>
									<html:multibox property="selectedCambi" >
										<bean-struts:write name="elencaCambi" property="codValuta" />
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

	     <!-- FINE  tabella corpo -->


 </div>
 		<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaCambi" bottom="true" ></sbn:blocchi>
		</div>
 <div id="divFooter">

			<!-- tabella bottoni -->
           <table align="center" border="0" style="height:40px" >
            <tr>
             <td >


				<c:choose>
					<c:when test="${sinteticaCambiForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti"
							property="methodSinteticaCambi">
							<bean:message key="ricerca.button.scegli"
								bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
				<c:otherwise>
					<html:submit styleClass="buttonSelezTuttiR" title="Seleziona tutto" property="methodSinteticaCambi">
						<bean:message key="ricerca.button.selTutti" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="buttonSelezNessunoR" title="Deseleziona tutto" property="methodSinteticaCambi">
						<bean:message key="ricerca.button.deselTutti" bundle="acquisizioniLabels" />
					</html:submit>

	    		</c:otherwise>

				</c:choose>
				<html:submit styleClass="pulsanti" property="methodSinteticaCambi">
					<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
				</html:submit>

	 			<html:submit styleClass="pulsanti" property="methodSinteticaCambi">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
		<c:choose>
			<c:when test="${!sinteticaCambiFor.LSRicerca}">
             <sbn:checkAttivita idControllo="CREA">

				<html:submit styleClass="pulsanti" property="methodSinteticaCambi">
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
