<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/gestionestampe/fornitori/stampaFornitori.do">
	<div id="divForm">
		<div id="divMessaggio"><sbn:errors />
	</div>
	<table   width="100%"  align="center"  ><!--
       	     <tr>
       	         tabella corpo COLONNA + LARGA
                <td align="left" valign="top" width="100%">

 <table   width="100%" border="0">
		     --><tr>
                       <td width="15%" class="etichetta" align="right"><div align="left" class="etichetta">
                        <bean:message  key="fornitori.label.nomForn" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td colspan="3" valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="nomForn" size="15" ></html:text>
                       </div></td>


		     </tr><!--
		   </table>


		 --><%--table   width="100%" border="0"--%>
		     <tr>
                       <td  width="15%" valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="fornitori.label.tpForn" bundle="gestioneStampeLabels" /></div></td>
                        <td  colspan="3" valign="top" scope="col" ><div align="left">

				          <html:select  styleClass="testoNormale"  property="tpForn" style="width:200px">
						  <html:optionsCollection  property="listaTpForn" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>


		     </tr>
		    <%--/table--%>



		  	 <%--table   width="100%" border="0"--%>
		     <tr>
                      <td  width="15%"  scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="fornitori.label.paese" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">

				          <html:select  styleClass="testoNormale"  property="paese" style="width:200px">
						  <html:optionsCollection  property="listaPaese" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>


                           <%-- td  valign="top" scope="col"  ><div align="left" class="etichetta"--%>
                        <td  width="15%"  scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="fornitori.label.prov" bundle="gestioneStampeLabels" /></div></td>
                        <td  valign="top" scope="col" ><div align="left">

				          <html:select  styleClass="testoNormale"  property="prov" style="width:100px">
						  <html:optionsCollection  property="listaProv" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
			 </tr>

		 	 <%--tabletable   width="100%" border="0"--%>
		     <tr>
                       <td  width="15%"  valign="top"  scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="fornitori.label.codForn" bundle="gestioneStampeLabels" />
                        </div></td>
                        <td colspan="3" valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="codForn" size="15" ></html:text>
                       </div></td>


		     </tr>
		   </table>

		   <%--/table--%>
		  <table   width="100%" border="0">
		      <tr>
			    <td>
			     	<hr color="#dde8f0"/>
			    </td>
			  </tr>
		  </table>
		  	 <table   width="100%" border="0">

		     <tr>
                        <td   scope="col" class="etichetta" align="right">
							<bean:message  key="ricerca.label.ricercaLocale" bundle="acquisizioniLabels" />
                        </td>
                        <td colspan="4" scope="col" align="left">
<!--							<html:checkbox property="ricercaLocale" value="true"></html:checkbox>-->
							<html:checkbox property="ricercaLocale" ></html:checkbox>
                        </td>
			</tr>

			<!--  biblioteca-->
				<tr>
					<td colspan="4">
					<div class="etichetta">
					<bean:message	key="ricerca.label.biblioteca" bundle="gestioneStampeLabels" />
					<html:text disabled="true" styleId="testoNormale" property="codBib"	size="5" maxlength="3"></html:text>
						<html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodStampaFornitori" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
						</html:submit>
						<bean-struts:write	name="stampaFornitoriForm" property="descrBib" /></div>
					</td>
				</tr>

		     <tr>
		                <td  width="15%"   valign="top" scope="col"  ><div align="left" class="etichetta">
                        <bean:message  key="fornitori.label.profAcq" bundle="gestioneStampeLabels" /></div></td>
                        <td colspan="3" valign="top" scope="col" ><div align="left">

				          <html:select  styleClass="testoNormale"  property="profAcq" style="width:200px" >
						  <html:optionsCollection  property="listaProfAcq" value="profilo.codice" label="profilo.descrizione" />
						  </html:select>
                        </div></td>
			 </tr>

			 		   <%--/table--%>
	  	<table   width="100%" border="0">
		      <tr>
			    <td>
			     	<hr color="#dde8f0"/>
			    </td>
			  </tr>
	   	</table>
	    <table>
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
   						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>
   						<td scope="col" align="center">&nbsp;</td>
   						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>
   						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>
   						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>
   						<td scope="col" align="center">&nbsp;</td>
						<td scope="col" align="center">&nbsp;</td>
   						<td scope="col" align="center">&nbsp;</td>

						<bean-struts:size id="comboSize" name="stampaFornitoriForm" property="elencoModelli" />
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
							<html:hidden property="tipoModello" value="${stampaFornitoriForm.elencoModelli[0].jrxml}" />
							</td>
						</logic:lessThan>

             </tr>
		  </table>



		   <%-- /table--%>


       	        <!-- FINE tabella corpo COLONNA + LARGA -->

	     </table>
	     	<HR>
			<jsp:include flush="true" page="../common/tipoStampa.jsp" />
			<HR>
</div>
<div id="divFooter">
	<table align="center" border="0" style="height:40px" >
		<tr >
			<td>
				<html:submit styleClass="pulsanti" property="methodStampaFornitori">
				<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
				</html:submit>

				<html:submit styleClass="pulsanti" property="methodStampaFornitori">
					<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
				</html:submit>
			</td>
		</tr>
	</table>
  </div>
</sbn:navform>
</layout:page>
