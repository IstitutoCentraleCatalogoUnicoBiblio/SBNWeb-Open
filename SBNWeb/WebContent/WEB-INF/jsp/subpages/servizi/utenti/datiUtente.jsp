<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<sbn:disableAll checkAttivita="GESTIONE">
	<c:choose>
		<c:when test="${navForm.tipoUtente eq ''}">
			<table width="100%" border="0">
				<tr>
					<td width="100px" class="etichetta" align="right"><strong>
					<bean:message key="servizi.utenti.codUtente" bundle="serviziLabels" />
					</strong></td>
					<td width="150px" align="left"><html:text
						styleId="testoNoBold" property="uteAna.codiceUtente" size="40"
						readonly="true"></html:text></td>
					<td width="100px" align="right" class="etichetta">&nbsp;</td>
					<td width="150px" align="left">&nbsp;</td>
				</tr>
				<tr>
					<td width="100px" align="right" class="etichetta">&nbsp;</td>
					<td width="150px" align="left">&nbsp;</td>
					<td width="100px" align="right" class="etichetta">&nbsp;</td>
					<td width="150px" align="left">&nbsp;</td>
				</tr>

				<tr>
					<td width="100px" class="etichetta" align="right"><strong>
					<bean:message key="servizi.utenti.tipoUte" bundle="serviziLabels" />
					</strong></td>
					<td width="150px" align="left"><html:select
						property="tipoUtente" disabled="${navForm.conferma} ">
						<html:option value="P" bundle="serviziLabels"
							key="servizi.utenti.persona"></html:option>
						<html:option value="E" bundle="serviziLabels"
							key="servizi.utenti.ente"></html:option>
					</html:select> <html:submit property="methodDettaglio"
						disabled="${navForm.conferma}">
						<bean:message key="servizi.utenti.confermaTipoUtente"
							bundle="serviziLabels" />
					</html:submit></td>

					<td width="100px" align="right" class="etichetta">&nbsp;</td>
					<td width="150px" align="left">&nbsp;</td>
				</tr>

			</table>
			<br>
			<br>
			<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center"><html:submit property="methodDettaglio">
						<bean:message key="servizi.bottone.indietro"
							bundle="serviziLabels" />
					</html:submit></td>
				</tr>
			</table>
			</div>
		</c:when>
		<c:otherwise>
			<table width="100%" border="0">
				<tr>
					<td width="100px" class="etichetta" align="right"><strong>
					<bean:message key="servizi.utenti.codUtente" bundle="serviziLabels" />
					</strong></td>
					<td width="150px" align="left"><html:text
						styleId="testoNoBold" property="uteAna.codiceUtente" size="40"
						readonly="true"></html:text></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td width="100px" class="etichetta" align="right"><strong>
					<c:choose>
						<c:when test="${navForm.tipoUtente eq 'P'}">
							<bean:message key="servizi.utenti.cognome" bundle="serviziLabels" />
						</c:when>
						<c:otherwise>
							<bean:message key="servizi.utenti.denominazione"
								bundle="serviziLabels" />
						</c:otherwise>
					</c:choose> </strong></td>
					<c:choose>
						<c:when test="${navForm.conferma}">
							<td width="150px" align="left"><html:text
								styleId="testoNoBold" property="uteAna.cognome" size="80"
								maxlength="80" disabled="${navForm.conferma}"></html:text></td>
						</c:when>
						<c:otherwise>
							<td width="150px" align="left"><html:text
								styleId="testoNoBold" property="uteAna.cognome" size="80"
								maxlength="80" disabled="uteAna.nuovoUte"></html:text></td>
						</c:otherwise>
					</c:choose>

					<c:choose>
						<c:when test="${navForm.tipoUtente eq 'P'}">
							<td width="100px" class="etichetta" align="right"><strong>
							<bean:message key="servizi.utenti.nome" bundle="serviziLabels" />
							</strong></td>
							<c:choose>
								<c:when test="${navForm.conferma}">
									<td width="150px" align="left"><html:text
										styleId="testoNoBold" property="uteAna.nome" size="50"
										maxlength="50" disabled="${navForm.conferma}"></html:text></td>
								</c:when>
								<c:otherwise>
									<td width="150px" align="left"><html:text
										styleId="testoNoBold" property="uteAna.nome" size="50"
										maxlength="50" readonly="uteAna.nuovoUte"></html:text></td>
								</c:otherwise>
							</c:choose>
						</c:when>
					</c:choose>
				</tr>
			</table>
		</c:otherwise>
	</c:choose>
</sbn:disableAll>
