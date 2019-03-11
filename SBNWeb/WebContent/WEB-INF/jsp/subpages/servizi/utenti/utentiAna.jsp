<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<br>
<br>
<sbn:disableAll checkAttivita="GESTIONE">
<table width="100%" border="0">
<c:choose>
	<c:when test="${navForm.tipoUtente eq 'P'}">
       <tr>
         <td width="100px" class="etichetta" align="right">
             <bean:message key="servizi.utenti.luogoNascita" bundle="serviziLabels" />
         </td>
         <td width="150px" class="etichetta" align="left">
             <html:text styleId="testoNoBold" property="uteAna.anagrafe.luogoNascita" size="25" maxlength="30" disabled="${navForm.conferma}"></html:text>
         </td>
         <td width="40px">&nbsp;</td>
         <td width="80px" class="etichetta" align="right">
             <bean:message key="servizi.utenti.sesso" bundle="serviziLabels" />
         </td>
         <td width="150px" align="left">
			<html:select property="uteAna.anagrafe.sesso" disabled="${navForm.conferma}">
			<html:option value=""></html:option>
			<html:option value="M" bundle="serviziLabels" key="servizi.utenti.sessom"></html:option>
			<html:option value="F" bundle="serviziLabels" key="servizi.utenti.sessof"></html:option>
			</html:select>
		  </td>
        </tr>
        <tr>
            <td width="100px" class="etichetta" align="right">
              <bean:message key="servizi.utenti.dataNascita" bundle="serviziLabels" />
            </td>
            <td width="150px" align="left">
              <html:text styleId="testoNoBold" property="uteAna.anagrafe.dataNascita" size="10"  maxlength="10" disabled="${navForm.conferma}"></html:text>
            </td>
            <td width="40px">&nbsp;</td>
			<td width="80px" class="etichetta" align="right">
			  <bean:message key="servizi.utenti.nazionalitaCittadinanza" bundle="serviziLabels" />
			</td>
			<td width="150px" align="left">
				<html:select property="uteAna.anagrafe.nazionalita" disabled="${navForm.conferma}">
				<html:optionsCollection property="nazCitta" value="codice" label="descrizioneCodice" />
				</html:select>
      		</td>
       </tr>
	</c:when>
</c:choose>
       <tr>
			<td width="100px" class="etichetta" align="right"><c:choose>
				<c:when test="${navForm.uteAna.professione.ente}">
					<bean:message key="servizi.utenti.codiceFiscale.ente" bundle="serviziLabels" />
				</c:when>
				<c:otherwise>
					<bean:message key="servizi.utenti.codiceFiscale" bundle="serviziLabels" />
				</c:otherwise>
			</c:choose></td>
			<td align="left" colspan="3"><html:text styleId="testoNoBold"
				property="uteAna.anagrafe.codFiscale" size="26" maxlength="16"
				disabled="${navForm.conferma}"/>
			</td>
			<c:if test="${navForm.tipoUtente eq 'E'}">
				<td width="100px" class="etichetta" align="right">
				<bean:message key="servizi.utenti.codiceAnagrafe" bundle="serviziLabels" />
				</td>
				<td width="150px" align="left"><html:text styleId="testoNoBold"
					property="uteAna.bibliopolo.codiceAnagrafe" size="10" maxlength="6"
					disabled="${navForm.uteAna.bibliopolo.uteBibl or navForm.conferma}" />
				</td>
			</c:if>
		</tr>
    </table>
    <br/>
    <label><em><strong>
    <c:choose>
    <c:when test="${navForm.tipoUtente eq 'P'}">
    	<bean:message key="servizi.utenti.residenza" bundle="serviziLabels" />
    </c:when>
    <c:otherwise>
    	<bean:message key="servizi.utenti.sede" bundle="serviziLabels" />
    </c:otherwise>
    </c:choose>
    </strong></em></label>
	<br/>
    <table width="100%" border="0">
       <tr>
          <td width="60px" class="etichetta" align="right">
             <bean:message key="servizi.utenti.indirizzoRes" bundle="serviziLabels" />
          </td>
          <td class="testoNoBold">
             <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.via" size="105" maxlength="105" disabled="${navForm.conferma}"></html:text>
          </td>
          <td width="40px" class="etichetta" align="right">
             <bean:message key="servizi.utenti.capRes" bundle="serviziLabels" />
          </td>
          <td class="testoNoBold">
             <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.cap" size="10" maxlength="10" disabled="${navForm.conferma}"></html:text>
          </td>
       </tr>
       <tr>
          <td width="60px" class="etichetta" align="right">
             <bean:message key="servizi.utenti.cittaRes" bundle="serviziLabels" />
          </td>
          <td class="testoNoBold">
             <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.citta" size="75" maxlength="50" disabled="${navForm.conferma}"></html:text>
          </td>
          <td width="40px" class="etichetta" align="right">
             <bean:message key="servizi.utenti.provRes" bundle="serviziLabels" />
          </td>
          <td class="testoNoBold" >
             <html:select property="uteAna.anagrafe.residenza.provincia" disabled="${navForm.conferma}">
             <html:optionsCollection property="provinciaResidenza" value="codice" label="descrizioneCodice" />
             </html:select>
          </td>
       </tr>
    </table>
    <table>
    	<tr>
          <td width="60px" class="etichetta" align="right">
             <bean:message key="servizi.utenti.nazRes" bundle="serviziLabels" />
          </td>
          <td align="left">
             <html:select property="uteAna.anagrafe.residenza.nazionalita" disabled="${navForm.conferma}">
			 <html:optionsCollection property="nazCitta" value="codice" label="descrizioneCodice" />
		     </html:select>
          </td>
       </tr>
    </table>
    <br/>

