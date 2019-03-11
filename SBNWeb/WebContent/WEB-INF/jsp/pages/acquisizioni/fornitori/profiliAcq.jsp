<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/fornitori/profiliAcq.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
			<br>
			   <div class="testoNormale">
					<bean:message  key="ricerca.label.intProfAcq" bundle="acquisizioniLabels" />
			   </div>
			<br>
				  	<table  align="center" width="100%" border="0" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0"><!--
 						<td width="25%" class="etichetta" align="center">
                  			<bean:message  key="ricerca.label.profilo" bundle="acquisizioniLabels" />
 						</td>
                       <td width="60%"class="etichetta" align="center">
                  			<bean:message  key="ricerca.label.descrizione" bundle="acquisizioniLabels" />
                       </td>
                      -->
						<td scope="col" align="center">
							<bean:message key="buono.label.tabBibl" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center">
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

                      <td width="5%">&nbsp;</td>
                    </tr>
		<c:choose>
			<c:when test="${profiliAcqForm.risultatiPresenti}">

				<logic:iterate id="elencaProfili" property="listaProfili"	name="profiliAcqForm">
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
						<td align="center"><bean-struts:write  name="elencaProfili" property="codBibl" /></td>
						<td align="center"><bean-struts:write  name="elencaProfili" property="profilo.codice" /></td>
						<td align="center"><bean-struts:write  name="elencaProfili" property="profilo.descrizione" /></td>
						<td align="center"><bean-struts:write  name="elencaProfili" property="sezione.codice" /></td>
						<td align="center"><bean-struts:write  name="elencaProfili" property="lingua.codice" /></td>
						<td align="center"><bean-struts:write  name="elencaProfili" property="paese.codice" /></td>



						<td>
							<html:multibox property="selectedProfili">
								<bean-struts:write name="elencaProfili" property="profilo.codice" />
							</html:multibox>
						</td>
					</tr>
				</logic:iterate>
			</c:when>
		</c:choose>
             </table>
  </div>
  <div id="divFooter">

	     </table>

	           <table align="center"  border="0" style="height:40px" >
                <tr >
                     <td valign="top" align="center">
					<html:submit styleClass="pulsanti" property="methodProfiliAcq">
						<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
					</html:submit>
		 			<html:submit styleClass="pulsanti" property="methodProfiliAcq">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>
                     </td>
                  </tr>
                </table>
     	  </div>
	</sbn:navform>
</layout:page>
