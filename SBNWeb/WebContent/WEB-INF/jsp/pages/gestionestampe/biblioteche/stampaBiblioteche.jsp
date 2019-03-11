<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionestampe/biblioteche/stampaBiblioteche.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors bundle="gestioneStampeMessages" /><sbn:errors />
		</div>
	<div id="content">
	<table   width="100%"  align="center"  >
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">

		  <table   width="100%" border="0">
		     <tr>
                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="biblioteche.label.livelloDiRicerca" bundle="gestioneStampeLabels" />
                        </div></td>
    					<td valign="top" scope="col" >
     					<div align="left" class="etichetta">
	     					<bean:message key="biblioteche.label.biblio" bundle="gestioneStampeLabels" />
	     					<%--html:radio property="biblioteca" value="biblioteche.label.biblio" /--%>
	     					<html:radio property="biblioteca" value="bibliotecheBiblio" />
						</div>
						</td>
     					<td valign="top" scope="col" >
     					<div align="left" class="etichetta">
	     					<bean:message key="biblioteche.label.polo" bundle="gestioneStampeLabels" />
    						<html:radio property="biblioteca" value="bibliotechePolo" />
	     					<%--html:radio property="biblioteca" value="biblioteche.label.polo" /--%>
						</div>
						</td>

			<%-- tr>
				<td width="90" class="etichetta"><bean:message
					key="ricerca.tiponome" bundle="gestioneBibliograficaLabels" />:</td>
				<td width="200"><bean:message key="ricerca.nomePersonale"
					bundle="gestioneBibliograficaLabels" /> <html:radio
					property="tipoAutore" value="autorePersonale" onchange="this.form.submit()"/></td>
				<td><bean:message key="ricerca.nomeCollettivo"
					bundle="gestioneBibliograficaLabels" /> <html:radio
					property="tipoAutore" value="autoreCollettivo" onchange="this.form.submit()"/></td>
			</tr--%>
		     </tr>
		  </table>
			<HR>
		  <table   width="100%" border="0">
		     <tr>
                        <td valign="top" width="20%" scope="col" ><div align="left" class="etichetta">
                        <bean:message  key="biblioteche.label.criteriDiRicerca" bundle="gestioneStampeLabels" />
                        </div></td>
		     	</tr>
            </table>
		  <table   width="100%" border="0">
		     <tr>
                        <%-- td   valign="top" scope="col" align="left"> <div class="etichetta" --%>
                        <td  width="15%"  scope="col"  ><div align="left" class="etichetta">
						<bean:message  key="biblioteche.label.tipoBiblioteca" bundle="gestioneStampeLabels" />
						</div></td>
                        <td colspan="5" valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="tipoBiblioteca">
						<html:optionsCollection  property="listaTipoBiblioteca" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <%--td valign="top" scope="col" align="left"> <div class="etichetta" >
						</div></td>
                        <td valign="top" scope="col" align="left"> <div class="etichetta" >
                        </div></td--%>
             </tr>
		     <tr>
                        <%-- td   valign="top" scope="col" align="left"> <div class="etichetta" --%>
                        <td  width="15%"  scope="col"  ><div align="left" class="etichetta">
						<bean:message  key="biblioteche.label.nomeBiblioteca" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="nomeBiblioteca" size="30"></html:text>
                        </td>
                         <td   valign="top" scope="col" align="right"> <div class="etichetta" >
						<bean:message  key="biblioteche.label.enteDiAppartenenza" bundle="gestioneStampeLabels" />
						</div></td>
                        <%-- td  valign="top" scope="col" align="left"--%>
                        <td  width="15%"  scope="col"  ><div align="left" class="etichetta">
 						<html:text styleId="testoNormale" property="enteDiAppartenenza" size="30"></html:text>
                        </td>
                        <td  valign="top" scope="col" align="left"><div class="etichetta" >
						<bean:message  key="biblioteche.label.polo" bundle="gestioneStampeLabels" /></div></td>
						<td>
 						<html:text styleId="testoNormale" property="polo" size="10"></html:text>
                        </td>
             </tr>
		</table>
		  <table   width="100%" border="0">
		     <tr>
                        <%-- td   valign="top" scope="col" align="left"> <div class="etichetta" --%>
                        <td  width="15%"  scope="col"  ><div align="left" class="etichetta">
						<bean:message  key="biblioteche.label.provincia" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<%--html:select  styleClass="testoNormale"  property="provincia"--%>
						<html:select  styleClass="testoNormale"  property="provincia" style="width:190px">
						<html:optionsCollection  property="listaProvincia" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <%-- td   valign="top" scope="col" align="left"> <div class="etichetta"--%>
                         <td  width="10%"  scope="col"  ><div align="right" class="etichetta">
						<bean:message  key="biblioteche.label.paese" bundle="gestioneStampeLabels" />
						</div></td>
                        <td colspan="2" valign="top" scope="col" align="left">
						<%--html:select  styleClass="testoNormale"  property="paese"--%>
						<html:select  styleClass="testoNormale"  property="paese" style="width:185px">
						<html:optionsCollection  property="listaPaese" value="codice" label="descrizione" />
						</html:select>
                        </td>
             </tr>
           </table>
           <table width="100%" border="0">
            	 <tr>


						<bean-struts:size id="comboSize" name="stampaBibliotecheForm" property="elencoModelli" />
						<logic:greaterEqual name="comboSize" value="2">
							<!--Selezione Modello Via Combo-->
							<td  width="15%"  scope="col"  ><div align="left" class="etichetta">
	                        <bean:message  key="biblioteche.label.modello" bundle="gestioneStampeLabels" />
							</div></td>
	   						<td colspan="5" valign="top" scope="col" align="left">
							<html:select  styleClass="testoNormale"  property="tipoModello" style="width:205px">
								<html:optionsCollection  property="elencoModelli"
									value="jrxml" label="descrizione" />
							</html:select>
							</td>
						</logic:greaterEqual>
						<logic:lessThan name="comboSize" value="2">
							<!--Selezione Modello Hidden-->
							<td  width="15%"  scope="col"  ><div align="left" class="etichetta">
							&nbsp;
							</div></td>
	   						<td colspan="5" valign="top" scope="col" align="left">
							&nbsp;
							<html:hidden property="tipoModello" value="${stampaBibliotecheForm.elencoModelli[0].jrxml}" />
							</td>
						</logic:lessThan>
            	 </tr>
            </table>





				</td>
       	        <!-- FINE tabella corpo COLONNA + LARGA -->
             </tr>
	     </table>
	     	<HR>
			<jsp:include flush="true" page="../common/tipoStampa.jsp" />
			<HR>

           <table align="center" border="0" style="height:40px" >
            <tr >
             <td>
			<html:submit styleClass="pulsanti" property="methodStampaBiblioteche">
				<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodStampaBiblioteche">
				<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
			</html:submit>
		 </td>
	  </tr>
   	 </table>

   	 </div>
	</sbn:navform>
</layout:page>
