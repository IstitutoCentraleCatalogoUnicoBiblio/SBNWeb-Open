<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/profiliacquisto/sinteticaProfili.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
		<c:set var="livelloRicerca" property="livelloRicerca">
		</c:set>

		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaProfili"	 ></sbn:blocchi>

		  <table  width="100%" >
			 <tr>
                  <td >
			  	<table  align="center" width="100%"   style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"  >
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td scope="col" align="center">
						</td>
						<td scope="col" align="center">
							<bean:message key="buono.label.tabBibl" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
<!--						<a title="Ordina per Profilo d'acquisto" href="#">
						<a title="Ordina per Cod. Sezione" href="SinteticaProfili.htm">
-->
							<bean:message key="ricerca.label.codProfilo" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" >
							<bean:message key="ricerca.label.descrizione" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.codSezione" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.lingua" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
							<bean:message key="ricerca.label.paese" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center"></td>
					</tr>
			<c:choose>
				<c:when test="${sinteticaProfiliForm.risultatiPresenti}">

				<logic:iterate id="elencaProfili" property="listaProfili"	name="sinteticaProfiliForm">
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
							<sbn:linkbutton index="progressivo" name="elencaProfili"
								value="progressivo"  key="button.ok"
								bundle="acquisizioniLabels" title="esamina" property="progrForm" />
						</td>
						<td align="center"><bean-struts:write  name="elencaProfili" property="codBibl" /></td>
						<td align="center"><bean-struts:write  name="elencaProfili" property="profilo.codice" /></td>
						<td align="center"><bean-struts:write  name="elencaProfili" property="profilo.descrizione" /></td>
						<td align="center"><bean-struts:write  name="elencaProfili" property="sezione.codice" /></td>
						<td align="center"><bean-struts:write  name="elencaProfili" property="lingua.codice" /></td>
						<td align="center"><bean-struts:write  name="elencaProfili" property="paese.codice" /></td>
						<td>
							<html:multibox property="selectedProfili">
								<bean-struts:write name="elencaProfili" property="chiave" />
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


			<!-- tabella bottoni -->
    </div>
	<div id="divFooter">
           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
				<c:choose>
					<c:when test="${sinteticaProfiliForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodSinteticaProfili">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>

					</c:when>
				<c:otherwise>
					<html:submit styleClass="buttonSelezTuttiR" title="Seleziona tutto" property="methodSinteticaProfili">
						<bean:message key="ricerca.button.selTutti" bundle="acquisizioniLabels" />
					</html:submit>

					<html:submit styleClass="buttonSelezNessunoR" title="Deseleziona tutto" property="methodSinteticaProfili">
						<bean:message key="ricerca.button.deselTutti" bundle="acquisizioniLabels" />
					</html:submit>


	    		</c:otherwise>

				</c:choose>
				<html:submit styleClass="pulsanti" property="methodSinteticaProfili">
					<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
				</html:submit>

	 			<html:submit styleClass="pulsanti" property="methodSinteticaProfili">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
		<c:choose>
			<c:when test="${!sinteticaProfiliForm.LSRicerca}">
		             <sbn:checkAttivita idControllo="CREA">

				<html:submit styleClass="pulsanti" property="methodSinteticaProfili">
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
