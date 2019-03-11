<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>


<div id="divForm">
<div id="divMessaggio"><sbn:errors	bundle="documentoFisicoMessages" /></div>
<table width="100%">
	<tr>
		<td><bean:message key="documentofisico.bibliotecaT"
			bundle="documentoFisicoLabels" /> <html:text
				disabled="${modelliEtichetteGestioneForm.disableBib}"
			styleId="testoNormale" property="codBib" size="3" maxlength="3"></html:text><bean-struts:write
							name="modelliEtichetteGestioneForm" property="descrBib" /></td>
	</tr>
</table>
<table width="100%" >
	<tr>
		<td  width="15%"><bean:message key="documentofisico.descrizioneBibT"
			bundle="documentoFisicoLabels" /></td><td> <html:text
			disabled="${modelliEtichetteGestioneForm.disable}"
			styleId="testoNormale" property="descrBibEtichetta" size="90" maxlength="80"></html:text></td>
	</tr>
	<tr>
		<td><bean:message key="documentofisico.nomeModelloT"
			bundle="documentoFisicoLabels" /> </td><td><html:text
			disabled="${modelliEtichetteGestioneForm.disableCodModello}"
			styleId="testoNormale" property="codModello" size="50" maxlength="30"></html:text></td>
	</tr>
	<tr>
		<td><bean:message key="documentofisico.descrizioneModelloT"
			bundle="documentoFisicoLabels" /> </td><td><html:text
			disabled="${modelliEtichetteGestioneForm.disable}"
			styleId="testoNormale" property="descrModello" size="80" maxlength="50"></html:text></td>
	</tr>
</table>
<HR>
<jsp:include flush="true"
	page="/WEB-INF/jsp/subpages/documentofisico/modelliEtichette/layoutPagina.jsp" />
<HR>
<jsp:include flush="true"
	page="/WEB-INF/jsp/subpages/documentofisico/modelliEtichette/layoutEtichetta.jsp" />
<HR>
<jsp:include flush="true"
	page="/WEB-INF/jsp/subpages/documentofisico/modelliEtichette/layoutCampi.jsp" />
<HR>
<jsp:include flush="true"
	page="/WEB-INF/jsp/subpages/documentofisico/modelliEtichette/layoutImmagini.jsp" />
<HR>
</div>

<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td --> <c:choose>
	<c:when test="${modelliEtichetteGestioneForm.conferma}">
		<table align="center">
			<tr>
				<td><html:submit styleClass="pulsanti"
					property="methodModelliGest">
					<bean:message key="documentofisico.bottone.si"
						bundle="documentoFisicoLabels" />
				</html:submit> <html:submit styleClass="pulsanti" property="methodModelliGest">
					<bean:message key="documentofisico.bottone.no"
						bundle="documentoFisicoLabels" />
				</html:submit></td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table align="center">
			<tr>
				<td><c:choose>
					<c:when test="${modelliEtichetteGestioneForm.esamina}">
						<html:submit styleClass="pulsanti" property="methodModelliGest">
							<bean:message key="documentofisico.bottone.indietro"
								bundle="documentoFisicoLabels" />
						</html:submit>
					</c:when>
					<c:otherwise>
						<html:submit styleClass="pulsanti" property="methodModelliGest">
							<bean:message key="documentofisico.bottone.indietro"
								bundle="documentoFisicoLabels" />
						</html:submit>
						<html:submit styleClass="pulsanti"
							disabled="sezioniCollocazioniGestioneForm.disable"
							property="methodModelliGest">
							<bean:message key="documentofisico.bottone.salva"
								bundle="documentoFisicoLabels" />
						</html:submit>
					</c:otherwise>
				</c:choose></td>
			</tr>
		</table>
	</c:otherwise>
</c:choose></div>
