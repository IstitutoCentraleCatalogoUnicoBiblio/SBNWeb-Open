<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<br>
<br>

	<div>
		<span style="font-weight: bold;"><bean:message key="servizi.erogazione.contesto" bundle="serviziLabels" />:</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<c:choose>
			<c:when test="${ErogazioneRicercaForm.locIllTutto eq 1}">
				<bean:message key="servizi.erogazione.contesto.locale" bundle="serviziLabels" />
			</c:when>
			<c:when test="${ErogazioneRicercaForm.locIllTutto eq 2}">
				<bean:message key="servizi.erogazione.contesto.ill" bundle="serviziLabels" />
			</c:when>
			<c:when test="${ErogazioneRicercaForm.locIllTutto eq 3}">
				<bean:message key="servizi.erogazione.contesto.tutti" bundle="serviziLabels" />
			</c:when>
		</c:choose>
		<br/>
		<br/>
		<span style="font-weight: bold;"><bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />:</span>&nbsp;&nbsp;
		${ sessionScope.descrTipoServizio }
	</div>

	<br/>
	<br/>
	<br/>
	<div>
		<p style="font-weight: bold;"><bean:message key="servizi.erogazione.servizi_attivita" bundle="serviziLabels" /></p>
		<div style="float:left; width:80px">
			<bean:message key="servizi.erogazione.modErogazione" bundle="serviziLabels" />
		</div>
		<div style="float:left; width:200px">
			<html:select property="anaMov.cod_erog" style="width:160px">
				<html:optionsCollection property="tariffeErogazioneVO"
					value="codErog" label="desModErog" />
			</html:select>
		</div>
		<div style="float:left; width:80px">
			<bean:message key="servizi.erogazione.attivitaSuccessiva" bundle="serviziLabels" />
		</div>
		<div style="float:none;">
			<html:select property="anaMov.codAttivitaSucc" style="width:160px">
				<html:optionsCollection property="iterServizioVO"
					value="progrIter" label="descrizione" />
			</html:select>
		</div>
		<br/>
	</div>


