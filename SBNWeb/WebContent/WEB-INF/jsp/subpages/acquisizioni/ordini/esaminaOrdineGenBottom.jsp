<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="bibaff">
	<bean-struts:write name="esaminaOrdineForm"
		property="biblioNONCentroSistema" />
</bean-struts:define>
<!-- tabella bottoni -->

<table align="center" border="0" style="height: 40px;" cellspacing="0"
	cellpadding="0">
	<tr>
		<sbn:checkAttivita idControllo="VALUTA">
			<td><html:submit styleClass="pulsanti"
					property="methodEsaminaOrdine">
					<bean:message key="ricerca.button.salva"
						bundle="acquisizioniLabels" />
				</html:submit> <html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
					<bean:message key="ricerca.button.ripristina"
						bundle="acquisizioniLabels" />
				</html:submit> <c:choose>
					<c:when test="${!esaminaOrdineForm.biblioNONCentroSistema}">
						<html:submit styleClass="pulsanti" property="methodEsaminaOrdine"
							disabled="${bibaff}">
							<bean:message key="ricerca.button.bibloaffil"
								bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
				</c:choose> <!--
			<logic:equal  name="esaminaOrdineForm" property="scegliTAB" value="R">
				<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
					<bean:message key="crea.button.visualizzInventari" bundle="acquisizioniLabels" />
				</html:submit>
			</logic:equal>
			--> <logic:equal name="esaminaOrdineForm" property="scegliTAB"
					value="R">
					<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
						<bean:message key="crea.button.associaInventari"
							bundle="acquisizioniLabels" />
					</html:submit>
				</logic:equal> <logic:notEqual name="esaminaOrdineForm" property="scegliTAB"
					value="R">
					<c:choose>
						<c:when test="${esaminaOrdineForm.datiOrdine.codOrdine eq ''}">
							<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
								<bean:message key="crea.button.importaDa"
									bundle="acquisizioniLabels" />
							</html:submit>
						</c:when>
					</c:choose>

				</logic:notEqual> <!--
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
				<bean:message key="ricerca.button.stampa" bundle="acquisizioniLabels" />
			</html:submit>
			--> <logic:equal name="esaminaOrdineForm" property="scegliTAB"
					value="A">
					<c:choose>
						<c:when test="${esaminaOrdineForm.datiOrdine.gestProf}">
							<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
								<bean:message key="ricerca.button.fornitoriProfili"
									bundle="acquisizioniLabels" />
							</html:submit>
						</c:when>
					</c:choose>
				</logic:equal> <html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
					<bean:message key="ricerca.button.indietro"
						bundle="acquisizioniLabels" />
				</html:submit></td>
		</sbn:checkAttivita>

		<sbn:checkAttivita idControllo="VALUTA" inverted="true">
			<td><html:submit styleClass="pulsanti"
					property="methodEsaminaOrdine">
					<bean:message key="ricerca.button.indietro"
						bundle="acquisizioniLabels" />
				</html:submit></td>
		</sbn:checkAttivita>

		<sbn:checkAttivita idControllo="VALUTA">
			<td><logic:equal name="esaminaOrdineForm"
					property="visibilitaIndietroLS" value="true">
					<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
						<bean:message key="ricerca.button.scegli"
							bundle="acquisizioniLabels" />
					</html:submit>
				</logic:equal></td>
		</sbn:checkAttivita>

	</tr>
</table>
<!-- fine tabella bottoni -->


