<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<br>
<table width="100%" border="0">
	<tr>
		<td class="etichetta" align="right">
			<bean:message key="servizi.utenti.cognomeNome" bundle="serviziLabels" />
		</td>
		<td>
			<html:text styleId="testoNoBold" property="ricerca.cognome" size="15" maxlength="80">
			</html:text>
			&nbsp;&nbsp;
			<html:text styleId="testoNoBold" property="ricerca.nome" size="15">
			</html:text>
		</td>
	<tr>
		<td class="etichetta" align="right">
			<bean:message key="servizi.utenti.dataNascita" bundle="serviziLabels" />
		</td>
		<td>
			<html:text styleId="testoNoBold" property="ricerca.dataNascita"
				size="12">
			</html:text>
		</td>
	</tr>
	<tr>
         <td class="etichetta" align="right">
             <bean:message key="servizi.utenti.luogoNascita" bundle="serviziLabels" />
         </td>
         <td class="etichetta" align="left">
             <html:text styleId="testoNoBold" property="ricerca.luogoNascita" size="25" disabled="${DettaglioUtentiAnaForm.conferma}"></html:text>
         </td>
	</tr>
	<tr>
		<td class="etichetta" align="right">
			<bean:message key="servizi.utenti.sesso" bundle="serviziLabels" />
		</td>
		<td align="left">
			<html:select property="ricerca.sesso"
				disabled="${DettaglioUtentiAnaForm.conferma}">
				<html:option value="M" bundle="serviziLabels"
					key="servizi.utenti.sessom"></html:option>
				<html:option value="F" bundle="serviziLabels"
					key="servizi.utenti.sessof"></html:option>
			</html:select>
		</td>
	</tr>
	<tr>
		<td width="24%" class="etichetta" align="right" class="style11">
			<strong> <bean:message key="servizi.utenti.codUtente"
					bundle="serviziLabels" /> </strong>
		</td>
		<td width="34%" align="left">
			<html:text styleId="testoNoBold" property="ricerca.codUte" size="5"
				disabled="false"></html:text>
		</td>
	</tr>
	<tr>
		<td class="etichetta" align="right">
			<bean:message key="servizi.utenti.codiceFiscale"
				bundle="serviziLabels" />
		</td>
		<td align="left">
			<html:text styleId="testoNoBold" property="ricerca.codFiscale"
				size="16"></html:text>
		</td>
	</tr>
	<tr>
		<td class="etichetta" align="right">
			<bean:message key="servizi.utenti.email" bundle="serviziLabels" />
		</td>
		<td>
			<html:text styleId="testoNoBold" property="ricerca.mail" size="50"></html:text>
		</td>
	</tr>
</table>