<c:choose>
	<c:when test="${navForm.tipoUtente eq 'P'}">

    <label>
    	<em><strong><bean:message key="servizi.utenti.domicilio" bundle="serviziLabels" /></strong></em>
    </label>
    <br>
    <table width="100%" border="0">
        <tr>
          <td  width="60px" class="etichetta" align="right">
           <bean:message key="servizi.utenti.indirizzoRes" bundle="serviziLabels" />
          </td>
          <td class="testoNoBold">
           <html:text styleId="testoNoBold" property="uteAna.anagrafe.domicilio.via" size="105" maxlength="105" disabled="${navForm.conferma}"></html:text>
          </td>
          <td  width="40px" class="etichetta" align="right">
           <bean:message key="servizi.utenti.capRes" bundle="serviziLabels" />
          </td>
          <td  class="testoNoBold">
           <html:text styleId="testoNoBold" property="uteAna.anagrafe.domicilio.cap" size="10" maxlength="10" disabled="${navForm.conferma}"></html:text>
          </td>
        </tr>
        <tr>
          <td width="60px" class="etichetta" align="right">
           <bean:message key="servizi.utenti.cittaRes" bundle="serviziLabels" />
          </td>
          <td class="testoNoBold">
           <html:text styleId="testoNoBold" property="uteAna.anagrafe.domicilio.citta" size="75" maxlength="50" disabled="${navForm.conferma}"></html:text>
          </td>
          <td width="40px" class="etichetta" align="right">
           <bean:message key="servizi.utenti.provRes" bundle="serviziLabels" />
          </td>
          <td class="testoNoBold">
           <html:select property="uteAna.anagrafe.domicilio.provincia" disabled="${navForm.conferma}">
           <html:optionsCollection property="provinciaResidenza" value="codice" label="descrizioneCodice" />
           </html:select>
          </td>
        </tr>
    </table>
    <br/>
	</c:when>
