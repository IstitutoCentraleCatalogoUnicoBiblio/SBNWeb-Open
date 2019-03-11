<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/gestionestampe/bollettario/stampaBollettario.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors />
		</div>
	<table   width="100%"  align="center" border="0" >

					<!--  biblioteca-->
			<tr>
					<td colspan="4">
					<div class="etichetta"><bean:message
						key="ricerca.label.biblioteca" bundle="gestioneStampeLabels" />
					<html:text disabled="true" styleId="testoNormale" property="codBib"
						size="5" maxlength="3"></html:text> <span disabled="true">
						<html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodStampaBollettario" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
						</html:submit>
						<bean-struts:write	name="stampaBollettarioForm" property="descrBib" />

					</span> </div>
					</td>
				</tr>
				<tr>
                        <td scope="col"  align="left" class="etichetta" style="width:15%">
                        	<bean:message  key="ordine.label.rilegaturaDataUscitaStp" bundle="acquisizioniLabels" />
                        </td>
                        <td style="width:25%">
				 		  	<html:text styleId="testoNormale" property="dataUscitaDa" size="10" ></html:text>
                        </td>
                        <td  align="left" class="etichetta" style="width:25%">
                        	<bean:message  key="ricerca.label.dataOrdineA" bundle="acquisizioniLabels" />
				 		  <html:text styleId="testoNormale" property="dataUscitaA" size="10"></html:text>
                        	<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />

                        </td>
                       <td  scope="col" align="center">&nbsp;</td>

			</tr>
		     <tr>
                        <td scope="col"  align="left" class="etichetta">
                        	<bean:message  key="ordine.label.rilegaturaDataRientroStp" bundle="acquisizioniLabels" />
                        </td>
                        <td>
				 		  	<html:text styleId="testoNormale" property="dataRientroDa" size="10" ></html:text>
                        </td>
                        <td  align="left" class="etichetta">
                        	<bean:message  key="ricerca.label.dataStampaOrdineA" bundle="acquisizioniLabels" />
				 		  <html:text styleId="testoNormale" property="dataRientroA" size="10" ></html:text>
                        	<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />

                        </td>
                       <td  scope="col" align="center">&nbsp;</td>

			</tr>


			<tr>

                        <td scope="col" align="left" class="etichetta">
                        	<bean:message  key="ordine.label.rilegaturaDataRientroPresuntaStp" bundle="acquisizioniLabels" />
                        </td>
                        <td>
				 		  <html:text styleId="testoNormale" property="dataRientroPresuntaDa" size="10" ></html:text>
                        </td>
						<td class="etichetta">
						<bean:message  key="ricerca.label.dataOrdineAbbA" bundle="acquisizioniLabels" />
				 		<html:text styleId="testoNormale" property="dataRientroPresuntaA" size="10" ></html:text>
                        	<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />

                        </td>
                       <td  scope="col" align="center">&nbsp;</td>

		     </tr>

		     <tr>
                        <td   scope="col" class="etichetta" align="right">
							<bean:message  key="ricerca.label.opereInRilegatura" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
							<html:checkbox property="ricercaLocale" ></html:checkbox>
                        </td>
                        <td   scope="col" class="etichetta" align="right">
							<bean:message  key="ricerca.label.ristampaEtichette" bundle="acquisizioniLabels" />
							<html:checkbox property="ristampaEtichette" ></html:checkbox>
                        </td>
                        <td  scope="col" align="left">&nbsp;</td>


			</tr>
		      <tr>
			    <td colspan="4">
			     	<hr color="#dde8f0"/>
			    </td>
			  </tr>

		     <tr>
                        <%-- td   valign="top" scope="col" align="left"> <div class="etichetta" --%>
                        <td  width="15%"  scope="col"  ><div align="left" class="etichetta">
						<bean:message  key="periodici.label.ordinamento" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<%--html:select  styleClass="testoNormale"  property="provincia"--%>
						<html:select  styleClass="testoNormale"  property="tipoOrdinamSelez" style="width:200px">
						<html:optionsCollection  property="listaTipiOrdinamento" value="codice" label="descrizione" />
						</html:select>

                        </td>

						<bean-struts:size id="comboSize" name="stampaBollettarioForm" property="elencoModelli" />
						<logic:greaterEqual name="comboSize" value="2">
							<!--Selezione Modello Via Combo-->
							<td  width="15%"  scope="col"  ><div align="left" class="etichetta">
							<bean:message key="fornitori.label.modello" bundle="gestioneStampeLabels" />
							</div></td>
							<td valign="top" scope="col" align="left">
							<html:select  styleClass="testoNormale"  property="tipoModello" >
								<html:optionsCollection  property="elencoModelli" value="jrxml" label="descrizione" />
							</html:select>
							</td>
						</logic:greaterEqual>
						<logic:lessThan name="comboSize" value="2">
							<!--Selezione Modello Hidden-->
							<td  width="15%"  scope="col"  ><div align="left" class="etichetta">
							&nbsp;
							</div></td>
							<td valign="top" scope="col" align="left">
							&nbsp;
							<html:hidden property="tipoModello" value="${stampaBollettarioForm.elencoModelli[0].jrxml}" />
							</td>
						</logic:lessThan>

             </tr>

		  </table>

	     	<HR>
			<jsp:include flush="true" page="../common/tipoStampa.jsp" />
			<HR>

</div>
<div id="divFooter">
	<table align="center" border="0" style="height:40px" >
		<tr >
			<td>
				<html:submit styleClass="pulsanti" property="methodStampaBollettario">
				<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
				</html:submit>
				<html:submit styleClass="pulsanti" property="methodStampaBollettario">
					<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
				</html:submit>
			</td>
		</tr>
	</table>
  </div>
</sbn:navform>
</layout:page>
