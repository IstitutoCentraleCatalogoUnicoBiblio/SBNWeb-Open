<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<br/>
<br/>
<sbn:disableAll checkAttivita="DIRITTI">
   	<table width="90%" border="0">
	   	<tr>
	      <td class="etichetta" align="center">
	         <bean:message key="servizi.utenti.inizioAutorizzazione" bundle="serviziLabels" />
	      </td>
	      <c:choose>
			<c:when test="${navForm.uteAna.bibliopolo.inizioAuto ne null and navForm.uteAna.bibliopolo.inizioAuto ne ''
			and !navForm.uteAna.nuovoUte and navForm.uteAnaOLD.bibliopolo.inizioAuto ne ''
			and navForm.uteAnaOLD.bibliopolo.inizioAuto ne null}">
		      <td class="etichetta" align="center">
		         <html:text styleId="testoNoBold" property="uteAna.bibliopolo.inizioAuto" size="10"  maxlength="10" disabled="true"></html:text>
		      </td>
			</c:when>
			<c:otherwise>
		      <td class="etichetta" align="center">
		         <html:text styleId="testoNoBold" property="uteAna.bibliopolo.inizioAuto" size="10"  maxlength="10" disabled="${navForm.conferma}"></html:text>
		      </td>
    		</c:otherwise>
			</c:choose>
	      <td width="100px">&nbsp;</td>
	      <td class="etichetta" align="center">
	         <bean:message key="servizi.utenti.fineAutorizzazione" bundle="serviziLabels" />
	      </td>
	      <td class="etichetta" align="center">
	         <html:text styleId="testoNoBold" property="uteAna.bibliopolo.fineAuto" size="10"  maxlength="10" disabled="${navForm.conferma}"></html:text>
	      </td>
	      <td rowspan="2" align="center" valign="middle">
	      <!--
	      	<html:submit property="methodDettaglio" disabled="${navForm.conferma}">
		  		<bean:message key="servizi.bottone.aggiornaMappaPerParametri" bundle="serviziLabels" />
			</html:submit>
	      -->
	      </td>
	   	</tr>
	   	<tr>
	      <td class="etichetta" align="center">
	         <bean:message key="servizi.utenti.inizioSospensione" bundle="serviziLabels" />
	      </td>
	      <td class="etichetta" align="center">
	         <html:text styleId="testoNoBold" property="uteAna.bibliopolo.inizioSosp" size="10"  maxlength="10" disabled="${navForm.conferma}"></html:text>
	      </td>
	      <td width="50px">&nbsp;</td>
	      <td class="etichetta" align="center">
	         <bean:message key="servizi.utenti.fineSospensione" bundle="serviziLabels" />
	      </td>
	      <td class="etichetta" align="center">
	         <html:text styleId="testoNoBold" property="uteAna.bibliopolo.fineSosp" size="10"  maxlength="10" disabled="${navForm.conferma}"></html:text>
	      </td>
	   	</tr>
	</table>
	<br/>
	<table border="0">
	<tr>
    	<td align="right" class="etichetta">
         	<bean:message key="servizi.utenti.autorizzazione" bundle="serviziLabels" />
        	<html:select property="uteAna.autorizzazioni.codTipoAutor" disabled="${navForm.conferma}">
      			<html:optionsCollection property="elencoAutorizzazioni" value="codAutorizzazione" label="desAutorizzazione" />
        	</html:select>
       </td>
       <td width="40px">&nbsp;</td>
       <td>
        	<html:submit property="methodDettaglio" disabled="${navForm.conferma}">
		  		<bean:message key="servizi.bottone.aggiornaServiziXAuto" bundle="serviziLabels" />
			</html:submit>
      	</td>
   	</tr>
   	</table>
   	<br/>

	<br/>


   <table cellspacing="1" cellpadding="1" border="0">
   <tr>
      <th align="center">
         <bean:message key="servizi.utenti.titDirittiAssegnati" bundle="serviziLabels" />
      </th>
   </tr>
   </table>

   <table class="sintetica">
   <tr bgcolor="#dde8f0">
      <th style="text-align:center; width:20%;">
