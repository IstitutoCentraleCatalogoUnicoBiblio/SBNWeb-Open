<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
	<table width="60%" border="0">
    	<tr>
        	<td width="8%" align="right" class="etichetta">
                 <bean:message key="servizi.utenti.bibliotecaServizio" bundle="serviziLabels" />
          	</td>
 			<c:choose>
		    	<c:when test="${ListaMovimentiForm.conferma}">
					<td>
	                	<html:text styleId="testoNoBold" property="movRicerca.codBibOperante"
							size="5" disabled="${ListaMovimentiForm.conferma}"></html:text>
					</td>
			    </c:when>
				<c:otherwise>
					<td>
	          	       <html:text styleId="testoNoBold" property="movRicerca.codBibOperante"
	                	size="5" disabled="${!ListaMovimentiForm.mov.nuovoMov}"></html:text>
					</td>
    			</c:otherwise>
	    	</c:choose>
        	<td width="8%" align="right" class="etichetta">
            	<bean:message key="servizi.erogazione.bibDocLet" bundle="serviziLabels" />
          	</td>
			<td>
       	    	<html:text styleId="testoNoBold" property="infoDocumentoVO.documentoNonSbnVO.codBib"
               		size="5" disabled="true"></html:text>
			</td>
        	<td width="8%" align="right" class="etichetta">
            	<bean:message key="servizi.erogazione.tipoDocLet" bundle="serviziLabels" />
          	</td>
			<td>
       	    	<html:text styleId="testoNoBold" property="infoDocumentoVO.documentoNonSbnVO.tipo_doc_lett"
               		size="5" disabled="true"></html:text>
			</td>
        	<td width="8%" align="right" class="etichetta">
            	<bean:message key="servizi.erogazione.codDocLet" bundle="serviziLabels" />
          	</td>
			<td>
       	    	<html:text styleId="testoNoBold" property="infoDocumentoVO.documentoNonSbnVO.cod_doc_lett"
               		size="5" disabled="true"></html:text>
			</td>
		</tr>
		<tr>
	   		<td  colspan="1" width="8%" align="right" class="etichetta">
				<bean:message key="servizi.erog.Segnatura"
							bundle="serviziLabels" />
			</td>
	   		<td colspan="7" width="8%" align="right" class="etichetta">
				<html:text styleId="testoNoBold" property="infoDocumentoVO.documentoNonSbnVO.segnatura"
				size="70" disabled="true"></html:text>
			</td>
	   	</tr>
		<tr>
	   		<td colspan="1" width="8%" align="right" class="etichetta">
				<bean:message key="servizi.erogazione.titolo"
							bundle="serviziLabels" />
			</td>
	   		<td  colspan="5" width="8%" align="right" class="etichetta">
				<html:text styleId="testoNoBold" property="infoDocumentoVO.documentoNonSbnVO.titolo"
				size="100" disabled="true"></html:text>
			</td>
	   	</tr>
    </table>
