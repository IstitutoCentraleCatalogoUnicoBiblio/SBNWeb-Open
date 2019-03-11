<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/documenti/sinteticaDoc.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<c:set var="livelloRicerca" property="livelloRicerca">
		</c:set>

		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaDoc"	></sbn:blocchi>

             <table  width="100%"  align="center" >
			 <tr>
                  <td colspan="9">
				  	<table  align="center" width="100%"   style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;" >
				  	<tr class="etichetta"  bgcolor="#dde8f0">
						<td scope="col" title="Tipo documento" align="center">
						</td>
						<td scope="col" title="Tipo documento" align="center">
 							<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" title="Codice documento" align="center">
 							<bean:message  key="ricerca.label.codice" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" title="Stato documento" align="center" >
 							<bean:message  key="buono.label.tabStato" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
 							<bean:message  key="ordine.label.tabTitolo" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
 							<bean:message  key="ordine.label.bid" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" colspan="2" align="center">
 							<bean:message  key="ricerca.label.utente" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" title="Data sugg.to lettore" align="center">
 							<bean:message  key="buono.label.dataBuono" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center"></td>
					</tr>
				<logic:iterate id="elencaDocumenti" property="listaDocumenti"	name="sinteticaDocForm">
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
							<sbn:linkbutton index="progressivo" name="elencaDocumenti"
								value="progressivo"  key="button.ok"
								bundle="acquisizioniLabels" title="esamina" property="progrForm" />
						</td>
						<td align="center"><bean-struts:write  name="elencaDocumenti" property="codBibl"/></td>
						<td align="center"><bean-struts:write  name="elencaDocumenti" property="codDocumento"/></td>
						<td align="center"><bean-struts:write  name="elencaDocumenti" property="statoSuggerimentoDocumento"/></td>
						<td align="center"><bean-struts:write  name="elencaDocumenti" property="titolo.descrizione"/></td>
						<td align="center"><bean-struts:write  name="elencaDocumenti" property="titolo.codice"/></td>
<!--						<bean-struts:write  name="elencaDocumenti" property="utente.codice1"/>-->
						<td align="center"><bean-struts:write  name="elencaDocumenti" property="utente.codice2"/></td>
						<td align="center"><bean-struts:write  name="elencaDocumenti" property="utente.codice3"/></td>
						<td align="center"><bean-struts:write  name="elencaDocumenti" property="dataIns"/></td>
						<td>
							<html:multibox property="selectedDocumenti">
								<bean-struts:write name="elencaDocumenti" property="chiave" />
							</html:multibox>
						</td>
					</tr>
				</logic:iterate>


					</table>
				  </td>
			  </tr>

             </table>
		</div>
 		<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaDoc" bottom="true" ></sbn:blocchi>
		</div>

		<div id="divFooter">

           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td align="center">



				<c:choose>
					<c:when test="${sinteticaDocForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodSinteticaDoc">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>

					</c:when>
				<c:otherwise>
					<html:submit styleClass="buttonSelezTuttiR" title="Seleziona tutto" property="methodSinteticaDoc">
						<bean:message key="ricerca.button.selTutti" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit   styleClass="buttonSelezNessunoR" title="Deseleziona tutto" property="methodSinteticaDoc">
						<bean:message key="ricerca.button.deselTutti" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit styleClass="pulsanti" property="methodSinteticaDoc">
						<bean:message key="ricerca.button.rifiuta" bundle="acquisizioniLabels" />
					</html:submit>

	    		</c:otherwise>

				</c:choose>
				<html:submit styleClass="pulsanti" property="methodSinteticaDoc">
					<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
				</html:submit>
				<!--
				<html:submit styleClass="pulsanti" property="methodSinteticaDoc">
					<bean:message key="ricerca.button.rifiuta" bundle="acquisizioniLabels" />
				</html:submit>
				-->
				<html:submit styleClass="pulsanti" property="methodSinteticaDoc">
					<bean:message key="ricerca.button.stampa" bundle="acquisizioniLabels" />
				</html:submit>

	 			<html:submit styleClass="pulsanti" property="methodSinteticaDoc">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
				<!--
				<html:submit styleClass="pulsanti" property="methodSinteticaDoc">
					<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
				</html:submit>
             	-->

             </td>
          </tr>
      	  </table>
     	  </div>
	</sbn:navform>
</layout:page>
