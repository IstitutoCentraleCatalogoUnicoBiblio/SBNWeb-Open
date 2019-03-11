<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/fornitori/sinteticaFornitori.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<c:set var="livelloRicerca" property="livelloRicerca" value="">
		</c:set>

			<c:choose>
         		<c:when test="${fornitoriRicercaParzialeForm.creazLegameTitEdit eq 'SI'}">
					<table border="0">
						<tr>
							<td class="etichetta"><bean:message key="ricerca.titoloRiferimento"
								bundle="gestioneBibliograficaLabels" />:</td>
							<td width="20" class="testoNormale"><html:text
								property="bid" size="10" readonly="true"
								></html:text></td>
							<td width="150" class="etichetta"><html:text
								property="descr" size="50" readonly="true"
								></html:text></td>
						</tr>
					</table>
				</c:when>
			</c:choose>


		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaFornitori"	 ></sbn:blocchi>

			  	<table  align="center" width="100%"  style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
		    		<tr><td  class="testoNormale" colspan="6">&nbsp;</td></tr>
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" align="center">	</td>
						<td scope="col" align="center">	</td>
						<td scope="col" align="center" style="width:1%;"></td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.cod" bundle="acquisizioniLabels" />
						</td>
						<td scope="col"  align="center" >
							<bean:message key="ricerca.label.nomeForn" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.unitaorg" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.indirizzo" bundle="acquisizioniLabels" />
						</td>
						<c:choose>
         					<c:when test="${sinteticaFornitoriForm.editore eq 'SI'}">
								<td scope="col" align="center">
									<bean:message key="ricerca.label.isbnEditore" bundle="acquisizioniLabels" />
								</td>
							</c:when>
						</c:choose>
						<td scope="col" align="center"></td>
					</tr>
				<c:choose>
					<c:when test="${sinteticaFornitoriForm.risultatiPresenti}">
					<logic:iterate id="elencaFornitori" property="listaFornitori" 	name="sinteticaFornitoriForm">
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
								<sbn:linkbutton index="progressivo" name="elencaFornitori"
									value="progressivo"  key="button.ok"
									bundle="acquisizioniLabels" title="esamina" property="progrForm" />
							</td>


							<c:choose>
						        <c:when test='${elencaFornitori.regione eq ""}'>
									<td align="center">&nbsp;</td>
						        </c:when>
						        <c:otherwise>
									<td align="center" title="editore">Editore</td>
						        </c:otherwise>
						    </c:choose>


							<c:choose>
						        <c:when test='${elencaFornitori.fornitoreBibl==null}'>
									<td align="center">&nbsp;</td>
						        </c:when>
						        <c:otherwise>
									<td align="center" title="Fornitore della Biblioteca">*</td>
						        </c:otherwise>
						    </c:choose>

							<td align="center"><bean-struts:write  name="elencaFornitori" property="tipoPartner"/></td>
							<td align="center"><bean-struts:write  name="elencaFornitori" property="codFornitore"/></td>
							<td align="center"><bean-struts:write  name="elencaFornitori" property="nomeFornitore"/></td>
							<td align="center"><bean-struts:write  name="elencaFornitori" property="unitaOrg"/></td>
							<td align="center"><bean-struts:write  name="elencaFornitori" property="indirizzoComposto"/></td>
							<c:choose>
         						<c:when test="${sinteticaFornitoriForm.editore eq 'SI'}">
									<td align="center"><bean-struts:write  name="elencaFornitori" property="isbnEditore"/></td>
								</c:when>
							</c:choose>
							<td>
								<html:multibox property="selectedFornitori">
									<bean-struts:write name="elencaFornitori" property="codFornitore" />
								</html:multibox>
							</td>
						</tr>
					</logic:iterate>
					</c:when>
				</c:choose>
				</table>

 </div>
 		<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaFornitori" bottom="true"></sbn:blocchi>
		</div>

 <div id="divFooter">
           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >

				<c:choose>
         			<c:when test="${sinteticaFornitoriForm.cartiglioEditore eq 'SI'}">
         				<html:submit styleClass="pulsanti" property="methodSinteticaFornitori">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>
		 				<html:submit styleClass="pulsanti" property="methodSinteticaFornitori">
							<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
						</html:submit>
         			</c:when>
					<c:otherwise>
					<c:choose>
					<c:when test="${sinteticaFornitoriForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodSinteticaFornitori">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
				<c:otherwise>
					<html:submit styleClass="buttonSelezTuttiR" title="Seleziona tutto" property="methodSinteticaFornitori">
						<bean:message key="ricerca.button.selTutti" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit styleClass="buttonSelezNessunoR" title="Deseleziona tutto" property="methodSinteticaFornitori">
						<bean:message key="ricerca.button.deselTutti" bundle="acquisizioniLabels" />
					</html:submit>


	    		</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${sinteticaFornitoriForm.cercaInPolo}">
						<html:submit styleClass="pulsanti" property="methodSinteticaFornitori">
							<bean:message key="ricerca.button.cercaInPolo" bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
				</c:choose>
				<html:submit styleClass="pulsanti" property="methodSinteticaFornitori">
					<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
				</html:submit>

	 			<html:submit styleClass="pulsanti" property="methodSinteticaFornitori">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>

				<html:submit styleClass="pulsanti" property="methodSinteticaFornitori">
					<bean:message key="ricerca.button.stampa" bundle="acquisizioniLabels" />
				</html:submit>

				<c:choose>
					<c:when test="${!sinteticaFornitoriForm.LSRicerca}">
				             <sbn:checkAttivita idControllo="CREA">

						<html:submit styleClass="pulsanti" property="methodSinteticaFornitori">
							<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
						</html:submit>
				             </sbn:checkAttivita>

					</c:when>
				</c:choose>

          		<c:choose>
         			<c:when test="${sinteticaFornitoriForm.editore eq 'SI'}">
						<html:submit styleClass="pulsanti" property="methodSinteticaFornitori">
							<bean:message key="ricerca.button.titCollEditore" bundle="acquisizioniLabels" />
						</html:submit>
		          		<c:choose>
		         			<c:when test="${sinteticaFornitoriForm.creazLegameTitEdit eq 'SI'}">
								<html:submit styleClass="pulsanti" property="methodSinteticaFornitori">
									<bean:message key="ricerca.button.creazLegameTitEdit" bundle="acquisizioniLabels" />
								</html:submit>
							</c:when>
						</c:choose>
					</c:when>
				</c:choose>
					</c:otherwise>
				</c:choose>



             </td>
          </tr>
      	  </table>
 	  </div>
	</sbn:navform>
</layout:page>
