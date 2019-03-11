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

        <tr  >
          <td class="etichetta"><bean:message  key="ordine.label.noteForn" bundle="acquisizioniLabels" /></td>
          <td >
			<html:textarea styleId="testoNormale"  property="datiOrdine.noteFornitore" rows="1" cols="70" readonly="${ordineAperto}"  ></html:textarea>
			<c:choose>
			<c:when test="${formName eq 'esaminaOrdineForm'}">
				<c:choose>
				<c:when test="${!esaminaOrdineForm.disabilitaTutto}">
					<sbn:tastiera limit="80" property="datiOrdine.noteFornitore" name="esaminaOrdineForm"></sbn:tastiera>
				</c:when>
				</c:choose>
			</c:when>
			<c:otherwise>
				<c:choose>
				<c:when test="${formName eq 'esaminaOrdineModForm'}">
					<c:choose>
					<c:when test="${!esaminaOrdineModForm.ordineApertoAbilitaInput &&  !esaminaOrdineModForm.disabilitaTutto}">
						<sbn:tastiera limit="80" property="datiOrdine.noteFornitore" name="esaminaOrdineModForm"></sbn:tastiera>
					</c:when>
					</c:choose>
				</c:when>
				</c:choose>
			</c:otherwise>
			</c:choose>
		  </td>
        </tr>
