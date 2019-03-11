<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="formName"
	name="org.apache.struts.action.mapping.instance" property="name"
	type="java.lang.String" />
<hr />
<table>
<tr>
	<td class="etichetta"><bean:message
			key="ordine.label.dataSpedizione" bundle="acquisizioniLabels" /></td>
	<td><html:text name="navForm"
			property="parametri.ordineCarrelloSpedizione.data" size="10" />
		&nbsp;&nbsp; <bean:message key="ordine.label.prgSpedizione"
			bundle="acquisizioniLabels" /> <html:text name="navForm"
			property="parametri.ordineCarrelloSpedizione.prgSpedizione" size="3" />
	</td>
</tr>
<tr><td colspan="2"><br/></td></tr>
</table>