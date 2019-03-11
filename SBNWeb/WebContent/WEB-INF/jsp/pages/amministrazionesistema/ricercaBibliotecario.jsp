<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
<div>
		<div id="divForm">
<sbn:navform action="/amministrazionesistema/ricercaBibliotecario.do">
			<div id="divMessaggio">
					<sbn:errors bundle="amministrazioneSistemaMessages" />
			</div>

<!--table border="1" width="40%" frame="box" rules="none" bgcolor="#FFCA80" align="center" cellspacing="15"-->
<table border="0" width="100%" align="center" cellspacing="15">

	<tr>
		<td width="15%">
		</td>
		<td width="20%">
		</td>
		<td width="8%">
		</td>
		<td width="12%">
		</td>
		<td width="9%">
		</td>
		<td width="10%">
		</td>
		<td width="20%">
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.bibliotecario.cognome" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="6">
			<html:text property="cognomeRic" size="80" maxlength="25"></html:text>
			<bean:message key="ricerca.biblioteca.esatta" bundle="amministrazioneSistemaLabels"/>:
			<html:checkbox property="checkEsattaCognome"></html:checkbox>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.bibliotecario.nome" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="6">
			<html:text property="nomeRic" size="80" maxlength="25"></html:text>
			<bean:message key="ricerca.biblioteca.esatta" bundle="amministrazioneSistemaLabels"/>:
			<html:checkbox property="checkEsattaNome"></html:checkbox>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.bibliotecario.username" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="2">
			<html:text property="usernameRic" size="22" maxlength="6"></html:text>
			<bean:message key="ricerca.biblioteca.esatta" bundle="amministrazioneSistemaLabels"/>:
			<html:checkbox property="checkEsattaUsername"></html:checkbox>
		</td>
		<td>
			<bean:message key="ricerca.bibliotecario.dataaccesso" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="3">
			<html:text property="dataAccesso" size="15" maxlength="10"></html:text>
			<font align="center" style="font-size: 11px">
				<i><bean:message key="ricerca.bibliotecario.formatodata" bundle="amministrazioneSistemaLabels"/></i>
			</font>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.bibliotecario.biblioteca" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="5">
			<html:select property="selezioneBibRic">
				<html:optionsCollection property="elencoBib" value="codice" label="descrizione"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.bibliotecario.abilitato" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<c:choose>
			<c:when test="${ricercaBibliotecarioForm.acquisizioni eq 'FALSE' || ricercaBibliotecarioForm.stampaEtichette eq 'FALSE'}">
				<td colspan="2">
					<bean:message key="ricerca.bibliotecario.abilitato.tutti" bundle="amministrazioneSistemaLabels"/>
					<html:radio property="checkAbilitato" value="tutti"></html:radio>
					<bean:message key="ricerca.bibliotecario.abilitato.si" bundle="amministrazioneSistemaLabels"/>
					<html:radio property="checkAbilitato" value="true"></html:radio>
					<bean:message key="ricerca.bibliotecario.abilitato.no" bundle="amministrazioneSistemaLabels"/>
					<html:radio property="checkAbilitato" value="false"></html:radio>
				</td>
			</c:when>
			<c:otherwise>
				<td colspan="2">
					<bean:message key="ricerca.bibliotecario.abilitato.tutti" bundle="amministrazioneSistemaLabels"/>
					<html:radio property="checkAbilitato" value="tutti" disabled="true"></html:radio>
					<bean:message key="ricerca.bibliotecario.abilitato.si" bundle="amministrazioneSistemaLabels"/>
					<html:radio property="checkAbilitato" value="true" disabled="true"></html:radio>
					<bean:message key="ricerca.bibliotecario.abilitato.no" bundle="amministrazioneSistemaLabels"/>
					<html:radio property="checkAbilitato" value="false" disabled="true"></html:radio>
				</td>
			</c:otherwise>
		</c:choose>
		<td>
			<bean:message key="ricerca.bibliotecario.ordinamento" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td>
			<html:select property="selezioneOrdinamento">
				<html:optionsCollection property="elencoOrdinamenti" value="codice" label="descrizione"/>
			</html:select>
		</td>
	</tr>
</table>

<br/>
		<div id="divFooter">
			<table border="0" style="height:40px" align="center">
				<tr>
					<td>
						<html:submit styleClass="pulsanti" property="methodRicBibliotecario">
							<bean:message key="ricerca.bibliotecario.button.cerca" bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</td>
					<c:choose>
						<c:when test="${ricercaBibliotecarioForm.acquisizioni eq 'FALSE' || ricercaBibliotecarioForm.stampaEtichette eq 'FALSE'}">
							<c:choose>
								<c:when test="${ricercaBibliotecarioForm.abilitatoNuovo eq 'TRUE'}">
									<td>
										<html:submit styleClass="pulsanti" property="methodRicBibliotecario">
											<bean:message key="ricerca.bibliotecario.button.nuovo" bundle="amministrazioneSistemaLabels" />
										</html:submit>
									</td>
								</c:when>
							</c:choose>
						</c:when>
					</c:choose>
					<td>
						<html:submit styleClass="buttonCleanCampi" property="methodRicBibliotecario" title="Pulisci campi">
							<bean:message key="ricerca.biblioteca.button.reset" bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>

    </sbn:navform>
  </layout:page>