<!--         <bean:message key="servizi.utenti.titCodice" bundle="serviziLabels" />-->
	     <bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />
      </th>
      <th style="text-align:center; width:20%;">
         <bean:message key="servizi.utenti.titServizio" bundle="serviziLabels" />
      </th>
      <th style="text-align:center; width:10%;">
         <bean:message key="servizi.utenti.titInizioAutorizzazione" bundle="serviziLabels" />
      </th>
      <th style="text-align:center; width:10%;">
         <bean:message key="servizi.utenti.titFineAutorizzazione" bundle="serviziLabels" />
      </th>
      <th style="text-align:center; width:10%;">
         <bean:message key="servizi.utenti.titInizioSospensione" bundle="serviziLabels" />
      </th>
      <th style="text-align:center; width:10%;">
         <bean:message key="servizi.utenti.titFineSospensione" bundle="serviziLabels" />
      </th>
      <th style="text-align:center; width:3%;">
         <bean:message key="servizi.autorizzazioni.header.codAut" bundle="serviziLabels" />
      </th>
      <th style="text-align:center; width:5%;">
         <bean:message key="servizi.utenti.titnoteServizio" bundle="serviziLabels" />
      </th>
      <th  style="text-align:center; width:3%;">
		<bean:message key="servizi.utenti.headerSelezionataMultipla" bundle="serviziLabels" />
	  </th>
   </tr>

   <logic:iterate id="listaServizi" property="uteAna.autorizzazioni.servizi"
      				name="navForm" indexId="index">
	<bs:define	id="codval">
		<bs:write name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].stato" %>' />
	</bs:define>
	<c:choose>
		<c:when test="${ (codval eq 1) or (codval eq 2) or (codval eq 3) or (codval eq 4) or (codval eq 5) or (codval eq 6) }">
			<sbn:rowcolor var="color" index="index"/>
		    <tr  bgcolor="${color}">
		      <td style="width:20%; text-align:center;">
<%--		         <bs:write name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].codice" %>' /> --%>
<%--		         <bs:write name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].descrizioneTipoServizio" %>' /> --%>
		         <bs:write name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].componiTipoServizio" %>' />

		      </td>
		      <td style="width:20%; text-align:center;">
<%--		         <bs:write  name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].servizio" %>' />  --%>
					<bs:write  name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].componi" %>' />
		      </td>
		      <td style="width:10%;">
		         <html:text style="text-align:center;" size="10"  name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].dataInizio" %>' maxlength="10" readonly="${navForm.conferma}" ></html:text>
		      </td>
		      <td style="width:10%;">
		         <html:text style="text-align:center;" size="10"  name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].dataFine" %>' maxlength="10" readonly="${navForm.conferma}" ></html:text>
		      </td>
		      <td style="width:10%;">
		         <html:text style="text-align:center;" size="10"  name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].sospDataInizio" %>' maxlength="10" readonly="${navForm.conferma}" ></html:text>
		      </td>
		      <td style="width:10%;">
		         <html:text style="text-align:center;" size="10"  name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].sospDataFine" %>' maxlength="10" readonly="${navForm.conferma}" ></html:text>
		      </td>
		      <td style="width:3%; text-align:center;">
		          <bs:write name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].flag_aut_ereditato" %>' />
		      </td>
		      <%--
		      <td style="width:3%; text-align:center;">
		          <bs:write name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].autorizzazione" %>' />
		      </td>
		      --%>
		      <%--
		      <td style="width:40%;">
		         <html:textarea  cols="35" name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].note" %>' readonly="${navForm.conferma}"  ></html:textarea>
		      </td>
				--%>

							<td   scope="col" align="center" width="5%">
								<input type="submit"  class="buttonImageNote"  name="tagNote" value="${index}" title="note"></input>
									<c:choose>
										<c:when test="${listaServizi.esisteNota}">
											*
										</c:when>
									</c:choose>
							</td>
		      <td style="width:5%;">
				 <html:checkbox  name="navForm" property='<%= "uteAna.autorizzazioni.listaServizi[" + index + "].cancella" %>' value="C" disabled="${navForm.conferma}"/>
			  </td>
		    </tr>
			<c:choose>
			<c:when test="${navForm.clicNotaPrg == index }">
				  	<tr class="testoNormale" bgcolor="${color}" >
						<td colspan="9" scope="col" align="center" >
	               			<bean:message  key="servizi.documenti.note" bundle="serviziLabels" />
								<html:textarea style="background-color: #FFFFCC;" styleId="testoNormale"  name="navForm"   property='<%="uteAna.autorizzazioni.listaServizi[" + index + "].note" %>' rows="1" cols="100" readonly="${navForm.conferma}"></html:textarea>
						</td>
					</tr>
			</c:when>
		</c:choose>
		</c:when>
		<c:otherwise>
    	</c:otherwise>
	</c:choose>
    </logic:iterate>
 	</table>
</sbn:disableAll>