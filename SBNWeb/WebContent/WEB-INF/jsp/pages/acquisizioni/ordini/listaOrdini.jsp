<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/ordini/listaOrdini.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
			<br>
			   <div class="testoNormale">
					<bean:message  key="ricerca.label.intOrdList" bundle="acquisizioniLabels" />
			   </div>
			<br>
		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodListaOrdini"	 ></sbn:blocchi>
		<c:choose>
			<c:when test="${navForm.provInterroga}">
						<table border="0">
							<tr>
								<td  class="etichetta"><bean:message key="ricerca.gestionali.codBib" bundle="gestioneBibliograficaLabels" />
<!--								<html:text styleId="testoNormale" property="codBiblioSelez" size="3" ></html:text>-->
								<html:select
									property="codBiblioSelez">
									<html:optionsCollection property="listaCodBib" value="codice" label="codice" />
								</html:select>


								<td align="center"><html:submit property="methodListaOrdini">
									<bean:message key="button.gestLocal.filtra"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>

							</tr>
						</table>
			</c:when>
		</c:choose>

				  	<table  align="center" width="100%" border="0" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td class="etichetta"  scope="col" align="center">
							<bean:message key="buono.label.tabBibl" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta" style="width:5%;"  scope="col" align="center">
							<bean:message key="buono.label.anno" bundle="acquisizioniLabels" />
						</td>

						<td class="etichetta" style="width:5%;"  scope="col" align="center">
							<bean:message key="ordine.label.tabTipo" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta"  scope="col" style="width:3%;" align="center">
							<bean:message key="ordine.label.tabNum" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta"  scope="col" align="center">
								<bean:message key="buono.label.dataBuono" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta" style="width:5%;"  scope="col" align="center">
								<bean:message key="buono.label.tabStato" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta" style="width:15%;" scope="col" align="center">
							<bean:message key="ordine.label.bid" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta" style="width:30%;" scope="col" align="center">
							<bean:message key="ordine.label.tabTitolo" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta" title="Natura" scope="col" align="center">
							<bean:message key="ordine.label.tabNatura" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta"  scope="col" align="center">
							<bean:message key="ordine.label.tabContinuativo" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta" title="Rinnovato" scope="col" align="center">
							<bean:message key="ordine.label.tabRinnovato" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta" colspan="2" style="width:30%;"  scope="col" align="center">
								<bean:message key="ordine.label.fornitore" bundle="acquisizioniLabels" />
						</td>
						<td class="etichetta"  scope="col" align="center" colspan="3">
								<bean:message key="buono.label.tabBilancio" bundle="acquisizioniLabels" />
						</td>
                        <td width="5%">&nbsp;</td>
                    </tr>
		<c:choose>
			<c:when test="${navForm.risultatiPresenti}">

				<logic:iterate id="item" property="listaOrdini"	name="navForm">
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

						<td align="center"><bs:write  name="item" property="codBibl"/></td>
						<td align="center"><bs:write  name="item" property="annoOrdine"/></td>
						<td align="center"><bs:write  name="item" property="tipoOrdine"/></td>
						<td align="center"><bs:write  name="item" property="codOrdine"/></td>
						<td align="center"><bs:write  name="item" property="dataOrdine"/></td>
						<td align="center"><bs:write  name="item" property="statoOrdine"/></td>
						<td align="center" style="width:5%;"><bs:write  name="item" property="titolo.codice"/></td>
						<td align="center"><bs:write  name="item" property="titolo.descrizione"/></td>
						<td align="center"><bs:write  name="item" property="naturaOrdine"/></td>
						<td  scope="col"><html:checkbox name="item" property="continuativo" disabled="true"></html:checkbox></td>
						<td  scope="col"><html:checkbox name="item" property="rinnovato" disabled="true"></html:checkbox></td>
						<td align="center" style="width:5%;"><bs:write  name="item" property="fornitore.codice"/></td>
						<td align="center"><bs:write  name="item" property="fornitore.descrizione"/></td>
						<td align="center"><bs:write  name="item" property="bilancio.codice1"/></td>
						<td align="center"><bs:write  name="item" property="bilancio.codice2"/></td>
						<td align="center"><bs:write  name="item" property="bilancio.codice3"/></td>
						<td>
							<bs:define id="operazioneValue">
							  <bs:write name="item" property="chiave" />
							</bs:define>
						<html:radio property="radioSel" value="${operazioneValue}"></html:radio>
						</td>
					</tr>
				</logic:iterate>
			</c:when>
		</c:choose>
             </table>
  </div>
<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodListaOrdini" bottom="true" ></sbn:blocchi>
		</div>
 <div id="divFooter">

  <div id="divFooter">
         <table align="center"  border="0" style="height:40px" >
             <tr >
               <td valign="top" align="center">
				<c:choose>
						<c:when test="${!navForm.provBo}">
						<logic:notEqual  name="navForm" property="provInterroga" value="true">
						<html:submit styleClass="pulsanti" property="methodListaOrdini">
							<bean:message key="ricerca.button.scegliInv" bundle="acquisizioniLabels" />
						</html:submit>
						</logic:notEqual>
	<!--				<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />-->

						<!--aggiunto logic il 11/11/08-->
						<logic:notEqual  name="navForm" property="provInterroga" value="false">
						<html:submit styleClass="pulsanti" property="methodListaOrdini">
							<bean:message key="ricerca.button.listaInventariOrdine" bundle="acquisizioniLabels" />
						</html:submit>
						</logic:notEqual>

			 			<html:submit styleClass="pulsanti" property="methodListaOrdini">
							<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
						</html:submit>
	<!--					<logic:notEqual  name="navForm" property="provInterroga" value="true">-->
	<!--					</logic:notEqual>-->
					</c:when>
					<c:otherwise>
						<html:submit styleClass="pulsanti" property="methodListaOrdini">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>
			 			<html:submit styleClass="pulsanti" property="methodListaOrdini">
							<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
						</html:submit>
					</c:otherwise>
				</c:choose>
                </td>
             </tr>
           </table>

     	  </div>
	</sbn:navform>
</layout:page>
