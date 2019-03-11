<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>

	<!--div id="divForm"-->
	<sbn:navform action="/amministrazionesistema/nuovoBibliotecario.do">
		<div id="divMessaggio"><sbn:errors
			bundle="amministrazioneSistemaMessages" /></div>

		<!--table border="1" width="40%" frame="box" rules="none" bgcolor="#FFCA80" align="center" cellspacing="15"-->
		<sbn:disableAll disabled="${nuovoBibliotecarioForm.conferma}">
			<table border="0" width="100%" align="center" cellspacing="15">
				<tr>
					<td width="18%"></td>
					<td width="14%"></td>
					<td width="2%"></td>
					<td width="8%"></td>
					<td width="10%"></td>
					<td width="10%"></td>
					<td width="48%"></td>
				</tr>
				<tr>
					<td colspan="3" align="left"><b><bean:message
						key="nuovo.bibliotecario.titolobox"
						bundle="amministrazioneSistemaLabels" />:</b></td>
				</tr>

				<tr>
					<td width="15%"><bean:message
						key="ricerca.bibliotecario.cognome"
						bundle="amministrazioneSistemaLabels" /> <font color="#FF0000">*</font>
					:</td>
					<td colspan="6"><html:text property="cognome" size="80"
						maxlength="25"></html:text> <font style="font-size: 11px">
					(<bean:message key="nuovo.bibliotecario.limite25"
						bundle="amministrazioneSistemaLabels" />) </font></td>
				</tr>
				<tr>
					<td><bean:message key="ricerca.bibliotecario.nome"
						bundle="amministrazioneSistemaLabels" /> <font color="#FF0000">*</font>
					:</td>
					<td colspan="6"><html:text property="nome" size="80"
						maxlength="25"></html:text> <font style="font-size: 11px">
					(<bean:message key="nuovo.bibliotecario.limite25"
						bundle="amministrazioneSistemaLabels" />) </font></td>
				</tr>
				<tr>
					<td><bean:message key="nuovo.bibliotecario.ufficio"
						bundle="amministrazioneSistemaLabels" />:</td>
					<td colspan="6"><html:text property="ufficio" size="80"
						maxlength="50"></html:text> <font style="font-size: 11px">
					(<bean:message key="nuovo.bibliotecario.limite50"
						bundle="amministrazioneSistemaLabels" />) </font></td>
				</tr>
				<c:choose>
					<c:when test="${nuovoBibliotecarioForm.nuovo}">
						<tr>
							<td><bean:message key="ricerca.bibliotecario.biblioteca"
								bundle="amministrazioneSistemaLabels" /> <font color="#FF0000">*</font>
							:</td>
							<td colspan="5"><html:select property="selezioneBiblio">
								<html:optionsCollection property="elencoBiblio" value="codice"
									label="descrizione" />
							</html:select></td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<td><bean:message key="ricerca.bibliotecario.biblioteca"
								bundle="amministrazioneSistemaLabels" /> <font color="#FF0000">*</font>
							:</td>
							<td colspan="5"><html:select property="selezioneBiblio"
								disabled="true">
								<html:optionsCollection property="elencoBiblio" value="codice"
									label="descrizione" />
							</html:select></td>
						</tr>
					</c:otherwise>
				</c:choose>
				<tr>
					<td><bean:message key="nuovo.bibliotecario.ruolo"
						bundle="amministrazioneSistemaLabels" /> <font color="#FF0000">*</font>
					:</td>
					<td><html:select property="selezioneRuolo">
						<html:optionsCollection property="elencoRuoli" value="codice"
							label="descrizione" />
					</html:select></td>
				</tr>
				<tr>
					<td><bean:message key="nuovo.bibliotecario.note"
						bundle="amministrazioneSistemaLabels" />:</td>
					<td colspan="6">
						<html:text property="note" size="80" maxlength="160" /><font style="font-size: 11px"> (<bean:message
						key="nuovo.bibliotecario.limite160"
						bundle="amministrazioneSistemaLabels" />) </font></td>
				</tr>
			</table>

			<hr>

			<table border="0" width="100%" align="center" cellspacing="15">
				<tr>
					<td width="18%"></td>
					<td width="24%"></td>
					<td width="10%"></td>
					<td width="3%"></td>
					<td width="12%"></td>
					<td width="33%"></td>
				</tr>
				<tr>
					<td colspan="2" align="left"><b><bean:message
						key="nuovo.bibliotecario.titolobox.abilitazione"
						bundle="amministrazioneSistemaLabels" />:</b></td>
				</tr>
				<tr>
					<td><bean:message key="ricerca.bibliotecario.username"
						bundle="amministrazioneSistemaLabels" />:</td>
					<td><html:text property="username" size="22" maxlength="6"></html:text>
					<font style="font-size: 11px"> (<bean:message
						key="nuovo.bibliotecario.limite6"
						bundle="amministrazioneSistemaLabels" />) </font></td>
					<td><bean:message key="nuovo.bibliotecario.scaduto"
						bundle="amministrazioneSistemaLabels" />:</td>
					<c:choose>
						<c:when test="${nuovoBibliotecarioForm.scaduto}">
							<td><b><bean:message
								key="nuovo.bibliotecario.scaduto.si"
								bundle="amministrazioneSistemaLabels" /></b></td>
						</c:when>
						<c:otherwise>
							<td><b><bean:message
								key="nuovo.bibliotecario.scaduto.no"
								bundle="amministrazioneSistemaLabels" /></b></td>
						</c:otherwise>
					</c:choose>
				</tr>
				<tr>
					<td><bean:message key="nuovo.bibliotecario.data.variazione"
						bundle="amministrazioneSistemaLabels" />:</td>
					<td><b><c:out
						value="${nuovoBibliotecarioForm.dataVariazione}"></c:out></b></td>
					<td colspan="2"><bean:message
						key="nuovo.bibliotecario.data.accesso"
						bundle="amministrazioneSistemaLabels" />:</td>
					<td colspan="2"><b><c:out
						value="${nuovoBibliotecarioForm.dataAccesso}"></c:out></b></td>
				</tr>
			</table>
			</sbn:disableAll>
			<hr>

			<table border="0" width="100%" align="center" cellspacing="15">
				<tr>
					<td colspan="3" style="font-size: 11px"><i><bean:message
						key="nuovo.bibliotecario.obbligo"
						bundle="amministrazioneSistemaLabels" /></i>.</td>
				</tr>
				<tr>
					<td colspan="3" style="font-size: 11px"><i><bean:message
						key="nuovo.bibliotecario.messaggio"
						bundle="amministrazioneSistemaLabels" /></i>.</td>
				</tr>
			</table>

			<div id="divFooter">
			<table border="0" style="height: 40px" align="center">
				<tr>
					<c:choose>
						<c:when test="${nuovoBibliotecarioForm.conferma}">
							<td><html:submit styleClass="pulsanti"
								property="methodNewBibliotecario">
								<bean:message key="nuovo.bibliotecario.conferma.si"
									bundle="amministrazioneSistemaLabels" />
							</html:submit></td>
							<td><html:submit styleClass="pulsanti"
								property="methodNewBibliotecario">
								<bean:message key="nuovo.bibliotecario.conferma.no"
									bundle="amministrazioneSistemaLabels" />
							</html:submit></td>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when
									test="${nuovoBibliotecarioForm.abilitatoNuovo}">
									<td><html:submit styleClass="pulsanti"
										property="methodNewBibliotecario">
										<bean:message key="nuovo.bibliotecario.salva"
											bundle="amministrazioneSistemaLabels" />
									</html:submit></td>
									<sbn:checkAttivita idControllo="PASSWORD">
										<td><html:submit property="methodNewBibliotecario">
											<bean:message key="servizi.bottone.resetPwd"
												bundle="serviziLabels" />
										</html:submit></td>
									</sbn:checkAttivita>
								</c:when>
							</c:choose>
							<c:if test="${nuovoBibliotecarioForm.abilitatoProfilo}">
								<td><html:submit styleClass="pulsanti"
									property="methodNewBibliotecario" disabled="false">
									<bean:message key="nuovo.bibliotecario.abilitazioni" bundle="amministrazioneSistemaLabels" />
									</html:submit>
								</td>
							</c:if>
							<td><html:submit styleClass="pulsanti"
								property="methodNewBibliotecario">
								<bean:message key="nuovo.bibliotecario.annulla"
									bundle="amministrazioneSistemaLabels" />
							</html:submit></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</table>
			</div>
	</sbn:navform>
</layout:page>