</c:choose>

	<table width="100%" border="0">
		<tr>
			<th width="100px">&nbsp;</th>
			<th></th>
			<th width="120px"></th>
			<th width="100px"></th>
			<th width="1%"></th>
			<th></th>
		</tr>
		<tr>
			<td><bean:message key="servizi.utenti.telefonoFisso" bundle="serviziLabels" /></td>
			<td><html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.telefono" size="20" maxlength="20" disabled="${navForm.conferma}"></html:text></td>
			<td></td>
			<td><bean:message key="servizi.utenti.faxRes" bundle="serviziLabels" /></td>
			<td></td>
			<td><html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.fax" size="15" maxlength="20" disabled="${navForm.conferma}"></html:text></td>
		</tr>
		<tr>
			<td><bean:message key="servizi.utenti.cellulare" bundle="serviziLabels" /></td>
			<td><html:text styleId="testoNoBold" property="uteAna.anagrafe.domicilio.telefono" size="20" maxlength="20" disabled="${navForm.conferma}"></html:text></td>
			<td></td>
			<td><bean:message key="servizi.utenti.email" bundle="serviziLabels" /></td>
			<td>1</td>
			<td><html:text styleId="testoNoBold" property="uteAna.anagrafe.postaElettronica" size="40" maxlength="80" disabled="${navForm.conferma}"></html:text></td>
		</tr>
		<c:if test="${navForm.tipoUtente eq 'P'}">
			<tr>
				<td colspan="4">&nbsp;</td>
				<td>2</td>
				<td><html:text styleId="testoNoBold" property="uteAna.anagrafe.postaElettronica2" size="40" maxlength="80" disabled="${navForm.conferma}"></html:text>
				&nbsp;&#x00B9;</td>

			</tr>
		</c:if>
	</table>

    <table width="80%" border="0">
    	<tr>
    		<th colspan="6" align="left"><bean:message key="servizi.utenti.smsSu" bundle="serviziLabels" /></th>
    	</tr>
    	<tr>
    		<td width="60px"><bean:message key="servizi.utenti.smsSuFisso" bundle="serviziLabels" /></td>
    		<td width="80px"><html:radio property="uteAna.tipoSMS" value="${navForm.uteAna.smsSuFisso}"  disabled="${navForm.conferma}" /></td>
    		<td width="60px"><bean:message key="servizi.utenti.smsSuMobile" bundle="serviziLabels" /></td>
    		<td width="80px"><html:radio property="uteAna.tipoSMS" value="${navForm.uteAna.smsSuMobile}"  disabled="${navForm.conferma}" /></td>
    		<td width="60px"><bean:message key="servizi.utenti.noSms" bundle="serviziLabels" /></td>
    		<td width="80px"><html:radio property="uteAna.tipoSMS" value="${navForm.uteAna.noSms}"        disabled="${navForm.conferma}" /></td>
    	</tr>
    </table>
    <br/>

    <table width="100%" border="0">
      <tr>
         <td width="100px" class="etichetta" align="right">
            <bean:message key="servizi.utenti.provenienza" bundle="serviziLabels" />
         </td>
         <td align="left">
           	<html:select property="uteAna.anagrafe.provenienza" disabled="${navForm.conferma}">
	       		<html:optionsCollection property="provenienze" value="codice" label="descrizioneCodice" />
           	</html:select>
         </td>
		<td colspan="2">
			<c:choose>
				<c:when test="${not navForm.mostraAltriDati}">
			       	<html:submit property="methodDettaglio" disabled="${navForm.conferma}">
				  	<bean:message key="servizi.bottone.altriDatiUtente" bundle="serviziLabels" />
					</html:submit>
				</c:when>
				<c:otherwise>&nbsp;</c:otherwise>
			</c:choose>
		</td>
      </tr>
   </table>

   <c:if test="${navForm.mostraAltriDati}">
	<c:choose>
		<c:when test="${navForm.tipoUtente eq 'P'}">
	  		<jsp:include	page="/WEB-INF/jsp/subpages/servizi/utenti/utentiDoc.jsp" />
		</c:when>
	</c:choose>

		<jsp:include	page="/WEB-INF/jsp/subpages/servizi/utenti/utentiPro.jsp" />
		<html:submit property="methodDettaglio" disabled="${navForm.conferma}">
		  	<bean:message key="servizi.bottone.chiudiAltriDatiUtente" bundle="serviziLabels" />
		</html:submit>
   </c:if>
	<br />
	<div class="msgOK1n">&#x00B9;&nbsp;</div>
	<div class="msgOK1">
		<bean:message key="servizi.utente.mail2.info" bundle="serviziLabels" />
	</div>

	<br />
</sbn:disableAll>

