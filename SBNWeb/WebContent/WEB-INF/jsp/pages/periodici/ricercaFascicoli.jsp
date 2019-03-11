<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/periodici/ricercaFascicoli.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<!--  biblioteca -->
		<br />
		<jsp:include page="/WEB-INF/jsp/subpages/periodici/intestazionePeriodico.jsp" flush="true" />
		<br />
		<table>
			<tr>
				<td><bean:message key="periodici.esame.range.fascicoli"
					bundle="periodiciLabels" />&#58;</td>
				<td><bean:message key="periodici.kardex.posiziona.da"
					bundle="periodiciLabels" />&nbsp; <html:text
					property="ricercaKardex.dataFrom" size="10" maxlength="10" /></td>
				<td><bean:message key="periodici.kardex.posiziona.a"
					bundle="periodiciLabels" />&nbsp; <html:text
					property="ricercaKardex.dataTo" size="10" maxlength="10" />
			</tr>
		</table>
		<br />
		<div id="divFooterCommon">
		<table>
			<tr>
				<td class="etichetta"><bean:message
					key="gestionesemantica.soggetto.elementoPerBlocco"
					bundle="gestioneSemanticaLabels" /> <html:text
					styleId="testoNormale"
					property="ricercaKardex.numeroElementiBlocco" size="4"
					maxlength="4" /></td>
			</tr>
		</table>
		</div>

		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td><sbn:bottoniera buttons="pulsanti" /></td>
			</tr>
		</table>
		</div>

	</sbn:navform>

</layout:page>


