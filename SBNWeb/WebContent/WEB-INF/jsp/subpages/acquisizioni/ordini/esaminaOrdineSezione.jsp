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

			<c:choose>
				<c:when test="${formName eq 'esaminaOrdineForm'}">
					<c:choose>
						<c:when test="${esaminaOrdineForm.datiOrdine.gestSez}">
				          <td class="etichetta" valign="top"><bean:message  key="ordine.label.sezione" bundle="acquisizioniLabels" /></td>
				          <td scope="col"  valign="top" align="left">
							<html:text styleId="testoNormale"  property="datiOrdine.sezioneAcqOrdine" size="7" maxlength="7" readonly="${ordineAperto}"  ></html:text>
					        <html:submit  styleClass="buttonImageListaSezione" property="methodEsaminaOrdine"  disabled="${ordineAperto}">
									<bean:message  key="ricerca.label.sezione" bundle="acquisizioniLabels" />
							</html:submit>
				    		&nbsp;&nbsp;
					        <bean:message  key="ordine.label.regTrib" bundle="acquisizioniLabels" />
							<html:text styleId="testoNormale"  property="datiOrdine.regTribOrdine"  size="10" maxlength="50" readonly="${ordineAperto}"   ></html:text>
				    		&nbsp;&nbsp;
					        <bean:message  key="ordine.label.numCopie" bundle="acquisizioniLabels" />
							<html:text styleId="testoNormale"   property="datiOrdine.numCopieOrdine"  size="2" maxlength="2" readonly="${ordineAperto}"  ></html:text>
				 		 </td>
						</c:when>
						<c:otherwise>
				          <td class="etichetta" valign="top"><bean:message  key="ordine.label.regTrib" bundle="acquisizioniLabels" /></td>
				          <td scope="col"  valign="top" align="left">
							<html:text styleId="testoNormale"  property="datiOrdine.regTribOrdine"  size="10" maxlength="50" readonly="${ordineAperto}"   ></html:text>
				    		&nbsp;&nbsp;
					        <bean:message  key="ordine.label.numCopie" bundle="acquisizioniLabels" />
							<html:text styleId="testoNormale"   property="datiOrdine.numCopieOrdine"  size="2" maxlength="2" readonly="${ordineAperto}"  ></html:text>
				 		 </td>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${formName eq 'esaminaOrdineModForm'}">
						<c:choose>
							<c:when test="${esaminaOrdineModForm.datiOrdine.gestSez}">
					          <td class="etichetta" valign="top"><bean:message  key="ordine.label.sezione" bundle="acquisizioniLabels" /></td>
					          <td scope="col"  valign="top" align="left">
								<html:text styleId="testoNormale"  property="datiOrdine.sezioneAcqOrdine" size="7" maxlength="7" readonly="${ordineAperto}"  ></html:text>
						        <html:submit  styleClass="buttonImageListaSezione" property="methodEsaminaOrdineMod" disabled="${ordineAperto}">
										<bean:message  key="ricerca.label.sezione" bundle="acquisizioniLabels" />
								</html:submit>
					    		&nbsp;&nbsp;
						        <bean:message  key="ordine.label.regTrib" bundle="acquisizioniLabels" />
								<html:text styleId="testoNormale"  property="datiOrdine.regTribOrdine"  size="10" maxlength="50" readonly="${ordineAperto}"   ></html:text>
					    		&nbsp;&nbsp;
						        <bean:message  key="ordine.label.numCopie" bundle="acquisizioniLabels" />
								<html:text styleId="testoNormale"   property="datiOrdine.numCopieOrdine"  size="2" maxlength="2" readonly="${ordineAperto}"  ></html:text>
				 		 </td>
						</c:when>
						<c:otherwise>
				          <td class="etichetta" valign="top"><bean:message  key="ordine.label.regTrib" bundle="acquisizioniLabels" /></td>
				          <td scope="col" valign="top" align="left">
								<html:text styleId="testoNormale"  property="datiOrdine.regTribOrdine"  size="10" maxlength="50" readonly="${ordineAperto}"   ></html:text>
					    		&nbsp;&nbsp;
						        <bean:message  key="ordine.label.numCopie" bundle="acquisizioniLabels" />
								<html:text styleId="testoNormale"   property="datiOrdine.numCopieOrdine"  size="2" maxlength="2" readonly="${ordineAperto}"  ></html:text>
				 		 </td>
						</c:otherwise>

						</c:choose>
					</c:when>
					</c:choose>
				</c:otherwise>
			</c:choose>

        </tr>
