<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<br/>
<br/>
<br/>
<div class="etichetta">
	<strong><bean:message key="servizi.utenti.biblioteca" bundle="serviziLabels" /></strong>
</div>
<sbn:disableAll checkAttivita="GESTIONE">
<table border="0">
     <tr>
        <td class="etichetta" width="120px">
           <bean:message key="servizi.utenti.biblio.credito" bundle="serviziLabels" />
        </td>
        <td >
           <html:text styleId="testoNoBold" property="uteAna.bibliopolo.biblioCredito" size="10" disabled="${navForm.conferma}"></html:text>
        </td>
        <td width="40px">&nbsp;</td>
        <td class="etichetta"  width="100px">
           <bean:message key="servizi.utenti.biblio.note" bundle="serviziLabels" />
        </td>
        <td >
         <html:textarea styleId="testoNoBold" property="uteAna.bibliopolo.biblioNote" cols="50" rows="5" disabled="${navForm.conferma}" />
        </td>
     </tr>
</table>
<br/>
<br/>

<div class="etichetta">
    <strong><bean:message key="servizi.utenti.polo" bundle="serviziLabels" /></strong>
</div>
<table border="0">
     <tr>
        <td class="etichetta" align="right" width="120px">
           <bean:message key="servizi.utenti.polo.credito" bundle="serviziLabels" />
        </td>
        <td align="left">
           <html:text styleId="testoNoBold" property="uteAna.bibliopolo.poloCredito" size="10" disabled="${navForm.conferma}"></html:text>
        </td>
        <td width="40px">&nbsp;</td>
        <td class="etichetta" align="right" width="100px">
           <bean:message key="servizi.utenti.polo.note" bundle="serviziLabels" />
        </td>
        <td>
        	<html:textarea styleId="testoNoBold" property="uteAna.bibliopolo.poloNote"  cols="50" rows="2" disabled="${navForm.conferma}"></html:textarea>
        </td>
     </tr>

     <tr>
        <td class="etichetta" align="right" width="120px">
           <bean:message key="servizi.utenti.biblio.polo.data.registarzione" bundle="serviziLabels" />
        </td>
        <td align="left">
        	<html:text styleId="testoNoBold" property="uteAna.bibliopolo.poloDataRegistrazione" size="10" disabled="${navForm.conferma}" readonly="true"></html:text>
        </td>
        <td width="40px">&nbsp;</td>
        <td class="etichetta" width="100px">
           <bean:message key="servizi.utenti.polo.infrazioni" bundle="serviziLabels" />
        </td>
        <td>
        	<html:textarea styleId="testoNoBold" property="uteAna.bibliopolo.poloInfrazioni" cols="50" rows="2" disabled="${navForm.conferma}">></html:textarea>
        </td>
     </tr>
</table>
</sbn:disableAll>