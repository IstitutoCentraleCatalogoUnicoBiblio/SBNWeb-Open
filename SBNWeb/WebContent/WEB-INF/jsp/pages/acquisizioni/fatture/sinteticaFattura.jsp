<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/fatture/sinteticaFattura.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<c:set var="livelloRicerca" property="livelloRicerca">
		</c:set>

		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaFattura"	 ></sbn:blocchi>

        <table  width="100%"  align="center" >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">

		  <table  width="100%" >
    		<tr><td  class="testoNormale" colspan="7">&nbsp;</td></tr>
			 <tr>
                  <td colspan="7">
				  	<table  align="center" width="100%" border="0" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td  scope="col" align="center">
						</td>
						<td  scope="col" align="center">
							<bean:message key="buono.label.tabBibl" bundle="acquisizioniLabels" />
						</td>
						<td  scope="col" align="center">
							<bean:message key="buono.label.anno" bundle="acquisizioniLabels" />
						</td>
						<td  scope="col" align="center">
							<bean:message key="ricerca.label.progr" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.numFatt" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.dataFattShort" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.dataRegShort" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.importoFatt" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.valutaFatt" bundle="acquisizioniLabels" />
						</td>
						<td  scope="col" align="center">
							<bean:message key="ordine.label.stato" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" div align="center">
							<bean:message key="ordine.label.tabTipo" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" colspan="2" align="center">
							<bean:message key="ordine.label.fornitore" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center"></td>

						<td scope="col" align="center"></td>

					</tr>
			<c:choose>
				<c:when test="${sinteticaFatturaForm.risultatiPresenti}">

				<logic:iterate id="elencaFatture" property="listaFatture"	name="sinteticaFatturaForm">
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
							<sbn:linkbutton index="progressivo" name="elencaFatture"
								value="progressivo"  key="button.ok"
								bundle="acquisizioniLabels" title="esamina" property="progrForm" />
						</td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="codBibl"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="annoFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="progrFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="numFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="dataFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="dataRegFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="importoFattura" format="0.00" /></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="valutaFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="denoStatoFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="tipoFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="fornitoreFattura.codice"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="fornitoreFattura.descrizione"/></td>

						<td>
							<html:multibox property="selectedFatture">
								<bean-struts:write name="elencaFatture" property="chiave" />
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
		  </td>
		  </tr>

            </table>
	</div>
 		<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaFattura" bottom="true"></sbn:blocchi>
		</div>

	<div id="divFooter">

           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
             	<!--
				<html:submit styleClass="pulsanti" property="methodSinteticaFattura">
					<bean:message key="ricerca.button.controllaordine" bundle="acquisizioniLabels" />
				</html:submit>
				-->


				<c:choose>
					<c:when test="${sinteticaFatturaForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodSinteticaFattura">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>

					</c:when>
				<c:otherwise>
					<html:submit styleClass="buttonSelezTuttiR" title="Seleziona tutto" property="methodSinteticaFattura">
						<bean:message key="ricerca.button.selTutti" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit styleClass="buttonSelezNessunoR" title="Deseleziona tutto" property="methodSinteticaFattura">
						<bean:message key="ricerca.button.deselTutti" bundle="acquisizioniLabels" />
					</html:submit>

	    		</c:otherwise>
				</c:choose>

				<c:choose>
					<c:when test="${sinteticaFatturaForm.abilitaEsamina}">
						<html:submit styleClass="pulsanti" property="methodSinteticaFattura">
							<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
				</c:choose>


	 			<html:submit styleClass="pulsanti" property="methodSinteticaFattura">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
		<c:choose>
			<c:when test="${!sinteticaFatturaForm.LSRicerca}">
		             <sbn:checkAttivita idControllo="CREA">

				<html:submit styleClass="pulsanti" property="methodSinteticaFattura">
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
