<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/sezioni/listaSezioni.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
			<br>
			   <div class="testoNormale">
					<bean:message  key="ricerca.label.intSezList" bundle="acquisizioniLabels" />
			   </div>
			<br>
		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodListaSezioni"	 ></sbn:blocchi>

				  	<table  align="center" width="100%" border="0" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0">
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
                      <td width="5%">&nbsp;</td>
                    </tr>
		<c:choose>
			<c:when test="${listaSezioniForm.risultatiPresenti}">

				<logic:iterate id="elencaSezioni" property="listaSezioni"	name="listaSezioniForm">
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

						<td align="center"><bean-struts:write  name="elencaSezioni" property="codBibl" /></td>
						<td align="center"><bean-struts:write  name="elencaSezioni" property="codiceSezione" /></td>
						<td align="center"><bean-struts:write  name="elencaSezioni" property="descrizioneSezione" /></td>
						<td style="text-align: right;"><bean-struts:write format="0.00" name="elencaSezioni" property="sommaDispSezione" /></td>
						<td><!--
							<html:multibox property="selectedSezioni">
								<bean-struts:write name="elencaSezioni" property="codiceSezione" />
							</html:multibox>
						-->
							<bean-struts:define id="operazioneValue">
							  <bean-struts:write name="elencaSezioni" property="codiceSezione" />
							</bean-struts:define>

						<html:radio property="radioSel" value="${operazioneValue}"></html:radio>
						</td>
					</tr>
				</logic:iterate>
			</c:when>
		</c:choose>
             </table>
  </div>
<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodListaSezioni" bottom="true" ></sbn:blocchi>
		</div>
 <div id="divFooter">

  <div id="divFooter">

	           <table align="center"  border="0" style="height:40px" >
                <tr >
                     <td valign="top" align="center">
					<html:submit styleClass="pulsanti" property="methodListaSezioni">
						<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
					</html:submit>
		 			<html:submit styleClass="pulsanti" property="methodListaSezioni">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>
                     </td>
                  </tr>
                </table>
     	  </div>
	</sbn:navform>
</layout:page>
