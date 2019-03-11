<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/fornitori/listaSuppFornitori.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>

		  <table  width="100%" >
		  	<!--
			<tr><td  class="testoNormale" >&nbsp;</td></tr>
 		    -->
    		<tr><td  class="testoNormale" colspan="6">&nbsp;</td></tr>
			 <tr>
                  <td  colspan="6">
				  	<table  align="center" width="100%"  style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">

				  	<tr  bgcolor="#dde8f0">
						<td scope="col" align="center" class="etichetta">
								<bean:message key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" class="etichetta">
								<bean:message key="ricerca.label.cod" bundle="acquisizioniLabels" />
						</td>
						<td scope="col"  align="center" class="etichetta" >
								<bean:message key="ricerca.label.nomeForn" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" class="etichetta">
								<bean:message key="ricerca.label.unitaorg" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" class="etichetta">
								<bean:message key="ricerca.label.indirizzo" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center" class="etichetta"></td>
					</tr>
					<c:choose>
						<c:when test="${listaSuppFornitoriForm.risultatiPresenti}">

							<logic:iterate id="elencaFornitori" property="listaFornitori"  	name="listaSuppFornitoriForm">
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
									<td align="center"><bean-struts:write  name="elencaFornitori" property="tipoPartner"/></td>
									<td align="center"><bean-struts:write  name="elencaFornitori" property="codFornitore"/></td>
									<td align="center"><bean-struts:write  name="elencaFornitori" property="nomeFornitore"/></td>
									<td align="center"><bean-struts:write  name="elencaFornitori" property="unitaOrg"/></td>
									<td align="center"><bean-struts:write  name="elencaFornitori" property="indirizzo"/></td>
									<td>
										<bean-struts:define id="fornScelto" name="elencaFornitori" property="codFornitore"/>
										<html:radio property="selectedFornitori"  value="${fornScelto}" />
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

           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
				<html:submit styleClass="pulsanti" property="methodListaSuppFornitori">
					<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
				</html:submit>

	 			<html:submit styleClass="pulsanti" property="methodListaSuppFornitori">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>

             </td>
          </tr>
      	  </table>
     	  </div>
	</sbn:navform>
</layout:page>
