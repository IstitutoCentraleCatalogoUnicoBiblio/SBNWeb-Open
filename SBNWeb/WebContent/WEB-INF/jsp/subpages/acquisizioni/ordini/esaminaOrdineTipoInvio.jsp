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
	<bean-struts:define id="ordineAperto" value="false"/>
	<c:choose>
	<c:when test="${esaminaOrdineModForm.ordineApertoAbilitaInput}">
		<bean-struts:define id="ordineAperto" value="true"/>
	</c:when>
	</c:choose>
	<c:choose>
	<c:when test="${esaminaOrdineModForm.disabilitaTutto}">
		<bean-struts:define id="ordineAperto" value="true"/>
	</c:when>
	</c:choose>

</c:when>
</c:choose>

<c:choose>
<c:when test="${formName eq 'esaminaOrdineForm'}">
	<bean-struts:define id="ordineAperto" value="false"/>
	<c:choose>
	<c:when test="${esaminaOrdineForm.disabilitaTutto}">
		<bean-struts:define id="ordineAperto" value="true"/>
	</c:when>
	</c:choose>

</c:when>
</c:choose>
        <tr>
          <td class="etichetta"><bean:message  key="ordine.label.tipoInvio" bundle="acquisizioniLabels" />
          </td>
          <td>
        	<html:select  styleClass="testoNormale" property="datiOrdine.tipoInvioOrdine" disabled="${ordineAperto}" >
			<html:optionsCollection  property="listaTipoInvio" value="codice" label="descrizioneCodice" />
			</html:select>
	        &nbsp;&nbsp;
	        <bean:message  key="ordine.label.urg" bundle="acquisizioniLabels" />
        	<html:select  styleClass="testoNormale" property="datiOrdine.codUrgenzaOrdine" disabled="${ordineAperto}" >
			<html:optionsCollection  property="listaUrg" value="codice" label="descrizioneCodice" />
			</html:select>
			&nbsp;&nbsp;
            <bean:message  key="ordine.label.proven" bundle="acquisizioniLabels" />
		    <html:text styleId="testoNormale"   property="datiOrdine.provenienza" size="25" readonly="true"></html:text>
          </td>
        </tr>
