<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Autore
		almaviva2 - Inizio Codifica Agosto 2006
-->

<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>


<logic:equal name="org.apache.struts.taglib.html.BEAN" property="oggettoRiferimento.enabled" value="true">

	<table border="0">
		<tr>
			<td class="etichetta"><bean:message
				key="ricerca.titoloRiferimento" bundle="gestioneBibliograficaLabels" />
			:</td>
			<td width="20" class="testoNormale"><html:text
				property="oggettoRiferimento.id" size="10" readonly="true" /></td>
			<td width="150" class="etichetta"><html:text
				property="oggettoRiferimento.testo" size="50" readonly="true" /></td>
		</tr>
	</table>
	<hr color="#dde8f0" />

</logic:equal>