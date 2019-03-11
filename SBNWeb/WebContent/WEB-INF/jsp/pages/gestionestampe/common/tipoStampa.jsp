<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<bean-struts:define id="formName" name="org.apache.struts.action.mapping.instance" property="name" type="java.lang.String" />

<html:xhtml />
<table width="100%" border="0">
	<tr>
		<td valign="top" width="20%" scope="col">
			<div align="left" class="etichetta">
				<bean:message key="stampa.formato.messaggio"
					bundle="gestioneStampeLabels" />
			</div>
		</td>
		<c:choose>
			<c:when test="${formName eq 'stampaServiziForm'}">
				<td><html:radio styleId="radioFormato" property="tipoFormato"
						value="XLS" /> <label for="radioFormato"><bean:message
							key="stampa.formato.xls" bundle="gestioneStampeLabels" /> </label></td>
			</c:when>
			<c:when test="${formName eq 'stampaListaFascicoliForm'}">
				<td><html:radio styleId="radioFormato" property="tipoFormato"
						value="XLS" /> <label for="radioFormato"><bean:message
							key="stampa.formato.xls" bundle="gestioneStampeLabels" /> </label></td>
			</c:when>
			<%--<c:when test="${(formName eq 'stampaEtichetteForm') or (stampaOnLineForm.tipoStampa eq 'STAMPA_ETICHETTE')}">
				<td><html:radio styleId="radioFormato" property="tipoFormato"
						value="PDF" /> <label for="radioFormato"><bean:message
							key="stampa.formato.pdf" bundle="gestioneStampeLabels" /> </label></td>
			</c:when>--%>
			<c:when test="${(StampaOnLineForm.tipoStampa eq 'STAMPA_ETICHETTE') && (StampaOnLineForm.modalita eq 'modBarCode')}">
				<td><html:radio styleId="radioFormato" property="tipoFormato"
						value="PDF" /> <label for="radioFormato"><bean:message
							key="stampa.formato.pdf" bundle="gestioneStampeLabels" /> </label>
				</td>
			</c:when>
			<c:when test="${formName eq 'stampaRegistroConservazioneForm'}">
				<td><html:radio styleId="radioFormato" property="tipoFormato"
						value="PDF" /> <label for="radioFormato"><bean:message
							key="stampa.formato.pdf" bundle="gestioneStampeLabels" /> </label></td>
			</c:when>
			<c:when test="${formName eq 'stampaRegistroTopograficoForm'}">
				<td><html:radio styleId="radioFormato" property="tipoFormato"
						value="PDF" /> <label for="radioFormato"><bean:message
							key="stampa.formato.pdf" bundle="gestioneStampeLabels" /> </label></td>
			</c:when>

			<c:when
				test="${(formName eq 'stampaOrdiniForm') or (StampaOnLineForm.tipoStampa eq 'STAMPA_BUONI_ORDINE')}">
				<td><html:radio styleId="radioFormato" property="tipoFormato"
						value="PDF" /> <label for="radioFormato"><bean:message
							key="stampa.formato.pdf" bundle="gestioneStampeLabels" /> </label></td>
				<td><html:radio styleId="radioFormato" property="tipoFormato"
						value="RTF" /> <label for="radioFormato"><bean:message
							key="stampa.formato.rtf" bundle="gestioneStampeLabels" /> </label></td>
			</c:when>
			<c:otherwise>
				<td><html:radio styleId="radioFormato" property="tipoFormato"
						value="PDF" /> <label for="radioFormato"><bean:message
							key="stampa.formato.pdf" bundle="gestioneStampeLabels" /> </label></td>
				<td><html:radio styleId="radioFormato" property="tipoFormato"
						value="RTF" /> <label for="radioFormato"><bean:message
							key="stampa.formato.rtf" bundle="gestioneStampeLabels" /> </label></td>
				<c:choose>
					<c:when test="${formName ne 'stampaTesserinoForm'}">
						<td><html:radio styleId="radioFormato" property="tipoFormato"
								value="HTML" /> <label for="radioFormato"><bean:message
									key="stampa.formato.html" bundle="gestioneStampeLabels" /> </label></td>
					</c:when>
				</c:choose>
				<td><html:radio styleId="radioFormato" property="tipoFormato"
						value="XLS" /> <label for="radioFormato"><bean:message
							key="stampa.formato.xls" bundle="gestioneStampeLabels" /> </label></td>
				<c:choose>
					<c:when test="${formName ne 'stampaTesserinoForm'}">
						<td><html:radio styleId="radioFormato" property="tipoFormato"
								value="CSV" /> <label for="radioFormato"><bean:message
									key="stampa.formato.csv" bundle="gestioneStampeLabels" /> </label></td>
					</c:when>
				</c:choose>
			</c:otherwise>
		</c:choose>
		<!-- FINE tabella corpo COLONNA + LARGA -->
	</tr>
</table>
