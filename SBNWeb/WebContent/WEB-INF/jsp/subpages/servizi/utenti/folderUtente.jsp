<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<sbn:disableAll disabled="${navForm.tipoUtente eq ''}">
    <table width="100%"  border="0" class="SchedaImg1" >
    <tr>
     <c:choose>
	  <c:when test="${navForm.folder eq 'A '}">
        <td  width="86" class="schedaOn" align="center">
			<div align="center">
				<bean:message key="servizi.utenti.tag.anagrafica" bundle="serviziLabels" />
			</div>
        </td>
	  </c:when>
	  <c:otherwise>
        <td  width="86" class="schedaOff" align="center">
	    	<html:submit property="methodDettaglio" disabled="${navForm.conferma}" styleClass="sintButtonLinkDefault">
    	    	<bean:message key="servizi.utenti.tag.anagrafica" bundle="serviziLabels" />
			</html:submit>
        </td>
	  </c:otherwise>
     </c:choose>
     <c:choose>
	  <c:when test="${navForm.folder eq 'U '}">
        <td  width="86" class="schedaOn" align="center">
			<div align="center">
				<bean:message key="servizi.utenti.tag.autorizzazioni" bundle="serviziLabels" />
			</div>
        </td>
	  </c:when>
	  <c:otherwise>
        <td  width="86" class="schedaOff" align="center">
	    	<html:submit property="methodDettaglio" disabled="${navForm.conferma}" styleClass="sintButtonLinkDefault">
    	    	<bean:message key="servizi.utenti.tag.autorizzazioni" bundle="serviziLabels" />
			</html:submit>
        </td>
	  </c:otherwise>
     </c:choose>
     <c:choose>
      <c:when test="${navForm.folder eq 'B '}">
        <td  width="86" class="schedaOn" align="center">
			<div align="center">
				<bean:message key="servizi.utenti.tag.bibliotecaPolo" bundle="serviziLabels" />
			</div>
        </td>
	  </c:when>
	  <c:otherwise>
        <td  width="86" class="schedaOff" align="center">
	    	<html:submit property="methodDettaglio" disabled="${navForm.conferma}" styleClass="sintButtonLinkDefault">
    	    	<bean:message key="servizi.utenti.tag.bibliotecaPolo" bundle="serviziLabels" />
			</html:submit>
        </td>
	  </c:otherwise>
     </c:choose>
     </tr>
    </table>
  </sbn:disableAll>
