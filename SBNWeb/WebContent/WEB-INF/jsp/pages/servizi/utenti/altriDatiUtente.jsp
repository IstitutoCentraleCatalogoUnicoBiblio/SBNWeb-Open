<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/utenti/AltriDatiUtente.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>
			<jsp:include
				page="/WEB-INF/jsp/subpages/servizi/utenti/datiUtente.jsp" />
			<jsp:include
				page="/WEB-INF/jsp/subpages/servizi/utenti/utentiDoc.jsp" />
			<jsp:include
				page="/WEB-INF/jsp/subpages/servizi/utenti/utentiPro.jsp" />
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<!-- BOTTONIERA SOLO td -->
					<td align="center">
						<html:submit property="methodAltriDati">
							<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
						</html:submit>
						<html:submit property="methodAltriDati">
							<bean:message key="servizi.bottone.annulla"
								bundle="serviziLabels" />
						</html:submit>
					</td>
					<!-- BOTTONIERA SOLO td -->
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
