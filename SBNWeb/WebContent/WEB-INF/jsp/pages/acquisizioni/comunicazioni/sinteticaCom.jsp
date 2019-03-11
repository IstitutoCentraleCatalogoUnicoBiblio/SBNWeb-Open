<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/comunicazioni/sinteticaCom.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<c:set var="livelloRicerca" property="livelloRicerca">
		</c:set>

		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaCom"	 ></sbn:blocchi>

		  <table  width="100%" border="0">
			 <tr>
                  <td colspan="10">
				  	<table  align="center" width="100%"  style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0">

						<td scope="col" align="center">
						</td>
						<td scope="col" align="center">
 							<bean:message  key="ricerca.label.tMsg" bundle="acquisizioniLabels" />
						</td>
						<!--
						<td scope="col" align="center">
 							<bean:message  key="ricerca.label.codice" bundle="acquisizioniLabels" />
						</td>
						-->
						<td scope="col" align="center"></td>
						<td scope="col" colspan="2" align="center">
 							<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
 							<bean:message  key="ricerca.label.oggetto" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" colspan="3" align="center">
 							<bean:message  key="ricerca.label.nrDocumento" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
 							<bean:message  key="buono.label.statoBuono" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
 							<bean:message  key="ordine.label.data" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center"></td>
					</tr>
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>
<!--						<td scope="col" align="center">&nbsp;</td>-->
						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">
 							<bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
 							<bean:message  key="buono.label.anno" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
 							<bean:message  key="ricerca.label.codice" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>

					</tr>
				<c:choose>
					<c:when test="${sinteticaComForm.risultatiPresenti}">

				<logic:iterate id="elencaComunicazioni" property="listaComunicazioni"	name="sinteticaComForm">
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
							<sbn:linkbutton index="progressivo" name="elencaComunicazioni"
								value="progressivo"  key="button.ok"
								bundle="acquisizioniLabels" title="esamina" property="progrForm" />
						</td>
						<td align="center"><bean-struts:write  name="elencaComunicazioni" property="desMessaggio"/>
						</td>
<!--						<td align="center"><bean-struts:write  name="elencaComunicazioni" property="codiceMessaggio"/></td>-->
						<td align="center"><bean-struts:write  name="elencaComunicazioni" property="direzioneComunicazioneLabel"/></td>
						<td align="center"><bean-struts:write  name="elencaComunicazioni" property="fornitore.codice"/></td>
						<td align="center"><bean-struts:write  name="elencaComunicazioni" property="fornitore.descrizione"/></td>
						<td align="center"><bean-struts:write  name="elencaComunicazioni" property="tipoDocumento"/>
						</td>
						<td align="center"><bean-struts:write  name="elencaComunicazioni" property="idDocumento.codice1"/></td>
						<td align="center"><bean-struts:write  name="elencaComunicazioni" property="idDocumento.codice2"/></td>
						<td align="center"><bean-struts:write  name="elencaComunicazioni" property="idDocumento.codice3"/></td>
						<td align="center"><bean-struts:write  name="elencaComunicazioni" property="desStato"/></td>
						<td align="center"><bean-struts:write  name="elencaComunicazioni" property="dataComunicazione"/></td>

						<td>
							<html:multibox property="selectedComunicazioni">
								<bean-struts:write name="elencaComunicazioni" property="chiave" />
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
		</div>
 		<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaCambi" bottom="true"></sbn:blocchi>
		</div>
 <div id="divFooter">

		<div id="divFooter">

           <table align="center"   style="height:40px" border="0">
            <tr>
             <td  >


				<c:choose>
					<c:when test="${sinteticaComForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodSinteticaCom">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
				<c:otherwise>
					<html:submit styleClass="buttonSelezTuttiR" title="Seleziona tutto" property="methodSinteticaCom">
						<bean:message key="ricerca.button.selTutti" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit styleClass="buttonSelezNessunoR" title="Deseleziona tutto" property="methodSinteticaCom">
						<bean:message key="ricerca.button.deselTutti" bundle="acquisizioniLabels" />
					</html:submit>

	    		</c:otherwise>

				</c:choose>
				<html:submit styleClass="pulsanti" property="methodSinteticaCom">
					<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
				</html:submit>

	 			<html:submit styleClass="pulsanti" property="methodSinteticaCom">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
		<c:choose>
			<c:when test="${!sinteticaComForm.LSRicerca}">
	             <sbn:checkAttivita idControllo="CREA">
					<sbn:checkAttivita idControllo="SIF_DA_PERIODICO" inverted="true" >
						<html:submit styleClass="pulsanti" property="methodSinteticaCom">
							<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
						</html:submit>
	            	</sbn:checkAttivita>
	            </sbn:checkAttivita>

			</c:when>
		</c:choose>
         </td>

          </tr>
      	  </table>
     	  </div>
	</sbn:navform>
</layout:page>
