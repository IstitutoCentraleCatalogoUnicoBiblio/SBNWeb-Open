<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<head>
	<title>Servizi SBN Web</title>

	<link rel="stylesheet" type="text/css"
		href='<html:rewrite page="/styles/login.css" paramScope="request" paramName="url" />' />
</head>


<div id="header"></div>
<div id="data">
<div><logic:present name="POLO_NAME" scope="session">
	<p id="polo"><bean-struts:write scope="session" name="POLO_NAME" /></p>
</logic:present></div>
 <div id="divForm">
  <table style="margin-top:0"  border="0">
	<tr>
    	<td width="100px" class="etichetta" align="left">
             <em><strong><bean:message key="servizi.nome.biblioteca.iscrizione"  bundle="serviziWebLabels" /></strong></em> :
         	<c:out value="${InserimentoUtenteWebForm.nome_biblio}"> </c:out>
         </td>
	</tr>
	</table>
	<div id="divMessaggio"><sbn:errors bundle="serviziWebMessages" /></div>
	<html:form action="/serviziweb/inserimentoutenteWeb.do">



     <table style="margin-top:0"  border="0">
     	<tr>
       	 <td width="100px" class="etichetta" align="left">
             <em><strong><bean:message key="servizi.utenti.campi.obbligatori"  bundle="serviziWebLabels" /></strong></em>
         </td>
     	</tr>
     </table>

     <c:if test="${empty InserimentoUtenteWebForm.password}">
     <em><strong><bean:message key="servizi.utenti.datiAnagrafici.obbligatorio"  bundle="serviziWebLabels" /></strong></em>

    <table style="margin-top:0"  border="2" >
       <tr>
       	 <td><bean:message key="servizi.utenti.cognome" bundle="serviziWebLabels" /></td>
         <td ><html:text name="InserimentoUtenteWebForm" property="uteAna.cognome"></html:text></td>
         <td></td>
       	 <td><bean:message key="servizi.utenti.nome" bundle="serviziWebLabels"/></td>
          <td><html:text name="InserimentoUtenteWebForm" property="uteAna.nome"></html:text></td>
       </tr>

       <tr>
         <td><bean:message key="servizi.utenti.luogoNascita" bundle="serviziWebLabels"/></td>
         <td><html:text name="InserimentoUtenteWebForm" property="uteAna.anagrafe.luogoNascita"></html:text></td>
         <td></td>

         <td> <bean:message key="servizi.utenti.sesso" bundle="serviziWebLabels" /></td>
            <td><html:select name="InserimentoUtenteWebForm"property="uteAna.anagrafe.sesso" disabled="${InserimentoUtenteWebForm.conferma}">
			<html:option value=""></html:option>
			<html:option value="M" bundle="serviziWebLabels" key="servizi.utenti.sessom"></html:option>
			<html:option value="F" bundle="serviziWebLabels" key="servizi.utenti.sessof"></html:option>
			</html:select> </td>
        </tr>

        <tr>
            <td><bean:message key="servizi.utenti.dataNascita" bundle="serviziWebLabels"/></td>
            <td><html:text styleId="testoNoBold" name="InserimentoUtenteWebForm" property="uteAna.anagrafe.dataNascita" size="10" disabled="${InserimentoUtenteWebForm.conferma}"></html:text></td>
            <td></td>
			<td><bean:message key="servizi.utenti.nazionalitaCittadinanza" bundle="serviziWebLabels" /></td>
			<td>
				<html:select name="InserimentoUtenteWebForm" property="uteAna.anagrafe.nazionalita" disabled="${InserimentoUtenteWebForm.conferma}"><html:optionsCollection name="InserimentoUtenteWebForm" property="nazCitta" value="codice" label="descrizioneCodice"/></html:select>		</td>
       	</tr>
       <tr>
			<td>
			  <bean:message key="servizi.utenti.codiceFiscale" bundle="serviziWebLabels" />
			</td>
			<td>
			  <html:text styleId="testoNoBold" property="uteAna.anagrafe.codFiscale" size="26" maxlength="16" disabled="${InserimentoUtenteWebForm.conferma}"></html:text>
			</td>
			<td></td>
			<td width="20px"><bean:message key="servizi.utenti.email.obbligatorio" bundle="serviziWebLabels" /></td>
			<td>
				1&nbsp;<html:text styleId="testoNoBold" property="uteAna.anagrafe.postaElettronica" size="47" maxlength="80" disabled="${InserimentoUtenteWebForm.conferma}"></html:text>
				<br/>
				2&nbsp;<html:text styleId="testoNoBold" property="uteAna.anagrafe.postaElettronica2" size="47" maxlength="80" disabled="${InserimentoUtenteWebForm.conferma}"></html:text>&nbsp;&#x00B9;
			</td>
       </tr>

    </table>
    <br>


				<div class="msgOK1">
					&#x00B9;&nbsp;
					<bean:message key="servizi.utente.mail2.info"
						bundle="serviziLabels" />
				</div>


				<br/>
   	<html:submit styleClass="pulsanti" property="paraminserimentoutenteWeb">
			<bean:message key="servizi.indietro.esci" bundle="serviziWebLabels" />
	</html:submit>
   	<html:submit styleClass="pulsanti" property="paraminserimentoutenteWeb">
			<bean:message key="button.avanti" bundle="serviziWebLabels" />
	</html:submit>
	</c:if>
	<br>
   	<br/>

	<c:if test="${not empty InserimentoUtenteWebForm.password}">
	<label>
       <em><strong><bean:message key="servizi.utenti.residenza.obbligatorio" bundle="serviziWebLabels" /></strong></em>
	</label>
    <table style="margin-top:0" border="2">
       <tr>
          <td >
             <bean:message key="servizi.utenti.indirizzoRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold">
             <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.via" size="65" maxlength="50" disabled="${InserimentoUtenteWebForm.conferma}"></html:text>
          </td>
          <td>
             <bean:message key="servizi.utenti.capRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold">
             <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.cap" size="5" maxlength="5" disabled="${InserimentoUtenteWebForm.conferma}"></html:text>
          </td>
       </tr>
       <tr>
          <td>
             <bean:message key="servizi.utenti.cittaRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold">
             <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.citta" size="65" maxlength="50" disabled="${InserimentoUtenteWebForm.conferma}"></html:text>
          </td>
          <td>
             <bean:message key="servizi.utenti.provRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold" >
             <html:select name="InserimentoUtenteWebForm" property="uteAna.anagrafe.residenza.provincia" disabled="${InserimentoUtenteWebForm.conferma}">
             <html:optionsCollection property="provinciaResidenza" value="codice" label="descrizioneCodice" />
             </html:select>
          </td>
       </tr>
       <tr>
          <td>
             <bean:message key="servizi.utenti.nazRes" bundle="serviziWebLabels" />
          </td>
          <td align="left">
             <html:select name="InserimentoUtenteWebForm" property="uteAna.anagrafe.residenza.nazionalita" disabled="${InserimentoUtenteWebForm.conferma}">
			 <html:optionsCollection property="nazCitta" value="codice" label="descrizioneCodice" />
		     </html:select>
          </td>
       </tr>
    </table>

    <label>
    	<em><strong><bean:message key="servizi.utenti.domicilio.diverso" bundle="serviziWebLabels" /></strong></em>
    </label>

    <table style="margin-top:0"  border="2">
        <tr>
          <td  width="40px" class="etichetta" align="left">
           <bean:message key="servizi.utenti.indirizzoRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold">
           <html:text styleId="testoNoBold" property="uteAna.anagrafe.domicilio.via" size="65" maxlength="50" disabled="${InserimentoUtenteWebForm.conferma}"></html:text>
          </td>
          <td  width="40px" class="etichetta" align="left">
           <bean:message key="servizi.utenti.capRes" bundle="serviziWebLabels" />
          </td>
          <td  class="testoNoBold">
           <html:text styleId="testoNoBold" property="uteAna.anagrafe.domicilio.cap" size="5" maxlength="5" disabled="${InserimentoUtenteWebForm.conferma}"></html:text>
          </td>
        </tr>
        <tr>
          <td width="40px" class="etichetta" align="left">
           <bean:message key="servizi.utenti.cittaRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold">
           <html:text styleId="testoNoBold" property="uteAna.anagrafe.domicilio.citta" size="65" maxlength="50" disabled="${InserimentoUtenteWebForm.conferma}"></html:text>
          </td>
          <td width="40px" class="etichetta" align="left">
           <bean:message key="servizi.utenti.provRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold">
           <html:select name="InserimentoUtenteWebForm" property="uteAna.anagrafe.domicilio.provincia" disabled="${InserimentoUtenteWebForm.conferma}">
           <html:optionsCollection property="provinciaResidenza" value="codice" label="descrizioneCodice" />
           </html:select>
          </td>
        </tr>
    </table>
    <em><strong><bean:message key="servizi.utenti.recapiti" bundle="serviziWebLabels" /></strong></em>
    <table style="margin-top:0"  border="2">
			<tr>
				<td width="20px"><bean:message key="servizi.utenti.telefonoFisso.obbligatorio" bundle="serviziWebLabels" /></td>
				<td> <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.telefono" size="40" maxlength="20" disabled="${InserimentoUtenteWebForm.conferma}"></html:text></td>
				<td></td>
				<td width="20px"><bean:message key="servizi.utenti.faxRes" bundle="serviziWebLabels" /></td>
				<td> <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.fax" size="40" maxlength="20" disabled="${InserimentoUtenteWebForm.conferma}"></html:text></td>
			</tr>
			<tr>
				<td lang="100" width="20px"><bean:message key="servizi.utenti.cellulare"  bundle="serviziWebLabels"  /></td>
				<td><html:text styleId="testoNoBold" property="uteAna.anagrafe.domicilio.telefono" size="40" maxlength="20" disabled="${InserimentoUtenteWebForm.conferma}"></html:text></td>

    		</tr>
	</table>
		<em><strong><bean:message key="servizi.utenti.smsSu" bundle="serviziWebLabels" /></strong></em>
     	<br>
      		<bean:message key="servizi.utenti.smsSuFisso" bundle="serviziWebLabels" />
    		<html:radio property="uteAna.tipoSMS" value="${InserimentoUtenteWebForm.uteAna.smsSuFisso}"  disabled="${InserimentoUtenteWebForm.conferma}" />
    		<bean:message key="servizi.utenti.smsSuMobile" bundle="serviziWebLabels" />
    		<html:radio property="uteAna.tipoSMS" value="${InserimentoUtenteWebForm.uteAna.smsSuMobile}"  disabled="${InserimentoUtenteWebForm.conferma}" />
    		<bean:message key="servizi.utenti.noSms" bundle="serviziWebLabels" />
    		<html:radio property="uteAna.tipoSMS" value="${InserimentoUtenteWebForm.uteAna.noSms}"        disabled="${InserimentoUtenteWebForm.conferma}" />
    	<br/>

	   	<br>
	   		<html:submit styleClass="pulsanti" property="paraminserimentoutenteWeb">
					<bean:message key="servizi.indietro.esci" bundle="serviziWebLabels" />
			</html:submit>

   			<html:submit styleClass="pulsanti" property="paraminserimentoutenteWeb">
					<bean:message key="servizi.bottone.indietro" bundle="serviziWebLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="paraminserimentoutenteWeb">
					<bean:message key="servizi.bottone.ok" bundle="serviziWebLabels" />
			</html:submit>
		<br/>
		</c:if>
		</html:form>
	</div>
  </div>

