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
<sbn:navform action="/amministrazionesistema/ricercaBiblioteca.do">
			<div id="divMessaggio">
					<sbn:errors bundle="amministrazioneSistemaMessages" />
			</div>

<!--table border="1" width="40%" frame="box" rules="none" bgcolor="#FFCA80" align="center" cellspacing="15"-->
<table border="0" width="100%" align="center" cellspacing="15">

	<tr>
		<td width="15%">
		</td>
		<td width="30%">
		</td>
		<td width="5%">
		</td>
		<td width="4%">
		</td>
		<td width="10%">
		</td>
		<td width="10%">
		</td>
		<td width="26%">
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.cdana" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td>
			<html:text property="codiceAnaRic" size="10" maxlength="6"></html:text>
		</td>
		<td colspan="2">
			<bean:message key="nuovo.biblioteca.cdbib" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="3">
			<html:text property="codicePoloRic" size="10" maxlength="3"></html:text>
			<html:text property="codiceBibRic" size="10" maxlength="2"></html:text>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.nome" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="6">
			<html:text property="nomeRic" size="81" maxlength="80"></html:text>
			<bean:message key="ricerca.biblioteca.inizio" bundle="amministrazioneSistemaLabels"/>
			<html:radio property="checkNome" value="inizio"></html:radio>
			<bean:message key="ricerca.biblioteca.esatta" bundle="amministrazioneSistemaLabels"/>
			<html:radio property="checkNome" value="esatta"></html:radio>
			<bean:message key="ricerca.biblioteca.parole" bundle="amministrazioneSistemaLabels"/>
			<html:radio property="checkNome" value="parole"></html:radio>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.indirizzo" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="6">
			<html:select property="selezioneDug">
				<html:optionsCollection property="elencoDug" value="codice" label="descrizione"/>
			</html:select>
			<html:text property="indirizzoRic" size="67" maxlength="80"></html:text>
			<bean:message key="ricerca.biblioteca.inizio" bundle="amministrazioneSistemaLabels"/>
			<html:radio property="checkIndirizzo" value="inizio"></html:radio>
			<bean:message key="ricerca.biblioteca.esatta" bundle="amministrazioneSistemaLabels"/>
			<html:radio property="checkIndirizzo" value="esatta"></html:radio>
			<bean:message key="ricerca.biblioteca.parole" bundle="amministrazioneSistemaLabels"/>
			<html:radio property="checkIndirizzo" value="parole"></html:radio>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.cap" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td>
			<html:text property="capRic" size="6" maxlength="5"></html:text>
			<bean:message key="ricerca.biblioteca.esatta" bundle="amministrazioneSistemaLabels"/>:
			<html:checkbox property="checkEsattaCap"></html:checkbox>
		</td>
		<td>
			<bean:message key="ricerca.biblioteca.localita" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="4">
			<html:text property="cittaRic" size="37" maxlength="30"></html:text>
			<bean:message key="ricerca.biblioteca.esatta" bundle="amministrazioneSistemaLabels"/>:
			<html:checkbox property="checkEsattaCitta"></html:checkbox>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.provincia" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="5">
			<html:select property="selezioneProvincia">
				<html:optionsCollection property="elencoProvince" value="codice" label="descrizione"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.tipo" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="2">
			<html:select property="selezioneTipoBib">
				<html:optionsCollection property="elencoTipiBib" value="codice" label="descrizione"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.sbn" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="2">
			<html:select property="checkBibInPolo">
				<html:optionsCollection property="elencoTipoBibSBN" value="codice" label="descrizione"/>
			</html:select>
		</td>
		<td>
			<bean:message key="ricerca.biblioteca.bibinpolo" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="2">
			<html:select property="selezioneFlagBib">
				<html:optionsCollection property="elencoFlagBib" value="codice" label="descrizione"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="ricerca.biblioteca.sistema.metro" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="2">
			<html:select property="codSistemaMetro" >
				<html:optionsCollection property="listaCodiciSistMetro" value="cd_tabella" label="ds_tabella"/>
			</html:select>
		</td>
		<td>
			<bean:message key="ricerca.bibliotecario.ordinamento" bundle="amministrazioneSistemaLabels"/>:
		</td>
		<td colspan="2">
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
						<html:submit styleClass="pulsanti" property="methodRicBiblioteca">
							<bean:message key="ricerca.biblioteca.button.cerca" bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</td>
					<c:choose>
						<c:when test="${navForm.acquisizioni eq 'FALSE' or navForm.scaricoInventariale eq 'FALSE'}">
							<c:choose>
								<c:when test="${navForm.abilitatoNuovo eq 'TRUE'}">
									<td>
										<html:submit styleClass="pulsanti" property="methodRicBiblioteca">
											<bean:message key="ricerca.biblioteca.button.nuovo" bundle="amministrazioneSistemaLabels" />
										</html:submit>
									</td>
								</c:when>
							</c:choose>
						</c:when>
					</c:choose>
					<sbn:checkAttivita idControllo="SERVIZI_ILL">
						<td>
							<html:submit styleClass="pulsanti" property="methodRicBiblioteca">
								<bean:message key="ricerca.biblioteca.button.nuovo" bundle="amministrazioneSistemaLabels" />
							</html:submit>
						</td>
					</sbn:checkAttivita>
					<td>
						<html:submit styleClass="buttonCleanCampi" property="methodRicBiblioteca" title="Pulisci campi">
							<bean:message key="ricerca.biblioteca.button.reset" bundle="amministrazioneSistemaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>

    </sbn:navform>
  </layout:page>