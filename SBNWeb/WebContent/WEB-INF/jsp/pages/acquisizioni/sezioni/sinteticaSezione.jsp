<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/sezioni/sinteticaSezione.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<c:set var="livelloRicerca" property="livelloRicerca">
		</c:set>

		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaSezione"	 ></sbn:blocchi>

	  		<!-- tabella corpo  -->

             <table  width="100%"   >
			 <tr >
                  <td colspan="7">
				  	<table  align="center" width="100%"  style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" style="width:30px;" align="center">
						</td>
						<td scope="col" style="width:30px;" align="center">
							<bean:message key="ricerca.label.codBibl" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.codSez" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.nomeSezione" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" >
							<bean:message key="ricerca.label.sommaDisp" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center"></td>
					</tr>

				<c:choose>
					<c:when test="${sinteticaSezioneForm.risultatiPresenti}">

					<logic:iterate id="elencaSezioni" property="listaSezioni"	name="sinteticaSezioneForm">
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
								<sbn:linkbutton index="progressivo" name="elencaSezioni"
									value="progressivo"  key="button.ok"
									bundle="acquisizioniLabels" title="esamina" property="progrForm" />
							</td>
							<td align="center"><bean-struts:write  name="elencaSezioni" property="codBibl" /></td>
							<td align="center"><bean-struts:write  name="elencaSezioni" property="codiceSezione" /></td>
							<td align="center"><bean-struts:write  name="elencaSezioni" property="descrizioneSezione" /></td>
							<td style="text-align: right;"><bean-struts:write format="0.00" name="elencaSezioni" property="sommaDispSezione" /></td>
							<td>
								<html:multibox property="selectedSezioni">
									<bean-struts:write name="elencaSezioni" property="chiave" />
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
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaSezione" bottom="true"></sbn:blocchi>
		</div>

		<div id="divFooter">
			<!-- tabella bottoni -->
           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td>
				<c:choose>
					<c:when test="${sinteticaSezioneForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodSinteticaSezione">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>

				<c:otherwise>
						<html:submit styleClass="buttonSelezTuttiR" title="Seleziona tutto" property="methodSinteticaSezione">
							<bean:message key="ricerca.button.selTutti" bundle="acquisizioniLabels" />
						</html:submit>

						<html:submit styleClass="buttonSelezNessunoR" title="Deseleziona tutto" property="methodSinteticaSezione">
							<bean:message key="ricerca.button.deselTutti" bundle="acquisizioniLabels" />
						</html:submit>

	    		</c:otherwise>
				</c:choose>

				<html:submit styleClass="pulsanti" property="methodSinteticaSezione">
					<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
				</html:submit>

	 			<html:submit styleClass="pulsanti" property="methodSinteticaSezione">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
		<c:choose>
			<c:when test="${!sinteticaSezioneForm.LSRicerca}">
		             <sbn:checkAttivita idControllo="CREA">

				<html:submit styleClass="pulsanti" property="methodSinteticaSezione">
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
