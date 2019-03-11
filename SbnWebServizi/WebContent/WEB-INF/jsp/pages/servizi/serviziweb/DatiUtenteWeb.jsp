<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/serviziweb/datiUtenteWeb.do">
	<div id="divForm">
		<div id="divMessaggio"> <sbn:errors bundle="serviziWebMessages" /></div>
	<br>

    <table style="margin-top:0"  border="0" >
    	<sbn:disableAll disabled="${navForm.modifica}">
       <tr>
       	 <td><bean:message key="servizi.utenti.cognome" bundle="serviziWebLabels" /></td>
         <td ><html:text name="navForm" property="uteAna.cognome" /></td>
         <td></td>
       	 <td><bean:message key="servizi.utenti.nome" bundle="serviziWebLabels"/></td>
          <td><html:text name="navForm" property="uteAna.nome" /></td>
       </tr>

       <tr>
         <td><bean:message key="servizi.utenti.luogoNascita" bundle="serviziWebLabels"/></td>
         <td><html:text name="navForm" property="uteAna.anagrafe.luogoNascita" /></td>
         <td></td>

         <td> <bean:message key="servizi.utenti.sesso" bundle="serviziWebLabels" /></td>
            <td><html:select name="navForm" property="uteAna.anagrafe.sesso" disabled="${navForm.conferma}">
			<html:option value=""></html:option>
			<html:option value="M" bundle="serviziWebLabels" key="servizi.utenti.sessom"></html:option>
			<html:option value="F" bundle="serviziWebLabels" key="servizi.utenti.sessof"></html:option>
			</html:select> </td>
        </tr>

        <tr>
            <td><bean:message key="servizi.utenti.dataNascita" bundle="serviziWebLabels"/></td>
            <td><html:text styleId="testoNoBold" name="navForm" property="uteAna.anagrafe.dataNascita" size="10" disabled="${navForm.conferma}" /></td>
            <td></td>
			<td><bean:message key="servizi.utenti.nazionalitaCittadinanza" bundle="serviziWebLabels" /></td>
			<td>
				<html:select name="navForm" property="uteAna.anagrafe.nazionalita" disabled="${navForm.conferma}">
					<html:optionsCollection name="navForm" property="nazCitta" value="codice" label="descrizioneCodice"/>
				</html:select>
			</td>
       	</tr>
       	</sbn:disableAll>
       <tr>
			<td>
			  <bean:message key="servizi.utenti.codiceFiscale" bundle="serviziWebLabels" />
			</td>
			<td>
			  <html:text styleId="testoNoBold" property="uteAna.anagrafe.codFiscale" size="26" maxlength="16"
			  	disabled="${navForm.conferma or (navForm.modifica and navForm.lock.lockCodFiscale) }" />
			</td>
			<td></td>
			<td width="20px"><bean:message key="servizi.utenti.email.obbligatorio" bundle="serviziWebLabels" /></td>
			<td><html:text styleId="testoNoBold" property="uteAna.anagrafe.postaElettronica" size="47" maxlength="80"
				disabled="${navForm.conferma or (navForm.modifica and navForm.lock.lockMailAddress) }" />
				<br/>
				<html:text styleId="testoNoBold" property="uteAna.anagrafe.postaElettronica2" size="47" maxlength="80"
				disabled="${navForm.conferma or (navForm.modifica and navForm.lock.lockMailAddress) }" />&nbsp;&#x00B9;
			</td>
       </tr>

    </table>
   	<br/>
	<sbn:disableAll disabled="${navForm.conferma or (navForm.modifica and navForm.lock.lockResidenza) }">
	<label>
       <em><strong><bean:message key="servizi.utenti.residenza.obbligatorio" bundle="serviziWebLabels" /></strong></em>
	</label>
    <table style="margin-top:0" border="0">
       <tr>
          <td >
             <bean:message key="servizi.utenti.indirizzoRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold">
             <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.via" size="65" maxlength="50" disabled="${navForm.conferma}" />
          </td>
          <td>
             <bean:message key="servizi.utenti.capRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold">
             <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.cap" size="5" maxlength="5" disabled="${navForm.conferma}" />
          </td>
       </tr>
       <tr>
          <td>
             <bean:message key="servizi.utenti.cittaRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold">
             <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.citta" size="65" maxlength="50" disabled="${navForm.conferma}" />
          </td>
          <td>
             <bean:message key="servizi.utenti.provRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold" >
             <html:select name="navForm" property="uteAna.anagrafe.residenza.provincia" disabled="${navForm.conferma}">
             <html:optionsCollection property="provinciaResidenza" value="codice" label="descrizioneCodice" />
             </html:select>
          </td>
       </tr>
       <tr>
          <td>
             <bean:message key="servizi.utenti.nazRes" bundle="serviziWebLabels" />
          </td>
          <td align="left">
             <html:select name="navForm" property="uteAna.anagrafe.residenza.nazionalita" disabled="${navForm.conferma}">
			 <html:optionsCollection property="nazCitta" value="codice" label="descrizioneCodice" />
		     </html:select>
          </td>
       </tr>
    </table>
    </sbn:disableAll>
   	<br/>
   	<sbn:disableAll disabled="${navForm.conferma or (navForm.modifica and navForm.lock.lockDomicilio) }">
    <label>
    	<em><strong><bean:message key="servizi.utenti.domicilio.diverso" bundle="serviziWebLabels" /></strong></em>
    </label>
    <table style="margin-top:0"  border="0">
        <tr>
          <td  width="40px" class="etichetta" align="left">
           <bean:message key="servizi.utenti.indirizzoRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold">
           <html:text styleId="testoNoBold" property="uteAna.anagrafe.domicilio.via" size="65" maxlength="50" disabled="${navForm.conferma}" />
          </td>
          <td  width="40px" class="etichetta" align="left">
           <bean:message key="servizi.utenti.capRes" bundle="serviziWebLabels" />
          </td>
          <td  class="testoNoBold">
           <html:text styleId="testoNoBold" property="uteAna.anagrafe.domicilio.cap" size="5" maxlength="5" disabled="${navForm.conferma}" />
          </td>
        </tr>
        <tr>
          <td width="40px" class="etichetta" align="left">
           <bean:message key="servizi.utenti.cittaRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold">
           <html:text styleId="testoNoBold" property="uteAna.anagrafe.domicilio.citta" size="65" maxlength="50" disabled="${navForm.conferma}" />
          </td>
          <td width="40px" class="etichetta" align="left">
           <bean:message key="servizi.utenti.provRes" bundle="serviziWebLabels" />
          </td>
          <td class="testoNoBold">
           <html:select name="navForm" property="uteAna.anagrafe.domicilio.provincia" disabled="${navForm.conferma}">
           <html:optionsCollection property="provinciaResidenza" value="codice" label="descrizioneCodice" />
           </html:select>
          </td>
        </tr>
    </table>
    </sbn:disableAll>
   	<br/>
   	<sbn:disableAll disabled="${navForm.conferma or (navForm.modifica and navForm.lock.lockTelefono) }">
    <em><strong><bean:message key="servizi.utenti.recapiti" bundle="serviziWebLabels" /></strong></em>
    <table style="margin-top:0"  border="0">
			<tr>
				<td width="20px"><bean:message key="servizi.utenti.telefonoFisso.obbligatorio" bundle="serviziWebLabels" /></td>
				<td> <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.telefono" size="40" maxlength="20" disabled="${navForm.conferma}" /></td>
				<td></td>
				<td width="20px"><bean:message key="servizi.utenti.faxRes" bundle="serviziWebLabels" /></td>
				<td> <html:text styleId="testoNoBold" property="uteAna.anagrafe.residenza.fax" size="40" maxlength="20" disabled="${navForm.conferma}" /></td>
			</tr>
			<tr>
				<td lang="100" width="20px"><bean:message key="servizi.utenti.cellulare"  bundle="serviziWebLabels"  /></td>
				<td><html:text styleId="testoNoBold" property="uteAna.anagrafe.domicilio.telefono" size="40" maxlength="20" disabled="${navForm.conferma}" /></td>

    		</tr>
	</table>
	<br/>
	<em><strong><bean:message key="servizi.utenti.smsSu" bundle="serviziWebLabels" /></strong></em>
     	<br>
      		<bean:message key="servizi.utenti.smsSuFisso" bundle="serviziWebLabels" />
    		<html:radio property="uteAna.tipoSMS" value="${navForm.uteAna.smsSuFisso}"  disabled="${navForm.conferma}" />
    		<bean:message key="servizi.utenti.smsSuMobile" bundle="serviziWebLabels" />
    		<html:radio property="uteAna.tipoSMS" value="${navForm.uteAna.smsSuMobile}"  disabled="${navForm.conferma}" />
    		<bean:message key="servizi.utenti.noSms" bundle="serviziWebLabels" />
    		<html:radio property="uteAna.tipoSMS" value="${navForm.uteAna.noSms}"        disabled="${navForm.conferma}" />
    	<br/>
    </sbn:disableAll>
    <br/>
  <div class="msgOK1">
					&#x00B9;&nbsp;
					<bean:message key="servizi.utente.mail2.info"
						bundle="serviziLabels" />
				</div>
	   	<br>
			<html:submit styleClass="pulsanti" property="paraminserimentoutenteWeb">
					<bean:message key="servizi.bottone.aggiorna" bundle="serviziWebLabels" />
			</html:submit>
		<br/>

	</div>
	</sbn:navform>
</layout:page>


