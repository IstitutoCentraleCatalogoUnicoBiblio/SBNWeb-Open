<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<table width="100%" align="center">
	<table width="100%" align="left" border="0" class="etichetta">
		<tr>
		<jsp:include
			page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" /></div>
		</tr>
		<tr>
			<td>&nbsp;&nbsp;<bean:message
				key="regingresso.label.dataDa" bundle="gestioneStampeLabels" />&nbsp;<html:text
				styleId="testoNormale" property="regingroDataDa" size="10"
				maxlength="10"></html:text></td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message
				key="regingresso.label.dataA" bundle="gestioneStampeLabels" />&nbsp;&nbsp;<html:text
				styleId="testoNormale" property="regingroDataA" size="10"
				maxlength="10"></html:text></td>
			<td>&nbsp;&nbsp;&nbsp;<bean:message
				key="regingresso.label.codTipoOrdine" bundle="gestioneStampeLabels" />&nbsp;&nbsp;</td>
			<td  colspan="2"><html:select
				styleClass="testoNormale" property="codTipoOrdine">&nbsp;&nbsp;
				<html:optionsCollection property="listaCodTipoOrdine" value="codice"
					label="descrizioneCodice" />
			</html:select></td>
		</tr>
	</table>

</table>
