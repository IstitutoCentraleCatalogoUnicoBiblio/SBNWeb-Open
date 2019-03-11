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
		<bean-struts:define id="ordineAperto" value="false" />
		<c:choose>
			<c:when test="${esaminaOrdineModForm.ordineApertoAbilitaInput}">
				<bean-struts:define id="ordineAperto" value="true" />
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${esaminaOrdineModForm.disabilitaTutto}">
				<bean-struts:define id="ordineAperto" value="true" />
			</c:when>
		</c:choose>
	</c:when>
</c:choose>
<c:choose>
	<c:when test="${formName eq 'esaminaOrdineForm'}">
		<bean-struts:define id="ordineAperto" value="false" />
		<c:choose>
			<c:when test="${esaminaOrdineForm.disabilitaTutto}">
				<bean-struts:define id="ordineAperto" value="true" />
			</c:when>
		</c:choose>
	</c:when>
</c:choose>
<tr>
	<td class="etichetta"><bean:message key="ordine.label.prezzo"
			bundle="acquisizioniLabels" />
	</td>
	<td><html:text styleId="testoNormale" property="prezzoStr"
			size="10" readonly="${ordineAperto}"></html:text> <html:select
			style="width:50px" styleClass="testoNormale" property="valuta"
			disabled="${ordineAperto}">
			<html:optionsCollection property="listaValuta" value="codice1"
				label="codice2" />
		</html:select> &nbsp;&nbsp; <bean:message key="ordine.label.prezzoInValRif" arg0="${navForm.valutaRif.desValuta}"
			bundle="acquisizioniLabels" /> <html:text styleId="testoNormale"
			property="prezzoEurStr" size="10" readonly="true"></html:text>
	</td>
</tr>
