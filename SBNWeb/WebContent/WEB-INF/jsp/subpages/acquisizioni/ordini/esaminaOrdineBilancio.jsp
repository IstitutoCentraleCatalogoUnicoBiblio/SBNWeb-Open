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
		<bean-struts:define id="ordineAperto"  value="true"/>
	</c:when>
	</c:choose>
</c:when>
</c:choose>

<c:choose>
<c:when test="${formName eq 'esaminaOrdineForm'}">
	<bean-struts:define id="ordineAperto" value="false"/>

	<c:choose>
	<c:when test="${esaminaOrdineForm.disabilitaTutto}">
		<bean-struts:define id="ordineAperto"  value="true"/>
	</c:when>
	</c:choose>
</c:when>
</c:choose>



		<c:choose>
			<c:when test="${formName eq 'esaminaOrdineForm'}">
				<c:choose>
					<c:when test="${esaminaOrdineForm.datiOrdine.gestBil}">
					<tr >
			          <td class="etichettaIntestazione" valign="top">
			          <bean:message  key="ordine.label.bilancio" bundle="acquisizioniLabels" />
			          </td>
			          <td class="etichetta" valign="top">
			            <bean:message  key="ordine.label.esercizio" bundle="acquisizioniLabels" />
						<html:text styleId="testoNormale"  property="datiOrdine.bilancio.codice1" size="4" readonly="${ordineAperto}" ></html:text>
			    		&nbsp;&nbsp;
						<bean:message  key="ordine.label.capitolo" bundle="acquisizioniLabels" />
			   			<html:text styleId="testoNormale"   property="datiOrdine.bilancio.codice2" maxlength="16" size="16" readonly="${ordineAperto}" ></html:text>
			    		&nbsp;&nbsp;
						<bean:message  key="ordine.label.tipoImpegno" bundle="acquisizioniLabels" />
						<html:select style="width:40px" styleClass="testoNormale" property="datiOrdine.bilancio.codice3" disabled="${ordineAperto}" onchange="this.form.submitDinamico.value='true'; this.form.submit();">
						<html:optionsCollection  property="listaTipoImpegno" value="codice" label="descrizioneCodice" />
						</html:select>
			            <html:submit  styleClass="buttonImage" property="methodEsaminaOrdine" disabled="${ordineAperto}">
							<bean:message  key="ordine.label.bilancio" bundle="acquisizioniLabels" />
						</html:submit>
					   </td>
			         </tr>
					</c:when>
				</c:choose>
			</c:when>
		<c:otherwise>
			<c:choose>
			<c:when test="${formName eq 'esaminaOrdineModForm'}">
				<c:choose>
					<c:when test="${esaminaOrdineModForm.datiOrdine.gestBil}">
					<tr >
			          <td class="etichettaIntestazione" valign="top">
			          <bean:message  key="ordine.label.bilancio" bundle="acquisizioniLabels" />
			          </td>
			          <td class="etichetta" valign="top">
			          	<bean:message  key="ordine.label.esercizio" bundle="acquisizioniLabels" />
						<html:text styleId="testoNormale"  property="datiOrdine.bilancio.codice1" size="4" readonly="${ordineAperto}" ></html:text>
			    		&nbsp;&nbsp;
			          	<bean:message  key="ordine.label.capitolo" bundle="acquisizioniLabels" />
			   			<html:text styleId="testoNormale"   property="datiOrdine.bilancio.codice2" maxlength="16" size="16" readonly="${ordineAperto}" ></html:text>
			    		&nbsp;&nbsp;
				        <bean:message  key="ordine.label.tipoImpegno" bundle="acquisizioniLabels" />
						<html:select style="width:40px" styleClass="testoNormale" property="datiOrdine.bilancio.codice3" disabled="${ordineAperto}" onchange="this.form.submitDinamico.value='true'; this.form.submit();">
						<html:optionsCollection  property="listaTipoImpegno" value="codice" label="descrizioneCodice" />
						</html:select>
			            <html:submit  styleClass="buttonImage" property="methodEsaminaOrdineMod" disabled="${ordineAperto}">
							<bean:message  key="ordine.label.bilancio" bundle="acquisizioniLabels" />
						</html:submit>
					   </td>
			         </tr>
					</c:when>
				</c:choose>
			</c:when>
			</c:choose>
		</c:otherwise>
		</c:choose>




