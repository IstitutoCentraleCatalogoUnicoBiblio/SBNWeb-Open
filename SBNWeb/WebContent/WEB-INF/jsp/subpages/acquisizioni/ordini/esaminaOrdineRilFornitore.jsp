<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="formName" name="org.apache.struts.action.mapping.instance" property="name" type="java.lang.String" />


<c:choose>
<c:when test="${formName eq 'esaminaOrdineModForm'}">
	<bean-struts:define id="noinput"  value="false"/>
	<c:choose>
	<c:when test="${esaminaOrdineModForm.ordineApertoAbilitaInput}">
		<bean-struts:define id="noinput"  value="true"/>
	</c:when>
	</c:choose>

	<c:choose>
	<c:when test="${esaminaOrdineModForm.disabilitaTutto}">
		<bean-struts:define id="noinput"  value="true"/>
	</c:when>
	</c:choose>

</c:when>
</c:choose>

<c:choose>
<c:when test="${formName eq 'esaminaOrdineForm'}">
	<bean-struts:define id="noinput"  value="false"/>
	<c:choose>
	<c:when test="${esaminaOrdineForm.disabilitaTutto}">
		<bean-struts:define id="noinput"  value="true"/>
	</c:when>
	</c:choose>

</c:when>
</c:choose>


        <tr  >
	    <td class="etichetta" valign="top" ><bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" /></td>
        <td   scope="col" align="left" valign="top" >
 		  <html:text styleId="testoNormale" property="datiOrdine.fornitore.codice" size="5"  maxlength="10" readonly="${noinput}"></html:text>
 		  <html:text styleId="testoNormale"   property="datiOrdine.fornitore.descrizione" size="50"  maxlength="50" readonly="${noinput}"></html:text>
		<c:choose>
		<c:when test="${formName eq 'esaminaOrdineModForm'}">
	        <html:submit  styleClass="buttonImage" property="methodEsaminaOrdineMod" disabled="${noinput}">
				<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
			</html:submit>
		</c:when>
		<c:otherwise>
	        <html:submit  styleClass="buttonImage" property="methodEsaminaOrdine"  disabled="${noinput}">
				<bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
			</html:submit>
		</c:otherwise>
		</c:choose>
          </td>
        </tr>
