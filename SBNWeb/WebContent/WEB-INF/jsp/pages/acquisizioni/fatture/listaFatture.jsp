<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/fatture/listaFatture.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
			<br>
			   <div class="testoNormale">
						<logic:notEqual  name="listaFattureForm" property="noteCredito" value="true">
							<bean:message  key="ricerca.label.intFattList" bundle="acquisizioniLabels" />
						</logic:notEqual>
			   </div>
			<br>
		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodListaFatture"	 ></sbn:blocchi>

				  	<table  align="center" width="100%" border="0" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0"><!--
						<td  scope="col" align="center">
						</td>
						--><td  scope="col" align="center">
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
							<bean:message key="buono.label.tabStato" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" div align="center">
							<bean:message key="ordine.label.tabTipo" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" colspan="2" align="center">
							<bean:message key="ordine.label.fornitore" bundle="acquisizioniLabels" />
						</td>
						<td scope="col" align="center"></td>
						<logic:notEqual  name="listaFattureForm" property="noteCredito" value="true">
							<td scope="col" align="center"></td>
						</logic:notEqual>

					</tr>
			<c:choose>
				<c:when test="${listaFattureForm.risultatiPresenti}">

				<logic:iterate id="elencaFatture" property="listaFatture"	name="listaFattureForm">
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
					<!--
						<td bgcolor="${color}" >
							<sbn:linkbutton index="progressivo" name="elencaFatture"
								value="progressivo"  key="button.ok"
								bundle="acquisizioniLabels" title="esamina" property="progrForm" />
						</td>
					-->
						<td align="center"><bean-struts:write  name="elencaFatture" property="codBibl"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="annoFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="progrFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="numFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="dataFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="dataRegFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="importoFattura" format="0.00" /></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="valutaFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="statoFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="tipoFattura"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="fornitoreFattura.codice"/></td>
						<td align="center"><bean-struts:write  name="elencaFatture" property="fornitoreFattura.descrizione"/></td>
						<logic:notEqual  name="listaFattureForm" property="noteCredito" value="true">
							<td>
								<bean-struts:define id="operazioneValue">
								  <bean-struts:write name="elencaFatture" property="chiave" />
								</bean-struts:define>
							<html:radio property="radioSel" value="${operazioneValue}"></html:radio>
							</td>
						</logic:notEqual>

					</tr>
				</logic:iterate>
				</c:when>
			</c:choose>

			</table>
		  </div>
<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodListaFatture" bottom="true" ></sbn:blocchi>
		</div>
 <div id="divFooter">

  <div id="divFooter">

	           <table align="center"  border="0" style="height:40px" >
                <tr >
                     <td valign="top" align="center">
					<logic:notEqual  name="listaFattureForm" property="noteCredito" value="true">
						<html:submit styleClass="pulsanti" property="methodListaFatture">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>
					</logic:notEqual>

					<logic:notEqual  name="listaFattureForm" property="noteCredito" value="true">
						<html:submit styleClass="pulsanti" property="methodListaFatture">
							<bean:message key="ricerca.button.esamina" bundle="acquisizioniLabels" />
						</html:submit>
					</logic:notEqual>

		 			<html:submit styleClass="pulsanti" property="methodListaFatture">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>
                     </td>
                  </tr>
                </table>
     	  </div>
	</sbn:navform>
</layout:page>
