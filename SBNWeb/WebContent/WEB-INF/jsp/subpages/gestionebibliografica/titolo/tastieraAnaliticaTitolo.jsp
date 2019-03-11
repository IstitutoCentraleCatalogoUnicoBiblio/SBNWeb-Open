<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area inferiore con i criteri di ricerca
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


<table align="center">
	<tr>
		<td align="center"><html:submit property="methodAnaTitTastiera">
			<bean:message key="anaTit.dettaglio"
				bundle="gestioneBibliograficaLabels" />
		</html:submit></td>
		<td align="center"><html:submit property="methodAnaTitTastiera">
			<bean:message key="anaTit.crea"
				bundle="gestioneBibliograficaLabels" />
		</html:submit></td>
		<td align="center"><html:submit property="methodAnaTitTastiera">
			<bean:message key="anaTit.vaiA"
				bundle="gestioneBibliograficaLabels" />
		</html:submit></td>
		<td class="etichetta"><html:select property="esaminaTitSelez">
			<html:optionsCollection property="listaEsaminaTit"
				value="descrizione" label="descrizione" />
		</html:select></td>
		<td align="center"><html:submit property="methodAnaTitTastiera">
			<bean:message key="anaTit.esamina"
				bundle="gestioneBibliograficaLabels" />
		</html:submit></td>
	</tr>
</table>

