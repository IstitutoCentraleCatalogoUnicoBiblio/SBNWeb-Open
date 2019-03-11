<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<div style="width:100%">
	<div class="etichetta" style="float:left; font-weight:bold; width:80px;">
		<bean:message key="servizi.utenti.biblioteca" bundle="serviziLabels" />
	</div>
	<div style="float:left; width:90px;">
		<c:choose>
		    	<c:when test="${ErogazioneRicercaForm.conferma}">
                	<html:text styleId="testoNoBold" property="anaMov.codBibOperante"
						size="5" disabled="${ErogazioneRicercaForm.conferma}"></html:text>
			    </c:when>
				<c:otherwise>
          	       <html:text styleId="testoNoBold" property="anaMov.codBibOperante"
                	size="5" disabled="${!ErogazioneRicercaForm.anaMov.nuovoMov}"></html:text>
    			</c:otherwise>
	    	</c:choose>
	</div>
	<div style="float:none;">
		<html:submit property="methodErogazione">
			<bean:message key="servizi.bottone.cambioBiblioteca" bundle="serviziLabels" />
		</html:submit>
	</div>
</div>

<!--
	<table width="100%" border="0">
    	<tr>
        	<td width="24%" align="right" class="etichetta"><strong>
                 <bean:message key="servizi.utenti.bibliotecaServizio" bundle="serviziLabels" />
          	</strong></td>
 			<c:choose>
		    	<c:when test="${ErogazioneRicercaForm.conferma}">
				<td>
                	<html:text styleId="testoNoBold" property="anaMov.codBibOperante"
						size="5" disabled="${ErogazioneRicercaForm.conferma}"></html:text>
				</td>
			    </c:when>
				<c:otherwise>
				<td>
          	       <html:text styleId="testoNoBold" property="anaMov.codBibOperante"
                	size="5" disabled="${!ErogazioneRicercaForm.anaMov.nuovoMov}"></html:text>
				</td>
    			</c:otherwise>
	    	</c:choose>
	   	</tr>
    </table>

  -->