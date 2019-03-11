<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>


<bean:message key="servizi.utenti.bibliotecaServizio" bundle="serviziLabels" />&nbsp;&nbsp;
<c:choose>
	<c:when test="${ListaMovimentiForm.conferma}">
		<html:text styleId="testoNoBold" property="movRicerca.codBibOperante" size="5" readonly="${ListaMovimentiForm.conferma}"></html:text>
	</c:when>
	<c:otherwise>
		<html:text styleId="testoNoBold" property="movRicerca.codBibOperante" size="5" readonly="${!ListaMovimentiForm.mov.nuovoMov}"></html:text>
	</c:otherwise>
</c:choose>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<bean:message key="servizi.erogazione.bibliotecaInventario" bundle="serviziLabels" />&nbsp;&nbsp;
<html:text styleId="testoNoBold" property="infoDocumentoVO.inventarioTitoloVO.codBib" size="3" readonly="true"></html:text>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<bean:message key="servizi.erogazione.serie" bundle="serviziLabels" />&nbsp;&nbsp;
<html:text styleId="testoNoBold" property="infoDocumentoVO.inventarioTitoloVO.codSerie" size="3" readonly="true"></html:text>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<bean:message key="servizi.erogazione.inventario" bundle="serviziLabels" />&nbsp;&nbsp;
<html:text styleId="testoNoBold" property="infoDocumentoVO.inventarioTitoloVO.codInvent" size="9" readonly="true"></html:text>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<br/>
<br/>

<bean:message key="servizi.erog.Segnatura" bundle="serviziLabels" />&nbsp;&nbsp;
<html:text styleId="testoNoBold" property="infoDocumentoVO.segnatura" size="70" readonly="true"></html:text>

<br/>
<br/>

<bean:message key="servizi.erogazione.titolo" bundle="serviziLabels" />&nbsp;&nbsp;
<html:text styleId="testoNoBold" property="infoDocumentoVO.titolo" size="100" readonly="true"></html:text>

<br/>

