<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bs:define id="formName"
	name="org.apache.struts.action.mapping.instance" property="name"
	type="java.lang.String" />
<tr>
	<td colspan="2"><hr /></td>
</tr>
<tr>
	<td class="etichetta"><bean:message key="ordine.label.dataSpedizione"
			bundle="acquisizioniLabels" /></td>
	<td><html:text disabled="true" name="navForm"
			property="datiOrdine.ordineCarrelloSpedizione.data"
			size="10" /> &nbsp;&nbsp; <bean:message
			key="ordine.label.prgSpedizione" bundle="acquisizioniLabels" /> <html:text
			disabled="true" name="navForm"
			property="datiOrdine.ordineCarrelloSpedizione.prgSpedizione" size="3" />
		&nbsp;&nbsp; <bean:message key="ordine.label.cartName"
			bundle="acquisizioniLabels" /> <html:text disabled="true"
			name="navForm"
			property="datiOrdine.ordineCarrelloSpedizione.cartName" size="30" />
	</td>
</tr>
