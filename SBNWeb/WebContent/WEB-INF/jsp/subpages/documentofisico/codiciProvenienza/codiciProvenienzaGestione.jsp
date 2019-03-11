<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>


<div id="divForm">
<div id="divMessaggio"><sbn:errors bundle="documentoFisicoMessages" /></div>
<table width="100%">
	<tr>
		<td colspan="3">
		<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
			bundle="documentoFisicoLabels" /> <html:text disabled="true"
			styleId="testoNormale" property="codBib" size="3" maxlength="3"></html:text>
		<bean-struts:write name="codiciProvenienzaGestioneForm"
			property="descrBib" /></div>
		</td>
	</tr>
</table>
<table width="100%" border="0">
	<tr>
		<th scope="col"><div class="etichetta">
		<bean:message key="documentofisico.codiceT" bundle="documentoFisicoLabels" /></div></th>
		<td><html:text disabled="${codiciProvenienzaGestioneForm.disableBib}" styleId="testoNormale"
				property="recProvInv.codProvenienza" size="10" maxlength="5"></html:text></td>
	</tr>
	<tr>
		<th class="etichetta" scope="col">
			<bean:message key="documentofisico.descrizioneT"
					bundle="documentoFisicoLabels" /></th>
		<td><html:textarea styleId="testoNormale"  disabled="${codiciProvenienzaGestioneForm.disableDescr}"
					property="recProvInv.descrProvenienza" cols="50"></html:textarea></td>
	</tr>
</table>
<c:choose>
	<c:when test="${codiciProvenienzaGestioneForm.date==true}">
		<table border="0" align="center">
			<tr>
				<td>
				<div class="etichetta"><bean:message
					key="documentofisico.dataInserimentoT"
					bundle="documentoFisicoLabels" /></div>
				</td>
				<td>
				<div class="etichetta"><bean:message
					key="documentofisico.dataAggiornamentoT"
					bundle="documentoFisicoLabels" /></div>
				</td>
			</tr>
			<tr>
				<td>
				<div class="testoNormale"><bean-struts:write
					name="codiciProvenienzaGestioneForm"
					property="recProvInv.dataIns" /></div>
				</td>
				<td>
				<div class="testoNormale"><bean-struts:write
					name="codiciProvenienzaGestioneForm"
					property="recProvInv.dataAgg" /></div>
				</td>
			</tr>
		</table>
	</c:when>
</c:choose>
</div>
<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<c:choose>
			<c:when test="${codiciProvenienzaGestioneForm.conferma}">
				<table align="center">
					<tr>
						<td><html:submit styleClass="pulsanti" property="methodProvInvGest">
								<bean:message key="documentofisico.bottone.si" bundle="documentoFisicoLabels" /></html:submit>
							<html:submit styleClass="pulsanti" property="methodProvInvGest">
								<bean:message key="documentofisico.bottone.no" bundle="documentoFisicoLabels" /></html:submit></td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
          <table align="center">
             <tr>
              <td>
				<html:submit styleClass="pulsanti" property="methodProvInvGest">
					<bean:message key="documentofisico.bottone.indietro"
							bundle="documentoFisicoLabels" /></html:submit>
					<html:submit styleClass="pulsanti" property="methodProvInvGest">
						<bean:message key="documentofisico.bottone.salva"
							bundle="documentoFisicoLabels" /></html:submit></td>
			</tr>
		</table>
		</c:otherwise>
		</c:choose>
</div>