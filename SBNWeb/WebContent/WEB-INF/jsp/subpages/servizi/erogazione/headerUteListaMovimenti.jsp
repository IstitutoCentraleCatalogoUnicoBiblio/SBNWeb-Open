<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<!-- Biblioteca operante -->
<bean:message key="servizi.utenti.bibliotecaServizio" bundle="serviziLabels" />&nbsp;&nbsp;
<c:choose>
   	<c:when test="${navForm.conferma}">
		<html:text styleId="testoNoBold" property="movRicerca.codBibOperante" size="5" readonly="${navForm.conferma}"></html:text>
    </c:when>
	<c:otherwise>
		<html:text styleId="testoNoBold" property="movRicerca.codBibOperante" size="5" readonly="${!navForm.mov.nuovoMov}"></html:text>
	</c:otherwise>
</c:choose>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<!-- Biblioteca utente -->
<bean:message key="servizi.utenti.bibliotecaUtente" bundle="serviziLabels" />&nbsp;&nbsp;
<html:text styleId="testoNoBold" property="utenteVO.codBib" size="5" readonly="true"></html:text>
<br/>
<br/>

<!-- Codice utente -->
<bean:message key="servizi.utenti.codUtente" bundle="serviziLabels" />&nbsp;&nbsp;
<html:text styleId="testoNoBold" property="utenteVO.codUtente" size="12" readonly="true"></html:text>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<!-- Cognome e Nome -->
<bean:message key="servizi.utenti.headerCognomeNome" bundle="serviziLabels" />&nbsp;&nbsp;
<html:text styleId="testoNoBold" property="utenteVO.cognomeNome" size="35" readonly="true"></html:text>
&nbsp;<html:submit styleClass="buttonImage" property="methodListaMovimentiUte" bundle="serviziLabels" disabled="${navForm.conferma or navForm.mov.nuovoMov}">
<bean:message key="servizi.bottone.esame.utente" bundle="serviziLabels" />
</html:submit>
<br/>

