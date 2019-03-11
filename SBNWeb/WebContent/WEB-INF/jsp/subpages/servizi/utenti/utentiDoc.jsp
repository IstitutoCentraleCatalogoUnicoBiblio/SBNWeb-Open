<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<br/>
<table width="80%" border="0">
	<tr>
		<th width="40%" class="etichetta" style="text-align:center;" scope="col"  bgcolor="#dde8f0">
		 <bean:message key="servizi.utenti.headerDocumento" bundle="serviziLabels" />
		</th>
		<th width="20%" class="etichetta" style="text-align:center;" scope="col" bgcolor="#dde8f0">
		 <bean:message key="servizi.utenti.headerNumero" bundle="serviziLabels" />
		</th>
		<th width="40%" class="etichetta" style="text-align:center;" scope="col" bgcolor="#dde8f0">
		 <bean:message key="servizi.utenti.headerAutRilascio" bundle="serviziLabels" />
	    </th>
	</tr>
	<c:forEach var="i" begin="0" end="3" varStatus="status">
	  <c:set var="color">
		<c:choose>
	        <c:when test='${color == "#FEF1E2"}'>
	            #FFCC99
	        </c:when>
	        <c:otherwise>
				#FEF1E2
	        </c:otherwise>
	    </c:choose>
	  </c:set>
	  <tr>
		<td bgcolor="${color}" class="testoNoBold">
	           <html:select property="uteAna.documento[${i}].documento" disabled="${DettaglioUtentiAnaForm.conferma}">
			   <html:optionsCollection property="elencoDocumenti" value="codice" label="descrizione" />
			   </html:select>
		</td>
		<td bgcolor="${color}" class="testoNoBold">
	           <html:text styleId="testoNoBold" property="uteAna.documento[${i}].numero" size="25" disabled="${DettaglioUtentiAnaForm.conferma}"></html:text>
		</td>
		<td bgcolor="${color}" class="testoNoBold">
	           <html:text styleId="testoNoBold" property="uteAna.documento[${i}].autRilascio" size="40" disabled="${DettaglioUtentiAnaForm.conferma}"></html:text>
		</td>
	  </tr>
	</c:forEach>
</table>
<br/>